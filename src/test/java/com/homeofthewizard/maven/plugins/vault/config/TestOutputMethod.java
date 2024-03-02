package com.homeofthewizard.maven.plugins.vault.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class TestOutputMethod {

    @Test
    public void shouldStoreMavenProperties(){
        var mavenPropOutMethod = OutputMethod.MavenProperties;
        var properties = new Properties();
        var secrets = new HashMap<String, String>();
        secrets.put("testSecretKey", "testSecretVal");
        var mapping = new Mapping("testSecretKey", "testPropertyName");

        mavenPropOutMethod.flush(properties, secrets, mapping);

        Assertions.assertTrue(properties.containsKey("testPropertyName"));
        Assertions.assertEquals("testSecretVal", properties.getProperty("testPropertyName"));
    }

    @Test
    public void shouldStoreSystemProperties(){
        var sysPropOutMethod = OutputMethod.SystemProperties;
        var properties = new Properties();
        var secrets = new HashMap<String, String>();
        secrets.put("testSecretKey1", "testSecretVal1");
        secrets.put("testSecretKey2", "testSecretVal2");
        var mapping1 = new Mapping("testSecretKey1", "testPropertyName1");
        var mapping2 = new Mapping("testSecretKey2", "testPropertyName2");

        sysPropOutMethod.flush(properties, secrets, mapping1);
        sysPropOutMethod.flush(properties, secrets, mapping2);

        Assertions.assertTrue(System.getProperties().containsKey("testPropertyName1"));
        Assertions.assertTrue(System.getProperties().containsKey("testPropertyName2"));
        Assertions.assertEquals("testSecretVal1", System.getProperty("testPropertyName1"));
        Assertions.assertEquals("testSecretVal2", System.getProperty("testPropertyName2"));
    }

    @Test
    public void shouldStoreEnvFile(){
        var envFileOutMethod = OutputMethod.EnvFile;
        var properties = new Properties();
        var secrets = new HashMap<String, String>();
        secrets.put("testSecretKey1", "testSecretVal1");
        secrets.put("testSecretKey2", "testSecretVal2");
        var mapping1 = new Mapping("testSecretKey1", "testPropertyName1");
        var mapping2 = new Mapping("testSecretKey2", "testPropertyName2");

        envFileOutMethod.flush(properties, secrets, mapping1);
        envFileOutMethod.flush(properties, secrets, mapping2);

        var envFile = Paths.get(".env").toFile();
        Assertions.assertTrue(envFile.exists());
        var createdProps = readEnvFile(envFile);
        Assertions.assertTrue(createdProps.containsKey("testPropertyName1"));
        Assertions.assertTrue(createdProps.containsKey("testPropertyName2"));
        Assertions.assertEquals("testSecretVal1", createdProps.getProperty("testPropertyName1"));
        Assertions.assertEquals("testSecretVal2", createdProps.getProperty("testPropertyName2"));
        Assertions.assertTrue(envFile.delete());
    }

    private Properties readEnvFile(File envFile) {
        Properties prop = new Properties();
        try(InputStream fis = new FileInputStream(envFile)) {
            prop.load(fis);
        }
        catch(Exception e) {
            System.out.println("Unable to find the specified properties file");
            e.printStackTrace();
        }
        return prop;
    }
}
