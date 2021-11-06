package com.k8ssandra.apps.providermonitor.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderMonitoringConfiguration {
  private Boolean enabled;
  private ServiceMonitoringConfiguration storage;
  private ServiceMonitoringConfiguration compute;
  private ServiceMonitoringConfiguration kubernetes;
}
