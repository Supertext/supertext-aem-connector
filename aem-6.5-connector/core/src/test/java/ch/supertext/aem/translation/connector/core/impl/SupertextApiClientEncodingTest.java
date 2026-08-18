package ch.supertext.aem.translation.connector.core.impl;

import ch.supertext.aem.translation.connector.core.HttpClientFactory;
import com.adobe.granite.license.ProductInfo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * End-to-end check of what the client actually puts on the wire.
 *
 * A real SupertextApiClient talks to a local stub server over real HTTP, so Gson
 * serialisation, the HttpEntity and Apache HttpClient are all exercised. The stub
 * captures the raw request bytes.
 *
 * Regression guard for the bug where StringEntity(String) defaulted to ISO-8859-1:
 * "Ü" was sent as the single byte 0xDC instead of the UTF-8 pair 0xC3 0x9C, the API
 * could not deserialise the body, and order creation failed with HTTP 500.
 */
public class SupertextApiClientEncodingTest {

    /** Contains characters that differ between ISO-8859-1 and UTF-8. */
    private static final String UMLAUT_TEXT = "Übersetzungsqualität für Prüfung";

    private HttpServer server;
    private volatile byte[] capturedBody;
    private volatile String capturedContentType;

    @Before
    public void startStubServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws java.io.IOException {
                capturedContentType = exchange.getRequestHeaders().getFirst("Content-Type");
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try (InputStream in = exchange.getRequestBody()) {
                    byte[] chunk = new byte[4096];
                    int read;
                    while ((read = in.read(chunk)) != -1) {
                        buffer.write(chunk, 0, read);
                    }
                }
                capturedBody = buffer.toByteArray();

                byte[] response = "[{\"Id\":4711}]".getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream out = exchange.getResponseBody()) {
                    out.write(response);
                }
            }
        });
        server.start();
    }

    @After
    public void stopStubServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void createOrderSendsUtf8EncodedBody() throws Exception {
        SupertextApiClient client = newClient();

        long orderId = client.createOrder("Testauftrag", "de", "fr", UMLAUT_TEXT,
                                          new ArrayList<Long>(), 1L, 166L, 4L);

        assertEquals("stub server should have answered", 4711L, orderId);
        assertNotNull("no request body captured", capturedBody);

        // The umlauts must survive a UTF-8 decode of the exact bytes sent.
        String decoded = new String(capturedBody, StandardCharsets.UTF_8);
        assertTrue("body is not valid UTF-8 / umlauts corrupted, got: " + decoded,
                   decoded.contains(UMLAUT_TEXT));

        // "Ü" must be the two-byte UTF-8 sequence, never the single ISO-8859-1 byte.
        assertTrue("expected UTF-8 encoding of U+00DC (0xC3 0x9C)",
                   indexOf(capturedBody, new byte[] { (byte) 0xC3, (byte) 0x9C }) >= 0);
        assertEquals("body still contains a bare 0xDC, i.e. ISO-8859-1 encoding", -1,
                     indexOf(capturedBody, new byte[] { (byte) 0xDC, (byte) 'b' }));

        // Declaring the charset keeps the server from having to guess.
        assertNotNull("no Content-Type sent", capturedContentType);
        assertTrue("Content-Type must declare UTF-8, got: " + capturedContentType,
                   capturedContentType.toLowerCase().contains("charset=utf-8"));
        assertTrue("Content-Type must stay application/json, got: " + capturedContentType,
                   capturedContentType.toLowerCase().contains("application/json"));
    }

    private SupertextApiClient newClient() {
        String baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/";

        HttpClientFactory factory = new HttpClientFactory() {
            @Override
            public org.apache.http.impl.client.CloseableHttpClient createHttpClient() {
                return HttpClients.createDefault();
            }
        };

        // ProductInfo is an AEM interface; only getShortName/getShortVersion are used.
        ProductInfo productInfo = (ProductInfo) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] { ProductInfo.class },
                (proxy, method, args) -> {
                    if ("getShortName".equals(method.getName())) {
                        return "AEM";
                    }
                    if ("getShortVersion".equals(method.getName())) {
                        return "6.5";
                    }
                    return method.getReturnType().isPrimitive() ? 0 : null;
                });

        return new SupertextApiClient(baseUrl, "user", "secret", factory, productInfo);
    }

    private static int indexOf(byte[] haystack, byte[] needle) {
        outer:
        for (int i = 0; i <= haystack.length - needle.length; i++) {
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
