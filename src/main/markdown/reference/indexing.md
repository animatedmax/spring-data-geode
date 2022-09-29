---
title: Configuring an Index
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











GemFire allows indexes 
to be created on Region data to improve the performance of OQL (Object
Query Language) queries.



In Spring Data for GemFire, indexes are declared with the `index` element, as the
following example shows:




```highlight
<gfe:index id="myIndex" expression="someField" from="/SomeRegion" type="HASH"/>
```




In Spring Data for GemFire's XML schema (also called the SDG XML
namespace), `index` bean declarations are not bound to a Region, unlike
GemFire's native `cache.xml`. Rather, they are top-level
elements similar to `<gfe:cache>` element. This lets you declare any
number of indexes on any Region, whether they were just created or
already exist — a significant improvement over GemFire's
native `cache.xml` format.



An `Index` must have a name. You can give the `Index` an explicit name
by using the `name` attribute. Otherwise, the bean name (that is, the
value of the `id` attribute) of the `index` bean definition is used as
the `Index` name.



The `expression` and `from` clause form the main components of an
`Index`, identifying the data to index (that is, the Region identified
in the `from` clause) along with what criteria (that is, `expression`)
is used to index the data. The `expression` should be based on what
application domain object fields are used in the predicate of
application-defined OQL queries used to query and look up the objects
stored in the Region.



Consider the following example, which has a `lastName` property:




```highlight
@Region("Customers")
class Customer {

  @Id
  Long id;

  String lastName;
  String firstName;

  ...
}
```




Now consider the following example, which has an application-defined
SDG Repository to query for `Customer` objects:




```highlight
interface CustomerRepository extends GemfireRepository<Customer, Long> {

  Customer findByLastName(String lastName);

  ...
}
```




The SDG Repository finder/query method results in the
following OQL statement being generated and ran:




```highlight
SELECT * FROM /Customers c WHERE c.lastName = '$1'
```




Therefore, you might want to create an `Index` with a statement similar
to the following:




```highlight
<gfe:index id="myIndex" name="CustomersLastNameIndex" expression="lastName" from="/Customers" type="HASH"/>
```




The `from` clause must refer to a valid, existing Region and is how an
`Index` gets applied to a Region. This is not specific to Spring Data for GemFire. It
is a feature of GemFire.



The `Index` `type` may be one of three enumerated values defined by
Spring Data for GemFire's
{sdg-javadoc}/org/springframework/data/gemfire/IndexType.html\[`IndexType`\]
enumeration: `FUNCTIONAL`, `HASH`, and `PRIMARY_KEY`.



Each of the enumerated values corresponds to one of the
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html\[`QueryService`\]
`create[|Key|Hash]Index` methods invoked when the actual `Index` is to
be created (or "defined" — you can find more on "defining" indexes in
the next section). For instance, if the `IndexType` is `PRIMARY_KEY`,
then the
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html#createKeyIndex-java.lang.String-java.lang.String-java.lang.String-\[QueryService.createKeyIndex(..)\]
is invoked to create a `KEY` `Index`.



The default is `FUNCTIONAL` and results in one of the
`QueryService.createIndex(..)` methods being invoked. See the Spring Data for GemFire
XML schema for a full set of options.



