package example.tctctc.com.tybookreader.bookshelf.presenter;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.utils.CustomUUId;
import example.tctctc.com.tybookreader.utils.FileUtils;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class ImportPresenter extends ImportContact.Presenter {

    @Override
    public void onAddBooks(List<File> files) {
        List<BookBean> list = new ArrayList<>();
        for (File file : files) {
            BookBean bean = new BookBean();
            //找出最后一个"."的位置，以此隔离出名字和后缀
            int i = file.getName().lastIndexOf(".");
            bean.setBookName(file.getName().substring(0,i));
            bean.setBookType(1);
            bean.setFileSize(FileUtils.getFileSize(file));
            bean.setLength(file.length());
            bean.setPath(file.getAbsolutePath());
            list.add(bean);
        }
        mModel.addBookList(list).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mView.toShelf();
            }
        });
    }

    @Override
    public void onGetFileList(File file) {
        mView.showFileList(mModel.getFileList(file));
    }

    @Override
    public void onStart() {

    }
}
