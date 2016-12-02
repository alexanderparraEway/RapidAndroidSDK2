package sdk.payment.eway.com.rapidandroidsdk.presentation;


import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Response;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.net.RapidRestAdapter;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.EncryptValuesUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.SubmitPaymentUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.AsynUserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.UserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.rxEncryptValuesUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.rxSubmitPaymentUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl.rxUserMessageUseCase;


/**
 * Created by alexanderparra on 9/11/16.
 */

public class RapidAPI {


    public static void setRapidEndpoint(String rapidEndpoint) { RapidRestAdapter.RapidEndpoint =rapidEndpoint; }

    public static void setPublicAPIKey(String publicAPIKey) { RapidRestAdapter.PublicAPIKey =publicAPIKey; }



    public static Response<SubmitPayResponse> submitPayment(Transaction transaction) throws IOException {

        SubmitPaymentUseCase submitPaymentUseCase = new SubmitPaymentUseCase(transaction);

        return submitPaymentUseCase.execute();

    }

    public static Call<SubmitPayResponse> asyncSubmitPayment(Transaction transaction){

        SubmitPaymentUseCase submitPaymentUseCase = new SubmitPaymentUseCase(transaction);

        return submitPaymentUseCase.asyncExecute();
    }

    public static Flowable<SubmitPayResponse> rxSubmitPayment(Transaction transaction){

        rxSubmitPaymentUseCase rxSubmitPayUseCase = new rxSubmitPaymentUseCase(transaction);

        return rxSubmitPayUseCase.rxExecute();

    }

    public static Response<EncryptItemsResponse> encryptValues(ArrayList<NVPair> values) throws IOException {

        EncryptValuesUseCase encryptValuesUseCase = new EncryptValuesUseCase();

        EncryptValuesUseCase.setValues(values);

        return encryptValuesUseCase.execute();


    }

    public static Call<EncryptItemsResponse> asyncEncryptValues(ArrayList<NVPair> values) throws IOException {

        EncryptValuesUseCase encryptValuesUseCase = new EncryptValuesUseCase();

        EncryptValuesUseCase.setValues(values);

        return encryptValuesUseCase.asyncExecute();


    }

    public static Flowable<EncryptItemsResponse> rxEncryptValues(ArrayList<NVPair> values) {

        rxEncryptValuesUseCase encryptValuesUseCase = new rxEncryptValuesUseCase();

        rxEncryptValuesUseCase.setValues(values);

        return encryptValuesUseCase.rxExecute();

    }

    public static UserMessageResponse userMessage(String language, String errorCodes){

        UserMessageUseCase userMessageUseCase = new UserMessageUseCase(language,errorCodes);

        return userMessageUseCase.execute();

    }

    public static AsynUserMessageUseCase.CallbackUserMessage asynUserMessage(String Language, String ErrorCodes, AsynUserMessageUseCase.CallbackUserMessage callbackUserMessage){

        final AsynUserMessageUseCase asynUserMessageUseCase = new AsynUserMessageUseCase(Language,ErrorCodes,callbackUserMessage);

        asynUserMessageUseCase.asyncExecute();

        return callbackUserMessage;

    }

    public static Flowable<UserMessageResponse> rxUserMessage(String language, String ErrorCodes) {

        rxUserMessageUseCase rxuserMessageUseCase = new rxUserMessageUseCase(language,ErrorCodes);

        return rxuserMessageUseCase.rxExecute();
    }

}
