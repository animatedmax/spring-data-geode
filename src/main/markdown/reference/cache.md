<div id="header">

# Configuring a Cache

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

To use {data-store-name}, you need to either create a new cache or
connect to an existing one. With the current version of
{data-store-name}, you can have only one open cache per VM (more
strictly speaking, per `ClassLoader`). In most cases, the cache should
only be created once.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">This section describes the creation and
configuration of a peer <code>Cache</code> member, appropriate in
peer-to-peer (P2P) topologies and cache servers. A <code>Cache</code>
member can also be used in stand-alone applications and integration
tests. However, in typical production systems, most application
processes act as cache clients, creating a <code>ClientCache</code>
instance instead. This is described in the <a
href="#bootstrap:cache:client">Configuring a {data-store-name}
ClientCache</a> and <a
href="#bootstrap:region:client">[bootstrap:region:client]</a>
sections.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

A peer `Cache` with default configuration can be created with the
following simple declaration:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:cache/>
```

</div>

</div>

<div class="paragraph">

During Spring container initialization, any `ApplicationContext`
containing this cache definition registers a `CacheFactoryBean` that
creates a Spring bean named `gemfireCache`, which references a
{data-store-name} `Cache` instance. This bean refers to either an
existing `Cache` or, if one does not already exist, a newly created one.
Since no additional properties were specified, a newly created `Cache`
applies the default cache configuration.

</div>

<div class="paragraph">

All {sdg-name} components that depend on the `Cache` respect this naming
convention, so you need not explicitly declare the `Cache` dependency.
If you prefer, you can make the dependency explicit by using the
`cache-ref` attribute provided by various {sdg-acronym} XML namespace
elements. Also, you can override the cache’s bean name using the `id`
attribute, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:cache id="myCache"/>
```

</div>

</div>

<div class="paragraph">

A {data-store-name} `Cache` can be fully configured using Spring.
However, {data-store-name}'s native XML configuration file, `cache.xml`,
is also supported. For situations where the {data-store-name} cache
needs to be configured natively, you can provide a reference to the
{data-store-name} XML configuration file by using the
`cache-xml-location` attribute, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:cache id="cacheConfiguredWithNativeCacheXml" cache-xml-location="classpath:cache.xml"/>
```

</div>

</div>

<div class="paragraph">

In this example, if a cache needs to be created, it uses a file named
`cache.xml` located in the classpath root to configure it.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The configuration makes use of Spring’s <a
href="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#resources"><code>Resource</code></a>
abstraction to locate the file. The <code>Resource</code> abstraction
lets various search patterns be used, depending on the runtime
environment or the prefix specified (if any) in the resource
location.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

In addition to referencing an external XML configuration file, you can
also specify {data-store-name} System
{x-data-store-docs}/reference/topics/gemfire_properties.html\[properties\]
that use any of Spring’s `Properties` support features.

</div>

<div class="paragraph">

For example, you can use the `properties` element defined in the `util`
namespace to define `Properties` directly or load properties from a
properties file, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:gfe="{spring-data-schema-namespace}"
       xmlns:util="http://www.springframework.org/schema/util"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
    {spring-data-schema-namespace} {spring-data-schema-location}
    http://www.springframework.org/schema/util https://www.springframework.org/schema/util/spring-util.xsd
">

  <util:properties id="gemfireProperties" location="file:/path/to/gemfire.properties"/>

  <gfe:cache properties-ref="gemfireProperties"/>

</beans>
```

</div>

</div>

<div class="paragraph">

Using a properties file is recommended for externalizing
environment-specific settings outside the application configuration.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Cache settings apply only when a new cache needs to
be created. If an open cache already exists in the VM, these settings
are ignored.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect1">

## Advanced Cache Configuration

<div class="sectionbody">

<div class="paragraph">

For advanced cache configuration, the `cache` element provides a number
of configuration options exposed as attributes or child elements, as the
following listing shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<!--(1)-->
<gfe:cache
    cache-xml-location=".."
    properties-ref=".."
    close="false"
    copy-on-read="true"
    critical-heap-percentage="90"
    eviction-heap-percentage="70"
    enable-auto-reconnect="false" <!--(2)-->
    lock-lease="120"
    lock-timeout="60"
    message-sync-interval="1"
    pdx-serializer-ref="myPdxSerializer"
    pdx-persistent="true"
    pdx-disk-store="diskStore"
    pdx-read-serialized="false"
    pdx-ignore-unread-fields="true"
    search-timeout="300"
    use-bean-factory-locator="true" <!--(3)-->
    use-cluster-configuration="false" <!--(4)-->
