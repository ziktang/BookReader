package example.tctctc.com.tybookreader.bookshelf.presenter;

import java.util.List;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.contact.ShelfContact;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
        mModel.loadBookList().subscribe(new Consumer<List<BookBean>>() {
            @Override
            public void accept(@NonNull List<BookBean> books) throws Exception {
                mView.refreshBookList(books);
            }
        });
    }

    @Override
    public void onDeleteBooks(List<BookBean> books) {
        mModel.removeBooks(books).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                onLoadBookList();
            }
        });
    }
}
