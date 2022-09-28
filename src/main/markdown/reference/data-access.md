<div id="header">

# Using the Data Access Namespace








In addition to the core XML namespace (`gfe`), Spring Data for GemFire provides a
data access XML namespace (`gfe-data`), which is primarily intended to
simplify the development of GemFire client applications. This
namespace currently contains support for GemFire
[Repositories](#gemfire-repositories) and Function
[execution](#function-execution), as well as a `<datasource>` tag that
offers a convenient way to connect to a GemFire cluster.





## An Easy Way to Connect to GemFire




For many applications, a basic connection to a GemFire data
grid using default values is sufficient. Spring Data for GemFire's `<datasource>` tag
provides a simple way to access data. The data source creates a
`ClientCache` and connection `Pool`. In addition, it queries the cluster
servers for all existing root Regions and creates an (empty) client
Region proxy for each one.




``` highlight
<gfe-data:datasource>
  <locator host="remotehost" port="1234"/>
</gfe-data:datasource>
```




The `<datasource>` tag is syntactically similar to `<gfe:pool>`. It may
be configured with one or more nested `locator` or `server` elements to
connect to an existing data grid. Additionally, all attributes available
to configure a Pool are supported. This configuration automatically
creates client Region beans for each Region defined on cluster members
connected to the Locator, so they can be seamlessly referenced by Spring
Data mapping annotations (`GemfireTemplate`) and autowired into
application classes.



Of course, you can explicitly configure client Regions. For example, if
you want to cache data in local memory, as the following example shows:




``` highlight
<gfe-data:datasource>
  <locator host="remotehost" port="1234"/>
</gfe-data:datasource>

<gfe:client-region id="Example" shortcut="CACHING_PROXY"/>
```






<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700


