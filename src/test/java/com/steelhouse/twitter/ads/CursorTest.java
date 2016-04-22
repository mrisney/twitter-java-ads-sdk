package com.steelhouse.twitter.ads;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.resource.Cursor;


public class CursorTest {
	
	private static final Logger log = LoggerFactory.getLogger(CursorTest.class);
	static private  TwitterAdsClient getTwitterAdsClient() {
	 
	
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

	
	public static void main(String args[]) throws Exception {
		Account account = client.getAccount(ACCOUNT_ID);
		Campaign campaign = new Campaign(account);
		Cursor cursor = new Cursor(campaign);
		
		log.debug("total number of object :"+cursor.count());
	
		//promotableUser
	}

}
