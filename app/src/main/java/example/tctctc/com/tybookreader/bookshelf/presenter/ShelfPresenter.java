package example.tctctc.com.tybookreader.bookshelf.presenter;

import java.util.List;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.contact.ShelfContact;
import rx.functions.Action1;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ShelfPresenter extends ShelfContact.Presenter {

    @Override
    public void onStart() {
    }

    @Override
    public void onLoadBookList() {
        mModel.loadBookList().subscribe(new Action1<List<BookBean>>() {
            @Override
            public void call(List<BookBean> books) {
                mView.refreshBookList(books);
            }
        });
    }

    @Override
    public void onDeleteBooks(List<BookBean> books) {
        mModel.removeBooks(books).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                onLoadBookList();
            }
        });
    }
}
