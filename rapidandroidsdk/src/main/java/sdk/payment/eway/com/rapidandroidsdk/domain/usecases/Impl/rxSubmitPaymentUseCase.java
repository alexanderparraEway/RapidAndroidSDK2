package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import io.reactivex.Flowable;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.RxUseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildSubmitRequest;


/**
 * Created by alexanderparra on 13/11/16.
 */

public class rxSubmitPaymentUseCase extends RxUseCase<SubmitPayResponse> {

    private Transaction transaction;
    private RapidRepository rapidRepository;


    public rxSubmitPaymentUseCase(Transaction transaction) {
        rapidRepository = new RapidDataRepository(new RapidDataFactory());
        this.transaction = transaction;
    }

    @Override
    protected Flowable<SubmitPayResponse> buildUseCaseObservable() {
        return rapidRepository.rxSubmitPayment(buildSubmitRequest(transaction));
    }


}
