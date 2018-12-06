package example.tctctc.com.tybookreader.main;

import android.app.Dialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bookshelf.view.ShelfFragment;
import example.tctctc.com.tybookreader.bookstore.view.StoreFragment;
import example.tctctc.com.tybookreader.common.barlibrary.ImmersionBar;
import example.tctctc.com.tybookreader.common.barlibrary.ImmersionBarUtil;
import example.tctctc.com.tybookreader.utils.DialogUtil;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_tab)
    TabLayout mTabLayout;

    @BindView(R.id.main_pager)
    ViewPager mPager;

    private List<Fragment> mFragments;
    private MainAdapter mAdapter;
    private Dialog permissionDialog;

    @Override
    protected void initView(View contextView) {
        initImmersionBar();
        initData();
        String[] tabs = getResources().getStringArray(R.array.main_tabs);
        mAdapter = new MainAdapter(getSupportFragmentManager(),mFragments,tabs);
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);

    }

    /**
     * 初始化沉浸式状态栏
     */
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        ImmersionBar mImmersionBar = ImmersionBar.with(this);

        if (ImmersionBarUtil.isSupportWhiteStatusBarDevice()) {
            mImmersionBar.statusBarDarkFont(true, 0.2f);
        } else {
            mImmersionBar.statusBarColor(R.color.white);
        }
        mImmersionBar.init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParams() {}

    @Override
    public void onViewClick(View view) {

    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new ShelfFragment());
        mFragments.add(new StoreFragment());
        checkPermission();
    }

    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                if (permissionDialog == null){
                    permissionDialog = DialogUtil.makeRemindInfoOneBtnDialog(MainActivity.this, "温馨提示", "为保证App的正常使用，需要存储相关的权限", "我知道了", new DialogUtil.RemindInfoOneBtnCallBack() {
                        @Override
                        public void click() {
                            AndPermission.with(MainActivity.this).runtime().setting().start();
                            MainActivity.this.finish();
                        }
                    });
                    permissionDialog.setCancelable(false);
                }

                if (!permissionDialog.isShowing()){
                    permissionDialog.show();
                }
            }
        }).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                if (permissionDialog!=null&&permissionDialog.isShowing()){
                    permissionDialog.dismiss();
                }
            }
        }).start();
    }
}
