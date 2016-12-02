package sdk.payment.eway.com.rapidandroidsdk.domain.usecases.Impl;

import java.util.ArrayList;

import retrofit2.Call;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.RapidDataRepository;
import sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata.RapidDataFactory;
import sdk.payment.eway.com.rapidandroidsdk.domain.repository.RapidRepository;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.UseCase;

import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildEncryptValues;

/**
 * Created by alexanderparra on 17/11/16.
 */

public class EncryptValuesUseCase extends UseCase<EncryptItemsResponse> {

    private static ArrayList<NVPair> values;
    private RapidRepository rapidRepository;

    public EncryptValuesUseCase() {

        rapidRepository = new RapidDataRepository(new RapidDataFactory());

    }

    public static void setValues(ArrayList<NVPair> values){
        EncryptValuesUseCase.values = values;
    }

    @Override
    protected Call<EncryptItemsResponse> buildUseCase() {
        return rapidRepository.EmpcryptValues(buildEncryptValues(EncryptValuesUseCase.values));
    }


}
