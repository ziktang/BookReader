package example.tctctc.com.tybookreader.bookshelf.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.adapter.DirectoryPageAdapter;
import example.tctctc.com.tybookreader.bookshelf.common.DialogManager;
import example.tctctc.com.tybookreader.bookshelf.common.PageManager;
import example.tctctc.com.tybookreader.bookshelf.common.ReadConfig;
import example.tctctc.com.tybookreader.common.barlibrary.BarHide;
import example.tctctc.com.tybookreader.common.barlibrary.ImmersionBar;
import example.tctctc.com.tybookreader.utils.FontUtils;
import example.tctctc.com.tybookreader.utils.StatusBarUtils;
import example.tctctc.com.tybookreader.view.CustomDrawerLayout;
import example.tctctc.com.tybookreader.view.ReadPageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ReadBookActivity extends BaseActivity implements ReadPageView.onTouchListener {

    public static final String TAG = "ReadBookActivity";

    private DialogManager mDialogManager;
    private PageManager mPageManager;

    private ReadConfig mConfig;

    @BindView(R.id.readPageView)
    ReadPageView mReadPageView;

    @BindView(R.id.drawer)
    CustomDrawerLayout mDrawer;

    @BindView(R.id.dialog)
    RelativeLayout mView;


    @BindView(R.id.directory_pager)
    ViewPager mViewPager;

    @BindView(R.id.directory_tab)
    TabLayout mTabLayout;

    private List<Fragment> mFragmentList = new ArrayList();

    //系统状态栏是否显示
    private boolean isShow;
    private BookBean mBookBean;

    @BindView(R.id.back)
    TextView mBackTv;
    @BindView(R.id.search)
    TextView mSearchTv;
    @BindView(R.id.mark)
    TextView mMarkTv;

    @BindView(R.id.titleBar)
    RelativeLayout mTitleBar;

    @Override
    protected void initView(View contextView) {
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");

        mBookBean = (BookBean) getIntent().getSerializableExtra("book");

        mConfig = new ReadConfig(this);

        mDialogManager = DialogManager.getInstance(this, mBookBean, mView);
        mPageManager = PageManager.getInstance(this, mBookBean);
        mDialogManager.setPageManager(mPageManager);


        mReadPageView.post(new Runnable() {
            @Override
            public void run() {
                mReadPageView.setTouchListener(ReadBookActivity.this);
                mReadPageView.setPageMode(mConfig.getPageTurnType());
                mPageManager.setReadPageView(mReadPageView);
                mPageManager.openBook();
            }
        });
        initDrawer();
        initTopDialog();

        mRxManager.onEvent("close drawer", new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) {
                mDrawer.closeDrawers();
            }
        });

        mRxManager.onEvent("open drawer", new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) {
                mDrawer.openDrawer(GravityCompat.START);
                onCenter();
            }
        });

        mRxManager.onEvent("pageTurn", new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer type) throws Exception {
                mReadPageView.setPageMode(type);
            }
        });
    }

    private void initDrawer() {
        mDrawer = (CustomDrawerLayout) findViewById(R.id.drawer);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                mRxManager.post("updateMark",1);
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


        mFragmentList.add(new DirectoryListFragment());
        mFragmentList.add(MarkListFragment.getInstance(mBookBean));
        mFragmentList.add(NoteListFragment.getInstance(mBookBean));
        String[] titles = getResources().getStringArray(R.array.read_drawer);
        final DirectoryPageAdapter adapter = new DirectoryPageAdapter(getSupportFragmentManager(), mFragmentList, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mFragmentList.size() - 1) {
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_book;
    }

    @Override
    protected void initParams() {
        ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

    @Override
    public void onViewClick(View view) {

    }

    public void initTopDialog() {
        int height = StatusBarUtils.getStatusBarHeight(this);
        mTitleBar.setPadding(0, height, 0, 0);
        mBackTv.setTypeface(FontUtils.getIconfont());
        mSearchTv.setTypeface(FontUtils.getIconfont());
        mMarkTv.setTypeface(FontUtils.getIconfont());
    }

    public void showTopView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTitleBar, "y", -mTitleBar.getHeight(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mTitleBar.setTranslationY(value);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTitleBar.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }


    public void hideTopView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTitleBar, "y", 0, -mTitleBar.getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mTitleBar.setTranslationY(value);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTitleBar.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }


    @OnClick({R.id.back, R.id.search, R.id.mark})
    public void onTopClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search:
                break;
            case R.id.mark:
                isMark(mPageManager.bookMark());
                break;
        }

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
        super.onPause();
        mConfig.saveConfig();
    }


    @Override
    public void onCenter() {
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
        if (!isShow) {
            showTopView();
            mDialogManager.showBottomDialog();
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).fullScreen(false).init();
            isMark(mPageManager.isMark());
            isShow = true;
        } else {
            hideTopView();
            mDialogManager.hideBottomDialog();
            ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();
            isShow = false;
        }
    }

    private void isMark(Boolean isMark) {
        if (isMark) {
            mMarkTv.setTextColor(Color.RED);
        } else {
            mMarkTv.setTextColor(getResources().getColor(R.color.icon_white));
        }
    }

    @Override
    public boolean onUpdateNext(boolean isNext) {
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
        if (!isShow) {
            return mPageManager.updateNextPage(isNext);
        } else {
            hideTopView();
            mDialogManager.hideBottomDialog();
            ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();
            isShow = false;
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
        super.onDestroy();
        mDialogManager.destroy();
        mPageManager.destroy();
    }
}
