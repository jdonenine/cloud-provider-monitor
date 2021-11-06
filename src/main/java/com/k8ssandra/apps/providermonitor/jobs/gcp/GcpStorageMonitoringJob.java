package com.k8ssandra.apps.providermonitor.jobs.gcp;

import com.google.cloud.storage.StorageException;
import com.k8ssandra.apps.providermonitor.exceptions.GcpStorageInitializationException;
import com.k8ssandra.apps.providermonitor.models.StorageBucket;
import com.k8ssandra.apps.providermonitor.services.gcp.GcpStorageService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@NoArgsConstructor
public class GcpStorageMonitoringJob implements Job {
  @Autowired
  private GcpStorageService gcpStorageService;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("Starting GCPStorageMonitoringJob");
    List<StorageBucket> buckets = null;
    try {
      buckets = gcpStorageService.getAllBuckets();
    } catch (GcpStorageInitializationException | StorageException e) {
      log.error("Unable to retrieve GCP Storage Buckets.", e);
      throw new JobExecutionException(e);
    }
    if (buckets != null) {
      log.info("Observed {} GCP Storage Buckets.", buckets.size());
    } else {
      log.warn("Unable to retrieve GCP Storage Bucket data");
    }
    log.info("Completed GCPStorageMonitoringJob");
  }
}
