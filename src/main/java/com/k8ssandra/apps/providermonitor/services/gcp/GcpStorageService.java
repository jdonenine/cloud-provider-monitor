package com.k8ssandra.apps.providermonitor.services.gcp;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.k8ssandra.apps.providermonitor.configuration.ApplicationConfiguration;
import com.k8ssandra.apps.providermonitor.configuration.gcp.GcpMonitoringConfiguration;
import com.k8ssandra.apps.providermonitor.configuration.gcp.GcpCredentials;
import com.k8ssandra.apps.providermonitor.exceptions.GcpStorageInitializationException;
import com.k8ssandra.apps.providermonitor.models.StorageBucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class GcpStorageService {
  private Storage storageService;
  private final String projectId;

  @Autowired
  public GcpStorageService(GcpCredentials gcpCredentials, ApplicationConfiguration applicationConfiguration) {
    projectId = applicationConfiguration != null && applicationConfiguration.getGcp() != null ? applicationConfiguration.getGcp().getProjectId() : null;
    try {
      storageService = StorageOptions
          .newBuilder()
          .setCredentials(gcpCredentials.getCredentials())
          .setProjectId(projectId)
          .build()
          .getService();
      log.info("GCP Storage interface has been initialized.");
    } catch (Exception e) {
      log.error("Unable to initialize GCP Storage interface, storage data will not be available.", e);
    }
  }

  public List<StorageBucket> getAllBuckets() throws GcpStorageInitializationException, StorageException {
    if (storageService == null) {
      throw new GcpStorageInitializationException();
    }
    Page<Bucket> page = storageService.list();
    return StreamSupport.stream(page.iterateAll().spliterator(), false)
        .filter(bucket -> bucket != null)
        .map(bucket -> StorageBucket.fromGcpBucket(bucket))
        .collect(Collectors.toList());
  }
}
