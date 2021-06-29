/*
 * Copyright 2011-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.redis.core;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.connection.RedisZSetCommands.Weights;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * ZSet (or SortedSet) operations bound to a certain key.
 *
 * @author Costin Leau
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Wongoo (望哥)
 * @author Andrey Shlykov
 */
public interface BoundZSetOperations<K, V> extends BoundKeyOperations<K> {

	/**
	 * Add {@code value} to a sorted set at the bound key, or update its {@code score} if it already exists.
	 *
	 * @param score the score.
	 * @param value the value.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
	 */
	@Nullable
	Boolean add(V value, double score);

	/**
	 * Add {@code value} to a sorted set at {@code key} if it does not already exists.
	 *
	 * @param score the score.
	 * @param value the value.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.5
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD NX</a>
	 */
	@Nullable
	Boolean addIfAbsent(V value, double score);

	/**
	 * Add {@code tuples} to a sorted set at the bound key, or update its {@code score} if it already exists.
	 *
	 * @param tuples must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
	 */
	@Nullable
	Long add(Set<TypedTuple<V>> tuples);

	/**
	 * Add {@code tuples} to a sorted set at {@code key} if it does not already exists.
	 *
	 * @param tuples must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.5
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD NX</a>
	 */
	@Nullable
	Long addIfAbsent(Set<TypedTuple<V>> tuples);

	/**
	 * Remove {@code values} from sorted set. Return number of removed elements.
	 *
	 * @param values must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrem">Redis Documentation: ZREM</a>
	 */
	@Nullable
	Long remove(Object... values);

	/**
	 * Increment the score of element with {@code value} in sorted set by {@code increment}.
	 *
	 * @param delta
	 * @param value the value.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
	 */
	@Nullable
	Double incrementScore(V value, double delta);

	/**
	 * Determine the index of element with {@code value} in a sorted set.
	 *
	 * @param o the value.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
	 */
	@Nullable
	Long rank(Object o);

	/**
	 * Determine the index of element with {@code value} in a sorted set when scored high to low.
	 *
	 * @param o the value.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
	 */
	@Nullable
	Long reverseRank(Object o);

	/**
	 * Get elements between {@code start} and {@code end} from sorted set.
	 *
	 * @param start
	 * @param end
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 */
	@Nullable
	Set<V> range(long start, long end);

	/**
	 * Get set of {@link Tuple}s between {@code start} and {@code end} from sorted set.
	 *
	 * @param start
	 * @param end
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 */
	@Nullable
	Set<TypedTuple<V>> rangeWithScores(long start, long end);

	/**
	 * Get elements where score is between {@code min} and {@code max} from sorted set.
	 *
	 * @param min
	 * @param max
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 */
	@Nullable
	Set<V> rangeByScore(double min, double max);

	/**
	 * Get set of {@link Tuple}s where score is between {@code min} and {@code max} from sorted set.
	 *
	 * @param min
	 * @param max
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 */
	@Nullable
	Set<TypedTuple<V>> rangeByScoreWithScores(double min, double max);

	/**
	 * Get elements in range from {@code start} to {@code end} from sorted set ordered from high to low.
	 *
	 * @param start
	 * @param end
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
	 */
	@Nullable
	Set<V> reverseRange(long start, long end);

	/**
	 * Get set of {@link Tuple}s in range from {@code start} to {@code end} from sorted set ordered from high to low.
	 *
	 * @param start
	 * @param end
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
	 */
	@Nullable
	Set<TypedTuple<V>> reverseRangeWithScores(long start, long end);

	/**
	 * Get elements where score is between {@code min} and {@code max} from sorted set ordered from high to low.
	 *
	 * @param min
	 * @param max
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
	 */
	@Nullable
	Set<V> reverseRangeByScore(double min, double max);

	/**
	 * Get set of {@link Tuple} where score is between {@code min} and {@code max} from sorted set ordered from high to
	 * low.
	 *
	 * @param min
	 * @param max
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
	 */
	@Nullable
	Set<TypedTuple<V>> reverseRangeByScoreWithScores(double min, double max);

	/**
	 * Count number of elements within sorted set with scores between {@code min} and {@code max}.
	 *
	 * @param min
	 * @param max
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
	 */
	@Nullable
	Long count(double min, double max);

	/**
	 * Count number of elements within sorted set with value between {@code Range#min} and {@code Range#max} applying
	 * lexicographical ordering.
	 *
	 * @param range must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.4
	 * @see <a href="https://redis.io/commands/zlexcount">Redis Documentation: ZLEXCOUNT</a>
	 */
	@Nullable
	Long lexCount(Range range);

