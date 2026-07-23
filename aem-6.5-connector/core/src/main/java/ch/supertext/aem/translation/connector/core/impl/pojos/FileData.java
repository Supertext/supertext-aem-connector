package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

public class FileData {
    @SerializedName("Id")
    private long id;
    @SerializedName("Name")
    private String name;
    @SerializedName("WordCount")
    private long wordCount;
    @SerializedName("DocumentType")
    private String documentType;

    public FileData(){}

    public FileData(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWordCount() {
        return wordCount;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
