package ch.supertext.aem.translation.connector.core;

public interface ProxyConfiguration {
    boolean isProxyEnabled();

    boolean isAuthenticationNeeded();

    String getProxyHost();

    int getProxyPort();

    String getProxyUser();

    String getProxyPassword();
}
