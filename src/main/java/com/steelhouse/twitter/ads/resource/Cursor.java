package com.steelhouse.twitter.ads.resource;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.GenericType;

public class Cursor<T> extends Resource {

	private static final Logger log = LoggerFactory.getLogger(Cursor.class);
	private Collection<T> resourceCollection;

	public Cursor(Object resource) {

		GenericType<Collection<T>> parameterizedGenericType = getParameterizedCollectionType(resource);

		try {
			Method methodGetResourceURL = resource.getClass().getDeclaredMethod("getResourceURL");
			String resourceURL = (String) methodGetResourceURL.invoke(resource);

			resourceCollection = getResource().path(resourceURL).accept(MediaType.APPLICATION_JSON_TYPE)
					.get(parameterizedGenericType);

		} catch (Exception e) {
			log.error("error = " + e.toString());
		}
	}

	public Boolean exhausted() {
		return false;
	}

	public int count() {
		return resourceCollection.size();
	}

	public Object first() {
		Object object = new Object();
		return object;
	}

	public int fetched() {
		return 1;
	}

	@SuppressWarnings("hiding")
	protected <T> GenericType<Collection<T>> getParameterizedCollectionType(Object obj) {
		ParameterizedType parameterizedGenericType = new ParameterizedType() {
			public Type[] getActualTypeArguments() {
				return new Type[] { obj.getClass() };
			}

			public Type getRawType() {
				return Collection.class;
			}

			public Type getOwnerType() {
				return Collection.class;
			}
		};
		return new GenericType<Collection<T>>(parameterizedGenericType) {
		};
	}

	public Object next() {
		Object object = new Object();
		return object;
	}

}
