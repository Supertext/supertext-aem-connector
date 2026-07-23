package ch.supertext.aem.translation.connector.core.impl.exceptions;

public class HttpClientFactoryException extends Exception {
    private static final long serialVersionUID = -2432706823865707413L;

    public HttpClientFactoryException(String message, Exception e) {
        super(message, e);
    }
}
