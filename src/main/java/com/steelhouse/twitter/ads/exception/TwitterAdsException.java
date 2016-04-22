package com.steelhouse.twitter.ads.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class TwitterAdsException extends WebApplicationException {

	private static final long serialVersionUID = 5898937029884055208L;

	public TwitterAdsException() {
	}

	public TwitterAdsException(Response response) {
		super(response);
	}

	public TwitterAdsException(int status) {
		super(status);
	}

	public TwitterAdsException(Response.Status status) {
		super(status);
	}

	public TwitterAdsException(Throwable cause) {
		super(cause);
	}

	public TwitterAdsException(Throwable cause, Response response) {
		super(cause, response);
	}

	public TwitterAdsException(Throwable cause, int status) {
		super(cause, status);
	}

	public TwitterAdsException(Throwable cause, Response.Status status) {
		super(cause, status);
	}
}