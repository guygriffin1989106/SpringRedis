/*
 * Copyright 2011 the original author or authors.
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
package org.springframework.data.keyvalue.redis.hash;

import java.util.Map;

/**
 * Core mapping contract between Java types and Redis hashes/maps. 
 * It's up to the implementation to support nested objects.
 * 
 * @author Costin Leau
 */
public interface HashMapper<T, K, V> {

	Map<K, V> toHash(T object);

	T fromHash(Map<K, V> hash);
}
