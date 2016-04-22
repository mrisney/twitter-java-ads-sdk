package com.steelhouse.twitter.ads.campaign;

import java.util.Date;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class TargetingCriteria extends Persistence<TargetingCriteria> {

	private String localized_name;
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
	private String targeting_type;
	@Setter
	private String targeting_value;
	@Setter
	private String tailored_audience_expansion;
	@Setter
	private String tailored_audience_type;

	protected final static String RESOURCE_COLLECTION = "/1/accounts/{account_id}/targeting_criteria";
	protected final static String RESOURCE = "/1/accounts/{account_id}/targeting_criteria/{id}";

	public TargetingCriteria(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public TargetingCriteria(Account account, String line_item_id, String targeting_type, String targeting_value,
			String tailored_audience_expansion, String tailored_audience_type) {

		this.setAccount_id(account.getId());
		this.line_item_id = line_item_id;
		this.targeting_type = targeting_type;
		this.targeting_value = targeting_value;
		this.tailored_audience_expansion = tailored_audience_expansion;
		this.tailored_audience_type = tailored_audience_type;
	}
}
