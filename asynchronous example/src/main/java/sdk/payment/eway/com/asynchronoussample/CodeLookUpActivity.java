package sdk.payment.eway.com.asynchronoussample;

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

import sdk.payment.eway.com.asynchronoussample.databinding.ActivityCodeLookUpBinding;
import sdk.payment.eway.com.asynchronoussample.databinding.ContentCodeLookUpBinding;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.UserMessageResponse;
import sdk.payment.eway.com.rapidandroidsdk.domain.usecases.AbsAsyncUserMessageUseCase;
import sdk.payment.eway.com.rapidandroidsdk.presentation.RapidAPI;

public class CodeLookUpActivity extends AppCompatActivity {

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

        RapidAPI.asynUserMessage(Locale.getDefault().getLanguage(), errorCodeNumber, new AbsAsyncUserMessageUseCase.CallbackUserMessage() {
            @Override
            public void onSuccess(UserMessageResponse response) {
                alertDialogResponse(errorHandler(response.getErrorMessages()));
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
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
