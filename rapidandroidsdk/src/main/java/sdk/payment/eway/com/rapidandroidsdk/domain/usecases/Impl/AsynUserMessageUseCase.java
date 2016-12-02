package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CodeDetail;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsAsyncUserMessageUseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildCodeLookUpRequest;

/**
 * Created by alexanderparra on 22/11/16.
 */

public class AsynUserMessageUseCase extends AbsAsyncUserMessageUseCase {

    private String language;
    private RapidRepository rapidRepository;
    private String ErrorCodes;
    private CodeLookupResponse codeLookupResponse;
    CallbackUserMessage callbackUserMessage;


    public AsynUserMessageUseCase(String language, String ErrorCodes,CallbackUserMessage callbackUserMessage) {

        this.language = language;
        this.ErrorCodes = ErrorCodes;
        rapidRepository = new RapidDataRepository(new RapidDataFactory());
        this.callbackUserMessage = callbackUserMessage;

    }


    @Override
    protected CallbackUserMessage buildUseCase() {

         rapidRepository
                 .userMessage(buildCodeLookUpRequest(language,ErrorCodes))
                 .enqueue(new Callback<CodeLookupResponse>() {
                     @Override
                     public void onResponse(Call<CodeLookupResponse> call, Response<CodeLookupResponse> response) {
                         codeLookupResponse = new CodeLookupResponse();
                         codeLookupResponse = response.body();

                         if(response.isSuccessful()) {
                             ArrayList<String> errorMessages = new ArrayList<>();
                             for (CodeDetail codeDetail : codeLookupResponse.getCodeDetails()) {
                                 errorMessages.add(codeDetail.getDisplayMessage());
                             }
                             callbackUserMessage.onSuccess(new UserMessageResponse(errorMessages,null));
                         }
                         else
                             callbackUserMessage.onError(new Throwable(response.message()));
                     }

                     @Override
                     public void onFailure(Call<CodeLookupResponse> call, Throwable t) {

                         callbackUserMessage.onError(t);

                     }
                 });

         return callbackUserMessage;
    }


}
