---
title: Working with GemFire Serialization
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












To improve overall performance of the GemFire In-memory Data
Grid, GemFire supports a dedicated serialization protocol,
called PDX, that is both faster and offers more compact results over
standard Java serialization in addition to working transparently across
various language platforms (Java, C++, and .NET).



See
{x-data-store-docs}/developing/data_serialization/PDX_Serialization_Features.html\[PDX
Serialization Features\] and
{x-data-store-wiki}/PDX+Serialization+Internals\[PDX Serialization
Internals\] for more details.



This chapter discusses the various ways in which Spring Data for GemFire simplifies
and improves GemFire's custom serialization in Java.





## Wiring deserialized instances




It is fairly common for serialized objects to have transient data.
Transient data is often dependent on the system or environment where it
lives at a certain point in time. For instance, a `DataSource` is
environment specific. Serializing such information is useless and
potentially even dangerous, since it is local to a certain VM or
machine. For such cases, Spring Data for GemFire offers a special
{x-data-store-javadoc}/org/apache/geode/Instantiator.html\[`Instantiator`\]
that performs wiring for each new instance created by GemFire
during deserialization.



Through such a mechanism, you can rely on the Spring container to inject
and manage certain dependencies, making it easy to split transient from
persistent data and have rich domain objects in a transparent manner.



Spring users might find this approach similar to that of
{spring-framework-docs}/#aop-atconfigurable\[`@Configurable`\]). The
`WiringInstantiator` works similarly to `WiringDeclarableSupport`,
trying to first locate a bean definition as a wiring template and
otherwise falling back to auto-wiring.



See the previous section ([\[apis:declarable\]](#apis:declarable)) for
more details on wiring functionality.



To use the SDG `Instantiator`, declare it as a bean, as the
following example shows:




``` highlight
<bean id="instantiator" class="org.springframework.data.gemfire.serialization.WiringInstantiator">
  <!-- DataSerializable type -->
  <constructor-arg>org.pkg.SomeDataSerializableClass</constructor-arg>
  <!-- type id -->
  <constructor-arg>95</constructor-arg>
</bean>
```




During the Spring container startup, once it has been initialized, the
`Instantiator`, by default, registers itself with the GemFire
serialization system and performs wiring on all instances of
`SomeDataSerializableClass` created by GemFire during
deserialization.





## Auto-generating Custom `Instantiators`




For data intensive applications, a large number of instances might be
created on each machine as data flows in. GemFire uses
reflection to create new types, but, for some scenarios, this might
prove to be expensive. As always, it is good to perform profiling to
quantify whether this is the case or not. For such cases, Spring Data for GemFire
allows the automatic generation of `Instatiator` classes, which
instantiate a new type (using the default constructor) without the use
of reflection. The following example shows how to create an
instantiator:




``` highlight
<bean id="instantiatorFactory" class="org.springframework.data.gemfire.serialization.InstantiatorFactoryBean">
  <property name="customTypes">
    <map>
      <entry key="org.pkg.CustomTypeA" value="1025"/>
      <entry key="org.pkg.CustomTypeB" value="1026"/>
    </map>
  </property>
</bean>
```




The preceding definition automatically generates two `Instantiators` for
two classes (`CustomTypeA` and `CustomTypeB`) and registers them with
GemFire under user ID `1025` and `1026`. The two
`Instantiators` avoid the use of reflection and create the instances
directly through Java code.





<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700


