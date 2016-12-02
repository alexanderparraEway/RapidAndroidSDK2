package sdk.payment.eway.com.rapidandroidsdk.domain.executor;

import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by alexanderparra on 14/11/16.
 */
@Singleton
public class UIThread implements  PostExecutionThread{

    @Override
    public Scheduler scheduler() {
        return AndroidSchedulers.mainThread();
    }
}
