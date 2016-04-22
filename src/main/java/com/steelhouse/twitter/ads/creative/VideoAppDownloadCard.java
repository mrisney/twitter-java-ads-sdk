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
public class VideoAppDownloadCard extends Persistence<VideoAppDownloadCard> {

	// read-only
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
	@Updatable
	private String name;
	@Setter
	@Updatable
	private String app_country_code;
	@Setter
	@Updatable
	private String iphone_app_id;
	@Setter
	@Updatable
	private String iphone_deep_link;
	@Setter
	@Updatable
	private String ipad_app_id;
	@Setter
	@Updatable
	private String ipad_deep_link;
	@Setter
	@Updatable
	private String googleplay_app_id;
	@Setter
	@Updatable
	private String googleplay_deep_link;
	@Setter
	@Updatable
	private String app_cta;
	@Setter
	@Updatable
	private String image_media_id;
	@Setter
	@Updatable
	private String video_id;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/cards/video_app_download";
	protected final static String RESOURCE = "/0/accounts/{account_id}/cards/video_app_download/{id}";

	public VideoAppDownloadCard(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public VideoAppDownloadCard(Account account, String name, String app_country_code, String iphone_app_id,
			String iphone_deep_link, String ipad_app_id, String ipad_deep_link, String googleplay_app_id,
			String googleplay_deep_link, String app_cta, String image_media_id, String video_id) {

		this.setAccount_id(account.getId());
		this.name = name;
		this.app_country_code = app_country_code;
		this.iphone_app_id = iphone_app_id;
		this.iphone_deep_link = iphone_deep_link;
		this.ipad_app_id = ipad_app_id;
		this.ipad_deep_link = ipad_deep_link;
		this.googleplay_app_id = googleplay_app_id;
		this.googleplay_deep_link = googleplay_deep_link;
		this.app_cta = app_cta;
		this.image_media_id = image_media_id;
		this.video_id = video_id;
	}
}