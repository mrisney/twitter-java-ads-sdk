package com.steelhouse.twitter.ads;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.audience.TailoredAudience;
import com.steelhouse.twitter.ads.campaign.AppList;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.campaign.FundingInstrument;
import com.steelhouse.twitter.ads.campaign.LineItem;
import com.steelhouse.twitter.ads.campaign.PromotableUser;
import com.steelhouse.twitter.ads.creative.Video;
import com.steelhouse.twitter.ads.resource.Cursor;
import com.steelhouse.twitter.ads.resource.Resource;
import com.sun.jersey.api.client.GenericType;

import lombok.Getter;
import lombok.ToString;

/**
 * The Ads API Account class which functions as a context container for the
 * advertiser and nearly all interactions with the API.
 **/

@Getter
@ToString
public class Account extends Resource<Account> {

	// read only properties
	private String id;
	private String name;
	private String salt;
	private String timezone;
	private Boolean deleted;
	private Boolean timezone_switch_at;
	private Date created_at;
	private Date updated_at;

	protected static final String RESOURCE_COLLECTION = "/1/accounts";
	protected static final String CAMPAIGNS = "/0/accounts/{account_id}/campaigns";
	protected static final String FEATURES = "/0/accounts/{account_id}/features";
	protected static final String SCOPED_TIMELINE = "/0/accounts/{account_id}/scoped_timeline";

	private static final Logger log = LoggerFactory.getLogger(Account.class);

	public Account() {
		registerObject(this);
	}

	public List<Account> getAllAcounts() throws Exception {
		String resource = RESOURCE_COLLECTION;
		log.debug("calling twitter endpoint : " + getResource().path(resource).toString());
		return getResource().path(resource).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Account>>() {});
	}

	/**
	 * @return a collection of campaigns available to the current account.
	 */
	@SuppressWarnings("rawtypes")
	public List<Campaign> getCampaigns() throws Exception {
		String resource = CAMPAIGNS.replace("{account_id}", id);
		log.debug("calling twitter endpoint : " + getResource().path(resource).toString());
		List<Campaign> campaigns = getResource().path(resource).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Campaign>>() {});
		campaigns.forEach(c -> c.setAccount_id(id));
		return campaigns;
	}

	public List<String> getFeatures() throws Exception {
		String resource = FEATURES.replace("{account_id}", id);
		log.debug("calling twitter endpoint : " + getResource().path(resource).toString());
		return getResource().path(resource).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<String>>() {});
	}

	/**
	 * 
	 * @return a collection of promotable users available to the current account.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<PromotableUser> getPromotableUsers() throws Exception {
		
		String resourceCollection = (String) FieldUtils.readStaticField(PromotableUser.class, "RESOURCE_COLLECTION", true);
		String resourcePath = resourceCollection.replace("{account_id}", id);
		
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		List<PromotableUser> promotableUsers = getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<PromotableUser>>() {});
		promotableUsers.forEach(p -> p.setAccount_id(id));
		return promotableUsers;
	}

	/**
	 * @return a collection of funding instruments available to the current
	 *         account.
	 */
	@SuppressWarnings("rawtypes")
	public List<FundingInstrument> getFundingInstruments() throws Exception {
		
		String resourceCollection = (String) FieldUtils.readStaticField(FundingInstrument.class, "RESOURCE_COLLECTION", true);
		String resourcePath = resourceCollection.replace("{account_id}", id);

		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		List<FundingInstrument> fundingInstruments = getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<FundingInstrument>>() {});
		fundingInstruments.forEach(f -> f.setAccount_id(id));
		return fundingInstruments;
	}

	/*
	 * @return a collection of funding instruments available to the current
	 * account.
	 */

	@SuppressWarnings("rawtypes")
	public List<LineItem> getLineItems() throws Exception {
		
		String resourceCollection = (String) FieldUtils.readStaticField(LineItem.class, "RESOURCE_COLLECTION", true);
		String resourcePath = resourceCollection.replace("{account_id}", id);
		
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		List<LineItem> lineItems = getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<LineItem>>() {});
		lineItems.forEach(l -> l.setAccount_id(id));
		return lineItems;
	}

	/*
	 * @return a collection of app lists available to the current account.
	 */

	@SuppressWarnings("rawtypes")
	public List<AppList> getAppList() throws Exception {
		
		String resourceCollection = (String) FieldUtils.readStaticField(AppList.class, "RESOURCE_COLLECTION", true);
		String resourcePath = resourceCollection.replace("{account_id}", id);
		
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		List<AppList> appLists = getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<AppList>>() {});
		appLists.forEach(a -> a.setAccount_id(id));
		return appLists;
	}

	/*
	 * @return a collection of tailored audiences available to the current
	 * account.
	 */

	@SuppressWarnings("rawtypes")
	public List<TailoredAudience> getTailoredAudiences() throws Exception {
	
		String resourceCollection = (String) FieldUtils.readStaticField(TailoredAudience.class, "RESOURCE_COLLECTION", true);
		String resourcePath = resourceCollection.replace("{account_id}", id);
			
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		List<TailoredAudience> tailoredAudiences = getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<TailoredAudience>>() {});
		tailoredAudiences.forEach(ta -> ta.setAccount_id(id));
		return tailoredAudiences;
	}

	/*
	 * @return a collection of videos available to the current account.
	 */

	@SuppressWarnings("rawtypes")
	public List<Video> getVideos() throws Exception {
		
		String resourceCollection = (String) FieldUtils.readStaticField(Video.class, "RESOURCE_COLLECTION", true);
		String resourcePath = resourceCollection.replace("{account_id}", id);
			
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		List<Video> videos = getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Video>>() {});
		videos.forEach(v -> v.setAccount_id(id));
		return videos;
	}

	/**
	 * Returns the most recent promotable Tweets created by one or more
	 * specified Twitter users
	 * 
	 */

	public List<ScopedTimeLine> getScopedTimelines() throws Exception {
		String resourcePath = SCOPED_TIMELINE.replace("{account_id}", id);
		log.debug("calling twitter endpoint : " + getResource().path(resourcePath).toString());
		return getResource().path(resourcePath).accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<ScopedTimeLine>>() {});

	}

	public Cursor getAll(Object object) {
		return new Cursor(object);
	}
}
