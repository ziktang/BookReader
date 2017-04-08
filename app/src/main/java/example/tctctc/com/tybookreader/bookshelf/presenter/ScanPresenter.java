package example.tctctc.com.tybookreader.bookshelf.presenter;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.utils.CustomUUId;
import example.tctctc.com.tybookreader.utils.FileUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ScanPresenter extends ScanContact.Presenter {
    private Subscriber<File> subscriber;

    @Override
    public void onAddBooks(List<File> files) {
        List<BookBean> list = new ArrayList<>();
        for (File file : files) {
            BookBean bean = new BookBean();
            //找出最后一个"."的位置，以此隔离出名字和后缀
            int i = file.getName().lastIndexOf(".");
            bean.setBookName(file.getName().substring(0,i));
            bean.setBookType(1);
            bean.setFileSize(FileUtils.getFileSize(file));
            bean.setLength(file.length());
            bean.setPath(file.getAbsolutePath());
            list.add(bean);
        }
        mModel.addBookList(list).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mView.toShelf();
            }
        });
    }

    @Override
    public void onStartScanBooks(File file, String rex) {
        mView.whenStartScan();
        subscriber = new Subscriber<File>() {
            @Override
            public void onCompleted() {
//                Log.i(TAG, "最终扫描书本数量" + mFiles.size());
//                long endTime = System.currentTimeMillis();
//                Log.i(TAG, "花费时间:" + (endTime - startTime) / 1000);
                mView.whenStopScan();
            }

            @Override
            public void onError(Throwable e) {
                mView.whenStopScan();
            }

            @Override
            public void onNext(File file) {
                mView.whenScan(file,mModel.getTotalNum());
            }
        };

        mModel.scanFile(file,rex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    @Override
    public void onStopScanBooks() {
        subscriber.unsubscribe();
        mView.whenStopScan();
    }


    @Override
    public void onStart() {

    }

}
