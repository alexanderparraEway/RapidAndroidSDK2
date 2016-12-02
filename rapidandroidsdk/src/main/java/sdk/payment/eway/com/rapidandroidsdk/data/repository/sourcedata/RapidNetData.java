package sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata;

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
 * Created by alexanderparra on 10/11/16.
 */

public interface RapidNetData {

    Flowable<SubmitPayResponse> rxSubmitPayment(SubmitPaymentRequest request);

    Call<SubmitPayResponse> SubmitResponse(SubmitPaymentRequest request);

    Flowable<EncryptItemsResponse> rxEncryptValues(EncryptValuesRequest request);

    Call<EncryptItemsResponse> EncryptValues(EncryptValuesRequest request);

    Flowable<CodeLookupResponse> rxUserMessage(CodeLookupRequest request);

    Call<CodeLookupResponse> UserMessage(CodeLookupRequest request);

}
