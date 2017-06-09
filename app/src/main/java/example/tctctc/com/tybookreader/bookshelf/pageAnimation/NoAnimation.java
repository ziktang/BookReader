package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public class NoAnimation extends AnimationProvider{


    public NoAnimation(Bitmap[] bitmaps, int mTotalWidth, int mTotalHeight) {
        super(bitmaps, mTotalWidth, mTotalHeight);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        changePosition(mReadPageView.getCurPosition());
        canvas.drawBitmap(mBitmaps[curPosition], 0, 0, null);
    }

    @Override
    public void drawAnima(Canvas canvas) {
        drawStatic(canvas);
    }

    @Override
    public void startAnimation(Scroller scroller) {

    }

    @Override
    public void cancel(Scroller scroller) {

    }
}
