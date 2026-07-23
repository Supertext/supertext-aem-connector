package ch.supertext.aem.translation.connector.core.impl.pojos;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuoteOption {
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("ShortDescription")
    private String shortDescription;
    @SerializedName("OrderTypeId")
    private long orderTypeId;
    @SerializedName("OrderTypeConfigurationId")
    private long orderTypeConfigurationId;
    @SerializedName("DeliveryOptions")
    private ArrayList<DeliveryOption> deliveryOptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public long getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public long getOrderTypeConfigurationId() {
        return orderTypeConfigurationId;
    }

    public void setOrderTypeConfigurationId(long orderTypeConfigurationId) {
        this.orderTypeConfigurationId = orderTypeConfigurationId;
    }

    public ArrayList<DeliveryOption> getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(ArrayList<DeliveryOption> deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }
}
