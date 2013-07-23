stackdriver-custommetrics-java
==============================

This project contains a library for use in Java applications to post custom metrics to Stackdriver using our https gateway.

[Stackdriver Custom Metrics Guide](http://feedback.stackdriver.com/knowledgebase/articles/181488-sending-custom-metrics-to-the-stackdriver-system)


## Adding to Your Maven Project
Releases of this project are available via Maven Central.  Here's how to import the 0.3 release

```
<dependency>
	<groupId>com.stackdriver</groupId>
	<artifactId>stackdriver-custommetrics</artifactId>
	<version>0.3</version>
</dependency>
```

## Examples
Here are some examples for how to use this code in your projects.  The package import is `com.stackdriver.api.custommetrics` and the main
class is `CustomMetricsPoster`.  Items in ALL CAPS below are meant to be placeholders for your values and will need to be substituted.


YOUR_API_KEY - insert the API key you generated in Stackdriver
 
YOUR_METRIC_NAME - custom metric name.  Related ones should have similar names so they can be wildcarded

POINT_VALUE - observation of the metric value

COLLECTED_AT_DATE - Java Date object that indicates when the observation took place

CURRENT_INSTANCE_ID - AWS or Rackspace Cloud instance ID for the instance that the metric applies to, when applicable  

```
import com.stackdriver.api.custommetrics.CustomMetricsPoster
```

### Sending a custom metric value, not tied to an instance, collected now
```
new CustomMetricsPoster(YOUR_API_KEY).sendMetricDataPoint(YOUR_METRIC_NAME, POINT_VALUE);

```

### Sending multiple metrics, all collected now
```
new CustomMetricsPoster(YOUR_API_KEY).sendMetricDataPoint(YOUR_METRIC_NAME, POINT_VALUE).sendMetricDataPoint(SECOND_METRIC_NAME, SECOND POINT VALUE);

```

### Sending a custom metric value, tied to an instance, collected now
```
new CustomMetricsPoster(YOUR_API_KEY).sendInstanceMetricDataPoint(YOUR_METRIC_NAME, POINT_VALUE, CURRENT_INSTANCE_ID);

```

### Sending a custom metric value, not tied to an instance, collected now
```
new CustomMetricsPoster(YOUR_API_KEY).sendMetricDataPoint(YOUR_METRIC_NAME, POINT_VALUE, COLLECTED_AT_DATE);

```

### Sending a custom metric value, tied to an instance, collected at some past time
```
new CustomMetricsPoster(YOUR_API_KEY).sendInstanceMetricDataPoint(YOUR_METRIC_NAME, POINT_VALUE, COLLECTED_AT_DATE, CURRENT_INSTANCE_ID);

```
