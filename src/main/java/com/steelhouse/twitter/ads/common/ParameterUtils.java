package com.steelhouse.twitter.ads.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ParameterUtils {
	private final static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private final static Logger log = LoggerFactory.getLogger(ParameterUtils.class);

	public static MultivaluedMap<String, String> getMappedValues(Object object) {
		MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();

		for (Method method : object.getClass().getMethods()) {
			if (method.getName().indexOf("set") > -1) {
				String propertyName = method.getName().replace("set", "").toLowerCase();
				try {
					Object value = PropertyUtils.getProperty(object, propertyName);
					if (value != null) {
						Class<?> pt = PropertyUtils.getPropertyType(object, propertyName);
						if (java.util.Date.class.equals(pt)) {
							Date dt = (Date) value;
							DateFormat df = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
							String dateAsString = df.format(dt);
							parameters.add(propertyName, dateAsString);
						} else if (pt.isArray()) {
							List<Object> objectList = Arrays.asList((Object[]) value);
							String csvString = Joiner.on(',').join(objectList);
							parameters.add(propertyName, csvString);
						} else {
							parameters.add(propertyName, value.toString());
						}
					}
				} catch (Exception e) {
					log.error(e.toString());
				}
			}
		}
		return parameters;
	}

	public static MultivaluedMap<String, String> getUpdateValues(Object object) {

		MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();

		for (Field field : object.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(com.steelhouse.twitter.ads.annotations.Updatable.class)) {

				try {
					String propertyName = field.getName().charAt(0) + field.getName().substring(1);
					Object value = PropertyUtils.getProperty(object, propertyName);

					if (value != null) {
						Class<?> pt = PropertyUtils.getPropertyType(object, propertyName);
						if (java.util.Date.class.equals(pt)) {
							Date dt = (Date) value;
							DateFormat df = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
							String dateAsString = df.format(dt);
							parameters.add(propertyName, dateAsString);
						} else if (pt.isArray()) {
							List<Object> objectList = Arrays.asList((Object[]) value);
							String csvString = Joiner.on(',').join(objectList);
							parameters.add(propertyName, csvString);
						} else {
							String propertyString = URLEncoder.encode(value.toString(), "UTF-8").replaceAll("\\+","%20");
							parameters.add(propertyName, propertyString);
						}
					}
				} catch (Exception e) {
					log.error(e.toString());
				}
			}
		}
		return parameters;
	}
}
