package com.k8ssandra.apps.providermonitor.configuration.gcp;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.k8ssandra.apps.providermonitor.configuration.ApplicationConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
@Slf4j
@Data
public class GcpCredentials {
  private Credentials credentials;

  @Autowired
  public GcpCredentials(ApplicationConfiguration applicationConfiguration) throws IOException {
    if (applicationConfiguration == null) {
      throw new IllegalArgumentException("ApplicationConfiguration is invalid.");
    }
    // If GCP is disabled, just eject
    if (applicationConfiguration.getGcp() == null || (applicationConfiguration.getGcp().getEnabled() != null && !applicationConfiguration.getGcp().getEnabled().booleanValue())) {
      return;
    }

    // Check for basic existence of the credentials path
    if (applicationConfiguration.getGcp().getCredentialsPath() == null || applicationConfiguration.getGcp().getCredentialsPath().isEmpty()) {
      throw new IllegalArgumentException("ApplicationConfiguration must contain a valid application.gcp.credentials-path value");
    }
    log.info("Loading GoogleCredentials from file `{}`", applicationConfiguration.getGcp().getCredentialsPath());
    credentials = GoogleCredentials
        .fromStream(new FileInputStream(applicationConfiguration.getGcp().getCredentialsPath()))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
    log.info("Completed loading GoogleCredentials from file.");
  }
}
