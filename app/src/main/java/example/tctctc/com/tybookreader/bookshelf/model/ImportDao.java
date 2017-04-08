package example.tctctc.com.tybookreader.bookshelf.model;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Collections;
import java.util.List;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.BookBeanDao;
import example.tctctc.com.tybookreader.bean.DaoMaster;
import example.tctctc.com.tybookreader.bean.DaoSession;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.common.FileComparater;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import example.tctctc.com.tybookreader.utils.FileUtils;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ImportDao implements ImportContact.Model {

    private static final String dbName = "BookBean";
    private BookBeanDao mDao;

    public ImportDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BookApplication.getContext(), dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession session = master.newSession();
        mDao = session.getBookBeanDao();
    }

    /**
     * 批量添加书籍
     *
     * @param books
     * @return
     */
    @Override
    public Observable<Boolean> addBookList(List<BookBean> books) {
        return Observable.just(books).map(new Func1<List<BookBean>, Boolean>() {
            @Override
            public Boolean call(List<BookBean> books) {
                mDao.insertInTx(books);
                return true;
            }
        }).compose(RxSchedulers.<Boolean>ioMain());
    }

    @Override
    public List<File> getFileList(File rootFile) {
        List<File> files = FileUtils.listFilterOneFolder(rootFile, ".txt");
        Collections.sort(files, new FileComparater());
        return files;
    }
}
