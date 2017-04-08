package example.tctctc.com.tybookreader.main;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bookshelf.view.ShelfFragment;
import example.tctctc.com.tybookreader.bookstore.view.StoreFragment;
import example.tctctc.com.tybookreader.utils.StatusBarUtils;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_tab)
    TabLayout mTabLayout;

    @BindView(R.id.main_pager)
    ViewPager mPager;

    private List<Fragment> mFragments;
    private MainAdapter mAdapter;

    @Override
    protected void initView(View contextView) {
        StatusBarUtils.setTranslucent(this);
        initData();
        String[] tabs = getResources().getStringArray(R.array.main_tabs);
        mAdapter = new MainAdapter(getSupportFragmentManager(),mFragments,tabs);
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
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
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.currentFragment instanceof ShelfFragment){
            ShelfFragment shelfFragment = (ShelfFragment) mAdapter.currentFragment;
            if (shelfFragment.onBackPressed()){

            }else {
                super.onBackPressed();
            }
        }
    }
}
