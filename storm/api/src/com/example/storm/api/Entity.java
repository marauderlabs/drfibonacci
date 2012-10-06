package com.example.storm.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.storm.DatabaseHelper;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Entity {

	String dbName() default "";
	
}