>

  <gfe:transaction-listener ref="myTransactionListener"/> <!--(5)-->

  <gfe:transaction-writer> <!--(6)-->
    <bean class="org.example.app.gemfire.transaction.TransactionWriter"/>
  </gfe:transaction-writer>

  <gfe:gateway-conflict-resolver ref="myGatewayConflictResolver"/> <!--(7)-->

  <gfe:jndi-binding jndi-name="myDataSource" type="ManagedDataSource"/> <!--(8)-->

</gfe:cache>
```

</div>

</div>

<div class="colist arabic">

1.  Attributes support various cache options. For further information
    regarding anything shown in this example, see the {data-store-name}
    [product documentation](https://docs.pivotal.io/gemfire). The
    `close` attribute determines whether the cache should be closed when
    the Spring application context is closed. The default is `true`.
    However, for use cases in which multiple application contexts use
    the cache (common in web applications), set this value to `false`.

2.  Setting the `enable-auto-reconnect` attribute to `true` (the default
    is `false`) lets a disconnected {data-store-name} member
    automatically reconnect and rejoin the {data-store-name} cluster.
    See the {data-store-name}
    {x-data-store-docs}/managing/autoreconnect/member-reconnect.html\[product
    documentation\] for more details.

3.  Setting the `use-bean-factory-locator` attribute to `true` (it
    defaults to `false`) applies only when both Spring (XML)
    configuration metadata and {data-store-name} `cache.xml` is used to
    configure the {data-store-name} cache node (whether client or peer).
    This option lets {data-store-name} components (such as
    `CacheLoader`) expressed in `cache.xml` be auto-wired with beans
    (such as `DataSource`) defined in the Spring application context.
    This option is typically used in conjunction with
    `cache-xml-location`.

4.  Setting the `use-cluster-configuration` attribute to `true` (the
    default is `false`) enables a {data-store-name} member to retrieve
    the common, shared Cluster-based configuration from a Locator. See
    the {data-store-name}
    {x-data-store-docs}/configuring/cluster_config/gfsh_persist.html\[product
    documentation\] for more details.

5.  Example of a `TransactionListener` callback declaration that uses a
    bean reference. The referenced bean must implement
    {x-data-store-javadoc}/org/apache/geode/cache/TransactionListener.html\[TransactionListener\].
    A `TransactionListener` can be implemented to handle transaction
    related events (such as afterCommit and afterRollback).

6.  Example of a `TransactionWriter` callback declaration using an inner
    bean declaration. The bean must implement
    {x-data-store-javadoc}/org/apache/geode/cache/TransactionWriter.html\[TransactionWriter\].
    The `TransactionWriter` is a callback that can veto a transaction.

7.  Example of a `GatewayConflictResolver` callback declaration using a
    bean reference. The referenced bean must implement
    {x-data-store-javadoc}/org/apache/geode/cache/util/GatewayConflictResolver.html
    \[GatewayConflictResolver\]. A `GatewayConflictResolver` is a
    `Cache`-level plugin that is called upon to decide what to do with
    events that originate in other systems and arrive through the WAN
    Gateway. which provides a distributed Region creation service.

8.  Declares a JNDI binding to enlist an external DataSource in a
    {data-store-name} transaction.

</div>

<div class="sect2">

### Enabling PDX Serialization

<div class="paragraph">

The preceding example includes a number of attributes related to
{data-store-name}'s enhanced serialization framework, PDX. While a
complete discussion of PDX is beyond the scope of this reference guide,
it is important to note that PDX is enabled by registering a
`PdxSerializer`, which is specified by setting the `pdx-serializer`
attribute.

</div>

<div class="paragraph">

{data-store-name} provides an implementing class
(`org.apache.geode.pdx.ReflectionBasedAutoSerializer`) that uses Java
Reflection. However, it is common for developers to provide their own
implementation. The value of the attribute is simply a reference to a
Spring bean that implements the `PdxSerializer` interface.

</div>

<div class="paragraph">

More information on serialization support can be found in
[\[serialization\]](#serialization).

</div>

</div>

<div class="sect2">

### Enabling Auto-reconnect

<div class="paragraph">

You should be careful when setting the
`<gfe:cache enable-auto-reconnect="[true|false*]>` attribute to `true`.

</div>

