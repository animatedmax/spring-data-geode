<div id="header">

# Annotation-based Configuration Quick Start

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

The following sections provide an overview to the SDG
annotations in order to get started quickly.

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
<td class="content">All annotations provide additional configuration
attributes along with associated <a
href="#bootstrap-annotation-config-properties">properties</a> to
conveniently customize the configuration and behavior of
GemFire at runtime. However, in general, none of the
attributes or associated properties are required to use a particular
GemFire feature. Simply declare the annotation to enable the
feature and you are done. Refer to the individual Javadoc of each
annotation for more details.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect1">

## Configure a `ClientCache` Application

<div class="sectionbody">

<div class="paragraph">

To configure and bootstrap a GemFire `ClientCache`
application, use the following:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
```

</div>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/ClientCacheApplication.html\[`@ClientCacheApplication`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-geode-applications\]](#bootstrap-annotation-config-geode-applications)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure a Peer `Cache` Application

<div class="sectionbody">

<div class="paragraph">

To configure and bootstrap a GemFire Peer `Cache` application,
use the following:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@PeerCacheApplication
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
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
<td class="content">If you would like to enable a
<code>CacheServer</code> that allows <code>ClientCache</code>
applications to connect to this server, then simply replace the
<code>@PeerCacheApplication</code> annotation with the
<code>@CacheServerApplication</code> annotation. This will start a
<code>CacheServer</code> running on "localhost", listening on the
default <code>CacheServer</code> port of <code>40404</code>.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/CacheServerApplication.html\[`@CacheServerApplication`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/PeerCacheApplication.html\[`@PeerCacheApplication`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-geode-applications\]](#bootstrap-annotation-config-geode-applications)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure an Embedded Locator

<div class="sectionbody">

<div class="paragraph">

Annotate your Spring `@PeerCacheApplication` or
`@CacheServerApplication` class with `@EnableLocator` to start an
embedded Locator bound to all NICs listening on the default Locator
port, `10334`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@CacheServerApplication
@EnableLocator
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
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
<td class="content"><code>@EnableLocator</code> can only be used with
GemFire server applications.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableLocator.html\[`@EnableLocator`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-embedded-services-locator\]](#bootstrap-annotation-config-embedded-services-locator)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure an Embedded Manager

<div class="sectionbody">

<div class="paragraph">

Annotate your Spring `@PeerCacheApplication` or
`@CacheServerApplication` class with `@EnableManager` to start an
embedded Manager bound to all NICs listening on the default Manager
port, `1099`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@CacheServerApplication
@EnableManager
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
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
<td class="content"><code>@EnableManager</code> can only be used with
GemFire server applications.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableManager.html\[`@EnableManager`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-embedded-services-manager\]](#bootstrap-annotation-config-embedded-services-manager)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure the Embedded HTTP Server

<div class="sectionbody">

<div class="paragraph">

Annotate your Spring `@PeerCacheApplication` or
`@CacheServerApplication` class with `@EnableHttpService` to start the
embedded HTTP server (Jetty) listening on port `7070`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@CacheServerApplication
@EnableHttpService
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
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
<td class="content"><code>@EnableHttpService</code> can only be used
with GemFire server applications.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableHttpService.html\[`@EnableHttpService`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-embedded-services-http\]](#bootstrap-annotation-config-embedded-services-http)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure the Embedded Memcached Server

<div class="sectionbody">

<div class="paragraph">

Annotate your Spring `@PeerCacheApplication` or
`@CacheServerApplication` class with `@EnableMemcachedServer` to start
the embedded Memcached server (Gemcached) listening on port `11211`, as
follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@CacheServerApplication
@EnableMemcachedServer
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
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
<td class="content"><code>@EnableMemcachedServer</code> can only be used
with GemFire server applications.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableMemcachedServer.html\[`@EnableMemcachedServer`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-embedded-services-memcached\]](#bootstrap-annotation-config-embedded-services-memcached)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Logging

<div class="sectionbody">

<div class="paragraph">

To configure or adjust GemFire logging, annotate your Spring,
GemFire client or server application class with
`@EnableLogging`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableLogging(logLevel="trace")
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
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
<td class="content">Default <code>log-level</code> is "config". Also,
this annotation will not adjust log levels in your application, only for
GemFire.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableLogging.html\[`@EnableLogging`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-logging\]](#bootstrap-annotation-config-logging)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Statistics

<div class="sectionbody">

<div class="paragraph">

To gather GemFire statistics at runtime, annotate your Spring,
GemFire client or server application class with
`@EnableStatistics`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableStatistics
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
```

