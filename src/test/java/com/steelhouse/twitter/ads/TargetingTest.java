package com.steelhouse.twitter.ads;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.enums.BidType;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Product;
import com.steelhouse.twitter.ads.targeting.ReachEstimate;

public class TargetingTest {
	
	Logger log = LoggerFactory.getLogger(getClass());

	static private TwitterAdsClient getTwitterAdsClient() {
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
	final static String ACCOUNT_ID = "gq10m0";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("gettingTwitterAds REST Client...");

		TwitterAdsClient.setTrace(false);
		TwitterAdsClient.setSandbox(true);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testReachEstimate() throws Exception {
		
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		
		Account account = client.getAccount("18ce54aq4d5");
		
		Long similarToFollowersOfUsers = Long.parseLong("14230524");
		
		ReachEstimate reachEstimate = ReachEstimate.builder()
				.account(account)
				.product_type(Product.PROMOTED_TWEETS)
				.objective(Objective.TWEET_ENGAGEMENTS)
				.campaign_daily_budget_amount_local_micro(550000)
				.currency("USD")
				.languages("en")
				.locations("b6b8d75a320f81d9")
				.similar_to_followers_of_users(similarToFollowersOfUsers)
				.gender(2)
				.build();
		
		assertNotNull("reach estimate builder to string", ReachEstimate.builder().toString());
		reachEstimate.load();
		assertNotNull("reach estimate infinite bid count has a max value", reachEstimate.getInfinite_bid_count().getMax());
		assertNotNull("reach estimate infinite bid count has a min value", reachEstimate.getInfinite_bid_count().getMin());
		
		//assertNotNull(reachEstimate.getImpresions().getMin());
		reachEstimate.setGender(1);
		reachEstimate.load();
		log.info(reachEstimate.toString());
	}
}
