package sdk.payment.eway.com.rapidandroidsdk.domain.repository;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptValuesRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;

/**
 * Created by alexanderparra on 9/11/16.
 */

public interface RapidRepository {

    Call<SubmitPayResponse> SubmitPayment(SubmitPaymentRequest request);

    Flowable<SubmitPayResponse> rxSubmitPayment(SubmitPaymentRequest request);

    Flowable<EncryptItemsResponse> rxEmpcryptValues(EncryptValuesRequest request);

    Call<EncryptItemsResponse> EmpcryptValues(EncryptValuesRequest request);

    Flowable<CodeLookupResponse> rxUserMessage(CodeLookupRequest request);

    Call<CodeLookupResponse> userMessage(CodeLookupRequest request);

}
