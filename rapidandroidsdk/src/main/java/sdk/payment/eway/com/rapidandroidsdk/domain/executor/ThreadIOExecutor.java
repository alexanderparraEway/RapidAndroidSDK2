package sdk.payment.eway.com.rapidandroidsdk.domain.executor;

import io.reactivex.Scheduler;

/**
 * Created by alexanderparra on 17/11/16.
 */

public interface ThreadIOExecutor {

    Scheduler SchedulerIO();
}
