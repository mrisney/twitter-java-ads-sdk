package com.steelhouse.twitter.ads.measurement;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.annotations.Updatable;
import com.steelhouse.twitter.ads.enums.WebEventTagType;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class WebEventTag extends Persistence<WebEventTag> {

	private Date created_at;
	private Date updated_at;
	private boolean deleted;
	private String embed_code;

	// writable
	@Setter
	private String id;
	private String account_id;
	@Setter
	@Updatable
	private String name;
	@Setter
	Number click_window;
	@Setter
	private Number view_through_window;
	@Setter
	private WebEventTagType type;
	@Setter
	private boolean retargeting_enabled;

	protected final static String RESOURCE = "/0/accounts/{account_id}/web_event_tags/{id}";
	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/web_event_tags";

	public WebEventTag(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public WebEventTag(Account account, String name, Number click_window, Number view_through_window,
			WebEventTagType type, boolean retargeting_enabled) {
		this.setAccount_id(account.getId());
		this.name = name;
		this.click_window = click_window;
		this.view_through_window = view_through_window;
		this.type = type;
		this.retargeting_enabled = retargeting_enabled;
	}
}
