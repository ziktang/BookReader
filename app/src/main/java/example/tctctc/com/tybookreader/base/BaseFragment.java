package example.tctctc.com.tybookreader.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.tctctc.com.tybookreader.common.rx.RxManager;

import static example.tctctc.com.tybookreader.BookApplication.getContext;

/**
 * Created by tctctc on 2016/11/25.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    public RxManager mRxManager;
    private Unbinder mUnbinder;
    private Context mContext;

    public String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        mRxManager = new RxManager();
        mUnbinder = ButterKnife.bind(this, rootView);
        mContext = getContext();
        this.initView();
        return rootView;
    }

    //初始化view
    protected abstract void initView();

    //获取布局文件
    public abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
        mUnbinder.unbind();
        mRxManager.clear();
    }

    public void showToast(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
