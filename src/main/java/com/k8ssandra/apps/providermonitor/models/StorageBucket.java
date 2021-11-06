package com.k8ssandra.apps.providermonitor.models;

import com.google.cloud.storage.Bucket;
import com.k8ssandra.apps.providermonitor.utils.ProviderConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StorageBucket extends ServiceResource {
  private String storageClass;

  public static StorageBucket fromGcpBucket(Bucket bucket) {
    if (bucket == null) {
      throw new IllegalArgumentException("Bucket provided must not be null");
    }
    StorageBucket sb = new StorageBucket();
    sb.setProvider(ProviderConstants.PROVIDER_GCP);
    sb.setId(bucket.getGeneratedId());
    sb.setName(bucket.getName());
    sb.setLocation(bucket.getLocation());
    sb.setLocationType(bucket.getLocationType());
    sb.setProjectId(bucket.getStorage() != null && bucket.getStorage().getOptions() != null ? bucket.getStorage().getOptions().getProjectId() : null);
    sb.setTimeCreated(bucket.getCreateTime());
    sb.setTimeUpdated(bucket.getUpdateTime());
    sb.setStorageClass(bucket.getStorageClass() != null ? bucket.getStorageClass().name() : null);
    sb.setLabels(bucket.getLabels());
    return sb;
  }
}
