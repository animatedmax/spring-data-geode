<div id="header">

# Apache Lucene Integration

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

{x-data-store-website}\[{data-store-name}\] integrates with [Apache
Lucene](https://lucene.apache.org/) to let you index and search on data
stored in {data-store-name} by using Lucene queries. Search-based
queries also include the ability to page through query results.

</div>

<div class="paragraph">

Additionally, {sdg-name} adds support for query projections based on the
Spring Data Commons projection infrastructure. This feature lets the
query results be projected into first-class application domain types as
needed by the application.

</div>

<div class="paragraph">

A Lucene `Index` must be created before any Lucene search-based query
can be run. A `LuceneIndex` can be created in Spring (Data for
{data-store-name}) XML config as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:lucene-index id="IndexOne" fields="fieldOne, fieldTwo" region-path="/Example"/>
```

</div>

</div>

<div class="paragraph">

Additionally, Apache Lucene allows the specification of
[analyzers](https://lucene.apache.org/core/6_5_0/core/org/apache/lucene/analysis/Analyzer.html)
per field and can be configured as shown in the following example:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:lucene-index id="IndexTwo" lucene-service-ref="luceneService" region-path="/AnotherExample">
    <gfe:field-analyzers>
        <map>
            <entry key="fieldOne">
                <bean class="example.AnalyzerOne"/>
             </entry>
            <entry key="fieldTwo">
                <bean class="example.AnalyzerTwo"/>
             </entry>
        </map>
    </gfe:field-analyzers>
</gfe:lucene-index>
```

</div>

</div>

<div class="paragraph">

The `Map` can be specified as a top-level bean definition and referenced
by using the `ref` attribute in the nested `<gfe:field-analyzers>`
element, as follows:
`<gfe-field-analyzers ref="refToTopLevelMapBeanDefinition"/>`.

</div>

<div class="paragraph">

{sdg-name}'s `LuceneIndexFactoryBean` API and {sdg-acronym}'s XML
namespace also lets a
{x-data-store-javadoc}/org/apache/geode/cache/lucene/LuceneSerializer.html\[`org.apache.geode.cache.lucene.LuceneSerializer`\]
be specified when you create the `LuceneIndex`. The `LuceneSerializer`
lets you configure the way objects are converted to Lucene documents for
the index when the object is indexed.

</div>

<div class="paragraph">

The following example shows how to add an `LuceneSerializer` to the
`LuceneIndex`:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<bean id="MyLuceneSerializer" class="example.CustomLuceneSerializer"/>

<gfe:lucene-index id="IndexThree" lucene-service-ref="luceneService" region-path="/YetAnotherExample">
    <gfe:lucene-serializer ref="MyLuceneSerializer">
</gfe:lucene-index>
```

</div>

</div>

<div class="paragraph">

You can specify the `LuceneSerializer` as an anonymous, nested bean
definition as well, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:lucene-index id="IndexThree" lucene-service-ref="luceneService" region-path="/YetAnotherExample">
    <gfe:lucene-serializer>
        <bean class="example.CustomLuceneSerializer"/>
    </gfe:lucene-serializer>
</gfe:lucene-index>
```

</div>

</div>

<div class="paragraph">

Alternatively, you can declare or define a `LuceneIndex` in Spring Java
config, inside a `@Configuration` class, as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@Bean(name = "Books")
@DependsOn("bookTitleIndex")
PartitionedRegionFactoryBean<Long, Book> booksRegion(GemFireCache gemfireCache) {

    PartitionedRegionFactoryBean<Long, Book> peopleRegion =
        new PartitionedRegionFactoryBean<>();

    peopleRegion.setCache(gemfireCache);
    peopleRegion.setClose(false);
    peopleRegion.setPersistent(false);

    return peopleRegion;
}

@Bean
LuceneIndexFactoryBean bookTitleIndex(GemFireCache gemFireCache,
        LuceneSerializer luceneSerializer) {

    LuceneIndexFactoryBean luceneIndex = new LuceneIndexFactoryBean();

    luceneIndex.setCache(gemFireCache);
    luceneIndex.setFields("title");
    luceneIndex.setLuceneSerializer(luceneSerializer);
    luceneIndex.setRegionPath("/Books");

    return luceneIndex;
}

@Bean
CustomLuceneSerializer myLuceneSerialier() {
    return new CustomeLuceneSerializer();
}
```

</div>

</div>

<div class="paragraph">

There are a few limitations of {data-store-name}'s, Apache Lucene
integration and support.

</div>

<div class="paragraph">

First, a `LuceneIndex` can only be created on a {data-store-name}
`PARTITION` Region.

</div>

<div class="paragraph">

Second, all `LuceneIndexes` must be created before the Region to which
the `LuceneIndex` applies.

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
<td class="content">To help ensure that all declared
<code>LuceneIndexes</code> defined in a Spring container are created
before the Regions on which they apply, {sdg-acronym} includes the
<code>org.springframework.data.gemfire.config.support.LuceneIndexRegionBeanFactoryPostProcessor</code>.
You may register this Spring
{spring-framework-javadoc}/org/springframework/beans/factory/config/BeanFactoryPostProcessor.html[<code>BeanFactoryPostProcessor</code>]
in XML config by using
<code>&lt;bean class="org.springframework.data.gemfire.config.support.LuceneIndexRegionBeanFactoryPostProcessor"/&gt;</code>.
The
<code>o.s.d.g.config.support.LuceneIndexRegionBeanFactoryPostProcessor</code>
may only be used when using {sdg-acronym} XML config. More details about
Spring’s <code>BeanFactoryPostProcessors</code> can be found
{spring-framework-docs}/core.html#beans-factory-extension-factory-postprocessors[here].</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

It is possible that these {data-store-name} restrictions will not apply
in a future release which is why the {sdg-acronym}
`LuceneIndexFactoryBean` API takes a reference to the Region directly as
well, rather than just the Region path.

</div>

<div class="paragraph">

This is more ideal when you want to define a `LuceneIndex` on an
existing Region with data at a later point during the application’s
lifecycle and as requirements demand. Where possible, {sdg-acronym}
strives to adhere to strongly-typed objects. However, for the time
being, you must use the `regionPath` property to specify the Region to
which the `LuceneIndex` is applied.

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
<td class="content">Additionally, in the preceding example, note the
presence of Spring’s <code>@DependsOn</code> annotation on the
<code>Books</code> Region bean definition. This creates a dependency
from the <code>Books</code> Region bean to the
<code>bookTitleIndex</code> <code>LuceneIndex</code> bean definition,
ensuring that the <code>LuceneIndex</code> is created before the Region
on which it applies.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

Now that once we have a `LuceneIndex`, we can perform Lucene-based data
access operations, such as queries.

</div>

</div>

</div>

<div class="sect1">

## Lucene Template Data Accessors

<div class="sectionbody">

<div class="paragraph">

{sdg-name} provides two primary templates for Lucene data access
operations, depending on how low of a level your application is prepared
to deal with.

</div>

<div class="paragraph">

The `LuceneOperations` interface defines query operations by using
{data-store-name}
{x-data-store-javadoc}/org/apache/geode/cache/lucene/package-summary.html\[Lucene
types\], which are defined in the following interface definition:

</div>

<div class="listingblock">

<div class="content">

``` highlight
public interface LuceneOperations {

    <K, V> List<LuceneResultStruct<K, V>> query(String query, String defaultField [, int resultLimit]
        , String... projectionFields);

    <K, V> PageableLuceneQueryResults<K, V> query(String query, String defaultField,
        int resultLimit, int pageSize, String... projectionFields);

    <K, V> List<LuceneResultStruct<K, V>> query(LuceneQueryProvider queryProvider [, int resultLimit]
        , String... projectionFields);

    <K, V> PageableLuceneQueryResults<K, V> query(LuceneQueryProvider queryProvider,
        int resultLimit, int pageSize, String... projectionFields);

    <K> Collection<K> queryForKeys(String query, String defaultField [, int resultLimit]);

    <K> Collection<K> queryForKeys(LuceneQueryProvider queryProvider [, int resultLimit]);

    <V> Collection<V> queryForValues(String query, String defaultField [, int resultLimit]);

    <V> Collection<V> queryForValues(LuceneQueryProvider queryProvider [, int resultLimit]);
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
<td class="content">The <code>[, int resultLimit]</code> indicates that
the <code>resultLimit</code> parameter is optional.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

The operations in the `LuceneOperations` interface match the operations
provided by the {data-store-name}'s
{x-data-store-javadoc}/org/apache/geode/cache/lucene/LuceneQuery.html\[LuceneQuery\]
interface. However, {sdg-acronym} has the added value of translating
proprietary {data-store-name} or Apache Lucene `Exceptions` into
Spring’s highly consistent and expressive DAO [exception
hierarchy](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#dao-exceptions),
particularly as many modern data access operations involve more than one
store or repository.

</div>

<div class="paragraph">

Additionally, {sdg-acronym}'s `LuceneOperations` interface can shield
your application from interface-breaking changes introduced by the
underlying {data-store-name} or Apache Lucene APIs when they occur.

</div>

<div class="paragraph">

However, it would be sad to offer a Lucene Data Access Object (DAO) that
only uses {data-store-name} and Apache Lucene data types (such as
{data-store-name}'s `LuceneResultStruct`). Therefore, {sdg-acronym}
gives you the `ProjectingLuceneOperations` interface to remedy these
important application concerns. The following listing shows the
`ProjectingLuceneOperations` interface definition:

</div>

<div class="listingblock">

<div class="content">

``` highlight
public interface ProjectingLuceneOperations {

    <T> List<T> query(String query, String defaultField [, int resultLimit], Class<T> projectionType);

    <T> Page<T> query(String query, String defaultField, int resultLimit, int pageSize, Class<T> projectionType);

    <T> List<T> query(LuceneQueryProvider queryProvider [, int resultLimit], Class<T> projectionType);

    <T> Page<T> query(LuceneQueryProvider queryProvider, int resultLimit, int pageSize, Class<T> projectionType);
}
```

</div>

</div>

<div class="paragraph">

The `ProjectingLuceneOperations` interface primarily uses application
domain object types that let you work with your application data. The
`query` method variants accept a projection type, and the template
applies the query results to instances of the given projection type by
using the Spring Data Commons Projection infrastructure.

</div>

<div class="paragraph">

Additionally, the template wraps the paged Lucene query results in an
instance of the Spring Data Commons `Page` abstraction. The same
projection logic can still be applied to the results in the page and are
lazily projected as each page in the collection is accessed.

</div>

<div class="paragraph">

By way of example, suppose you have a class representing a `Person`, as
follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
class Person {

    Gender gender;

    LocalDate birthDate;

    String firstName;
    String lastName;

    ...

    String getName() {
        return String.format("%1$s %2$s", getFirstName(), getLastName());
    }
}
```

</div>

</div>

<div class="paragraph">

Additionally, you might have a single interface to represent people as
`Customers`, depending on your application view, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
interface Customer {

    String getName()

}
```

</div>

</div>

<div class="paragraph">

If I define the following `LuceneIndex`…​

</div>

<div class="listingblock">

<div class="content">

``` highlight
@Bean
LuceneIndexFactoryBean personLastNameIndex(GemFireCache gemfireCache) {

    LuceneIndexFactoryBean personLastNameIndex =
        new LuceneIndexFactoryBean();

    personLastNameIndex.setCache(gemfireCache);
    personLastNameIndex.setFields("lastName");
    personLastNameIndex.setRegionPath("/People");

    return personLastNameIndex;
}
```

</div>

</div>

<div class="paragraph">

Then you could query for people as `Person` objects, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
List<Person> people = luceneTemplate.query("lastName: D*", "lastName", Person.class);
```

</div>

</div>

<div class="paragraph">

Alternatively, you could query for a `Page` of type `Customer`, as
follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
Page<Customer> customers = luceneTemplate.query("lastName: D*", "lastName", 100, 20, Customer.class);
```

</div>

</div>

<div class="paragraph">

The `Page` can then be used to fetch individual pages of the results, as
follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
List<Customer> firstPage = customers.getContent();
```

</div>

</div>

<div class="paragraph">

Conveniently, the Spring Data Commons `Page` interface also implements
`java.lang.Iterable<T>`, making it easy to iterate over the contents.

</div>

<div class="paragraph">

The only restriction to the Spring Data Commons Projection
infrastructure is that the projection type must be an interface.
However, it is possible to extend the provided SDC Projection
infrastructure and provide a custom
[`ProjectionFactory`](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/projection/ProjectionFactory.html)
that uses [CGLIB](https://github.com/cglib/cglib) to generate proxy
classes as the projected entity.

</div>

<div class="paragraph">

You can use `setProjectionFactory(:ProjectionFactory)` to set a custom
`ProjectionFactory` on a Lucene template.

</div>

</div>

</div>

<div class="sect1">

## Annotation Configuration Support

<div class="sectionbody">

<div class="paragraph">

Finally, {sdg-name} provides annotation configuration support for
`LuceneIndexes`.

</div>

<div class="paragraph">

Eventually, the {sdg-acronym} Lucene support will finds its way into the
Repository infrastructure extension for {data-store-name} so that Lucene
queries can be expressed as methods on an application `Repository`
interface, in much the same way as the [OQL
support](#gemfire-repositories.queries.executing) works today.

</div>

<div class="paragraph">

However, in the meantime, if you want to conveniently express
`LuceneIndexes`, you can do so directly on your application domain
objects, as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@PartitionRegion("People")
class Person {

    Gender gender;

    @Index
    LocalDate birthDate;

    String firstName;

    @LuceneIndex;
    String lastName;

    ...
}
```

</div>

</div>

<div class="paragraph">

To enable this feature, you must use {sdg-acronym}'s annotation
configuration support specifically with the `@EnableEntityDefineRegions`
and `@EnableIndexing` annotations, as follows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
@PeerCacheApplication
@EnableEntityDefinedRegions
@EnableIndexing
class ApplicationConfiguration {

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
<td class="content"><code>LuceneIndexes</code> can only be created on
{data-store-name} servers since <code>LuceneIndexes</code> only apply to
<code>PARTITION</code> Regions.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

Given our earlier definition of the `Person` class, the {sdg-acronym}
annotation configuration support finds the `Person` entity class
definition and determines that people are stored in a `PARTITION` Region
called “People” and that the `Person` has an OQL `Index` on `birthDate`
along with a `LuceneIndex` on `lastName`.

</div>

</div>

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700

</div>

</div>
