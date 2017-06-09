package example.tctctc.com.tybookreader.bookshelf.contact;

import java.util.List;

import example.tctctc.com.tybookreader.base.BaseModel;
import example.tctctc.com.tybookreader.base.BasePresenter;
import example.tctctc.com.tybookreader.base.BaseView;
import example.tctctc.com.tybookreader.bean.BookBean;
import io.reactivex.Observable;

/**
 * Created by tctctc on 2017/3/24.
 */

public interface ShelfContact {
    interface View extends BaseView {
        void refreshBookList(List<BookBean> books);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void onLoadBookList();

        public abstract void onDeleteBooks(List<BookBean> books);
    }

    interface Model extends BaseModel {
        Observable<List<BookBean>> loadBookList();

        Observable<Boolean> removeDeleteBooks(List<BookBean> books);

        Observable<Boolean> removeBooks(List<BookBean> books);
    }
}
