package sdk.payment.eway.com.rapidandroidsdk.domain.executor;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by alexanderparra on 17/11/16.
 */

public class IOThread implements ThreadIOExecutor {

    @Override
    public Scheduler SchedulerIO() {
        return Schedulers.io();
    }
}
