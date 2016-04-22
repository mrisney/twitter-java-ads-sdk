package examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Tweet;
import com.steelhouse.twitter.ads.campaign.Tweet.Preview;
import com.steelhouse.twitter.ads.creative.PromotedTweet;
import com.steelhouse.twitter.ads.creative.WebsiteCard;

public class WebsiteCardExample {

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
	final static String SANDBOX_ACCOUNT_ID = "gq10m0";

	public static void main(String[] args) throws Exception {

		TwitterAdsClient.setTrace(true);
		TwitterAdsClient.setSandbox(false);

		// get the first account
		Account account = client.getAccount(ACCOUNT_ID);
		
	

		// WebsiteCard websiteCard = new WebsiteCard(account);
		WebsiteCard websiteCard = WebsiteCard.builder()
				.account(account)
				.name("Ryan's first WebSite CARD")
				.website_title("Testssssss")
				.website_url("http://www.steelhouse.com")
				.image_media_id("715981645640118272")
				.build();

		System.out.println(websiteCard.toString());
		websiteCard.save();
		System.out.println(websiteCard.getPreview_url());

		Tweet.Preview examplePreview = Tweet
				.Preview.builder()
				.account(account)
				.status("Hello")
				.card_id(websiteCard.getId())
				.build();

		List<Preview> previews = examplePreview.create();
		for (Preview preview : previews) {
			System.out.println("PLATFORM = " + preview.getPlatform());
			System.out.println("PREVIEW = " + preview.getPreview());

		}

		String status = "Hello World #" + websiteCard.getPreview_url();
		List<String> mediaIds = new ArrayList<String>();
		mediaIds.add(websiteCard.getId());
		Tweet tweet = Tweet.builder()
				.account(account)
				.status(status)
				.as_user_id("4846476000")
				.build();
		
		String tweetId = tweet.create();
		String lineItemId = account.getLineItems().get(0).getId();
		Long tweetIdL = Long.parseLong(tweetId);
		
		

		 PromotedTweet promotedTweet = PromotedTweet.builder()
				 			.account(account)
				 			.line_item_id(lineItemId)
				 			.tweet_ids(tweetIdL)
				 			.build(); 
		promotedTweet.save();
		 
		// websiteCard.delete();
		//System.out.println(websiteCard.toString());

		//WebsiteCard websiteCardDel = new WebsiteCard(account);
		//websiteCardDel.setId("1kgqi");
		//websiteCardDel.delete();

	}

}
