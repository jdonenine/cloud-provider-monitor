package com.k8ssandra.apps.providermonitor.exceptions;

public class ProviderServiceInitializationException extends Throwable {
  private String provider;
  private String service;

  public ProviderServiceInitializationException(String provider, String service) {
    super(buildMessage(provider, service));
    this.provider = provider;
    this.service = service;
  }

  private static final String buildMessage(String provider, String service) {
    StringBuilder sb = new StringBuilder();
    sb.append("Provider service request could not be completed: ");
    sb.append(provider != null ? provider : "?");
    sb.append("/");
    sb.append(service != null ? service : "?");
    sb.append(" is not available.");
    return sb.toString();
  }
}
