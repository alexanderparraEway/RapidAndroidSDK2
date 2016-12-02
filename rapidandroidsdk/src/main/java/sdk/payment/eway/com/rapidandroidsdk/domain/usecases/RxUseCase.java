package sdk.payment.eway.com.rapidandroidsdk.domain.usecases;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import sdk.payment.eway.com.rapidandroidsdk.domain.exception.Error;
import sdk.payment.eway.com.rapidandroidsdk.domain.executor.IOThread;
import sdk.payment.eway.com.rapidandroidsdk.domain.executor.PostExecutionThread;
import sdk.payment.eway.com.rapidandroidsdk.domain.executor.ThreadIOExecutor;
import sdk.payment.eway.com.rapidandroidsdk.domain.executor.UIThread;


/**
 * Created by alexanderparra on 9/11/16.
 */

public abstract class RxUseCase<T> {

    private PostExecutionThread postExecutionThread;
    private ThreadIOExecutor threadIOExecutor;

    public RxUseCase() {
        threadIOExecutor = new IOThread();
        postExecutionThread = new UIThread();

    }

    public Flowable<T> rxExecute(){

        return this.buildUseCaseObservable()
                 .subscribeOn(threadIOExecutor.SchedulerIO())
                 .onErrorResumeNext(Error.<T>errorToken())
                 .observeOn(postExecutionThread.scheduler());

    }


    protected abstract Flowable<T> buildUseCaseObservable();

}
