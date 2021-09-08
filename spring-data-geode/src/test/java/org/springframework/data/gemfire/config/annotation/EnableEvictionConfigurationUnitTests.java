/*
 * Copyright 2016-2021 the original author or authors.
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
 *
 */
package org.springframework.data.gemfire.config.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.data.gemfire.config.annotation.EnableEviction.EvictionPolicy;

import org.junit.After;
import org.junit.Test;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.EvictionAttributes;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.util.ObjectSizer;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.eviction.EvictionActionType;
import org.springframework.data.gemfire.eviction.EvictionAttributesFactoryBean;
import org.springframework.data.gemfire.eviction.EvictionPolicyType;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.gemfire.tests.mock.annotation.EnableGemFireMockObjects;
import org.springframework.data.gemfire.util.ArrayUtils;

/**
 * Unit Tests for the {@link EnableEviction} annotation and {@link EvictionConfiguration} class.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.mockito.Mockito
 * @see org.apache.geode.cache.EvictionAttributes
 * @see org.apache.geode.cache.Region
 * @see org.springframework.data.gemfire.config.annotation.EnableEviction
 * @see org.springframework.data.gemfire.config.annotation.EvictionConfiguration
 * @see org.springframework.data.gemfire.eviction.EvictionAttributesFactoryBean
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.data.gemfire.tests.mock.annotation.EnableGemFireMockObjects
 * @since 1.9.0
 */
public class EnableEvictionConfigurationUnitTests extends IntegrationTestsSupport {

	private ConfigurableApplicationContext applicationContext;

	@After
	public void tearDown() {
		closeApplicationContext(this.applicationContext);
		destroyAllGemFireMockObjects();
	}

	private void assertEvictionAttributes(Region<?, ?> region, EvictionAttributes expectedEvictionAttributes) {

		assertThat(region).isNotNull();
		assertThat(region.getAttributes()).isNotNull();
		assertEvictionAttributes(region.getAttributes().getEvictionAttributes(), expectedEvictionAttributes);
	}

	private void assertEvictionAttributes(EvictionAttributes actualEvictionAttributes,
			EvictionAttributes expectedEvictionAttributes) {

		assertThat(actualEvictionAttributes).isNotNull();
		assertThat(actualEvictionAttributes.getAction()).isEqualTo(expectedEvictionAttributes.getAction());
		assertThat(actualEvictionAttributes.getAlgorithm()).isEqualTo(expectedEvictionAttributes.getAlgorithm());
		assertThat(actualEvictionAttributes.getObjectSizer()).isEqualTo(expectedEvictionAttributes.getObjectSizer());

		if (!EvictionPolicyType.HEAP_PERCENTAGE.equals(
				EvictionPolicyType.valueOf(actualEvictionAttributes.getAlgorithm()))) {
			assertThat(actualEvictionAttributes.getMaximum()).isEqualTo(expectedEvictionAttributes.getMaximum());
		}
	}

	@SuppressWarnings("unchecked")
	protected <K, V> Region<K, V> getRegion(String beanName) {
		return applicationContext.getBean(beanName, Region.class);
	}

	private AnnotationConfigApplicationContext newApplicationContext(Class<?>... annotatedClasses) {
		return new AnnotationConfigApplicationContext(annotatedClasses);
	}

	private EvictionAttributes newEvictionAttributes(Integer maximum, EvictionPolicyType type, EvictionActionType action,
			ObjectSizer... objectSizer) {

		EvictionAttributesFactoryBean evictionAttributesFactory = new EvictionAttributesFactoryBean();

		evictionAttributesFactory.setAction(action.getEvictionAction());
		evictionAttributesFactory.setObjectSizer(ArrayUtils.getFirst(objectSizer));
		evictionAttributesFactory.setThreshold(maximum);
		evictionAttributesFactory.setType(type);
		evictionAttributesFactory.afterPropertiesSet();

		return evictionAttributesFactory.getObject();
	}

	@Test
	public void usesDefaultEvictionPolicyConfiguration() {

		applicationContext = newApplicationContext(DefaultEvictionPolicyConfiguration.class);

		EvictionAttributes defaultEvictionAttributes = EvictionAttributes.createLRUEntryAttributes();

		assertEvictionAttributes(applicationContext.getBean("PartitionRegion", Region.class), defaultEvictionAttributes);
		assertEvictionAttributes(applicationContext.getBean("ReplicateRegion", Region.class), defaultEvictionAttributes);
	}

