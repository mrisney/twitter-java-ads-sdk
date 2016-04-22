package com.steelhouse.twitter.ads;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steelhouse.twitter.ads.campaign.AppList;
import com.steelhouse.twitter.ads.campaign.AppList.apps;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.FundingInstrument;
import com.steelhouse.twitter.ads.campaign.LineItem;
import com.steelhouse.twitter.ads.campaign.PromotableUser;
import com.steelhouse.twitter.ads.campaign.TargetingCriteria;
import com.steelhouse.twitter.ads.campaign.Tweet;
import com.steelhouse.twitter.ads.campaign.Tweet.Preview;
import com.steelhouse.twitter.ads.client.ClientService;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.creative.AppDownloadCard;
import com.steelhouse.twitter.ads.creative.WebsiteCard;
import com.steelhouse.twitter.ads.enums.BidType;
import com.steelhouse.twitter.ads.enums.BidUnit;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Optimizations;
import com.steelhouse.twitter.ads.enums.Placement;
import com.steelhouse.twitter.ads.enums.Product;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class CampaignTest {

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

	static private String uploadTestImage() {
		TwitterAdsClient.setTrace(false);
		TwitterAdsClient.setSandbox(true);

		// upload image

		String domain = "https://upload.twitter.com";
		String resource = "/1.1/media/upload.json";
		String filePath = "src/test/resources/angry.bird.800.320.png";

		ClientService clientService = ClientServiceFactory.getInstance();
		Client client = clientService.getClient();

		WebResource webResource = client.resource(domain);
		final FileDataBodyPart filePart = new FileDataBodyPart("media", new File(filePath));
		final FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		FormDataMultiPart multiPart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);

		ClientResponse response = webResource.path(resource).type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, multiPart);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		JsonObject jsonObj = new JsonParser().parse(output).getAsJsonObject();
		return jsonObj.get("media_id_string").getAsString();

	}

	final static TwitterAdsClient client = getTwitterAdsClient();
	final static String ACCOUNT_ID = "18ce54aq4d5";
	final static String SANDBOX_ACCOUNT_ID = "gq10m0";
	static String IMAGE_MEDIA_ID = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		TwitterAdsClient.setTrace(false);
		TwitterAdsClient.setSandbox(true);
		IMAGE_MEDIA_ID = uploadTestImage();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCreateCampaign() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);

		// get the first account
		Account account = client.getAccount(ACCOUNT_ID);

		// get the first funding instrument
		String fundingInstrumentId = account.getFundingInstruments().get(0).getId();

		Date now = Date.from(Instant.now());

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, +7);
		Date weekFromToday = cal.getTime();

		Campaign campaign = Campaign.builder()
				.account(account)
				.name("Monday Morning Test Campaign")
				.funding_instrument_id(fundingInstrumentId)
				.currency("US")
				.daily_budget_amount_local_micro(50000)
				.total_budget_amount_local_micro(50000)
				.standard_delivery(true)
				.start_time(now)
				.end_time(weekFromToday)
				.paused(true)
				.build();

		assertNotNull("campaign builder to string", Campaign.builder().toString());
		campaign.save();


		assertTrue("campaign, from builder style now has an id generated", campaign.toString().contains("id"));

		campaign.setName("Updated Test Campaign");
		Number dailyBudgetAmountLocalMicro = 1000000;
		Number totalBudgetAmountLocalMicro = 2000000;
		campaign.setDaily_budget_amount_local_micro(dailyBudgetAmountLocalMicro);
		campaign.setTotal_budget_amount_local_micro(totalBudgetAmountLocalMicro);
		//campaign.setDuration_in_days(7);
		campaign.setStandard_delivery(true);
		campaign.setPaused(false);
		//campaign.setFrequency_cap(1);
		
		// campaign.setStart_time(now);
		// campaign.setEnd_time(weekFromToday);
		campaign.save();

		assertTrue("campaign now has an id generated", campaign.toString().contains("id"));
		assertTrue("campaign name has been updated", campaign.getName().equals("Updated Test Campaign"));
		assertTrue("campaign daily budget has been updated", campaign.getDaily_budget_amount_local_micro().toString()
				.equals(dailyBudgetAmountLocalMicro.toString()));
		assertTrue("campaign total budget has been updated", campaign.getTotal_budget_amount_local_micro().toString()
				.equals(totalBudgetAmountLocalMicro.toString()));
		assertFalse("campaign paused has been set to false", campaign.isPaused());

		// test reloading capability
		campaign.setPaused(true);
		campaign.save();
		String id = campaign.getId();
		Campaign previousCampaign = new Campaign(account);
		previousCampaign.setId(id);
		
		previousCampaign.load();
		assertTrue("campaign is reloaded, and paused has been returned to false", previousCampaign.isPaused());
		previousCampaign.setFrequency_cap(1);
	
		campaign.delete();
		assertTrue("campaign has been deleted", campaign.isDeleted());
		
		TwitterAdsClient.setSandbox(true);

	}

	@Test
	public void testPromotableUsers() throws Exception {

		// get the first account
		Account account = client.getAccounts().get(0);

		// get the first promotable User
		PromotableUser promotableUser = new PromotableUser(account);
		promotableUser.load();
		assertTrue("promotable user now has an id generated", promotableUser.toString().contains("id"));
		assertNotNull("promotable user has a user id", promotableUser.getUser_id());
	}

	@Test
	public void testAppList() throws Exception {
		TwitterAdsClient.setSandbox(false);
		TwitterAdsClient.setTrace(true);

		Account account = client.getAccount(ACCOUNT_ID);
		// get the first account
		// Account account = client.getAccounts().get(0);

		/*
		 * 
		 * List<String> ids = Arrays.asList("343200656");
		 * appList.create("AngryBirds", ids);
		 */
		AppList appList = new AppList(account);
		List<apps> applications = appList.getApps();
		assertNotNull("appList to string", appList.toString());
		assertThat(applications.size(), is(greaterThan(0)));
		/*
		 * for (apps app : applications) {
		 * assertNotNull(app.getApp_store_identifier());
		 * assertNotNull(app.getOs_type()); }
		 */

	}

	@Test
	public void testLineItem() throws Exception {

		TwitterAdsClient.setSandbox(false);
		TwitterAdsClient.setTrace(true);

		Account account = client.getAccount(ACCOUNT_ID);

		// get the first funding instrument
		String fundingInstrumentId = account.getFundingInstruments().get(0).getId();

		FundingInstrument fundingInstrument = new FundingInstrument(account);
		fundingInstrument.setId("kry7m");
		fundingInstrument.load();
		log.info("FUNDIN ISNUTRUMENT " + fundingInstrument.toString());

		log.info("Funding isntrument id = " + fundingInstrument.getId());
		Date now = Date.from(Instant.now());

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, +7);
		Date weekFromToday = cal.getTime();

		// get the first campaign
		Campaign campaign = Campaign.builder().account(account).funding_instrument_id(fundingInstrumentId)
				.currency("US").daily_budget_amount_local_micro(5000000).total_budget_amount_local_micro(50000000)
				.name("Test Campaign Builder").paused(true).standard_delivery(true).start_time(now)
				.end_time(weekFromToday).build();

		assertNotNull("campaign builder to string", Campaign.builder().toString());
		campaign.save();

		Placement[] placements = new Placement[] { Placement.TWITTER_TIMELINE };

		LineItem lineItem = LineItem.builder().account(account).campaign_id(campaign.getId()).name("Test Line Item")
				.product_type(Product.PROMOTED_TWEETS).placements(placements).objective(Objective.TWEET_ENGAGEMENTS)
				.optimization(Optimizations.DEFAULT).bid_unit(BidUnit.ENGAGEMENT).bid_amount_local_micro(100000)
				.paused(true).build();

		assertNotNull("line item builder to string", LineItem.builder().toString());
		lineItem.save();
		assertTrue("LineItem Conversation  now has an id generated", lineItem.toString().contains("id"));

		lineItem.setName("Updated Line Item");
		lineItem.setAdvertiser_domain("steelhouse.com");
		lineItem.setAutomatically_select_bid(true);
		lineItem.setBid_type(BidType.MAX);
		lineItem.setPaused(false);
		lineItem.save();

		assertTrue("campaign now has an id generated", lineItem.toString().contains("id"));
		assertTrue("lineItem name has been updated", lineItem.getName().equals("Updated Line Item"));
		assertFalse("campaign paused has been set to false", lineItem.isPaused());

		// clean up
		lineItem.delete();
		assertTrue("line item criteria has been deleted", lineItem.isDeleted());
		campaign.delete();
		assertTrue("campaign has been deleted", campaign.isDeleted());
		TwitterAdsClient.setSandbox(true);
	}

	@Test
	public void testTweetPreview() throws Exception {

		TwitterAdsClient.setSandbox(false);
		TwitterAdsClient.setTrace(true);

		Account account = client.getAccount(ACCOUNT_ID);
		String userId = account.getPromotableUsers().get(0).getUser_id();

		WebsiteCard websiteCard = WebsiteCard.builder()
				.account(account)
				.name("TestCampaign")
				.image_media_id(IMAGE_MEDIA_ID)
				.website_title("Test Title")
				.website_url("http://www.twitter.com")
				.build();

		websiteCard.save();

		Tweet.Preview tweetPreview = Tweet.Preview.builder()
				 //.preview_target("PUBLISHER_NETWORK")
				.as_user_id(userId)
				.card_id(websiteCard.getId())
				 //.media_ids(mediaIds)
				.account(account).status("Hello Ads @JavaSDK").build();

		assertNotNull("campaign preview builder to string", Tweet.Preview.builder().toString());

		List<Preview> tweetPreviews = tweetPreview.create();
		assertTrue("tweet previews, from builder style now has an id generated",tweetPreview.toString().contains("id"));
		assertNotNull(tweetPreviews);
		assertThat(tweetPreviews, hasSize(3));

		for (Preview preview : tweetPreviews) {
			assertNotNull(preview.getPlatform());
			assertNotNull(preview.getPreview());
		}

		// test with media id, remove card id
		tweetPreview.setCard_id(null);

		List<String> mediaIds = new ArrayList<String>();
		mediaIds.add(IMAGE_MEDIA_ID);
		tweetPreview.setMedia_ids(mediaIds);
		tweetPreviews = tweetPreview.create();
		assertTrue("tweet previews, from builder style now has an id generated",
				tweetPreview.toString().contains("id"));

		// clean up
		websiteCard.delete();
		TwitterAdsClient.setSandbox(true);
	}

	@Test
	public void testTweet() throws Exception {
		TwitterAdsClient.setSandbox(false);
		TwitterAdsClient.setTrace(true);

		Account account = client.getAccount(ACCOUNT_ID);
		String userId = account.getPromotableUsers().get(0).getUser_id();

		List<String> mediaIds = new ArrayList<String>();
		mediaIds.add(IMAGE_MEDIA_ID);

		String randomStringStatus = RandomStringUtils.randomAlphabetic(64);

		Tweet tweet = Tweet.builder().account(account).status(randomStringStatus)
				// .media_ids(mediaIds)
				.as_user_id(userId).build();
		assertNotNull("tweet builder to string", Tweet.builder().toString());
		String id = tweet.create();
		Long tweetIdL = Long.parseLong(id);
		assertTrue("tweet now has an id generated", tweet.toString().contains("id"));

		// cleanup
		Tweet.destroy(tweetIdL);
		TwitterAdsClient.setSandbox(true);
	}

	@Test
	public void testFundingInstrument() throws Exception {

		Account account = client.getAccounts().get(0);
		FundingInstrument fundingInstrument = account.getFundingInstruments().get(0);
		assertNotNull(fundingInstrument.toString());
	}

	@Test
	public void testTargetingCriteria() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);


		Campaign campaign = new Campaign(account);
		campaign.setFunding_instrument_id(account.getFundingInstruments().get(0).getId());
		campaign.setDaily_budget_amount_local_micro(50000);
		campaign.setTotal_budget_amount_local_micro(500000);
		campaign.setName("Test Campaign");
		campaign.setPaused(true);

		Date now = Date.from(Instant.now());
		campaign.setStart_time(now);

		LocalDate weekAheadLocalDate = LocalDate.now().plusDays(7);
		Instant instant = weekAheadLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date weekFromToday = Date.from(instant);
		campaign.setEnd_time(weekFromToday);
		campaign.save();

		LineItem lineItem = new LineItem(account);

		lineItem.setCampaign_id(campaign.getId());
		lineItem.setName("First Line Item");
		lineItem.setProduct_type(Product.PROMOTED_TWEETS);

		// using enums
		Placement[] placements = new Placement[] { Placement.TWITTER_TIMELINE, Placement.TWITTER_SEARCH };
		lineItem.setPlacements(placements);
		lineItem.setObjective(Objective.WEBSITE_CLICKS);
		lineItem.setOptimization(Optimizations.DEFAULT);
		lineItem.setBid_unit(BidUnit.LINK_CLICK);
		lineItem.setBid_amount_local_micro(10000);
		lineItem.setPaused(true);
		lineItem.save();

		assertTrue("LineItem Conversation  now has an id generated", lineItem.toString().contains("id"));

		String lineItemId = lineItem.getId();

		TargetingCriteria targetingCriteria = TargetingCriteria.builder()
												.account(account)
												.targeting_type("PHRASE_KEYWORD")
												.targeting_value("AngryBirds")
												.line_item_id(lineItemId)
												.build();
		assertNotNull("targeting criteria to string", TargetingCriteria.builder().toString());
		targetingCriteria.save();

		assertTrue("Targeting Criteria now has an id generated", targetingCriteria.toString().contains("id"));

		// test load, reload
		String id = targetingCriteria.getId();
		TargetingCriteria loadedTargetingCriteria = new TargetingCriteria(account);
		loadedTargetingCriteria.setId(id);
		loadedTargetingCriteria.load();
		assertTrue("loaded TargetingCriteria is same as updated",
				loadedTargetingCriteria.getTargeting_type().equals("PHRASE_KEYWORD"));

		// cleanup
		targetingCriteria.delete();
		assertTrue("targeting criteria has been deleted", targetingCriteria.isDeleted());
		lineItem.delete();
		assertTrue("line item criteria has been deleted", lineItem.isDeleted());
		campaign.delete();
		assertTrue("campaign has been deleted", campaign.isDeleted());
		
		TwitterAdsClient.setSandbox(true);
	}
}
