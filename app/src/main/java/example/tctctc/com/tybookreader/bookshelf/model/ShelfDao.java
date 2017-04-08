package example.tctctc.com.tybookreader.bookshelf.model;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.List;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.BookBeanDao;
import example.tctctc.com.tybookreader.bean.DaoMaster;
import example.tctctc.com.tybookreader.bean.DaoSession;
import example.tctctc.com.tybookreader.bookshelf.contact.ShelfContact;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import example.tctctc.com.tybookreader.utils.CollectionUtils;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ShelfDao implements ShelfContact.Model{

    private static final String dbName = "BookBean";
    private BookBeanDao mDao;

    public ShelfDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BookApplication.getContext(), dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession session = master.newSession();
        mDao = session.getBookBeanDao();
    }

    @Override
    public Observable<List<BookBean>> loadBookList() {
        return Observable.just("all").map(new Func1<String, List<BookBean>>() {
            @Override
            public List<BookBean> call(String s) {
                QueryBuilder queryBuilder = mDao.queryBuilder();
                return queryBuilder.build().listLazy();
            }
        }).compose(RxSchedulers.<List<BookBean>>ioMain());
    }

    /**
     * 根据集合批量移出书架且删除源文件
     *
     * @param books
     * @return
     */
    @Override
    public Observable<Boolean> removeDeleteBooks(List<BookBean> books) {
        return Observable.just(books).map(new Func1<List<BookBean>, Boolean>() {
            @Override
            public Boolean call(List<BookBean> books) {
                //从书架数据库删除数据
                if (!CollectionUtils.isEmpty(books))
                    mDao.deleteInTx(books);
                //删除源文件
                for (BookBean bean : books) {
                    File file = new File(bean.getPath());
                    if (file.exists()) file.delete();
                }
                return true;
            }
        }).compose(RxSchedulers.<Boolean>ioMain());
    }


    /**
     * 根据集合批量移出书架
     *
     * @param books
     * @return
     */
    @Override
    public Observable<Boolean> removeBooks(List<BookBean> books) {
        return Observable.just(books).map(new Func1<List<BookBean>, Boolean>() {
            @Override
            public Boolean call(List<BookBean> books) {
                if (!CollectionUtils.isEmpty(books))
                    mDao.deleteInTx(books);
                return true;
            }
        }).compose(RxSchedulers.<Boolean>ioMain());
    }

}
