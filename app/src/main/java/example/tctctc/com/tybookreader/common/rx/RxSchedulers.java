package example.tctctc.com.tybookreader.common.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class RxSchedulers {
    public static <T> Observable.Transformer<T,T> ioMain(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
