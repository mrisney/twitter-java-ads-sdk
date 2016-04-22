package com.steelhouse.twitter.ads;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.campaign.FundingInstrument;
import com.steelhouse.twitter.ads.client.ClientService;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.resource.Resource;
import com.sun.jersey.api.client.GenericType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class TwitterAdsClient extends Resource<TwitterAdsClient> {

	/**
	 * 
	 * Creates a new Twitter Ads API client instance.
	 * 
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 * @param accessTokenSecret
	 * 
	 */

	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private boolean sandbox;
	private boolean trace;

	protected final static String RESOURCE = "/1/accounts/{account_id}";
	protected final static String RESOURCE_COLLECTION = "/1/accounts";
	private static Logger log = LoggerFactory.getLogger(TwitterAdsClient.class);

	public TwitterAdsClient(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {

		registerObject(this);

		ClientServiceFactory.consumerKey = consumerKey;
		ClientServiceFactory.consumerSecret = consumerSecret;
		ClientServiceFactory.accessToken = accessToken;
		ClientServiceFactory.accessTokenSecret = accessTokenSecret;

		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
	}

	public static void setSandbox(boolean enabled) {
		log.debug("settting sandbox = " + enabled);
		ClientServiceFactory.getInstance().setSandbox(enabled);
	}
	
	public static void setDomain(String domain) {
		log.debug("settting sandbox domain to : = " + domain);
		ClientServiceFactory.getInstance().setDomain(domain);
	}

	public static void setTrace(boolean enabled) {
		log.debug("setting trace = " + enabled);
		ClientServiceFactory.getInstance().setTrace(enabled);
	}

	public List<Account> getAccounts() throws Exception {
		log.debug("calling twitter endpoint : " + getResource().path(RESOURCE_COLLECTION).toString());
		return getResource().path(RESOURCE_COLLECTION).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Account>>() {});
	}

	public Account getAccount(String id) throws Exception {
		String resourcePath = RESOURCE.replace("{account_id}", id);
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		return getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
	}
}
