package com.stackdriver.api.custommetrics;

import java.util.Date;

import org.testng.annotations.Test;

public class CustomMetricsIntegrationTests {
	
	public static final String TEST_API_KEY = "YOUR API KEY HERE";
	public static final String TEST_METRIC_NAME = "stackdriver.test.javaclient";
	
	public static final String TEST_INSTANCE_METRIC_NAME = "java.custom.instancemetric";
	public static final String TEST_INSTANCE_ID = "YOUR INSTANCE ID HERE";
	
	@Test
	public void testSendDataPointRemote() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 5.0, new Date(pointTime));
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		CustomMetricsPoster metricsPoster = new CustomMetricsPoster(TEST_API_KEY);
		metricsPoster.sendMetrics(message);
	}
	
	@Test
	public void testSendInstanceDataPointRemote() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new InstanceDataPoint(TEST_INSTANCE_METRIC_NAME, 5.0, new Date(pointTime), TEST_INSTANCE_ID);
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		CustomMetricsPoster metricsPoster = new CustomMetricsPoster(TEST_API_KEY);
		metricsPoster.sendMetrics(message);
	}
}
