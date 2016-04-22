package com.steelhouse.twitter.ads.creative;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PromotedAccount extends Persistence<PromotedAccount> {

	// read-only
	private String approval_status;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	// writable
	@Setter
	private String id;
	private String account_id;
	@Setter
	private String line_item_id;
	@Setter
	private String user_id;
	@Setter
	private boolean paused;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/promoted_accounts";
	protected final static String RESOURCE = "/0/accounts/{account_id}/promoted_accounts/{id}";
	protected final static String RESOURCE_STATS = "/0/stats/accounts/{account_id}/promoted_accounts";

	public PromotedAccount(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public PromotedAccount(Account account, String line_item_id, String user_id, boolean paused) {
		this.setAccount_id(account.getId());
		this.line_item_id = line_item_id;
		this.user_id = user_id;
		this.paused = paused;
	}
}
