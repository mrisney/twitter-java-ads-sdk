package examples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.RandomStringUtils;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.FundingInstrument;
import com.steelhouse.twitter.ads.campaign.LineItem;
import com.steelhouse.twitter.ads.campaign.PromotableUser;
import com.steelhouse.twitter.ads.campaign.Tweet;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.creative.PromotedTweet;
import com.steelhouse.twitter.ads.enums.BidUnit;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Optimizations;
import com.steelhouse.twitter.ads.enums.Placement;
import com.steelhouse.twitter.ads.enums.Product;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PromotedTweetExample {

	private static TwitterAdsClient getTwitterAdsClient() {
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/config.properties");
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

		Account account = client.getAccount(ACCOUNT_ID);
		/*
		 * String fundingInstrumentId =
		 * account.getFundingInstruments().get(0).getId(); String userId =
		 * account.getPromotableUsers().get(0).getId();
		 * 
		 * System.out.println(userId);
		 * 
		 * Campaign campaign = account.getCampaigns().get(0);
		 * 
		 * String lineItemId = account.getLineItems().get(0).getId();
		 * 
		 * Tweet tweet = Tweet.builder() .account(account) .status(
		 * "Angry Birds rawks @ angrybirds") .as_user_id("4846476000") .build();
		 * 
		 * String tweetId = tweet.create();
		 * 
		 * Long tweetIdL = Long.parseLong(tweetId);
		 * 
		 * PromotedTweet promotedTweet = PromotedTweet.builder()
		 * .account(account) .line_item_id(lineItemId) .tweet_ids(tweetIdL)
		 * .paused(false) .build(); promotedTweet.save();
		 * System.out.println(promotedTweet.toString());
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * //String resourcePath = "/0/accounts/{account_id}/promoted_tweets";
		 */

		WebResource webResource = ClientServiceFactory.getInstance().getResource();
		webResource.addFilter(new LoggingFilter());
		String resourcePath = "/0/accounts/" + account.getId() + "/promoted_tweets";

		List<PromotedTweet> promotedTweets = webResource.path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE)
				.get(new GenericType<List<PromotedTweet>>() {
				});

		for (PromotedTweet promotedTweet : promotedTweets) {
			System.out.println(promotedTweet);

		}
	}
}
