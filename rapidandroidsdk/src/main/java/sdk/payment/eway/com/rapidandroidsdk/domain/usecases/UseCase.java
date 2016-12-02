package sdk.payment.eway.com.rapidandroidsdk.domain.usecases;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;

/**
 * Created by alexanderparra on 17/11/16.
 */

public abstract class UseCase<T> {


    private static ArrayList<NVPair> values;

    protected abstract Call<T> buildUseCase();


    public Response<T> execute() throws IOException {

        return this.buildUseCase().execute();

    }

    public Call<T> asyncExecute(){

        return this.buildUseCase();
    }

}
