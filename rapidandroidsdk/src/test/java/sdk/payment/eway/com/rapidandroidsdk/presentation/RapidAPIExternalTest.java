package sdk.payment.eway.com.rapidandroidsdk.presentation;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CardDetails;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Customer;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Payment;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.TransactionType;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.net.Enviroment.RapidEnviroment;
import sdk.payment.eway.com.rapidandroidsdk.data.net.RapidRestAdapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by alexanderparra on 1/12/16.
 */

public class RapidAPIExternalTest {

    private final String PRODUCTION_URL = "https://api.ewaypayments.com";
    private final String SANDBOX_URL = "https://api.sandbox.ewaypayments.com";
    private final String DEV_URL= " http://dev.eway.com.au";
    private final String CARD_NUMBER = "4444333322221111";
    private final String CARD_CVN = "123";
    private final String expMonth = "11";
    private final String expYear = "2025";
    private final String CARD_NAME = "Alexander";
    private final String KEY = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";


    @Test
    public void setProductionRapidEndPointTest(){

        RapidAPI.setRapidEndpoint(RapidEnviroment.PRODUCTION_URL);
        Assert.assertEquals(RapidRestAdapter.RapidEndpoint,PRODUCTION_URL);

    }

    @Test
    public void setSandBoxRapidEndPointTest(){

        RapidAPI.setRapidEndpoint(RapidEnviroment.SANDBOX_URL);
        Assert.assertEquals(RapidRestAdapter.RapidEndpoint,SANDBOX_URL);
    }
    @Test
    public void setDevRapidEndpointsTest(){

        RapidAPI.setRapidEndpoint(RapidEnviroment.DEV_URL);
        Assert.assertEquals(RapidRestAdapter.RapidEndpoint,DEV_URL);

    }

    @Test
    public void encryptValuesSuccessfulResponseTest() throws IOException {

        RapidAPI.setPublicAPIKey(KEY);
        RapidAPI.setRapidEndpoint(RapidEnviroment.SANDBOX_URL);

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", CARD_NUMBER));
        values.add(new NVPair("CVN", CARD_CVN));

        EncryptItemsResponse response = RapidAPI.encryptValues(values).body();

        Assert.assertNotNull(response);
        assertThat(response.getItems().size(),greaterThan(0));

    }

    @Test
    public void asyncEncryptValuesSuccessfulResponseTest() throws IOException {

        RapidAPI.setPublicAPIKey(KEY);
        RapidAPI.setRapidEndpoint(RapidEnviroment.SANDBOX_URL);

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", CARD_NUMBER));
        values.add(new NVPair("CVN", CARD_CVN));


        RapidAPI.asyncEncryptValues(values).enqueue(new Callback<EncryptItemsResponse>() {
            @Override
            public void onResponse(Call<EncryptItemsResponse> call, Response<EncryptItemsResponse> response) {
                Assert.assertNotNull(response);
                assertThat(response.body().getItems().size(),greaterThan(0));
            }

            @Override
            public void onFailure(Call<EncryptItemsResponse> call, Throwable t) {

            }
        });

    }


    @Test
    public void submitPaymentTestSuccessfulResponseTest() throws IOException {


        RapidAPI.setPublicAPIKey(KEY);
        RapidAPI.setRapidEndpoint(RapidEnviroment.SANDBOX_URL);

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", CARD_NUMBER));
        values.add(new NVPair("CVN", CARD_CVN));

        EncryptItemsResponse response = RapidAPI.encryptValues(values).body();


        CardDetails cardDetails = new CardDetails();
        Customer customer = new Customer();
        final Transaction transaction = new Transaction();
        TransactionType transType = TransactionType.Purchase;
        int totalAmount = 100;
        Payment payment = new Payment();
        payment.setTotalAmount(totalAmount);


        cardDetails.setNumber(response.getItems().get(0).getValue());
        cardDetails.setName(CARD_NAME);
        cardDetails.setCVN(response.getItems().get(1).getValue());
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(transType);
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        SubmitPayResponse submitPayResponse = RapidAPI.submitPayment(transaction).body();

        Assert.assertNotNull(submitPayResponse.getAccessCode());
        assertThat(response.getItems().size(),greaterThan(0));


    }






}
