package example.tctctc.com.tybookreader.bookshelf.presenter;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import example.tctctc.com.tybookreader.app.Constant;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanFile;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
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

    private Disposable mDisposable;
    private List<BookBean> mImportBookBeans;

    @Override
    public void onAddBooks(List<ScanFile> scanFiles) {
        List<BookBean> list = new ArrayList<>();
        for (ScanFile scanFile : scanFiles) {
            File file = scanFile.getFile();
            BookBean bean = new BookBean();
            //找出最后一个"."的位置，以此隔离出名字和后缀
            int i = file.getName().lastIndexOf(".");
            bean.setBookName(file.getName().substring(0, i));
            bean.setBookType(BookBean.BOOK_TYPE_LOCAL);
            bean.setFileSize(FileUtils.getFileSize(file));
            bean.setStatus(BookBean.BOOK_STATUS_IMPORT);
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
    public void onStartScanBooks(final File file, final String rex) {
        mView.whenStartScan();
        mImportBookBeans = mModel.loadImportedBooks();
//        Observable.just(1).delay(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
                mModel.scanFile(file, rex).subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                        mRxManager.add(disposable);
                    }

                    @Override
                    public void onNext(File file) {
                        Log.i(TAG,"thread id :"+Thread.currentThread().getId());
                        if (file.exists()) {
                            ScanFile scanFile = new ScanFile(file, false);
                            if (isImported(file)){
                                scanFile.setImported(true);
                            }
                            mView.whenScan(scanFile);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.whenStopScan();
                    }

                    @Override
                    public void onComplete() {
                        mView.whenStopScan();
                    }
                });
//            }
//        });
    }


    @Override
    public void onStopScanBooks() {
        Log.i(TAG,"onStopScanBooks");
        mDisposable.dispose();
        mView.whenStopScan();
    }

    private boolean isImported(File file){
        if (file == null || !file.exists() ||file.isFile()){
            return false;
        }
        for (BookBean bookBean:mImportBookBeans){
            if (bookBean.getPath()!=null&&bookBean.getPath().equals(file.getAbsolutePath())){
                return true;
            }
        }
        return false;
    }


    @Override
    public void onStart() {
        mContext = mView.getContext();
    }
}
