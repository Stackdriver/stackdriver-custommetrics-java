package com.stackdriver.api.custommetrics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.logging.Logger;

/**
 * As the name suggests, this class is used to post custom metrics about your infrastructure, services, or application
 * to Stackdriver over HTTP. <br/>
 * See http://feedback.stackdriver.com/knowledgebase/articles/181488-sending-custom-metrics-to-the-stackdriver-system
 * for more information on Stackdriver custom metrics.
 * 
 */
public class CustomMetricsPoster {
	
	private static final Logger LOGGER = Logger.getLogger(CustomMetricsPoster.class.getName());

	public static final String DEFAULT_ENDPOINT_URL = "https://custom-gateway.stackdriver.com/v1/custom";

	public static final String POST_CONTENT_TYPE = "application/json";

	public static final int GATEWAY_TIMEOUT_MILLIS = 3000;

	private String apiKey;

	private URL endpointUrl;

	private Proxy proxy;

	/**
	 * Basic constructor, most applications will use this. Posts to the Stackdriver default custom metrics endpoint
	 * without an HTTP proxy in between.
	 * 
	 * @param apiKey
	 *            active API key for your Stackdriver account
	 */
	public CustomMetricsPoster(final String apiKey) {
		this(apiKey, null, -1, null);
	}

	/**
	 * If you use an HTTP proxy, use this constructor. Adds the HTTP proxy information to the basic constructor.
	 * 
	 * @param apiKey
	 *            active API key for your Stackdriver account
	 * @param proxyHost
	 *            String for the HTTP proxy hostname
	 * @param proxyPort
	 *            int with the proxy port
	 */
	public CustomMetricsPoster(final String apiKey, final String proxyHost, final int proxyPort) {
		this(apiKey, proxyHost, proxyPort, null);
	}

	/**
	 * If you want to test with a custom endpoint URL, use this constructor. Adds a URL argument that will be posted to
	 * instead of the Stackdriver endpoint. If you have something that eats http posts for debug purposes and you want
	 * to see what you're sending, this is the one for you.
	 * 
	 * @param apiKey
	 *            active API key for your Stackdriver account
	 * @param endpointUrl
	 *            java.net.URL for where the messages will be posted to
	 */
	public CustomMetricsPoster(final String apiKey, final String endpointUrl) {
		this(apiKey, null, -1, endpointUrl);
	}

	/**
	 * If you're posting to a custom URL through a proxy, use this. Probably not too many of you, but this is the
	 * constructor that actually does things so leaving it open for anyone who may need it. <br/>
	 * proxy and endpointUrl are optional, pass nulls to not have them, or use the constructors without those arguments.
	 * 
	 * @param apiKey
	 *            active API key for your Stackdriver account
	 * @param proxyHost
	 *            String for the HTTP proxy hostname
	 * @param proxyPort
	 *            int with the proxy port
	 * @param endpointUrl
	 *            java.net.URL for where the messages will be posted to
	 */
	public CustomMetricsPoster(final String apiKey, final String proxyHost, final int proxyPort, final String endpointUrl) {
		LOGGER.fine("creating CustomMetricsPoster");
		
		// make sure an API key was passed
		if (apiKey != null && apiKey.length() > 0) {
			LOGGER.fine("setting API key to " + apiKey);
			this.apiKey = apiKey;
		} else {
			LOGGER.severe("API key not passed, cannot proceed");
			throw new IllegalArgumentException("API key is required for constructing a StackdriverCustomMetricsPoster");
		}

		// set up the HTTP proxy if requested
		if (proxyHost != null && proxyPort > 0) {
			LOGGER.fine("proxy information passed host=" + proxyHost + ", port=" + proxyPort);
			this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		}

		// override the endpoint if requested, otherwise set to the default
		try {
			if (endpointUrl != null) {
				
				this.endpointUrl = new URL(endpointUrl);
			} else {
					this.endpointUrl = new URL(DEFAULT_ENDPOINT_URL);
			}
		} catch (MalformedURLException e) {
			LOGGER.severe("Invalid endpoint URL supplied " + endpointUrl);
			throw new IllegalArgumentException("Endpoint URL passed to a StackdriverCustomMetricsPoster must be a valid URL, pass null to use the default");
		}
	}

	/**
	 * This is the method that does the posting. It serializes your message to JSON and posts it to the Stackdriver
	 * endpoint.
	 * 
	 * @param message
	 *            GatewayMessage object with one or more DataPoint objects in it
	 * 
	 */
	public void sendMetrics(final CustomMetricsMessage message) {
		this.sendMetrics(message, false);
	}
	
	/**
	 * Test version of the metric poster that performs the usual validation but writes to the log instead of the 
	 * Stackdriver endpoint, so the metrics won't really be sent.
	 * 
	 * @param message
	 *            GatewayMessage object with one or more DataPoint objects in it
	 * 
	 */
	public void sendMetricsLocal(final CustomMetricsMessage message) {
		this.sendMetrics(message, true);
	}
	
	public void sendMetrics(final CustomMetricsMessage message, final boolean localMode) {
		// check the message
		if (message == null) {
			LOGGER.severe("can't send metrics, message is missing");
			throw new IllegalArgumentException("message is required for postMetricsToGateway");
		} else if (message.getDataPoints() == null || message.getDataPoints().size() == 0) {
			LOGGER.severe("can't send metrics, no data points are present in the message");
			throw new IllegalArgumentException("message for postMetricsToGateway must contain one or more data points");
		}

		// serialize message to JSON
		String messageJson = message.toJson();

		if (localMode) {
			LOGGER.info("sendMetrics called in local mode, received message:");
			LOGGER.info(messageJson);
		} else {
			// post message to HTTP
			LOGGER.fine("sendMetrics called in remote mode, message ready for gateway:");
			LOGGER.fine(messageJson);
			
			this.postMetricMessageToGateway(messageJson);
		}
	}

	/**
	 * Posts the message to the Stackdriver gateway endpoint 
	 * 
	 * @param messageJson String containing JSON message payload
	 */
	protected void postMetricMessageToGateway(final String messageJson) {
		HttpURLConnection urlConnection = null;
		try {
			LOGGER.fine("sending data to the Stackdriver gateway");
			if (this.proxy == null) {
				urlConnection = (HttpURLConnection) this.endpointUrl.openConnection();
			} else {
				urlConnection = (HttpURLConnection) this.endpointUrl.openConnection(this.proxy);
			}
			
			LOGGER.fine("http connection established to Stackdriver gateway");

			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setReadTimeout(GATEWAY_TIMEOUT_MILLIS);
			urlConnection.setRequestProperty("content-type", "application/json; charset=utf-8");
			urlConnection.setRequestProperty("x-stackdriver-apikey", this.apiKey);

			LOGGER.fine("writing message body ");
			
			// send JSON to the output stream
			OutputStream os = urlConnection.getOutputStream();
			os.write(messageJson.getBytes());
			os.flush();
			os.close();

			LOGGER.fine("awaiting gateway response");
			
			int responseCode = urlConnection.getResponseCode();
			if (responseCode >= 300) {
				LOGGER.warning("Stackdriver gateway returned a non-200 response: " + responseCode);
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "UTF-8"));
			    String errorMessage = in.readLine();
			    in.close();
			    LOGGER.warning(errorMessage);
			}
		} catch (Exception e) {
			LOGGER.severe("Error connecting to the Stackdriver gateway " + e.toString());
			// TODO: exception for exceptions going to HTTP
		} finally {
			if (urlConnection != null) {
				LOGGER.fine("disconnecting from Stackdriver gateway");
				urlConnection.disconnect();
			}
		}
	}
}
