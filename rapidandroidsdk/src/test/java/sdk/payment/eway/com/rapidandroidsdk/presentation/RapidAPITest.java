package sdk.payment.eway.com.rapidandroidsdk.presentation;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CardDetails;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Customer;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Payment;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.TransactionType;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.RapidConfigurationException;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.net.RapidRestApi;
import sdk.payment.eway.com.rapidandroidsdk.data.net.socket.OkhttpBuilderForTLS;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static sdk.payment.eway.com.rapidandroidsdk.data.net.RapidRestAdapter.PublicAPIKey;
import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildEncryptValues;
import static sdk.payment.eway.com.rapidandroidsdk.data.util.RequestBuilder.buildSubmitRequest;


/**
 * Created by alexanderparra on 30/11/16.
 */

public class RapidAPITest {


    private final String CARD_NUMBER = "4444333322221111";
    private final String CARD_CVN = "123";
    private final String expMonth = "11";
    private final String expYear = "2025";

    private final String CARD_VALUE = "eCrypted:Xm2h1rXvIQ6xYyHUuGyHunTeZWD2aF0t0Mij5eyA9OVKYmFcCv9HAgM5BMTuSQXdoAXJZaD6qTIJdVcQxgShhyUAx+3BEo+DcMjbQkBrMFNITxZHBwCC8NF/jwCV446RPGCJGFvj3LyNKlSu4XR4KwuLhCKo8zPbv5W3lMFqDEmG8S7MqndKR18QOfZBYYIojuL687RYUNWjYawWDIYtOSm43CgoKkNBZ/jJuDPQuOZn0l/L5LOZ+J4DzP9GbOUXgSvQvKgpX9SiYelBwxsmLNqbQ0Cbfw4X9GZimDJ7a/YlITSae/C/21gX6i/GUKI7sELrZ+Ws4DHyeAqQ/XJM+g==";
    private final String CVN_VALUE = "eCrypted:LpIp5ApX5467eS+rX5tIc3Wq69zrjwUbfW44ydQMsjoI+zYlE0jg2aerr4bwRdbS+29lZ4fZCgMiSdy2CvnCd1bDQwBkhe4xkabYLGrP6m0WTtAUMR/aN5Gf0kJ6miO86ouQbB6fPBIZSPYOLc14t2u0CNF2NDEK4mZGcOTWFxhecyNCkbJeE92EuXRNMjMDD1/3zgW8GEsbiV8Eok0urFY2HpBhrBeP6F+hDjN5h/GGYEsCfID6vrlDtRXpCHFzdCQvlbS7PMdiBihDqrfZ5q+Hsv6TQDkOhoVCRwv1w7ErQLC1d7ctHxniImZBLyFMki2aT8w3yLhU5zCBw0dOsg==";
    private final String CARD_NAME = "Alexander";
    private final String ENCRYPT_JSON = "{\"Method\":1,\"Items\":[{\"Name\":\"Card\",\"Value\":\"eCrypted:C2a+L7PyyTcesSWsQDK4A8ygl2d14jJQn91HAN/pNV0RG7ZS7I0s9xf0gpUVdHboiVKaFVy42MnEM4safpUF+y6p6aYQ46vbbT3PdpaAH9amwnNL2tVy9rQntowxQXin57zJPAfKZM9vzUMTNW+AB7xkeRU1b/9s+2t1JI3bf+IcjhOxHv7qm+e60b3PKA88eaSb6EFi2MQPggFIxQtV8pEy4GUyZs48CQFvkK9SjCzvulONjTjLtAY1CCJR2G29xmiCed9Y7qMKI4x4gIOnzViu0z20+fco0l+NCNwZCg6NP02togKcm4pzl86DHntOrfPVpKWAv79j8J2taA5zZQ==\"},{\"Name\":\"CVN\",\"Value\":\"eCrypted:NrEiT8c9E8Ungp+/h3lel05SVZFbLkQ3k95shKQ6s2FxGSb8lE/7+sCKoIIbEZU3SIKd8WI91SJCPjTtB0N+6v5+/jSldBdeXyh6P+xdC4N4q3FENEM/4QFZ/P3mOx91jeLDgsvjB9EVABbfxJFk7lJacZhcI6tMB858FVLrzpxsmeUNIU5HJXewT2aldqi9e8x/+wwG/BfapFvtlmxeKdOUCIi02sr2IeA/G97bGK9RF9Xg4mb2hztlAl32eoTW5Q8x/5g6iceIxwW8plk6G/4iD9910OLi4pHpEgn07AjOBbAVZnROq4BZ4atf3h9uCL5IeC6WJkEOZJLdsHMRFA==\"}],\"Errors\":null}";
    private final String SUBMIT_PAYMENT_JASON ="{\"AccessCode\":\"60CF3CDqh-67UEaeyP7vjKlLhj69n8avcp5g_lr9WYEPi6o5XgJhTjCULJG3P494-4YxcpWU6iwqGi0L-PBTmQBu_xI1w7x6GeUI_juKqqZIp1qYqutd4mii4KqwgxYdjV9Lq\",\"Status\":\"Accepted\",\"Errors\":null}";
    private static final String VERSIONREPORTED = "1.2.2";


