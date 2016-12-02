package sdk.payment.eway.com.rapidandroidsdk.domain.usecases;

import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;

/**
 * Created by alexanderparra on 30/11/16.
 */

public abstract class AbsAsyncUserMessageUseCase {


    protected abstract CallbackUserMessage buildUseCase();

    public CallbackUserMessage asyncExecute(){ return this.buildUseCase(); }


    public interface CallbackUserMessage {

        void onSuccess(UserMessageResponse response);
        void onError(Throwable throwable);
    }

}
