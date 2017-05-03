/*
 * Copyright 2017 the original author or authors.
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
package org.springframework.data.redis.connection.lettuce;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ValueScanCursor;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisSetCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnection.LettuceResult;
import org.springframework.data.redis.connection.lettuce.LettuceConnection.LettuceTxResult;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyBoundCursor;
import org.springframework.data.redis.core.ScanIteration;
import org.springframework.data.redis.core.ScanOptions;

/**
 * @author Christoph Strobl
 * @since 2.0
 */
class LettuceSetCommands implements RedisSetCommands {

	private final LettuceConnection connection;

	public LettuceSetCommands(LettuceConnection connection) {
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sAdd(byte[], byte[][])
	 */
	@Override
	public Long sAdd(byte[] key, byte[]... values) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sadd(key, values)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sadd(key, values)));
				return null;
			}
			return getConnection().sadd(key, values);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sCard(byte[])
	 */
	@Override
	public Long sCard(byte[] key) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().scard(key)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().scard(key)));
				return null;
			}
			return getConnection().scard(key);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sDiff(byte[][])
	 */
	@Override
	public Set<byte[]> sDiff(byte[]... keys) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sdiff(keys)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sdiff(keys)));
				return null;
			}
			return getConnection().sdiff(keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sDiffStore(byte[], byte[][])
	 */
	@Override
	public Long sDiffStore(byte[] destKey, byte[]... keys) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sdiffstore(destKey, keys)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sdiffstore(destKey, keys)));
				return null;
			}
			return getConnection().sdiffstore(destKey, keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sInter(byte[][])
	 */
	@Override
	public Set<byte[]> sInter(byte[]... keys) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sinter(keys)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sinter(keys)));
				return null;
			}
			return getConnection().sinter(keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sInterStore(byte[], byte[][])
	 */
	@Override
	public Long sInterStore(byte[] destKey, byte[]... keys) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sinterstore(destKey, keys)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sinterstore(destKey, keys)));
				return null;
			}
			return getConnection().sinterstore(destKey, keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sIsMember(byte[], byte[])
	 */
	@Override
	public Boolean sIsMember(byte[] key, byte[] value) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sismember(key, value)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sismember(key, value)));
				return null;
			}
			return getConnection().sismember(key, value);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sMembers(byte[])
	 */
	@Override
	public Set<byte[]> sMembers(byte[] key) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().smembers(key)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().smembers(key)));
				return null;
			}
			return getConnection().smembers(key);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sMove(byte[], byte[], byte[])
	 */
	@Override
	public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().smove(srcKey, destKey, value)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().smove(srcKey, destKey, value)));
				return null;
			}
			return getConnection().smove(srcKey, destKey, value);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sPop(byte[])
	 */
	@Override
	public byte[] sPop(byte[] key) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().spop(key)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().spop(key)));
				return null;
			}
			return getConnection().spop(key);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sRandMember(byte[])
	 */
	@Override
	public byte[] sRandMember(byte[] key) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().srandmember(key)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().srandmember(key)));
				return null;
			}
			return getConnection().srandmember(key);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sRandMember(byte[], long)
	 */
	@Override
	public List<byte[]> sRandMember(byte[] key, long count) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult((RedisFuture) getAsyncConnection().srandmember(key, count),
						LettuceConverters.bytesCollectionToBytesList()));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().srandmember(key, count),
						LettuceConverters.bytesCollectionToBytesList()));
				return null;
			}
			return LettuceConverters.toBytesList(getConnection().srandmember(key, count));
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sRem(byte[], byte[][])
	 */
	@Override
	public Long sRem(byte[] key, byte[]... values) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().srem(key, values)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().srem(key, values)));
				return null;
			}
			return getConnection().srem(key, values);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sUnion(byte[][])
	 */
	@Override
	public Set<byte[]> sUnion(byte[]... keys) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sunion(keys)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sunion(keys)));
				return null;
			}
			return getConnection().sunion(keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sUnionStore(byte[], byte[][])
	 */
	@Override
	public Long sUnionStore(byte[] destKey, byte[]... keys) {
		try {
			if (isPipelined()) {
				pipeline(connection.newLettuceResult(getAsyncConnection().sunionstore(destKey, keys)));
				return null;
			}
			if (isQueueing()) {
				transaction(connection.newLettuceTxResult(getConnection().sunionstore(destKey, keys)));
				return null;
			}
			return getConnection().sunionstore(destKey, keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.RedisSetCommands#sScan(byte[], org.springframework.data.redis.core.ScanOptions)
	 */
	@Override
	public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
		return sScan(key, 0, options);
	}

	/**
	 * @since 1.4
	 * @param key
	 * @param cursorId
	 * @param options
	 * @return
	 */
	public Cursor<byte[]> sScan(byte[] key, long cursorId, ScanOptions options) {

		return new KeyBoundCursor<byte[]>(key, cursorId, options) {

			@Override
			protected ScanIteration<byte[]> doScan(byte[] key, long cursorId, ScanOptions options) {

				if (isQueueing() || isPipelined()) {
					throw new UnsupportedOperationException("'SSCAN' cannot be called in pipeline / transaction mode.");
				}

				io.lettuce.core.ScanCursor scanCursor = connection.getScanCursor(cursorId);
				ScanArgs scanArgs = connection.getScanArgs(options);

				ValueScanCursor<byte[]> valueScanCursor = getConnection().sscan(key, scanCursor, scanArgs);
				String nextCursorId = valueScanCursor.getCursor();

				List<byte[]> values = connection.failsafeReadScanValues(valueScanCursor.getValues(), null);
				return new ScanIteration<>(Long.valueOf(nextCursorId), values);
			}

			protected void doClose() {
				LettuceSetCommands.this.connection.close();
			}

		}.open();
	}

	private boolean isPipelined() {
		return connection.isPipelined();
	}

	private boolean isQueueing() {
		return connection.isQueueing();
	}

	private void pipeline(LettuceResult result) {
		connection.pipeline(result);
	}

	private void transaction(LettuceTxResult result) {
		connection.transaction(result);
	}

	RedisClusterAsyncCommands<byte[], byte[]> getAsyncConnection() {
		return connection.getAsyncConnection();
	}

	public RedisClusterCommands<byte[], byte[]> getConnection() {
		return connection.getConnection();
	}

	private DataAccessException convertLettuceAccessException(Exception ex) {
		return connection.convertLettuceAccessException(ex);
	}

}
