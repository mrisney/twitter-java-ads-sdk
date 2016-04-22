package examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.JsonObject;
import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.Tweet;
import com.steelhouse.twitter.ads.client.ClientService;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.common.Constants;
import com.steelhouse.twitter.ads.creative.PromotedTweet;
import com.steelhouse.twitter.ads.resource.Resource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class StatusUtils {

	private static TwitterAdsClient getTwitterAdsClient() {
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/config.properties.steelhousedev.properties");
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String consumerKey = properties.getProperty("consumer.key");
		String consumerSecret = properties.getProperty("consumer.secret");
		String accessToken = properties.getProperty("access.token");
		String accessSecret = properties.getProperty("access.secret");
		return new TwitterAdsClient(consumerKey, consumerSecret, accessToken, accessSecret);
	}

	final static TwitterAdsClient client = getTwitterAdsClient();
	final static String ACCOUNT_ID = "18ce54aq4d5";

	public static void main(String[] args) throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		String domain = "https://ads-api.twitter.com";

		TwitterAdsClient.setDomain(domain);

		WebResource webResource = Resource.getResource();
		webResource.addFilter(new LoggingFilter());

		String resourcePath = "/0/accounts/" + ACCOUNT_ID + "/promoted_tweets";

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("with_deleted", "true");

		List<PromotedTweet> promotedTweets = webResource
				.queryParams(params)
				.path(resourcePath)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(new GenericType<List<PromotedTweet>>() {});
		
		
		for (PromotedTweet promotedTweet : promotedTweets) {
			//System.out.println(promotedTweet.toString());
			Long tweetIdL =  Long.parseLong(promotedTweet.getTweet_id());
			System.out.println(tweetIdL);
			Tweet.destroy(tweetIdL);
		}

	}
}