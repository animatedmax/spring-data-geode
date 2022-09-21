<div id="header">

# Using the Data Access Namespace

</div>

<div id="content">

<div id="preamble">

<div class="sectionbody">

<div class="paragraph">

In addition to the core XML namespace (`gfe`), {sdg-name} provides a
data access XML namespace (`gfe-data`), which is primarily intended to
simplify the development of {data-store-name} client applications. This
namespace currently contains support for {data-store-name}
[Repositories](#gemfire-repositories) and Function
[execution](#function-execution), as well as a `<datasource>` tag that
offers a convenient way to connect to a {data-store-name} cluster.

</div>

</div>

</div>

<div class="sect1">

## An Easy Way to Connect to {data-store-name}

<div class="sectionbody">

<div class="paragraph">

For many applications, a basic connection to a {data-store-name} data
grid using default values is sufficient. {sdg-name}'s `<datasource>` tag
provides a simple way to access data. The data source creates a
`ClientCache` and connection `Pool`. In addition, it queries the cluster
servers for all existing root Regions and creates an (empty) client
Region proxy for each one.

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe-data:datasource>
  <locator host="remotehost" port="1234"/>
</gfe-data:datasource>
```

</div>

</div>

<div class="paragraph">

The `<datasource>` tag is syntactically similar to `<gfe:pool>`. It may
be configured with one or more nested `locator` or `server` elements to
connect to an existing data grid. Additionally, all attributes available
to configure a Pool are supported. This configuration automatically
creates client Region beans for each Region defined on cluster members
connected to the Locator, so they can be seamlessly referenced by Spring
Data mapping annotations (`GemfireTemplate`) and autowired into
application classes.

</div>

<div class="paragraph">

Of course, you can explicitly configure client Regions. For example, if
you want to cache data in local memory, as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe-data:datasource>
  <locator host="remotehost" port="1234"/>
</gfe-data:datasource>

<gfe:client-region id="Example" shortcut="CACHING_PROXY"/>
```

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
