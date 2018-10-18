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
        mContext = mView.getContext();
    }

    @Override
    public void onLoadBookList() {
        mModel.loadImportedBooksAsyn().subscribe(new Consumer<List<BookBean>>() {
            @Override
            public void accept(@NonNull List<BookBean> books) throws Exception {
                mView.refreshBookList(books);
            }
        });
    }

    @Override
    public void onDeleteBook(long bookId) {
        mModel.removeBook(bookId);
    }
}
