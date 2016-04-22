package com.steelhouse.twitter.ads.campaign;

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
public class Campaign extends Persistence<Campaign> {

	private Boolean servable;
	private String[] reasons_not_servable;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	// writable
	@Setter
	private String id;
	@Setter
	@Updatable
	private String name;
	private String account_id;
	@Setter
	private String funding_instrument_id;
	@Setter
	// @Updatable
	private Date start_time;
	@Setter
	// @Updatable
	private Date end_time;
	@Setter
	@Updatable
	private boolean paused;
	@Setter
	@Updatable
	private String currency;
	@Setter
	@Updatable
	private boolean standard_delivery;
	@Setter
	@Updatable
	private Number daily_budget_amount_local_micro;
	@Setter
	@Updatable
	private Number total_budget_amount_local_micro;
	@Setter
	@Updatable
	private Number duration_in_days;
	@Setter
	@Updatable
	private Number frequency_cap;

	protected final static String RESOURCE = "/0/accounts/{account_id}/campaigns/{id}";
	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/campaigns";

	public Campaign(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public Campaign(Account account, String name, String funding_instrument_id, Date start_time, Date end_time,
			boolean paused, String currency, boolean standard_delivery, Number daily_budget_amount_local_micro,
			Number total_budget_amount_local_micro) {

		this.setAccount_id(account.getId());
		this.name = name;
		this.funding_instrument_id = funding_instrument_id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.paused = paused;
		this.currency = currency;
		this.standard_delivery = standard_delivery;
		this.daily_budget_amount_local_micro = daily_budget_amount_local_micro;
		this.total_budget_amount_local_micro = total_budget_amount_local_micro;
	}
}