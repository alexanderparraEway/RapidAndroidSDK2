package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import org.reactivestreams.Publisher;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CodeDetail;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.RxUseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildCodeLookUpRequest;

/**
 * Created by alexanderparra on 13/11/16.
 */

public class rxUserMessageUseCase extends RxUseCase<UserMessageResponse> {

    private String language;
    private RapidRepository rapidRepository;
    private String ErrorCodes;

    public rxUserMessageUseCase(String language, String ErrorCodes) {

        this.language = language;
        this.ErrorCodes = ErrorCodes;
        rapidRepository = new RapidDataRepository(new RapidDataFactory());

    }

    @Override
    protected Flowable<UserMessageResponse> buildUseCaseObservable() {

        return rapidRepository.rxUserMessage(buildCodeLookUpRequest(language,ErrorCodes))
                .flatMap(new Function<CodeLookupResponse, Publisher<UserMessageResponse>>() {
                    @Override
                    public Publisher<UserMessageResponse> apply(final CodeLookupResponse codeLookupResponse) throws Exception {
                        return Flowable.create(new FlowableOnSubscribe<UserMessageResponse>() {
                            @Override
                            public void subscribe(FlowableEmitter<UserMessageResponse> e) throws Exception {
                                ArrayList<String> errorMessages = new ArrayList<>();
                                for (CodeDetail codeDetail : codeLookupResponse.getCodeDetails()) {
                                    errorMessages.add(codeDetail.getDisplayMessage());
                                }
                                e.onNext(new UserMessageResponse(errorMessages,null));
                            }
                        }, BackpressureStrategy.BUFFER);
                    }
                });

    }

}
