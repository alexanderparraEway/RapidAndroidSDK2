package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import retrofit2.Call;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.UseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildSubmitRequest;

/**
 * Created by alexanderparra on 18/11/16.
 */

public class SubmitPaymentUseCase extends UseCase<SubmitPayResponse> {

    private Transaction transaction;
    private RapidRepository rapidRepository;

    public SubmitPaymentUseCase(Transaction transaction) {
        this.rapidRepository = new RapidDataRepository(new RapidDataFactory());
        this.transaction = transaction;

    }

    @Override
    protected Call<SubmitPayResponse> buildUseCase() {
        return this.rapidRepository.SubmitPayment(buildSubmitRequest(transaction));
    }


}