	@Test
	public void usesCustomEvictionPolicyConfiguration() {

		applicationContext = newApplicationContext(CustomEvictionPolicyConfiguration.class);

		ObjectSizer mockObjectSizer = applicationContext.getBean("mockObjectSizer", ObjectSizer.class);

		EvictionAttributes customEvictionAttributes =
			newEvictionAttributes(65536, EvictionPolicyType.MEMORY_SIZE, EvictionActionType.OVERFLOW_TO_DISK,
				mockObjectSizer);

		assertEvictionAttributes(applicationContext.getBean("PartitionRegion", Region.class), customEvictionAttributes);
		assertEvictionAttributes(applicationContext.getBean("ReplicateRegion", Region.class), customEvictionAttributes);
	}

	@Test
	public void usesRegionSpecificEvictionPolicyConfiguration() {

		applicationContext = newApplicationContext(RegionSpecificEvictionPolicyConfiguration.class);

		ObjectSizer mockObjectSizer = applicationContext.getBean("mockObjectSizer", ObjectSizer.class);

		EvictionAttributes partitionRegionEvictionAttributes =
			newEvictionAttributes(null, EvictionPolicyType.HEAP_PERCENTAGE, EvictionActionType.OVERFLOW_TO_DISK,
				mockObjectSizer);

		EvictionAttributes replicateRegionEvictionAttributes = newEvictionAttributes(10000,
			EvictionPolicyType.ENTRY_COUNT, EvictionActionType.LOCAL_DESTROY);

		assertEvictionAttributes(applicationContext.getBean("PartitionRegion", Region.class),
			partitionRegionEvictionAttributes);

		assertEvictionAttributes(applicationContext.getBean("ReplicateRegion", Region.class),
			replicateRegionEvictionAttributes);
	}

	@Test
	public void usesLastMatchingEvictionPolicyConfiguration() {

		applicationContext = newApplicationContext(LastMatchingWinsEvictionPolicyConfiguration.class);

		EvictionAttributes lastMatchingEvictionAttributes =
			newEvictionAttributes(99, EvictionPolicyType.ENTRY_COUNT, EvictionActionType.OVERFLOW_TO_DISK);

		assertEvictionAttributes(applicationContext.getBean("PartitionRegion", Region.class),
			lastMatchingEvictionAttributes);

		assertEvictionAttributes(applicationContext.getBean("ReplicateRegion", Region.class),
			lastMatchingEvictionAttributes);
	}

	@PeerCacheApplication
	@EnableGemFireMockObjects
	@SuppressWarnings("unused")
	static class CacheRegionConfiguration {

		@Bean("PartitionRegion")
		PartitionedRegionFactoryBean<Object, Object> mockPartitionRegion(Cache gemfireCache) {

			PartitionedRegionFactoryBean<Object, Object> partitionRegion =
				new PartitionedRegionFactoryBean<>();

			partitionRegion.setCache(gemfireCache);
			partitionRegion.setPersistent(false);

			return partitionRegion;
		}

		@Bean("ReplicateRegion")
		ReplicatedRegionFactoryBean<Object, Object> mockReplicateRegion(Cache gemfireCache) {

			ReplicatedRegionFactoryBean<Object, Object> replicateRegion =
				new ReplicatedRegionFactoryBean<>();

			replicateRegion.setCache(gemfireCache);
			replicateRegion.setPersistent(false);

			return replicateRegion;
		}

		@Bean
		ObjectSizer mockObjectSizer() {
			return mock(ObjectSizer.class);
		}
	}

	@EnableEviction
	static class DefaultEvictionPolicyConfiguration extends CacheRegionConfiguration { }

	@EnableEviction(policies = @EvictionPolicy(maximum = 65536, type = EvictionPolicyType.MEMORY_SIZE,
		action = EvictionActionType.OVERFLOW_TO_DISK, objectSizerName = "mockObjectSizer"))
	static class CustomEvictionPolicyConfiguration extends CacheRegionConfiguration { }

	@EnableEviction(policies = {
		@EvictionPolicy(maximum = 85, type = EvictionPolicyType.HEAP_PERCENTAGE, action = EvictionActionType.OVERFLOW_TO_DISK,
			objectSizerName = "mockObjectSizer", regionNames = "PartitionRegion"),
		@EvictionPolicy(maximum = 10000, type = EvictionPolicyType.ENTRY_COUNT, action = EvictionActionType.LOCAL_DESTROY,
			regionNames = "ReplicateRegion")
	})
	static class RegionSpecificEvictionPolicyConfiguration extends CacheRegionConfiguration { }

	@EnableEviction(policies = {
		@EvictionPolicy(maximum = 1, type = EvictionPolicyType.ENTRY_COUNT, action = EvictionActionType.LOCAL_DESTROY,
			objectSizerName = "mockObjectSizer", regionNames = "ReplicateRegion"),
		@EvictionPolicy(maximum = 99, type = EvictionPolicyType.ENTRY_COUNT, action = EvictionActionType.OVERFLOW_TO_DISK)
	})
	static class LastMatchingWinsEvictionPolicyConfiguration extends CacheRegionConfiguration { }

}
