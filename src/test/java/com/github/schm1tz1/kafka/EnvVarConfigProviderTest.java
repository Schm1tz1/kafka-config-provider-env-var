package com.github.schm1tz1.kafka;

import org.apache.kafka.common.config.ConfigData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EnvVarConfigProviderTest {

    private EnvVarConfigProvider envVarConfigProvider = null;

    @BeforeEach
    public void setup() {
        Map<String, String> testEnvVars = new HashMap<>() {
            {
                put("var1", "value1");
                put("var2", "value2");
                put("var3", "value3");
            }
        };
        envVarConfigProvider = new EnvVarConfigProvider(testEnvVars);
    }

    @Test
    void testGetAllEnvVarsNotEmpty() {
        ConfigData properties = envVarConfigProvider.get("");
        assertNotEquals(0, properties.data().size());
    }

    @Test
    void testGetMultipleKeysAndCompare() {
        ConfigData properties = envVarConfigProvider.get("");
        assertNotEquals(0, properties.data().size());
        assertEquals("value1", properties.data().get("var1"));
        assertEquals("value2", properties.data().get("var2"));
        assertEquals("value3", properties.data().get("var3"));
    }

    @Test
    public void testGetOneKeyWithNullPath() {
        ConfigData config = envVarConfigProvider.get(null, Collections.singleton("var2"));
        Map<String, String> data = config.data();

        assertEquals(1, data.size());
        assertEquals("value2", data.get("var2"));
    }

    @Test
    void testGetWhitelistedEnvVars() {
        Set<String> whiteList = new HashSet<>(Arrays.asList("var1", "var2"));
        Set<String> keys = envVarConfigProvider.get("", whiteList).data().keySet();
        assertEquals(whiteList, keys);
    }

}
