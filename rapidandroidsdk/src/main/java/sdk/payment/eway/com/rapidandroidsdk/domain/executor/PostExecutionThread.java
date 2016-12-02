package sdk.payment.eway.com.rapidandroidsdk.domain.executor;


import io.reactivex.Scheduler;

/**
 * Created by alexanderparra on 9/11/16.
 */

public interface PostExecutionThread {

     Scheduler scheduler();
}
