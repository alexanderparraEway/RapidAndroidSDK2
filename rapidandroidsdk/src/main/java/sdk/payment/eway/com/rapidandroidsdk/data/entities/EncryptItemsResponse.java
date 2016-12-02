package sdk.payment.eway.com.rapidandroidsdk.data.entities;



import java.util.ArrayList;

import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;

/**
 * Created by alexander.parra on 23/10/2015.
 */
public class EncryptItemsResponse {
    private String Method;
    private ArrayList<NVPair>Items;
    private String Errors;

    public EncryptItemsResponse(String errors, ArrayList<NVPair> values, String method) {
        Errors = errors;
        Items = values;
        Method = method;
    }

    public EncryptItemsResponse(){}

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

    public String getErrors() {
        return Errors;
    }

    public void setErrors(String errors) {
        Errors = errors;
    }
}
