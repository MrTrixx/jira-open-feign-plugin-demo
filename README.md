# Custom Jira Plugin written with usage OpenFeign library

## Features

- Usage OpenFeign http client in Jira Plugin to reduce boilerplate code for HTTP client
- Custom autoconfiguration Feign Client by BeanFactoryPostProcessor
  - **Notes**: It's not starter spring-cloud-starter-openfeign from Spring Boot
- Allowing creating feign client using bean definition without autoconfiguration

## How to check work

- Check pom.xml how to configure database for local JIRA (I recommend is using PostgreSQL)
  - **Notes**: Use docker compose for local JIRA
- Change jira url (or use default from AMPS) and user credentials in [spring-plugin.properties](src/main/resources/ru/jira/open/feign/plugin/demo/spring-plugin.properties)
- Deploy plugin after atlas-debug or deploy plugin from admin UI in standalone JIRA
- Turn on logs for plugin in admin UI for package ru.jira.open.feign.plugin.demo and set log level DEBUG
- (**Optional**) Run debug mode *local* JIRA
- Send HTTP requests to two plugin endpoints in class [DemoFeignClientResource](src/main/java/ru/jira/open/feign/plugin/demo/resource/DemoFeignClientResource.java)
- Check logs from endpoints 

## Notes 

- You can create Your OpenFeign clients to check integration plugin with any services in Your environment
  - You can choose use autoconfiguration or create OpenFeign client using hardcoding in annotation
- I used OkHttpClient as default under hood client for OpenFeign client interfaces
- Please, also read comments in code