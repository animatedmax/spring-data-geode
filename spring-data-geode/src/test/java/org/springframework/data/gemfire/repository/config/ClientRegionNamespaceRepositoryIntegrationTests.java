/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.data.gemfire.repository.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.fork.ServerProcess;
import org.springframework.data.gemfire.process.ProcessWrapper;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.gemfire.repository.sample.PersonRepository;
import org.springframework.data.gemfire.test.support.ClientServerIntegrationTestsSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests testing and asserting basic (CRUD) data access operations using a SDG {@link GemfireRepository}
 * in a client/server topology configured with SDG's XML namespace and bootstrapped with Spring.
 *
 * @author David Turanski
 * @author John Blum
 * @see org.springframework.data.gemfire.process.ProcessWrapper
 * @see org.springframework.data.gemfire.repository.GemfireRepository
 * @see org.springframework.data.gemfire.test.support.ClientServerIntegrationTestsSupport
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SuppressWarnings("unused")
public class ClientRegionNamespaceRepositoryIntegrationTests extends ClientServerIntegrationTestsSupport {

	private static ProcessWrapper gemfireServer;

	@Autowired
	private PersonRepository repository;

	@BeforeClass
	public static void startGemFireServer() throws Exception {

		int availablePort = findAvailablePort();

		gemfireServer = run(ServerProcess.class,
			String.format("-D%s=%d", GEMFIRE_CACHE_SERVER_PORT_PROPERTY, availablePort),
			getServerContextXmlFileLocation(ClientRegionNamespaceRepositoryIntegrationTests.class));

		waitForServerToStart(DEFAULT_HOSTNAME, availablePort);

		System.setProperty(GEMFIRE_CACHE_SERVER_PORT_PROPERTY, String.valueOf(availablePort));
	}

	@AfterClass
	public static void stopGemFireServer() {
		stop(gemfireServer);
		System.clearProperty(GEMFIRE_CACHE_SERVER_PORT_PROPERTY);
	}

	@Test
	public void findAllAndCountMatch() {

		assertThat(this.repository.count()).isEqualTo(2);
		assertThat(this.repository.findAll()).hasSize(2);
	}
}
