package sdk.payment.eway.com.rapidandroidsdk.domain.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

/**
 * Created by alexanderparra on 14/11/16.
 */
@Singleton
public class JobExecutor implements ThreadExecutor {

    private final int INITIAL_POOL_SIZE = 3;

    private final int MAX_POOL_SIZE = 5;

    private final int KEEP_ALIVE_TIME = 10;

    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> workQueue;
    
    private final ThreadPoolExecutor threadPoolExecutor;

    private final ThreadFactory threadFactory;

    public JobExecutor() {

        workQueue = new LinkedBlockingDeque<Runnable>();

        threadFactory = new JobThreadFactory();

        threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT, this.workQueue, this.threadFactory);
    }

    @Override
    public void execute(Runnable runnable) {

        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        this.threadPoolExecutor.execute(runnable);

    }

    private class JobThreadFactory implements ThreadFactory{

        int counter = 0;
        private final String THREAD_NAME = "android_";


        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable,THREAD_NAME + String.valueOf(counter++));
        }
    }
}