<div class="paragraph">

Generally, 'auto-reconnect' should only be enabled in cases where
{sdg-name}'s XML namespace is used to configure and bootstrap a new,
non-application {data-store-name} server added to a cluster. In other
words, 'auto-reconnect' should not be enabled when {sdg-name} is used to
develop and build a {data-store-name} application that also happens to
be a peer `Cache` member of the {data-store-name} cluster.

</div>

<div class="paragraph">

The main reason for this restriction is that most {data-store-name}
applications use references to the {data-store-name} `Cache` or Regions
in order to perform data access operations. These references are
“injected” by the Spring container into application components (such as
Repositories) for use by the application. When a peer member is
forcefully disconnected from the rest of the cluster, presumably because
the peer member has become unresponsive or a network partition separates
one or more peer members into a group too small to function as an
independent distributed system, the peer member shuts down and all
{data-store-name} component references (caches, Regions, and others)
become invalid.

</div>

<div class="paragraph">

Essentially, the current forced disconnect processing logic in each peer
member dismantles the system from the ground up. The JGroups stack shuts
down, the distributed system is put in a shutdown state and, finally,
the cache is closed. Effectively, all memory references become stale and
are lost.

</div>

<div class="paragraph">

After being disconnected from the distributed system, a peer member
enters a “reconnecting” state and periodically attempts to rejoin the
distributed system. If the peer member succeeds in reconnecting, the
member rebuilds its “view” of the distributed system from existing
members and receives a new distributed system ID. Additionally, all
caches, Regions, and other {data-store-name} components are
reconstructed. Therefore, all old references, which may have been
injected into application by the Spring container, are now stale and no
longer valid.

</div>

<div class="paragraph">

{data-store-name} makes no guarantee (even when using the
{data-store-name} public Java API) that application cache, Regions, or
other component references are automatically refreshed by the reconnect
operation. As such, {data-store-name} applications must take care to
refresh their own references.

</div>

<div class="paragraph">

Unfortunately, there is no way to be notified of a disconnect event and,
subsequently, a reconnect event either. If that were the case, you would
have a clean way to know when to call
`ConfigurableApplicationContext.refresh()`, if it were even applicable
for an application to do so, which is why this “feature” of
{data-store-name} is not recommended for peer `Cache` applications.

</div>

<div class="paragraph">

For more information about 'auto-reconnect', see {data-store-name}'s
{x-data-store-docs}/managing/autoreconnect/member-reconnect.html\[product
documentation\].

</div>

</div>

<div class="sect2">

### Using Cluster-based Configuration

<div class="paragraph">

{data-store-name}'s Cluster Configuration Service is a convenient way
for any peer member joining the cluster to get a “consistent view” of
the cluster by using the shared, persistent configuration maintained by
a Locator. Using the cluster-based configuration ensures the peer
member’s configuration is compatible with the {data-store-name}
Distributed System when the member joins.

</div>

<div class="paragraph">

This feature of {sdg-name} (setting the `use-cluster-configuration`
attribute to `true`) works in the same way as the `cache-xml-location`
attribute, except the source of the {data-store-name} configuration
meta-data comes from the network through a Locator, as opposed to a
native `cache.xml` file residing in the local file system.

</div>

<div class="paragraph">

All {data-store-name} native configuration metadata, whether from
`cache.xml` or from the Cluster Configuration Service, gets applied
before any Spring (XML) configuration metadata. As a result, Spring’s
config serves to “augment” the native {data-store-name} configuration
metadata and would most likely be specific to the application.

</div>

<div class="paragraph">

