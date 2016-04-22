package com.steelhouse.twitter.ads.targeting;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.common.ParameterUtils;
import com.steelhouse.twitter.ads.enums.BidType;
import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.Product;
import com.steelhouse.twitter.ads.resource.Resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReachEstimate extends Resource<ReachEstimate> {

	private String account_id;
	private Product product_type;
	private Objective objective;
	private Number bid_amount_local_micro;
	private String currency;
	private Number campaign_daily_budget_amount_local_micro;
	private String user_id;
	private BidType bid_type;
	private Number gender;
	private List<String> followers_of_users;
	private Long similar_to_followers_of_users;
	private Impresions impresions;
	private Count count;
	private Infinite_bid_count infinite_bid_count;
	private Engagements engagements;
	private Estimated_daily_spend_local_micro estimated_daily_spend_local_micro;
	private String languages;
	private String locations;

	@Getter
	@ToString
	public class Impresions {
		private Number max;
		private Number min;
	}

	@Getter
	@ToString
	public class Count {
		private Number max;
		private Number min;
	}

	@Getter
	@ToString
	public class Infinite_bid_count {
		private Number max;
		private Number min;
	}

	@Getter
	@ToString
	public class Engagements {
		private Number max;
		private Number min;
	}

	@Getter
	@ToString
	public class Estimated_daily_spend_local_micro{
		private Number max;
		private Number min;
	}
	protected final static String RESOURCE = "/1/accounts/{account_id}/reach_estimate";

	public ReachEstimate(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public ReachEstimate(Account account, Product product_type, Objective objective,
			Number campaign_daily_budget_amount_local_micro, String currency, Number gender,
			Long similar_to_followers_of_users, String user_id, BidType bid_type, String languages, 
			String locations) {

		this.setAccount_id(account.getId());
		this.product_type = product_type;
		this.objective = objective;
		this.campaign_daily_budget_amount_local_micro = campaign_daily_budget_amount_local_micro;
		this.currency = currency;
		this.gender = gender;
		this.similar_to_followers_of_users = similar_to_followers_of_users;
		this.user_id = user_id;
		this.bid_type = bid_type;
		this.languages = languages;
		this.locations = locations;
	}

	@Override
	public void load() {
		
		String resourcePath = RESOURCE.replace("{account_id}", account_id);
		MultivaluedMap<String, String> parameters = ParameterUtils.getMappedValues(this);
		parameters.remove("infinite_bid_count");
		
		JsonObject jsonObj = getResource()
				.path(resourcePath)
				.queryParams(parameters)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(JsonObject.class);
		
		/**
		 * smack on forehead, oy vey, I decided to use nested objects ....
		 */
		Gson gson = new Gson();
		this.setImpresions(gson.fromJson(jsonObj.get("impressions"), Impresions.class));
		this.setCount(gson.fromJson(jsonObj.get("count"), Count.class));
		this.setEngagements(gson.fromJson(jsonObj.get("engagements"), Engagements.class));
		this.setInfinite_bid_count(gson.fromJson(jsonObj.get("infinite_bid_count"), Infinite_bid_count.class));
		this.setEstimated_daily_spend_local_micro(gson.fromJson(jsonObj.get("estimated_daily_spend_local_micro"), Estimated_daily_spend_local_micro.class));
	}

}
