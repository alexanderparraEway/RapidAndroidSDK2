package sdk.payment.eway.com.rapidandroidsdk.data.entities;



import java.util.ArrayList;

import sdk.payment.eway.com.rapidandroidsdk.data.beans.CodeDetail;

public class CodeLookupResponse {
    private String Language;
    private ArrayList<CodeDetail> CodeDetails;
    private String Errors;

    public CodeLookupResponse(String errors, String language, ArrayList<CodeDetail> codeDetails) {
        Language = language;
        CodeDetails = codeDetails;
        Errors = errors;
    }

    public CodeLookupResponse() {

    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public ArrayList<CodeDetail> getCodeDetails() {
        return CodeDetails;
    }

    public void setCodeDetails(ArrayList<CodeDetail> codeDetails) {
        CodeDetails = codeDetails;
    }

    public String getErrors() {
        return Errors;
    }

    public void setErrors(String errors) {
        Errors = errors;
    }
}
