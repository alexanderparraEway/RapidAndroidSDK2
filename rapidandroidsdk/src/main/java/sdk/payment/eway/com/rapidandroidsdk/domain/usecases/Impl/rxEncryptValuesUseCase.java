package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import java.util.ArrayList;

import io.reactivex.Flowable;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.RxUseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildEncryptValues;

/**
 * Created by alexanderparra on 13/11/16.
 */

public class rxEncryptValuesUseCase extends RxUseCase<EncryptItemsResponse> {

    private RapidRepository rapidRepository;
    private static ArrayList<NVPair> values;


    public rxEncryptValuesUseCase(){

        rapidRepository = new RapidDataRepository(new RapidDataFactory());

    }

    public static void setValues(ArrayList<NVPair> values){
        rxEncryptValuesUseCase.values = values;
    }


    @Override
    protected Flowable<EncryptItemsResponse> buildUseCaseObservable() {
        return rapidRepository.rxEmpcryptValues(buildEncryptValues(rxEncryptValuesUseCase.values));
    }
}
