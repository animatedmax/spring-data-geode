---
title: POJO Mapping
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







This section covers:


<div class="ulist">

- [Entity Mapping](#mapping.entities)

- [Repository Mapping](#mapping.repositories)

- [MappingPdxSerializer](#mapping.pdx-serializer)



Unresolved directive in mapping.adoc -
include::../{spring-data-commons-include}/object-mapping.adoc\[leveloffset=+1\]





## Entity Mapping




Spring Data for GemFire provides support to map entities that are stored in a Region.
The mapping metadata is defined by using annotations on application
domain classes, as the following example shows:


<div class="exampleblock">

<div class="title">

Example 1. Mapping a domain class to a GemFire Region





```highlight
@Region("People")
public class Person {

  @Id Long id;

  String firstname;
  String lastname;

  @PersistenceConstructor
  public Person(String firstname, String lastname) {
    // …
  }

  …
}
```






The `@Region` annotation can be used to customize the Region in which an
instance of the `Person` class is stored. The `@Id` annotation can be
used to annotate the property that should be used as the cache Region
key, identifying the Region entry. The `@PersistenceConstructor`
annotation helps to disambiguate multiple potentially available
constructors, taking parameters and explicitly marking the constructor
annotated as the constructor to be used to construct entities. In an
application domain class with no or only a single constructor, you can
omit the annotation.



In addition to storing entities in top-level Regions, entities can be
stored in Sub-Regions as well, as the following example shows:




```highlight
@Region("/Users/Admin")
public class Admin extends User {
  …
}

@Region("/Users/Guest")
public class Guest extends User {
  …
}
```




Be sure to use the full path of the GemFire Region, as defined
with the Spring Data for GemFire XML namespace by using the `id` or `name` attributes
of the `<*-region>` element.


<div class="sect2">

### Entity Mapping by Region Type


In addition to the `@Region` annotation, Spring Data for GemFire also recognizes
type-specific Region mapping annotations: `@ClientRegion`,
`@LocalRegion`, `@PartitionRegion`, and `@ReplicateRegion`.



Functionally, these annotations are treated exactly the same as the
generic `@Region` annotation in the SDG mapping
infrastructure. However, these additional mapping annotations are useful
in Spring Data for GemFire's annotation configuration model. When combined with the
`@EnableEntityDefinedRegions` configuration annotation on a Spring
`@Configuration` annotated class, it is possible to generate Regions in
the local cache, whether the application is a client or peer.



These annotations let you be more specific about what type of Region
your application entity class should be mapped to and also has an impact
on the data management policies of the Region (for example,
partition — also known as sharding — versus replicating data).



Using these type-specific Region mapping annotations with the
SDG annotation configuration model saves you from having to
explicitly define these Regions in configuration.






## Repository Mapping




As an alternative to specifying the Region in which the entity is stored
by using the `@Region` annotation on the entity class, you can also
specify the `@Region` annotation on the entity's `Repository` interface.
See [\[gemfire-repositories\]](#gemfire-repositories) for more details.



However, suppose you want to store a `Person` record in multiple
GemFire Regions (for example, `People` and `Customers`). Then
you can define your corresponding `Repository` interface extensions as
follows:




```highlight
@Region("People")
public interface PersonRepository extends GemfireRepository<Person, String> {
…
}

@Region("Customers")
public interface CustomerRepository extends GemfireRepository<Person, String> {
...
}
```




Then, using each Repository individually, you can store the entity in
multiple GemFire Regions, as the following example shows:




```highlight
@Service
class CustomerService {

  CustomerRepository customerRepo;

  PersonRepository personRepo;

  Customer update(Customer customer) {
    customerRepo.save(customer);
    personRepo.save(customer);
    return customer;
  }
```




You can even wrap the `update` service method in a Spring managed
transaction, either as a local cache transaction or a global
transaction.





## MappingPdxSerializer




Spring Data for GemFire provides a custom
{x-data-store-javadoc}/org/apache/geode/pdx/PdxSerializer.html\[`PdxSerializer`\]
implementation, called `MappingPdxSerializer`, that uses Spring Data
mapping metadata to customize entity serialization.



The serializer also lets you customize entity instantiation by using the
Spring Data `EntityInstantiator` abstraction. By default, the serializer
use the `ReflectionEntityInstantiator`, which uses the persistence
constructor of the mapped entity. The persistence constructor is either
the default constructor, a singly declared constructor, or a constructor
explicitly annotated with `@PersistenceConstructor`.



To provide arguments for constructor parameters, the serializer reads
fields with the named constructor parameter, explicitly identified by
using Spring's `@Value` annotation, from the supplied
{x-data-store-javadoc}/org/apache/geode/pdx/PdxReader.html\[`PdxReader`\],
as shown in the following example:


<div class="exampleblock">

<div class="title">

Example 2. Using `@Value` on entity constructor parameters





```highlight
public class Person {

  public Person(@Value("#root.thing") String firstName, @Value("bean") String lastName) {
    …
  }
}
```






An entity class annotated in this way has the "thing" field read from
the `PdxReader` and passed as the argument value for the constructor
parameter, `firstname`. The value for `lastName` is a Spring bean with
the name "bean".



In addition to the custom instantiation logic and strategy provided by
`EntityInstantiators`, the `MappingPdxSerializer` also provides
capabilities well beyond GemFire's own
{x-data-store-javadoc}/org/apache/geode/pdx/ReflectionBasedAutoSerializer.html\[`ReflectionBasedAutoSerializer`\].



While GemFire's `ReflectionBasedAutoSerializer` conveniently
uses Java Reflection to populate entities and uses regular expressions
to identify types that should be handled (serialized and deserialized)
by the serializer, it cannot, unlike `MappingPdxSerializer`, perform the
following:


<div class="ulist">

- Register custom `PdxSerializer` objects per entity field or property
  names and types.

- Conveniently identifies ID properties.

- Automatically handles read-only properties.

- Automatically handles transient properties.

- Allows more robust type filtering in a `null` and type-safe manner
  (for example, not limited to only expressing types with regex).



We now explore each feature of the `MappingPdxSerializer` in a bit more
detail.


<div class="sect2">

### Custom PdxSerializer Registration


The `MappingPdxSerializer` gives you the ability to register custom
`PdxSerializers` based on an entity's field or property names and types.



For example, suppose you have defined an entity type modeling a `User`
as follows:




```highlight
package example.app.security.auth.model;

public class User {

  private String name;

  private Password password;

  ...
}
```




While the user's name probably does not require any special logic to
serialize the value, serializing the password on the other hand might
require additional logic to handle the sensitive nature of the field or
property.



Perhaps you want to protect the password when sending the value over the
network, between a client and a server, beyond TLS alone, and you only
want to store the salted hash. When using the `MappingPdxSerializer`,
you can register a custom `PdxSerializer` to handle the user's password,
as follows:


<div class="exampleblock">

<div class="title">

Example 3. Registering custom `PdxSerializers` by POJO field/property
type





```highlight
Map<?, PdxSerializer> customPdxSerializers = new HashMap<>();

customPdxSerializers.put(Password.class, new SaltedHashPasswordPdxSerializer());

mappingPdxSerializer.setCustomPdxSerializers(customPdxSerializers);
```






After registering the application-defined
`SaltedHashPasswordPdxSerializer` instance with the `Password`
application domain model type, the `MappingPdxSerializer` will then
consult the custom `PdxSerializer` to serialize and deserialize all
`Password` objects regardless of the containing object (for example,
`User`).



However, suppose you want to customize the serialization of `Passwords`
only on `User` objects. To do so, you can register the custom
`PdxSerializer` for the `User` type by specifying the fully qualified
name of the `Class's` field or property, as the following example shows:


<div class="exampleblock">

<div class="title">

Example 4. Registering custom `PdxSerializers` by POJO field/property
name





```highlight
Map<?, PdxSerializer> customPdxSerializers = new HashMap<>();

customPdxSerializers.put("example.app.security.auth.model.User.password", new SaltedHashPasswordPdxSerializer());

mappingPdxSerializer.setCustomPdxSerializers(customPdxSerializers);
```






Notice the use of the fully-qualified field or property name (that is
`example.app.security.auth.model.User.password`) as the custom
`PdxSerializer` registration key.




Note
</div></td>
<td class="content">You could construct the registration key by using a
more logical code snippet, such as the following:
<code>User.class.getName().concat(".password");</code>. We recommended
this over the example shown earlier. The preceding example tried to be
as explicit as possible about the semantics of registration.</td>
</tr>
</tbody>
</table>



<div class="sect2">

### Mapping ID Properties


Like GemFire's `ReflectionBasedAutoSerializer`,
SDG's `MappingPdxSerializer` is also able to determine the
identifier of the entity. However, `MappingPdxSerializer` does so by
using Spring Data's mapping metadata, specifically by finding the entity
property designated as the identifier using Spring Data's
{spring-data-commons-javadoc}/org/springframework/data/annotation/Id.html\[`@Id`\]
annotation. Alternatively, any field or property named "id", not
explicitly annotated with `@Id`, is also designated as the entity's
identifier.



For example:




```highlight
class Customer {

  @Id
  Long id;

  ...
}
```




In this case, the `Customer` `id` field is marked as the identifier
field in the PDX type metadata by using
{x-data-store-javadoc}/org/apache/geode/pdx/PdxWriter.html#markIdentityField-java.lang.String-\[`PdxWriter.markIdentifierField(:String)`\]
when the `PdxSerializer.toData(..)` method is called during
serialization.



<div class="sect2">

### Mapping Read-only Properties


What happens when your entity defines a read-only property?



First, it is important to understand what a "read-only" property is. If
you define a POJO by following the
[JavaBeans](https://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html)
specification (as Spring does), you might define a POJO with a read-only
property, as follows:




```highlight
package example;

class ApplicationDomainType {

  private AnotherType readOnly;

  public AnotherType getReadOnly() [
    this.readOnly;
  }

  ...
}
```




The `readOnly` property is read-only because it does not provide a
setter method. It only has a getter method. In this case, the `readOnly`
property (not to be confused with the `readOnly` `DomainType` field) is
considered read-only.



As a result, the `MappingPdxSerializer` will not try to set a value for
this property when populating an instance of `ApplicationDomainType` in
the `PdxSerializer.fromData(:Class<ApplicationDomainType>, :PdxReader)`
method during deserialization, particularly if a value is present in the
PDX serialized bytes.



This is useful in situations where you might be returning a view or
projection of some entity type and you only want to set state that is
writable. Perhaps the view or projection of the entity is based on
authorization or some other criteria. The point is, you can leverage
this feature as is appropriate for your application's use cases and
requirements. If you want the field or property to always be written,
simply define a setter method.



<div class="sect2">

### Mapping Transient Properties


Likewise, what happens when your entity defines `transient` properties?



You would expect the `transient` fields or properties of your entity not
to be serialized to PDX when serializing the entity. That is exactly
what happens, unlike GemFire's own
`ReflectionBasedAutoSerializer`, which serializes everything accessible
from the object through Java Reflection.



The `MappingPdxSerializer` will not serialize any fields or properties
that are qualified as being transient, either by using Java's own
`transient` keyword (in the case of class instance fields) or by using
the
{spring-data-commons-javadoc}/org/springframework/data/annotation/Transient.html\[`@Transient`\]
Spring Data annotation on either fields or properties.



For example, you might define an entity with transient fields and
properties as follows:




```highlight
package example;

class Process {

  private transient int id;

  private File workingDirectory;

  private String name;

  private Type type;

  @Transient
  public String getHostname() {
    ...
  }

  ...
}
```




Neither the `Process` `id` field nor the readable `hostname` property
are written to PDX.



<div class="sect2">

### Filtering by Class Type


Similar to GemFire's `ReflectionBasedAutoSerializer`,
SDG's `MappingPdxSerializer` lets you filter the types of
objects that are serialized and deserialized.



However, unlike GemFire's `ReflectionBasedAutoSerializer`,
which uses complex regular expressions to express which types the
serializer handles, SDG's `MappingPdxSerializer` uses the much
more robust
[`java.util.function.Predicate`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)
interface and API to express type-matching criteria.


<div class="admonitionblock tip">


Tip
</div></td>
<td class="content">If you like to use regular expressions, you can
implement a <code>Predicate</code> using Java's <a
href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/package-summary.html">regular
expression support</a>.</td>
</tr>
</tbody>
</table>



The nice part about Java's `Predicate` interface is that you can compose
`Predicates` by using convenient and appropriate API methods, including:
[`and(:Predicate)`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html#and-java.util.function.Predicate-),
[`or(:Predicate)`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html#or-java.util.function.Predicate-),
and
[`negate()`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html#negate--).



The following example shows the `Predicate` API in action:




```highlight
Predicate<Class<?>> customerTypes =
  type -> Customer.class.getPackage().getName().startsWith(type.getName()); // Include all types in the same package as `Customer`

Predicate includedTypes = customerTypes
  .or(type -> User.class.isAssignble(type)); // Additionally, include User sub-types (e.g. Admin, Guest, etc)

mappingPdxSerializer.setIncludeTypeFilters(includedTypes);

mappingPdxSerializer.setExcludeTypeFilters(
  type -> !Reference.class.getPackage(type.getPackage()); // Exclude Reference types
```





Note
</div></td>
<td class="content">Any <code>Class</code> object passed to your
<code>Predicate</code> is guaranteed not to be <code>null</code>.</td>
</tr>
</tbody>
</table>



SDG's `MappingPdxSerializer` includes support for both include
and exclude class type filters.


<div class="sect3">

#### Exclude Type Filtering


By default, SDG's `MappingPdxSerializer` registers pre-defined
`Predicates` that filter, or exclude types from the folliowing packages:


<div class="ulist">

- `java.*`

- `com.gemstone.gemfire.*`

- `org.apache.geode.*`

- `org.springframework.*`



In addition, the `MappingPdxSerializer` filters `null` objects when
calling `PdxSerializer.toData(:Object, :PdxWriter)` and `null` class
types when calling `PdxSerializer.fromData(:Class<?>, :PdxReader)`
methods.



It is very easy to add exclusions for other class types, or an entire
package of types, by simply defining a `Predicate` and adding it to the
`MappingPdxSerializer` as shown earlier.



The `MappingPdxSerializer.setExcludeTypeFilters(:Predicate<Class<?>>)`
method is additive, meaning it composes your application-defined type
filters with the existing, pre-defined type filter `Predicates`
indicated above using the `Predicate.and(:Predicate<Class<?>>)` method.



However, what if you want to include a class type (for example,
`java.security Principal`) implicitly excluded by the exclude type
filters? See [Include Type
Filtering](#mapping.pdx-serializer.type-filtering.includes).



<div class="sect3">

#### Include Type Filtering


If you want to include a class type explicitly, or override a class type
filter that implicitly excludes a class type required by your
application (for example, `java.security.Principal`, which is excluded
by default with the `java.*` package exclude type filter on
`MappingPdxSerializer`), then just define the appropriate `Predicate`
and add it to the serializer using
`MappingPdxSerializer.setIncludeTypeFilters(:Predicate<Class<?>>)`
method, as follows:




```highlight
Predicate<Class<?>> principalTypeFilter =
  type -> java.security.Principal.class.isAssignableFrom(type);

mappingPdxSerializer.setIncludeTypeFilters(principalTypeFilters);
```




Again, the
`MappingPdxSerializer.setIncludeTypeFilters(:Predicate<Class<?>>)`
method, like `setExcludeTypeFilters(:Predicate<Class<?>>)`, is additive
and therefore composes any passed type filter using
`Predicate.or(:Predicate<Class<?>>)`. This means you may call
`setIncludeTypeFilters(:Predicate<Class<?>>)` as many time as necessary.



When include type filters are present, then the `MappingPdxSerializer`
makes a decision of whether to de/serialize an instance of a class type
when the class type is either not implicitly excluded OR when the class
type is explicitly included, whichever returns true. Then, an instance
of the class type will be serialized or deserialized appropriately.



For example, when a type filter of `Predicate<Class<Principal>>` is
explicitly registered as shown previously, it cancels out the implicit
exclude type filter on `java.*` package types.







<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700


