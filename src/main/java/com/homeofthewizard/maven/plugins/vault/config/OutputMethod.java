package com.homeofthewizard.maven.plugins.vault.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Properties;

public enum OutputMethod {
  MavenProperties{
    @Override
    public void flush(Properties properties, Map<String, String> secrets, Mapping mapping) {
      setMavenProperties(properties, secrets, mapping);
    }
  },
  SystemProperties{
    @Override
    public void flush(Properties properties, Map<String, String> secrets, Mapping mapping) {
      setSystemProperties(secrets, mapping);
    }
  },
  EnvFile{
    @Override
    public void flush(Properties properties, Map<String, String> secrets, Mapping mapping) {
      createEnvFile(secrets, mapping);
    }
  };

  public abstract void flush(Properties properties, Map<String, String> secrets, Mapping mapping);

  /**
   * Creates an .envFile and put the secrets in it, respecting the key/property mapping definition given.
   * @param secrets secrets fetched from Vault.
   * @param mapping mapping defined in maven project.
   */
  private static void createEnvFile(Map<String, String> secrets, Mapping mapping) {
    try (FileWriter fileWriter = new FileWriter(".env", true)) {
      var buffer = new BufferedWriter(fileWriter);
      var printer = new PrintWriter(buffer);
      printer.format("%s=%s\n",mapping.getProperty(),secrets.get(mapping.getKey()));
      printer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets the secrets in System.getProperties(), respecting the key/property mapping definition given.
   * @param secrets secrets fetched from Vault.
   * @param mapping mapping defined in maven project.
   */
  private static void setSystemProperties(Map<String, String> secrets, Mapping mapping) {
    System.setProperty(mapping.getProperty(), secrets.get(mapping.getKey()));
  }

  /**
   * Sets the secrets in mavenProject.properties, respecting the key/property mapping definition given.
   * @param properties maven project properties
   * @param secrets secrets fetched from Vault.
   * @param mapping mapping defined in maven project.
   */
  private static void setMavenProperties(Properties properties, Map<String, String> secrets, Mapping mapping) {
    properties.setProperty(mapping.getProperty(), secrets.get(mapping.getKey()));
  }
}
