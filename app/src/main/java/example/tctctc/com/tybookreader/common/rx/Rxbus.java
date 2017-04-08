package example.tctctc.com.tybookreader.common.rx;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by tctctc on 2017/3/18.
 */

/**
 * 基于Rxjava实现的Android跨组件事件工具，类似EventBus
 */
public class Rxbus {
    //全局单例
    private static Rxbus mRxbus;

    public static synchronized Rxbus get() {
        if (null == mRxbus) {
            mRxbus = new Rxbus();
        }
        return mRxbus;
    }

    //所有的事件线，每个事件线上都有一个list<Subject>等待接受事件消息
    private ConcurrentHashMap<Object, List<Subject>> mSubjectMaps = new ConcurrentHashMap<>();


    /**
     * 注册监听
     *
     * @param tag
     * @return
     */
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjects = mSubjectMaps.get(tag);
        if (null == subjects) {
            subjects = new ArrayList<>();
            mSubjectMaps.put(tag, subjects);
        }
        Subject<T, T> subject = PublishSubject.create();
        subjects.add(subject);
        return subject;
    }

    /**
     * 取消监听
     *
     * @param tag
     */
    public void unRegister(@NonNull Object tag) {
        List<Subject> subjects = mSubjectMaps.get(tag);
        if (null != subjects) {
            mSubjectMaps.remove(tag);
        }
    }

    /**
     * 取消监听某一Observable
     *
     * @param tag
     * @param observable
     * @return
     */
    public Rxbus unRegister(@NonNull Object tag, @NonNull Observable observable) {
        if (null == observable) {
            return mRxbus;
        }
        List<Subject> subjects = mSubjectMaps.get(tag);
        if (subjects != null)
            subjects.remove(observable);
        if (isEmpty(subjects)) {
            mSubjectMaps.remove(tag);
        }
        return mRxbus;
    }

    /**
     * 发送消息，默认以类名为tag
     *
     * @param content
     */
    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }


    /**
     * 发送消息
     *
     * @param tag
     * @param content
     */
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjects = mSubjectMaps.get(tag);
        if (!isEmpty(subjects)) {
            for (Subject subject : subjects) {
                subject.onNext(content);
            }
        }
    }

    public boolean isEmpty(Collection<Subject> collection) {
        return collection == null || collection.isEmpty();
    }
}