Again, to enable this feature, specify the following in the Spring XML
config:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:cache use-cluster-configuration="true"/>
```

</div>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">While certain {data-store-name} tools, such as
<em>Gfsh</em>, have their actions “recorded” when schema-like changes
are made (for example,
<code>gfsh&gt;create region --name=Example --type=PARTITION</code>),
{sdg-name}'s configuration metadata is not recorded. The same is true
when using {data-store-name}'s public Java API directly. It, too, is not
recorded.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

For more information on {data-store-name}'s Cluster Configuration
Service, see the
{x-data-store-docs}/configuring/cluster_config/gfsh_persist.html\[product
documentation\].

</div>

</div>

</div>

</div>

<div class="sect1">

## Configuring a {data-store-name} CacheServer

<div class="sectionbody">

<div class="paragraph">

{sdg-name} includes dedicated support for configuring a
{x-data-store-javadoc}/org/apache/geode/cache/server/CacheServer.html\[CacheServer\],
allowing complete configuration through the Spring container, as the
following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:gfe="{spring-data-schema-namespace}"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd
    {spring-data-schema-namespace} {spring-data-schema-location}
">

  <gfe:cache/>

  <!-- Example depicting serveral {data-store-name} CacheServer configuration options -->
  <gfe:cache-server id="advanced-config" auto-startup="true"
       bind-address="localhost" host-name-for-clients="localhost" port="${gemfire.cache.server.port}"
       load-poll-interval="2000" max-connections="22" max-message-count="1000" max-threads="16"
       max-time-between-pings="30000" groups="test-server">

    <gfe:subscription-config eviction-type="ENTRY" capacity="1000" disk-store="file://${java.io.tmpdir}"/>

  </gfe:cache-server>

  <context:property-placeholder location="classpath:cache-server.properties"/>

</beans>
```

</div>

</div>

<div class="paragraph">

The preceding configuration shows the `cache-server` element and the
many available options.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Rather than hard-coding the port, this configuration
uses Spring’s <a
href="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#xsd-config-body-schemas-context">context</a>
namespace to declare a <code>property-placeholder</code>. A <a
href="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-factory-placeholderconfigurer">property
placeholder</a> reads one or more properties files and then replaces
property placeholders with values at runtime. Doing so lets
administrators change values without having to touch the main
application configuration. Spring also provides <a
href="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#expressions">SpEL</a>
and an <a
href="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-environment">environment
abstraction</a> to support externalization of environment-specific
properties from the main codebase, easing deployment across multiple
machines.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">To avoid initialization problems, the
<code>CacheServer</code> started by {sdg-name} starts
<strong>after</strong> the Spring container has been fully initialized.
Doing so lets potential Regions, listeners, writers or instantiators
that are defined declaratively to be fully initialized and registered
before the server starts accepting connections. Keep this in mind when
programmatically configuring these elements, as the server might start
before your components and thus not be seen by the clients connecting
right away.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect1">

## Configuring a {data-store-name} ClientCache

<div class="sectionbody">

<div class="paragraph">

