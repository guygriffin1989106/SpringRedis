/*
 * Copyright 2010-2011  original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.keyvalue.redis.connection;

import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * Thread-safe factory of Redis connections.
 * 
 * @author Costin Leau
 */
public interface RedisConnectionFactory extends PersistenceExceptionTranslator {

	/**
	 * Provides a suitable connection for interacting with Redis.
	 * 
	 * @return connection for interacting with Redis.
	 */
	RedisConnection getConnection();
}
