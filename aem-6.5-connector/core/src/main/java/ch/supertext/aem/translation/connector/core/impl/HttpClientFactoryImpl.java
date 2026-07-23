package ch.supertext.aem.translation.connector.core.impl;

import ch.supertext.aem.translation.connector.core.HttpClientFactory;
import ch.supertext.aem.translation.connector.core.ProxyConfiguration;
import ch.supertext.aem.translation.connector.core.impl.exceptions.HttpClientFactoryException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;

public class HttpClientFactoryImpl implements HttpClientFactory {
    private static final Logger log = LoggerFactory.getLogger(HttpClientFactoryImpl.class);
    private final ProxyConfiguration networkConfiguration;

    public HttpClientFactoryImpl(ProxyConfiguration networkConfiguration) {
        this.networkConfiguration = networkConfiguration;
    }

    @Override
    public CloseableHttpClient createHttpClient() throws HttpClientFactoryException {
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (TrustStrategy) (arg0, arg1) -> true).build();

            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

            httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sslContext);

            if (networkConfiguration.isProxyEnabled()) {
                log.debug("Proxy enabled with {} {}", networkConfiguration.getProxyHost(), networkConfiguration.getProxyPort());

                HttpHost proxy = new HttpHost(networkConfiguration.getProxyHost(), networkConfiguration.getProxyPort());

                httpClientBuilder.setProxy(proxy);

                if (networkConfiguration.isAuthenticationNeeded()) {
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(
                            networkConfiguration.getProxyUser(), networkConfiguration.getProxyPassword()));

                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            }

            return httpClientBuilder.build();
        } catch (Exception e) {
            throw new HttpClientFactoryException("Could not create http client allowing all ssl certificate", e);
        }
    }
}
