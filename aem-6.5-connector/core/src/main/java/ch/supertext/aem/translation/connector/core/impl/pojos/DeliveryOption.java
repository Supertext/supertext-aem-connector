package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

public class DeliveryOption {
    @SerializedName("Price")
    private double price;
    @SerializedName("DeliveryDate")
    private String deliveryDate;
    @SerializedName("DeliveryId")
    private long deliveryId;
    @SerializedName("Name")
    private String name;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
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
}