	/**
	 * Remove and return the value with its score having the lowest score from sorted set at the bound key.
	 *
	 * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
	 * @since 2.6
	 */
	@Nullable
	TypedTuple<V> popMin();

	/**
	 * Remove and return {@code count} values with their score having the lowest score from sorted set at the bound key.
	 *
	 * @param count number of elements to pop.
	 * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
	 * @since 2.6
	 */
	@Nullable
	Set<TypedTuple<V>> popMin(long count);

	/**
	 * Remove and return the value with its score having the lowest score from sorted set at the bound key. <b>Blocks
	 * connection</b> until element available or {@code timeout} reached.
	 *
	 * @param timeout
	 * @param unit must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="https://redis.io/commands/bzpopmin">Redis Documentation: BZPOPMIN</a>
	 * @since 2.6
	 */
	@Nullable
	TypedTuple<V> popMin(long timeout, TimeUnit unit);

	/**
	 * Remove and return the value with its score having the lowest score from sorted set at the bound key. <b>Blocks
	 * connection</b> until element available or {@code timeout} reached.
	 *
	 * @param timeout must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @throws IllegalArgumentException if the timeout is {@literal null} or negative.
	 * @see <a href="https://redis.io/commands/bzpopmin">Redis Documentation: BZPOPMIN</a>
	 * @since 2.6
	 */
	@Nullable
	default TypedTuple<V> popMin(Duration timeout) {

		Assert.notNull(timeout, "Timeout must not be null");
		Assert.isTrue(!timeout.isNegative(), "Timeout must not be negative");

		return popMin(TimeoutUtils.toSeconds(timeout), TimeUnit.SECONDS);
	}

	/**
	 * Remove and return the value with its score having the highest score from sorted set at the bound key.
	 *
	 * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
	 * @since 2.6
	 */
	@Nullable
	TypedTuple<V> popMax();

	/**
	 * Remove and return {@code count} values with their score having the highest score from sorted set at the bound key.
	 *
	 * @param count number of elements to pop.
	 * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
	 * @since 2.6
	 */
	@Nullable
	Set<TypedTuple<V>> popMax(long count);

	/**
	 * Remove and return the value with its score having the highest score from sorted set at the bound key. <b>Blocks
	 * connection</b> until element available or {@code timeout} reached.
	 *
	 * @param timeout
	 * @param unit must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="https://redis.io/commands/bzpopmax">Redis Documentation: BZPOPMAX</a>
	 * @since 2.6
	 */
	@Nullable
	TypedTuple<V> popMax(long timeout, TimeUnit unit);

	/**
	 * Remove and return the value with its score having the highest score from sorted set at the bound key. <b>Blocks
	 * connection</b> until element available or {@code timeout} reached.
	 *
	 * @param timeout must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @throws IllegalArgumentException if the timeout is {@literal null} or negative.
	 * @see <a href="https://redis.io/commands/bzpopmax">Redis Documentation: BZPOPMAX</a>
	 * @since 2.6
	 */
	@Nullable
	default TypedTuple<V> popMax(Duration timeout) {

		Assert.notNull(timeout, "Timeout must not be null");
		Assert.isTrue(!timeout.isNegative(), "Timeout must not be negative");

		return popMax(TimeoutUtils.toSeconds(timeout), TimeUnit.SECONDS);
	}

	/**
	 * Returns the number of elements of the sorted set stored with given the bound key.
	 *
	 * @see #zCard()
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
	 */
	@Nullable
	Long size();

	/**
	 * Get the size of sorted set with the bound key.
	 *
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 1.3
	 * @see <a href="https://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
	 */
	@Nullable
	Long zCard();

	/**
	 * Get the score of element with {@code value} from sorted set with key the bound key.
	 *
	 * @param o the value.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
	 */
	@Nullable
	Double score(Object o);

	/**
	 * Get the scores of elements with {@code values} from sorted set with key the bound key.
	 *
	 * @param o the values.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zmscore">Redis Documentation: ZMSCORE</a>
	 * @since 2.6
	 */
	@Nullable
	List<Double> score(Object... o);

	/**
	 * Remove elements in range between {@code start} and {@code end} from sorted set with the bound key.
	 *
	 * @param start
	 * @param end
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
	 */
	@Nullable
	Long removeRange(long start, long end);

