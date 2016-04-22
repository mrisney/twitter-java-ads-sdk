package com.steelhouse.twitter.ads.creative;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.annotations.Updatable;
import com.steelhouse.twitter.ads.enums.AppCTA;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString; 

@Getter
@ToString
public class AppDownloadCard extends Persistence<AppDownloadCard> {

	// read-only
	private String preview_url;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	// writable
	@Setter
	private String id;
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
	private AppCTA app_cta;
	@Setter
	@Updatable
	private String custom_icon_media_id;
	@Setter
	@Updatable
	private String custom_app_description;

	protected final static String RESOURCE_COLLECTION = "/1/accounts/{account_id}/cards/app_download";
	protected final static String RESOURCE = "/1/accounts/{account_id}/cards/app_download/{id}";

	public AppDownloadCard(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public AppDownloadCard(Account account, String name, String app_country_code, String iphone_app_id,
			String iphone_deep_link, String ipad_app_id, String ipad_deep_link, String googleplay_app_id,
			String googleplay_deep_link, AppCTA app_cta, String custom_icon_media_id, String custom_app_description) {

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
		this.custom_icon_media_id = custom_icon_media_id;
		this.custom_app_description = custom_app_description;
	}
}