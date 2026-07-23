package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Quote {
    @SerializedName("Currency")
    private String currency;
    @SerializedName("WordCount")
    private long wordCount;
    @SerializedName("Options")
    private ArrayList<QuoteOption> options;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getWordCount() {
        return wordCount;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }

    public ArrayList<QuoteOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<QuoteOption> options) {
        this.options = options;
    }


}
