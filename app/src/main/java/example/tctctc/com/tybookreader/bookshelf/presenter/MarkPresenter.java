package example.tctctc.com.tybookreader.bookshelf.presenter;


import java.util.List;

import example.tctctc.com.tybookreader.bean.MarkBean;
import example.tctctc.com.tybookreader.bookshelf.contact.MarkContact;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by tctctc on 2017/3/25.
 * Function:
 */

public class MarkPresenter extends MarkContact.Presenter {


    @Override
    public void loadMarkList() {
        mModel.getMarkList().subscribe(new Consumer<List<MarkBean>>() {
            @Override
            public void accept(@NonNull List<MarkBean> list) throws Exception {
                mView.showMarkList(list);
            }
        });
    }

    @Override
    public void itemClick(MarkBean bean) {
        mView.onItemClick(bean);
    }

    @Override
    public void onStart() {

    }
}
