package sdk.payment.eway.com.rapidandroidsdk.data.entities;



import java.util.ArrayList;

import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;

public class EncryptValuesRequest {
    private String Method;
    private ArrayList<NVPair> Items;

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public ArrayList<NVPair> getItems() {
        return Items;
    }

    public void setItems(ArrayList<NVPair> items) {
        Items = items;
    }
}
