package com.stackdriver.api.custommetrics;

import java.util.Date;

public class InstanceDataPoint extends DataPoint {

	// (optional) instance ID that this custom metric applies to
	private String instanceId;


	public InstanceDataPoint(String name, double value, Date collectedAt, String instanceId) {
		super(name, value, collectedAt);
		this.instanceId = instanceId;
	}
	
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * Serialize this object to JSON string, so we don't need a Jackson dependency in the project
	 * <br/>
	 * Not super robust about special characters, so please don't use them in your names/values
	 * (quotes/brackets/braces etc) or the gateway will give you a 400 for invalid JSON.
	 * 
	 * @return a String in JSON format representing this object
	 */
	@Override
	public String toJson() {
		return String.format("{\"name\":\"%s\",\"value\":%f,\"collected_at\":%d, \"instance\": \"%s\"}", this.getName(), this.getValue(), this.getCollectedAtEpoch(), this.getInstanceId());
	}
}
