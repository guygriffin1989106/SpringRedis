/*
 * Copyright 2010 the original author or authors.
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

package org.springframework.datastore.redis.connection;

import java.util.List;
import java.util.Set;

/**
 * Hash-specific commands supported by Redis.
 * 
 * @author Costin Leau
 */
public interface RedisHashCommands {

	public interface Entry {
		public byte[] getField();

		public byte[] getValue();
	}

	Boolean hSet(byte[] key, byte[] field, byte[] value);

	Boolean hSetNX(byte[] key, byte[] field, byte[] value);

	byte[] hGet(byte[] key, byte[] field);

	List<byte[]> hMGet(byte[] key, byte[]... fields);

	void hMSet(byte[] key, byte[][] fields, byte[][] values);

	Integer hIncrBy(byte[] key, byte[] field, int delta);

	Boolean hExists(byte[] key, byte[] field);

	Boolean hDel(byte[] key, byte[] field);

	Integer hLen(byte[] key);

	Set<byte[]> hKeys(byte[] key);

	List<byte[]> hVals(byte[] key);

	Set<Entry> hGetAll(byte[] key);
}
