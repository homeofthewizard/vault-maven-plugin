/*
 * Copyright 2017 Decipher Technology Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.homeofthewizard.maven.plugins.vault;

import com.homeofthewizard.maven.plugins.vault.client.VaultBackendProvider;
import com.homeofthewizard.maven.plugins.vault.client.VaultClient;
import com.homeofthewizard.maven.plugins.vault.config.OutputMethod;
import com.homeofthewizard.maven.plugins.vault.config.Server;
import com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationMethodFactory;
import com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationMethodProvider;
import com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationSysProperties;
import io.github.jopenlibs.vault.VaultException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Provides an abstract class for mojos that work with Vault.
 */
abstract class VaultMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true)
  protected MavenProject project;

  @Parameter(required = true)
  protected List<Server> servers;

  @Parameter(property = "vault.appRole.roleId")
  protected List<String> roleIds;

  @Parameter(property = "vault.appRole.secretId")
  protected List<String> secretIds;

  @Parameter(property = "vault.github.pat")
  protected List<String> pats;

  @Parameter(property = "vault.authenticationMethod")
  protected List<String> authMethods;

  @Parameter(defaultValue = "MavenProperties", property = "vault.outputMethod")
  protected OutputMethod outputMethod;

  @Parameter(defaultValue = "false", property = "vault.skipExecution")
  protected boolean skipExecution;

  private final AuthenticationMethodProvider authenticationMethodProvider;
  protected final VaultClient vaultClient;

  VaultMojo() {
    this.authenticationMethodProvider = new AuthenticationMethodFactory();
    var vaultBackendProvider = new VaultBackendProvider();
    this.vaultClient = VaultClient.createForBackend(vaultBackendProvider);
  }

  VaultMojo(AuthenticationMethodProvider authenticationMethodProvider,
            VaultClient vaultClient) {
    this.authenticationMethodProvider = authenticationMethodProvider;
    this.vaultClient = vaultClient;
  }

  @Override
  public void execute() throws MojoExecutionException {
    if (this.skipExecution) {
      return;
    }
    executeVaultAuthentication();
    executeVaultOperation();
  }

  private void executeVaultAuthentication() throws MojoExecutionException {
    try {
      var authSystemArgs = new AuthenticationSysProperties(authMethods, pats, roleIds, secretIds);
      vaultClient.authenticateIfNecessary(servers, authSystemArgs, authenticationMethodProvider);
    } catch (VaultException e) {
      throw new MojoExecutionException("Exception thrown authenticating.", e);
    }
  }

  abstract void executeVaultOperation() throws MojoExecutionException;
}
