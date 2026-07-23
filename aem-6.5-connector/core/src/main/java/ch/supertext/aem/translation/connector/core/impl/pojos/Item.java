package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("Id")
    private String id;
    @SerializedName("Content")
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
