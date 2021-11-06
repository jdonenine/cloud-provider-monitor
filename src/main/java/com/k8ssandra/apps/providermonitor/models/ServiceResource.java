package com.k8ssandra.apps.providermonitor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResource {
  private String provider;
  private String name;
  private String id;
  private String projectId;
  private String owner;
  private Long timeCreated;
  private Long timeUpdated;
  private String location;
  private String locationType;
  private Map<String, String> labels;
}
