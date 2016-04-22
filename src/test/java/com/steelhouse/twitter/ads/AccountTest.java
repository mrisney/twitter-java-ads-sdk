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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.audience.TailoredAudience;
import com.steelhouse.twitter.ads.campaign.AppList;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.FundingInstrument;
import com.steelhouse.twitter.ads.campaign.LineItem;
import com.steelhouse.twitter.ads.campaign.PromotableUser;
import com.steelhouse.twitter.ads.creative.Video;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Placement;
import com.steelhouse.twitter.ads.enums.Product;
import com.steelhouse.twitter.ads.enums.TimeLineScope;
import com.steelhouse.twitter.ads.resource.Cursor;

public class AccountTest {
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
	public void testGetAllAcounts() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		List<Account> accounts = client.getAccounts();
		assertThat("These should be at least one account", accounts.size(), is(greaterThan(0)));
		String id = accounts.get(0).getId();
		
	}

	@Test
	public void testGetFundingInstruments() throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);

		List<FundingInstrument> fundingInstruments = account.getFundingInstruments();
		assertThat("These should be at least one fundingInstrument", fundingInstruments.size(), is(greaterThan(0)));
	}

	@Test
	public void testGetCampaigns() throws Exception {
		

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);
		

		// create your campaign
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

		List<Campaign> campaigns = account.getCampaigns();
		assertThat("These should be at least one campaign", campaigns.size(), is(greaterThan(0)));
		campaign.delete();
	}

	@Test
	public void testGetFeatures() throws Exception {
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);
		
		List<String> features = account.getFeatures();
		assertThat(features.size(), is(greaterThan(0)));
	}

	@Test
	public void testGetLineItems() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);
		

		Campaign campaign = new Campaign(account);
		campaign.setFunding_instrument_id(account.getFundingInstruments().get(0).getId());
		campaign.setDaily_budget_amount_local_micro(50000);
		campaign.setTotal_budget_amount_local_micro(500000);
		campaign.setName("My New Campaign");
		campaign.setPaused(true);

		Date now = Date.from(Instant.now());
		campaign.setStart_time(now);

		LocalDate weekAheadLocalDate = LocalDate.now().plusDays(7);
		Instant instant = weekAheadLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date weekFromToday = Date.from(instant);
		campaign.setEnd_time(weekFromToday);

		campaign.save();

		LineItem newLineItem = new LineItem(account);

		newLineItem.setCampaign_id(campaign.getId());
		newLineItem.setName("First Line Item");
		newLineItem.setProduct_type(Product.PROMOTED_TWEETS);

		// using enums
		Placement[] placements = new Placement[] { Placement.TWITTER_TIMELINE, Placement.TWITTER_SEARCH };
		newLineItem.setPlacements(placements);
		newLineItem.setObjective(Objective.TWEET_ENGAGEMENTS);
		newLineItem.setBid_amount_local_micro(10000);
		newLineItem.setPaused(true);
		newLineItem.save();

		List<LineItem> lineItems = account.getLineItems();
		assertThat(lineItems.size(), is(greaterThan(0)));

		LineItem lineItem = lineItems.get(0);
		assertNotNull(lineItem);
		assertNotNull(lineItem.toString());
		assertEquals(lineItem.getAccount_id(), account.getId());
		assertTrue("lineItem user now has an id generated", lineItem.toString().contains("id"));
		campaign.delete();
	}

	@Test
	public void testGetApplist() throws Exception {
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);
		
		
		// AppList appList = new AppList(account);
		// List<String> ids = Arrays.asList("343200656");
		// appList.create("AngryBirds", ids);

		List<AppList> appLists = account.getAppList();
		assertThat(appLists.size(), is(greaterThan(0)));
	}

	@Test
	public void testGetTailoredAudiences() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);
		
		
		List<TailoredAudience> tailoredAudiencs = account.getTailoredAudiences();
		assertNotNull(tailoredAudiencs);
		// assertThat(tailoredAudiencs.size(), is(greaterThan(0)));

	}

	@Test
	public void testGetVideos() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);
		
		
		List<Video> videos = account.getVideos();
		assertNotNull(videos);
		assertThat(videos.size(), is(greaterThan(0)));
	}

	@Test
	public void testGetAlll() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);
		
		
		Campaign campaign = new Campaign(account);
		Cursor cursor = account.getAll(campaign);
		assertNotNull(cursor);
		//assertThat(cursor.count(), is(greaterThan(0)));
	}

	@Test
	public void testGetPromotableUsers() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(true);
		Account account = client.getAccount(SANDBOX_ACCOUNT_ID);
		
		
		List<PromotableUser> promotableUsers = account.getPromotableUsers();
		assertThat(promotableUsers.size(), is(greaterThan(0)));

		PromotableUser promotableUser = promotableUsers.get(0);
		assertNotNull(promotableUser);
		assertNotNull(promotableUser.toString());
		assertEquals(promotableUser.getAccount_id(), account.getId());

	}

	@Test
	public void testGetScopedTimeline() throws Exception {
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		Account account = client.getAccount(ACCOUNT_ID);
		
		String id = "";
		List<ScopedTimeLine> timeLineScopes = account.getScopedTimelines();
		for (ScopedTimeLine timeLine : timeLineScopes){
			assertTrue("timeline now has an id generated", timeLine.toString().contains("id"));
			assertNotNull(timeLine.getUser().getId());
			id = timeLine.getId_str();
		}
		assertNotNull(timeLineScopes);
		
		// test load
		
		ScopedTimeLine loadedTimeLine = new ScopedTimeLine(account);
		loadedTimeLine.setId(id);
		//loadedTimeLine.load();
		
		
	}
}
