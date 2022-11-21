# kafka-config-provider-env-var [![Java CI with Maven](https://github.com/Schm1tz1/kafka-config-provider-env-var/actions/workflows/maven.yml/badge.svg)](https://github.com/Schm1tz1/kafka-config-provider-env-var/actions/workflows/maven.yml)
This repository contains a **Kafka Configuration Provider for environment variables**. 
It can be used to externalize configs and inject values of environment variales into property values. 
To use this feature, simply add this jat to the classpath of your application and use it with the Apache Kafka ConfigProvider configuration (example for a ssl key provided via env var):
```properties
config.providers=env
config.providers.env.class=com.github.schm1tz1.kafka.EnvVarConfigProvider
ssl.key.password=${env::KEY_PASSPHRASE}
```
Please note the `::` in this example is correct as in the default interface you can provide a *path* parameter that is used e.g. for file or directory providers and has no meaning here so it is kept empty.

There is also a Kafka Improvement Proposal [KIP-887](https://cwiki.apache.org/confluence/display/KAFKA/KIP-887%3A+Add+ConfigProvider+to+make+use+of+environment+variables) in place to add this feature to Apache Kafka.
