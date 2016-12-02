package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CodeDetail;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsUserMessageUseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildCodeLookUpRequest;

/**
 * Created by alexanderparra on 25/11/16.
 */

public class UserMessageUseCase extends AbsUserMessageUseCase {

    private String language;
    private RapidRepository rapidRepository;
    private String ErrorCodes;
    private ArrayList<String> errorMessages = new ArrayList<>();



    public UserMessageUseCase(String language, String errorCodes) {
        this.language = language;
        rapidRepository = new RapidDataRepository(new RapidDataFactory());
        ErrorCodes = errorCodes;
    }

    @Override
    protected UserMessageResponse buildUseCase() {

        Call<CodeLookupResponse> responseCall = rapidRepository.userMessage(buildCodeLookUpRequest(language,ErrorCodes));
        try {
            Response<CodeLookupResponse> response = responseCall.execute();

            for (CodeDetail codeDetail : response.body().getCodeDetails()) {
                errorMessages.add(codeDetail.getDisplayMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new UserMessageResponse(errorMessages,null);
    }
}
