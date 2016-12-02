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

import java.util.List;
import java.util.Locale;
import io.reactivex.android.schedulers.AndroidSchedulers;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;
import sdk.payment.eway.com.rapidandroidsdk2.databinding.ActivityRxCodeLookUpBinding;
import sdk.payment.eway.com.rapidandroidsdk2.databinding.ContentRxCodeLookUpBinding;


public class RxCodeLookUpActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String errorCodeNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityRxCodeLookUpBinding mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_rx_code_look_up);
        setSupportActionBar(mainBinding.toolbar);

        mainBinding.includedCode.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton(mainBinding.includedCode);
            }
        });

        mainBinding.includedCode.nextPageCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPageButton();
            }
        });

    }


    public void submitButton(ContentRxCodeLookUpBinding binding){
        progressDialog = ProgressDialog.show(RxCodeLookUpActivity.this, "Processing", "Processing payment", true);
        fetchDataFromForm(binding);
        RapidAPI.rxUserMessage(Locale.getDefault().getLanguage(),errorCodeNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<UserMessageResponse>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(UserMessageResponse userMessageResponse) {
                        alertDialogResponse(errorHandler(userMessageResponse.getErrorMessages()));
                    }

                    @Override
                    public void onError(Throwable t) {
                        alertDialogResponse(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void fetchDataFromForm(ContentRxCodeLookUpBinding binding){
        errorCodeNumber = noNullObjects(binding.errorCodes.getText().toString());
    }

    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
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

    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(RxCodeLookUpActivity.this)
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


    public void nextPageButton(){
        Intent intent = new Intent(RxCodeLookUpActivity.this,RxSampleMainActivity.class);
        startActivity(intent);
    }



}
