package com.steelhouse.twitter.ads.campaign;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.resource.Resource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PromotableUser extends Resource<PromotableUser> {

	// read-only
	private String id;
	private String promotable_user_type;
	private String user_id;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;
	private String account_id;

	protected final static String RESOURCE_COLLECTION = "/1/accounts/{account_id}/promotable_users";
	protected final static String RESOURCE = "/1/accounts/{account_id}/promotable_users/{id}";

	public PromotableUser(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

}
