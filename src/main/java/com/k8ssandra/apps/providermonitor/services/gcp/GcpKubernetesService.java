package com.k8ssandra.apps.providermonitor.services.gcp;

import com.google.api.client.util.Lists;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.container.v1.ClusterManagerClient;
import com.google.cloud.container.v1.ClusterManagerSettings;
import com.google.container.v1.Cluster;
import com.google.container.v1.ListClustersRequest;
import com.google.container.v1.ListClustersResponse;
import com.k8ssandra.apps.providermonitor.configuration.ApplicationConfiguration;
import com.k8ssandra.apps.providermonitor.configuration.gcp.GcpMonitoringConfiguration;
import com.k8ssandra.apps.providermonitor.configuration.gcp.GcpCredentials;
import com.k8ssandra.apps.providermonitor.exceptions.GcpKubernetesServiceInitializationException;
import com.k8ssandra.apps.providermonitor.models.KubernetesCluster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GcpKubernetesService {
  private ClusterManagerClient clusterService;
  private String projectId;

  @Autowired
  public GcpKubernetesService(GcpCredentials gcpCredentials, ApplicationConfiguration applicationConfiguration) {
    projectId = applicationConfiguration != null && applicationConfiguration.getGcp() != null ? applicationConfiguration.getGcp().getProjectId() : null;

    try {
      ClusterManagerSettings clusterManagerSettings =
          ClusterManagerSettings.newBuilder()
              .setCredentialsProvider(FixedCredentialsProvider.create(gcpCredentials.getCredentials()))
              .build();
      clusterService = ClusterManagerClient.create(clusterManagerSettings);
      log.info("GCP ClusterManagerClient interface has been initialized.");
    } catch (Exception e) {
      log.error("Unable to initialize GCP ClusterManagerClient interface, Kubernetes cluster data will not be available.", e);
    }
  }

  public List<KubernetesCluster> getAllClusters() throws GcpKubernetesServiceInitializationException {
    if (clusterService == null) {
      throw new GcpKubernetesServiceInitializationException();
    }

    ListClustersResponse response = clusterService.listClusters(
        ListClustersRequest.newBuilder().
            setParent("projects/" + projectId + "/locations/-")
            .build()
    );
    if (response == null || response.getClustersList() == null) {
      return Lists.newArrayList();
    }

    return response.getClustersList().stream()
        .filter(cluster -> cluster != null)
        .map(cluster -> KubernetesCluster.fromGcpCluster(cluster, projectId))
        .collect(Collectors.toList());
  }
}
