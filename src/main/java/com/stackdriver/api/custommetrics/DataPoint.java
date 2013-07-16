package com.stackdriver.api.custommetrics;

import java.util.Date;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains information about a single data point for a custom metric.
 * <br/>
 * One or more of these will be wrapped in a GatewayMessage for sending to the Stackdriver gateway
 * 
 * @see CustomMetricsMessage
 */
public class DataPoint {
	
	private static final Logger LOGGER = Logger.getLogger(DataPoint.class.getName());

	// the name of your custom metric
	private String name;

	// the numeric value for this observation of the metric
	private double value;

	// Java-friendly date object, will emit a unix timestamp
	private Date collectedAt;

	public DataPoint(String name, double value, Date collectedAt) {
		LOGGER.fine(String.format("creating DataPoint name=%s value=%s collectedAt=%s", name, value, collectedAt));
		this.name = name;
		this.value = value;
		this.collectedAt = collectedAt;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(final String name) {
		this.name = name;
	}

	@JsonProperty("value")
	public double getValue() {
		return this.value;
	}

	@JsonProperty("value")
	public void setValue(final double value) {
		this.value = value;
	}

	@JsonIgnore
	public Date getCollectedAt() {
		return this.collectedAt;
	}

	@JsonIgnore
	public void setCollectedAt(final Date collectedAt) {
		this.collectedAt = collectedAt;
	}

	/**
	 * Get the collectedAt date as a Unix epoch timestamp <br/>
	 * This is primarily here to support JSON Serialization
	 * 
	 * @return an int with the Unix timestamp, or 0 if not defined
	 */
	@JsonProperty("collected_at")
	public long getCollectedAtEpoch() {
		if (this.collectedAt == null) {
			throw new IllegalStateException("collectedAt must be set, can't get as epoch");
		}
		return (this.collectedAt.getTime() / 1000);
	}

	/**
	 * Set the collectedAt date using a Unix timestamp <br/>
	 * This is primarily here to support deserialization from JSON
	 * 
	 * @param collectedAtEpoch
	 *            timestamp in Unix timestamp format
	 */
	@JsonProperty("collected_at")
	public void setCollectedAtEpoch(final long collectedAtEpoch) {
		long millis = collectedAtEpoch * 1000;
		this.collectedAt = new Date(millis);
	}
}
