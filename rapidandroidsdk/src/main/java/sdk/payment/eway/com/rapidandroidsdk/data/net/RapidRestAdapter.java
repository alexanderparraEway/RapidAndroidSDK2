package sdk.payment.eway.com.rapidandroidsdk.data.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;


import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.RapidConfigurationException;
import sdk.payment.eway.com.rapidandroidsdk.data.net.socket.OkhttpBuilderForTLS;

/**
 * Created by alexanderparra on 9/11/16.
 */

public class RapidRestAdapter {

    public static String RapidEndpoint;
    public static String PublicAPIKey;
    private static final String PRODUCTION = "production";
    private static final String PRODUCTION_URL = "https://api.ewaypayments.com";
    private static final String SANDBOX = "sandbox";
    private static final String SANDBOX_URL = "https://api.sandbox.ewaypayments.com";
    private static final String DEVELOPMENT = "dev";
    private static final String DEV_URL= " http://dev.eway.com.au";
    private static final String VERSIONREPORTED = "1.2.2";



    // Adapter Asynchronous and Synchronous call post for  Encryption
    public static RapidRestApi callPost() throws RapidConfigurationException, KeyManagementException, NoSuchAlgorithmException {
        //Restful call
        String baseUrl = filterUrl(RapidEndpoint);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getEwayClient())
                .build();
        RapidRestApi adapter = retrofit.create(RapidRestApi.class);
        return adapter;
    }

    //Adapter for Asynchronous/Synchronous call rxjava
    public static RapidRestApi rxCallPost() throws RapidConfigurationException, KeyManagementException, NoSuchAlgorithmException {
        //Restful call
        String baseUrl = filterUrl(RapidEndpoint);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getEwayClient())
                .build();

        RapidRestApi adapter = retrofit.create(RapidRestApi.class);

        return adapter;
    }

    private static OkHttpClient getEwayClient() throws RapidConfigurationException, NoSuchAlgorithmException, KeyManagementException {

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

    private static String filterUrl(String url) throws RapidConfigurationException {
        errorCheck();
        String baseUrl = url;
        if (baseUrl.contains(PRODUCTION)) {
            baseUrl = PRODUCTION_URL;
        } else if (baseUrl.contains(SANDBOX)) {
            baseUrl = SANDBOX_URL;
        } else if(baseUrl.contains(DEVELOPMENT)){
            baseUrl = DEV_URL;
        }
        return baseUrl;
    }

    private static void errorCheck() throws RapidConfigurationException{
        if (PublicAPIKey == null || PublicAPIKey.isEmpty()) {
            throw new RapidConfigurationException("S9991");
        }
        if (RapidEndpoint == null || RapidEndpoint.isEmpty()) {
            throw new RapidConfigurationException("S9990");
        }

        String urlPattern="^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        if(!RapidEndpoint.matches(urlPattern))
            throw new RapidConfigurationException("S9992");
    }


}
