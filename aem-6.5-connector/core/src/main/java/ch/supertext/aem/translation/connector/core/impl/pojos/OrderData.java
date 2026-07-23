package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderData {
    @SerializedName("Id")
    private long id;
    @SerializedName("DeliveryId")
    private long deliveryId;
    @SerializedName("OrderName")
    private String name;
    @SerializedName("OrderTypeConfigurationId")
    private long orderTypeConfigurationId;
    @SerializedName("OrderTypeId")
    private long orderTypeId;
    @SerializedName("Referrer")
    private String referrer;
    @SerializedName("SystemName")
    private String systemName;
    @SerializedName("SystemVersion")
    private String systemVersion;
    @SerializedName("ComponentName")
    private String componentName;
    @SerializedName("ComponentVersion")
    private String componentVersion;
    @SerializedName("SourceLang")
    private String sourceLang;
    @SerializedName("TargetLanguages")
    private String[] targetLanguages;
    @SerializedName("AdditionalInformation")
    private String comment;
    @SerializedName("StatusId")
    private long statusId;
    @SerializedName("WordCount")
    private long wordCount;
    @SerializedName("Files")
    private List<FileData> files;

    public class Status {
        public final static long NEW = 1;
        public final static long DELIVERED = 8;
        public final static long COLLECTED = 9;
        public final static long ON_HOLD = 16;
        public final static long NEEDS_REVISION = 17;
    }

    public OrderData(){}

    public OrderData(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrderTypeConfigurationId() {
        return orderTypeConfigurationId;
    }

    public void setOrderTypeConfigurationId(long orderTypeConfigurationId) {
        this.orderTypeConfigurationId = orderTypeConfigurationId;
    }

    public long getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String[] getTargetLanguages() {
        return targetLanguages;
    }

    public void setTargetLanguages(String[] targetLanguages) {
        this.targetLanguages = targetLanguages;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public long getWordCount() {
        return wordCount;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }

    public List<FileData> getFiles() {
        return files;
    }

    public void setFiles(List<FileData> files) {
        this.files = files;
    }
}
