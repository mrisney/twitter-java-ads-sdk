package com.steelhouse.twitter.ads.campaign;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.resource.Resource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class FundingInstrument extends Resource<FundingInstrument> {

	@Setter
	private String id;
	private String account_id;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;
	private String description;
	private boolean cancelled;
	private Date start_time;
	private Date end_time;
	private boolean paused;
	private Number credit_remaining_local_micro;
	private boolean able_to_fund;
	private String[] reasons_not_able_to_fund;
	private String currency;
	private Number funded_amount_local_micro;
	private String type;

	protected final static String RESOURCE = "/1/accounts/{account_id}/funding_instruments/{id}";
	protected final static String RESOURCE_COLLECTION = "/1/accounts/{account_id}/funding_instruments";

	public FundingInstrument(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}
}
