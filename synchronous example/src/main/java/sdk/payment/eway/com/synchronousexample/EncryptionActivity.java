package sdk.payment.eway.com.synchronousexample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsAsyncUserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;
import sdk.payment.eway.com.synchronousexample.databinding.ActivityEncryptionBinding;
import sdk.payment.eway.com.synchronousexample.databinding.ContentEncryptionBinding;

/**
 * Created by alexanderparra on 30/11/16.
 */

public class EncryptionActivity  extends AppCompatActivity {

    private String cardNumber;
    private String cvnNumber;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityEncryptionBinding mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_encryption);
        setSupportActionBar(mainBinding.toolbar);

        mainBinding.included.submitEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton(mainBinding.included);
            }
        });

        mainBinding.included.nextPageEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPageButton();
            }
        });

    }

    public void nextPageButton(){
        Intent intent = new Intent(EncryptionActivity.this,CodeLookUpActivity.class);
        startActivity(intent);
    }

    public void submitButton(ContentEncryptionBinding binding){

        progressDialog = ProgressDialog.show(EncryptionActivity.this, "Processing", "Processing payment", true);
        fetchDataFromForm(binding);


        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));


        Task.callInBackground(new Callable<EncryptItemsResponse>() {

            public EncryptItemsResponse call() throws IOException { return RapidAPI.encryptValues(values).body(); }

        }).continueWith(new Continuation<EncryptItemsResponse, EncryptItemsResponse>() {
            @Override
            public EncryptItemsResponse then(Task<EncryptItemsResponse> task) throws Exception {

                if(task.getResult().getErrors()== null) {
                    StringBuilder message = new StringBuilder();
                    for (NVPair pair : task.getResult().getItems()) {
                        message.append(pair.getName()).append(": ").append(pair.getValue()).append("\n");
                    }
                    String composeMessage = message.toString();

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    alertDialogResponse(composeMessage);
                }
                else
                    userMessage(task.getResult().getErrors());

                return null;
            }
        },Task.UI_THREAD_EXECUTOR);


    }

    private void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(EncryptionActivity.this)
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

    private String errorHandler(List<String> response) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String errorMsg : response) {
            result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
            i++;
        }
        return result.toString();
    }


    private void fetchDataFromForm(ContentEncryptionBinding binding){
        cardNumber = noNullObjects(binding.cardNoEncrypt.getText().toString());
        cvnNumber = noNullObjects(binding.cvnNumberEncrypt.getText().toString());
    }

    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

}
