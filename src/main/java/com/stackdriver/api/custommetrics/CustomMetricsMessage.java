package com.stackdriver.api.custommetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains the information needed for a Stackdriver gateway message.  Included
 * are one or more data points, a protocol version, and a timestamp.
 * 
 * @see DataPoint for the contents of the data point
 */
public class CustomMetricsMessage {
	
	private static final Logger LOGGER = Logger.getLogger(CustomMetricsMessage.class.getName());
	
	private static final int PROTOCOL_VERSION = 1;
	
	// timestamp when this message was generated, auto-set in constructor
	private long timestamp;
	
	// one or more data points
	private List<DataPoint> dataPoints;
	
	/**
	 * Constructor for a full message.
	 */
	public CustomMetricsMessage() {
		LOGGER.fine("creating CustomMetricsMessage");
		this.timestamp = (System.currentTimeMillis() / 1000);
		this.dataPoints = new ArrayList<DataPoint>();
	}

	/**
	 * Return the timestamp from when this message was created.  Included for protocol compatability
	 * 
	 * @return a unix timestamp representing the creation time of this object
	 */
	@JsonProperty("timestamp")
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Get the current protocol version, which is a constant.
	 * 
	 * This accessor is included for JSON serialization purposes
	 * 
	 * @return the current protocol version.
	 */
	@JsonProperty("proto_version")
	public int getProtocolVersion() {
		return PROTOCOL_VERSION;
	}
	
	/**
	 * Get the current data points for this message
	 * 
	 * @return List of DataPoint objects
	 */
	@JsonProperty("data")
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	/**
	 * This list setter is mainly used in JSON deserialization
	 * 
	 * @param dataPoints List of DataPoint objects
	 */
	@JsonProperty("data")
	public void setDataPoints(final List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}
	
	/**
	 * Add a data point to this message.  Multiple data points per message are supported
	 * 
	 * @param dataPoint DataPoint object to add
	 */
	public void addDataPoint(final DataPoint dataPoint) {
		this.dataPoints.add(dataPoint);
	}
}
