package com.k8ssandra.apps.providermonitor.configuration.gcp;

import com.k8ssandra.apps.providermonitor.configuration.ProviderMonitoringConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@EqualsAndHashCode(callSuper = true)
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcpMonitoringConfiguration extends ProviderMonitoringConfiguration {
  private Boolean enabled;
  private String credentialsPath;
  private String projectId;
}
