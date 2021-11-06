package com.k8ssandra.apps.providermonitor.exceptions;

import static com.k8ssandra.apps.providermonitor.utils.ProviderConstants.PROVIDER_GCP;
import static com.k8ssandra.apps.providermonitor.utils.ProviderConstants.SERVICE_KUBERNETES;

public class GcpKubernetesServiceInitializationException extends ProviderServiceInitializationException {
  public GcpKubernetesServiceInitializationException() {
    super(PROVIDER_GCP, SERVICE_KUBERNETES);
  }
}
