/*
 * Copyright 2017-2024 the original author or authors.
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
 *  limitations under the License.
 */
package org.springframework.data.redis.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * Unit Tests for {@link RedisAssertions}.
 *
 * @author John Blum
 */
@ExtendWith(MockitoExtension.class)
class RedisAssertionsUnitTests {

	@Mock
	private Supplier<String> mockSupplier;

	@Test
	void requireNonNullWithMessageAndArgumentsIsSuccessful() {
		assertThat(RedisAssertions.requireNonNull("test", "Test message")).isEqualTo("test");
	}

	@Test
	void requireNonNullWithMessageAndArgumentsThrowsIllegalArgumentException() {

		assertThatIllegalArgumentException()
			.isThrownBy(() -> RedisAssertions.requireNonNull(null, "This is a %s", "test"))
			.withMessage("This is a test")
			.withNoCause();
	}

	@Test
	void requireNonNullWithSupplierIsSuccessful() {

		assertThat(RedisAssertions.requireNonNull("mock", this.mockSupplier)).isEqualTo("mock");

		verifyNoInteractions(this.mockSupplier);
	}

	@Test
	void requireNonNullWithSupplierThrowsIllegalArgumentException() {

		doReturn("Mock message").when(this.mockSupplier).get();

		assertThatIllegalArgumentException()
			.isThrownBy(() -> RedisAssertions.requireNonNull(null, this.mockSupplier))
			.withMessage("Mock message")
			.withNoCause();

		verify(this.mockSupplier, times(1)).get();
		verifyNoMoreInteractions(this.mockSupplier);
	}

	@Test
	void requireNonNullWithRuntimeExceptionSupplierIsSuccessful() {

		assertThat(RedisAssertions.requireNonNull("mock", () -> new InvalidDataAccessApiUsageException("TEST")))
			.isEqualTo("mock");
	}

	@Test
	@SuppressWarnings("all")
	void requireNonNullWithThrowsRuntimeException() {

		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
			.isThrownBy(() -> RedisAssertions.requireNonNull(null,
				() -> new InvalidDataAccessApiUsageException("TEST")))
			.withMessage("TEST")
			.withNoCause();
	}

	@Test
	void requireStateWithMessageAndArgumentsIsSuccessful() {
		assertThat(RedisAssertions.requireState("test", "Mock message")).isEqualTo("test");
	}

	@Test
	void requireStateWithMessageAndArgumentsThrowsIllegalStateException() {

		assertThatIllegalStateException()
			.isThrownBy(() -> RedisAssertions.requireState(null, "This is a %s", "test"))
			.withMessage("This is a test")
			.withNoCause();
	}

	@Test
	void requireStateWithSupplierIsSuccessful() {

		assertThat(RedisAssertions.requireState("test", this.mockSupplier)).isEqualTo("test");

		verifyNoInteractions(this.mockSupplier);
	}

	@Test
	void requiredStateWithSupplierThrowsIllegalStateException() {

		doReturn("Mock message").when(this.mockSupplier).get();

		assertThatIllegalStateException()
			.isThrownBy(() -> RedisAssertions.requireState(null, this.mockSupplier))
			.withMessage("Mock message")
			.withNoCause();

		verify(this.mockSupplier, times(1)).get();
		verifyNoMoreInteractions(this.mockSupplier);
	}
}
