package com.stackdriver.api.custommetrics;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstanceDataPoint extends DataPoint {

	// (optional) instance ID that this custom metric applies to
	private String instanceId;


	public InstanceDataPoint(String name, double value, Date collectedAt, String instanceId) {
		super(name, value, collectedAt);
		this.instanceId = instanceId;
	}
	
	@JsonProperty("instance")
	public String getInstanceId() {
		return instanceId;
	}

	@JsonProperty("instance")
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
}
