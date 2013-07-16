package com.stackdriver.api.custommetrics;

import java.util.Date;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class CustomMetricsTests {

	public static final String TEST_API_KEY = "YOUR API KEY HERE";
	public static final String TEST_METRIC_NAME = "stackdriver.test.javaclient";
	
	public static final String TEST_INSTANCE_METRIC_NAME = "java.custom.instancemetric";
	public static final String TEST_INSTANCE_ID = "YOUR INSTANCE NAME HERE";
	
	@Test
	public void testSendDataPointLocal() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 0.0, new Date(pointTime));
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		CustomMetricsPoster metricsPoster = new CustomMetricsPoster(TEST_API_KEY);
		metricsPoster.sendMetricsLocal(message);
	}
	
	@Test
	public void testSendDataPointRemote() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 3.0, new Date(pointTime));
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		CustomMetricsPoster metricsPoster = new CustomMetricsPoster(TEST_API_KEY);
		metricsPoster.sendMetrics(message);
	}
	
	@Test
	public void testAddDataPoint() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 0.0, new Date(pointTime));
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		Assert.assertEquals(message.getDataPoints().size(), 1);
	}
	
	@Test
	public void testDateConversion() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 0.0, new Date(pointTime));
		Assert.assertEquals(point.getCollectedAtEpoch(), pointTime / 1000);
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
