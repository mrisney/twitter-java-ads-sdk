package com.steelhouse.twitter.ads.audience;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.campaign.Campaign;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TailoredAudience extends Persistence<TailoredAudience> {

	// read-only
	@Setter(AccessLevel.PRIVATE)
	private Date created_at;
	@Setter(AccessLevel.PRIVATE)
	private Date updated_at;
	@Setter(AccessLevel.PRIVATE)
	private boolean deleted;
	@Setter(AccessLevel.PRIVATE)
	private int audience_size;
	@Setter(AccessLevel.PRIVATE)
	private String audience_type;
	@Setter(AccessLevel.PRIVATE)
	private String metadata;
	@Setter(AccessLevel.PRIVATE)
	private String partner_source;
	@Setter(AccessLevel.PRIVATE)
	private String reasons_not_targetable;
	@Setter(AccessLevel.PRIVATE)
	private String targetable;
	@Setter(AccessLevel.PRIVATE)
	private String targetable_types;
	@Setter(AccessLevel.PRIVATE)

	// writable
	private String id;
	private String name;
	private String list_type;
	private String account_id;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/tailored_audiences";
	protected final static String RESOURCE = "/0/accounts/{account_id}/tailored_audiences/{id}";
	protected final static String RESOURCE_UPDATE = "/0/accounts/{account_id}/tailored_audience_changes";
	protected final static String OPT_OUT = "/0/accounts/{account_id}/tailored_audiences/global_opt_out";

	public TailoredAudience(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}
}