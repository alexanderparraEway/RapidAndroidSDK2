package sdk.payment.eway.com.rapidandroidsdk2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CardDetails;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Customer;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Payment;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.TransactionType;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.RapidConfigurationException;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;
import sdk.payment.eway.com.rapidandroidsdk2.databinding.ActivityRxSampleMainBinding;
import sdk.payment.eway.com.rapidandroidsdk2.databinding.ContentRxSampleMainBinding;


public class RxSampleMainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String totalAmount;
    private String cardName;
    private String cardNumber;
    private String cvnNumber;
    private String expMonth;
    private String expYear;
    private String transTypes;
    private CardDetails cardDetails = new CardDetails();
    private Transaction transaction = new Transaction();
    private Payment payment = new Payment();
    private Customer customer = new Customer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityRxSampleMainBinding mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_rx_sample_main);
        setSupportActionBar(mainBinding.toolbar);

        transTypesPopulator(mainBinding.included);
        dateTimePopulator(mainBinding.included);
        RapidAPI.setPublicAPIKey("epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B");
        RapidAPI.setRapidEndpoint("https://api.sandbox.ewaypayments.com/");

        mainBinding.included.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton(mainBinding.included);
            }
        });

        mainBinding.included.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton();
            }
        });

    }


    public void submitButton(ContentRxSampleMainBinding binding) {
        try {
            progressDialog = ProgressDialog.show(RxSampleMainActivity.this, "Processing", "Processing payment", true);
            fetchDataFromForm(binding);
            payment.setTotalAmount(Integer.parseInt(totalAmount));
            cardDetails.setName(cardName);
            fetchEncryptData(cardNumber, cvnNumber);
        } catch (RapidConfigurationException e) {
           e.printStackTrace();
        }

    }


    public void nextButton(){
        Intent intent = new Intent(RxSampleMainActivity.this,RxEncryptionActivity.class);
        startActivity(intent);

    }

    private void fetchEncryptData(String cardNumber, String cvnNumber) throws RapidConfigurationException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));

        RapidAPI.rxEncryptValues(values)
                .subscribeWith(new Subscriber<EncryptItemsResponse>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(EncryptItemsResponse response) {
                        if(response.getErrors() == null)
                            submitPayment(response);
                        else
                            userMessage(new Throwable(response.getErrors()));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void submitPayment(EncryptItemsResponse response){

        cardDetails.setNumber(response.getItems().get(0).getValue());
        cardDetails.setCVN(response.getItems().get(1).getValue());
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.valueOf((transTypes.isEmpty() ? TransactionType.Purchase.toString() : transTypes.toString())));
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        RapidAPI.rxSubmitPayment(transaction)
                .subscribeWith(new Subscriber<SubmitPayResponse>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(SubmitPayResponse value) {
                        if (value.getErrors() == null)
                            alertDialogResponse(value.getAccessCode());
                        else
                            userMessage(new Throwable(value.getErrors()));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void userMessage(Throwable throwable){

        RapidAPI.rxUserMessage(Locale.getDefault().getLanguage(), throwable.getMessage())
                .subscribeWith(new Subscriber<UserMessageResponse>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(UserMessageResponse value) {
                         alertDialogResponse(errorHandler(value.getErrorMessages()));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void transTypesPopulator(ContentRxSampleMainBinding binding) {


        List<String> list = new ArrayList<>();
        for (TransactionType type : TransactionType.values()) {
            list.add(type.name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.transTypes.setAdapter(dataAdapter);

    }

    private void dateTimePopulator(ContentRxSampleMainBinding binding) {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, makeSequence(1, 12));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.expMonth.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeSequence
                (Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) + 10));
        binding.expYear.setAdapter(dataAdapter);
    }

    private List<String> makeSequence(int begin, int end) {
        List<String> ret = new ArrayList(end - begin + 1);

        for (int i = begin; i <= end; ret.add(
                ((end > 12 ? "0000" : "00") + Integer.toString(i)).substring(Integer.toString(i++).length())))
            ;

        return ret;
    }

    private String errorHandler(List<String> response) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String errorMsg : response) {
            result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
            i++;
        }
        return result.toString();
    }
    private void fetchDataFromForm(ContentRxSampleMainBinding binding){

        totalAmount = noNullObjects(binding.totalAmount.getText().toString());
        cardName = noNullObjects(binding.cardName.getText().toString());
        cardNumber = noNullObjects(binding.cardNumber.getText().toString());
        cvnNumber = noNullObjects(binding.cvn.getText().toString());
        expMonth = binding.expMonth.getSelectedItem().toString();
        expYear = binding.expYear.getSelectedItem().toString();
        transTypes = binding.transTypes.getSelectedItem().toString();

    }
    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(RxSampleMainActivity.this)
                .setTitle("Result")
                .setMessage(result)
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
