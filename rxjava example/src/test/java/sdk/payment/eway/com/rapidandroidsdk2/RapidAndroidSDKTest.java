package sdk.payment.eway.com.rapidandroidsdk2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class RapidAndroidSDKTest {

    private String END_POINT_URL = "https://api.sandbox.ewaypayments.com/";
    private String publicKey = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";
    private String cardNumber = "4444333322221111";
    private String cvnNumber = "123";
    final ArrayList<NVPair> values = new ArrayList<>();



    @Test
    public void rxEncryptValues() throws Exception {

        RapidAPI.setPublicAPIKey(publicKey);
        RapidAPI.setRapidEndpoint(END_POINT_URL);
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));

        RapidAPI.rxEncryptValues(values)
                .subscribe(new Consumer<EncryptItemsResponse>() {
                    @Override
                    public void accept(EncryptItemsResponse response) throws Exception {

                        assertThat(response,is(notNullValue()));

                    }
                });

    }
}