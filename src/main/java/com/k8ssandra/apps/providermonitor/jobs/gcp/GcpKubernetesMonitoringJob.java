package com.k8ssandra.apps.providermonitor.jobs.gcp;

import com.k8ssandra.apps.providermonitor.exceptions.GcpKubernetesServiceInitializationException;
import com.k8ssandra.apps.providermonitor.models.KubernetesCluster;
import com.k8ssandra.apps.providermonitor.services.gcp.GcpKubernetesService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@NoArgsConstructor
public class GcpKubernetesMonitoringJob implements Job {
  @Autowired
  private GcpKubernetesService gcpKubernetesService;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("Starting GcpKubernetesMonitoringJob");
    List<KubernetesCluster> clusters = null;
    try {
      clusters = gcpKubernetesService.getAllClusters();
    } catch (GcpKubernetesServiceInitializationException e) {
      log.error("Unable to retrieve GCP Kubernetes Clusters.", e);
      throw new JobExecutionException(e);
    }
    if (clusters != null) {
      log.info("Observed {} GCP Kubernetes Clusters.", clusters.size());
    } else {
      log.warn("Unable to retrieve GCP Kubernetes Cluster data");
    }
    log.info("Completed GcpKubernetesMonitoringJob");
  }
}