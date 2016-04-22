package examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.Tweet;

public class CampaignUtils {
	
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

	public static void main(String[] args)  throws Exception{
		
		client.setTrace(true);
		client.setSandbox(false);
		
		// get the first account
		Account account = client.getAccount(ACCOUNT_ID);
		//Long tweetId = Long.parseLong("712387868958728192");
		//Tweet.destroy(tweetId);
		
		Campaign campaign = account.getCampaigns().get(0);
	
		System.out.println(campaign.toString());
		
		//campaign.load();
		
		//campaign.setName("Marc's First Campaign");
		
		//campaign.setTotal_budget_amount_local_micro(500);
		//campaign.setPaused(true);
	
		//campaign.save();
	//	System.out.println("campaign name = "+ campaign.getName());
		
		/*
		
		for (Campaign campaign : account.getCampaigns()){
			campaign.delete();
		}
		 */
		
		/*
		String fundingInstrumentId = account.getFundingInstruments().get(0).getId();
		
		Date now = Date.from(Instant.now());
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, +7);
		Date weekFromToday = cal.getTime();
		
		Campaign campaign = Campaign.builder()
				.account(account)
				.funding_instrument_id(fundingInstrumentId)
				.currency("US")
				.daily_budget_amount_local_micro(50000)
				.total_budget_amount_local_micro(50000)
				.name("Test")
				.paused(true)
				.standard_delivery(true)
				.start_time(now)
				.end_time(weekFromToday)
				.build();
		
		campaign.save();
		System.out.println(campaign.toString());
		campaign.setName("TEST OF A NEW");
	
		//campaign.setDuration_in_days(7);
		//campaign.setFrequency_cap(12);
	
		
		cal.add(Calendar.DAY_OF_MONTH, +1);
		Date tomorrow = cal.getTime();

		//campaign.setStart_time(tomorrow);
		
		cal.add(Calendar.DAY_OF_MONTH, +1);
		Date dayAfter = cal.getTime();
		
		//campaign.setFrequency_cap(5);
		//campaign.setEnd_time(dayAfter);
		campaign.setPaused(false);
		campaign.setStandard_delivery(true);
		//campaign.setCurrency("DE");
		campaign.save();
		System.out.println(campaign.toString());
		campaign.delete();
		*/
	}

}
