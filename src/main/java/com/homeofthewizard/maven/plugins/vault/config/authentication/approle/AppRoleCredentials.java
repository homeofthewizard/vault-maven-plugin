package com.homeofthewizard.maven.plugins.vault.config.authentication.approle;

import java.io.Serializable;

public class AppRoleCredentials implements Serializable {
  private String roleId;
  private String secretId;

  public AppRoleCredentials(String roleId, String secretId) {
    this.roleId = roleId;
    this.secretId = secretId;
  }

  public AppRoleCredentials() {
  }

  public String getSecretId() {
    return secretId;
  }

  public String getRoleId() {
    return roleId;
  }
}
