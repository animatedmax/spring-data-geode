<div id="header">

# Document Structure




The following chapters explain the core functionality offered by
Spring Data for GemFire:


<div class="ulist">

- [\[bootstrap\]](#bootstrap) describes the configuration support
  provided for configuring, initializing, and accessing
  GemFire Caches, Regions, and related distributed system
  components.

- [\[apis\]](#apis) explains the integration between the
  GemFire APIs and the various data access features available
  in Spring, such as template-based data access, exception translation,
  transaction management, and caching.

- [\[serialization\]](#serialization) describes enhancements to
  GemFire's serialization and deserialization of managed
  objects.

- [\[mapping\]](#mapping) describes persistence mapping for POJOs stored
  in GemFire using Spring Data.

- [\[gemfire-repositories\]](#gemfire-repositories) describes how to
  create and use Spring Data Repositories to access data stored in
  GemFire by using basic CRUD and simple query operations.

- [\[function-annotations\]](#function-annotations) describes how to
  create and use GemFire Functions by using annotations to
  perform distributed computations where the data lives.

- [\[apis:continuous-query\]](#apis:continuous-query) describes how to
  use GemFire's Continuous Query (CQ) functionality to process
  a stream of events based on interest that is defined and registered
  with GemFire's OQL (Object Query Language).

- [\[gemfire-bootstrap\]](#gemfire-bootstrap) describes how to configure
  and bootstrap a Spring `ApplicationContext` running in an
  GemFire server using `Gfsh`.

- [\[samples\]](#samples) describes the examples provided with the
  distribution to illustrate the various features available in
  Spring Data for GemFire.



<div id="footer">

<div id="footer-text">

Last updated 2022-09-20 10:33:13 -0700


