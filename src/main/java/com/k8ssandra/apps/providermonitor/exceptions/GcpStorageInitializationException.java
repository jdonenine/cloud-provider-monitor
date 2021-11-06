package com.k8ssandra.apps.providermonitor.exceptions;

import static com.k8ssandra.apps.providermonitor.utils.ProviderConstants.PROVIDER_GCP;
import static com.k8ssandra.apps.providermonitor.utils.ProviderConstants.SERVICE_STORAGE;

public class GcpStorageInitializationException extends ProviderServiceInitializationException {
  public GcpStorageInitializationException() {
    super(PROVIDER_GCP, SERVICE_STORAGE);
  }
}
