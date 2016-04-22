package examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.FundingInstrument;
import com.steelhouse.twitter.ads.campaign.LineItem;
import com.steelhouse.twitter.ads.campaign.TargetingCriteria;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.enums.BidUnit;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Optimizations;
import com.steelhouse.twitter.ads.enums.Placement;
import com.steelhouse.twitter.ads.enums.Product;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class QuickStart {

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

	final static  TwitterAdsClient client = getTwitterAdsClient();
	final static String ACCOUNT_ID = "18ce54aq4d5";
	

	public static void main(String args[]) throws Exception {

		// initialize the twitter ads api client
		//TwitterAdsClient client = new TwitterAdsClient(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_SECRET);
		
		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);
		
		Account account = client.getAccount(ACCOUNT_ID);
		
		// get the first financial instrument
		FundingInstrument fundingInstrument = account.getFundingInstruments().get(0);

		// create your campaign
		Date now = Date.from(Instant.now());

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, +7);
		Date weekFromToday = cal.getTime();

		Campaign campaign = Campaign.builder()
				.account(account)
				.name("Jason Demo Sprint Test Campaign")
				.funding_instrument_id(fundingInstrument.getId())
				.currency("US")
				.daily_budget_amount_local_micro(5000000)
				.total_budget_amount_local_micro(5000000)
				.standard_delivery(true)
				.start_time(now)
				.end_time(weekFromToday)
				.paused(true)
				.build();

	
		campaign.save();
		System.out.println("campaign newly created : " + campaign.toString());

		LineItem lineItem = new LineItem(account);
		
		lineItem.setCampaign_id(campaign.getId());
		lineItem.setName("Targeted  Line Item");
		lineItem.setProduct_type(Product.PROMOTED_TWEETS);

		// using enums
		Placement[] placements = new Placement[] { Placement.TWITTER_TIMELINE, Placement.TWITTER_SEARCH };
		lineItem.setPlacements(placements);
		lineItem.setObjective(Objective.WEBSITE_CLICKS);
		lineItem.setBid_amount_local_micro(100000);
		lineItem.setOptimization(Optimizations.DEFAULT);
		lineItem.setBid_unit(BidUnit.LINK_CLICK);
		lineItem.setPaused(true);
		lineItem.save();

		// Get the location	
		
		
		WebResource webResource = ClientServiceFactory.getInstance().getResource();
		webResource.addFilter(new LoggingFilter());
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("location_type", "CITY");
		queryParams.add("q", "Marina Del Ray CA, CA, USA");
		queryParams.add("country_code", "US");
		queryParams.add("city_code", "Marina Del Ray");
	
		String resourcePath = "/0/targeting_criteria/locations";
		
		List<Map> maps = webResource
				.path(resourcePath)
				.queryParams(queryParams)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(new GenericType<List<Map>>() {});
		
		Map targetingCriteriaMap  = maps.get(0);
		String targetingValue = (String) targetingCriteriaMap.get("targeting_value");		
		String targetingType = (String) targetingCriteriaMap.get("targeting_type");		
		System.out.println(targetingType +" : " +targetingValue );

		
		// add location  targeting criteria
		TargetingCriteria locationTargetingCriteria = new TargetingCriteria(account);
		locationTargetingCriteria.setLine_item_id(lineItem.getId());
		locationTargetingCriteria.setTargeting_type(targetingType);
		locationTargetingCriteria.setTargeting_value(targetingValue);
		locationTargetingCriteria.save();

		
		// add an age targeting criteria
		TargetingCriteria genderTargetingCriteria = new TargetingCriteria(account); 
		genderTargetingCriteria.setLine_item_id(lineItem.getId());
		genderTargetingCriteria.setTargeting_type("GENDER");
		genderTargetingCriteria.setLine_item_id(lineItem.getId());
		genderTargetingCriteria.setTargeting_value("2");
		genderTargetingCriteria.save();
		
	}
}