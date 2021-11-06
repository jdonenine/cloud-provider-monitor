package com.k8ssandra.apps.providermonitor.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMonitoringConfiguration {
  private Boolean enabled = true;
  private Integer frequencyM = 1;
}
