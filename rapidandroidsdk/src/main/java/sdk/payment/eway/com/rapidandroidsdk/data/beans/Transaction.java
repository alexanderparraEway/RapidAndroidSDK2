package sdk.payment.eway.com.rapidandroidsdk.data.beans;



import java.util.ArrayList;

import sdk.payment.eway.com.rapidandroidsdk.data.entities.SubmitPaymentRequest;

public class Transaction {
    private TransactionType TransactionType;
    private Customer Customer;
    private ShippingDetails ShippingDetails;
    private SubmitPaymentRequest.ShippingAddress shippingAddress;
    private Payment Payment;
    private ArrayList<LineItem> LineItems;
    private ArrayList<String> Options;
    private String PartnerID;
    private String androidPay;

    public TransactionType getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        TransactionType = transactionType;
    }

    public Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(Customer customer) {
        Customer = customer;
    }

    public ShippingDetails getShippingDetails() {
        return ShippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        ShippingDetails = shippingDetails;
    }

    public String getAndroidPay() {
        return androidPay;
    }

    public void setAndroidPay(String androidPay) {
        this.androidPay = androidPay;
    }

    public Payment getPayment() {
        return Payment;
    }

    public void setPayment(Payment payment) {
        Payment = payment;
    }

    public ArrayList<LineItem> getLineItems() {
        return LineItems;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        LineItems = lineItems;
    }

    public ArrayList<String> getOptions() {
        return Options;
    }

    public void setOptions(ArrayList<String> options) {
        Options = options;
    }

    public String getPartnerID() {
        return PartnerID;
    }

    public void setPartnerID(String partnerID) {
        PartnerID = partnerID;
    }

    public SubmitPaymentRequest.ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(SubmitPaymentRequest.ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
