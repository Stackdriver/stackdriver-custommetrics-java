stackdriver-custommetrics-java
==============================

This project contains a library for use in Java applications to post custom metrics to Stackdriver using our https gateway.

[Stackdriver Custom Metrics Guide](http://feedback.stackdriver.com/knowledgebase/articles/181488-sending-custom-metrics-to-the-stackdriver-system)


## Examples
Here are some examples for how to use this code in your projects.  The package import is `com.stackdriver.api.custommetrics` and the main
class is `CustomMetricsPoster`.  Items in ALL CAPS below are meant to be placeholders for your values and will need to be substituted.

### Example of sending a metric not bound to an instance
```
long pointTime = System.currentTimeMillis();
DataPoint point = new DataPoint(YOUR_METRIC_NAME, POINT_VALUE, new Date(pointTime));
CustomMetricsMessage message = new CustomMetricsMessage();
message.addDataPoint(point);
CustomMetricsPoster metricsPoster = new CustomMetricsPoster(YOUR_API_KEY);
metricsPoster.sendMetrics(message);
```


### Example of sending a metric bound to an instance id
```
long pointTime = System.currentTimeMillis();
DataPoint point = new InstanceDataPoint(YOUR_METRIC_NAME, POINT_VALUE ,new Date(pointTime), YOUR_INSTANCE_ID);
CustomMetricsMessage message = new CustomMetricsMessage();
message.addDataPoint(point);
CustomMetricsPoster metricsPoster = new CustomMetricsPoster(YOUR_API_KEY);
metricsPoster.sendMetrics(message);
```
