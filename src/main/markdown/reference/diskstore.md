<div id="header">

# Configuring a DiskStore

</div>

<div id="content">

<div class="paragraph">

Spring Data for GemFire supports `DiskStore` configuration and creation through the
`disk-store` element, as the following example shows:

</div>

<div class="listingblock">

<div class="content">

``` highlight
<gfe:disk-store id="Example" auto-compact="true" max-oplog-size="10"
                queue-size="50" time-interval="9999">
    <gfe:disk-dir location="/disk/location/one" max-size="20"/>
    <gfe:disk-dir location="/disk/location/two" max-size="20"/>
</gfe:disk-store>
```

</div>

</div>

<div class="paragraph">

`DiskStore` instances are used by Regions for file system persistent
backup and overflow of evicted entries as well as persistent backup for
WAN Gateways. Multiple GemFire components may share the same
`DiskStore`. Additionally, multiple file system directories may be
defined for a single `DiskStore`, as shown in the preceding example.

</div>

<div class="paragraph">

See GemFire's documentation for a complete explanation of
{x-data-store-docs}/developing/storing_data_on_disk/chapter_overview.html\[Persistence
and Overflow\] and configuration options on `DiskStore` instances.

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700

</div>

</div>
