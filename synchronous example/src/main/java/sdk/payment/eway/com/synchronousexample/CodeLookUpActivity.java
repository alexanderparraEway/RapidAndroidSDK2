package sdk.payment.eway.com.synchronousexample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsAsyncUserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;
import sdk.payment.eway.com.synchronousexample.databinding.ActivityCodeLookUpBinding;
import sdk.payment.eway.com.synchronousexample.databinding.ContentCodeLookUpBinding;

/**
 * Created by alexanderparra on 30/11/16.
 */

public class CodeLookUpActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;
    private String errorCodeNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityCodeLookUpBinding mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_code_look_up);
        setSupportActionBar(mainBinding.toolbar);

        mainBinding.included.nextPageCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodeLookUpActivity.this,SampleMainActivity.class);
                startActivity(intent);
            }
        });

        mainBinding.included.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton(mainBinding.included);
            }
        });

    }

    public void submitButton(ContentCodeLookUpBinding binding){

        progressDialog = ProgressDialog.show(CodeLookUpActivity.this, "Processing", "Processing payment", true);
        fetchDataFromForm(binding);

        Task.callInBackground(new Callable<UserMessageResponse>() {
            @Override
            public UserMessageResponse call() throws Exception {
                return RapidAPI.userMessage(Locale.getDefault().getLanguage(), errorCodeNumber);
            }
        }).continueWith(new Continuation<UserMessageResponse, Void>() {
            @Override
            public Void then(Task<UserMessageResponse> task) throws Exception {
                alertDialogResponse(errorHandler(task.getResult().getErrorMessages()));
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);

    }


    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(CodeLookUpActivity.this)
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

    private void fetchDataFromForm(ContentCodeLookUpBinding binding){
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
}
