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
public class WebsiteCard extends Persistence<WebsiteCard> {

	private String preview_url;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	// writable
	private String account_id;
	@Setter
	private String id;
	@Setter
	@Updatable(required = true, maxLength = 80)
	private String name;
	@Setter
	@Updatable(required = true, maxLength = 70)
	private String website_title;
	@Setter
	@Updatable(required = true)
	private String website_url;
	@Setter
	@Updatable(required = true)
	private String image_media_id;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/cards/website";
	protected final static String RESOURCE = "/0/accounts/{account_id}/cards/website/{id}";

	public WebsiteCard(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public WebsiteCard(Account account, String name, String website_title, String website_url, String image_media_id) {
		this.setAccount_id(account.getId());
		this.name = name;
		this.website_title = website_title;
		this.website_url = website_url;
		this.image_media_id = image_media_id;
	}
}
