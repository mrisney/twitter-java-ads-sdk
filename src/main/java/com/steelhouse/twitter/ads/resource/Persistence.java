package com.steelhouse.twitter.ads.resource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steelhouse.twitter.ads.common.FieldCopier;
import com.steelhouse.twitter.ads.common.ParameterUtils;

public class Persistence<T> extends Resource<T> {
	private final static Logger log = LoggerFactory.getLogger(Persistence.class);

	public void save() {

		Object object = null;
		MultivaluedMap<String, String> params = null;
		String id = getObjectId();
	

		if (null == id) {

			params = ParameterUtils.getMappedValues(resourceObject);
			params.remove("account_id");
			
			object = getResource()
					.path(getResourcePath())
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.post(resourceObject.getClass(), params);

			log.info("saved {}, saved value : {}", resourceObject.getClass().getTypeName(), object.toString());

		} else {

			params = ParameterUtils.getUpdateValues(resourceObject);
			
			object = getResource()
					.path(getResourcePath())
					.queryParams(params)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.put(resourceObject.getClass());

			log.info("updated {}, updated value : {}", resourceObject.getClass().getTypeName(), object.toString());
		}

		FieldCopier.instance().copyFields(object, resourceObject);

	}

	public void delete() {

		Object deletedObject = getResource().path(getResourcePath()).accept(MediaType.APPLICATION_JSON_TYPE)
				.delete(resourceObject.getClass());

		log.info("deleted {}, deleted value : {}", resourceObject.getClass().getTypeName(), deletedObject.toString());

		FieldCopier.instance().copyFields(deletedObject, resourceObject);

	}
}