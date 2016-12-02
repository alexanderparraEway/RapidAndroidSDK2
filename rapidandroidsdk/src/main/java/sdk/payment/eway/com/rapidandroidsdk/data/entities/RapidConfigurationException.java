package sdk.payment.eway.com.rapidandroidsdk.data.entities;

import java.util.ArrayList;

import sdk.payment.eway.com.rapidandroidsdk.data.beans.Utils;

public class RapidConfigurationException extends Exception {
    private ArrayList<String> ErrorCodes;

    public RapidConfigurationException(String... errorCodes) {
        ErrorCodes = Utils.newArrayList(errorCodes);
    }

    public RapidConfigurationException(ArrayList<String> errorCodes) {
        ErrorCodes = errorCodes;
    }

    public String getErrorCodes() throws RapidConfigurationException {
        return Utils.join(",", ErrorCodes);
    }
}
