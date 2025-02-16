/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.configuration;

import com.fasterxml.jackson.module.kotlin.KotlinModule;
import kotlin.Deprecated;
import kotlin.coroutines.Continuation;
import org.springdoc.core.parsers.KotlinCoroutinesReturnTypeParser;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Spring doc kotlin configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.enable-kotlin:true}")
@ConditionalOnClass(Continuation.class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocKotlinConfiguration {

	/**
	 * Instantiates a new Spring doc kotlin configuration.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	public SpringDocKotlinConfiguration(ObjectMapperProvider objectMapperProvider) {
		getConfig().addRequestWrapperToIgnore(Continuation.class)
				.addDeprecatedType(Deprecated.class);
		objectMapperProvider.jsonMapper().registerModule(new KotlinModule());
	}


	/**
	 * The type Kotlin module configuration.
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(KotlinModule.class)
	class KotlinModuleConfiguration {
		/**
		 * Instantiates a new Kotlin module configuration.
		 * @param objectMapperProvider the object mapper provider
		 */
		public KotlinModuleConfiguration(ObjectMapperProvider objectMapperProvider) {
			objectMapperProvider.jsonMapper().registerModule( new KotlinModule.Builder().build());
		}
	}

	/**
	 * Kotlin coroutines return type parser kotlin coroutines return type parser.
	 *
	 * @return the kotlin coroutines return type parser
	 */
	@Bean
	@Lazy(false)
	@ConditionalOnMissingBean
	KotlinCoroutinesReturnTypeParser kotlinCoroutinesReturnTypeParser() {
		return new KotlinCoroutinesReturnTypeParser();
	}

}
