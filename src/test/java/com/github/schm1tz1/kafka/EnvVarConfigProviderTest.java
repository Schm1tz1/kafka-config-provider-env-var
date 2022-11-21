package com.github.schm1tz1.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EnvVarConfigProviderTest {

    private EnvVarConfigProvider envVarConfigProvider = null;

    @BeforeEach
    public void setup() {
        envVarConfigProvider = new EnvVarConfigProvider();
    }

    @Test
    void testGetAllEnvVarsNotEmpty() {
        assertNotEquals(0, envVarConfigProvider.get("").data().size());
    }

    @Test
    void getShellFromEnvVars() {
        System.out.println(envVarConfigProvider.get("").data().get("SHELL"));
    }

    @Test
    void testWhitelistedEnvVars() {
        Set<String> whiteList = new HashSet<>(Arrays.asList("PWD"));
        Set<String> keys = envVarConfigProvider.get("", whiteList).data().keySet();
        assertEquals(whiteList, keys);
    }

}