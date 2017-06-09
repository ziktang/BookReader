package example.tctctc.com.tybookreader.bookshelf.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.Collections;
import java.util.List;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.BookBeanDao;
import example.tctctc.com.tybookreader.bean.DaoMaster;
import example.tctctc.com.tybookreader.bean.DaoSession;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.bookshelf.contact.ShelfContact;
import example.tctctc.com.tybookreader.common.FileComparater;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import example.tctctc.com.tybookreader.utils.CollectionUtils;
import example.tctctc.com.tybookreader.utils.FileUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static android.content.ContentValues.TAG;

/**
 * Created by tctctc on 2017/4/3.
 * Function:
 */

public class BookDao implements ShelfContact.Model, ScanContact.Model, ImportContact.Model {

    private static final String dbName = "Book";
    private static BookBeanDao sDao;
    private int totalNum;
    private File mFile = new File("");
    public BookDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BookApplication.getContext(), dbName);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession session = master.newSession();
        sDao = session.getBookBeanDao();
//        sDao = getDao();
    }

//    public static BookBeanDao getDao() {
//        if (sDao == null) {
//
//        }
//        return sDao;
//    }

    public List<BookBean> loadAll(){
        QueryBuilder queryBuilder = sDao.queryBuilder();
        return queryBuilder.build().listLazy();
    }

    @Override
    public List<File> getFileList(File rootFile) {
        List<File> files = FileUtils.listFilterOneFolder(rootFile, ".txt");
        return files;
    }


    /**
     * 批量添加书籍
     *
     * @param books
     * @return
     */
    @Override
    public Observable<Boolean> addBookList(final List<BookBean> books) {

        return Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                sDao.insertInTx(books);
                Log.i("AAA","------------33333333333333333333333");
                e.onNext(true);
            }
        }).compose(RxSchedulers.<Boolean>ioMain());
    }

    @Override
    public Flowable<File> scanFile(final File file, final String rex) {
        return Flowable.create(new FlowableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<File> emitter) throws Exception {
                listFiles(file, rex, emitter);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulers.<File>ioMainFlow());
    }

    @Override
    public int getTotalNum() {
        return totalNum;
    }

    @Override
    public Observable<List<BookBean>> loadBookList() {
        return Observable.just("all").map(new Function<String, List<BookBean>>() {
            @Override
            public List<BookBean> apply(@NonNull String s) throws Exception {
                QueryBuilder queryBuilder = sDao.queryBuilder();
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
        return Observable.just(books).map(new Function<List<BookBean>, Boolean>() {
            @Override
            public Boolean apply(@NonNull List<BookBean> books) throws Exception {
                //从书架数据库删除数据
                if (!CollectionUtils.isEmpty(books))
                    sDao.deleteInTx(books);
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
        return Observable.just(books).map(new Function<List<BookBean>, Boolean>() {
            @Override
            public Boolean apply(@NonNull List<BookBean> books) throws Exception {
                if (!CollectionUtils.isEmpty(books))
                    sDao.deleteInTx(books);
                return true;
            }
        }).compose(RxSchedulers.<Boolean>ioMain());
    }

    public void listFiles(File file, final String rex, @NonNull FlowableEmitter<File> emitter) {
        File[] files = file.listFiles();
        for (File f : files) {
            totalNum++;
            if (f.isFile() && f.getName().endsWith(rex) && f.length() > 20 * 1024f) {
                emitter.onNext(f);
            } else if (f.isDirectory() && !f.isHidden()) {
                listFiles(f, rex, emitter);
            }
            if (f.isDirectory()){
//                Log.d("AAA","name:"+f.getName()+"----isHidden:"+f.isHidden()+"---length:"+f.length());
            }
        }
    }

    public static void update(BookBean bookBean) {
        sDao.update(bookBean);
    }
}
