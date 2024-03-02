package com.homeofthewizard.maven.plugins.vault.config.authentication.approle;

import static com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationMethodFactory.APP_ROLE_TAG;

import com.homeofthewizard.maven.plugins.vault.config.Server;
import com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationMethod;
import io.github.jopenlibs.vault.VaultException;
import io.github.jopenlibs.vault.api.Auth;


public class AppRoleAuthMethod extends AuthenticationMethod<AppRoleCredentials> {
  private Server server;
  private String roleId;
  private String secretId;

  /**
   * Initializes a new instance of the {@link AuthenticationMethod} class.
   *
   * @param auth Auth
   * @param server Server
   */
  public AppRoleAuthMethod(Auth auth, Server server) {
    super(auth, AppRoleCredentials.class);
    this.server = server;
  }

  /**
   * AppRoleAuthMethod.
   *
   * @param auth authentcation
   * @param roleId roleId
   * @param secretId secrectId
   */
  public AppRoleAuthMethod(Auth auth, String roleId, String secretId, Server server) {
    super(auth, AppRoleCredentials.class);
    this.roleId = roleId;
    this.secretId = secretId;
    this.server = server;
  }

  /**
   * A method that helps authenticate via a git AppRole.
   *
   * @throws VaultException in case authentication fails
   */
  public void login() throws VaultException {
    var appRoleCredentials = (roleId != null && secretId != null)
            ? new AppRoleCredentials(roleId, secretId)
            : getAuthCredentials(server.getAuthentication().get(APP_ROLE_TAG));

    String token = auth
            .loginByAppRole(appRoleCredentials.getRoleId(), appRoleCredentials.getSecretId())
            .getAuthClientToken();

    server.setToken(token);
  }
}
