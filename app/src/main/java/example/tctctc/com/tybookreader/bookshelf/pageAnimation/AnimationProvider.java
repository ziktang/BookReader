package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public abstract class AnimationProvider {

    public boolean cancel = true;

    public Bitmap mCurrentBitmap;
    public Bitmap mNextBitmap;

    public int mTotalWidth;
    public int mTotalHeight;


    public AnimationProvider(Bitmap currentBitmap,Bitmap nextBitmap,int mTotalWidth,int mTotalHeight){
        mCurrentBitmap = currentBitmap;
        mNextBitmap = nextBitmap;
        this.mTotalWidth = mTotalWidth;
        this.mTotalHeight = mTotalHeight;
    }

    public abstract void drawStatic(Canvas canvas);
    public abstract void drawAnima(Canvas canvas);

}
