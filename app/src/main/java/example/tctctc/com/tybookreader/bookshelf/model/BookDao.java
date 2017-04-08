package example.tctctc.com.tybookreader.bookshelf.model;

import android.database.sqlite.SQLiteDatabase;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.bean.BookBeanDao;
import example.tctctc.com.tybookreader.bean.DaoMaster;
import example.tctctc.com.tybookreader.bean.DaoSession;

/**
 * Created by tctctc on 2017/4/3.
 * Function:
 */

public class BookDao {
    private static final String dbName = "BookBean";
    private static BookBeanDao mDao;

    public static BookBeanDao getInstance() {
        if (mDao == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BookApplication.getContext(), dbName, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster master = new DaoMaster(db);
            DaoSession session = master.newSession();
            mDao = session.getBookBeanDao();
        }
        return mDao;
    }

}
