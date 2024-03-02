package com.homeofthewizard.maven.plugins.vault.config.authentication;

import java.util.List;

public class AuthenticationSysProperties {
  private final List<String> authMethods;
  private final List<String> pats;
  private final List<String> roleIds;
  private final List<String> secretIds;

  /**
   * AuthenticationSysProperties.
   *
   * @param authMethods authMethods
   * @param pats pats
   * @param roleIds roleIds
   * @param secretIds secretIds
   */
  public AuthenticationSysProperties(List<String> authMethods, List<String> pats,
                                     List<String> roleIds, List<String> secretIds) {
    this.authMethods = authMethods;
    this.pats = pats;
    this.roleIds = roleIds;
    this.secretIds = secretIds;
  }

  /**
   * AuthenticationSysProperties.
   *
   */
  public AuthenticationSysProperties() {
    this.authMethods = List.of();
    this.pats = List.of();
    this.roleIds = List.of();
    this.secretIds = List.of();
  }

  public List<String> getAuthMethods() {
    return authMethods;
  }

  public List<String> getPats() {
    return pats;
  }

  public List<String> getRoleIds() {
    return roleIds;
  }

  public List<String> getSecretIds() {
    return secretIds;
  }
}
