package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuoteData {
    @SerializedName("ContentType")
    private String contentType;
    @SerializedName("SourceLang")
    private String sourceLang;
    @SerializedName("TargetLanguages")
    private String[] targetLanguages;
    @SerializedName("Files")
    private List<FileData> files;
    @SerializedName("Group")
    private List<Group> groups;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public List<FileData> getFiles() {
        return files;
    }

    public void setFiles(List<FileData> files) {
        this.files = files;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
