package example.tctctc.com.tybookreader.common.rx;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

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
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    /**
     * 注入监听
     * @param tag
     * @param action1
     */
    public <T>void onEvent(Object tag, Action1<T> action1){
        Observable<T> observable = mRxbus.register(tag);
        mObservableMaps.put(tag,observable);
        mCompositeSubscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        }));

    }

    //发送消息
    public void post(Object tag,Object content){
        mRxbus.post(tag,content);
    }

    //管理添加
    public void add(Subscription subscription){
        mCompositeSubscription.add(subscription);
    }

    /**
     * 清理所有的监听和订阅，防止内存泄露
     */
    public void clear(){
        mCompositeSubscription.unsubscribe();
        for (Map.Entry<Object, Observable<?>> entry : mObservableMaps.entrySet()) {
            mRxbus.unRegister(entry.getKey(),entry.getValue());
        }
    }
}
