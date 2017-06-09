package example.tctctc.com.tybookreader.bookshelf.contact;


import java.util.List;

import example.tctctc.com.tybookreader.base.BaseModel;
import example.tctctc.com.tybookreader.base.BasePresenter;
import example.tctctc.com.tybookreader.base.BaseView;
import example.tctctc.com.tybookreader.bean.MarkBean;
import io.reactivex.Observable;

/**
 * Created by tctctc on 2017/3/24.
 */

public interface MarkContact {
    interface View extends BaseView {
        void showMarkList(List<MarkBean> list);
        void onItemClick(MarkBean bean);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void loadMarkList();
        public abstract void itemClick(MarkBean bean);
    }

    interface Model extends BaseModel {
        Observable<List<MarkBean>> getMarkList();
    }
}
