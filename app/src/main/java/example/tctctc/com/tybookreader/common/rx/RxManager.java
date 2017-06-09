package example.tctctc.com.tybookreader.common.rx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by tctctc on 2017/3/18.
 */
/**
 * 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 */
public class RxManager {

    public Rxbus mRxbus = Rxbus.get();

    //管理Rxbus订阅
    private Map<Object,Observable<?>> mObservableMaps = new HashMap<>();
    //管理Observables-Subscribers订阅
    private CompositeDisposable mDisposable = new CompositeDisposable();

    /**
     * 注入监听
     * @param tag
     * @param action1
     */
    public <T>void onEvent(Object tag, Consumer<T> action1){
        Observable<T> observable = mRxbus.register(tag);
        mObservableMaps.put(tag,observable);
        mDisposable.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }));

    }

    //发送消息
    public void post(Object tag,Object content){
        mRxbus.post(tag,content);
    }

    //管理添加
    public void add(Disposable disposable){
        mDisposable.add(disposable);
    }

    /**
     * 清理所有的监听和订阅，防止内存泄露
     */
    public void clear(){
        mDisposable.dispose();
        for (Map.Entry<Object, Observable<?>> entry : mObservableMaps.entrySet()) {
            mRxbus.unRegister(entry.getKey(),entry.getValue());
        }
    }
}
