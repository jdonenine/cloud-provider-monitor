package com.k8ssandra.apps.providermonitor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodePool extends ServiceResource {
  private Integer initialNodeCount;
  private List<String> locations;
  private String kubernetesVersion;
  private Integer ipv4CidrSize;
  private String machineType;
  private Integer diskSizeGb;
  private String diskType;
  private Boolean autoScalingEnabled;
  private Integer autoScalingMinNodeCount;
  private Integer autoScalingMaxNodeCount;

  public static NodePool fromGcpNodePool(com.google.container.v1.NodePool nodePool, String projectId) {
    if (nodePool == null) {
      throw new IllegalArgumentException("NodePool provided must not be null");
    }
    NodePool np = new NodePool();
    np.setName(nodePool.getName());
    np.setProjectId(projectId);
    np.setId(nodePool.getName());
    np.setLocations(nodePool.getLocationsList());
    np.setInitialNodeCount(nodePool.getInitialNodeCount());
    np.setKubernetesVersion(nodePool.getVersion());
    np.setIpv4CidrSize(nodePool.getPodIpv4CidrSize());
    if (nodePool.getAutoscaling() != null) {
      np.setAutoScalingEnabled(nodePool.getAutoscaling().getEnabled());
      np.setAutoScalingMinNodeCount(nodePool.getAutoscaling().getMinNodeCount());
      np.setAutoScalingMaxNodeCount(nodePool.getAutoscaling().getMaxNodeCount());
    } else {
      np.setAutoScalingEnabled(false);
    }
    if (nodePool.getConfig() != null) {
      np.setMachineType(nodePool.getConfig().getMachineType());
      np.setDiskType(nodePool.getConfig().getDiskType());
      np.setDiskSizeGb(nodePool.getConfig().getDiskSizeGb());
    }
    return np;
  }
}
