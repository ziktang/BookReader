package example.tctctc.com.tybookreader.bookshelf.contact;

import java.io.File;
import java.util.List;

import example.tctctc.com.tybookreader.base.BaseModel;
import example.tctctc.com.tybookreader.base.BasePresenter;
import example.tctctc.com.tybookreader.base.BaseView;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanBook;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by tctctc on 2017/3/24.
 */

public interface ScanContact {
    interface View extends BaseView {
        void toShelf();

        void whenStartScan();

        void whenStopScan();

        void refresh(int totalNum);

        void whenScan(File file);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void onAddBooks(List<ScanBook> files);

        public abstract void onStartScanBooks(File file, String rex);

        public abstract void onStopScanBooks();
    }

    interface Model extends BaseModel {
        Observable<Boolean> addBookList(List<BookBean> been);

        Flowable<File> scanFile(File file, String rex);

        int getTotalNum();

    }
}
