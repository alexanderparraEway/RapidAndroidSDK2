package sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata;

import android.util.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptValuesRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.RapidConfigurationException;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.net.RapidRestAdapter;

/**
 * Created by alexanderparra on 10/11/16.
 */

public class RapidNetRepository implements RapidNetData {


    @Override
    public Flowable<SubmitPayResponse> rxSubmitPayment(SubmitPaymentRequest request) {
        try {
            return RapidRestAdapter.rxCallPost()
                    .rxObsSubmitPayment(request)
                    .toFlowable(BackpressureStrategy.BUFFER);
        } catch (RapidConfigurationException e) {
            return exceptionError(e);
        } catch (KeyManagementException e) {
            return exceptionError(e);
        } catch (NoSuchAlgorithmException e) {
            return exceptionError(e);
        }
    }

    @Override
    public Call<SubmitPayResponse> SubmitResponse(SubmitPaymentRequest request) {
        try {
            return RapidRestAdapter.callPost()
                    .submitPayment(request);
        } catch (RapidConfigurationException e) {
            SubmitPayResponse response = null;
            try {
                response = new SubmitPayResponse(e.getErrorCodes(), "", "");
            } catch (RapidConfigurationException e1) {
                e1.printStackTrace();
            }
            try {
                throw new RapidConfigurationException(response.getErrors());
            } catch (RapidConfigurationException e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (KeyManagementException e) {
            SubmitPayResponse response = new SubmitPayResponse(e.getMessage(), "", "");
            try {
                throw new RapidConfigurationException(response.getErrors());
            } catch (RapidConfigurationException e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (NoSuchAlgorithmException e) {
            SubmitPayResponse response = new SubmitPayResponse(e.getMessage(), "", "");
            try {
                throw new RapidConfigurationException(response.getErrors());
            } catch (RapidConfigurationException e1) {
                e1.printStackTrace();

            }
            return null;
        }


    }

    @Override
    public Flowable<EncryptItemsResponse> rxEncryptValues(EncryptValuesRequest request) {
        try {
            return RapidRestAdapter.rxCallPost()
                    .rxObsEncryptValues(request)
                    .toFlowable(BackpressureStrategy.BUFFER);
        } catch (RapidConfigurationException e) {
            return exceptionError(e);
        } catch (KeyManagementException e) {
            return exceptionError(e);
        } catch (NoSuchAlgorithmException e) {
            return exceptionError(e);
        }
    }

    @Override
    public Call<EncryptItemsResponse> EncryptValues(EncryptValuesRequest request) {
        try {
            return RapidRestAdapter.callPost().encryptValues(request);
        } catch (RapidConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Flowable<CodeLookupResponse> rxUserMessage(CodeLookupRequest request) {
        try {
            return RapidRestAdapter.rxCallPost()
                    .rxCodeLookUp(request)
                    .toFlowable(BackpressureStrategy.BUFFER);
        } catch (RapidConfigurationException e) {
            return exceptionError(e);
        } catch (KeyManagementException e) {
            return exceptionError(e);
        } catch (NoSuchAlgorithmException e) {
            return exceptionError(e);
        }
    }

    @Override
    public Call<CodeLookupResponse> UserMessage(CodeLookupRequest request) {
        try {
            return RapidRestAdapter.callPost().codeLookUp(request);
        } catch (RapidConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    private <T> Flowable<T> exceptionError(final Exception exception){

        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                e.onError(exception);
            }
        },BackpressureStrategy.ERROR);

    }
}
