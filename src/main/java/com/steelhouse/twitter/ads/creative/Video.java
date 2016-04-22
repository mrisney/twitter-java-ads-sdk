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
public class Video extends Persistence<Video> {
	
	private String approval_status;
	private Boolean tweeted;
	private Boolean ready_to_tweet;
	private Double duration;
	private String[] reasons_not_servable;
	private String preview_url;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;
	
	// writable
	@Setter
	// @Updatable(required = true)
	private String id;
	// @Updatable(required = true)
	private String account_id;
	@Setter
	@Updatable
	private String title;
	@Setter
	@Updatable
	private String description;
	
	
	@Setter
	@Updatable
	private String video_media_id;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/videos";
	protected final static String RESOURCE = "/0/accounts/{account_id}/videos/{id}";

	public Video(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public Video(Account account, String title, String description, String video_media_id) {
		this.setAccount_id(account.getId());
		this.title = title;
		this.description = description;
		this.video_media_id = video_media_id;
	}

}
