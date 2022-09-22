<div id="header">

# Configuring the Snapshot Service

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

Spring Data for GemFire supports cache and Region snapshots by using
{x-data-store-docs}/managing/cache_snapshots/chapter_overview.html\[GemFire's
Snapshot Service\]. The out-of-the-box Snapshot Service support offers
several convenient features to simplify the use of GemFire's
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/CacheSnapshotService.html\[Cache\]
and
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/RegionSnapshotService.html\[Region\]
Snapshot Service APIs.

</div>

<div class="paragraph">

As the
{x-data-store-docs}/managing/cache_snapshots/chapter_overview.html\[GemFire
documentation\] explains, snapshots let you save and subsequently reload
the cached data later, which can be useful for moving data between
environments, such as from production to a staging or test environment
in order to reproduce data-related issues in a controlled context. You
can combine Spring Data for GemFire's Snapshot Service support with [Spring’s bean
definition
profiles](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-definition-profiles)
to load snapshot data specific to the environment as necessary.

</div>

<div class="paragraph">

Spring Data for GemFire's support for GemFire's Snapshot Service begins
with the `<gfe-data:snapshot-service>` element from the `<gfe-data>` XML
namespace.

</div>

<div class="paragraph">

For example, you can define cache-wide snapshots to be loaded as well as
saved by using a couple of snapshot imports and a data export
definition, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe-data:snapshot-service id="gemfireCacheSnapshotService">
  <gfe-data:snapshot-import location="/absolute/filesystem/path/to/import/fileOne.snapshot"/>
  <gfe-data:snapshot-import location="relative/filesystem/path/to/import/fileTwo.snapshot"/>
  <gfe-data:snapshot-export
      location="/absolute/or/relative/filesystem/path/to/export/directory"/>
</gfe-data:snapshot-service>
```

</div>

</div>

<div class="paragraph">

You can define as many imports and exports as you like. You can define
only imports or only exports. The file locations and directory paths can
be absolute or relative to the Spring Data for GemFire application, which is the JVM
process’s working directory.

</div>

<div class="paragraph">

The preceding example is pretty simple, and the Snapshot Service defined
in this case refers to the GemFire cache instance with the
default name of `gemfireCache` (as described in
[\[bootstrap:cache\]](#bootstrap:cache)). If you name your cache bean
definition something other than the default, you can use the `cache-ref`
attribute to refer to the cache bean by name, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:cache id="myCache"/>
...
<gfe-data:snapshot-service id="mySnapshotService" cache-ref="myCache">
  ...
</gfe-data:snapshot-service>
```

</div>

</div>

<div class="paragraph">

You can also define a Snapshot Service for a particular Region by
specifying the `region-ref` attribute, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:partitioned-region id="Example" persistent="false" .../>
...
<gfe-data:snapshot-service id="gemfireCacheRegionSnapshotService" region-ref="Example">
  <gfe-data:snapshot-import location="relative/path/to/import/example.snapshot/>
  <gfe-data:snapshot-export location="/absolute/path/to/export/example.snapshot/>
</gfe-data:snapshot-service>
```

</div>

</div>

<div class="paragraph">

When the `region-ref` attribute is specified, Spring Data for GemFire's
`SnapshotServiceFactoryBean` resolves the `region-ref` attribute value
to a Region bean defined in the Spring container and creates a
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/RegionSnapshotService.html\[`RegionSnapshotService`\].
The snapshot import and export definitions function the same way.
However, the `location` must refer to a file on an export.

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
<td class="content">GemFire is strict about imported snapshot
files actually existing before they are referenced. For exports,
GemFire creates the snapshot file. If the snapshot file for
export already exists, the data is overwritten.</td>
</tr>
</tbody>
</table>

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
<td class="content">Spring Data for GemFire includes a
<code>suppress-import-on-init</code> attribute on the
<code>&lt;gfe-data:snapshot-service&gt;</code> element to suppress the
configured Snapshot Service from trying to import data into the cache or
Region on initialization. Doing so is useful, for example, when data
exported from one Region is used to feed the import of another
Region.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect1">

## Snapshot Location

<div class="sectionbody">

<div class="paragraph">

With the cache-based Snapshot Service (that is, a
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/CacheSnapshotService.html\[`CacheSnapshotService`\])
you would typically pass it a directory containing all the snapshot
files to load rather than individual snapshot files, as the overloaded
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/CacheSnapshotService.html#load-java.io.File-org.apache.geode.cache.snapshot.SnapshotOptions.SnapshotFormat\[`load`\]
method in the `CacheSnapshotService` API indicates.

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
<td class="content">Of course, you can use the overloaded
<code>load(:File[], :SnapshotFormat, :SnapshotOptions)</code> method to
get specific about which snapshot files to load into the
GemFire cache.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

However, Spring Data for GemFire recognizes that a typical developer workflow might
be to extract and export data from one environment into several snapshot
files, zip all of them up, and then conveniently move the zip file to
another environment for import.

</div>

<div class="paragraph">

Therefore, Spring Data for GemFire lets you specify a jar or zip file on import for a
`cache`-based Snapshot Service, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
  <gfe-data:snapshot-service id="cacheBasedSnapshotService" cache-ref="gemfireCache">
    <gfe-data:snapshot-import location="/path/to/snapshots.zip"/>
  </gfe-data:snapshot-service>
```

