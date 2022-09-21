<div id="header">

# Bootstrapping a Spring ApplicationContext in {data-store-name}

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

Normally, a Spring-based application [bootstraps
{data-store-name}](#bootstrap) by using {sdg-name}'s features. By
specifying a `<gfe:cache/>` element that uses the {sdg-name} XML
namespace, a single embedded {data-store-name} peer `Cache` instance is
created and initialized with default settings in the same JVM process as
your application.

</div>

<div class="paragraph">

However, it is sometimes necessary (perhaps as a requirement imposed by
your IT organization) that {data-store-name} be fully managed and
operated by the provided {data-store-name} tool suite, perhaps using
{x-data-store-docs}/tools_modules/gfsh/chapter_overview.html\[Gfsh\]. By
using *Gfsh*, {data-store-name} bootstraps your Spring
`ApplicationContext` rather than the other way around. Instead of an
application server or a Java main class that uses Spring Boot,
{data-store-name} does the bootstrapping and hosts your application.

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
<td class="content">{data-store-name} is not an application server. In
addition, there are limitations to using this approach where the
{data-store-name} cache configuration is concerned.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect1">

## Using {data-store-name} to Bootstrap a Spring Context Started with Gfsh

<div class="sectionbody">

<div class="paragraph">

In order to bootstrap a Spring `ApplicationContext` in {data-store-name}
when starting a {data-store-name} server using *Gfsh*, you must use
{data-store-name}'s
{x-data-store-docs}/basic_config/the_cache/setting_cache_initializer.html\[initalizer\]
capability. An initializer block can declare a application callback that
is launched after the cache is initialized by {data-store-name}.

</div>

<div class="paragraph">

An initializer is declared within an
{x-data-store-docs}/reference/topics/cache_xml.html#initializer\[initializer\]
element by using a minimal snippet of {data-store-name}'s native
`cache.xml`. To bootstrap the Spring `ApplicationContext`, a `cache.xml`
file is required, in much the same way as a minimal snippet of Spring
XML config is needed to bootstrap a Spring `ApplicationContext`
configured with component scanning (for example
`<context:component-scan base-packages="…​"/>`).

</div>

<div class="paragraph">

Fortunately, such an initializer is already conveniently provided by the
framework: the
{sdg-javadoc}/org/springframework/data/gemfire/support/SpringContextBootstrappingInitializer.html\[`SpringContextBootstrappingInitializer`\].

</div>

<div class="paragraph">

The following example shows a typical, yet minimal, configuration for
this class inside {data-store-name}'s `cache.xml` file:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<?xml version="1.0" encoding="UTF-8"?>
<cache xmlns="http://geode.apache.org/schema/cache"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://geode.apache.org/schema/cache https://geode.apache.org/schema/cache/cache-1.0.xsd"
       version="1.0">

  <initializer>
    <class-name>org.springframework.data.gemfire.support.SpringContextBootstrappingInitializer</class-name>
    <parameter name="contextConfigLocations">
      <string>classpath:application-context.xml</string>
    </parameter>
  </initializer>

</cache>
```

</div>

</div>

<div class="paragraph">

The `SpringContextBootstrappingInitializer` class follows conventions
similar to Spring’s `ContextLoaderListener` class, which is used to
bootstrap a Spring `ApplicationContext` inside a web application, where
`ApplicationContext` configuration files are specified with the
`contextConfigLocations` Servlet context parameter.

</div>

<div class="paragraph">

In addition, the `SpringContextBootstrappingInitializer` class can also
be used with a `basePackages` parameter to specify a comma-separated
list of base packages that contain appropriately annotated application
components. The Spring container searches these components to find and
create Spring beans and other application components in the classpath,
as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<?xml version="1.0" encoding="UTF-8"?>
<cache xmlns="http://geode.apache.org/schema/cache"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://geode.apache.org/schema/cache https://geode.apache.org/schema/cache/cache-1.0.xsd"
       version="1.0">

  <initializer>
    <class-name>org.springframework.data.gemfire.support.SpringContextBootstrappingInitializer</class-name>
    <parameter name="basePackages">
      <string>org.mycompany.myapp.services,org.mycompany.myapp.dao,...</string>
    </parameter>
  </initializer>

</cache>
```

</div>

</div>

<div class="paragraph">

Then, with a properly configured and constructed `CLASSPATH` and
`cache.xml` file (shown earlier) specified as a command-line option when
starting a {data-store-name} server in *Gfsh*, the command-line would be
as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
gfsh>start server --name=ExampleServer --log-level=config ...
    --classpath="/path/to/application/classes.jar:/path/to/spring-data-geode-<major>.<minor>.<maint>.RELEASE.jar"
    --cache-xml-file="/path/to/geode/cache.xml"
```

</div>

</div>

<div class="paragraph">

The `application-context.xml` can be any valid Spring configuration
metadata, including all of the {sdg-acronym} XML namespace elements. The
only limitation with this approach is that a {data-store-name} cache
cannot be configured by using the {sdg-acronym} XML namespace. In other
words, none of the `<gfe:cache/>` element attributes (such as
`cache-xml-location`, `properties-ref`, `critical-heap-percentage`,
`pdx-serializer-ref`, `lock-lease`, and others) can be specified. If
used, these attributes are ignored.

</div>

<div class="paragraph">

The reason for this is that {data-store-name} itself has already created
and initialized the cache before the initializer gets invoked. As a
result, the cache already exists and, since it is a “singleton”, it
cannot be re-initialized or have any of its configuration augmented.

</div>

</div>

</div>

<div class="sect1">

## Lazy-wiring {data-store-name} Components

<div class="sectionbody">

<div class="paragraph">

{sdg-name} already provides support for auto-wiring {data-store-name}
components (such as `CacheListeners`, `CacheLoaders`, `CacheWriters` and
so on) that are declared and created by {data-store-name} in `cache.xml`
by using {sdg-acronym}'s `WiringDeclarableSupport` class, as described
in [\[apis:declarable:autowiring\]](#apis:declarable:autowiring).
However, this works only when Spring is the one doing the bootstrapping
(that is, when Spring bootstraps {data-store-name}).

</div>

<div class="paragraph">

When your Spring `ApplicationContext` is bootstrapped by
{data-store-name}, these {data-store-name} application components go
unnoticed, because the Spring `ApplicationContext` does not exist yet.
The Spring `ApplicationContext` does not get created until
{data-store-name} calls the initializer block, which only occurs after
all the other {data-store-name} components (cache, Regions, and others)
have already been created and initialized.

</div>

<div class="paragraph">

To solve this problem, a new `LazyWiringDeclarableSupport` class was
introduced. This new class is aware of the Spring `ApplicationContext`.
The intention behind this abstract base class is that any implementing
class registers itself to be configured by the Spring container that is
eventually created by {data-store-name} once the initializer is called.
In essence, this gives your {data-store-name} application components a
chance to be configured and auto-wired with Spring beans defined in the
Spring container.

</div>

<div class="paragraph">

In order for your {data-store-name} application components to be
auto-wired by the Spring container, you should create an application
class that extends the `LazyWiringDeclarableSupport` and annotate any
class member that needs to be provided as a Spring bean dependency,
similar to the following example:

</div>

<div class="listingblock">

<div class="content">

``` highlight
public class UserDataSourceCacheLoader extends LazyWiringDeclarableSupport
    implements CacheLoader<String, User> {

  @Autowired
  private DataSource userDataSource;

  ...
}
```

</div>

</div>

<div class="paragraph">

As implied in the `CacheLoader` example above, you might necessarily
(though rarely) have defined both a Region and a `CacheListener`
component in {data-store-name} `cache.xml`. The `CacheLoader` may need
access to an application Repository (or perhaps a JDBC `DataSource`
defined in the Spring `ApplicationContext`) for loading `Users` into a
{data-store-name} `REPLICATE` Region on startup.

</div>

<div class="paragraph">

CAUTION

</div>

<div class="exampleblock">

<div class="content">

<div class="paragraph">

Be careful when mixing the different life-cycles of {data-store-name}
and the Spring container together in this manner. Not all use cases and
scenarios are supported. The {data-store-name} `cache.xml` configuration
would be similar to the following (which comes from {sdg-acronym}'s test
suite):

</div>

<div class="listingblock">

<div class="content">

``` highlight
<?xml version="1.0" encoding="UTF-8"?>
<cache xmlns="http://geode.apache.org/schema/cache"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://geode.apache.org/schema/cache https://geode.apache.org/schema/cache/cache-1.0.xsd"
       version="1.0">

  <region name="Users" refid="REPLICATE">
    <region-attributes initial-capacity="101" load-factor="0.85">
      <key-constraint>java.lang.String</key-constraint>
      <value-constraint>org.springframework.data.gemfire.repository.sample.User</value-constraint>
      <cache-loader>
        <class-name>
          org.springframework.data.gemfire.support.SpringContextBootstrappingInitializerIntegrationTests$UserDataStoreCacheLoader
        </class-name>
      </cache-loader>
    </region-attributes>
  </region>

  <initializer>
    <class-name>org.springframework.data.gemfire.support.SpringContextBootstrappingInitializer</class-name>
    <parameter name="basePackages">
      <string>org.springframework.data.gemfire.support.sample</string>
    </parameter>
  </initializer>

</cache>
```

</div>

</div>

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
