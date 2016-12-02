package sdk.payment.eway.com.rapidandroidsdk2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;

import sdk.payment.eway.com.rapidandroidsdk.data.beans.NVPair;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;
import sdk.payment.eway.com.rapidandroidsdk2.databinding.ActivityRxEncryptionBinding;
import sdk.payment.eway.com.rapidandroidsdk2.databinding.ContentRxEncryptionBinding;


public class RxEncryptionActivity extends AppCompatActivity {


    private String cardNumber;
    private String cvnNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityRxEncryptionBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_rx_encryption);
        setSupportActionBar(binding.toolbar);

        binding.included.submitEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxEncryption(binding.included);
            }
        });

        binding.included.nextPageEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RxEncryptionActivity.this,RxCodeLookUpActivity.class);
                startActivity(intent);
            }
        });


    }


    private void rxEncryption(ContentRxEncryptionBinding binding){

        progressDialog = ProgressDialog.show(RxEncryptionActivity.this, "Processing", "Processing payment", true);
        fetchDataFromForm(binding);
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
                        StringBuilder message = new StringBuilder();
                        for (NVPair pair : response.getItems()) {
                            message.append(pair.getName()).append(": ").append(pair.getValue()).append("\n");
                        }
                        String tempMessage = message.toString();
                        alertDialogResponse(tempMessage);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(RxEncryptionActivity.this)
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



    private void fetchDataFromForm(ContentRxEncryptionBinding binding){
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
