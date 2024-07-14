---
layout: page
title: About Vault Maven Plugin
nav_order: 1
---

# Maven comes to the rescue to manage your app credentials
{: .fs-9 }

The Vault Maven Plugin allows you to fetch credentials from HashiCorp Vault and make it available to your application.  
Leveraging maven's portability and its plugin based system,  
it allows easy integration with your app and can be used as a single tool for all environments (developers’ local, CI pipelines, or production K8s environment).
{: .fs-6 .fw-300 }

[![Quick Start](https://img.shields.io/badge/-Quick%20Start%20%F0%9F%9A%80-blue?style=for-the-badge&logo=rocket)](/vault-maven-plugin/usage.html)
[![Github button](https://img.shields.io/badge/-View%20it%20on%20Github-gray?style=for-the-badge&logo=github)](https://github.com/HomeOfTheWizard/vault-maven-plugin)
[![Maven Central Releases](https://img.shields.io/badge/-Maven%20Releases-orange?style=for-the-badge&logo=apache%20maven)](https://central.sonatype.com/artifact/com.homeofthewizard/vault-maven-plugin)
[![JavaDoc](https://img.shields.io/badge/-JavaDocs%F0%9F%93%84-green?style=for-the-badge)](https://www.javadoc.io/doc/com.homeofthewizard/vault-maven-plugin/latest)


---


## What is it ?
Basically, a maven plugin, hence an application that can be executed via maven command line,  
like `mvn vault:pull` to retrieve your application's secrets from Hashicorp Vault for you,  
or `mvn vault:push` to push new secrets to your Vault instance.     
   
By providing a simple configuration via a `pom.xml`, giving the necessary identifications and secrets' keys to fetch, it fetches them for you and inject them as environment variables to the execution context of maven.  
  
As a result your application can use its secrets directly from those env variables, which are existing only during it's execution, instead of storing them locally. Which is more secure :policeman:  
The only thing to manage is the credentials to login into Vault, and the list of secret keys you need :massage_man:.  
* * *
## Why and When to use it ?
An alternative to the [vault agent](https://developer.hashicorp.com/vault/docs/agent-and-proxy/agent) and framework specific tools like [spring-cloud-vault](https://cloud.spring.io/spring-cloud-vault/reference/html/), the project aims to answer use cases that  
require the separation of the configuration from the component containing the business logic,    
and require the portability and flexibility of the component managing the secrets,  
while keeping the necessary configuration for all that as simple as possible (a single pom.xml file).  
  
For more details, have a look at the [rationals](rationals.markdown) behind the project.

* * *
## Example use cases
Some CI tools that already working with maven, for example liquibase or sonar-scanner which both have maven plugins,    
are the best suite for using the Vault Maven Plugin.  
  
Java applications that use maven to build/package can also profit from it.     
Spring already have a Spring-Vault library providing the same help. But it may be the case that you do not use spring-cloud.    
  
Even if you do not use maven at all, you can create a `pom.xml` just for the configuration of this plugin that will fetch the secrets,   
and pass the secrets to your application via some other way.  
Example: another maven plugin that exports the environment variables to the execution context of your application.      
   
We provide an example for each use cases above, [further](/vault-maven-plugin/examples.html) in this documentation.  
