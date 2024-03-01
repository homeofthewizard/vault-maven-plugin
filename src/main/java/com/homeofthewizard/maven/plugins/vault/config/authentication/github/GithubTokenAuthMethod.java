package com.homeofthewizard.maven.plugins.vault.config.authentication.github;

import static com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationMethodFactory.GITHUB_TOKEN_TAG;

import com.homeofthewizard.maven.plugins.vault.config.Server;
import com.homeofthewizard.maven.plugins.vault.config.authentication.AuthenticationMethod;
import io.github.jopenlibs.vault.VaultException;
import io.github.jopenlibs.vault.api.Auth;

public class GithubTokenAuthMethod extends AuthenticationMethod<GithubToken> {

  private Server server;
  private String cliPat;

  /**
   * Initializes a new instance of the {@link GithubTokenAuthMethod} class.
   *
   * @param auth Auth
   * @param cliPat String
   * @param server Server
   */
  public GithubTokenAuthMethod(Auth auth, String cliPat, Server server) {
    super(auth, GithubToken.class);
    this.cliPat = cliPat;
    this.server = server;
  }

  /**
   * Initializes a new instance of the {@link GithubTokenAuthMethod} class.
   *
   * @param auth Auth
   * @param server Server
   */
  public GithubTokenAuthMethod(Auth auth, Server server) {
    super(auth, GithubToken.class);
    this.server = server;
  }

  /**
   * A method that helps authenticate via a git PAT.
   *
   * @throws VaultException in case authentication fails
   */
  public void login() throws VaultException {
    var githubPat = cliPat != null
            ? cliPat : getAuthCredentials(server.getAuthentication().get(GITHUB_TOKEN_TAG)).getPat();

    String token = auth
                .loginByGithub(githubPat)
                .getAuthClientToken();

    server.setToken(token);
  }
}
