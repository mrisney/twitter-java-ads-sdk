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
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class TwitterAdsClientTest {

	static private  TwitterAdsClient getTwitterAdsClient() {
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

	final static Properties properties = new Properties();
	final static TwitterAdsClient client = getTwitterAdsClient();
	final static String ACCOUNT_ID = "gq10m0";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TwitterAdsClient.setSandbox(true);
		TwitterAdsClient.setTrace(true);
	}
	
	
	@Test
	public void testClientProperties() throws Exception {
		String consumerKey = properties.getProperty("consumer.key");
		String consumerSecret = properties.getProperty("consumer.secret");
		String accessToken = properties.getProperty("access.token");
		String accessSecret = properties.getProperty("access.secret");

		TwitterAdsClient client = new TwitterAdsClient(consumerKey, consumerSecret, accessToken, accessSecret);

		client.setConsumerKey(properties.getProperty("consumer.key"));
		client.setConsumerSecret(properties.getProperty("consumer.secret"));
		client.setAccessToken(properties.getProperty("access.token"));
		client.setAccessTokenSecret(properties.getProperty("access.secret"));
		
		assertEquals("client has consumer key", client.getConsumerKey(), consumerKey);
		assertEquals("client has consumer secret", client.getConsumerSecret(), consumerSecret);
		assertEquals("client has access token", client.getAccessToken(), accessToken);
		assertEquals("client has access secret", client.getAccessTokenSecret(), accessSecret);
		
		assertNotNull(client.toString());
	}

	
	@Test 
	public void testGetAccount() throws Exception {
		Account account = client.getAccount(ACCOUNT_ID);
		assertEquals(account.getId(), ACCOUNT_ID);
		assertTrue("account now has an id generated",account.toString().contains("id"));

	}
	
	@Test 
	public void testGetAccounts() throws Exception {
		List<Account> accounts = client.getAccounts();
		assertThat("These should be at least one account", accounts.size(), is(greaterThan(0)));
		String id = accounts.get(0).getId();
		assertEquals(id, ACCOUNT_ID);
		
	}	
}
