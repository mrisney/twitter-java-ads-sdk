package com.steelhouse.twitter.ads.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.common.Constants;
import com.steelhouse.twitter.ads.json.GsonJerseyProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

public class ClientServiceImpl implements ClientService {

	public String consumerKey;
	public String consumerSecret;
	public String accessToken;
	public String accessTokenSecret;
	public String domain;
	public boolean trace;
	public boolean sandbox;

	private final static Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

	/**
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 * @param accessTokenSecret
	 */

	public ClientServiceImpl(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		super();
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
	}

	public void setSandbox(boolean enabled) {
		this.sandbox = enabled;
	}

	public void setTrace(boolean enabled) {
		this.trace = enabled;
	}

	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	
	@Override
	public WebResource getResource() {
		WebResource webResource = null;
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().add(GsonJerseyProvider.class);
		Client client = Client.create(config);
		OAuthParameters oaParams = new OAuthParameters().signatureMethod("HMAC-SHA1").consumerKey(consumerKey)
				.token(accessToken).version("1.0");
		OAuthSecrets oaSecrets = new OAuthSecrets().consumerSecret(consumerSecret).tokenSecret(accessTokenSecret);

		OAuthClientFilter oAuthFilter = new OAuthClientFilter(client.getProviders(), oaParams, oaSecrets);
		client.addFilter(oAuthFilter);

		if (this.sandbox) {
			webResource = client.resource(Constants.SANDBOX_BASE_API_URL);
		} else if (null!= this.domain){
			webResource = client.resource(this.domain);
		}
		else{
			webResource = client.resource(Constants.BASE_API_URL);
		}
		
		
		if (this.trace) {
			webResource.addFilter(new LoggingFilter());
		}
		return webResource;
	}

	public Client getClient() {

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		OAuthParameters oaParams = new OAuthParameters().signatureMethod("HMAC-SHA1").consumerKey(consumerKey)
				.token(accessToken).version("1.0");

		OAuthSecrets oaSecrets = new OAuthSecrets().consumerSecret(consumerSecret).tokenSecret(accessTokenSecret);

		OAuthClientFilter oAuthFilter = new OAuthClientFilter(client.getProviders(), oaParams, oaSecrets);
		client.addFilter(oAuthFilter);

		return client;
	}
}
