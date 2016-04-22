package com.steelhouse.twitter.ads.campaign;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.annotations.Updatable;

import com.steelhouse.twitter.ads.enums.BidUnit;
import com.steelhouse.twitter.ads.enums.BidType;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Optimizations;
import com.steelhouse.twitter.ads.enums.Placement;
import com.steelhouse.twitter.ads.enums.Product;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class LineItem extends Persistence<LineItem> {

	// read-only
	private String preview_url;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	// writable
	@Setter
	private String id;
	@Setter
	@Updatable
	private String name;
	@Setter
	@Updatable
	private String campaign_id;
	@Setter
	@Updatable
	private String line_item_id;
	@Updatable
	private String account_id;
	@Setter
	private String advertiser_domain;
	@Setter
	private String[] categories;
	@Setter
	private String charge_by;
	@Setter
	@Updatable
	private String include_sentiment;
	@Setter
	private Objective objective;
	@Setter
	@Updatable
	private Optimizations optimization;
	@Setter
	@Updatable
	private boolean paused;
	@Setter
	private String primary_web_event_tag;
	@Setter
	private Product product_type;
	@Setter
	private Placement[] placements;
	@Setter
	@Updatable
	private BidUnit bid_unit;
	@Setter
	@Updatable
	private BidType bid_type;
	@Setter
	// @Updatable
	private boolean automatically_select_bid;
	@Setter
	@Updatable
	private Number bid_amount_local_micro;
	@Setter
	@Updatable
	private Number total_budget_amount_local_micro;
	@Setter
	@Updatable
	private String[] tracking_tags;

	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/line_items";
	protected final static String RESOURCE = "/0/accounts/{account_id}/line_items/{id}";

	public LineItem(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public LineItem(Account account, String name, String campaign_id, Product product_type, String advertiser_domain,
			String[] categories, boolean paused, String charge_by, String include_sentiment, Objective objective,
			Optimizations optimization, Placement[] placements, BidUnit bid_unit, Number bid_amount_local_micro,
			Number total_budget_amount_local_micro, String[] tracking_tags) {
		
		this.setAccount_id(account.getId());
		this.campaign_id = campaign_id;
		this.name = name;
		this.advertiser_domain = advertiser_domain;
		this.categories = categories;
		this.product_type = product_type;
		this.paused = paused;
		this.charge_by = charge_by;
		this.include_sentiment = include_sentiment;
		this.objective = objective;
		this.optimization = optimization;
		this.placements = placements;
		this.bid_unit = bid_unit;
		this.bid_amount_local_micro = bid_amount_local_micro;
		this.total_budget_amount_local_micro = total_budget_amount_local_micro;
		this.tracking_tags = tracking_tags;
	}
}
