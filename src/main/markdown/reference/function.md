<div id="header">

# Configuring the Function Service




Spring Data for GemFire provides [annotation](#function-annotations) support for
implementing, registering and executing GemFire Functions.



Spring Data for GemFire also provides XML namespace support for registering
GemFire
{x-data-store-javadoc}/org/apache/geode/cache/execute/Function.html\[Functions\]
for remote function execution.



See GemFire's
{x-data-store-docs}/developing/function_exec/chapter_overview.html\[documentation\]
for more information on the Function execution framework.



GemFire Functions are declared as Spring beans and must
implement the `org.apache.geode.cache.execute.Function` interface or
extend `org.apache.geode.cache.execute.FunctionAdapter`.



The namespace uses a familiar pattern to declare Functions, as the
following example shows:




``` highlight
<gfe:function-service>
  <gfe:function>
      <bean class="example.FunctionOne"/>
      <ref bean="function2"/>
  </gfe:function>
</gfe:function-service>

<bean id="function2" class="example.FunctionTwo"/>
```




<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700


