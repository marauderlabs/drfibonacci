/*******************************************************************************
 * Copyright 2012 Google, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.turbomanage.storm.types;

import java.util.Date;

import com.turbomanage.storm.api.Converter;

/**
 * User-supplied TypeConverter.
 * 
 * @author David M. Chandler
 */
@Converter(forTypes = { Date.class })
public class DateConverter extends TypeConverter<Date, Long> {

	public static final DateConverter GET = new DateConverter();
	
	@Override
	public Long toSql(Date javaValue) {
		if (javaValue == null) 
			return null;
		return javaValue.getTime();
	}

	@Override
	public Date fromSql(Long sqlValue) {
		if (sqlValue == null) {
			return null;
		}
		return new Date(sqlValue);
	}

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.LONG;
	}

	@Override
	public Long fromString(String strValue) {
		return Long.valueOf(strValue);
	}

}