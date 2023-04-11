# kafka-config-provider-env-var [![Java CI with Maven](https://github.com/Schm1tz1/kafka-config-provider-env-var/actions/workflows/maven.yml/badge.svg)](https://github.com/Schm1tz1/kafka-config-provider-env-var/actions/workflows/maven.yml)
This repository contains a **Kafka Configuration Provider for environment variables**. 
It can be used to externalize configs and inject values of environment variales into property values. 
To use this feature, simply add this jat to the classpath of your application and use it with the Apache Kafka ConfigProvider configuration (example for a ssl key provided via env var):
```properties
config.providers=env
config.providers.env.class=com.github.schm1tz1.kafka.EnvVarConfigProvider
ssl.key.password=${env:KEY_PASSPHRASE}
```

There is also a Kafka Improvement Proposal [KIP-887](https://cwiki.apache.org/confluence/display/KAFKA/KIP-887%3A+Add+ConfigProvider+to+make+use+of+environment+variables) merged into Apache Kafka trunk. Planned for v3.5.0.
