package com.k8ssandra.apps.providermonitor.models;

import com.google.container.v1.Cluster;
import com.k8ssandra.apps.providermonitor.utils.ProviderConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KubernetesCluster extends ServiceResource{
  private Long timeExpire;
  private String network;
  private String subnetwork;
  private String ipv4Cidr;
  private String serviceIpv4Cidr;
  private List<String> locations;
  private String kubernetesVersion;
  private List<NodePool> nodePools;
  private Integer numNodePools;

  public static KubernetesCluster fromGcpCluster(Cluster cluster, String projectId) {
    if (cluster == null) {
      throw new IllegalArgumentException("Cluster provided must not be null");
    }

    KubernetesCluster kc = new KubernetesCluster();
    kc.setProvider(ProviderConstants.PROVIDER_GCP);
    kc.setProjectId(projectId);
    kc.setId(cluster.getName());
    kc.setName(cluster.getName());
    kc.setLabels(cluster.getResourceLabelsMap());
    kc.setLocation(cluster.getLocation());
    kc.setTimeCreated(cluster.getCreateTime() != null && !cluster.getCreateTime().isEmpty() ? ZonedDateTime.parse(cluster.getCreateTime()).toEpochSecond() * 1000L : null);
    kc.setTimeExpire(cluster.getExpireTime() != null && !cluster.getExpireTime().isEmpty() ? ZonedDateTime.parse(cluster.getExpireTime()).toEpochSecond() * 1000L : null);
    kc.setKubernetesVersion(cluster.getCurrentMasterVersion());
    kc.setIpv4Cidr(cluster.getClusterIpv4Cidr());
    kc.setServiceIpv4Cidr(cluster.getServicesIpv4Cidr());
    kc.setNetwork(cluster.getNetwork());
    kc.setSubnetwork(cluster.getSubnetwork());
    kc.setLocations(cluster.getLocationsList());
    kc.setNumNodePools(cluster.getNodePoolsCount());
    if (cluster.getNodePoolsList() != null) {
      kc.setNodePools(cluster.getNodePoolsList().stream()
          .filter(nodePool -> nodePool != null)
          .map(nodePool -> NodePool.fromGcpNodePool(nodePool, projectId))
          .collect(Collectors.toList())
      );
    }
    return kc;
  }
}
