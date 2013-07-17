package com.stackdriver.api.custommetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
	public int getProtocolVersion() {
		return PROTOCOL_VERSION;
	}
	
	/**
	 * Get the current data points for this message
	 * 
	 * @return List of DataPoint objects
	 */
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	/**
	 * This list setter is mainly used in JSON deserialization
	 * 
	 * @param dataPoints List of DataPoint objects
	 */
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
	
	/**
	 * Serialize this object to JSON string, so we don't need a Jackson dependency in the project
	 * <br/>
	 * Not super robust about special characters, so please don't use them in your names/values
	 * (quotes/brackets/braces etc) or the gateway will give you a 400 for invalid JSON.
	 * 
	 * @return a String in JSON format representing this object and nested data points
	 */
	public String toJson() {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{");
		jsonBuilder.append(String.format("\"timestamp\":%d,", this.getTimestamp()));
		jsonBuilder.append(String.format("\"proto_version\":%d,", this.getProtocolVersion()));
		jsonBuilder.append("\"data\":[");
		int pointsCount = 0;
		for (DataPoint point: this.getDataPoints()) {
			if (pointsCount++ > 0) {
				jsonBuilder.append(',');
			}
			jsonBuilder.append(point.toJson());
		}
		jsonBuilder.append("]");
		jsonBuilder.append("}");
		return jsonBuilder.toString();
	}
}
