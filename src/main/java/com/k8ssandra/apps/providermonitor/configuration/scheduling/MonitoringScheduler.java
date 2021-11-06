package com.k8ssandra.apps.providermonitor.configuration.scheduling;

import com.google.common.collect.Lists;
import com.k8ssandra.apps.providermonitor.configuration.ApplicationConfiguration;
import com.k8ssandra.apps.providermonitor.configuration.gcp.GcpMonitoringConfiguration;
import com.k8ssandra.apps.providermonitor.jobs.MonitoringJobBundle;
import com.k8ssandra.apps.providermonitor.jobs.gcp.GcpKubernetesMonitoringJob;
import com.k8ssandra.apps.providermonitor.jobs.gcp.GcpStorageMonitoringJob;
import com.k8ssandra.apps.providermonitor.utils.ProviderConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
@Slf4j
public class MonitoringScheduler {
  private final ApplicationConfiguration applicationConfiguration;
  private final SchedulerFactoryBean schedulerFactoryBean;

  @Autowired
  public MonitoringScheduler(ApplicationConfiguration applicationConfiguration, SchedulerFactoryBean schedulerFactoryBean) {
    this.applicationConfiguration = applicationConfiguration;
    this.schedulerFactoryBean = schedulerFactoryBean;

    initializeConfiguredSchedules();
  }

  private void initializeConfiguredSchedules() {
    if (applicationConfiguration == null) {
      log.warn("Unable to initialize monitoring jobs from configuration, the configuration provided is invalid.");
      return;
    }

    if (applicationConfiguration.getGcp() != null && (applicationConfiguration.getGcp().getEnabled() == null || applicationConfiguration.getGcp().getEnabled().booleanValue())) {
      List<MonitoringJobBundle> gcpJobBundles = buildGCPMonitoringJobBundles();
      if (gcpJobBundles != null && !gcpJobBundles.isEmpty()) {
        gcpJobBundles.stream()
            .forEach(bundle -> {
              try {
                schedulerFactoryBean.getScheduler().scheduleJob(bundle.getJobDetail(), bundle.getTrigger());
                log.info("Scheduled job '{}/{}'.", bundle.getJobDetail().getKey().getGroup(), bundle.getJobDetail().getKey().getName());
              } catch (SchedulerException e) {
                log.error("Unable to schedule job '{}/{}'.", bundle.getJobDetail().getKey().getGroup(), bundle.getJobDetail().getKey().getName());
              }
            });
      }
    }
  }

  private List<MonitoringJobBundle> buildGCPMonitoringJobBundles() {
    GcpMonitoringConfiguration configuration = applicationConfiguration.getGcp();
    if (configuration == null) {
      return Lists.newArrayList();
    }

    List<MonitoringJobBundle> jobBundles = Lists.newArrayList();

    // Storage
    if (configuration.getStorage() != null && (configuration.getStorage().getEnabled() == null || configuration.getStorage().getEnabled().booleanValue())) {
      String jobId = ProviderConstants.PROVIDER_GCP + "-" + ProviderConstants.SERVICE_STORAGE + "-monitoring";
      String jobDescription = "Collect " + ProviderConstants.SERVICE_STORAGE + " data from " + ProviderConstants.PROVIDER_GCP;
      String jobGroup = ProviderConstants.PROVIDER_GCP;

      JobDetail jobDetail = JobBuilder.newJob(GcpStorageMonitoringJob.class).withIdentity(jobId, jobGroup).withDescription(jobDescription).build();

      Trigger trigger = newTrigger().
          withIdentity(jobId, jobGroup).
          withSchedule(
              simpleSchedule().
                  withIntervalInMinutes(configuration.getStorage().getFrequencyM() != null && configuration.getStorage().getFrequencyM() > 0L ? configuration.getStorage().getFrequencyM() : 1).
                  repeatForever()
          ).build();

      jobBundles.add(new MonitoringJobBundle(ProviderConstants.PROVIDER_GCP, ProviderConstants.SERVICE_STORAGE, trigger, jobDetail));
    }

    // Compute
    if (configuration.getCompute() != null && (configuration.getCompute().getEnabled() == null || configuration.getCompute().getEnabled().booleanValue())) {

    }

    // Kubernetes
    if (configuration.getKubernetes() != null && (configuration.getKubernetes().getEnabled() == null || configuration.getKubernetes().getEnabled().booleanValue())) {
      String jobId = ProviderConstants.PROVIDER_GCP + "-" + ProviderConstants.SERVICE_KUBERNETES + "-monitoring";
      String jobDescription = "Collect " + ProviderConstants.SERVICE_KUBERNETES + " data from " + ProviderConstants.PROVIDER_GCP;
      String jobGroup = ProviderConstants.PROVIDER_GCP;

      JobDetail jobDetail = JobBuilder.newJob(GcpKubernetesMonitoringJob.class).withIdentity(jobId, jobGroup).withDescription(jobDescription).build();

      Trigger trigger = newTrigger().
          withIdentity(jobId, jobGroup).
          withSchedule(
              simpleSchedule().
                  withIntervalInMinutes(configuration.getKubernetes().getFrequencyM() != null && configuration.getKubernetes().getFrequencyM() > 0L ? configuration.getKubernetes().getFrequencyM() : 1).
                  repeatForever()
          ).build();

      jobBundles.add(new MonitoringJobBundle(ProviderConstants.PROVIDER_GCP, ProviderConstants.SERVICE_STORAGE, trigger, jobDetail));
    }

    return jobBundles;
  }
}
