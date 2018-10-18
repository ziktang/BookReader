package example.tctctc.com.tybookreader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import example.tctctc.com.tybookreader.bookshelf.common.PageManager;
import example.tctctc.com.tybookreader.bookshelf.common.ReadConfig;
import example.tctctc.com.tybookreader.bookshelf.pageAnimation.AnimationProvider;
import example.tctctc.com.tybookreader.bookshelf.pageAnimation.CoverAnimation;
import example.tctctc.com.tybookreader.bookshelf.pageAnimation.NoAnimation;
import example.tctctc.com.tybookreader.bookshelf.pageAnimation.SlideAnimation;

/**
 * Created by tctctc on 2017/3/26.
 * Function:
 */

public class ReadPageView extends View {

    public static final String TAG = "ReadPageView";

    private Bitmap[] mBitmaps;

    private int touchX;
    private int touchY;
    private int downY;
    private int downX;

    private int move;

    private Scroller mScroller;
    private AnimationProvider mAnimationProvider;
    private PageManager mPageManager;
    private onTouchListener mTouchListener;

    private boolean isCancel;
    private boolean isMove;
    public int slop = 10;

    private Rect mCenterRect;
    private Rect mLeftRect;
    private Rect mRightRect;

    private boolean hasNext;
    private boolean hasLast;

    private int curPosition;
    private int mLastMove;
    private boolean isCheckLast;
    private boolean isCheckNext;

    public ReadPageView(Context context) {
        this(context, null);
    }


    public ReadPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
//
//        Field field = null; // 通过View类得到字段，不能通过实例得到字段。
//        field = View.class.getDeclaredField("mTouchSlop");
//        field.setAccessible(true); // 设置Java不检查权限。
//        Log.i(TAG, "mTouchSlop before:" + field.getInt(this));
//        field.setInt(this, slop); // 设置字段的值，设置只有滑动长度大于2px的时候，才进行滑动
//        Log.i(TAG, "mTouchSlop" + field.getInt(this));
    }


    public void setPageMode(int pageMode) {
        switch (pageMode) {
            case ReadConfig.TURN_NO:
                mAnimationProvider = new NoAnimation(mBitmaps, getWidth(), getHeight());
                break;
            case ReadConfig.TURN_COVER:
                mAnimationProvider = new CoverAnimation(mBitmaps, getWidth(), getHeight());
                break;
            case ReadConfig.TURN_SLIDE:
                mAnimationProvider = new SlideAnimation(mBitmaps, getWidth(), getHeight());
                break;
            default:
                mAnimationProvider = new CoverAnimation(mBitmaps, getWidth(), getHeight());
                break;
//            case ReadConfig.TURN_PAPER:
//                Toast.makeText(getContext(), "仿真模式", Toast.LENGTH_SHORT).show();
//                mAnimationProvider = new PaperAnimation(mBitmaps, mTotalWidth, mTotalHeight);
        }
        mAnimationProvider.setPageView(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        log("width:"+width + "  height:" + height );

        Bitmap mCurrentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Bitmap mNextBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Bitmap mLastBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        mBitmaps = new Bitmap[]{mLastBitmap, mCurrentBitmap, mNextBitmap};

        mCenterRect = new Rect(width * 1 / 5, 0, width * 4 / 5, height);
        mLeftRect = new Rect(0, 0, width * 1 / 5, height);
        mRightRect = new Rect(width * 4 / 5, 0, width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        log("width:"+w + "  height:" + h + "  oldw:"+oldw + "  oldh:" + oldh );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        log("width:"+(right - left) + "  height:" + (bottom - top) );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        log("onDraw" );
        if (isMove) {
            mAnimationProvider.drawAnima(canvas);
        } else {
            mAnimationProvider.drawStatic(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPageManager.getReadStatus() != PageManager.ReadStatus.READING || !mPageManager.isReady()) {
            return true;
        }

        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            Log.i(TAG, "isFinished");
            return true;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                isCancel = false;
                isMove = false;
                isCheckNext = false;
                isCheckLast = false;

                mAnimationProvider.changePosition(curPosition);

                mAnimationProvider.setDown(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                touchX = x;
                touchY = y;
                move = touchX - downX;

                if (!isMove) {
                    if (Math.abs(move) >= slop) {
                        isMove = true;
                        mLastMove = move;
                    }
                } else {


                    if (move > 0 && !isCheckLast) {
                        isCheckLast = true;
                        hasLast = mTouchListener.onUpdateNext(false);
                    }

                    if (move < 0 && !isCheckNext) {
                        isCheckNext = true;
                        hasNext = mTouchListener.onUpdateNext(true);
                    }

                    isCancel = Math.abs(mLastMove) > Math.abs(move);

                    //没有上一页或者下一页时，将触摸点设置为down点
                    if ((move > 0 && !hasLast) || (move < 0 && !hasNext)) {
                        isCancel = true;
                        downX = touchX;
                        mAnimationProvider.setDown(touchX, touchY);
                        mAnimationProvider.setTouch(touchX, touchY);
                        return true;
                    }

                    mAnimationProvider.setTouch(touchX, touchY);
                    mLastMove = move;
                    this.postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove) {
                    if (mCenterRect.contains(x, y)) {
                        mTouchListener.onCenter();
                    } else if (mLeftRect.contains(x, y) && mTouchListener.onUpdateNext(false)) {
                        startAnima(false);
                    } else if (mRightRect.contains(x, y) && mTouchListener.onUpdateNext(true)) {
                        startAnima(true);
                    }
                } else {
                    if (isCancel) {
                        mAnimationProvider.cancel(mScroller);
                    } else {
                        mAnimationProvider.startAnimation(mScroller);
                        mPageManager.updateLastNext();
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    private void startAnima(boolean isNext) {
        mPageManager.updateLastNext();
        isMove = true;
        mAnimationProvider.setTouch(downX, downY);
        mAnimationProvider.setOrientation(isNext);
        mAnimationProvider.startAnimation(mScroller);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mAnimationProvider.setTouch(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            if (mScroller.getCurrX() == mScroller.getFinalX()) {
                isMove = false;
            }
        }
    }

    public void log(String s) {
        Log.i(TAG, s);
    }

    public Bitmap[] getBitmaps() {
        return mBitmaps;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    public int getCurPosition() {
        return curPosition;
    }

    public void setPageManager(PageManager pageManager) {
        mPageManager = pageManager;
    }


    public void setTouchListener(onTouchListener touchListener) {
        mTouchListener = touchListener;
    }

    public boolean isAnima() {
        return isMove;
    }

    public interface onTouchListener {
        void onCenter();

        boolean onUpdateNext(boolean isNext);
    }
}
