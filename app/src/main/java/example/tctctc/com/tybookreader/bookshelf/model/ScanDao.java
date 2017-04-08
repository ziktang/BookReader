package example.tctctc.com.tybookreader.bookshelf.model;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.List;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.BookBeanDao;
import example.tctctc.com.tybookreader.bean.DaoMaster;
import example.tctctc.com.tybookreader.bean.DaoSession;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ScanDao implements ScanContact.Model{

    private int totalNum;
    private static final String dbName = "BookBean";
    private BookBeanDao mDao;

    public ScanDao(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BookApplication.getContext(), dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession session = master.newSession();
        mDao = session.getBookBeanDao();
    }


    /**
     * 批量添加书籍
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
    public Observable<File> scanFile(File file, final String rex) {
        return Observable.just(file).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return listFiles(file, rex);
            }
        }).compose(RxSchedulers.<File>ioMain());
    }

    @Override
    public int getTotalNum() {
        return totalNum;
    }

    public Observable<File> listFiles(File file, final String rex) {
        //记录扫描过的文件数,包括文件夹
        totalNum++;
        //若遇到文件夹，则继续递归扫描
        if (file.isDirectory()){
            if (!file.isHidden())
                return Observable.from(file.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return listFiles(file, rex);
                    }
                });
            else{
                return Observable.just(new File(""));
            }
        }else {
            return Observable.just(file).filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return file.exists() && file.canRead() && file.getName().endsWith(rex) && file.length() > 20 * 1024f;
                }

            });
        }
    }

}
