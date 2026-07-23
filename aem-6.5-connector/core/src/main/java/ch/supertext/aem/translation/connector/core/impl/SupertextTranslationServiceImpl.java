/*
*************************************************************************
Supertext AG
Copyright 2020 Supertext AG
Copyright [first year code created] Adobe Systems Incorporated
All Rights Reserved.
 
NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
terms of the Adobe license agreement accompanying it.  If you have received this file from a
source other than Adobe, then your use, modification, or distribution of it requires the prior
written permission of Adobe.
*************************************************************************
 */

package ch.supertext.aem.translation.connector.core.impl;

import ch.supertext.aem.translation.connector.core.impl.config.SupertextTranslationCloudConfigImpl;
import ch.supertext.aem.translation.connector.core.impl.exceptions.SupertextApiClientException;
import ch.supertext.aem.translation.connector.core.impl.pojos.*;
import com.adobe.granite.comments.Comment;
import com.adobe.granite.comments.CommentCollection;
import com.adobe.granite.translation.api.*;
import com.adobe.granite.translation.api.TranslationConstants.TranslationMethod;
import com.adobe.granite.translation.api.TranslationConstants.TranslationStatus;
import com.adobe.granite.translation.core.common.AbstractTranslationService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//THIS IS A PROTOTYPE WITH CODE SMELLS, WORKAROUNDS
public class SupertextTranslationServiceImpl extends AbstractTranslationService {
    private static final Logger log = LoggerFactory.getLogger(SupertextTranslationServiceImpl.class);
    private static final String SERVICE_LABEL = "supertext";
    private static final String SERVICE_ATTRIBUTION = "Translation By Supertext";
    private static final String TAG_METADATA = "/tag-metadata";
    private static final String ASSET_METADATA = "/asset-metadata";
    private static final String I18NCOMPONENTSTRINGDICT = "/i18n-dictionary";
    private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    private SupertextApiClient supertextApiClient;
    private JcrOperator jcrOperator;

    class TranslationScopeImpl implements TranslationScope {
        private long wordCount;
        private double price;
        private String currency;
        private Date deliveryDate;

        public TranslationScopeImpl(long wordCount, double price, String currency, Date deliveryDate) {
            this.wordCount = wordCount;
            this.price = price;
            this.currency = currency;
            this.deliveryDate = deliveryDate;
        }

        @Override
        public int getWordCount() {
            log.debug("TranslationScopeImpl.getWordCount");

            return (int) wordCount;
        }

        @Override
        public int getImageCount() {
            log.debug("TranslationScopeImpl.getImageCount");

            return 0;
        }

        @Override
        public int getVideoCount() {
            log.debug("TranslationScopeImpl.getVideoCount");

            return 0;
        }

        @Override
        public Map<String, String> getFinalScope() {
            log.debug("TranslationScopeImpl.getFinalScope");

            Map<String, String> newScope = new LinkedHashMap<String, String>();
            newScope.put("Word count", Integer.toString(getWordCount()));
            newScope.put("Delivery date", deliveryDate.toString());
            newScope.put("TranslationScope:CostEstimate", currency + " " + price);
            return newScope;
        }
    }

    // Constructor
    public SupertextTranslationServiceImpl(Map<String, String> availableLanguageMap,
            Map<String, String> availableCategoryMap, String name, TranslationConfig translationConfig,
            SupertextApiClient supertextApiClient, JcrOperator jcrOperator) {
        super(availableLanguageMap, availableCategoryMap, name, SERVICE_LABEL, SERVICE_ATTRIBUTION,
                SupertextTranslationCloudConfigImpl.ROOT_PATH, TranslationMethod.HUMAN_TRANSLATION, translationConfig);

        log.debug("SupertextTranslationServiceImpl.ctor");

        this.availableLanguageMap = availableLanguageMap;
        this.supertextApiClient = supertextApiClient;
        this.jcrOperator = jcrOperator;
    }

    @Override
    public Map<String, String> supportedLanguages() {
        log.debug("SupertextTranslationServiceImpl.supportedLanguages");

        return Collections.unmodifiableMap(availableLanguageMap);
    }

    @Override
    public boolean isDirectionSupported(String sourceLanguage, String targetLanguage) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.isDirectionSupported");
        // It should return true, if translation provider provides translation from
        // sourceLanguage to targetLanguage
        // otherwise false

