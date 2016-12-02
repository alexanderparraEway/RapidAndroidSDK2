package sdk.payment.eway.com.rapidandroidsdk.domain.exception;


import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ScalarCallable;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.CodeLookupResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.EncryptItemsResponse;
import sdk.payment.eway.com.rapidandroidsdk.data.entities.RapidConfigurationException;


/**
 * Created by alexanderparra on 13/11/16.
 */

public class Error {

    public static <T>Function<Throwable, Publisher<? extends T>> errorToken () {
        return new Function<Throwable, Publisher<? extends T>>() {
            @Override
            public Publisher<? extends T> apply(Throwable throwable) throws Exception {
                try {
                    if (throwable.getMessage().contains("401"))
                        throw new RapidConfigurationException("S9993");
                    if (throwable.getMessage().contains("443"))
                        throw new RapidConfigurationException("S9991");

                    throw new RapidConfigurationException("S9992");

                } catch (final RapidConfigurationException exception) {

                    return Flowable.create(new FlowableOnSubscribe<T>() {
                        @Override
                        public void subscribe(FlowableEmitter<T> e) throws Exception {
                            e.onError(new Throwable(exception.getErrorCodes()));
                        }
                    }, BackpressureStrategy.BUFFER);
                }
            }
        };
    }

}
