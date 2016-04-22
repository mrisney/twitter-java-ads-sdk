package com.steelhouse.twitter.ads;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.LineItem;
import com.steelhouse.twitter.ads.campaign.Tweet;
import com.steelhouse.twitter.ads.client.ClientService;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.creative.AppDownloadCard;
import com.steelhouse.twitter.ads.creative.ImageAppDownloadCard;
import com.steelhouse.twitter.ads.creative.ImageConversationCard;
import com.steelhouse.twitter.ads.creative.LeadGenCard;
import com.steelhouse.twitter.ads.creative.PromotedAccount;
import com.steelhouse.twitter.ads.creative.PromotedTweet;
import com.steelhouse.twitter.ads.creative.Video;
import com.steelhouse.twitter.ads.creative.VideoAppDownloadCard;
import com.steelhouse.twitter.ads.creative.VideoConversationCard;
import com.steelhouse.twitter.ads.creative.WebsiteCard;
import com.steelhouse.twitter.ads.enums.AppCTA;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Placement;
import com.steelhouse.twitter.ads.enums.Product;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class CreativeTest {

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

	static String IMAGE_MEDIA_ID = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IMAGE_MEDIA_ID = uploadTestImage();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testWebsiteCard() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);

		WebsiteCard websiteCard = WebsiteCard.builder()
				.account(account)
				.name("TestCampaign")
				.image_media_id(IMAGE_MEDIA_ID)
				.website_title("Test Title")
				.website_url("http://www.twitter.com")
				.build();

		websiteCard.save();

		assertNotNull(WebsiteCard.builder().toString());
		websiteCard.save();
		assertTrue("Website Card now has an id generated", websiteCard.toString().contains("id"));

		websiteCard.setName("Updated Test Campaign");
		websiteCard.setWebsite_title("Updated Test Title");
		websiteCard.setWebsite_url("https://dev.twitter.com");
		websiteCard.save();
		
		// test load
		String id = websiteCard.getId();
		
		WebsiteCard savedWebsiteCard = new WebsiteCard(account);
		savedWebsiteCard.setId(id);
		savedWebsiteCard.load();
		
		
		assertTrue("Website Card name has been updated", savedWebsiteCard.getName().equals("Updated Test Campaign"));
		assertTrue("Website Card title has been updated", savedWebsiteCard.getWebsite_title().equals("Updated Test Title"));

		// test reload
		savedWebsiteCard.setName("LoadTestUpdateName");
		savedWebsiteCard.reload();
		assertFalse("Website Card image id has not been updated", savedWebsiteCard.getName().equals("LoadTestUpdateName"));
		
	    // clean up
		websiteCard.delete();
		assertTrue("WebSite card has been deleted", websiteCard.isDeleted());

	}

	@Test
	public void testVideo() throws Exception {
	
		Client uploadClient = ClientServiceFactory.getInstance().getClient();

		String domain = "https://upload.twitter.com";
		String uploadResource = "/1.1/media/upload.json";

		WebResource webResource = uploadClient.resource(domain);
		webResource.addFilter(new LoggingFilter());

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();

		params.add("command", "INIT");
		params.add("media_type", "video/mp4");
		params.add("media_category", "amplify_video");

		String filePath = "src/test/resources/doge.mp4";
		File file = new File(filePath);
		int bytes = (int) file.length();
		String totalBytes = Integer.toString(bytes);
		params.add("total_bytes", totalBytes);

		ClientResponse response = webResource.path(uploadResource).type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, params);

		String output = response.getEntity(String.class);
		JsonObject jsonObj = new JsonParser().parse(output).getAsJsonObject();
		String mediaId = jsonObj.get("media_id_string").getAsString();

		final FormDataMultiPart form = new FormDataMultiPart();
		form.field("command", "APPEND");
		form.field("media_id", mediaId);
		form.field("segment_index", "0");

		final FileDataBodyPart filePart = new FileDataBodyPart("media", file);
		final FormDataMultiPart multiPartForm = (FormDataMultiPart) form.bodyPart(filePart);

		response = webResource
				.path(uploadResource)
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, multiPartForm);

		MultivaluedMap<String, String> finalizeParams = new MultivaluedMapImpl();
		finalizeParams.add("command", "FINALIZE");
		finalizeParams.add("media_id", mediaId);

		response = webResource
				.path(uploadResource)
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, finalizeParams);

		TimeUnit.SECONDS.sleep(5);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("command", "STATUS");
		queryParams.add("media_id", mediaId);

		response = webResource
				.path(uploadResource)
				.queryParams(queryParams)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(ClientResponse.class);
		output = response.getEntity(String.class);

		System.out.println("STATUS ="+output);
	
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		
		Account account = client.getAccount("luyeg");

		Video video = Video.builder()
				.account(account)
				.description("test")
				.title("testtitle")
				.video_media_id(mediaId)
				.build();
		
		assertNotNull(Video.builder().toString());
		video.save();
		
		
		
		System.out.println("video to string" + video.toString());
		assertTrue("Video now has an id generated", video.toString().contains("id"));

		video.setDescription("updated test video description");
		video.setTitle("updated test video title");
		//video.save();

	
		//video.delete();

		VideoConversationCard videoConversationCard = VideoConversationCard.builder()
				.account(account)
				.name("test video card")
				.title("Show the world")
				.first_cta("#DogeLove")
				.first_cta_tweet("I heart this #vide")
				.second_cta("#WhoDoestLoveDoge")
				.second_cta_tweet("Me to#DogeRawks")
				.thank_you_text("thank you for watching")
				.thank_you_url("http://wwww.angrybirds.com")
				.video_id(video.getId())
				.image_media_id(IMAGE_MEDIA_ID)
				.build();
		
		assertNotNull(VideoConversationCard.builder().toString());

		//videoConversationCard.save();
		assertTrue("video conversation download card has an id generated",
				videoConversationCard.toString().contains("id"));

		videoConversationCard.setTitle("Show the world");
		videoConversationCard.setName("test video card");
		videoConversationCard.setFirst_cta("#DogeLove");
		videoConversationCard.setSecond_cta("#WhoDoestLoveDoge");
		videoConversationCard.setFirst_cta_tweet("I heart this #video");
		videoConversationCard.setSecond_cta_tweet("Me to#DogeRawks");
		videoConversationCard.setThank_you_text("thank you for watching");
		videoConversationCard.setThank_you_url("http://wwww.angrybirds.com");
		videoConversationCard.setImage_media_id(IMAGE_MEDIA_ID);
		videoConversationCard.setVideo_id(video.getId());
		// videoConversationCard.save();

		assertTrue("Video Conversation Card now has an id generated",
				videoConversationCard.toString().contains("name"));
		
		
	}

	@Test
	public void testPromotedTweat() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);

		String userId = account.getPromotableUsers().get(0).getUser_id();

		String randomString = RandomStringUtils.randomAlphabetic(64);
		log.debug("random string =" + randomString);

		Tweet tweet = Tweet.builder().account(account).status(randomString).as_user_id(userId).build();
		String tweetId = tweet.create();
		Long tweetIdL = Long.parseLong(tweetId);

		assertNotNull(tweetId);

		List<Long> tweetIds = new ArrayList<Long>();
		tweetIds.add(tweetIdL);

		String lineItemId = account.getLineItems().get(0).getId();
		assertNotNull(lineItemId);

		PromotedTweet promotedTweet = PromotedTweet.builder()
				.account(account)
				.line_item_id(lineItemId)
				.tweet_ids(tweetIdL)
				.paused(true)
				.build();

		assertNotNull(PromotedTweet.builder().toString());
		promotedTweet.save();

		System.out.println(promotedTweet.toString());
		assertTrue("Promoted Tweet has an id generated", promotedTweet.toString().contains("id"));

		// promotedTweet.setPaused(false);
		// promotedTweet.save();
		// assertFalse("promoted tweet has been set to not paused",
		// promotedTweet.isPaused());

		// cleanup
		//promotedTweet.delete();
		Tweet.destroy(tweetIdL);
		

		 //assertTrue("promoted tweet has been deleted",promotedTweet.isDeleted());

	}

	@Test
	public void testImageConversationCard() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);

		ImageConversationCard imageConversationCard = ImageConversationCard.builder().account(account)
				.name("Advertiser Image Conversation Card Sample").title("Tell the world").first_cta("#ShareNow")
				.first_cta_tweet("I #Heart @AdsAPI!").thank_you_text("thank you")
				.thank_you_url("https://example.com/thankyou").image_media_id(IMAGE_MEDIA_ID).build();

		assertNotNull(ImageConversationCard.builder().toString());
		// imageConversationCard.save();

		assertTrue("image conversation card has an id generated", imageConversationCard.toString().contains("id"));

		imageConversationCard.setName("Update");
		imageConversationCard.setTitle("Tell the world Update");
		imageConversationCard.setFirst_cta("#ShareNowUpdate");
		imageConversationCard.setFirst_cta_tweet("UpdateI #Heart @AdsAPI!");
		imageConversationCard.setThank_you_text("Thank YouUpdate");
		imageConversationCard.setThank_you_url("https://example.com/thankyou/update");

		// imageConversationCard.save();
		assertTrue("image app download card has an id generated", imageConversationCard.toString().contains("name"));

		// clean up
		// imageConversationCard.delete();
		// assertTrue("image app download card has been deleted",
		// imageConversationCard.isDeleted());
	}

	@Test
	public void testImageAppDownloadCard() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);

		ImageAppDownloadCard imageAppDownloadCard = ImageAppDownloadCard.builder()
				.account(account)
				.name("test")
				.app_country_code("US")
				.iphone_app_id("343200656")
				.app_cta(AppCTA.INSTALL_OPEN)
				.wide_app_image_media_id(IMAGE_MEDIA_ID)
				.build();

		assertNotNull(ImageAppDownloadCard.builder().toString());

		imageAppDownloadCard.save();

		assertTrue("image app download card has an id generated", imageAppDownloadCard.toString().contains("name"));

		imageAppDownloadCard.setName("updatedtest");
		imageAppDownloadCard.setApp_country_code("DE");
		imageAppDownloadCard.setIphone_app_id("343200656");
		imageAppDownloadCard.setApp_cta(AppCTA.INSTALL_OPEN);
		imageAppDownloadCard.setWide_app_image_media_id(IMAGE_MEDIA_ID);
		imageAppDownloadCard.save();

		assertTrue("image app download card has an id generated", imageAppDownloadCard.toString().contains("name"));

		// clean up
		imageAppDownloadCard.delete();
		assertTrue("image app download card has been deleted", imageAppDownloadCard.isDeleted());
	}

	@Test
	public void testPromotedAccount() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount("luyeg");

		// get the first funding instrument
		String fundingInstrumentId = account.getFundingInstruments().get(0).getId();
        
		Date now = Date.from(Instant.now());

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, +7);
		Date weekFromToday = cal.getTime();

		Campaign campaign = Campaign.builder()
				.account(account)
				.name("Test Campaign Builder")
				.funding_instrument_id(fundingInstrumentId)
				.currency("US")
				.daily_budget_amount_local_micro(50000)
				.total_budget_amount_local_micro(50000)
				.standard_delivery(true).start_time(now)
				.end_time(weekFromToday)
				.paused(true)
				.build();

		assertNotNull("campaign builder to string", Campaign.builder().toString());
		campaign.save();

		Placement[] placements = new Placement[] { Placement.ALL_ON_TWITTER };
		LineItem lineItem = LineItem.builder()
				.account(account)
				.campaign_id(campaign.getId())
				.name("Promoted Account Line Item")
				.product_type(Product.PROMOTED_ACCOUNT)
				.placements(placements)
				.objective(Objective.CUSTOM)
				.bid_amount_local_micro(10000)
				.paused(true)
				.build();
		
		lineItem.save();
		

	
		 PromotedAccount promotedAccount = PromotedAccount.builder()
		 .account(account)
		 .line_item_id(lineItem.getId())
		 .user_id("36716200")
		 .paused(false)
		 .build();
		 
		  assertNotNull(PromotedAccount.builder().toString());
		  promotedAccount.save(); 
		  assertTrue("promoted account has an id generated",promotedAccount.toString().contains("id"));
		  
		  String id = promotedAccount.getId();
		  
		  // test load
		  PromotedAccount savedPromotedAccount = new PromotedAccount(account);
		  savedPromotedAccount.setId(id);
		  savedPromotedAccount.load();
		  assertFalse("promoted account previously was paused",savedPromotedAccount.isPaused());
		  
		  
		  
		  // clean up 
		  lineItem.delete();
		  assertTrue("line item criteria has been deleted", lineItem.isDeleted());
		  campaign.delete();
		  assertTrue("campaign has been deleted", campaign.isDeleted());
	}

	@Test
	public void testAppDownloadCard() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);

		AppDownloadCard appDownloadCard = AppDownloadCard.builder().account(account).name("Advertiser App Card Sample")
				.app_country_code("US").iphone_app_id("333903271").app_cta(AppCTA.PLAY).build();

		assertNotNull(appDownloadCard.builder().toString());

		appDownloadCard.save();
		assertTrue("app download card has an id generated", appDownloadCard.toString().contains("id"));

		appDownloadCard.setName("UpdatedTestApp");
		appDownloadCard.setApp_cta(AppCTA.INSTALL_OPEN);

		appDownloadCard.save();
		assertTrue("app download card name has been updated", appDownloadCard.getName().equals("UpdatedTestApp"));
		assertTrue("app download card country code has been updated",
				appDownloadCard.getApp_cta().equals(AppCTA.INSTALL_OPEN));

		// test load, reload
		String id = appDownloadCard.getId();
		AppDownloadCard loadedAppDownloadCard = new AppDownloadCard(account);
		loadedAppDownloadCard.setId(id);
		loadedAppDownloadCard.reload();
		assertTrue("loaded app download card name is same as updated",
				loadedAppDownloadCard.getName().equals("UpdatedTestApp"));
		loadedAppDownloadCard.setCustom_app_description("Test Description");
		loadedAppDownloadCard.reload();
		assertNull(loadedAppDownloadCard.getCustom_app_description());

		// clean up
		appDownloadCard.delete();
		assertTrue("app download card has been deleted", appDownloadCard.isDeleted());
	}

	@Test
	public void testLeadGenCard() throws Exception {
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);

		String domain = "https://upload.twitter.com";
		String resource = "/1.1/media/upload.json";
		String filePath = "src/test/resources/test.image.800.200.png";

		ClientService clientService = ClientServiceFactory.getInstance();
		Client uploadClient = clientService.getClient();

		WebResource webResource = uploadClient.resource(domain);
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
		String mediaIdString = jsonObj.get("media_id_string").getAsString();

		LeadGenCard leadGenCard = LeadGenCard.builder().account(account).name("british_celebratory_card")
				.title("Some leadgen card title").image_media_id(mediaIdString).fallback_url("https://dev.twitter.com")
				.privacy_policy_url("https://twitter.com/privacy").cta("test").build();

		assertNotNull(LeadGenCard.builder().toString());
		leadGenCard.save();
		assertTrue("Leadgencard has an id generated", leadGenCard.toString().contains("id"));

		leadGenCard.setName("updated_british_celebratory_card");
		leadGenCard.setTitle("Some leadgen card title Updated");
		leadGenCard.setImage_media_id(mediaIdString);
		leadGenCard.setFallback_url("https://dev.twitter.com/api");
		leadGenCard.setPrivacy_policy_url("https://twitter.com/privacy");
		leadGenCard.setCta("UpdatedJoin our List");

		leadGenCard.save();
		assertTrue("lead gen card name has been updated",
				leadGenCard.getName().equals("updated_british_celebratory_card"));

		String id = leadGenCard.getId();
		leadGenCard = new LeadGenCard(account);
		leadGenCard.setId(id);
		leadGenCard.load();
		assertTrue("lead gen card name has been loaded",
				leadGenCard.getName().equals("updated_british_celebratory_card"));

		// clean up
		leadGenCard.delete();
		assertTrue("Lead gen card card has been deleted", leadGenCard.isDeleted());

	}

	@Test
	public void testVideoAppDownloadCard() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);

		VideoAppDownloadCard videoAppDownloadCard = VideoAppDownloadCard.builder().account(account)
				.name("Advertiser App Card Sample").iphone_app_id("333903271").app_country_code("US")
				.image_media_id(IMAGE_MEDIA_ID).video_id("f94fda2a-315f-4d7a-8f27-e63d1c350bd0").build();

		assertNotNull(videoAppDownloadCard.builder().toString());

		// videoAppDownloadCard.save();
		assertTrue("video app download card has an id generated", videoAppDownloadCard.toString().contains("name"));

		videoAppDownloadCard.setName("updatedtest");
		videoAppDownloadCard.setApp_country_code("DE");
		videoAppDownloadCard.setIphone_app_id("343200656");
		// videoAppDownloadCard.save();

		assertTrue("video app download card has an id generated", videoAppDownloadCard.toString().contains("name"));

		// clean up
		// videoAppDownloadCard.delete();
		// assertTrue("video app download card has been deleted",
		// videoAppDownloadCard.isDeleted());
	}
}