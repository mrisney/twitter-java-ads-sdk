package com.steelhouse.twitter.ads.campaign;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.enums.Platform;
import com.steelhouse.twitter.ads.resource.Resource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tweet extends Resource<Tweet> {

	private String id;
	private String account_id;
	private String status;
	private String as_user_id;
	private String trim_user;
	private List<String> media_ids;
	private String video_id;
	private String video_title;
	private String video_description;
	private String video_cta;
	private String video_cta_value;

	public static String TWEET_CREATE = "/1/accounts/{account_id}/tweet";
	private final static Logger log = LoggerFactory.getLogger(Tweet.class);

	public Tweet(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public Tweet(Account account, String status, String as_user_id, List<String> media_ids, String video_id,
			String video_title, String video_description, String video_cta, String video_cta_value) {

		this.setAccount_id(account.getId());
		this.status = status;
		this.as_user_id = as_user_id;
		this.media_ids = media_ids;
		this.video_id = video_id;
		this.video_title = video_title;
		this.video_description = video_description;
		this.video_cta = video_cta;
		this.video_cta_value = video_cta_value;
	}

	public String create() throws Exception {
		String resource = TWEET_CREATE.replace("{account_id}", account_id);
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();

		params.add("status", status);

		if (null != media_ids) {
			params.add("media_ids", Joiner.on(',').join(media_ids));
		}
		if (null != as_user_id) {
			params.add("as_user_id", as_user_id);
		}

		log.debug("calling twitter endpoint : " + getResource().path(resource).toString());
		Map map = getResource().path(resource).type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON_TYPE).post(Map.class, params);
		log.debug(map.toString());
		return (String) map.get("id_str");
	}

	public static void destroy(Long tweetId) {
	
		Client client = ClientServiceFactory.getInstance().getClient();
	
		String domain = "https://api.twitter.com/";
		String resourcePath = "/1.1/statuses/destroy/" + tweetId.toString() + ".json";

		WebResource webResource = client.resource(domain);
		
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();

		params.add("id", tweetId.toString());

		ClientResponse response = webResource
				.path(resourcePath)
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, params);
		String output = response.getEntity(String.class);
		log.info(output);
	}

	@Getter
	@Setter
	@ToString
	public static class Preview {

		private String account_id;
		@Setter(AccessLevel.NONE)
		private Platform platform;
		@Setter(AccessLevel.NONE)
		private String preview;
		private String status;
		private String as_user_id;
		private List<String> media_ids;
		private String card_id;
		private String preview_target;

		protected final static String TWEET_PREVIEW = "/0/accounts/{account_id}/tweet/preview";
		protected final static String TWEET_ID_PREVIEW = "/0/accounts/{account_id}/tweet/preview/{id}";

		@Builder
		public Preview(Account account, String status, String as_user_id, List<String> media_ids, String card_id,
				String preview_target) {

			setAccount_id(account.getId());
			setStatus(status);
			setAs_user_id(as_user_id);
			setMedia_ids(media_ids);
			setCard_id(card_id);
			setPreview_target(preview_target);
		}

		public List<Tweet.Preview> create() {
			List<Tweet.Preview> previews = null;
			try {

				String resource = TWEET_PREVIEW.replace("{account_id}", account_id);
				MultivaluedMap<String, String> params = new MultivaluedMapImpl();
				status = URLEncoder.encode(status, "UTF-8").replaceAll("\\+", "%20");
				params.add("status", status);

				if (null != card_id) {
					params.add("card_id", card_id);
				}

				if (null != preview_target) {
					params.add("preview_target", preview_target);
				}

				if (null != media_ids) {
					params.add("media_ids", Joiner.on(',').join(media_ids));
				}
				previews = getResource()
						.path(resource)
						.queryParams(params)
						.accept(MediaType.APPLICATION_JSON_TYPE)
						.get(new GenericType<List<Tweet.Preview>>() {});
			} catch (Exception e) {
				log.error(e.toString());
			}
			return previews;
		}
	}
}