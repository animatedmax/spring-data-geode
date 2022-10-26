---
title:  Spring Session for VMware GemFire 2.7.0 Documentation
---

<!-- 
 Copyright (c) VMware, Inc. 2022. All rights reserved.
 Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 agreements. See the NOTICE file distributed with this work for additional information regarding
 copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance with the License. You may obtain a
 copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 or implied. See the License for the specific language governing permissions and limitations under
 the License.
-->

<!--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

Spring Session for VMware GemFire provides an implementation of the core [Spring Session](https://spring.io/projects/spring-session) framework using VMware GemFire to manage a user's _Session_ information by leveraging Spring Data for VMware GemFire.

By integrating with VMware GemFire, you have the full power of this technology (_Strong Consistency_, _Low Latency_, _High Availability_, _Resiliency_, etc.) at your fingertips in your Spring Boot or Spring Data applications.

This reference guide explains how to add the Spring Session for VMware GemFire dependency to your Spring Boot or Spring Data project. Once the dependency has been added, refer to the [Spring Boot for Apache Geode Reference Guide](https://docs.spring.io/spring-boot-data-geode-build/current/reference/html5/) and [Spring Session](https://docs.spring.io/spring-session-data-geode/docs/current/reference/html5/)for in-depth information about using the dependency.

## <a id="add-to-project"></a>Add Spring Session for VMware GemFire to a Project

The Spring Session for VMware GemFire dependency is available from the [Pivotal Commercial Maven Repository](https://commercial-repo.pivotal.io/login/auth). Access to the Pivotal Commercial Maven Repository requires a one-time registration step to create an account.

Spring Session for VMware GemFire requires users to add the GemFire repository to their projects.

To add Spring Session for VMware GemFire to a project:

1. In a browser, navigate to the [Pivotal Commercial Maven Repository](https://commercial-repo.pivotal.io/login/auth).

2. Click the **Create Account** link.

3. Complete the information in the registration page.

4. Click **Register**.

5. After registering, you will receive a confirmation email. Follow the instruction in this email to activate your account.

6. After account activation, log in to the  [Pivotal Commercial Maven Repository](https://commercial-repo.pivotal.io/login/auth) to access the configuration information found in [gemfire-release-repo](https://commercial-repo.pivotal.io/repository/gemfire-release-repo).

7. Add the GemFire repository to your project:

    * **Maven**: Add the following block to the `pom.xml` file:

        ```xml
        <repository>
            <id>gemfire-release-repo</id>
            <name>Pivotal GemFire Release Repository</name>
            <url>https://commercial-repo.pivotal.io/data3/gemfire-release-repo/gemfire</url>
        </repository>
        ```

    * **Gradle**: Add the following block to the `build.gradle` file:

        ```
        maven {
            credentials {
                username "$gemfireRepoUserName"
                password "$gemfireRepoPassword"
            }
            url = uri("https://commercial-repo.pivotal.io/data3/gemfire-release-repo/gemfire")
        }
        ```

8. Add your Pivotal Commercial Maven Repository credentials.

    * **Maven**

        Add the following to the `.m2/settings.xml` file. Replace `MY-USERNAME@example` and `MY-DECRYPTED-PASSWORD` with your Pivotal Commercial Maven Repository credentials.
        ```
        <settings>
            <servers>
                <server>
                    <id>gemfire-release-repo</id>
                    <username>MY-USERNAME@example.com</username>
                    <password>MY-DECRYPTED-PASSWORD</password>
                </server>
            </servers>
        </settings>
        ```
        Replace `MY-USERNAME@example` and `MY-DECRYPTED-PASSWORD` with your Pivotal Commercial Maven Repository credentials.

    * **Gradle**

        Add the following to the local (`.gradle/gradle.properties`) or project `gradle.properties` file. Replace `MY-USERNAME@example` and `MY-DECRYPTED-PASSWORD` with your Pivotal Commercial Maven Repository credentials.
        ```
        gemfireRepoUsername=MY-USERNAME@example.com
        gemfireRepoPassword=MY-DECRYPTED-PASSWORD
        ```

9. After you have set up the repository and credentials, add the Spring Session for VMware GemFire dependency to your application.

    **For Spring Boot Applications**

        The `<artifactId>` should match the **major.minor** version of GemFire that your application is connecting with. For example, if you are using GemFire version 9.15.1, then the `artifactId` will be `spring-data-gemfire-9.15`.

        The `<version>` should match the **major.minor** version of Spring Boot that your application is using. For example, if you are using Spring Boot version 2.7.4, then the `version` will be `2.7.0`.
        * **Maven**

            ```
            <dependency>
                <groupId>com.vmware.gemfire</groupId>
                <artifactId>spring-gemfire-starter-session-[GEMFIRE VERSION]</artifactId>
                <version>[Spring Boot MAJOR.MINOR Version]</version>
            </dependency>
            ```

        * **Gradle**

            ```
            implementation "com.vmware.gemfire:spring-gemfire-starter-session-[GemFire Version]:[Spring Boot MAJOR.MINOR Version]"
            ```

    **For Spring Data Applications**

        The `<artifactId>` should match the **major.minor version of GemFire that your application is connecting with.  For example, if you are using GemFire version 9.15.1, then the `artifactId` will be `spring-data-gemfire-9.15`.

        The `<version>` should match the **major.minor** version of Spring Data that your application is using. For example, if you are using Spring Data version 2.7.4, then the `version` will be `2.7.0`  The **patch** version does not correlate with the patch version of Spring Session, and is updated independently with each Spring Session for VMware GemFire release.
        * **Maven**

        <dependency>
            <groupId>com.vmware.gemfire</groupId>
            <artifactId>spring-session-gemfire-[GEMFIRE VERSION]</artifactId>
            <version>[Spring Boot MAJOR.MINOR Version]</version>
        </dependency>

        * **Gradle**

            ```
            implementation "com.vmware.gemfire:spring-session-gemfire-[GEMFIRE VERSION]:[Spring Boot MAJOR.MINOR Version]"
            ```


Your Spring Boot application is now ready to connect with your GemFire instance.

### <a id="modules"></a>Modules

To enable additional functionality, such as Spring Boot Actuator or Spring Session, declare any of the following modules in your .pom or .gradle file (in addition to the `spring-gemfire-starter` dependency):

* `spring-gemfire-starter-actuator-[GemFire Version]:[Spring Boot Major.Minor]`
* `spring-gemfire-starter-logging-[GemFire Version]:[Spring Boot Major.Minor]`
* `spring-gemfire-starter-session-[GemFire Version]:[Spring Boot Major.Minor]`


## <a id="reference-guide"></a>Reference Guide

For further information, refer to the [Spring Boot for Apache Geode Reference Guide](https://docs.spring.io/spring-boot-data-geode-build/current/reference/html5/) and [Spring Session](https://docs.spring.io/spring-session-data-geode/docs/current/reference/html5/) reference documentation.
