package sdk.payment.eway.com.rapidandroidsdk.data.net;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptValuesRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;

/**
 * Created by alexanderparra on 9/11/16.
 */

public interface RapidRestApi {

    // Asynchronous or synchronous api
    @Headers("Content-Type: application/json")
    @POST("/payment")
    Call<SubmitPayResponse> submitPayment(@Body SubmitPaymentRequest request);

    @Headers("Content-Type: application/json")
    @POST("/payment")
    Observable<SubmitPayResponse> rxObsSubmitPayment(@Body SubmitPaymentRequest request);

    @Headers("Content-Type: application/json")
    @POST("/encrypt")
    Call<EncryptItemsResponse> encryptValues(@Body EncryptValuesRequest request);

    @Headers("Content-Type: application/json")
    @POST("/encrypt")
    Observable<EncryptItemsResponse> rxObsEncryptValues(@Body EncryptValuesRequest request);

    @Headers("Content-Type: application/json")
    @POST("/codelookup")
    Call<CodeLookupResponse> codeLookUp(@Body CodeLookupRequest request);

    @Headers("Content-Type: application/json")
    @POST("/codelookup")
    Observable<CodeLookupResponse> rxCodeLookUp(@Body CodeLookupRequest request);

    @Headers("Content-Type: application/json")
    @POST("/aprapid/payment")
    Call<SubmitPayResponse> androidPaySubmitPayment(@Body SubmitPaymentRequest request);
}
