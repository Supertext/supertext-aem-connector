package ch.supertext.aem.translation.connector.core;

import ch.supertext.aem.translation.connector.core.impl.exceptions.HttpClientFactoryException;
import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientFactory {
    CloseableHttpClient createHttpClient() throws HttpClientFactoryException;
}
