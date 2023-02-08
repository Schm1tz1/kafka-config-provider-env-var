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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.schm1tz1.kafka.EnvVarConfigProviderConfig.ENV_VAR_CONFIG_PROVIDER_PATTERN_CONFIG;

public class EnvVarConfigProvider implements ConfigProvider {
    private final Map<String, String> envVarMap;
    private Pattern envVarPattern;

    public EnvVarConfigProvider() {
        envVarMap = getEnvVars();
    }

    public EnvVarConfigProvider(Map<String, String> envVarsAsArgument) {
        envVarMap = envVarsAsArgument;
    }

    private static final Logger log = LoggerFactory.getLogger(EnvVarConfigProvider.class);

    @Override
    public void configure(Map<String, ?> configs) {
        if (configs.keySet().contains(ENV_VAR_CONFIG_PROVIDER_PATTERN_CONFIG)) {
            envVarPattern = Pattern.compile(
                    String.valueOf(configs.get(ENV_VAR_CONFIG_PROVIDER_PATTERN_CONFIG))
            );
        } else {
            envVarPattern = Pattern.compile(".*");
            log.info("No pattern for environment variables provided. Using default pattern '(.*)'.");
        }
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
     * @param path    path, not used for environment variables
     * @param keys the keys whose values will be retrieved.
     * @return the configuration data.
     */
    @Override
    public ConfigData get(String path, Set<String> keys) {

        if (path != null && !path.isEmpty()) {
            log.error("Path is not supported for EnvVarConfigProvider, invalid value '{}'", path);
            throw new ConfigException("Path is not supported for EnvVarConfigProvider, invalid value '" + path + "'");
        }

        if (envVarMap == null) {
            return new ConfigData(new HashMap<>());
        }

        Map<String, String> filteredEnvVarMap = envVarMap;

        filteredEnvVarMap = envVarMap.entrySet().stream()
                .filter(envVar -> envVarPattern.asPredicate().test(envVar.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );

        if (keys == null) {
            return new ConfigData(filteredEnvVarMap);
        }

        HashMap<String, String> filteredData = new HashMap<>(filteredEnvVarMap);
        filteredData.keySet().retainAll(keys);

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
