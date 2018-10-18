package example.tctctc.com.tybookreader.bookshelf.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.greenrobot.greendao.query.QueryBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.app.Constant;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.BookBeanDao;
import example.tctctc.com.tybookreader.bean.DaoMaster;
import example.tctctc.com.tybookreader.bean.DaoSession;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.bookshelf.contact.ShelfContact;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import example.tctctc.com.tybookreader.utils.CollectionUtils;
import example.tctctc.com.tybookreader.utils.FileUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tctctc on 2017/4/3.
 * Function:
 */

public class BookDao implements ShelfContact.Model, ScanContact.Model, ImportContact.Model {

    private static final String dbName = "Book";
    private static BookBeanDao sDao;
    public BookDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BookApplication.getContext(), dbName);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession session = master.newSession();
        sDao = session.getBookBeanDao();
    }

    /**
     * 获取所有书，包括已移除出书架但数据库有数据记录的
     * @return
     */
    public List<BookBean> loadAll(){
        QueryBuilder queryBuilder = sDao.queryBuilder();
        return queryBuilder.build().listLazy();
    }

    @Override
    public List<File> getFileList(Context context, File rootFile) {
        List<File> files = FileUtils.listFilterOneFolder(rootFile, context.getString(R.string.file_type_txt), Constant.BookShelf.SCAN_BOOK_SIZE);
        return files;
    }

    /**
     * 获取已导入到书架的书
     * @return
     */
    @Override
    public List<BookBean> loadImportedBooks() {
        List<BookBean> bookBeans = new ArrayList<>();
        bookBeans.addAll(loadAll());
        Iterator<BookBean> iterator = bookBeans.iterator();
        while (iterator.hasNext()){
            BookBean bookBean = iterator.next();
            if (bookBean.getStatus() != BookBean.BOOK_STATUS_IMPORT){
                iterator.remove();
            }
        }
        return bookBeans;
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
    public Observable<File> scanFile(final File file, final String rex) {
        return Observable.create(new ObservableOnSubscribe<File>() {

            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                listFiles(file,emitter);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<BookBean>> loadImportedBooksAsyn() {
        return Observable.create(new ObservableOnSubscribe<List<BookBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookBean>> observableEmitter) throws Exception {
                observableEmitter.onNext(loadImportedBooks());
                observableEmitter.onComplete();
            }
        }).compose(RxSchedulers.<List<BookBean>>ioMain());
    }

    /**
     * 根据集合批量移出书架且删除源文件
     *
     * @param books
     * @return
     */
    public Observable<Boolean> removeDeleteBooks(List<BookBean> books) {
        return Observable.just(books).map(new Function<List<BookBean>, Boolean>() {
            @Override
            public Boolean apply(@NonNull List<BookBean> books) throws Exception {
                //从书架数据库删除数据
                if (!CollectionUtils.isEmpty(books)){
                    sDao.deleteInTx(books);
                }
                //删除源文件
                for (BookBean bean : books) {
                    File file = new File(bean.getPath());
                    if (file.exists()){
                        file.delete();
                    }
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
    public Observable<Boolean> removeBooks(List<BookBean> books) {
        return Observable.just(books).map(new Function<List<BookBean>, Boolean>() {
            @Override
            public Boolean apply(@NonNull List<BookBean> books) throws Exception {
                //仅仅移除出书架，设置为移除状态
                if (!CollectionUtils.isEmpty(books)){
                    for (BookBean bookBean:books){
                        bookBean.setStatus(BookBean.BOOK_STATUS_REMOVE);
                    }
                    sDao.updateInTx(books);
                }
                return true;
            }
        }).compose(RxSchedulers.<Boolean>ioMain());
    }

    @Override
    public void removeBook(long bookId) {
        sDao.deleteByKey(bookId);
    }

    public void listFiles(File file, @NonNull ObservableEmitter<File> emitter) {
        if (file == null || !file.exists() || file.isFile()){
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(Constant.BookShelf.FILE_TYPE_TXT) && f.length() > Constant.BookShelf.SCAN_BOOK_SIZE) {
                emitter.onNext(f);
            } else if (f.isDirectory() && !f.isHidden()) {
                listFiles(f,emitter);
            }
        }
    }

    public static void update(BookBean bookBean) {
        sDao.update(bookBean);
    }
}