</div>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableStatistics.html\[`@EnableStatistics`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-statistics\]](#bootstrap-annotation-config-statistics)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure PDX

<div class="sectionbody">

<div class="paragraph">

To enable GemFire PDX serialization, annotate your Spring,
GemFire client or server application class with `@EnablePdx`,
as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnablePdx
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
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
<td class="content">GemFire PDX Serialization is an
alternative to Java Serialization with many added benefits. For one, it
makes short work of making all of your application domain model types
serializable without having to implement
<code>java.io.Serializable</code>.</td>
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
<td class="content">By default, SDG configures the
<code>MappingPdxSerializer</code> to serialize your application domain
model types, which does not require any special configuration
out-of-the-box in order to properly identify application domain objects
that need to be serialized and then perform the serialization since, the
logic in <code>MappingPdxSerializer</code> is based on Spring Data's
mapping infrastructure. See <a
href="#mapping.pdx-serializer">[mapping.pdx-serializer]</a> for more
details.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnablePdx.html\[`@EnablePdx`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-pdx\]](#bootstrap-annotation-config-pdx)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure SSL

<div class="sectionbody">

<div class="paragraph">

To enable GemFire SSL, annotate your Spring, GemFire
client or server application class with `@EnableSsl`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableSsl(components = SERVER)
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
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
<td class="content">Minimally, GemFire requires you to specify
a keystore &amp; truststore using the appropriate configuration
attributes or properties. Both keystore &amp; truststore configuration
attributes or properties may refer to the same <code>KeyStore</code>
file. Additionally, you will need to specify a username and password to
access the <code>KeyStore</code> file if the file has been secured.</td>
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
<td class="content">GemFire SSL allows you to configure the
specific components of the system that require TLS, such as
client/server, Locators, Gateways, etc. Optionally, you can specify that
all components of GemFire use SSL with "ALL".</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableSsl.html\[`@EnableSsl`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-ssl\]](#bootstrap-annotation-config-ssl)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Security

<div class="sectionbody">

<div class="paragraph">

To enable GemFire security, annotate your Spring,
GemFire client or server application class with
`@EnableSecurity`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableSecurity
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
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
<td class="content">On the server, you must configure access to the auth
credentials. You may either implement the GemFire
{x-data-store-javadoc}/org/apache/geode/security/SecurityManager.html[<code>SecurityManager</code>]
interface or declare 1 or more Apache Shiro <code>Realms</code>. See <a
href="#bootstrap-annotation-config-security-server">[bootstrap-annotation-config-security-server]</a>
for more details.</td>
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
<td class="content">On the client, you must configure a username and
password. See <a
href="#bootstrap-annotation-config-security-client">[bootstrap-annotation-config-security-client]</a>
for more details.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableSecurity.html\[`@EnableSecurity`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-security\]](#bootstrap-annotation-config-security)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure GemFire Properties

<div class="sectionbody">

<div class="paragraph">

To configure other, low-level GemFire properties not covered
by the feature-oriented, SDG configuration annotations,
annotate your Spring, GemFire client or server application
class with `@GemFireProperties`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@PeerCacheApplication
@EnableGemFireProperties(
    cacheXmlFile = "/path/to/cache.xml",
    conserveSockets = true,
    groups = "GroupOne",
    remoteLocators = "lunchbox[11235],mailbox[10101],skullbox[12480]"
)
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
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
<td class="content">Some GemFire properties are client-side
only while others are server-side only. Please review the
GemFire
{x-data-store-docs}/reference/topics/gemfire_properties.html[docs] for
the appropriate use of each property.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableGemFireProperties.html\[`@EnableGemFireProperties`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-gemfire-properties\]](#bootstrap-annotation-config-gemfire-properties)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Caching

<div class="sectionbody">

<div class="paragraph">

To use GemFire as a *caching provider* in Spring's
{spring-framework-docs}/integration.html#cache\[*Cache Abstraction*\],
and have SDG automatically create GemFire Regions
for the caches required by your application service components, then
annotate your Spring, GemFire client or server application
class with `@EnableGemfireCaching` and `@EnableCachingDefinedRegions`,
as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableCachingDefinedRegions
@EnableGemfireCaching
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
```

</div>

</div>

<div class="paragraph">

Then, simply go on to define the application services that require
caching, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@Service
public class BookService {

    @Cacheable("Books")
    public Book findBy(ISBN isbn) {
        ...
    }
}
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
<td class="content"><code>@EnableCachingDefinedRegions</code> is
optional. That is, you may manually define your Regions if you
desire.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableCachingDefinedRegions.html\[`@EnableCachingDefinedRegions`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/cache/config/EnableGemfireCaching.html\[`@EnableGemfireCaching`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-caching\]](#bootstrap-annotation-config-caching)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Regions, Indexes, Repositories and Entities for Persistent Applications

<div class="sectionbody">

<div class="paragraph">

To make short work of creating Spring, GemFire persistent
client or server applications, annotate your application class with
`@EnableEntityDefinedRegions`, `@EnableGemfireRepositories` and
`@EnableIndexing`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableEntityDefinedRegions(basePackageClasses = Book.class)
@EnableGemfireRepositories(basePackageClasses = BookRepository.class)
@EnableIndexing
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
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
<td class="content">The <code>@EnableEntityDefinedRegions</code>
annotation is required when using the <code>@EnableIndexing</code>
annotation. See <a
href="#bootstrap-annotation-config-region-indexes">[bootstrap-annotation-config-region-indexes]</a>
for more details.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

Next, define your entity class and use the `@Region` mapping annotation
to specify the Region in which your entity will be stored. Use the
`@Indexed` annotation to define Indexes on entity fields used in your
application queries, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
package example.app.model;

import ...;

@Region("Books")
public class Book {

  @Id
  private ISBN isbn;

  @Indexed;
  private Author author;

  @Indexed
  private LocalDate published;

  @LuceneIndexed
  private String title;

}
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
<td class="content">The <code>@Region("Books")</code> entity class
annotation is used by the <code>@EnableEntityDefinedRegions</code> to
determine the Regions required by the application. See <a
href="#bootstrap-annotation-config-region-types">[bootstrap-annotation-config-region-types]</a>
and <a href="#mapping">[mapping]</a> for more details.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

Finally, define your CRUD Repository with simple queries to persist and
access `Books`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
package example.app.repo;

import ...;

public interface BookRepository extends CrudRepository {

  List<Book> findByAuthorOrderByPublishedDesc(Author author);

}
```

</div>

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">See <a
href="#gemfire-repositories">[gemfire-repositories]</a> for more
details.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableEntityDefinedRegions.html\[`@EnableEntityDefinedRegions`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/repository/config/EnableGemfireRepositories.html\[`@EnableGemfireRepositories`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableIndexing.html\[`@EnableIndexing`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/mapping/annotation/Region.html\[`@Region`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/mapping/annotation/Indexed.html\[`@Indexed`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/mapping/annotation/LuceneIndexed.html\[`@LuceneIndexed`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-regions\]](#bootstrap-annotation-config-regions)
for more details.

</div>

<div class="paragraph">

See [\[gemfire-repositories\]](#gemfire-repositories) for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Client Regions from Cluster-defined Regions

<div class="sectionbody">

<div class="paragraph">

Alternatively, you can define client \[\*PROXY\] Regions from Regions
already defined in the cluster using `@EnableClusterDefinedRegions`, as
follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@SpringBootApplication
@ClientCacheApplication
@EnableClusterDefinedRegions
@EnableGemfireRepositories
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  ...
}
```

</div>

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-region-cluster-defined\]](#bootstrap-annotation-config-region-cluster-defined)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Functions

<div class="sectionbody">

<div class="paragraph">

GemFire Functions are useful in distributed compute scenarios
where a potentially expensive computation requiring data can be
performed in parallel across the nodes in the cluster. In this case, it
is more efficient to bring the logic to where the data is located
(stored) rather than requesting and fetching the data to be processed by
the computation.

</div>

<div class="paragraph">

Use the `@EnableGemfireFunctions` along with the `@GemfireFunction`
annotation to enable GemFire Functions definitions implemented
as methods on POJOs, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@PeerCacheApplication
@EnableGemfireFunctions
class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

  @GemfireFunction
  Integer computeLoyaltyPoints(Customer customer) {
    ...
  }
}
```

</div>

</div>

<div class="paragraph">

Use the `@EnableGemfireFunctionExecutions` along with 1 of the Function
calling annotations: `@OnMember`, `@OnMembers`, `@OnRegion`, `@OnServer`
and `@OnServers`.

</div>

<div class="listingblock">

<div class="content">

``` highlight
@ClientCacheApplication
@EnableGemfireFunctionExecutions(basePackageClasses = CustomerRewardsFunction.class)
class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}

@OnRegion("Customers")
interface CustomerRewardsFunctions {

  Integer computeLoyaltyPoints(Customer customer);

}
```

</div>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/function/config/EnableGemfireFunctions.html\[`@EnableGemfireFunctions`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/function/annotation/GemfireFunction.html\[`@GemfireFunction`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/function/config/EnableGemfireFunctionExecutions.html\[`@EnableGemfireFunctionExecutions`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/function/annotation/OnMember.html\[`@OnMember`
Javadoc\],
{sdg-javadoc}/org/springframework/data/gemfire/function/annotation/OnMembers.html\[`@OnMembers`
Javadoc\],
{sdg-javadoc}/org/springframework/data/gemfire/function/annotation/OnRegion.html\[`@OnRegion`
Javadoc\],
{sdg-javadoc}/org/springframework/data/gemfire/function/annotation/OnServer.html\[`@OnServer`
Javadoc\], and
{sdg-javadoc}/org/springframework/data/gemfire/function/annotation/OnServers.html\[`@OnServers`
Javadoc\].

</div>

<div class="paragraph">

See [\[function-annotations\]](#function-annotations) for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Continuous Query

<div class="sectionbody">

<div class="paragraph">

Real-time, event stream processing is becoming an increasingly important
task for data-intensive applications, primarily in order to respond to
user requests in a timely manner. GemFire Continuous Query
(CQ) will help you achieve this rather complex task quite easily.

</div>

<div class="paragraph">

Enable CQ by annotating your application class with
`@EnableContinuousQueries` and define your CQs along with the associated
event handlers, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@ClientCacheApplication
@EnableContinuousQueries
class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
```

</div>

</div>

<div class="paragraph">

Then, define your CQs by annotating the associated handler method with
`@ContinousQuery`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@Service
class CustomerService {

  @ContinuousQuery(name = "CustomerQuery", query = "SELECT * FROM /Customers c WHERE ...")
  public void process(CqEvent event) {
    ...
  }
}
```

</div>

</div>

<div class="paragraph">

Anytime an event occurs changing the `Customer` data to match the
predicate in your continuous OQL query (CQ), the `process` method will
be called.

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
<td class="content">GemFire CQ is a client-side feature
only.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableContinuousQueries.html\[`@EnableContinuousQueries`
Javadoc\].

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/listener/annotation/ContinuousQuery.html\[`@ContinuousQuery`
Javadoc\].

</div>

<div class="paragraph">

See [\[apis:continuous-query\]](#apis:continuous-query) and
[\[bootstrap-annotation-config-continuous-queries\]](#bootstrap-annotation-config-continuous-queries)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure Cluster Configuration

<div class="sectionbody">

<div class="paragraph">

When developing Spring Data applications using GemFire as
GemFire `ClientCache` applications, it is useful during
development to configure the server to match the client in a
client/server topology. In fact, GemFire expects that when you
have a "/Example" PROXY `Region` on the client, that a matching `Region`
by name (i.e. "Example") exists in the server.

</div>

<div class="paragraph">

You could use *Gfsh* to create every Region and Index that your
application requires, or, you could simply push the configuration
meta-data already expressed when developing your Spring Data application
using GemFire when you run it.

</div>

<div class="paragraph">

This is as simple as annotation your main application class with
`@EnableClusterConfiguration(..)`:

</div>

<div class="listingblock">

<div class="title">

Using `@EnableClusterConfiguration`

</div>

<div class="content">

``` highlight
@ClientCacheApplication
@EnableClusterConfiguration(useHttp = true)
class ClientApplication {
  ...
}
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
<td class="content">Most of the time, when using a client/server
topology, particularly in production environments, the servers of the
cluster will be started using <em>Gfsh</em>. In which case, it customary
to use HTTP(S) to send the configuration metadata (e.g. Region &amp;
Index definitions) to the cluster. When HTTP is used, the configuration
metadata is sent to the Manager in the cluster and distributed across
the server nodes in the cluster consistently.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock warning">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Warning
</div></td>
<td class="content">In order to use
<code>@EnableClusterConfiguration</code> you must declare the
<code>org.springframework:spring-web</code> dependency in your Spring
application classpath.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/config/annotation/EnableClusterConfiguration.html\[`@EnableClusterConfiguration`
Javadoc\].

</div>

<div class="paragraph">

See
[\[bootstrap-annotation-config-cluster\]](#bootstrap-annotation-config-cluster)
for more details.

</div>

</div>

</div>

<div class="sect1">

## Configure `GatewayReceivers`

<div class="sectionbody">

<div class="paragraph">

The replication of data between different GemFire clusters is
an increasingly important fault-tolerance and high-availability (HA)
mechanism. GemFire WAN replication is a mechanism that allows
one GemFire cluster to replicate its data to another
GemFire cluster in a reliable and fault-tolerant manner.

</div>

<div class="paragraph">

GemFire WAN replication requires two components to be
configured:

</div>

<div class="ulist">

- `GatewayReceiver` - The WAN replication component that receives data
  from a remote GemFire cluster's `GatewaySender`.

- `GatewaySender` - The WAN replication component that sends data to a
  remote GemFire cluster's `GatewayReceiver`.

</div>

<div class="paragraph">

To enable a `GatewayReceiver`, the application class needs to be
annotated with `@EnableGatewayReceiver` as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@CacheServerApplication
@EnableGatewayReceiver(manualStart = false, startPort = 10000, endPort = 11000, maximumTimeBetweenPings = 1000,
    socketBufferSize = 16384, bindAddress = "localhost",transportFilters = {"transportBean1", "transportBean2"},
    hostnameForSenders = "hostnameLocalhost"){
      ...
      ...
    }
}
class MySpringApplication { .. }
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
<td class="content">GemFire <code>GatewayReceiver</code> is a
server-side feature only and can only be configured on a
<code>CacheServer</code> or peer <code>Cache</code> node.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/wan/annotation/EnableGatewayReceiver.html\[`@EnableGatewayReceiver`
Javadoc\].

</div>

</div>

</div>

<div class="sect1">

## Configure `GatewaySenders`

<div class="sectionbody">

<div class="paragraph">

To enable `GatewaySender`, the application class needs to be annotated
with `@EnableGatewaySenders` and `@EnableGatewaySender` as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@CacheServerApplication
@EnableGatewaySenders(gatewaySenders = {
        @EnableGatewaySender(name = "GatewaySender", manualStart = true,
            remoteDistributedSystemId = 2, diskSynchronous = true, batchConflationEnabled = true,
            parallel = true, persistent = false,diskStoreReference = "someDiskStore",
            orderPolicy = OrderPolicyType.PARTITION, alertThreshold = 1234, batchSize = 100,
            eventFilters = "SomeEventFilter", batchTimeInterval = 2000, dispatcherThreads = 22,
            maximumQueueMemory = 400,socketBufferSize = 16384,
            socketReadTimeout = 4000, regions = { "Region1"}),
        @EnableGatewaySender(name = "GatewaySender2", manualStart = true,
            remoteDistributedSystemId = 2, diskSynchronous = true, batchConflationEnabled = true,
            parallel = true, persistent = false, diskStoreReference = "someDiskStore",
            orderPolicy = OrderPolicyType.PARTITION, alertThreshold = 1234, batchSize = 100,
            eventFilters = "SomeEventFilter", batchTimeInterval = 2000, dispatcherThreads = 22,
            maximumQueueMemory = 400, socketBufferSize = 16384,socketReadTimeout = 4000,
            regions = { "Region2" })
}){
class MySpringApplication { .. }
}
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
<td class="content">GemFire <code>GatewaySender</code> is a
server-side feature only and can only be configured on a
<code>CacheServer</code> or a peer <code>Cache</code> node.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

In the above example, the application is configured with 2 Regions,
`Region1` and `Region2`. In addition, two `GatewaySenders` will be
configured to service both Regions. `GatewaySender1` will be configured
to replicate `` Region1's data and `GatewaySender2 `` will be configured
to replicate \`Region2's data.

</div>

<div class="paragraph">

As demonstrated each `GatewaySender` property can be configured on each
`EnableGatewaySender` annotation.

</div>

<div class="paragraph">

It is also possible to have a more generic, "defaulted" properties
approach, where all properties are configured on the
`EnableGatewaySenders` annotation. This way, a set of generic, defaulted
values can be set on the parent annotation and then overridden on the
child if required, as demonstrated below:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@CacheServerApplication
@EnableGatewaySenders(gatewaySenders = {
        @EnableGatewaySender(name = "GatewaySender", transportFilters = "transportBean1", regions = "Region2"),
        @EnableGatewaySender(name = "GatewaySender2")},
        manualStart = true, remoteDistributedSystemId = 2,
        diskSynchronous = false, batchConflationEnabled = true, parallel = true, persistent = true,
        diskStoreReference = "someDiskStore", orderPolicy = OrderPolicyType.PARTITION, alertThreshold = 1234, batchSize = 1002,
        eventFilters = "SomeEventFilter", batchTimeInterval = 2000, dispatcherThreads = 22, maximumQueueMemory = 400,
        socketBufferSize = 16384, socketReadTimeout = 4000, regions = { "Region1", "Region2" },
        transportFilters = { "transportBean2", "transportBean1" })
class MySpringApplication { .. }
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
<td class="content">When the <code>regions</code> attribute is left
empty or not populated, the <code>GatewaySender</code>(s) will
automatically attach itself to every configured <code>Region</code>
within the application.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

See
{sdg-javadoc}/org/springframework/data/gemfire/wan/annotation/EnableGatewaySenders.html\[`@EnableGatewaySenders`
Javadoc\] and
{sdg-javadoc}/org/springframework/data/gemfire/wan/annotation/EnableGatewaySender.html\[`@EnableGatewaySender`
Javadoc\].

</div>

</div>

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700

</div>

</div>
