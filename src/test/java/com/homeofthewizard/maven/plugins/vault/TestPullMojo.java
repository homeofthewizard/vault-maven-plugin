package com.homeofthewizard.maven.plugins.vault;

import com.bettercloud.vault.VaultException;
import com.google.common.collect.ImmutableList;
import com.homeofthewizard.maven.plugins.vault.client.VaultClient;
import com.homeofthewizard.maven.plugins.vault.config.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.homeofthewizard.maven.plugins.vault.VaultTestHelper.randomPaths;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TestPullMojo {

    private static final URL VAULT_CERTIFICATE = IntTestVaults.class.getResource("certificate.pem");
    private static final String VAULT_HOST = System.getProperty("vault.host", "localhost");
    private static final String VAULT_PORT = System.getProperty("vault.port", "443");
    private static final String VAULT_SERVER = String.format("https://%s:%s", VAULT_HOST, VAULT_PORT);
    private static final String VAULT_TOKEN = System.getProperty("vault.token");
    private static String githubTokenTag = GithubToken.class.getDeclaredFields()[0].getName();
    private static TreeMap map;
    static {
        map = new TreeMap<>();
        map.put(githubTokenTag,"token");
    }
    private static final Map<String, TreeMap> VAULT_GITHUB_AUTH = Map.of(AuthenticationMethodFactory.GITHUB_TOKEN_TAG, map);
    @Test
    public void testExcecuteSkip() throws MojoExecutionException, URISyntaxException, VaultException {
        List<Path> paths = randomPaths(10, 10);
        var authenticationMethodProvider = Mockito.mock(AuthenticationMethodProvider.class);
        var client = Mockito.mock(VaultClient.class);
        doNothing().when(client).authenticateIfNecessary(any(),any());
        doNothing().when(client).pull(any(),any(),any());

        var mojo = new PullMojo(authenticationMethodProvider, client);
        mojo.project = new MavenProject();
        mojo.servers = ImmutableList.of(new Server(VAULT_SERVER, VAULT_TOKEN, true, new File(VAULT_CERTIFICATE.toURI()), VAULT_GITHUB_AUTH, "", paths, false, 2));
        mojo.skipExecution = true;

        mojo.execute();

        verify(client, times(0)).pull(any(),any(),any());
        verify(client, times(0)).authenticateIfNecessary(any(),any());
    }

    @Test
    public void testExecute() throws MojoExecutionException, URISyntaxException, VaultException {
        List<Path> paths = randomPaths(10, 10);
        var authenticationMethodProvider = Mockito.mock(AuthenticationMethodProvider.class);
        var client = Mockito.mock(VaultClient.class);
        doNothing().when(client).authenticateIfNecessary(any(),any());
        doNothing().when(client).pull(any(),any(),any());

        var mojo = new PullMojo(authenticationMethodProvider, client);
        mojo.project = new MavenProject();
        mojo.servers = ImmutableList.of(new Server(VAULT_SERVER, VAULT_TOKEN, true, new File(VAULT_CERTIFICATE.toURI()), VAULT_GITHUB_AUTH, "", paths, false, 2));
        mojo.skipExecution = false;

        mojo.execute();

        verify(client, times(1)).pull(any(),any(),any());
        verify(client, times(1)).authenticateIfNecessary(any(),any());
    }

    @Test
    public void testPull() throws MojoExecutionException, URISyntaxException, VaultException {
        List<Path> paths = randomPaths(10, 10);
        var authenticationMethodProvider = Mockito.mock(AuthenticationMethodProvider.class);
        var client = Mockito.mock(VaultClient.class);
        doNothing().when(client).pull(any(),any(),any());

        var mojo = new PullMojo(authenticationMethodProvider, client);
        mojo.project = new MavenProject();
        mojo.servers = ImmutableList.of(new Server(VAULT_SERVER, VAULT_TOKEN, true, new File(VAULT_CERTIFICATE.toURI()), VAULT_GITHUB_AUTH, "", paths, false, 2));
        mojo.skipExecution = false;

        mojo.executeVaultOperation();

        verify(client, times(1)).pull(any(),any(),any());
    }

    @Test
    public void testPullSkip() throws MojoExecutionException, URISyntaxException, VaultException {
        List<Path> paths = randomPaths(10, 10);
        var authenticationMethodProvider = Mockito.mock(AuthenticationMethodProvider.class);
        var client = Mockito.mock(VaultClient.class);
        doNothing().when(client).push(any(),any());

        var mojo = new PullMojo(authenticationMethodProvider, client);
        mojo.project = new MavenProject();
        mojo.servers = ImmutableList.of(new Server(VAULT_SERVER, VAULT_TOKEN, true, new File(VAULT_CERTIFICATE.toURI()), VAULT_GITHUB_AUTH, "", paths, false, 2));
        mojo.skipExecution = true;

        mojo.executeVaultOperation();

        verify(client, times(0)).pull(any(),any(),any());
    }

    @Test
    public void testNotPullIfAuthenticationFails() throws MojoExecutionException, URISyntaxException, VaultException {
        List<Path> paths = randomPaths(10, 10);
        var authenticationMethodProvider = Mockito.mock(AuthenticationMethodProvider.class);
        var client = Mockito.mock(VaultClient.class);
        doThrow(VaultException.class).when(client).authenticateIfNecessary(any(),any());
        doNothing().when(client).pull(any(),any(),any());

        var mojo = new PullMojo(authenticationMethodProvider, client);
        mojo.project = new MavenProject();
        mojo.servers = ImmutableList.of(new Server(VAULT_SERVER, VAULT_TOKEN, true, new File(VAULT_CERTIFICATE.toURI()), VAULT_GITHUB_AUTH, "", paths, false, 2));
        mojo.skipExecution = false;

        Assertions.assertThrows(MojoExecutionException.class, ()-> mojo.execute());
        verify(client, times(1)).authenticateIfNecessary(any(),any());
        verify(client, times(0)).pull(any(),any(),any());
    }

    @Test
    public void testPullFails() throws URISyntaxException, VaultException, MojoExecutionException {
        List<Path> paths = randomPaths(10, 10);
        var authenticationMethodProvider = Mockito.mock(AuthenticationMethodProvider.class);
        var client = Mockito.mock(VaultClient.class);
        doThrow(VaultException.class).when(client).pull(any(),any(),any());

        var mojo = new PullMojo(authenticationMethodProvider, client);
        mojo.project = new MavenProject();
        mojo.servers = ImmutableList.of(new Server(VAULT_SERVER, VAULT_TOKEN, true, new File(VAULT_CERTIFICATE.toURI()), VAULT_GITHUB_AUTH, "", paths, false, 2));
        mojo.skipExecution = false;

        var ex = Assertions.assertThrows(MojoExecutionException.class, ()->mojo.executeVaultOperation());
        Assertions.assertTrue(ex.getMessage().contains("Exception thrown pulling secrets."));
    }
}

