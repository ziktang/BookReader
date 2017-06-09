package example.tctctc.com.tybookreader.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.tctctc.com.tybookreader.common.rx.RxManager;

/**
 * Created by tctctc on 2017/4/24.
 * Function:
 */

public abstract class BasePageFragment extends Fragment {

    private boolean isViewCreated;
    private boolean isDataLoad;

    private boolean isLazyLoad = false;

    private View rootView;
    public RxManager mRxManager;
    private Unbinder mUnbinder;
    private Context mContext;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isLazyLoad){
            judgeLoadData();
        }
        Log.d("AAA", "setUserVisibleHint");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        mRxManager = new RxManager();
        mUnbinder = ButterKnife.bind(this, rootView);
        mContext = getContext();
        Log.d("AAA", "onCreateView");
        this.initView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        Log.d("AAA", "onViewCreated");
        judgeLoadData();
    }

    //初始化view
    protected abstract void initView();

    //获取布局文件
    public abstract int getLayoutId();

    private void judgeLoadData() {
        judgeLoadData(false);
    }

    public void judgeLoadData(boolean isForceLoad) {

        if (isLazyLoad) {
            if (getUserVisibleHint() && isViewCreated && (!isDataLoad || isForceLoad)) {
                isDataLoad = fetchData();
            }
        } else {
            if (isViewCreated) {
                isDataLoad = fetchData();
            }
        }
    }

    public abstract boolean fetchData();

    public boolean isLazyLoad() {
        return isLazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        isLazyLoad = lazyLoad;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mRxManager.clear();
    }
}
