package com.steelhouse.twitter.ads;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.creative.WebsiteCard;
import com.steelhouse.twitter.ads.enums.WebEventTagType;
import com.steelhouse.twitter.ads.measurement.WebEventTag;

public class MeasurementTest {

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

	final static String ACCOUNT_ID = "18ce54aq4d5";
	final static String SANDBOX_ACCOUNT_ID = "gq10m0";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetWebEventTag() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount("luyeg");

		WebEventTag webEventTag = WebEventTag.builder()
				.account(account)
				.name("TestWebTag")
				.click_window(1)
				.view_through_window(0)
				.type(WebEventTagType.SITE_VISIT)
				.retargeting_enabled(true)
				.build();

		assertNotNull(WebEventTag.builder().toString());
		webEventTag.save();
		assertTrue("WebEvent tag now has an id generated", webEventTag.toString().contains("id"));
		assertTrue("WebEvent tag now has embedded code ", webEventTag.getEmbed_code().contains("src"));
		
		// test load
		/*
		
		String id = webEventTag.getId();

		WebEventTag savedWebEventTag = new WebEventTag(account);
		savedWebEventTag.setId(id);
		savedWebEventTag.load();

		assertTrue("Website Card name has been loaded", savedWebEventTag.getName().equals("TestWebTag"));

		// test reload
		savedWebEventTag.setName("ReLoadTestUpdateName");
		savedWebEventTag.reload();
		assertFalse("WebEvent name image id has not been updated",savedWebEventTag.getName().equals("TestWebTag"));
*/
		// clean up
	//	webEventTag.delete();
	//	assertTrue("WebSite card has been deleted", webEventTag.isDeleted());
	}

}
