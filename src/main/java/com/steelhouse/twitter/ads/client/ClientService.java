package com.steelhouse.twitter.ads.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public interface ClientService {
	public WebResource getResource();
	public Client getClient();
	public void setDomain(String domain);
	public void setSandbox(boolean enabled);
	public void setTrace(boolean enabled);
}
