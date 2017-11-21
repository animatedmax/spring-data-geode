/*
 * Copyright 2012 the original author or authors.
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
 *
 */

package org.springframework.data.gemfire.config.annotation;

import static java.util.Arrays.stream;
import static org.springframework.data.gemfire.util.ArrayUtils.nullSafeArray;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.gemfire.RegionLookupFactoryBean;
import org.springframework.data.gemfire.config.annotation.support.AbstractAnnotationConfigSupport;
import org.springframework.data.gemfire.config.annotation.support.EmbeddedServiceConfigurationSupport;
import org.springframework.data.gemfire.util.CollectionUtils;
import org.springframework.data.gemfire.util.PropertiesBuilder;

/**
 * The {@link OffHeapConfiguration} class is a Spring {@link ImportBeanDefinitionRegistrar} capable of
 * enabling Pivotal GemFire/Apache Geode cache {@link Region Regions} to use Off-Heap memory for data storage.
 *
 * @author John Blum
 * @see org.springframework.context.annotation.ImportBeanDefinitionRegistrar
 * @see org.springframework.data.gemfire.config.annotation.EnableOffHeap
 * @see org.springframework.data.gemfire.config.annotation.support.EmbeddedServiceConfigurationSupport
 * @since 1.9.0
 */
public class OffHeapConfiguration extends EmbeddedServiceConfigurationSupport {

	/**
	 * Returns the {@link EnableOffHeap} {@link java.lang.annotation.Annotation} {@link Class} type.
	 *
	 * @return the {@link EnableOffHeap} {@link java.lang.annotation.Annotation} {@link Class} type.
	 * @see org.springframework.data.gemfire.config.annotation.EnableOffHeap
	 */
	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return EnableOffHeap.class;
	}

	/* (non-Javadoc) */
	@Override
	protected void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
			Map<String, Object> annotationAttributes, BeanDefinitionRegistry registry) {

		BeanDefinitionBuilder builder =
			BeanDefinitionBuilder.genericBeanDefinition(OffHeapBeanFactoryPostProcessor.class);

		builder.addConstructorArgValue(resolveProperty(cacheOffHeapProperty("region-names"),
			String[].class, (String[]) annotationAttributes.get("regionNames")));

		registry.registerBeanDefinition(generateBeanName(OffHeapBeanFactoryPostProcessor.class),
			builder.getBeanDefinition());
	}

	/* (non-Javadoc) */
	@Override
	protected Properties toGemFireProperties(Map<String, Object> annotationAttributes) {

		return PropertiesBuilder.create()
			.setProperty("off-heap-memory-size",
				resolveProperty(cacheOffHeapProperty("memory-size"),
					(String) annotationAttributes.get("memorySize")))
			.build();
	}

	/* (non-Javadoc) */
	@SuppressWarnings("unused")
	protected static class OffHeapBeanFactoryPostProcessor extends AbstractAnnotationConfigSupport
			implements BeanFactoryPostProcessor {

		private final Set<String> regionNames;

		protected OffHeapBeanFactoryPostProcessor(Set<String> regionNames) {
			this.regionNames = CollectionUtils.nullSafeSet(regionNames);
		}

		@Override
		protected Class<? extends Annotation> getAnnotationType() {
			throw new UnsupportedOperationException("Not Implemented");
		}

		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

			stream(nullSafeArray(beanFactory.getBeanDefinitionNames(), String.class)).forEach(beanName ->
				Optional.of(beanFactory.getBeanDefinition(beanName))
					.filter(beanDefinition -> isTargetedRegionBean(beanName, beanDefinition, beanFactory))
					.ifPresent(beanDefinition -> beanDefinition.getPropertyValues()
						.addPropertyValue("offHeap", true)));
		}

		boolean isTargetedRegionBean(String beanName, BeanDefinition beanDefinition,
				ConfigurableListableBeanFactory beanFactory) {

			return isNamedRegion(beanName, beanDefinition, beanFactory) && isRegionBean(beanDefinition, beanFactory);
		}

		boolean isRegionBean(BeanDefinition beanDefinition, ConfigurableListableBeanFactory beanFactory) {

			return Optional.ofNullable(beanDefinition)
				.flatMap(it -> resolveBeanClass(it, beanFactory.getBeanClassLoader()))
				.filter(beanClass -> RegionLookupFactoryBean.class.isAssignableFrom(beanClass))
				.isPresent();
		}

		boolean isNamedRegion(String beanName, BeanDefinition beanDefinition, BeanFactory beanFactory) {

			return CollectionUtils.isEmpty(regionNames)
				|| CollectionUtils.containsAny(regionNames, getBeanNames(beanName, beanDefinition, beanFactory));
		}

		Collection<String> getBeanNames(String beanName, BeanDefinition beanDefinition, BeanFactory beanFactory) {

			Collection<String> beanNames = new HashSet<>();

			beanNames.add(beanName);

			Collections.addAll(beanNames, beanFactory.getAliases(beanName));

			PropertyValue regionName = beanDefinition.getPropertyValues().getPropertyValue("regionName");

			if (regionName != null) {

				Object regionNameValue = regionName.getValue();

				if (regionNameValue != null) {
					beanNames.add(regionNameValue.toString());
				}
			}

			return beanNames;
		}
	}
}