In addition to defining a {data-store-name} peer
{x-data-store-javadoc}/org/apache/geode/cache/Cache.html\[`Cache`\],
{sdg-name} also supports the definition of a {data-store-name}
{x-data-store-javadoc}/org/apache/geode/cache/client/ClientCache.html\[`ClientCache`\]
in a Spring container. A `ClientCache` definition is similar in
configuration and use to the {data-store-name} peer
[Cache](#bootstrap:cache) and is supported by the
`org.springframework.data.gemfire.client.ClientCacheFactoryBean`.

</div>

<div class="paragraph">

The simplest definition of a {data-store-name} cache client using
default configuration follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<beans>
  <gfe:client-cache/>
</beans>
```

</div>

</div>

<div class="paragraph">

`client-cache` supports many of the same options as the
[Cache](#bootstrap:cache:advanced) element. However, as opposed to a
full-fledged peer `Cache` member, a cache client connects to a remote
cache server through a Pool. By default, a Pool is created to connect to
a server running on `localhost` and listening to port `40404`. The
default Pool is used by all client Regions unless the Region is
configured to use a specific Pool.

</div>

<div class="paragraph">

Pools can be defined with the `pool` element. This client-side Pool can
be used to configure connectivity directly to a server for individual
entities or for the entire cache through one or more Locators.

</div>

<div class="paragraph">

For example, to customize the default Pool used by the `client-cache`,
the developer needs to define a Pool and wire it to the cache
definition, as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<beans>
  <gfe:client-cache id="myCache" pool-name="myPool"/>

  <gfe:pool id="myPool" subscription-enabled="true">
    <gfe:locator host="${gemfire.locator.host}" port="${gemfire.locator.port}"/>
  </gfe:pool>
</beans>
```

</div>

</div>

<div class="paragraph">

The `<client-cache>` element also has a `ready-for-events` attribute. If
the attribute is set to `true`, the client cache initialization includes
a call to
{x-data-store-javadoc}/org/apache/geode/cache/client/ClientCache.html#readyForEvents\[`ClientCache.readyForEvents()`\].

</div>

<div class="paragraph">

[\[bootstrap:region:client\]](#bootstrap:region:client) covers
client-side configuration in more detail.

</div>

<div class="sect2">

### {data-store-name}'s DEFAULT Pool and {sdg-name} Pool Definitions

<div class="paragraph">

If a {data-store-name} `ClientCache` is local-only, then no Pool
definition is required. For instance, you can define the following:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:client-cache/>

<gfe:client-region id="Example" shortcut="LOCAL"/>
```

</div>

</div>

<div class="paragraph">

In this case, the “Example” Region is `LOCAL` and no data is distributed
between the client and a server. Therefore, no Pool is necessary. This
is true for any client-side, local-only Region, as defined by the
{data-store-name}'s
{x-data-store-javadoc}/org/apache/geode/cache/client/ClientRegionShortcut.html\[`ClientRegionShortcut`\]
(all `LOCAL_*` shortcuts).

</div>

<div class="paragraph">

However, if a client Region is a (caching) proxy to a server-side
Region, a Pool is required. In that case, there are several ways to
define and use a Pool.

</div>

<div class="paragraph">

When a `ClientCache`, a Pool, and a proxy-based Region are all defined
but not explicitly identified, {sdg-name} resolves the references
automatically, as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:client-cache/>

<gfe:pool>
  <gfe:locator host="${geode.locator.host}" port="${geode.locator.port}"/>
</gfe:pool>

<gfe:client-region id="Example" shortcut="PROXY"/>
```

</div>

</div>

<div class="paragraph">

In the preceding example, the `ClientCache` is identified as
`gemfireCache`, the Pool as `gemfirePool`, and the client Region as
“Example”. However, the `ClientCache` initializes {data-store-name}'s
`DEFAULT` Pool from `gemfirePool`, and the client Region uses the
`gemfirePool` when distributing data between the client and the server.

</div>

<div class="paragraph">

Basically, {sdg-name} resolves the preceding configuration to the
following:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:client-cache id="gemfireCache" pool-name="gemfirePool"/>

<gfe:pool id="gemfirePool">
  <gfe:locator host="${geode.locator.host}" port="${geode.locator.port}"/>
</gfe:pool>

<gfe:client-region id="Example" cache-ref="gemfireCache" pool-name="gemfirePool" shortcut="PROXY"/>
```

</div>

</div>

<div class="paragraph">

{data-store-name} still creates a Pool called `DEFAULT`. {sdg-name}
causes the `DEFAULT` Pool to be initialized from the `gemfirePool`.
Doing so is useful in situations where multiple Pools are defined and
client Regions are using separate Pools, or do not declare a Pool at
all.

</div>

<div class="paragraph">

Consider the following:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:client-cache pool-name="locatorPool"/>

<gfe:pool id="locatorPool">
  <gfe:locator host="${geode.locator.host}" port="${geode.locator.port}"/>
</gfe:pool>

<gfe:pool id="serverPool">
  <gfe:server host="${geode.server.host}" port="${geode.server.port}"/>
</gfe:pool>

<gfe:client-region id="Example" pool-name="serverPool" shortcut="PROXY"/>

<gfe:client-region id="AnotherExample" shortcut="CACHING_PROXY"/>

<gfe:client-region id="YetAnotherExample" shortcut="LOCAL"/>
```

</div>

</div>

<div class="paragraph">

In this setup, the {data-store-name} `client-cache` `DEFAULT` pool is
initialized from `locatorPool`, as specified by the `pool-name`
attribute. There is no {sdg-name}-defined `gemfirePool`, since both
Pools were explicitly identified (named) — `locatorPool` and
`serverPool`, respectively.

</div>

<div class="paragraph">

The “Example” Region explicitly refers to and exclusively uses the
`serverPool`. The `AnotherExample` Region uses {data-store-name}'s
`DEFAULT` Pool, which, again, was configured from the `locatorPool`
based on the client cache bean definition’s `pool-name` attribute.

</div>

<div class="paragraph">

Finally, the `YetAnotherExample` Region does not use a Pool, because it
is `LOCAL`.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The <code>AnotherExample</code> Region would first
look for a Pool bean named <code>gemfirePool</code>, but that would
require the definition of an anonymous Pool bean (that is,
<code>&lt;gfe:pool/&gt;</code>) or a Pool bean explicitly named
<code>gemfirePool</code> (for example,
<code>&lt;gfe:pool id="gemfirePool"/&gt;</code>).</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">If we either changed the name of
<code>locatorPool</code> to <code>gemfirePool</code> or made the Pool
bean definition be anonymous, it would have the same effect as the
preceding configuration.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700

</div>

</div>
