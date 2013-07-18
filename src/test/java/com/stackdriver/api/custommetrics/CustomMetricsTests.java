package com.stackdriver.api.custommetrics;

import java.util.Date;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class CustomMetricsTests {

	public static final String TEST_API_KEY = "LOCALTESTAPIKEY-UNUSED";
	public static final String TEST_METRIC_NAME = "stackdriver.test.javaclient";
	
	public static final String TEST_INSTANCE_METRIC_NAME = "java.custom.instancemetric";
	public static final String TEST_INSTANCE_ID = "YOUR INSTANCE ID HERE";
	
	@Test
	public void testSendDataPointLocal() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 0.0, new Date(pointTime));
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		CustomMetricsPoster metricsPoster = new CustomMetricsPoster(); // no params, local mode
		metricsPoster.sendMetrics(message);
	}
	
	@Test
	public void testSendPointShortFormLocal() {
		new CustomMetricsPoster().sendMetricDataPoint(TEST_METRIC_NAME, 0.0);
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
	public void testDataPointJson() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 100.0, new Date(pointTime));
		String json = point.toJson();
		System.out.println(json);
	}
	
	@Test
	public void testMessageJson() {
		long pointTime = System.currentTimeMillis();
		DataPoint point = new DataPoint(TEST_METRIC_NAME, 0.0, new Date(pointTime));
		DataPoint point2 = new DataPoint(TEST_METRIC_NAME, 2.0, new Date(pointTime + 30000));
		CustomMetricsMessage message = new CustomMetricsMessage();
		message.addDataPoint(point);
		message.addDataPoint(point2);
		String json = message.toJson();
		System.out.println(json);
	}
	
}
