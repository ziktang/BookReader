package example.tctctc.com.tybookreader.bookshelf.presenter;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanFile;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.bookshelf.importBook.ScanFileComparater;
import example.tctctc.com.tybookreader.utils.CustomUUId;
import example.tctctc.com.tybookreader.utils.FileUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ImportPresenter extends ImportContact.Presenter {

    private List<BookBean> mBooks;

    @Override
    public void onAddBooks(List<ScanFile> scanFiles) {
        List<BookBean> list = new ArrayList<>();
        for (ScanFile scanFile : scanFiles) {
            BookBean bean = new BookBean();
            File file = scanFile.getFile();
            //找出最后一个"."的位置，以此隔离出名字和后缀
            int i = file.getName().lastIndexOf(".");
            bean.setBookId(CustomUUId.get().nextId());
            bean.setBookName(file.getName().substring(0,i));
            bean.setBookType(BookBean.BOOK_TYPE_LOCAL);
            bean.setStatus(BookBean.BOOK_STATUS_IMPORT);
            bean.setFileSize(FileUtils.getFileSize(file));
            bean.setPath(file.getAbsolutePath());
            list.add(bean);
        }

        mModel.addBookList(list).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                mView.toShelf();
            }
        });
    }

    @Override
    public void onGetFileList(File rootFile) {
        List<File> files = mModel.getFileList(mContext,rootFile);
        mBooks = mModel.loadImportedBooks();
        List<ScanFile> scanFiles = new ArrayList<>();
        int importableNum = 0;
        for (File file : files) {
            ScanFile scanFile = new ScanFile(file,false);
            scanFile.setChecked(false);
            if (file.isFile()){
                for(BookBean bookBean:mBooks){
                    if(bookBean.getPath().equals(file.getAbsolutePath())){
                        scanFile.setImported(true);
                    }
                }
                if(!scanFile.isImported()){
                    ++importableNum;
                }
            }
            scanFiles.add(scanFile);
        }
        Collections.sort(scanFiles,new ScanFileComparater());
        mView.showScanBookList(scanFiles,importableNum);
    }

    @Override
    public void onStart() {
        mContext = mView.getContext();
    }
}
