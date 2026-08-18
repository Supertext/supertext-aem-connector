package ch.supertext.aem.translation.connector.core.impl;

import ch.supertext.aem.translation.connector.core.HttpClientFactory;
import ch.supertext.aem.translation.connector.core.impl.exceptions.SupertextApiClientException;
import ch.supertext.aem.translation.connector.core.impl.pojos.*;

import com.adobe.granite.license.ProductInfo;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SupertextApiClient {
    private static final Logger log = LoggerFactory.getLogger(SupertextApiClient.class);
    private static final String QUOTE_URL = "api/v1/translation/quote";
    private static final String CREATE_ORDER_URL = "api/v1.1/translation/order";
    private static final String UPDATE_ORDER_STATUS_URL = "api/v1/order/status";
    private static final String ORDER_FEEDBACK_URL = "api/v1/order/feedback";
    private static final String GET_ORDER_URL = "api/v1/order";
    private static final String UPLOAD_FILE_URL = "api/v1/files/files";
    private static final String GET_FILE_URL = "filedownloads/file";
    private static final String REFERRER_NAME = "Adobe Experience Manager Connector";
    private static final String COMPONENT_NAME = "supertext-connector";
    private static final String COMPONENT_VERSION = "2.0.0";

    private String serverUrl;
    private String authHeader;
    private HttpClientFactory httpClientFactory;
    private ProductInfo productInfo;

    public SupertextApiClient(String serverUrl, String username, String password, HttpClientFactory httpClientFactory,
            ProductInfo productInfo) {
        this.serverUrl = serverUrl;
        this.httpClientFactory = httpClientFactory;
        this.productInfo = productInfo;

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        this.authHeader = "Basic " + new String(encodedAuth);
    }

    public Quote getQuote(String sourceLanguage, String targetLanguage, String text)
            throws SupertextApiClientException {
        return getQuote(getTextQuoteData(sourceLanguage, targetLanguage, text));
    }

    public Quote getQuote(String sourceLanguage, String targetLanguage, ArrayList<Long> uploadedFiles)
            throws SupertextApiClientException {
        return getQuote(getFileQuoteData(sourceLanguage, targetLanguage, uploadedFiles));
    }

    public Quote getQuote(QuoteData quoteData) throws SupertextApiClientException {
        try {

            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            try {
                URI uri = createApiUrl(QUOTE_URL);
                httpclient = httpClientFactory.createHttpClient();

                Gson gson = new Gson();
                String json = gson.toJson(quoteData);
                HttpPost jsonHttpPost = getJsonHttpPost(uri, json);

                log.debug("call rest http post: {} {}", new String[] { uri.toString(), json });
                response = httpclient.execute(jsonHttpPost);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    log.error("error", response.getStatusLine());
                    throw new SupertextApiClientException(
                            "Could not get quotes. Unexpected server response: " + response.getStatusLine());
                }

                HttpEntity entity = response.getEntity();

                return gson.fromJson(new InputStreamReader(entity.getContent()), Quote.class);
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could not get quotes.", e);
        }
    }

    public long createOrder(String name, String sourceLanguage, String targetLanguage, String comment,
            ArrayList<Long> uploadedFiles, long statusId, long orderTypeConfigurationId, long devilveryId)
            throws SupertextApiClientException {
        try {

            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            try {
                URI uri = createApiUrl(CREATE_ORDER_URL);
                httpclient = httpClientFactory.createHttpClient();

                OrderData orderData = getDefaultOrderData(name, sourceLanguage, targetLanguage, comment, uploadedFiles,
                        statusId, orderTypeConfigurationId, devilveryId);

                Gson gson = new Gson();
                String json = gson.toJson(orderData);
                HttpPost jsonHttpPost = getJsonHttpPost(uri, json);

                log.debug("call rest http post: {} {}", new String[] { uri.toString(), json });
                response = httpclient.execute(jsonHttpPost);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    log.error("error", response.getStatusLine());
                    throw new SupertextApiClientException(
                            "Could not create orderData. Unexpected server response: " + response.getStatusLine());
                }

                HttpEntity entity = response.getEntity();

                OrderData[] result = gson.fromJson(new InputStreamReader(entity.getContent()), OrderData[].class);

                return result[0].getId();
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could not create order.", e);
        }
    }

    public void updateOrderStatus(long orderId, long statusId) throws SupertextApiClientException {
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            try {
                URI uri = createApiUrl(UPDATE_ORDER_STATUS_URL + "/" + orderId + "/" + statusId);
                httpclient = httpClientFactory.createHttpClient();

                HttpPut jsonHttpPut = getJsonHttpPut(uri, "{}");

                response = httpclient.execute(jsonHttpPut);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new SupertextApiClientException(
                            "Could not update the order. Unexpected server response: " + response.getStatusLine());
                }
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could not get order.", e);
        }
    }

    public void rejectOrder(long orderId, String commentTitle, String commentMessage) throws SupertextApiClientException {
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            FeedbackComment feedbackComment = new FeedbackComment();
            feedbackComment.setOrderId(orderId);
            feedbackComment.setTitle(commentTitle);
            feedbackComment.setComment(commentMessage);
            Feedback feedback = new Feedback();
            feedback.setOrderId(orderId);
            feedback.setRating(-1);
            feedback.setPostEditing(true);
            feedback.setComments(new FeedbackComment[] { feedbackComment });

            try {
                URI uri = createApiUrl(ORDER_FEEDBACK_URL);
                httpclient = httpClientFactory.createHttpClient();

                Gson gson = new Gson();
                String json = gson.toJson(feedback);
                log.debug("feedback {}", json);
                HttpPost jsonHttpPost = getJsonHttpPost(uri, json);

                response = httpclient.execute(jsonHttpPost);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_ACCEPTED
                        && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new SupertextApiClientException(
                            "Could not reject the order. Unexpected server response: " + response.getStatusLine());
                }
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could not reject the order.", e);
        }
    }

    public OrderData getOrderData(long orderId) throws SupertextApiClientException {
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            try {
                URI uri = createApiUrl(GET_ORDER_URL + "/" + orderId);
                httpclient = httpClientFactory.createHttpClient();
                HttpGet httpGet = getJsonHttpGet(uri);

                response = httpclient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new SupertextApiClientException(
                            "Could not get the order. Unexpected server response: " + response.getStatusLine());
                }

                HttpEntity entity = response.getEntity();
                Gson gson = new Gson();

                return gson.fromJson(new InputStreamReader(entity.getContent()), OrderData.class);
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could not get order.", e);
        }
    }

    public FileData uploadFile(String fileName, InputStream inputStream, String mimeType)
            throws SupertextApiClientException {
        try {

            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            try {
                URI uri = createApiUrl(UPLOAD_FILE_URL);
                httpclient = httpClientFactory.createHttpClient();
                HttpPost httpPost = getFileMultipartHttpPost(uri, fileName, inputStream, mimeType);

                response = httpclient.execute(httpPost);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new SupertextApiClientException("Could not upload a file. Uri: " + uri
                            + " -> Unexpected server response: " + response.getStatusLine());
                }

                HttpEntity entity = response.getEntity();
                Gson gson = new Gson();

                FileData[] result = gson.fromJson(new InputStreamReader(entity.getContent()), FileData[].class);

                return result[0];
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could not upload file.", e);
        }
    }

    public InputStream getFile(long fileId) throws SupertextApiClientException {
        try {

            CloseableHttpClient httpclient = null;
            CloseableHttpResponse response = null;

            try {
                URI uri = createApiUrl(GET_FILE_URL + "/" + fileId + "/" + fileId);
                System.out.println(uri);
                httpclient = httpClientFactory.createHttpClient();
                HttpGet httpGet = getHttpGet(uri);

                response = httpclient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new SupertextApiClientException(
                            "Could not get the files data. Unexpected server response: " + response.getStatusLine());
                }

                HttpEntity entity = response.getEntity();

                byte[] byteArray = IOUtils.toByteArray(entity.getContent());
                // byteArray.length
                return new ByteArrayInputStream(byteArray);
            } finally {
                if (response != null) {
                    response.close();
                }

                if (httpclient != null) {
                    httpclient.close();
                }
            }

        } catch (SupertextApiClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("error", e);
            throw new SupertextApiClientException("Could upload file.", e);
        }
    }

    private URI createApiUrl(String path) throws URISyntaxException {
        String pathWithSlashAtBeginning = !path.startsWith("/") ? "/" + path : path;
        URIBuilder uriBuilder = new URIBuilder(serverUrl);
        String serverUrlPath = uriBuilder.getPath() == null ? "" : uriBuilder.getPath();
        URI finalUri = uriBuilder.setPath(serverUrlPath + pathWithSlashAtBeginning).build().normalize();
        log.debug("Created API URI {} from {} and {}", new String[] { finalUri.toString(), serverUrl, path });
        return finalUri;
    }

    private QuoteData getFileQuoteData(String sourceLanguage, String targetLanguage, ArrayList<Long> uploadedFiles) {
        QuoteData quoteData = new QuoteData();
        quoteData.setContentType("text/html");
        quoteData.setSourceLang(sourceLanguage);
        quoteData.setTargetLanguages(new String[] { targetLanguage });
        quoteData.setFiles(getFileDatas(uploadedFiles));
        return quoteData;
    }

    private QuoteData getTextQuoteData(String sourceLanguage, String targetLanguage, String text) {
        QuoteData quoteData = new QuoteData();
        quoteData.setContentType("text/html");
        quoteData.setSourceLang(sourceLanguage);
        quoteData.setTargetLanguages(new String[] { targetLanguage });
        Item item = new Item();
        item.setContent(text);
        item.setId("1");
        Group group = new Group();
        group.setGroupId("1");
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item);
        group.setItems(items);
        return quoteData;
    }

    private OrderData getDefaultOrderData(String name, String sourceLanguage, String targetLanguage, String comment,
            ArrayList<Long> uploadedFiles, long statusId, long orderTypeConfigurationId, long deliveryId) {
        OrderData orderData = new OrderData();
        orderData.setName(name);
        orderData.setOrderTypeConfigurationId(orderTypeConfigurationId);
        orderData.setDeliveryId(deliveryId);
        orderData.setReferrer(REFERRER_NAME);
        orderData.setSystemName(productInfo.getShortName());
        orderData.setSystemVersion(productInfo.getShortVersion());
        orderData.setComponentName(COMPONENT_NAME);
        orderData.setComponentVersion(COMPONENT_VERSION);
        orderData.setSourceLang(sourceLanguage);
        orderData.setTargetLanguages(new String[] { targetLanguage });
        orderData.setComment(comment);
        orderData.setStatusId(statusId);
        orderData.setFiles(getFileDatas(uploadedFiles));
        return orderData;
    }

    private ArrayList<FileData> getFileDatas(ArrayList<Long> uploadedFiles) {
        ArrayList<FileData> fileDatas = new ArrayList<FileData>();
        for (long uploadedFile : uploadedFiles) {
            fileDatas.add(new FileData(uploadedFile));
        }
        return fileDatas;
    }

    private HttpPost getJsonHttpPost(URI uri, String json) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(json));
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        httpPost.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        return httpPost;
    }

    private HttpGet getJsonHttpGet(URI uri) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        httpGet.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        return httpGet;
    }

    private HttpPut getJsonHttpPut(URI uri, String json) throws UnsupportedEncodingException {
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setEntity(new StringEntity(json));
        httpPut.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        httpPut.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        httpPut.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return httpPut;
    }

    private HttpGet getHttpGet(URI uri) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        return httpGet;
    }

    private HttpPost getFileMultipartHttpPost(URI uri, String fileName, InputStream inputStream, String mimeType) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        httpPost.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("DocumentTypeId", "1", ContentType.TEXT_PLAIN);// Original document type
        builder.addTextBody("ElementTypeId", "2", ContentType.TEXT_PLAIN);// Order
        builder.addBinaryBody("file", inputStream, ContentType.create(mimeType), fileName);

        @SuppressWarnings("PackageAccessibility")
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        return httpPost;
    }
}
