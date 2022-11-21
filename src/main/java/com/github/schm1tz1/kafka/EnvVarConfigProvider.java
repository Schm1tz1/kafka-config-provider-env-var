package com.github.schm1tz1.kafka;

import org.apache.kafka.common.config.ConfigData;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.provider.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnvVarConfigProvider implements ConfigProvider {

    private static final Logger log = LoggerFactory.getLogger(EnvVarConfigProvider.class);

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public void close() throws IOException {
    }

    /**
     * @param s unused
     * @return returns environment variables as configuration
     */
    @Override
    public ConfigData get(String s) {
        return get(s, null);
    }

    /**
     * @param s    unused
     * @param keys the keys whose values will be retrieved.
     * @return the configuration data.
     */
    @Override
    public ConfigData get(String s, Set<String> keys) {
        Map<String, String> data = getEnvVars();

        if (data == null) {
            return new ConfigData(new HashMap<>());
        }

        if (keys == null) {
            return new ConfigData(data);
        }

        Map<String, String> filteredData = data
                .entrySet()
                .stream()
                .filter(envVar -> keys.contains(envVar.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new ConfigData(filteredData);
    }

    private Map<String, String> getEnvVars() {
        try {
            return System.getenv();
        } catch (Exception e) {
            log.error("Could not read environment variables", e);
            throw new ConfigException("Could not read environment variables");
        }
    }
}
