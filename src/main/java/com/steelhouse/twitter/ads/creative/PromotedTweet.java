package com.steelhouse.twitter.ads.creative;

import java.util.Date;
import java.util.List;

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.creative.Video.VideoBuilder;
import com.steelhouse.twitter.ads.annotations.Updatable;
import com.steelhouse.twitter.ads.resource.Persistence;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PromotedTweet extends Persistence<PromotedTweet> {

	private String approval_status;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;
    private  String tweet_id;
	// writable
	@Setter
	@Updatable(required = true)
	private String id;
	@Updatable(required = true)
	private String account_id;
	@Setter
	private String line_item_id;
	@Setter
	@Updatable
	private Long tweet_ids;
	// @Updatable
	@Setter
	private String[] display_properties;
	@Setter
	@Updatable
	private boolean paused;

	protected final static String RESOURCE = "/0/accounts/{account_id}/promoted_tweets/{id}";
	protected final static String RESOURCE_COLLECTION = "/0/accounts/{account_id}/promoted_tweets";
	protected final static String RESOURCE_STATS = "/0/stats/accounts/{account_id}/promoted_tweets";

	public PromotedTweet(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	@Builder
	public PromotedTweet(Account account, String line_item_id, Long tweet_ids, boolean paused) {
		this.setAccount_id(account.getId());
		this.line_item_id = line_item_id;
		this.tweet_ids = tweet_ids;
		this.paused = paused;
	}
}