</div>

</div>

<div class="paragraph">

Spring Data for GemFire conveniently extracts the provided zip file and treats it as
a directory import (load).

</div>

</div>

</div>

<div class="sect1">

## Snapshot Filters

<div class="sectionbody">

<div class="paragraph">

The real power of defining multiple snapshot imports and exports is
realized through the use of snapshot filters. Snapshot filters implement
GemFire's
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/SnapshotFilter.html\[`SnapshotFilter`\]
interface and are used to filter Region entries for inclusion into the
Region on import and for inclusion into the snapshot on export.

</div>

<div class="paragraph">

Spring Data for GemFire lets you use snapshot filters on import and export by using
the `filter-ref` attribute or an anonymous, nested bean definition, as
the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:cache/>

<gfe:partitioned-region id="Admins" persistent="false"/>
<gfe:partitioned-region id="Guests" persistent="false"/>

<bean id="activeUsersFilter" class="example.gemfire.snapshot.filter.ActiveUsersFilter/>

<gfe-data:snapshot-service id="adminsSnapshotService" region-ref="Admins">
  <gfe-data:snapshot-import location="/path/to/import/users.snapshot">
    <bean class="example.gemfire.snapshot.filter.AdminsFilter/>
  </gfe-data:snapshot-import>
  <gfe-data:snapshot-export location="/path/to/export/active/admins.snapshot" filter-ref="activeUsersFilter"/>
</gfe-data:snapshot-service>

<gfe-data:snapshot-service id="guestsSnapshotService" region-ref="Guests">
  <gfe-data:snapshot-import location="/path/to/import/users.snapshot">
    <bean class="example.gemfire.snapshot.filter.GuestsFilter/>
  </gfe-data:snapshot-import>
  <gfe-data:snapshot-export location="/path/to/export/active/guests.snapshot" filter-ref="activeUsersFilter"/>
</gfe-data:snapshot-service>
```

</div>

</div>

<div class="paragraph">

In addition, you can express more complex snapshot filters by using the
`ComposableSnapshotFilter` class. This class implements
GemFire's
{x-data-store-javadoc}/org/apache/geode/cache/snapshot/SnapshotFilter.html\[SnapshotFilter\]
interface as well as the
[Composite](https://en.wikipedia.org/wiki/Composite_pattern) software
design pattern.

</div>

<div class="paragraph">

In a nutshell, the
[Composite](https://en.wikipedia.org/wiki/Composite_pattern) software
design pattern lets you compose multiple objects of the same type and
treat the aggregate as single instance of the object type — a powerful
and useful abstraction.

</div>

<div class="paragraph">

`ComposableSnapshotFilter` has two factory methods, `and` and `or`. They
let you logically combine individual snapshot filters using the AND and
OR logical operators, respectively. The factory methods take a list of
`SnapshotFilters`.

</div>

<div class="paragraph">

The following example shows a definition for a
`ComposableSnapshotFilter`:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<bean id="activeUsersSinceFilter" class="org.springframework.data.gemfire.snapshot.filter.ComposableSnapshotFilter"
      factory-method="and">
  <constructor-arg index="0">
    <list>
      <bean class="org.example.app.gemfire.snapshot.filter.ActiveUsersFilter"/>
      <bean class="org.example.app.gemfire.snapshot.filter.UsersSinceFilter"
            p:since="2015-01-01"/>
    </list>
  </constructor-arg>
</bean>
```

