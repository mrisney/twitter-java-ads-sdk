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
public class LeadGenCard extends Persistence<LeadGenCard> {

	// read-only
	private String preview_url;
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
	@Updatable(maxLength = 80)
	private String name;
	@Setter
	@Updatable(required = true)
	private String image_media_id;
	@Setter
	@Updatable(maxLength = 20)
	private String cta;
	@Setter
	@Updatable(maxLength = 255)
	private String fallback_url;
	@Setter
	@Updatable(maxLength = 255)
	private String privacy_policy_url;
	@Setter
	@Updatable(maxLength = 255)
	private String title;
	@Setter
	@Updatable(maxLength = 255)
	private String submit_url;
	@Setter
	@Updatable(maxLength = 255)
	private String submit_method;
	@Setter
	@Updatable(maxLength = 255)
	private String custom_destination_url;
	@Setter
	@Updatable(maxLength = 255)
	private String custom_destination_text;
	@Setter
	@Updatable(maxLength = 255)
	private String custom_key_screen_name;
	@Setter
	@Updatable(maxLength = 255)
	private String custom_key_name;
	@Setter
	@Updatable(maxLength = 255)
	private String custom_key_email;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/cards/lead_gen";
	protected final static String RESOURCE = "/0/accounts/{account_id}/cards/lead_gen/{id}";

	public LeadGenCard(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public LeadGenCard(Account account, String name, String image_media_id, String cta, String fallback_url,
			String privacy_policy_url, String title, String submit_url, String submit_method,
			String custom_destination_url, String custom_destination_text, String custom_key_screen_name,
			String custom_key_name, String custom_key_email) {

		this.setAccount_id(account.getId());
		this.name = name;
		this.image_media_id = image_media_id;
		this.cta = cta;
		this.fallback_url = fallback_url;
		this.privacy_policy_url = privacy_policy_url;
		this.title = title;
		this.submit_url = submit_url;
		this.submit_method = submit_method;
		this.custom_destination_url = custom_destination_url;
		this.custom_key_screen_name = custom_key_screen_name;
		this.custom_key_name = custom_key_name;
		this.custom_key_email = custom_key_email;
	}
}
