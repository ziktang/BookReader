package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public class PaperAnimation extends AnimationProvider {


    public PaperAnimation(Bitmap currentBitmap, Bitmap nextBitmap, int mTotalWidth, int mTotalHeight) {
        super(currentBitmap, nextBitmap, mTotalWidth, mTotalHeight);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (cancel){
            canvas.drawBitmap(mCurrentBitmap,0,0,null);
        }else{
            canvas.drawBitmap(mNextBitmap,0,0,null);
        }
    }

    @Override
    public void drawAnima(Canvas canvas) {

    }
}
