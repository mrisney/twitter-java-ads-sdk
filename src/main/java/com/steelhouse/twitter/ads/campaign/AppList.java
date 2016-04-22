package com.steelhouse.twitter.ads.campaign;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.common.base.Joiner;
import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.resource.Resource;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class AppList extends Resource<AppList> {

	// read-only
	private String id;
	private String name;
	private String account_id;
	@Setter
	private List<apps> apps;

	@Getter
	public class apps {
		private String app_store_identifier;
		private String os_type;
	}

	protected final static String RESOURCE_COLLECTION = "/1/accounts/{account_id}/app_lists";
	protected final static String RESOURCE = "/1/accounts/{account_id}/app_lists/{id}";

	public AppList(Account account) {
		this.setAccount_id(account.getId());
	}

	public void setAccount_id(String id) {
		this.account_id = id;
		registerObject(this);
	}

	public List<apps> getApps() {
		String resource = RESOURCE_COLLECTION.replace("{account_id}", account_id);

		List<apps> applications = getResource().path(resource).accept(MediaType.APPLICATION_JSON_TYPE)
				.get(new GenericType<List<apps>>() {
				});

		return applications;
	}

	public void create(String name, List<String> ids) {

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("name", name);
		params.add("app_store_identifiers", Joiner.on(',').join(ids));

		String resource = RESOURCE_COLLECTION.replace("{account_id}", account_id);

		AppList appList = getResource()
				.path(resource)
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(AppList.class, params);
		// System.out.println(appList.toString());
		// ResourceUtils.copyAtoB(appList, this.getClass());
	}
}
