---
layout: page
title: The rationals behind it
nav_order: 2
---

## Why and When to use it ?
Today, most of the software applications are communicating with other applications, using some sort of credentials for identifying themselves before establishing a secure communication,    
or some encryption keys for securing the communication itself.  
[Hashicorp Vault](https://www.vaultproject.io/) allows us to securely store those secrets, and share them with necessary counterparts.
Those counterparts may be developers who want to run the application on their local environments, or the application's different execution environments (Test, Production, CI/CD ect...)

Organisations that has a Hashicorp Vault, in general, are already using a cloud based architecture.    
Hence, already have some ways for the application's credentials to be fetched automatically from Vault, instead of storing them locally on each environment separately.  
Ex: The most popular way for an app running on Orchestrated Container environments like Kubernetes is the [vault agent sidecar](https://www.hashicorp.com/blog/refresh-secrets-for-kubernetes-applications-with-vault-agent).    
CI tools like Jenkins have a Vault plugin to fetch the secrets from Vault server.  
Some java frameworks provide solutions so that your application can fetch secrets from vault directly, like [spring-cloud-vault](https://cloud.spring.io/spring-cloud-vault/reference/html/) or the Quarkus [vault-extension](https://docs.quarkiverse.io/quarkus-vault/dev/index.html).

All those plugins/tools, have advantages and disadvantages, making each of the solutions ideal for specific use cases. The maven-vault plugin has its special usage as well.

{: .important }
Its biggest differences are: the simple usage (single config file for all environments, pom.xml), portability and flexibility leveraging maven's pure java platform and plugin based system. 

---

## Comparing the alternatives

### 1. Configuration
First of all the other alternatives require you to declare your secrets on their own configuration file, causing duplication among different config files.
For example the vault agent has a specific configuration file. Spring cloud asks you to change lots of thins in your code base to use it.  
both solutions require a new skill and knowledge to use it.

By using the maven plugin to fetch your secrets, you can unify all your configuration and secrets declaration in a single file, the pom.xml.  
No need to learn a new tool. :massage:

### 2. Portability
Environment specific tools, like vault agent for kubernetes or plugins for the CI tools, have their advantage in being optimised for that environment. But forces us to maintain multiple configs for multiple environments. Which makes the configuration overhead even worse.  
It may also be the case that :
* Your enterprise's IT infrastructure does not have such tools yet (no containers, no CI tools with plugins),
* It may be in a transition phase, or maybe some legacy applications that are not compatible with such tools.
* **Such tools are not available on developers' local environments,**  
  where lots of enterprises still provide Windows PCs without a Docker or K8s. 

Maven, hence the maven plugin, can be used wherever a JDK is available.  
  
Also, the solutions provided by development frameworks like Spring cloud or Quarkus keeps the portability of the java environment, but asks you to change a lot your codebase, and they are framework specific.  
Plus, according to the 12-factor app [requirements](https://www.hashicorp.com/blog/twelve-factor-applications-with-consul), there should be a clear separation between the configuration and the business logic of the application,
which is clearly not the case when you embed in your application jar the management of your config and secrets.

This plugin aims to help all those cases. :santa:

---

## Big picture

|                                                                        | portability | fast execution | less configuration overhead | 12 factor compatibility |
|------------------------------------------------------------------------|:-----------:|:--------------:|:---------------------------:|:-----------------------:|
| vault agent                                                            |             |       ✅        |                             |                         |
| spring-cloud-vault                                                     |      ✅      |       ✅        |                             |                         |
| vaul-maven-plugin                                                      |      ✅      |                |              ✅              |            ✅            |
| **[maven deamon](https://github.com/apache/maven-mvnd)** (coming soon) |      ✅      |       ✅        |              ✅              |            ✅            |

A performance study of the execution of this plugin via mvnd is done [here](https://github.com/HomeOfTheWizard/vault-mvnd-benchmark).

### Special cases 

{: .warning }
The idea behind using plugins/tools optimised for specific environments, like jenkins Vault plugin or the Vault Agent on K8s, is to make your app boot faster.  
Maven before running the plugin to fetch the secrets, and run your application, does lots of things. This of course has its overhead in terms of execution time and memory footprint.

But there are some cases, for example batch applications, where we may prefer to tradeoff for the simplicity of configuration over the speed of startup. ⚖️

Another sweet spot for the maven plugin is when you do not use the [dynamic secrets](https://www.hashicorp.com/blog/why-we-need-dynamic-secrets) or you do not modify your secrets on the [kv engine]() very often.
If you are sensible to the boot time of your application, lets say you have a web scaling app running on K8s. 
Keep in mind that fetching the secrets in time of execution has an implication of I/O, network and additional time of startup.
You can use this plugin to create your secret.yaml for K8s at its deployment time instead of the boot time. :wink:

:rocket: With the speed of mvnd, the execution of this plugin may soon be feasible even at the startup of your pod! see the study on this topic [here](https://github.com/HomeOfTheWizard/vault-mvnd-benchmark), and ongoing discussions [here](https://github.com/apache/maven-mvnd/issues/496). 
