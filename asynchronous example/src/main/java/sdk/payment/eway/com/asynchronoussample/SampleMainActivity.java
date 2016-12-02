package sdk.payment.eway.com.asynchronoussample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sdk.payment.eway.com.asynchronoussample.databinding.ActivitySampleMainBinding;
import sdk.payment.eway.com.asynchronoussample.databinding.ContentSampleMainBinding;
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
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsAsyncUserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsUserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;


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
        RapidAPI.setRapidEndpoint("https://api.sandbox.ewaypayments.com/");

        mainBinding.included.submitPayment.setOnClickListener(new View.OnClickListener() {
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

    public void clickOnSubmitButton(ContentSampleMainBinding binding)  {
        progressDialog = ProgressDialog.show(SampleMainActivity.this,"Processing","Processing payment", true);
        fetchDataFromForm(binding);
        payment.setTotalAmount(Integer.parseInt(totalAmount));
        cardDetails.setName(cardName);
        try {
            fetchEncryptData(cardNumber, cvnNumber);
        } catch (RapidConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clickOnNextPageButton(){

        Intent intent = new Intent(SampleMainActivity.this,EncryptionActivity.class);
        SampleMainActivity.this.startActivity(intent);

    }

    private void fetchEncryptData(String cardNumber, String cvnNumber) throws RapidConfigurationException, IOException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));

        RapidAPI.asyncEncryptValues(values).enqueue(new Callback<EncryptItemsResponse>() {
            @Override
            public void onResponse(Call<EncryptItemsResponse> call, Response<EncryptItemsResponse> response) {
                if(response.body().getErrors() == null)
                    submitTransaction(response.body());
                else
                    userMessage(response.body().getErrors());
            }

            @Override
            public void onFailure(Call<EncryptItemsResponse> call, Throwable t) {
                userMessage(t.getMessage());
            }
        });

    }

    private void submitTransaction(EncryptItemsResponse response){

        cardDetails.setNumber(response.getItems().get(0).getValue());
        cardDetails.setCVN(response.getItems().get(1).getValue());
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.valueOf((transTypes.isEmpty() ? TransactionType.Purchase.toString() : transTypes.toString())));
        transaction.setPayment(payment);
        transaction.setCustomer(customer);

        RapidAPI.asyncSubmitPayment(transaction)
                .enqueue(new Callback<SubmitPayResponse>() {
            @Override
            public void onResponse(Call<SubmitPayResponse> call, Response<SubmitPayResponse> response) {

                if (response.body().getErrors() == null)
                    alertDialogResponse(response.body().getAccessCode());
                else
                    userMessage(response.body().getErrors());

            }

            @Override
            public void onFailure(Call<SubmitPayResponse> call, Throwable t) {userMessage(t.getMessage());}
        });

    }

    private void userMessage(String message){

        RapidAPI.asynUserMessage(Locale.getDefault().getLanguage(), message, new AbsAsyncUserMessageUseCase.CallbackUserMessage() {
            @Override
            public void onSuccess(UserMessageResponse response) {
                alertDialogResponse(errorHandler(response.getErrorMessages()));
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

    }



    private void fetchDataFromForm(ContentSampleMainBinding binding){

        totalAmount = noNullObjects(binding.totalAmount.getText().toString());
        cardName = noNullObjects(binding.cardName.getText().toString());
        cardNumber = noNullObjects(binding.cardNumber.getText().toString());
        cvnNumber = noNullObjects(binding.cvn.getText().toString());
        expMonth = binding.expMonthCard.getSelectedItem().toString();
        expYear = binding.expYearCard.getSelectedItem().toString();
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
        if(binding.expMonthCard != null)
            binding.expMonthCard.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeSequence
                (Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) + 10));
        if(binding.expYearCard != null)
            binding.expYearCard.setAdapter(dataAdapter);
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