    @Test
    public void encryptValuesSuccessfulResponseTest() throws IOException, InterruptedException, RapidConfigurationException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", CARD_NUMBER));
        values.add(new NVPair("CVN", CARD_CVN));

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.enqueue(new MockResponse().setBody(ENCRYPT_JSON));


        RapidRestApi rapidRestApi = initRestAPI(mockWebServer);

        Call<EncryptItemsResponse> call = rapidRestApi.encryptValues(buildEncryptValues(values));
        Response<EncryptItemsResponse> response = call.execute();

        assertNotNull(response);
        assertThat(response.body().getItems().size(),greaterThan(0));

        //Testing paths
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/encrypt",recordedRequest.getPath());

        //Testing headers
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertEquals(";eWAY SDK Android " + VERSIONREPORTED, recordedRequest.getHeader("User-Agent"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));

        mockWebServer.shutdown();



    }
    @Test
    public void asyncEncryptValuesSuccessfulResponseTest() throws IOException, InterruptedException, RapidConfigurationException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", CARD_NUMBER));
        values.add(new NVPair("CVN", CARD_CVN));

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.enqueue(new MockResponse().setBody(ENCRYPT_JSON));


        RapidRestApi rapidRestApi = initRestAPI(mockWebServer);

                rapidRestApi.encryptValues(buildEncryptValues(values))
                .enqueue(new Callback<EncryptItemsResponse>() {
                    @Override
                    public void onResponse(Call<EncryptItemsResponse> call, Response<EncryptItemsResponse> response) {
                        assertNotNull(response);
                        assertThat(response.body().getItems().size(),greaterThan(0));
                    }

                    @Override
                    public void onFailure(Call<EncryptItemsResponse> call, Throwable t) {

                    }
                });

        //Testing paths
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/encrypt",recordedRequest.getPath());


        //Testing headers
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertEquals(";eWAY SDK Android " + VERSIONREPORTED, recordedRequest.getHeader("User-Agent"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));

        mockWebServer.shutdown();

    }

    @Test
    public void rxEncryptValuesSuccessfulResponseTest() throws IOException, InterruptedException, RapidConfigurationException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", CARD_NUMBER));
        values.add(new NVPair("CVN", CARD_CVN));

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.enqueue(new MockResponse().setBody(ENCRYPT_JSON));


        RapidRestApi rapidRestApi = initRxRestAPI(mockWebServer);

        rapidRestApi.rxObsEncryptValues(buildEncryptValues(values)).toFlowable(BackpressureStrategy.BUFFER)
                .subscribeWith(new Subscriber<EncryptItemsResponse>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(EncryptItemsResponse response) {
                        assertNotNull(response);
                        assertThat(response.getItems().size(),greaterThan(0));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //Testing paths
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/encrypt",recordedRequest.getPath());


        //Testing headers
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertEquals(";eWAY SDK Android " + VERSIONREPORTED, recordedRequest.getHeader("User-Agent"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));

        mockWebServer.shutdown();

    }

    @Test
    public void submitPaymentSuccessfulResponseTest() throws IOException, RapidConfigurationException, InterruptedException {

        CardDetails cardDetails = new CardDetails();
        Customer customer = new Customer();
        Transaction transaction = new Transaction();
        int totalAmount = 100;
        Payment payment = new Payment();
        payment.setTotalAmount(totalAmount);


        cardDetails.setNumber(CARD_VALUE);
        cardDetails.setName(CARD_NAME);
        cardDetails.setCVN(CVN_VALUE);
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        mockWebServer.enqueue(new MockResponse().setBody(SUBMIT_PAYMENT_JASON));

        RapidRestApi rapidRestApi = initRestAPI(mockWebServer);

        Call<SubmitPayResponse> callPayment = rapidRestApi.submitPayment(buildSubmitRequest(transaction));
        Response<SubmitPayResponse> submitPaymentResponse = callPayment.execute();

        Assert.assertNotNull(submitPaymentResponse.body().getAccessCode());

        //Testing paths
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/payment",recordedRequest.getPath());

        //Testing headers
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertEquals(";eWAY SDK Android " + VERSIONREPORTED, recordedRequest.getHeader("User-Agent"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));

        mockWebServer.shutdown();
    }

    @Test
    public void asyncSubmitPaymentSuccessfulResponseTest() throws IOException, RapidConfigurationException, InterruptedException {

        CardDetails cardDetails = new CardDetails();
        Customer customer = new Customer();
        Transaction transaction = new Transaction();
        int totalAmount = 100;
        Payment payment = new Payment();
        payment.setTotalAmount(totalAmount);


        cardDetails.setNumber(CARD_VALUE);
        cardDetails.setName(CARD_NAME);
        cardDetails.setCVN(CVN_VALUE);
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        mockWebServer.enqueue(new MockResponse().setBody(SUBMIT_PAYMENT_JASON));

        RapidRestApi rapidRestApi = initRestAPI(mockWebServer);

        rapidRestApi.submitPayment(buildSubmitRequest(transaction))
                .enqueue(new Callback<SubmitPayResponse>() {
                    @Override
                    public void onResponse(Call<SubmitPayResponse> call, Response<SubmitPayResponse> response) {
                        Assert.assertNotNull(response.body().getAccessCode());
                    }

                    @Override
                    public void onFailure(Call<SubmitPayResponse> call, Throwable t) {

                    }
                });

        //Testing paths
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/payment", recordedRequest.getPath());

        //Testing headers
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertEquals(";eWAY SDK Android " + VERSIONREPORTED, recordedRequest.getHeader("User-Agent"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));

        mockWebServer.shutdown();
    }


    @Test
    public void rxSubmitPaymentSuccessfulResponseTest() throws IOException, RapidConfigurationException, InterruptedException {

        CardDetails cardDetails = new CardDetails();
        Customer customer = new Customer();
        Transaction transaction = new Transaction();
        int totalAmount = 100;
        Payment payment = new Payment();
        payment.setTotalAmount(totalAmount);


        cardDetails.setNumber(CARD_VALUE);
        cardDetails.setName(CARD_NAME);
        cardDetails.setCVN(CVN_VALUE);
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        mockWebServer.enqueue(new MockResponse().setBody(SUBMIT_PAYMENT_JASON));

        RapidRestApi rapidRestApi = initRxRestAPI(mockWebServer);

        rapidRestApi.rxObsSubmitPayment(buildSubmitRequest(transaction)).toFlowable(BackpressureStrategy.BUFFER)
                .subscribeWith(new Subscriber<SubmitPayResponse>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(SubmitPayResponse submitPayResponse) {
                        Assert.assertNotNull(submitPayResponse.getAccessCode());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //Testing paths
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/payment", recordedRequest.getPath());

        //Testing headers
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertEquals(";eWAY SDK Android " + VERSIONREPORTED, recordedRequest.getHeader("User-Agent"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));

        mockWebServer.shutdown();
    }







    private RapidRestApi initRestAPI(MockWebServer mockWebServer) throws RapidConfigurationException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getEwayClient())
                .build();


        return retrofit.create(RapidRestApi.class);

    }

    private RapidRestApi initRxRestAPI(MockWebServer mockWebServer) throws RapidConfigurationException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getEwayClient())
                .build();


        return retrofit.create(RapidRestApi.class);

    }

    private OkHttpClient getEwayClient() throws RapidConfigurationException {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest  = chain.request();
                        String credential = Credentials.basic(PublicAPIKey, "");
                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .header("Authorization", credential)
                                .header("User-Agent",";eWAY SDK Android " + VERSIONREPORTED)
                                .method(originalRequest.method(),originalRequest.body());
                        Request request = requestBuilder.build();
                        return  chain.proceed(request);
                    }
                }).addInterceptor(loggingInterceptor);

        return OkhttpBuilderForTLS.enableTls12OnPreLollipop(okHttpClient).build();


    }




}
