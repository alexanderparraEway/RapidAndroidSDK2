package sdk.payment.eway.com.synchronousexample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;


import bolts.Continuation;
import bolts.Task;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.CardDetails;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Customer;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Payment;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.Transaction;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.TransactionType;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPayResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.net.Enviroment.RapidEnviroment;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;
import sdk.payment.eway.com.synchronousexample.databinding.ActivitySampleMainBinding;
import sdk.payment.eway.com.synchronousexample.databinding.ContentSampleMainBinding;

public class SampleMainActivity extends AppCompatActivity {



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

        final ActivitySampleMainBinding mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_sample_main);
        setSupportActionBar(mainBinding.toolbar);

        transTypesPopulator(mainBinding.included);
        dateTimePopulator(mainBinding.included);
        RapidAPI.setPublicAPIKey("epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B");
        RapidAPI.setRapidEndpoint(RapidEnviroment.SANDBOX_URL);

        mainBinding.included.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnSubmitButton(mainBinding.included);
            }
        });

        mainBinding.included.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnNextPageButton();
            }
        });

    }


    private void clickOnSubmitButton(ContentSampleMainBinding binding)  {
        progressDialog = ProgressDialog.show(SampleMainActivity.this,"Processing","Processing payment", true);
        fetchDataFromForm(binding);
        try {
            fetchEncryptData(cardNumber, cvnNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void fetchEncryptData(String cardNumber, String cvnNumber) throws IOException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));

        Task.callInBackground(new Callable<EncryptItemsResponse>() {

            public EncryptItemsResponse call() throws IOException { return RapidAPI.encryptValues(values).body(); }

        }).continueWithTask(new Continuation<EncryptItemsResponse, Task<SubmitPayResponse>>() {
            @Override
            public Task<SubmitPayResponse> then(Task<EncryptItemsResponse> task) throws Exception { return submitTransaction(task.getResult()); }

        },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<SubmitPayResponse, Void>() {
            @Override
            public Void then(Task<SubmitPayResponse> task) throws Exception {

                if(task.getResult().getErrors()== null)
                    alertDialogResponse(task.getResult().getAccessCode());
                else
                    userMessage(task.getResult().getErrors());

                return null;
            }
        },Task.UI_THREAD_EXECUTOR);

    }



    private Task<SubmitPayResponse> submitTransaction(EncryptItemsResponse response) throws IOException {

        payment.setTotalAmount(Integer.parseInt(totalAmount));
        cardDetails.setName(cardName);
        cardDetails.setNumber(response.getItems().get(0).getValue());
        cardDetails.setCVN(response.getItems().get(1).getValue());
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.valueOf((transTypes.isEmpty() ? TransactionType.Purchase.toString() : transTypes.toString())));
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        SubmitPayResponse submitPayResponse = RapidAPI.submitPayment(transaction).body();
        if(RapidAPI.submitPayment(transaction).isSuccessful()&& submitPayResponse.getErrors()
                !=null)
            alertDialogResponse(submitPayResponse.getAccessCode());

        Task<SubmitPayResponse> reponse = Task.forResult(submitPayResponse);

        return reponse;
    }

    private void userMessage(String message){

        UserMessageResponse response = RapidAPI.userMessage(Locale.getDefault().getLanguage(), message);
        if(response.getErrors() != null)
            alertDialogResponse(errorHandler(response.getErrorMessages()));

    }

    private void clickOnNextPageButton(){
        Intent intent = new Intent(this,EncryptionActivity.class);
        startActivity(intent);
    }

    private void fetchDataFromForm(ContentSampleMainBinding binding){

        totalAmount = noNullObjects(binding.totalAmount.getText().toString());
        cardName = noNullObjects(binding.cardName.getText().toString());
        cardNumber = noNullObjects(binding.cardNumber.getText().toString());
        cvnNumber = noNullObjects(binding.cvn.getText().toString());
        expMonth = binding.expMonth.getSelectedItem().toString();
        expYear = binding.expYear.getSelectedItem().toString();
        transTypes = binding.transTypes.getSelectedItem().toString();

    }

    private void transTypesPopulator(ContentSampleMainBinding binding) {

        List<String> list = new ArrayList<>();
        for (TransactionType type : TransactionType.values()) {
            list.add(type.name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.transTypes.setAdapter(dataAdapter);

    }

    private void dateTimePopulator(ContentSampleMainBinding binding) {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, makeSequence(1, 12));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(binding.expMonth != null)
            binding.expMonth.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeSequence
                (Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) + 10));
        if(binding.expYear != null)
            binding.expYear.setAdapter(dataAdapter);
    }

    private List<String> makeSequence(int begin, int end) {
        List<String> ret = new ArrayList(end - begin + 1);

        for (int i = begin; i <= end; ret.add(
                ((end > 12 ? "0000" : "00") + Integer.toString(i)).substring(Integer.toString(i++).length())))
            ;

        return ret;
    }

    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(SampleMainActivity.this)
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

    private String errorHandler(List<String> response) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String errorMsg : response) {
            result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
            i++;
        }
        return result.toString();
    }

    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

}
