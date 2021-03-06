/*
 * This file is a component of the thundr-contrib-gae-admin library,
 * a software library from Atomic Leopard.
 *
 * Copyright (C) 2015 Atomic Leopard, <admin@atomicleopard.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.gae.admin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.search.Index;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class KindDto {
	public String name;
	public String className;
	public String searchIndex;
	public List<FieldDto> fields;
	public Map<String, List<Object>> possibleValues;

	public KindDto(String name, String className, String searchIndex, List<Field> fields, Map<String, List<Object>> possibleValues) {
		super();
		this.name = name;
		this.className = className;
		this.searchIndex = searchIndex;
		this.fields = Lists.transform(fields, FieldDto.ToFieldDto);
		this.possibleValues = possibleValues;
	}

	public FieldDto get(String fieldName) {
		for (FieldDto field : fields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		return null;
	}
	
	public static final Function<Kind, KindDto> ToEntityDto = new Function<Kind, KindDto>() {
		@Override
		public KindDto apply(Kind kind) {
			List<Field> fields = new ArrayList<>();
			Map<String, List<Object>> possibleValues = new HashMap<>();

			for (Property p : kind.properties()) {
				fields.add(p.field());
				possibleValues.put(p.field().getName(), p.possibleValues());
			}

			final Index index = kind.index();
			return new KindDto(kind.name(), kind.metadata().getEntityClass().getName(), index != null ? index.getName() : null, fields, possibleValues);
		}
	};

}