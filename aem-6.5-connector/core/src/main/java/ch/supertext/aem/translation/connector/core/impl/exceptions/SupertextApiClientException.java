package ch.supertext.aem.translation.connector.core.impl.exceptions;

public class SupertextApiClientException extends Exception {
    private static final long serialVersionUID = 2933513679138917347L;

    public SupertextApiClientException(String message) {
        super(message);
    }

    public SupertextApiClientException(String message, Exception e){
        super(message, e);
    }
}
