package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import static android.R.attr.duration;
import static android.R.attr.orientation;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public class CoverAnimation extends AnimationProvider {


    public static final String TAG = "CoverAnimation";
    private int border;

    private GradientDrawable mGradientDrawable;
    private int[] mBackShadowColors;

    private static final int DURATION = 450;

    public static final int SHADOW_WIDTH = 30;

    public CoverAnimation(Bitmap[] bitmaps, int mTotalWidth, int mTotalHeight) {
        super(bitmaps, mTotalWidth, mTotalHeight);

        mBackShadowColors = new int[]{0x66000000, 0x00000000};
        mGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mGradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        changePosition(mReadPageView.getCurPosition());
        canvas.drawBitmap(mBitmaps[curPosition], 0, 0, null);
    }

    @Override
    public void drawAnima(Canvas canvas) {
        //从左向右滑，向前翻页.mCurrentBitmap不动，mLastBitmap向右滑入
        if (!isNext) {
            border = move;

            canvas.save();
            canvas.clipRect(move, 0, mTotalWidth, mTotalHeight);
            canvas.drawBitmap(mBitmaps[curPosition], 0, 0, null);
            canvas.restore();

            canvas.save();
            canvas.clipRect(0, 0, move, mTotalHeight);
            canvas.drawBitmap(mBitmaps[lastPosition], move - mTotalWidth, 0, null);
            canvas.restore();

        }
        //从右向左滑，向后翻页.mNextBitmap不动， mCurrentBitmap向左滑出
        else {
            border = mTotalWidth + move;

            canvas.save();
            canvas.clipRect(mTotalWidth + move, 0, mTotalWidth, mTotalHeight);
            canvas.drawBitmap(mBitmaps[nextPosition], 0, 0, null);
            canvas.restore();

            canvas.save();
            canvas.clipRect(0, 0, mTotalWidth + move, mTotalHeight);
            canvas.drawBitmap(mBitmaps[curPosition], move, 0, null);
            canvas.restore();
        }
        drawShader(canvas, border, border + SHADOW_WIDTH);
    }

    @Override
    public void startAnimation(Scroller scroller) {
        if (!isNext) {
            scroller.startScroll(mCurX, 0, mTotalWidth - move, mTotalHeight, DURATION);
        } else {
            scroller.startScroll(mCurX, 0, -(mTotalWidth + move), mTotalHeight, DURATION);
        }
    }

    @Override
    public void cancel(Scroller scroller) {
        scroller.startScroll(mCurX, 0, -move, mTotalHeight, DURATION);
    }

    private void drawShader(Canvas canvas, int left, int right) {
        mGradientDrawable.setBounds(left, 0, right, mTotalHeight);
        mGradientDrawable.draw(canvas);
    }
}
