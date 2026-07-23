package ch.supertext.aem.translation.connector.core.impl;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.translation.api.TranslationConstants;
import com.day.cq.commons.jcr.JcrUtil;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class JcrOperator {
    private static final Logger log = LoggerFactory.getLogger(JcrOperator.class);
    private static final String JCR_BASE_PATH = "/var/supertext-tms/";
    private static final String JCR_NAME_PROPERTY = "name";
    private static final String JCR_DUEDATE_PROPERTY = "duedate";
    private static final String JCR_STATUS_PROPERTY = "status";
    private static final String JCR_SUPERTEXT_ID_PROPERTY = "supertextid";
    private static final String JCR_SOURCE_LANGUAGE_PROPERTY = "languagesource";
    private static final String JCR_TARGET_LANGUAGE_PROPERTY = "languagetarget";
    private static final String JCR_DESCRIPTION_LANGUAGE_PROPERTY = "description";
    private static final String JCR_UPLOADED_FILES_PROPERTY = "uploadedfiles";
    private static final String JCR_ORDER_TYPE_CONFIGURATION_ID_PROPERTY = "orderTypeConfigurationid";
    private static final String JCR_DELIVERY_ID_PROPERTY = "deliveryid";
    private static final String JCR_WORD_COUNT_PROPERTY = "wordcount";
    private static final String JCR_PRICE_PROPERTY = "price";
    private static final String JCR_CURRENCY_PROPERTY = "currency";
    private static final String JCR_DELIVERY_DATE_PROPERTY = "deliverydate";
    private static final String JCR_CONTENT_NODE = "jcr:content";

    private final ResourceResolverFactory resourceResolverFactory;

    private Session session;
    private ResourceResolver resourceResolver;

    public JcrOperator(ResourceResolverFactory resourceResolverFactory) {
        this.resourceResolverFactory = resourceResolverFactory;
    }

    public void startSession() {
        session = getSupertextServiceResolver().adaptTo(Session.class);
    }

    public void endSession() {
        if (session == null || !session.isLive()) {
            return;
        }

        session.logout();
        session = null;
    }

    public boolean isValidJobPath(String jobPath) {
        return jobPath.startsWith(JCR_BASE_PATH);
    }

    public String createTranslationJob(String name, Date dueDate, String sourceLanguageCode, String targetLanguageCode,
            String description) throws RepositoryException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'/" + getCleanedName(name) + "_'HHmmssSSS");
        String jobPath = JCR_BASE_PATH + formatter.format(new Date());
        Node jobNode = JcrUtil.createPath(jobPath, "sling:Folder", "nt:unstructured", session, false);
        JcrUtil.setProperty(jobNode, JCR_NAME_PROPERTY, name);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(dueDate);
        JcrUtil.setProperty(jobNode, JCR_DUEDATE_PROPERTY, calendar);
        JcrUtil.setProperty(jobNode, JCR_STATUS_PROPERTY, TranslationConstants.TranslationStatus.SUBMITTED.toString());
        JcrUtil.setProperty(jobNode, JCR_SOURCE_LANGUAGE_PROPERTY, sourceLanguageCode);
        JcrUtil.setProperty(jobNode, JCR_TARGET_LANGUAGE_PROPERTY, targetLanguageCode);
        JcrUtil.setProperty(jobNode, JCR_DESCRIPTION_LANGUAGE_PROPERTY, description);

        session.save();

        return jobPath;
    }

    public String getFallbackServerUrl() throws RepositoryException {
        String path = JCR_BASE_PATH + "fallbackConfiguration";
        Node node = JcrUtils.getNodeIfExists(path, session);

        if (node != null) {
            return node.getProperty("serverUrl").getString();
        }

        return "";
    }

    public String getFallbackUsername() throws RepositoryException {
        String path = JCR_BASE_PATH + "fallbackConfiguration";
        Node node = JcrUtils.getNodeIfExists(path, session);

        if (node != null) {
            return node.getProperty("username").getString();
        }

        return "";
    }

    public String getFallbackApiKey() throws RepositoryException {
        String path = JCR_BASE_PATH + "fallbackConfiguration";
        Node node = JcrUtils.getNodeIfExists(path, session);

        if (node != null) {
            return node.getProperty("apiKey").getString();
        }

        return "";
    }

    public String getJobPath(String name, TranslationConstants.TranslationStatus status) throws RepositoryException {
        Node baseNode = JcrUtils.getNodeIfExists(JCR_BASE_PATH, session);

        NodeIterator folderNodes = baseNode.getNodes();
        while (folderNodes.hasNext()) {
            Node folderNode = folderNodes.nextNode();

            NodeIterator projectNodesIterator = folderNode.getNodes(getCleanedName(name) + "_*");

            while (projectNodesIterator.hasNext()) {
                Node projectNode = projectNodesIterator.nextNode();
                String statusProperty = projectNode.getProperty(JCR_STATUS_PROPERTY).getString();
                TranslationConstants.TranslationStatus projectStatus = TranslationConstants.TranslationStatus
                        .fromString(statusProperty);
                if (projectStatus == status) {
                    return projectNode.getPath();
                }
            }
        }

        return null;
    }

    public long getSupertextId(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_SUPERTEXT_ID_PROPERTY).getLong();
    }

    public void setSupertextId(String path, long id) throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_SUPERTEXT_ID_PROPERTY, id);
        session.save();
    }

    public String getName(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_NAME_PROPERTY).getString();
    }

    public Date getDueDate(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_DUEDATE_PROPERTY).getDate().getTime();
    }

    public TranslationConstants.TranslationStatus getTranslationStatus(String path) throws RepositoryException {
        return TranslationConstants.TranslationStatus
                .fromString(getNode(path).getProperty(JCR_STATUS_PROPERTY).getString());
    }

    public void setTranslationStatus(String path, TranslationConstants.TranslationStatus status)
            throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_STATUS_PROPERTY, status.toString());
        session.save();
    }

    public String getSourceLanguage(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_SOURCE_LANGUAGE_PROPERTY).getString();
    }

    public String getTargetLanguage(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_TARGET_LANGUAGE_PROPERTY).getString();
    }

    public String getDescription(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_DESCRIPTION_LANGUAGE_PROPERTY).getString();
    }

    public long getOrderTypeConfigurationId(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_ORDER_TYPE_CONFIGURATION_ID_PROPERTY).getLong();
    }

    public void setOrderTypeConfigurationId(String path, long id) throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_ORDER_TYPE_CONFIGURATION_ID_PROPERTY, id);
        session.save();
    }

    public long getDeliveryId(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_DELIVERY_ID_PROPERTY).getLong();
    }

    public void setDeliveryId(String path, long id) throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_DELIVERY_ID_PROPERTY, id);
        session.save();
    }

    public long getWordCount(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_WORD_COUNT_PROPERTY).getLong();
    }

    public void setWordCount(String path, long wordCount) throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_WORD_COUNT_PROPERTY, wordCount);
        session.save();
    }

    public double getPrice(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_PRICE_PROPERTY).getLong();
    }

    public void setPrice(String path, double price) throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_PRICE_PROPERTY, price);
        session.save();
    }

    public String getCurrency(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_CURRENCY_PROPERTY).getString();
    }

    public void setCurrency(String path, String currency) throws RepositoryException {
        JcrUtil.setProperty(getNode(path), JCR_CURRENCY_PROPERTY, currency);
        session.save();
    }

    public Date getDeliveryDate(String path) throws RepositoryException {
        return getNode(path).getProperty(JCR_DELIVERY_DATE_PROPERTY).getDate().getTime();
    }

    public void setDeliveryDate(String path, Date deliveryDate) throws RepositoryException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(deliveryDate);
        JcrUtil.setProperty(getNode(path), JCR_DELIVERY_DATE_PROPERTY, calendar);
        session.save();
    }

    public ArrayList<Long> getUploadedFiles(String path) throws RepositoryException {
        ArrayList<Long> uploadedFiles = new ArrayList<Long>();

        Node node = getNode(path);
        if (!node.hasProperty(JCR_UPLOADED_FILES_PROPERTY)) {
            return uploadedFiles;
        }

        Value[] values = node.getProperty(JCR_UPLOADED_FILES_PROPERTY).getValues();
        for (Value value : values) {
            uploadedFiles.add(value.getLong());
        }

        return uploadedFiles;
    }

    public void addUploadedFile(String path, long id) throws RepositoryException {
        Node node = getNode(path);

        Long[] ids = new Long[1];
        ids[0] = id;

        if (node.hasProperty(JCR_UPLOADED_FILES_PROPERTY)) {
            Value[] values = node.getProperty(JCR_UPLOADED_FILES_PROPERTY).getValues();
            ids = new Long[values.length + 1];
            for (int i = 0; i < values.length; i++) {
                ids[i] = values[i].getLong();
            }
            ids[values.length] = id;
        }

        JcrUtil.setProperty(node, JCR_UPLOADED_FILES_PROPERTY, ids);
        session.save();
    }

    public InputStream getObject(String path) throws RepositoryException {
        Node objectNode = JcrUtils.getNodeIfExists(path, session);
        Node contentObjectNode = objectNode.getNode(JCR_CONTENT_NODE);
        return JcrUtils.readFile(contentObjectNode);
    }

    public void saveObject(String path, InputStream inputStream) throws RepositoryException {
        Node jcrNode = JcrUtil.createPath(path, "nt:unstructured", "nt:unstructured", session, false);
        ValueFactory valueFactory;
        if (inputStream != null) {
            valueFactory = session.getValueFactory();
            Binary contentValue = valueFactory.createBinary(inputStream);
            Node contentNode = jcrNode.addNode(JCR_CONTENT_NODE, "nt:resource");
            contentNode.setProperty("jcr:data", contentValue);
        }
        session.save();
    }

    public boolean isObjectSaved(String path) throws RepositoryException {
        return JcrUtils.getNodeIfExists(path, session) != null;
    }

    public void notifyProjectMembers(String title, String message, String path) {
        try {
            ResourceResolver resourceResolver = getSupertextServiceResolver();
            TaskManager taskManager = resourceResolver.adaptTo(TaskManager.class);

            Task task = taskManager.getTaskManagerFactory().newTask(Task.DEFAULT_TASK_TYPE);
            task.setName(title);
            task.setDescription(message);
            task.setContentPath(path);

            taskManager.createTask(task);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getCleanedName(String name) {
        return name.replaceAll("[^a-zA-Z0-9]*", "");
    }

    private ResourceResolver getSupertextServiceResolver() {
        if ((this.resourceResolver == null) || (!this.resourceResolver.isLive())) {
            try {
                this.resourceResolver = this.resourceResolverFactory.getServiceResourceResolver(
                        Collections.<String, Object>singletonMap("sling.service.subservice", "supertext-service"));
            } catch (LoginException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return this.resourceResolver;
    }

    private Node getNode(String path) throws RepositoryException {
        session.refresh(true);

        return JcrUtils.getNodeIfExists(path, session);
    }
}
