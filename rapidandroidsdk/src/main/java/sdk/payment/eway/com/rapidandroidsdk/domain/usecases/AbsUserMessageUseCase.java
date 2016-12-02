package sdk.payment.eway.com.rapidandroidsdk.domain.usecases;

import retrofit2.Call;
import retrofit2.Response;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;

/**
 * Created by alexanderparra on 23/11/16.
 */

public abstract class AbsUserMessageUseCase {


    protected abstract UserMessageResponse buildUseCase();

    public UserMessageResponse execute(){ return this.buildUseCase(); }



}
