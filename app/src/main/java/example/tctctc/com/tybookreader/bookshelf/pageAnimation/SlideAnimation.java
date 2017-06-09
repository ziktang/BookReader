package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public class SlideAnimation extends AnimationProvider {

    public static final String TAG = "SlideAnimation";

    //绘制的bitmap区域
    private Rect mCurSrcRect;
    //将bitmap绘制到什么区域
    private Rect mCurDstRect;

    //绘制的bitmap区域
    private Rect mNextSrcRect;
    //将bitmap绘制到什么区域
    private Rect mNextDstRect;

    public SlideAnimation(Bitmap[] bitmaps, int mTotalWidth, int mTotalHeight) {
        super(bitmaps, mTotalWidth, mTotalHeight);
        mCurSrcRect = new Rect(0, 0, 0, mTotalHeight);
        mCurDstRect = new Rect(0, 0, 0, mTotalHeight);

        mNextSrcRect = new Rect(0, 0, 0, mTotalHeight);
        mNextDstRect = new Rect(0, 0, 0, mTotalHeight);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        changePosition(mReadPageView.getCurPosition());
        canvas.drawBitmap(mBitmaps[curPosition], 0, 0, null);
    }

    @Override
    public void drawAnima(Canvas canvas) {

        //从左向右滑，向前翻页
        if (!isNext) {

            canvas.save();
            canvas.clipRect(move, 0, mTotalWidth, mTotalHeight);
            canvas.drawBitmap(mBitmaps[curPosition], move, 0, null);
            canvas.restore();

            canvas.save();
            canvas.clipRect(0, 0, move, mTotalHeight);
            canvas.drawBitmap(mBitmaps[lastPosition], move - mTotalWidth, 0, null);
            canvas.restore();
//            mCurSrcRect.left = 0;
//            mCurSrcRect.right = mTotalWidth - move;
//
//            mCurDstRect.left = move;
//            mCurDstRect.right = mTotalWidth;
//
//
//            mNextSrcRect.left = mTotalWidth - move;
//            mNextSrcRect.right = mTotalWidth;
//
//            mNextDstRect.left = 0;
//            mNextDstRect.right = move;
//            canvas.drawBitmap(mBitmaps[curPosition], mCurSrcRect, mCurDstRect, null);
//            canvas.drawBitmap(mBitmaps[lastPosition], mNextSrcRect, mNextDstRect, null);
        }
        //从右向左滑，向后翻页
        else {

            canvas.save();
            canvas.clipRect(mTotalWidth + move, 0, mTotalWidth, mTotalHeight);
            canvas.drawBitmap(mBitmaps[nextPosition], mTotalWidth + move, 0, null);
            canvas.restore();

            canvas.save();
            canvas.clipRect(0, 0, mTotalWidth + move, mTotalHeight);
            canvas.drawBitmap(mBitmaps[curPosition], move, 0, null);
            canvas.restore();
//            mCurSrcRect.left = -move;
//            mCurSrcRect.right = mTotalWidth;
//
//            mCurDstRect.left = 0;
//            mCurDstRect.right = mTotalWidth + move;
//
//            mNextSrcRect.left = 0;
//            mNextSrcRect.right = -move;
//
//            mNextDstRect.left = mTotalWidth + move;
//            mNextDstRect.right = mTotalWidth;
//
//            canvas.drawBitmap(mBitmaps[curPosition], mCurSrcRect, mCurDstRect, null);
//            canvas.drawBitmap(mBitmaps[nextPosition], mNextSrcRect, mNextDstRect, null);
        }
    }


    @Override
    public void startAnimation(Scroller scroller) {
        if (!isNext) {
            scroller.startScroll(mCurX, 0, mTotalWidth - move, mTotalHeight, 450);
        } else {
            scroller.startScroll(mCurX, 0, -(mTotalWidth + move), mTotalHeight, 450);
        }
    }

    @Override
    public void cancel(Scroller scroller) {
        scroller.startScroll(mCurX, 0, -move, mTotalHeight, 450);
    }
}
