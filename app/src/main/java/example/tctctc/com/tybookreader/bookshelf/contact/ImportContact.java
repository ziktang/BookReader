package example.tctctc.com.tybookreader.bookshelf.contact;

import java.io.File;
import java.util.List;

import example.tctctc.com.tybookreader.base.BaseModel;
import example.tctctc.com.tybookreader.base.BasePresenter;
import example.tctctc.com.tybookreader.base.BaseView;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanBook;
import io.reactivex.Observable;

/**
 * Created by tctctc on 2017/3/24.
 */

public interface ImportContact {
    interface View extends BaseView {
        void showScanBookList(List<ScanBook> files,int fileNum,int importableNum);
        void toShelf();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void onAddBooks(List<ScanBook> files);
        public abstract void onGetFileList(File file);
    }

    interface Model extends BaseModel {
        Observable<Boolean> addBookList(List<BookBean> been);
        List<File> getFileList(File rootFile);
    }
}