	/**
	 * Remove elements in {@link Range} from sorted set with the bound key.
	 *
	 * @param range must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.5
	 * @see <a href="https://redis.io/commands/zremrangebylex">Redis Documentation: ZREMRANGEBYLEX</a>
	 */
	@Nullable
	Long removeRangeByLex(Range range);

	/**
	 * Remove elements with scores between {@code min} and {@code max} from sorted set with the bound key.
	 *
	 * @param min
	 * @param max
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zremrangebyscore">Redis Documentation: ZREMRANGEBYSCORE</a>
	 */
	@Nullable
	Long removeRangeByScore(double min, double max);

	/**
	 * Union sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKey must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 */
	@Nullable
	Long unionAndStore(K otherKey, K destKey);

	/**
	 * Union sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKeys must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 */
	@Nullable
	Long unionAndStore(Collection<K> otherKeys, K destKey);

	/**
	 * Union sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKeys must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @param aggregate must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.1
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 */
	@Nullable
	Long unionAndStore(Collection<K> otherKeys, K destKey, Aggregate aggregate);

	/**
	 * Union sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKeys must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @param aggregate must not be {@literal null}.
	 * @param weights must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.1
	 * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
	 */
	@Nullable
	Long unionAndStore(Collection<K> otherKeys, K destKey, Aggregate aggregate, Weights weights);

	/**
	 * Intersect sorted sets at the bound key and {@code otherKey} and store result in destination {@code destKey}.
	 *
	 * @param otherKey must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 */
	@Nullable
	Long intersectAndStore(K otherKey, K destKey);

	/**
	 * Intersect sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKeys must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 */
	@Nullable
	Long intersectAndStore(Collection<K> otherKeys, K destKey);

	/**
	 * Intersect sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKeys must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @param aggregate must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.1
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 */
	@Nullable
	Long intersectAndStore(Collection<K> otherKeys, K destKey, Aggregate aggregate);

	/**
	 * Intersect sorted sets at the bound key and {@code otherKeys} and store result in destination {@code destKey}.
	 *
	 * @param otherKeys must not be {@literal null}.
	 * @param destKey must not be {@literal null}.
	 * @param aggregate must not be {@literal null}.
	 * @param weights must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.1
	 * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
	 */
	@Nullable
	Long intersectAndStore(Collection<K> otherKeys, K destKey, Aggregate aggregate, Weights weights);

	/**
	 * Iterate over elements in zset at the bound key. <br />
	 * <strong>Important:</strong> Call {@link Cursor#close()} when done to avoid resource leak.
	 *
	 * @param options
	 * @return
	 * @since 1.4
	 */
	Cursor<TypedTuple<V>> scan(ScanOptions options);

	/**
	 * Get all elements with lexicographical ordering with a value between {@link Range#getMin()} and
	 * {@link Range#getMax()}.
	 *
	 * @param range must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 1.7
	 * @see <a href="https://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
	 */
	@Nullable
	default Set<V> rangeByLex(Range range) {
		return rangeByLex(range, Limit.unlimited());
	}

	/**
	 * Get all elements {@literal n} elements, where {@literal n = } {@link Limit#getCount()}, starting at
	 * {@link Limit#getOffset()} with lexicographical ordering having a value between {@link Range#getMin()} and
	 * {@link Range#getMax()}.
	 *
	 * @param range must not be {@literal null}.
	 * @param limit can be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 1.7
	 * @see <a href="https://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
	 */
	@Nullable
	Set<V> rangeByLex(Range range, Limit limit);

	/**
	 * Get all elements with reverse lexicographical ordering with a value between {@link Range#getMin()} and
	 * {@link Range#getMax()}.
	 *
	 * @param range must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.4
	 * @see <a href="https://redis.io/commands/zrevrangebylex">Redis Documentation: ZREVRANGEBYLEX</a>
	 */
	@Nullable
	default Set<V> reverseRangeByLex(Range range) {
		return reverseRangeByLex(range, Limit.unlimited());
	}

	/**
	 * Get all elements {@literal n} elements, where {@literal n = } {@link Limit#getCount()}, starting at
	 * {@link Limit#getOffset()} with reverse lexicographical ordering having a value between {@link Range#getMin()} and
	 * {@link Range#getMax()}.
	 *
	 * @param range must not be {@literal null}.
	 * @param limit can be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 2.4
	 * @see <a href="https://redis.io/commands/zrevrangebylex">Redis Documentation: ZREVRANGEBYLEX</a>
	 */
	@Nullable
	Set<V> reverseRangeByLex(Range range, Limit limit);

	/**
	 * @return never {@literal null}.
	 */
	RedisOperations<K, V> getOperations();
}
