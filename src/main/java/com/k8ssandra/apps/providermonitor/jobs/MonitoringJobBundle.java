package com.k8ssandra.apps.providermonitor.jobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Trigger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringJobBundle {
  private String provider;
  private String service;
  private Trigger trigger;
  private JobDetail jobDetail;
}
