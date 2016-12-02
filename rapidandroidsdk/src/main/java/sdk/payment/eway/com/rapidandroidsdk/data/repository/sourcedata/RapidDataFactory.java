package sdk.payment.eway.com.rapidandroidsdk.data.repository.sourcedata;


import javax.inject.Inject;
import javax.inject.Singleton;

import sdk.payment.eway.com.rapidandroidsdk.data.net.RapidRestAdapter;

/**
 * Created by alexanderparra on 9/11/16.
 */
@Singleton
public class RapidDataFactory {

    private RapidNetRepository rapidNetRepository;


    public RapidDataFactory() {

        rapidNetRepository = new RapidNetRepository();

    }

    public RapidNetData createDataSource(){

        return rapidNetRepository;
    }


}
