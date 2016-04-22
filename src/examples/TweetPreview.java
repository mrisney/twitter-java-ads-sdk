package examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Tweet;
import com.steelhouse.twitter.ads.campaign.Tweet.Preview;

public class TweetPreview {

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


	public static void main(String[] args) throws Exception {

		// does not seem to work on sandbox
		client.setSandbox(false);

		// get the first account
		Account account = client.getAccount(ACCOUNT_ID);

		// turn on trace
		client.setTrace(true);

		// load media item(s), get the uid -- see the MediaUpload.java example
		//String[] mediaIds = new String[] { "709866515726622721" };
		List<String> mediaIds = new ArrayList<String>();
		mediaIds.add("709866515726622721");

		Tweet.Preview preview = Tweet.Preview
				.builder()
				.account(account)
				.status("Hello Ads @JavaSDK")
				.media_ids(mediaIds)
				.build();
		List<Preview> previews = preview.create();
		
		// Old Skool - Iterator style
		Iterator<Preview> previewsIterator = previews.iterator();
		while (previewsIterator.hasNext()) {
			System.out.println(previewsIterator.next().toString());
		}

		for (Preview createdPreview : previews) {
			System.out.println("PLATFORM = "+ createdPreview.getPlatform());
			System.out.println("PREVIEW = "+ createdPreview.getPreview());
		}
		
	Tweet tweet = Tweet.builder().account(account).status("Hello Ads @JavaSDK").as_user_id("4846476000").build();
	tweet.create();
	}
}
