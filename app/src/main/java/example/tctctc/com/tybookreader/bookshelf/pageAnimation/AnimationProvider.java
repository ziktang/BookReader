package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.Scroller;

import example.tctctc.com.tybookreader.view.ReadPageView;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public abstract class AnimationProvider {

    public int move;
    public int mCurX;
    public int mCurY;
    public int mDownX;
    public int mDownY;

    public int lastPosition;
    public int curPosition;
    public int nextPosition;

    public Bitmap[] mBitmaps;

    public void setTouch(int curX, int curY) {
        mCurX = curX;
        mCurY = curY;

        move = mCurX - mDownX;
        isNext = move < 0;
    }

    public void setDown(int downX, int downY) {
        mDownX = downX;
        mDownY = downY;
    }

    public int mTotalWidth;
    public int mTotalHeight;

    public boolean isNext;

    public ReadPageView mReadPageView;


    public AnimationProvider(Bitmap[] bitmaps, int mTotalWidth, int mTotalHeight) {
        this.mTotalWidth = mTotalWidth;
        this.mTotalHeight = mTotalHeight;
        mBitmaps = bitmaps;
    }


    public void setPageView(ReadPageView pageView) {
        mReadPageView = pageView;
    }

    public abstract void drawStatic(Canvas canvas);

    public abstract void drawAnima(Canvas canvas);

    public abstract void startAnimation(Scroller scroller);

    public abstract void cancel(Scroller scroller);

    public void setOrientation(boolean isNext) {
        this.isNext = isNext;
    }

    public void changePosition(int curPosition) {
        this.curPosition = curPosition;
        lastPosition = curPosition == 0 ? 2 : curPosition - 1;
        nextPosition = curPosition == 2 ? 0 : curPosition + 1;
    }
}
