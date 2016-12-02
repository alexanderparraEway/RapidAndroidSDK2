package sdk.payment.eway.com.rapidandroidsdk.data.util;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Utils;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptValuesRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;

/**
 * Created by alexanderparra on 13/11/16.
 */

public class RequestBuilder {

    private static final String PROCESSPAYMENT = "ProcessPayment";
    private static final String ECRYPT = "eCrypt";

    public static SubmitPaymentRequest buildSubmitRequest (Transaction transaction) {

        SubmitPaymentRequest request = new SubmitPaymentRequest();
        request.setMethod(PROCESSPAYMENT);
        request.setCustomer(Utils.transformCustomer(transaction.getCustomer()));
        request.setShippingAddress(Utils.transformShippingAddress(transaction.getShippingDetails()));
        request.setShippingMethod(Utils.nullSafeGetShippingMethod(transaction));
        request.setItems(transaction.getLineItems());
        request.setOptions(Utils.transformOptions(transaction.getOptions()));
        request.setPayment(transaction.getPayment());
        request.setDeviceID(UUID.randomUUID().toString()); //The method Utils.getUniquePsuedoID is having performance issues
        request.setPartnerID(transaction.getPartnerID());
        request.setTransactionType(Utils.nullSafeGetTransactionType(transaction));

        return request;
    }

    public static EncryptValuesRequest buildEncryptValues(ArrayList<NVPair> Values){

        EncryptValuesRequest request = new EncryptValuesRequest();
        request.setMethod(ECRYPT);
        request.setItems(Values);
        return request;
    }

    public static CodeLookupRequest buildCodeLookUpRequest(String language, String ErrorCodes){

        return new CodeLookupRequest(nullSafeGetLocale(language),parseErrorCodeList(ErrorCodes));

    }

    private static String nullSafeGetLocale(String Language) {
        return (Language != null) ? Language : Locale.getDefault().toString();
    }

    private static ArrayList<String> parseErrorCodeList(String ErrorCodes) {
        return (ErrorCodes != null) ? Utils.newArrayList(ErrorCodes.trim().replaceAll("\\s+", "").split(",")) : Utils.<String>newArrayList();
    }



}