        return sourceLanguage != targetLanguage;
    }

    @Override
    public String detectLanguage(String toDetectSource, TranslationConstants.ContentType contentType)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.detectLanguage");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public TranslationResult translateString(String sourceString, String sourceLanguage, String targetLanguage,
            TranslationConstants.ContentType contentType, String contentCategory) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.translateString");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public TranslationResult[] translateArray(String[] sourceStringArr, String sourceLanguage, String targetLanguage,
            TranslationConstants.ContentType contentType, String contentCategory) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.translateArray");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public TranslationResult[] getAllStoredTranslations(String sourceString, String sourceLanguage,
            String targetLanguage, TranslationConstants.ContentType contentType, String contentCategory, String userId,
            int maxTranslations) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getAllStoredTranslations");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public void storeTranslation(String[] originalText, String sourceLanguage, String targetLanguage,
            String[] updatedTranslation, TranslationConstants.ContentType contentType, String contentCategory,
            String userId, int rating, String path) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.storeTranslation");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public void storeTranslation(String originalText, String sourceLanguage, String targetLanguage,
            String updatedTranslation, TranslationConstants.ContentType contentType, String contentCategory,
            String userId, int rating, String path) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.storeTranslation");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public String createTranslationJob(String name, String description, String sourceLanguage, String targetLanguage,
            Date dueDate, TranslationState state, TranslationMetadata jobMetadata) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.createTranslationJob");
        log.debug("name {}", name);
        log.debug("description {}", description);
        log.debug("sourceLanguage {}", sourceLanguage);
        log.debug("targetLanguage {}", targetLanguage);
        log.debug("dueDate {}", dueDate);
        log.debug("state.getStatusId {}", state.getStatus());
        log.debug("jobMetadata.getTranslationState().getStatusId() {}", jobMetadata.getTranslationState().getStatus());

        jcrOperator.startSession();

        try {
            String jobPath = null;

            if (state.getStatus() == TranslationStatus.SCOPE_COMPLETED) {
                jobPath = jcrOperator.getJobPath(name, TranslationStatus.SCOPE_COMPLETED);
            }

            Date validatedDueDate = validateDueDate(dueDate);

            return jobPath != null ? jobPath
                    : jcrOperator.createTranslationJob(name, validatedDueDate, sourceLanguage, targetLanguage,
                            description);
        } catch (Exception e) {
            String message = "Could not create translation job.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }
    }

    @Override
    public TranslationScope getFinalScope(String jobPath) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getFinalScope");

        jcrOperator.startSession();

        try {
            if (jcrOperator.getTranslationStatus(jobPath) != TranslationStatus.SCOPE_COMPLETED) {
                return new TranslationScopeImpl(0, 0, "", jcrOperator.getDueDate(jobPath));
            }

            return new TranslationScopeImpl(jcrOperator.getWordCount(jobPath), jcrOperator.getPrice(jobPath),
                    jcrOperator.getCurrency(jobPath), jcrOperator.getDeliveryDate(jobPath));
        } catch (Exception e) {
            String message = "Could not get final scope.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }
    }

    @Override
    public TranslationStatus updateTranslationJobState(String jobPath, TranslationState state)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.updateTranslationJobState");
        log.debug("jobPath {}", jobPath);
        log.debug("TranslationState.getStatusId {}", state.getStatus());

        TranslationStatus currentTranslationStatus = state.getStatus();

        if (!jcrOperator.isValidJobPath(jobPath)) {
            return currentTranslationStatus;
        }

        if (currentTranslationStatus == TranslationStatus.CANCEL) {
            return getTranslationJobStatus(jobPath);
        }

        jcrOperator.startSession();

        try {
            if (currentTranslationStatus == TranslationStatus.COMMITTED_FOR_TRANSLATION) {
                log.debug("Uploaded all Translation Objects of job {}", jobPath);

                createOrderWithCorrectDeliveryDate(jobPath);

                currentTranslationStatus = TranslationStatus.TRANSLATION_IN_PROGRESS;
            } else if (currentTranslationStatus == TranslationStatus.SCOPE_REQUESTED) {

                updateScopeData(jobPath);

                currentTranslationStatus = TranslationStatus.SCOPE_COMPLETED;
            } else if (currentTranslationStatus == TranslationStatus.READY_FOR_REVIEW) {
                try {
                    supertextApiClient.updateOrderStatus(jcrOperator.getSupertextId(jobPath),
                            OrderData.Status.COLLECTED);
                } catch (SupertextApiClientException e) {
                    throw new TranslationException("Could not update supertext order state.", e,
                            TranslationException.ErrorCode.GENERAL_EXCEPTION);
                }
            } else if (currentTranslationStatus == TranslationStatus.REJECTED) {
                try {
                    rejectJob(jobPath, state.getComment());
                } catch (SupertextApiClientException e) {
                    throw new TranslationException("Could not update supertext order state.", e,
                            TranslationException.ErrorCode.GENERAL_EXCEPTION);
                }
            }

            jcrOperator.setTranslationStatus(jobPath, currentTranslationStatus);

            return currentTranslationStatus;
        } catch (Exception e) {
            String message = "Could not update translation job.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }
    }

    @Override
    public TranslationStatus getTranslationJobStatus(String jobPath) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getTranslationJobStatus");

        if (!jcrOperator.isValidJobPath(jobPath)) {
            return TranslationStatus.UNKNOWN_STATE;
        }

        jcrOperator.startSession();

        try {
            TranslationStatus localTranslationStatus = jcrOperator.getTranslationStatus(jobPath);

            if (localTranslationStatus != TranslationStatus.TRANSLATION_IN_PROGRESS) {
                return localTranslationStatus;
            }

            OrderData orderData = supertextApiClient.getOrderData(jcrOperator.getSupertextId(jobPath));
            long status = orderData.getStatusId();

            if (status == OrderData.Status.DELIVERED) {
                jcrOperator.setTranslationStatus(jobPath, TranslationStatus.TRANSLATED);
                return TranslationStatus.TRANSLATED;
            }

            return jcrOperator.getTranslationStatus(jobPath);
        } catch (Exception e) {
            String message = "Could not get translation job status.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }
    }

    @Override
    public CommentCollection<Comment> getTranslationJobCommentCollection(String jobPath) {
        log.debug("SupertextTranslationServiceImpl.getTranslationJobCommentCollection");
        return null;
    }

    @Override
    public void addTranslationJobComment(String jobPath, Comment comment) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.addTranslationJobComment");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public InputStream getTranslatedObject(String jobPath, TranslationObject translationObject)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getTranslatedObject");
        log.debug("jobPath {}", jobPath);
        log.debug("translationObject.getId {}", translationObject.getId());
        log.debug("translationObject.getTitle {}", translationObject.getTitle());
        log.debug("translationObject.getTranslationObjectSourcePath {}",
                translationObject.getTranslationObjectSourcePath());
        log.debug("translationObject.getTranslationObjectTargetPath {}",
                translationObject.getTranslationObjectTargetPath());

        jcrOperator.startSession();

        try {
            String path = jobPath + getObjectPath(translationObject);

            if (jcrOperator.isObjectSaved(path)) {
                return jcrOperator.getObject(path);
            }

            String fileNameHash = getFileNameHash(getObjectPath(translationObject), translationObject.getTitle());
            OrderData orderData = supertextApiClient.getOrderData(jcrOperator.getSupertextId(jobPath));

            for (FileData file : orderData.getFiles()) {
                log.debug("TEST HASH {} {}", file.getName(), fileNameHash);
                if (file.getName().startsWith(fileNameHash) && file.getDocumentType().contains("Final")) {
                    return supertextApiClient.getFile(file.getId());
                }
            }

            throw new TranslationException("Translated object not found in the order",
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);

        } catch (Exception e) {
            String message = "Could not get translated object.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }
    }

    @Override
    public String uploadTranslationObject(String jobPath, TranslationObject translationObject)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.uploadTranslationObject");
        log.debug("jobPath {}", jobPath);
        log.debug("translationObj.getId {}", translationObject.getId());
        log.debug("translationObj.getTitle {}", translationObject.getTitle());
        log.debug("translationObj.getTranslationObjectSourcePath {}",
                translationObject.getTranslationObjectSourcePath());
        log.debug("translationObj.getTranslationObjectTargetPath {}",
                translationObject.getTranslationObjectTargetPath());
        log.debug("translationObject.getMimeType(){}", translationObject.getMimeType());

        jcrOperator.startSession();

        try {
            String path = jobPath + getObjectPath(translationObject);

            if (!translationObject.getMimeType().toLowerCase().startsWith("text/")) {
                jcrOperator.saveObject(path, translationObject.getTranslationObjectXMLInputStream());
                return path;
            }

            String fileName = getFileNameHash(getObjectPath(translationObject), translationObject.getTitle()) + "_"
                    + translationObject.getTitle() + ".xml";

            FileData fileData = supertextApiClient.uploadFile(fileName,
                    translationObject.getTranslationObjectXMLInputStream(), translationObject.getMimeType());

            if (fileData.getWordCount() == 0) {
                jcrOperator.saveObject(path, translationObject.getTranslationObjectXMLInputStream());
            } else {
                jcrOperator.addUploadedFile(jobPath, fileData.getId());
            }

            return path;
        } catch (Exception e) {
            String message = "Could not upload translation object.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }
    }

    @Override
    public TranslationStatus updateTranslationObjectState(String jobPath, TranslationObject translationObject,
            TranslationState state) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.updateTranslationObjectState");
        log.debug("jobPath {}", jobPath);
        log.debug("TranslationState.getStatus {}", state.getStatus());

        jcrOperator.startSession();

        try {
            if (state.getStatus() == TranslationStatus.REJECTED) {
                try {
                    rejectJob(jobPath, state.getComment());
                } catch (SupertextApiClientException e) {
                    throw new TranslationException("Could not update supertext order state", e,
                            TranslationException.ErrorCode.GENERAL_EXCEPTION);
                }
            }

        } catch (Exception e) {
            String message = "Could not update translation object.";

            log.error(message, e);

            throw new TranslationException(message, e,
                    TranslationException.ErrorCode.GENERAL_EXCEPTION);
        } finally {
            jcrOperator.endSession();
        }

        return state.getStatus();
    }

    @Override
    public TranslationStatus getTranslationObjectStatus(String jobPath, TranslationObject translationObject)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getTranslationObjectStatus");

        return getTranslationJobStatus(jobPath);
    }

    @Override
    public TranslationStatus[] updateTranslationObjectsState(String jobPath, TranslationObject[] translationObjects,
            TranslationState[] states) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.updateTranslationObjectsState");

        TranslationStatus[] retStatus = new TranslationStatus[states.length];
        for (int index = 0; index < states.length; index++) {
            retStatus[index] = updateTranslationObjectState(jobPath, translationObjects[index], states[index]);
        }
        return retStatus;
    }

    @Override
    public TranslationStatus[] getTranslationObjectsStatus(String jobPath, TranslationObject[] translationObjects)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getTranslationObjectsStatus");

        TranslationStatus[] retStatus = new TranslationStatus[translationObjects.length];
        for (int index = 0; index < translationObjects.length; index++) {
            retStatus[index] = getTranslationObjectStatus(jobPath, translationObjects[index]);
        }
        return retStatus;
    }

    @Override
    public CommentCollection<Comment> getTranslationObjectCommentCollection(String jobPath,
            TranslationObject translationObject) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.getTranslationObjectCommentCollection");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public void addTranslationObjectComment(String jobPath, TranslationObject translationObject, Comment comment)
            throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.addTranslationObjectComment");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public void updateTranslationJobMetadata(String jobPath, TranslationMetadata jobMetadata,
            TranslationMethod translationMethod) throws TranslationException {
        log.debug("SupertextTranslationServiceImpl.updateTranslationJobMetadata");

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    @Override
    public void updateDueDate(String jobPath, Date date) throws TranslationException {
        log.debug("NEW DUE DATE:{}", date);

        throw new TranslationException("This function is not implemented",
                TranslationException.ErrorCode.SERVICE_NOT_IMPLEMENTED);
    }

    private String getFileNameHash(String path, String title) throws UnsupportedEncodingException {
        String toHash = (path + title);
        byte[] digest = DigestUtils.getMd5Digest().digest(toHash.getBytes());

        int halfLength = digest.length / 2;
        byte[] shortenDigest = new byte[halfLength];

        for (int i = 0, j = halfLength; i < halfLength; ++i, ++j) {
            shortenDigest[i] = (byte) (digest[i] ^ digest[j]);
        }

        return new String(Hex.encodeHex(shortenDigest));
    }

    private Date validateDueDate(Date dueDate) {
        if (dueDate != null) {
            return dueDate;
        }

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    private String getSupertextLanguageCode(String languageCode) {
        String supertextLanguageCode = languageCode.replace("_", "-");
        log.debug("Changed language code {} to {}", new String[] { languageCode, supertextLanguageCode });
        return supertextLanguageCode;
    }

    private String getObjectPath(TranslationObject translationObject) {

        if (translationObject.getTranslationObjectSourcePath() != null
                && !translationObject.getTranslationObjectSourcePath().isEmpty()) {
            return translationObject.getTranslationObjectSourcePath();
        } else if (translationObject.getTitle().equals("TAGMETADATA")) {
            return TAG_METADATA;
        } else if (translationObject.getTitle().equals("ASSETMETADATA")) {
            return ASSET_METADATA;
        } else if (translationObject.getTitle().equals("I18NCOMPONENTSTRINGDICT")) {
            return I18NCOMPONENTSTRINGDICT;
        }
        return null;
    }

    private void createOrderWithCorrectDeliveryDate(String jobPath)
            throws RepositoryException, SupertextApiClientException, ParseException, TranslationException {
        updateScopeData(jobPath);

        long deliveryId = jcrOperator.getDeliveryId(jobPath);
        ArrayList<Long> uploadedFiles = jcrOperator.getUploadedFiles(jobPath);

        long orderId = supertextApiClient.createOrder(jcrOperator.getName(jobPath),
                getSupertextLanguageCode(jcrOperator.getSourceLanguage(jobPath)),
                getSupertextLanguageCode(jcrOperator.getTargetLanguage(jobPath)), jcrOperator.getDescription(jobPath),
                uploadedFiles, OrderData.Status.NEW, jcrOperator.getOrderTypeConfigurationId(jobPath), deliveryId);

        jcrOperator.setSupertextId(jobPath, orderId);
    }

    private void updateScopeData(String jobPath)
            throws RepositoryException, SupertextApiClientException, ParseException, TranslationException {
        ArrayList<Long> uploadedFiles = jcrOperator.getUploadedFiles(jobPath);
        Quote quote = supertextApiClient.getQuote(getSupertextLanguageCode(jcrOperator.getSourceLanguage(jobPath)),
                getSupertextLanguageCode(jcrOperator.getTargetLanguage(jobPath)), uploadedFiles);
        QuoteOption quoteOption = quote.getOptions().get(0);
        DeliveryOption deliveryOption = getDeliveryOption(jcrOperator.getDueDate(jobPath), quoteOption);
        Date deliveryDate = ISO_DATE_FORMAT.parse(deliveryOption.getDeliveryDate());

        Date dueDate = jcrOperator.getDueDate(jobPath);
        if (deliveryDate.getTime() > dueDate.getTime()) {
            jcrOperator.notifyProjectMembers("Translation takes longer than project due date",
                    "Please be aware that the delivery date of the translation is after the projects due date.",
                    jobPath);
        }

        saveScopeData(jobPath, quote, quoteOption, deliveryOption, deliveryDate);
    }

    private DeliveryOption getDeliveryOption(Date dueDate, QuoteOption quoteOption) throws ParseException {
        TreeMap<Long, DeliveryOption> timeWithDeliveryIds = new TreeMap<Long, DeliveryOption>(
                Collections.reverseOrder());
        DeliveryOption lastDeliveryOption = new DeliveryOption();

        for (DeliveryOption deliveryOption : quoteOption.getDeliveryOptions()) {
            timeWithDeliveryIds.put(ISO_DATE_FORMAT.parse(deliveryOption.getDeliveryDate()).getTime(),
                    deliveryOption);
        }

        log.debug("DeliveryIds: {}", timeWithDeliveryIds);

        for (Map.Entry<Long, DeliveryOption> entry : timeWithDeliveryIds.entrySet()) {
            if (entry.getKey() <= dueDate.getTime()) {
                return entry.getValue();

            }

            lastDeliveryOption = entry.getValue();
        }

        return lastDeliveryOption;
    }

    private void saveScopeData(String jobPath, Quote quote, QuoteOption quoteOption, DeliveryOption deliveryOption, Date deliveryDate)
            throws RepositoryException, ParseException {
        jcrOperator.setOrderTypeConfigurationId(jobPath, quoteOption.getOrderTypeConfigurationId());
        jcrOperator.setDeliveryId(jobPath, deliveryOption.getDeliveryId());
        jcrOperator.setWordCount(jobPath, quote.getWordCount());
        jcrOperator.setPrice(jobPath, deliveryOption.getPrice());
        jcrOperator.setCurrency(jobPath, quote.getCurrency());
        jcrOperator.setDeliveryDate(jobPath, deliveryDate);
    }

    private void rejectJob(String jobPath, Comment comment) throws SupertextApiClientException, RepositoryException {
        String author = comment == null ? "" : comment.getAuthorName();
        String message = comment == null ? "" : comment.getMessage();
        String safeMessage = message == null ? "" : message.replaceAll("[^\\x00-\\x7F]", "");

        log.debug("Rejecting {} with comment {}", jobPath, safeMessage);

        supertextApiClient.rejectOrder(jcrOperator.getSupertextId(jobPath),
                "Feedback from " + author,
                safeMessage);
    }
}
