package sdk.payment.eway.com.rapidandroidsdk.data.beans;

/**
 * Created by alexander.parra on 16/03/2016.
 */
public class AndroidPayID {
    private String encryptedMessage;
    private String ephemeralPublicKey;
    private String tag;

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public String getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    public void setEphemeralPublicKey(String ephemeralPublicKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
