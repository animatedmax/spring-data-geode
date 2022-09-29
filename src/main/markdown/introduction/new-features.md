---
title: New Features
---

<!-- 
 Copyright (c) VMware, Inc. 2022. All rights reserved.
 Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 agreements. See the NOTICE file distributed with this work for additional information regarding
 copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance with the License. You may obtain a
 copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 or implied. See the License for the specific language governing permissions and limitations under
 the License.
-->

<!--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

Note
</div></td>
<td class="content">As of the 1.2.0.RELEASE, this project, formerly
known as Spring GemFire, has been renamed to {sdg-name} to reflect that
it is now a module of the {spring-data-website}[Spring Data] project and
built on {x-data-store-website}[{data-store-name}].</td>
</tr>
</tbody>
</table>




<div class="sect1">

## New in the 2.0 Release



<div class="ulist">

- Upgraded to {data-store-name} 9.1.1.

- Upgraded to Spring Data Commons 2.0.8.RELEASE.

- Upgraded to Spring Framework 5.0.7.RELEASE.

- Reorganized the SDG codebase by packaging different classes and
  components by concern.

- Added extensive support for Java 8 types, particularly in the SD
  Repository abstraction.

- Changed to the Repository interface and abstraction, e.g. IDs are no
  longer required to be `java.io.Serializable`.

- Set `@EnableEntityDefinedRegions` annotation `ignoreIfExists`
  attribute to `true` by default.

- Set `@Indexed` annotation `override` attribute to `false` by default.

- Renamed `@EnableIndexes` to `@EnableIndexing`.

- Introduced a `InterestsBuilder` class to easily and conveniently
  express Interests in keys and values between client and server when
  using JavaConfig.

- Added support in the Annotation configuration model for Off-Heap,
  Redis Adapter, and {data-store-name}'s new Security framework.




<div class="sect1">

## New in the 2.1 Release



<div class="ulist">

- Upgraded to {data-store-name} {data-store-version}.

- Upgraded to Spring Framework 5.1.0.RELEASE.

- Upgraded to Spring Data Commons 2.1.0.RELEASE.

- Added support for parallel cache/Region snapshots along with invoking
  callbacks when loading snapshots.

- Added support for registering QueryPostProcessors to customize the OQL
  generated fro Repository query methods.

- Added support for include/exclude TypeFilters in
  o.s.d.g.mapping.MappingPdxSerializer.

- Updated docs.




<div class="sect1">

## New in the 2.2 Release



<div class="ulist">

- Upgraded to {data-store-name} {data-store-version}.

- Upgraded to Spring Framework 5.2.0.RELEASE.

- Upgraded to Spring Data Commons 2.2.0.RELEASE.

- Add Annotation configuration support to configure and bootstrap
  {data-store-name} Locator applications using `@LocatorApplication`.

- Added Annotation configuration support for GatewayReceivers and
  GatewaySenders.

- Updated docs.





<div id="footer">

<div id="footer-text">

Last updated 2022-09-21 15:45:31 -0700


