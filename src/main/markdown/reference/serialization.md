<div id="header">

# Working with {data-store-name} Serialization

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

To improve overall performance of the {data-store-name} In-memory Data
Grid, {data-store-name} supports a dedicated serialization protocol,
called PDX, that is both faster and offers more compact results over
standard Java serialization in addition to working transparently across
various language platforms (Java, C++, and .NET).

</div>

<div class="paragraph">

See
{x-data-store-docs}/developing/data_serialization/PDX_Serialization_Features.html\[PDX
Serialization Features\] and
{x-data-store-wiki}/PDX+Serialization+Internals\[PDX Serialization
Internals\] for more details.

</div>

<div class="paragraph">

This chapter discusses the various ways in which {sdg-name} simplifies
and improves {data-store-name}'s custom serialization in Java.

</div>

</div>

</div>

<div class="sect1">

## Wiring deserialized instances

<div class="sectionbody">

<div class="paragraph">

It is fairly common for serialized objects to have transient data.
Transient data is often dependent on the system or environment where it
lives at a certain point in time. For instance, a `DataSource` is
environment specific. Serializing such information is useless and
potentially even dangerous, since it is local to a certain VM or
machine. For such cases, {sdg-name} offers a special
{x-data-store-javadoc}/org/apache/geode/Instantiator.html\[`Instantiator`\]
that performs wiring for each new instance created by {data-store-name}
during deserialization.

</div>

<div class="paragraph">

Through such a mechanism, you can rely on the Spring container to inject
and manage certain dependencies, making it easy to split transient from
persistent data and have rich domain objects in a transparent manner.

</div>

<div class="paragraph">

Spring users might find this approach similar to that of
{spring-framework-docs}/#aop-atconfigurable\[`@Configurable`\]). The
`WiringInstantiator` works similarly to `WiringDeclarableSupport`,
trying to first locate a bean definition as a wiring template and
otherwise falling back to auto-wiring.

</div>

<div class="paragraph">

See the previous section ([\[apis:declarable\]](#apis:declarable)) for
more details on wiring functionality.

</div>

<div class="paragraph">

To use the {sdg-acronym} `Instantiator`, declare it as a bean, as the
following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<bean id="instantiator" class="org.springframework.data.gemfire.serialization.WiringInstantiator">
  <!-- DataSerializable type -->
  <constructor-arg>org.pkg.SomeDataSerializableClass</constructor-arg>
  <!-- type id -->
  <constructor-arg>95</constructor-arg>
</bean>
```

</div>

</div>

<div class="paragraph">

During the Spring container startup, once it has been initialized, the
`Instantiator`, by default, registers itself with the {data-store-name}
serialization system and performs wiring on all instances of
`SomeDataSerializableClass` created by {data-store-name} during
deserialization.

</div>

</div>

</div>

<div class="sect1">

## Auto-generating Custom `Instantiators`

<div class="sectionbody">

<div class="paragraph">

For data intensive applications, a large number of instances might be
created on each machine as data flows in. {data-store-name} uses
reflection to create new types, but, for some scenarios, this might
prove to be expensive. As always, it is good to perform profiling to
quantify whether this is the case or not. For such cases, {sdg-name}
allows the automatic generation of `Instatiator` classes, which
instantiate a new type (using the default constructor) without the use
of reflection. The following example shows how to create an
instantiator:

</div>

<div class="listingblock">

<div class="content">

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

</div>

</div>

<div class="paragraph">

The preceding definition automatically generates two `Instantiators` for
two classes (`CustomTypeA` and `CustomTypeB`) and registers them with
{data-store-name} under user ID `1025` and `1026`. The two
`Instantiators` avoid the use of reflection and create the instances
directly through Java code.

</div>

</div>

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700

</div>

</div>
