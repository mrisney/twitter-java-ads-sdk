package com.steelhouse.twitter.ads.creative;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.annotations.Updatable;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class VideoConversationCard extends Persistence<VideoConversationCard> {


	private Boolean servable;
	private String preview_url;
	private String video_url;
	private String video_poster_url;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	// writable
	@Setter
	@Updatable(required = true)
	private String id;
	@Updatable(required = true)
	private String account_id;
	@Setter
	@Updatable(required = true)
	private String name;
	@Setter
	@Updatable(required = true)
	private String title;
	@Setter
	@Updatable(required = true)
	private String first_cta;
	@Setter
	@Updatable(required = true)
	private String first_cta_tweet;
	@Setter
	@Updatable
	private String second_cta;
	@Setter
	@Updatable
	private String second_cta_tweet;
	@Setter
	@Updatable(required = true)
	private String thank_you_text;
	@Setter
	@Updatable
	private String thank_you_url;
	@Setter
	@Updatable(required = true)
	private String image_media_id;
	@Setter
	@Updatable(required = true)
	private String video_id;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/cards/video_conversation";
	protected final static String RESOURCE = "/0/accounts/{account_id}/cards/video_conversation/{id}";

	public VideoConversationCard(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public VideoConversationCard(Account account, String name, String title, String first_cta, String first_cta_tweet,
			String second_cta, String second_cta_tweet, String thank_you_text, String thank_you_url,
			String image_media_id, String video_id) {

		this.setAccount_id(account.getId());
		this.name = name;
		this.title = title;
		this.first_cta = first_cta;
		this.first_cta_tweet = first_cta_tweet;
		this.second_cta = second_cta;
		this.second_cta_tweet = second_cta_tweet;
		this.thank_you_text = thank_you_text;
		this.thank_you_url = thank_you_url;
		this.image_media_id = image_media_id;
	}
}
