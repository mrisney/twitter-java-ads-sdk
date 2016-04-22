package com.steelhouse.twitter.ads.client;

public class ClientServiceFactory {

	public static String consumerKey;
	public static String consumerSecret;
	public static String accessToken;
	public static String accessTokenSecret;

	private volatile static ClientService clientService;

	public static ClientService getInstance() {
		if (clientService == null) {
			synchronized (ClientServiceImpl.class) {
				if (clientService == null) {
					clientService = new ClientServiceImpl(consumerKey, consumerSecret, accessToken, accessTokenSecret);
				}
			}
		}
		return clientService;
	}
}