package com.steelhouse.twitter.ads;

import java.util.Date;

import com.steelhouse.twitter.ads.enums.Objective;
import com.steelhouse.twitter.ads.enums.TimeLineScope;
import com.steelhouse.twitter.ads.resource.Persistence;
import com.steelhouse.twitter.ads.resource.Resource;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ScopedTimeLine extends Resource<ScopedTimeLine> {

	// read-only
	private String id_str;
	private String text;
	private String source;
	private String rel;
	private String truncated;
	private String in_reply_to_status_id;
	private String in_reply_to_user_id;
	private String in_reply_to_user_id_str;
	private String in_reply_to_screen_name;
	private String created_at;
	private Date updated_at;
	private boolean deleted;
	private User user;
	private boolean favorited;
	private String account_id;

	@Getter
	public class User {
		private Long id;
		private String id_str;
		private String name;
		private String screen_name;
	}

	// writable properties
	@Setter
	private String id;
	@Setter
	private String user_id;
	@Setter
	private TimeLineScope scoped_to;
	@Setter
	private Objective objective;
	@Setter
	private boolean trim_user;
	@Setter
	private Number count;

	protected static final String RESOURCE = "/1/accounts/{account_id}/scoped_timeline";

	public ScopedTimeLine(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}
}
