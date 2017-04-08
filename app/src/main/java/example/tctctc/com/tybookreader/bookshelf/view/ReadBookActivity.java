package example.tctctc.com.tybookreader.bookshelf.view;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.common.DialogManager2;
import example.tctctc.com.tybookreader.bookshelf.common.PageManager;
import example.tctctc.com.tybookreader.bookshelf.common.ReadConfig;
import example.tctctc.com.tybookreader.utils.StatusBarUtils;
import example.tctctc.com.tybookreader.view.CustomDrawerLayout;
import example.tctctc.com.tybookreader.view.ReadPageView;
import rx.functions.Action1;

public class ReadBookActivity extends BaseActivity implements ReadPageView.onTouchListener {

    private DialogManager2 mDialogManager;
    private PageManager mPageManager;

    private ReadConfig mConfig;

    @BindView(R.id.readPageView)
    ReadPageView mReadPageView;

    @BindView(R.id.drawer)
    CustomDrawerLayout mDrawer;

    @BindView(R.id.dialog)
    RelativeLayout mView;


    //系统状态栏是否显示
    private boolean isShow;
    private BookBean mBookBean;

    @Override
    protected void initView(View contextView) {

        StatusBarUtils.setTranslucent(this);
        mBookBean = (BookBean) getIntent().getSerializableExtra("book");

        mReadPageView.setTouchListener(this);
        mConfig = new ReadConfig(this);

        mDialogManager = DialogManager2.getInstance(this, mBookBean, mView);
        mPageManager = PageManager.getInstance(this, mBookBean);
        mDialogManager.setPageManager(mPageManager);

        mPageManager.setReadPageView(mReadPageView);
        mPageManager.openBook();

        mDrawer = (CustomDrawerLayout) findViewById(R.id.drawer);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDrawer.closeDrawers();
                }
                return false;
            }
        });

        mRxManager.onEvent("close drawer", new Action1<String>() {
            @Override
            public void call(String s) {
                mDrawer.closeDrawers();
            }
        });

        mRxManager.onEvent("open drawer", new Action1<Object>() {
            @Override
            public void call(Object o) {
                mDrawer.openDrawer(GravityCompat.START);
                onCenter();
            }
        });

        mRxManager.onEvent("onPageSelected", new Action1<Boolean>() {
            @Override
            public void call(Boolean isLast) {
                if (isLast) {
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else if (mDrawer.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_UNLOCKED) {
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_book;
    }

    @Override
    protected void initParams() {

    }

    @Override
    public void onViewClick(View view) {

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            StatusBarUtils.setFullScreen(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConfig.saveConfig();
    }


    @Override
    public void onCenter() {
        if (!isShow) {
            mDialogManager.showBottomDialog();
            StatusBarUtils.setTranslucent(this);
            isShow = true;
        } else {
            mDialogManager.hideBottomDialog();
            StatusBarUtils.setFullScreen(this);
            isShow = false;
        }
    }

    @Override
    public boolean onPrePage() {
        mPageManager.lastPage();
        return true;
    }

    @Override
    public boolean onNextPage() {
        mPageManager.nextPage();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialogManager.destroy();
        mPageManager.destroy();
    }
}
