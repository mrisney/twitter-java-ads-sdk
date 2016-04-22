package com.steelhouse.twitter.ads.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://www.javacodegeeks.com/2016/03/implementing-annotation-interface.html

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Updatable {
	boolean required() default false;

	int maxLength() default 255;
}
