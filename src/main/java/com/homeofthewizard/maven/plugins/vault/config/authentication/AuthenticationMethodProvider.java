package com.homeofthewizard.maven.plugins.vault.config.authentication;

import com.homeofthewizard.maven.plugins.vault.config.Server;
import io.github.jopenlibs.vault.VaultException;

/**
 * Interface providing methods to get the authentication method from the server config.
 */
public interface AuthenticationMethodProvider {

  AuthenticationMethod fromServer(Server server) throws VaultException;

  AuthenticationMethod fromSystemProperties(Server server, AuthenticationSysProperties systemProperties,
                                            int counter) throws VaultException;
}
