package example.tctctc.com.tybookreader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import example.tctctc.com.tybookreader.bookshelf.pageAnimation.AnimationProvider;
import example.tctctc.com.tybookreader.bookshelf.pageAnimation.NoAnimation;

/**
 * Created by tctctc on 2017/3/26.
 * Function:
 */

public class ReadPageView extends View {
    private Context mContext;
    private int mTotalWidth;
    private int mTotalHeight;

    private Bitmap mCurrentBitmap;
    private Bitmap mNextBitmap;

    private boolean isAnima;

    private AnimationProvider mAnimationProvider;
    private onTouchListener mTouchListener;

    public ReadPageView(Context context) {
        this(context, null);
    }


    public ReadPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mTotalWidth = metric.widthPixels;
        mTotalHeight = metric.heightPixels;
        mCurrentBitmap = Bitmap.createBitmap(mTotalWidth, mTotalHeight, Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(mTotalWidth, mTotalHeight, Bitmap.Config.RGB_565);

        mAnimationProvider = new NoAnimation(mCurrentBitmap, mNextBitmap, mTotalWidth, mTotalHeight);
    }


//    public void setPageMode(int pageMode){
//        switch (pageMode) {
//            case ReadConfig.TURN_NO:
//                mAnimationProvider = new NoAnimation(mCurrentBitmap,mNextBitmap);
//            case ReadConfig.TURN_COVER:
//                Toast.makeText(getContext(),"覆盖模式",Toast.LENGTH_SHORT).show();
//                mAnimationProvider = new CoverAnimation(mCurrentBitmap,mNextBitmap);
//            case ReadConfig.TURN_SLIDE:
//                Toast.makeText(getContext(),"滑动模式",Toast.LENGTH_SHORT).show();
//                mAnimationProvider = new SlideAnimation(mCurrentBitmap,mNextBitmap);
//            case ReadConfig.TURN_PAPER:
//                Toast.makeText(getContext(),"仿真模式",Toast.LENGTH_SHORT).show();
//                mAnimationProvider = new PaperAnimation(mCurrentBitmap,mNextBitmap);
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (isAnima){
        mAnimationProvider.drawAnima(canvas);
//        }else{
//            mAnimationProvider.drawStatic(canvas);
//        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                isAnima = true;
                break;

            case MotionEvent.ACTION_UP:
                if (x > mTotalWidth * 1 / 5 && x < mTotalWidth * 4 / 5 && y > mTotalHeight * 1 / 3 && y < mTotalHeight * 2 / 3) {
                    mTouchListener.onCenter();
                } else if (x <= mTotalWidth * 1 / 5) {
                    mTouchListener.onPrePage();
                } else if (x >= mTotalWidth * 4 / 5) {
                    mTouchListener.onNextPage();
                }
                break;
        }
        return true;
    }

    public int getTotalWidth() {
        return mTotalWidth;
    }

    public int getTotalHeight() {
        return mTotalHeight;
    }

    public Bitmap getNextBitmap() {
        return mNextBitmap;
    }

    public void setNextBitmap(Bitmap nextBitmap) {
        mNextBitmap = nextBitmap;
    }

    public Bitmap getCurrentBitmap() {
        return mCurrentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        mCurrentBitmap = currentBitmap;
    }

    public void setTouchListener(onTouchListener touchListener) {
        mTouchListener = touchListener;
    }

    public void setAnima(boolean anima) {
        isAnima = anima;
    }

    public boolean isAnima() {
        return isAnima;
    }


    public interface onTouchListener {
        void onCenter();

        boolean onPrePage();

        boolean onNextPage();
    }


}
