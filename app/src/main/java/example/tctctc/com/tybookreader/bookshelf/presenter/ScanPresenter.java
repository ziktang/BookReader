package example.tctctc.com.tybookreader.bookshelf.presenter;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanBook;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import example.tctctc.com.tybookreader.utils.FileUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ScanPresenter extends ScanContact.Presenter {

    public static final String TAG = "ScanPresenter";

    private Subscriber<File> mSubscriber;
    private Subscription mSubscription;
    private Disposable mDisposable;

    @Override
    public void onAddBooks(List<ScanBook> scanBooks) {
        List<BookBean> list = new ArrayList<>();
        for (ScanBook scanBook : scanBooks) {
            File file = scanBook.getFile();
            BookBean bean = new BookBean();
            //找出最后一个"."的位置，以此隔离出名字和后缀
            int i = file.getName().lastIndexOf(".");
            bean.setBookName(file.getName().substring(0, i));
            bean.setMBookType(1);
            bean.setFileSize(FileUtils.getFileSize(file));
//            bean.setLength(file.length());
            bean.setPath(file.getAbsolutePath());
            list.add(bean);
        }

        Log.i("AAA","111111111111111111111");
        mModel.addBookList(list).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                Log.i("AAA","2222222222222222222");
                mView.toShelf();
            }
        });
    }

    @Override
    public void onStartScanBooks(File file, String rex) {
        mView.whenStartScan();

        Observable.interval(16, TimeUnit.MILLISECONDS).compose(RxSchedulers.<Long>ioMain()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }
            @Override
            public void onNext(@NonNull Long aLong) {
                mView.refresh(mModel.getTotalNum());
            }
            @Override
            public void onError(@NonNull Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });

        mSubscriber = new Subscriber<File>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                mSubscription = s;
            }

            @Override
            public void onNext(File file) {
                mView.whenScan(file);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.whenStopScan();
            }

            @Override
            public void onComplete() {
                onStopScanBooks();
            }
        };
        mModel.scanFile(file, rex)
                .subscribe(mSubscriber);
    }


    @Override
    public void onStopScanBooks() {
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        Log.i("AAA","onStopScanBooks");
        mSubscription.cancel();
        Log.i("AAA","cancel");
        mDisposable.dispose();
        mView.whenStopScan();
        mView.refresh(mModel.getTotalNum());
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscription.cancel();
        mDisposable.dispose();
    }
}