</div>

</div>

<div class="paragraph">

You could then go on to combine the `activesUsersSinceFilter` with
another filter by using `or`, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<bean id="covertOrActiveUsersSinceFilter" class="org.springframework.data.gemfire.snapshot.filter.ComposableSnapshotFilter"
      factory-method="or">
  <constructor-arg index="0">
    <list>
      <ref bean="activeUsersSinceFilter"/>
      <bean class="example.gemfire.snapshot.filter.CovertUsersFilter"/>
    </list>
  </constructor-arg>
</bean>
```

</div>

</div>

</div>

</div>

<div class="sect1">

## Snapshot Events

<div class="sectionbody">

<div class="paragraph">

By default, Spring Data for GemFire uses GemFire's Snapshot Services on
startup to import data and on shutdown to export data. However, you may
want to trigger periodic, event-based snapshots, for either import or
export, from within your Spring application.

</div>

<div class="paragraph">

For this purpose, Spring Data for GemFire defines two additional Spring application
events, extending Spring’s
[`ApplicationEvent`](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/ApplicationEvent.html)
class for imports and exports, respectively:
`ImportSnapshotApplicationEvent` and `ExportSnapshotApplicationEvent`.

</div>

<div class="paragraph">

The two application events can be targeted for the entire
GemFire cache or for individual GemFire Regions. The
constructors in these classes accept an optional Region pathname (such
as `/Example`) as well as zero or more `SnapshotMetadata` instances.

</div>

<div class="paragraph">

The array of `SnapshotMetadata` overrides the snapshot metadata defined
by `<gfe-data:snapshot-import>` and `<gfe-data:snapshot-export>`
sub-elements, which are used in cases where snapshot application events
do not explicitly provide `SnapshotMetadata`. Each individual
`SnapshotMetadata` instance can define its own `location` and `filters`
properties.

</div>

<div class="paragraph">

All snapshot service beans defined in the Spring `ApplicationContext`
receive import and export snapshot application events. However, only
matching Snapshot Service beans process import and export events.

</div>

<div class="paragraph">

A Region-based `[Import|Export]SnapshotApplicationEvent` matches if the
Snapshot Service bean defined is a `RegionSnapshotService` and its
Region reference (as determined by the `region-ref` attribute) matches
the Region’s pathname, as specified by the snapshot application event.

</div>

<div class="paragraph">

A Cache-based `[Import|Export]SnapshotApplicationEvent` (that is, a
snapshot application event without a Region pathname) triggers all
Snapshot Service beans, including any `RegionSnapshotService` beans, to
perform either an import or export, respectively.

</div>

<div class="paragraph">

You can use Spring’s
{spring-framework-javadoc}/org/springframework/context/ApplicationEventPublisher.html\[`ApplicationEventPublisher`\]
interface to fire import and export snapshot application events from
your application as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@Component
public class ExampleApplicationComponent {

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Resource(name = "Example")
  private Region<?, ?> example;

  public void someMethod() {

    ...

    File dataSnapshot = new File(System.getProperty("user.dir"), "/path/to/export/data.snapshot");

    SnapshotFilter myFilter = ...;

    SnapshotMetadata exportSnapshotMetadata =
        new SnapshotMetadata(dataSnapshot, myFilter, null);

    ExportSnapshotApplicationEvent exportSnapshotEvent =
        new ExportSnapshotApplicationEvent(this, example.getFullPath(), exportSnapshotMetadata)

    eventPublisher.publishEvent(exportSnapshotEvent);

    ...
  }
}
```

</div>

</div>

<div class="paragraph">

In the preceding example, only the `/Example` Region’s Snapshot Service
bean picks up and handles the export event, saving the filtered,
“/Example” Region’s data to the `data.snapshot` file in a sub-directory
of the application’s working directory.

</div>

<div class="paragraph">

Using the Spring application events and messaging subsystem is a good
way to keep your application loosely coupled. You can also use Spring’s
{spring-framework-docs}/#scheduling-task-scheduler\[Scheduling\]
services to fire snapshot application events on a periodic basis.

</div>

</div>

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700

</div>

</div>
