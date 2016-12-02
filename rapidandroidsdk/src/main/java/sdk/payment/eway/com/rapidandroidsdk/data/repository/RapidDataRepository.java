package sdk.payment.eway.com.rapidandroidsdk.data.repository;



import io.reactivex.Flowable;
import retrofit2.Call;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptValuesRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;

/**
 * Created by alexanderparra on 9/11/16.
 */

public class RapidDataRepository implements RapidRepository {

    private RapidDataFactory rapidDataFactory;


    public RapidDataRepository(RapidDataFactory rapidDataFactory) {
        this.rapidDataFactory = rapidDataFactory;
    }

    @Override
    public Call<SubmitPayResponse>SubmitPayment(SubmitPaymentRequest request) {
        return this.rapidDataFactory.createDataSource().SubmitResponse(request);
    }


    @Override
    public Flowable<SubmitPayResponse> rxSubmitPayment(SubmitPaymentRequest request) {
        return this.rapidDataFactory.createDataSource().rxSubmitPayment(request);
    }

    @Override
    public Flowable<EncryptItemsResponse> rxEmpcryptValues(EncryptValuesRequest request) {
        return this.rapidDataFactory.createDataSource().rxEncryptValues(request);
    }

    @Override
    public Call<EncryptItemsResponse> EmpcryptValues(EncryptValuesRequest request) {
        return this.rapidDataFactory.createDataSource().EncryptValues(request);
    }

    @Override
    public Flowable<CodeLookupResponse> rxUserMessage(CodeLookupRequest request) {
        return this.rapidDataFactory.createDataSource().rxUserMessage(request);
    }

    @Override
    public Call<CodeLookupResponse> userMessage(CodeLookupRequest request) {
        return this.rapidDataFactory.createDataSource().UserMessage(request);
    }

}