For more information on indexing in GemFire, see "[Working
with
Indexes](https://gemfire90.docs.pivotal.io/geode/developing/query_index/query_index.html)"
in GemFire's User Guide.





## Defining Indexes




In addition to creating indexes up front as `Index` bean definitions are
processed by Spring Data for GemFire on Spring container initialization, you may also
define all of your application indexes prior to creating them by using
the `define` attribute, as follows:




```highlight
<gfe:index id="myDefinedIndex" expression="someField" from="/SomeRegion" define="true"/>
```




When `define` is set to `true` (it defaults to `false`), it does not
actually create the `Index` at that moment. All "defined" Indexes are
created all at once, when the Spring `ApplicationContext` is "refreshed"
or, to put it differently, when a `ContextRefreshedEvent` is published
by the Spring container. Spring Data for GemFire registers itself as an
`ApplicationListener` listening for the `ContextRefreshedEvent`. When
fired, Spring Data for GemFire calls
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html#createDefinedIndexes\[`QueryService.createDefinedIndexes()`\].



Defining indexes and creating them all at once boosts speed and
efficiency when creating indexes.



See "[Creating Multiple Indexes at
Once](https://gemfire90.docs.pivotal.io/geode/developing/query_index/create_multiple_indexes.html)"
for more details.





## `IgnoreIfExists` and `Override`




Two Spring Data for GemFire `Index` configuration options warrant special mention:
`ignoreIfExists` and `override`.



These options correspond to the `ignore-if-exists` and `override`
attributes on the `<gfe:index>` element in Spring Data for GemFire's XML namespace,
respectively.


<div class="admonitionblock warning">


Warning
</div></td>
<td class="content">Make sure you absolutely understand what you are
doing before using either of these options. These options can affect the
performance and resources (such as memory) consumed by your application
at runtime. As a result, both of these options are disabled (set to
<code>false</code>) in SDG by default.</td>
</tr>
</tbody>
</table>




Note
</div></td>
<td class="content">These options are only available in Spring Data for GemFire and
exist to workaround known limitations with GemFire.
GemFire has no equivalent options or functionality.</td>
</tr>
</tbody>
</table>



Each option significantly differs in behavior and entirely depends on
the type of GemFire `Index` exception thrown. This also means
that neither option has any effect if a GemFire Index-type
exception is not thrown. These options are meant to specifically handle
GemFire `IndexExistsException` and
`IndexNameConflictException` instances, which can occur for various,
sometimes obscure reasons. The exceptions have the following causes:


<div class="ulist">

- An
  {x-data-store-javadoc}/org/apache/geode/cache/query/IndexExistsException.html\[`IndexExistsException`\]
  is thrown when there exists another `Index` with the same definition
  but a different name when attempting to create an `Index`.

- An
  {x-data-store-javadoc}/org/apache/geode/cache/query/IndexNameConflictException.html\[`IndexNameConflictException`\]
  is thrown when there exists another `Index` with the same name but
  possibly different definition when attempting to create an `Index`.



Spring Data for GemFire's default behavior is to fail-fast, always. So, neither
`Index` *Exception* are "handled" by default. These `Index` exceptions
are wrapped in a SDG `GemfireIndexException` and rethrown. If
you wish for Spring Data for GemFire to handle them for you, you can set either of
these `Index` bean definition options to `true`.



`IgnoreIfExists` always takes precedence over `Override`, primarily
because it uses fewer resources, simply because it returns the
"existing" `Index` in both exceptional cases.


<div class="sect2">

### `IgnoreIfExists` Behavior


When an `IndexExistsException` is thrown and `ignoreIfExists` is set to
`true` (or `<gfe:index ignore-if-exists="true">`), then the `Index` that
would have been created by this `index` bean definition or declaration
is simply ignored, and the existing `Index` is returned.



There is little consequence in returning the existing `Index`, since the
`index` bean definition is the same, as determined by GemFire
itself, not SDG.



However, this also means that no `Index` with the "name" specified in
your `index` bean definition or declaration actually exists from
GemFire's perspective (that is, with
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html#getIndexes\[`QueryService.getIndexes()`\]).
Therefore, you should be careful when writing OQL query statements that
use query hints, especially query hints that refer to the application
`Index` being ignored. Those query hints need to be changed.



When an `IndexNameConflictException` is thrown and `ignoreIfExists` is
set to `true` (or `<gfe:index ignore-if-exists="true">`), the `Index`
that would have been created by this `index` bean definition or
declaration is also ignored, and the "existing" `Index` is again
returned, as when an `IndexExistsException` is thrown.



However, there is more risk in returning the existing `Index` and
ignoring the application's definition of the `Index` when an
`IndexNameConflictException` is thrown. For a
`IndexNameConflictException`, while the names of the conflicting indexes
are the same, the definitions could be different. This situation could
have implications for OQL queries specific to the application, where you
would presume the indexes were defined specifically with the application
data access patterns and queries in mind. However, if like-named indexes
differ in definition, this might not be the case. Consequently, you
should verify your `Index` names.




Note
</div></td>
<td class="content">SDG makes a best effort to inform the user
when the <code>Index</code> being ignored is significantly different in
its definition from the existing <code>Index</code>. However, in order
for SDG to accomplish this, it must be able to find the
existing <code>Index</code>, which is looked up by using the
GemFire API (the only means available).</td>
</tr>
</tbody>
</table>



<div class="sect2">

### `Override` Behavior


When an `IndexExistsException` is thrown and `override` is set to `true`
(or `<gfe:index override="true">`), the `Index` is effectively renamed.
Remember, `IndexExistsExceptions` are thrown when multiple indexes exist
that have the same definition but different names.



Spring Data for GemFire can only accomplish this by using GemFire's API, by
first removing the existing `Index` and then recreating the `Index` with
the new name. It is possible that either the remove or subsequent create
invocation could fail. There is no way to execute both actions
atomically and rollback this joint operation if either fails.



However, if it succeeds, then you have the same problem as before with
the `ignoreIfExists` option. Any existing OQL query statement using
query hints that refer to the old `Index` by name must be changed.



When an `IndexNameConflictException` is thrown and `override` is set to
`true` (or `<gfe:index override="true">`), the existing `Index` can
potentially be re-defined. We say "potentially" because it is possible
for the like-named, existing `Index` to have exactly the same definition
and name when an `IndexNameConflictException` is thrown.



If so, SDG is smart and returns the existing `Index` as is,
even on `override`. There is no harm in this behavior, since both the
name and the definition are exactly the same. Of course, SDG
can only accomplish this when SDG is able to find the existing
`Index`, which is dependent on GemFire's APIs. If it cannot be
found, nothing happens and a SDG `GemfireIndexException` is
thrown that wraps the `IndexNameConflictException`.



However, when the definition of the existing `Index` is different,
SDG attempts to re-create the `Index` by using the `Index`
definition specified in the `index` bean definition. Make sure this is
what you want and make sure the `index` bean definition matches your
expectations and application requirements.



<div class="sect2">

### How Does `IndexNameConflictExceptions` Actually Happen?


It is probably not all that uncommon for `IndexExistsExceptions` to be
thrown, especially when multiple configuration sources are used to
configure GemFire (Spring Data for GemFire, GemFire Cluster
Config, GemFire native `cache.xml`, the API, and so on). You
should definitely prefer one configuration method and stick with it.



However, when does an `IndexNameConflictException` get thrown?



One particular case is an `Index` defined on a `PARTITION` Region (PR).
When an `Index` is defined on a `PARTITION` Region (for example, `X`),
GemFire distributes the `Index` definition (and name) to other
peer members in the cluster that also host the same `PARTITION` Region
(that is, "X"). The distribution of this `Index` definition to, and
subsequent creation of, this `Index` by peer members is on a
need-to-know basis (that is, by peer member hosting the same PR) is
performed asynchronously.



During this window of time, it is possible that these pending PR
`Indexes` cannot be identified by GemFire — such as with a
call to
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html#getIndexes\[`QueryService.getIndexes()`\]
with
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html#getIndexes-org.apache.geode.cache.Region\[`QueryService.getIndexes(:Region)`\],
or even with
{x-data-store-javadoc}/org/apache/geode/cache/query/QueryService.html#getIndex-org.apache.geode.cache.Region-java.lang.String\[`QueryService.getIndex(:Region, indexName:String)`\].



As a result, the only way for SDG or other GemFire
cache client applications (not involving Spring) to know for sure is to
attempt to create the `Index`. If it fails with either an
`IndexNameConflictException` or even an `IndexExistsException`, the
application knows there is a problem. This is because the `QueryService`
`Index` creation waits on pending `Index` definitions, whereas the other
GemFire API calls do not.



In any case, SDG makes a best effort and attempts to inform
you what has happened or is happening and tell you the corrective
action. Given that all GemFire `QueryService.createIndex(..)`
methods are synchronous, blocking operations, the state of
GemFire should be consistent and accessible after either of
these index-type exceptions are thrown. Consequently, SDG can
inspect the state of the system and act accordingly, based on your
configuration.



In all other cases, SDG embraces a fail-fast strategy.






<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700


