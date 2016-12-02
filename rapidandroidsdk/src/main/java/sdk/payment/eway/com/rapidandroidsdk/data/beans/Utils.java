package sdk.payment.eway.com.rapidandroidsdk.data.beans;

import android.os.Build;
import android.util.Log;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import sdk.payment.eway.com.rapidandroidsdk.data.entities.RapidConfigurationException;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;

public class Utils {

    public static <T> ArrayList<T> newArrayList(T... tValues) {
        ArrayList<T> result = new ArrayList<>();
        for (T value : tValues) {
            result.add(value);
        }
        return result;
    }

    public static <T> String join(String delimiter, Collection<T> tValues) throws RapidConfigurationException {
        String string = "";
        for (T value : tValues) {
            string += value + " " + delimiter;
        }
        return string.length() > 0 ? string.substring(0, string.length() - 1 - delimiter.length()) : string;
    }


    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    public static String nullSafeGetShippingMethod(Transaction transaction) {
        if (transaction != null && transaction.getShippingDetails() != null && transaction.getShippingDetails().getShippingMethod() != null)
            return transaction.getShippingDetails().getShippingMethod().toString();
        else
            return null;
    }

    public static String nullSafeGetTransactionType(Transaction transaction) {
        return transaction.getTransactionType() != null ? transaction.getTransactionType().toString() : null;
    }

    public static List<SubmitPaymentRequest.Options> transformOptions(ArrayList<String> source) {
        if(source!=null){
            ArrayList<SubmitPaymentRequest.Options> target = new ArrayList<SubmitPaymentRequest.Options>();
            for(String value:source){
                target.add(new SubmitPaymentRequest.Options(value));
            }
            return target;
        }else
        return null;
    }

    public static SubmitPaymentRequest.ShippingAddress transformShippingAddress(ShippingDetails source) {
        if (source != null) {
            SubmitPaymentRequest.ShippingAddress target = new SubmitPaymentRequest.ShippingAddress();
            target.setFirstName(source.getFirstName());
            target.setLastName(source.getLastName());
            transferAddressData(source.getShippingAddress(),target);
            target.setPhone(source.getPhone());
            return target;
        } else {
            return null;
        }
    }

    public static SubmitPaymentRequest.Customer transformCustomer(Customer source) {
        if (source != null) {
            SubmitPaymentRequest.Customer target = new SubmitPaymentRequest.Customer();
            target.setTitle(source.getTitle());
            target.setFirstName(source.getFirstName());
            target.setLastName(source.getLastName());
            target.setCompanyName(source.getCompanyName());
            target.setJobDescription(source.getJobDescription());
            transferAddressData(source.getAddress(), target);
            target.setTokenCustomerID(source.getTokenCustomerID());
            target.setEmail(source.getEmail());
            target.setPhone(source.getPhone());
            target.setMobile(source.getMobile());
            target.setComments(source.getComments());
            target.setFax(source.getFax());
            target.setUrl(source.getUrl());
            target.setCardDetails(transformCardDetails(source.getCardDetails()));
            return target;
        } else {
            return null;
        }
    }

    private static SubmitPaymentRequest.CardDetails transformCardDetails(CardDetails source) {
        if (source != null) {
            SubmitPaymentRequest.CardDetails target = new SubmitPaymentRequest.CardDetails();
            target.setName(source.getName());
            target.setNumber(source.getNumber());
            target.setCVN(source.getCVN());
            target.setExpiryMonth(source.getExpiryMonth());
            target.setExpiryYear(source.getExpiryYear());
            target.setStartMonth(source.getStartMonth());
            target.setStartYear(source.getStartYear());
            return target;
        } else
            return null;
    }

    private static void transferAddressData(Address source, SubmitPaymentRequest.Addressed target) {
        if(source!=null){
            target.setStreet1(source.getStreet1());
            target.setStreet2(source.getStreet2());
            target.setCity(source.getCity());
            target.setState(source.getState());
            target.setPostalCode(source.getPostalCode());
            target.setCountry(source.getCountry());
        }
    }

    static String putHeaderToAndroidPay(String jsonFormatted){

        Moshi moshi = new Moshi.Builder().build();
        AndroidPayID androidPayID = null;
        JsonAdapter<AndroidPayID> adapter = moshi.adapter(AndroidPayID.class);
        try {
            androidPayID = adapter.fromJson(jsonFormatted);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonFormatted = adapter.toJson(androidPayID);
        Log.d("SecuredData","AndroidPayID:"+jsonFormatted);

        return "AndroidPayID:"+jsonFormatted;
    }


}
