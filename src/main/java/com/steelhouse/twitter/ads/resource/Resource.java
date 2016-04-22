package com.steelhouse.twitter.ads.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.steelhouse.twitter.ads.common.FieldCopier;
import com.steelhouse.twitter.ads.exception.TwitterAdsException;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Resource<T> {

	private final static Logger log = LoggerFactory.getLogger(Resource.class);

	protected Object resourceObject;

	protected final void registerObject(Object object) {
		this.resourceObject = object;
	}

	protected Object getResourceObject() {
		return resourceObject;
	}

	protected final String getObjectId() {
		try {
			return (String) FieldUtils.readField(resourceObject, "id", true);
		} catch (Exception e) {
			return null;
		}
	}

	private String getAccountId() {
		try {
			return (String) FieldUtils.readField(resourceObject, "account_id", true);
		} catch (Exception e) {
			return null;
		}
	}

	protected String getResourcePath() {

		String id = getObjectId();
		String account_id = getAccountId();
		String resourcePath = null;
		
		try {
		
			String resource = (String) FieldUtils.readField(resourceObject, "RESOURCE", true);
			String resourceCollection = (String) FieldUtils.readField(resourceObject, "RESOURCE_COLLECTION", true);
			
			if (null != resourceCollection) {
				
				resourcePath =  (id != null) ? resource.replace("{account_id}", account_id).replace("{id}", id)
						: resourceCollection.replace("{account_id}", account_id);
			}

		} catch (Exception e) {
			log.warn("unable to read 'RESOURCE' or 'RESOURCE_COLLECTION', errror : {}", e.toString());
			// return null;
		}
		log.info("resource path = "+resourcePath);
		return resourcePath;
	}

	public static WebResource getResource() {
		return ClientServiceFactory.getInstance().getResource();
	}

	public Cursor getAll() {
		return null;
	}

	/**
	 * Returns an object instance for a given resource.
	 **/
	public void load() {

		MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
		parameters.add("id", getObjectId());

		try {
			Object object = getResource()
					.path(getResourcePath())
					.queryParams(parameters)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.get(resourceObject.getClass());

			FieldCopier.instance().copyFields(object, resourceObject);

		} catch (UniformInterfaceException ue) {
			List<Map> errors = ue.getResponse().getEntity(new GenericType<List<Map>>() {});
			throw new TwitterAdsException(ue.getResponse().getStatus());
		}
	}

	public void reload() {
		
		Object object = getResource()
				.path(getResourcePath())
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(resourceObject.getClass());

		FieldCopier.instance().copyFields(object, resourceObject);
	}
}
