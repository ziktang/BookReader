package example.tctctc.com.tybookreader.bookshelf.pageAnimation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.List;

import example.tctctc.com.tybookreader.view.ReadPageView;
import hugo.weaving.DebugLog;

import static android.R.attr.x;
import static android.R.attr.y;
import static example.tctctc.com.tybookreader.R.id.set;

/**
 * Created by tctctc on 2017/3/31.
 * Function:
 */

public class PaperAnimation{
//    private ReadPageView mPageView;
//
//    public static final String TAG = "PaperAnimation";
//
//    //扭曲网格数量
//    public int SUB_WIDTH = 19, SUB_HEIGHT = 19;
//    //view宽度
//    private float mViewWidth;
//
//    //每个网格的宽高尺寸
//    private float mSubMinWidth;
//    private float mSubMinHeight;
//
//    //扭曲数组
//    private float[] mVerts;
//
//    //文字画笔
//    private Paint mTextPaint;
//
//    //view高度
//    private float mViewHeight;
//
//    private Path mPathMoonBtm;
//    //月牙path
//    private Path mPathMoonRight;
//
//    //替补path
//    private Path mPathAdd;
//
//    private Path mPathBorder;
//    //重叠path
//    private Path mPathFold;
//    //下一页path
//    private Path mPathFoldAndNext;
//    //位置限制圆
//    private Region mRegionCircle;
//
//    //月牙区域
//    private Region mRegionMoon;
//    //替补区域
//    private Region mRegionAdd;
//    //重叠区域
//    private Region mRegionFold;
//    //下一页区域
//    private Region mRegionFoldAndNext;
//    //当前页区域
//    private Region mRegionCurrent;
//
//    //移动时需要的计算量
//    private float mL;
//    //移动时需要的计算量
//    private float mK;
//
//    //重叠区域页面偏离角度
//    private float mDegree;
//    //重叠区域页面偏离方向
//    private Ratio mRatio;
//
//    //避免精度损失的附加值
//    private float mValueAdded;
//
//    //自动翻页延迟
//    private long delayMillis = 25;
//
//    //模拟页面被抬起的高度的一个比例
//    public float PAGER_HEIGHT = 1 / 4f;
//
//    public float SHOWDOW_WIDTH = 1 / 25f;
//    public float SHOWDOW_WIDTH_NEXT = 1 / 8f;
//
//    public int showdowWidth;
//    public int showdowNextWidth;
//
//    //滑动Handler
////    private SlideHandler mHandler = new SlideHandler();
//
//    //底部贝塞尔曲线起始点，弧线中间的顶点，结束点
//    private PointF mBtmStart, mBtmCenter, mBtmEnd;
//    //右部贝塞尔曲线起始点，弧线中间的顶点，结束点
//    private PointF mRightStart, mRightCenter, mRightEnd;
//    private int[] mBackShadowColors;
//    private GradientDrawable mGradientDrawable;
//    private float sizeShort;
//    private float sizeLong;
//    private int[] mBackShadowLightColors;
//    private GradientDrawable mGradientShort;
//    private GradientDrawable mGradientLong;
//
//    private boolean isSlide;
//    private boolean isCheck;
//
//    private int mStartX;
//    private int mStartY;
//
//    private Bitmap mFirstBitmap, mSecondBitmap;
//
//    public PaperAnimation(Bitmap[] bitmaps, int mTotalWidth, int mTotalHeight) {
//        super(bitmaps, mTotalWidth, mTotalHeight);
//        init();
//    }
//
//
//    /**
//     * 计算计算页脚限制区域
//     */
//    private void computeShortSizeRegion() {
//        Path pathShortSize = new Path();
//        RectF rectF = new RectF();
//
////        pathShortSize.addCircle(mViewWidth * 1 / 4f, mViewHeight, mViewWidth * 3 / 4f, Path.Direction.CCW);
////        RectF rectF = new RectF(-mViewWidth/2f,mViewHeight - mViewWidth*3/4,mViewWidth,mViewHeight+mViewWidth*3/4f);
////        pathShortSize.addArc(rectF,0,-90);
////        pathShortSize.moveTo(mViewWidth * 1 / 4f, mViewHeight - mViewWidth * 3 / 4f);
////        pathShortSize.quadTo(mViewWidth, mViewHeight - mViewWidth * 3 / 4f, mViewWidth, mViewHeight);
////        pathShortSize.lineTo(mViewWidth * 1 / 4f, mViewHeight);
////        pathShortSize.close();
//        pathShortSize.addCircle(mViewWidth / 4f, mViewHeight, mViewWidth * 3 / 4f, Path.Direction.CCW);
//
//        pathShortSize.computeBounds(rectF, true);
//        mRegionCircle.setPath(pathShortSize, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
//    }
//
//    /**
//     * 重置path
//     */
//    private void pathReset() {
//        mPathFold.reset();
//        mPathFoldAndNext.reset();
//        mPathMoonRight.reset();
//        mPathMoonBtm.reset();
//        mPathAdd.reset();
//    }
//
//
//    /**
//     * 根据path得出Region
//     *
//     * @param path
//     * @return
//     */
//    public Region computeRegion(Path path) {
//        Region region = new Region();
//        RectF rectF = new RectF();
//
//        path.computeBounds(rectF, false);
//        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
//        return region;
//    }
//
//    @DebugLog
//    private void init() {
//        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setColor(0x66000000);
//        mTextPaint.setStrokeWidth(1);
//        mTextPaint.setStyle(Paint.Style.STROKE);
//
//        mPathMoonBtm = new Path();
//        mPathMoonRight = new Path();
//        mPathAdd = new Path();
//        mPathFold = new Path();
//        mPathFoldAndNext = new Path();
//        mPathBorder = new Path();
//
//        mRegionAdd = new Region();
//        mRegionMoon = new Region();
//        mRegionCircle = new Region();
//        mRegionFold = new Region();
//        mRegionFoldAndNext = new Region();
//        mRegionCurrent = new Region();
//
//
//        mBtmStart = new PointF();
//        mBtmCenter = new PointF();
//        mBtmEnd = new PointF();
//
//        mRightStart = new PointF();
//        mRightCenter = new PointF();
//        mRightEnd = new PointF();
//
//        //网格数组初始化
//        mVerts = new float[(SUB_WIDTH + 1) * (SUB_HEIGHT + 1) * 2];
//
//
//        mViewWidth = mTotalWidth;
//        mViewHeight = mTotalHeight;
//
//        showdowWidth = (int) (mViewWidth * SHOWDOW_WIDTH);
//        showdowNextWidth = (int) (mViewWidth * SHOWDOW_WIDTH_NEXT);
//
//        //计算页脚限制区域
//        computeShortSizeRegion();
//
//        //当前页区域
//        mRegionCurrent.set(0, 0, (int) mViewWidth, (int) mViewHeight);
//
//        //当前页脚位置初始化
////        mCurX = mViewWidth;
////        mCurY = mViewHeight;
//
//        //精度附加值
//        mValueAdded = 1 / 400f * mViewHeight;
//
//        //网格最小单元宽高尺寸
//        mSubMinWidth = mViewWidth / (SUB_WIDTH + 1);
//        mSubMinHeight = mViewHeight / (SUB_HEIGHT + 1);
//
//        mBackShadowColors = new int[]{0x66000000, 0x00000000};
//        mBackShadowLightColors = new int[]{0x33000000, 0x00000000};
//        mGradientDrawable = new GradientDrawable();
//        mGradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//
//        mGradientShort = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mBackShadowColors);
//        mGradientShort.setBounds(0, 0, (int) mViewHeight * 2, showdowNextWidth);
//        mGradientShort.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//
//        mGradientLong = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
//        mGradientLong.setBounds(0, 0, showdowNextWidth, (int) mViewWidth * 2);
//        mGradientLong.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//    }
//
//    private void calVerts(float sizeLong, float sizeShort, float btmControlX, float rightControlY) {
//        //扭曲的开始位置
//        int mSubWidthStart = Math.round(btmControlX / mSubMinWidth) - 1;
//        //扭曲的结束位置
//        int mSubWidthEnd = Math.round(((btmControlX + PAGER_HEIGHT * sizeShort) / mSubMinWidth)) + 1;
//
//        int mSubHeightStart = (int) (rightControlY / mSubMinHeight) - 1;
//        int mSubHeightEnd = (int) (rightControlY + PAGER_HEIGHT * sizeLong / mSubMinHeight) + 1;
//
//
//        //扭曲的偏移值
//        float mOffsetLong = PAGER_HEIGHT / 2F * sizeLong;
//        //扭曲的偏移递增比例
//        float mMulOffsetLong = 1.0F;
//
//        float mOffsetShort = PAGER_HEIGHT / 2F * sizeShort;
//        float mMulOffsetShort = 1.0F;
//
//
//        int index = 0;
//
//        for (int y = 0; y <= SUB_HEIGHT; y++) {
//            float fy = mViewHeight * y / SUB_HEIGHT;
//            for (int x = 0; x <= SUB_WIDTH; x++) {
//                float fx = mViewWidth * x / SUB_WIDTH;
//
//                if (x == SUB_WIDTH) {
//                    if (y >= mSubHeightStart && y <= mSubHeightEnd) {
//                        fx = mViewWidth * x / SUB_WIDTH + mOffsetLong * mMulOffsetLong;
//                        mMulOffsetLong = mMulOffsetLong / 1.5f;
//                    }
//                }
//
//                if (y == SUB_HEIGHT) {
//                    if (x >= mSubWidthStart && x <= mSubWidthEnd) {
//                        fy = mViewHeight * y / SUB_HEIGHT + mOffsetShort * mMulOffsetShort;
//                        mMulOffsetShort = mMulOffsetShort / 1.5f;
//                    }
//                }
//
//                mVerts[index * 2 + 0] = fx;
//                mVerts[index * 2 + 1] = fy;
//
//                index += 1;
//            }
//        }
//    }
//
//
//    private void calXy() {
//        if (isSlide) return;
//        if (!mRegionCircle.contains(mCurX, mCurY)) {
//            mCurY = (int) (mViewHeight - Math.sqrt(Math.pow(mViewWidth * 3 / 4f, 2) - Math.pow(mCurX - mViewWidth / 4f, 2)));
//            mCurY = (int) (mCurY + mValueAdded);
//        }
//    }
//
//    /**
//     * 为isSlide提供对外的停止方法便于必要时释放滑动动画
//     */
////    public void slideStop() {
////        isSlide = false;
////    }
//    @Override
//    public void drawStatic(Canvas canvas) {
//        changePosition(mReadPageView.getCurPosition());
//        canvas.drawBitmap(mBitmaps[curPosition], 0, 0, null);
//    }
//
//    @DebugLog
//    @Override
//    public void drawAnima(Canvas canvas) {
//        calXy();
//
//        pathReset();
//
//
//        mK = mViewWidth - mCurX;
//        mL = mViewHeight - mCurY;
//
//        float temp = (float) (Math.pow(mK, 2) + Math.pow(mL, 2));
//
//        sizeShort = temp / (2f * mK);
//        sizeLong = temp / (2f * mL);
//
////        calVerts(sizeLong,sizeShort,mViewWidth - sizeShort,mViewHeight - sizeLong);
//
//        /*
//         * 根据长短边边长计算旋转角度并确定mRatio的值
//         */
//        if (sizeShort < sizeLong) {
//            mRatio = Ratio.SHORT;
//            float sin = (mK - sizeShort) / sizeShort;
//            mDegree = (float) (Math.asin(sin) / Math.PI * 180);
//        } else {
//            mRatio = Ratio.LONG;
//            float cos = mK / sizeLong;
//            mDegree = (float) (Math.acos(cos) / Math.PI * 180);
//        }
//
////        calVerts(sizeLong, sizeShort, mViewWidth - sizeShort, mViewHeight - sizeLong);
//
//        if (sizeLong > mViewHeight) {
//            float an = sizeLong - mViewHeight;
//            // 三角形AMN的MN边
//            float largerTrianShortSize = an / (sizeLong - (mViewHeight - mCurY)) * (mViewWidth - mCurX);
//
//            // 三角形AQN的QN边
//            float smallTrianShortSize = an / sizeLong * sizeShort;
//
//            float top1X = mViewWidth - largerTrianShortSize;
//            float top2X = mViewWidth - smallTrianShortSize;
//
//            float btmControlX = mViewWidth - sizeShort;
//            float btmControlY = mViewHeight;
//
//            mBtmStart.x = btmControlX - PAGER_HEIGHT * sizeShort;
//            mBtmStart.y = mViewHeight;
//
//            mBtmEnd.x = btmControlX - (mK - sizeShort) * PAGER_HEIGHT;
//            mBtmEnd.y = mViewHeight - mL * PAGER_HEIGHT;
//
//            //根据二阶贝塞尔曲线方程，曲线中点即为方程在t等于0.5时的点;
//            float t = 0.5f;
//            mBtmCenter.x = t * t * mBtmStart.x + 2 * t * (1f - t) * btmControlX + (1f - t) * (1f - t) * mBtmEnd.x;
//            mBtmCenter.y = t * t * mBtmStart.y + 2 * t * (1f - t) * btmControlY + (1f - t) * (1f - t) * mBtmEnd.y;
//
//            //计算带月牙的重叠区域
//            mPathFold.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathFold.quadTo(btmControlX, btmControlY, mBtmEnd.x, mBtmEnd.y);
//            mPathFold.lineTo(mCurX, mCurY);
//            mPathFold.lineTo(top1X, 0);
//            mPathFold.lineTo(top2X, 0);
//
//
//            //计算替补区域
//            mPathAdd.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathAdd.lineTo(top2X, 0);
//            mPathAdd.lineTo(mBtmCenter.x, mBtmCenter.y);
//            mPathAdd.close();
//
//
//            //计算月牙区域
//            mPathMoonBtm.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathMoonBtm.quadTo(btmControlX, btmControlY, mBtmEnd.x, mBtmEnd.y);
//            mPathMoonBtm.close();
//
//            //计算重叠与下一页总区域
//            mPathFoldAndNext.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathFoldAndNext.quadTo(btmControlX, btmControlY, mBtmEnd.x, mBtmEnd.y);
//            mPathFoldAndNext.lineTo(mCurX, mCurY);
//            mPathFoldAndNext.lineTo(top1X, 0);
//            mPathFoldAndNext.lineTo(mViewWidth, 0);
//            mPathFoldAndNext.lineTo(mViewWidth, mViewHeight);
//            mPathFoldAndNext.close();
//
//            mRegionMoon = computeRegion(mPathMoonBtm);
//        } else {
//
//            /* *************底部点的计算***************/
//
//            float btmControlX = mViewWidth - sizeShort;
//            float btmControlY = mViewHeight;
//
//            mBtmStart.x = btmControlX - PAGER_HEIGHT * sizeShort;
//            mBtmStart.y = mViewHeight;
//
//            mBtmEnd.x = btmControlX - (mK - sizeShort) * PAGER_HEIGHT;
//            mBtmEnd.y = mViewHeight - mL * PAGER_HEIGHT;
//
//            //根据二阶贝塞尔曲线方程，曲线中点即为方程在t等于0.5时的点;
//            float t = 0.5f;
//            mBtmCenter.x = 0.25F * mBtmStart.x + 0.5F * btmControlX + 0.25F * mBtmEnd.x;
//            mBtmCenter.y = 0.25F * mBtmStart.y + 0.5F * btmControlY + 0.25F * mBtmEnd.y;
//
//            /* *************右边点的计算***************/
//
//            float rightControlX = mViewWidth;
//            float rightControlY = mViewHeight - sizeLong;
//
//            mRightStart.x = rightControlX;
//            mRightStart.y = rightControlY - PAGER_HEIGHT * sizeLong;
//
//            mRightEnd.x = mCurX + (1 - PAGER_HEIGHT) * mK;
//            mRightEnd.y = mCurY - (sizeLong - mL) * (1 - PAGER_HEIGHT);
//
//
//            //根据二阶贝塞尔曲线方程，曲线中点即为方程在t等于0.5时的点;
//            t = 0.5f;
//            mRightCenter.x = t * t * mRightStart.x + 2 * t * (1 - t) * rightControlX + (1 - t) * (1 - t) * mRightEnd.x;
//            mRightCenter.y = t * t * mRightStart.y + 2 * t * (1 - t) * rightControlY + (1 - t) * (1 - t) * mRightEnd.y;
//
//
//            //限制右侧曲线起点
//            if (mRightStart.y <= 0) {
//                mRightStart.y = 0;
//            }
//
//            //限制底部左侧曲线起点
//            if (mBtmStart.x <= 0) {
//                mBtmStart.x = 0;
//            }
//
//            //根据底部左侧限制点重新计算贝塞尔曲线顶点坐标
//            float partOfShortLength = PAGER_HEIGHT * sizeShort;
//            if (btmControlX >= -mValueAdded && btmControlX <= partOfShortLength - mValueAdded) {
//                float f = btmControlX / partOfShortLength;
//                t = 0.5F * f;
//
//                float bezierPeakTemp = 1 - t;
//                float bezierPeakTemp1 = bezierPeakTemp * bezierPeakTemp;
//                float bezierPeakTemp2 = 2 * t * bezierPeakTemp;
//                float bezierPeakTemp3 = t * t;
//
//                mBtmCenter.x = bezierPeakTemp1 * mBtmStart.x + bezierPeakTemp2 * btmControlX + bezierPeakTemp3 * mBtmEnd.x;
//                mBtmCenter.y = bezierPeakTemp1 * mBtmStart.y + bezierPeakTemp2 * btmControlY + bezierPeakTemp3 * mBtmEnd.y;
//            }
//
//            //根据右侧限制点重新计算贝塞尔曲线顶点坐标
//            float partOfLongLength = PAGER_HEIGHT * sizeLong;
//            if (rightControlY >= -mValueAdded && rightControlY <= partOfLongLength - mValueAdded) {
//                float f = rightControlY / partOfLongLength;
//                t = 0.5F * f;
//
//                float bezierPeakTemp = 1 - t;
//                float bezierPeakTemp1 = bezierPeakTemp * bezierPeakTemp;
//                float bezierPeakTemp2 = 2 * t * bezierPeakTemp;
//                float bezierPeakTemp3 = t * t;
//
//                //贝塞尔曲线的弧线中间顶点
//                mRightCenter.x = bezierPeakTemp1 * mRightStart.x + bezierPeakTemp2 * rightControlX + bezierPeakTemp3 * mRightEnd.x;
//                mRightCenter.y = bezierPeakTemp1 * mRightStart.y + bezierPeakTemp2 * rightControlY + bezierPeakTemp3 * mRightEnd.y;
//            }
//
//
//            //计算带月牙的重叠区域
//            mPathFold.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathFold.quadTo(btmControlX, btmControlY, mBtmEnd.x, mBtmEnd.y);
//            mPathFold.lineTo(mCurX, mCurY);
//            mPathFold.lineTo(mRightEnd.x, mRightEnd.y);
//            mPathFold.quadTo(rightControlX, rightControlY, mRightStart.x, mRightStart.y);
////            mPathFold.close();
//
//            //替补区域
//            mPathAdd.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathAdd.lineTo(mRightStart.x, mRightStart.y);
//            mPathAdd.lineTo(mRightCenter.x, mRightCenter.y);
//            mPathAdd.lineTo(mBtmCenter.x, mBtmCenter.y);
//            mPathAdd.close();
//
//            //底部月牙
//            mPathMoonBtm.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathMoonBtm.quadTo(btmControlX, btmControlY, mBtmEnd.x, mBtmEnd.y);
//            mPathMoonBtm.close();
//
//            //右边月牙
//            mPathMoonRight.moveTo(mRightEnd.x, mRightEnd.y);
//            mPathMoonRight.quadTo(rightControlX, rightControlY, mRightStart.x, mRightStart.y);
//            mPathMoonRight.close();
//
//            //计算重叠与下一页的总区域
//            mPathFoldAndNext.moveTo(mBtmStart.x, mBtmStart.y);
//            mPathFoldAndNext.quadTo(btmControlX, btmControlY, mBtmEnd.x, mBtmEnd.y);
//            mPathFoldAndNext.lineTo(mCurX, mCurY);
//
//            mPathFoldAndNext.lineTo(mRightEnd.x, mRightEnd.y);
//            mPathFoldAndNext.quadTo(rightControlX, rightControlY, mRightStart.x, mRightStart.y);
//
//            mPathFoldAndNext.lineTo(mViewWidth, mViewHeight);
//            mPathFoldAndNext.close();
//
//            //换算月牙Region
//            Region mRegionMoonBtm = computeRegion(mPathMoonBtm);
//            Region mRegionMoonRight = computeRegion(mPathMoonRight);
//            mRegionMoon.op(mRegionMoonRight, mRegionMoonBtm, Region.Op.UNION);
//        }
//        //带月牙的重叠区域
//        mRegionFold = computeRegion(mPathFold);
//        //重叠区域与下一页总区域
//        mRegionFoldAndNext = computeRegion(mPathFoldAndNext);
//        //替补区域
//        mRegionAdd = computeRegion(mPathAdd);
//
//        //替补区域加入重叠区域
//        mRegionFold.op(mRegionAdd, Region.Op.UNION);
//        //重叠区域剔除月牙区域
//        mRegionFold.op(mRegionMoon, Region.Op.DIFFERENCE);
//
//        //下一页区域
//        mRegionFoldAndNext.op(mRegionFold, Region.Op.DIFFERENCE);
//
//        //画
//        drawBitmap(canvas);
//    }
//
//    @DebugLog
//    private void drawBitmap(Canvas canvas) {
//
//        //画当前页区域
//        canvas.save();
//        canvas.clipRegion(mRegionCurrent);
//        canvas.drawBitmap(mFirstBitmap, 0, 0, null);
//        canvas.restore();
//
//
//        canvas.save();
//
//        /*
//         * 根据长短边标识计算折叠区域图像
//         */
//        canvas.translate(mCurX, mCurY);
//        if (mRatio == Ratio.SHORT) {
//            canvas.rotate(90 - mDegree);
//            canvas.translate(0, -mViewHeight + showdowWidth);
//            canvas.scale(-1, 1);
//            canvas.translate(-mViewWidth + showdowWidth, 0);
//        } else {
//            canvas.rotate(-(90 - mDegree));
//            canvas.translate(-mViewWidth + showdowWidth, 0);
//            canvas.scale(1, -1);
//            canvas.translate(0, -mViewHeight + showdowWidth);
//        }
//
//        mGradientDrawable.setColors(mBackShadowColors);
//        mGradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
//        mGradientDrawable.setBounds((int) mViewWidth - showdowWidth, 0, (int) mViewWidth, (int) mViewHeight - showdowWidth);
//        mGradientDrawable.draw(canvas);
//
//        mGradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
//        mGradientDrawable.setBounds(0, (int) mViewHeight - showdowWidth, (int) mViewWidth - showdowWidth, (int) mViewHeight);
//        mGradientDrawable.draw(canvas);
//
//        //画重叠区域
//        canvas.clipRegion(mRegionFold);
//        //底色
//        canvas.drawColor(0xFFD6CFC6);
//        canvas.translate(-showdowWidth, -showdowWidth);
//
//        canvas.drawBitmap(mSecondBitmap, 0, 0, null);
////        canvas.drawBitmapMesh(mCurrentBitmap, SUB_WIDTH, SUB_HEIGHT, mVerts, 0, null, 0, null);
//        canvas.drawColor(0xEED6CFC6);
//        //扭曲
//        canvas.restore();
//
//
//        //画下一页区域
//        canvas.save();
//        canvas.clipRegion(mRegionFoldAndNext);
//
//        canvas.drawBitmap(mSecondBitmap, 0, 0, null);
//
//        if (mRatio == Ratio.SHORT) {
//            mDegree = (float) (Math.atan2(sizeShort, sizeLong) / Math.PI * 180);
//            canvas.translate(mViewWidth - sizeShort - sizeShort * PAGER_HEIGHT, mViewHeight);
//            canvas.rotate(-(90 - mDegree));
//            mGradientShort.draw(canvas);
//        } else {
//            mDegree = (float) (Math.atan2(sizeLong, sizeShort) / Math.PI * 180);
//            canvas.translate(mViewWidth, mViewHeight - sizeLong - sizeLong * PAGER_HEIGHT);
//            canvas.rotate(90 - mDegree);
//            mGradientLong.draw(canvas);
//        }
//
//        canvas.restore();
//
//        canvas.drawPath(mPathFold, mTextPaint);
//    }
//
//    @Override
//    public void setDown(int downX, int downY) {
//        super.setDown(downX, downY);
//        isSlide = false;
//        isCheck = false;
//    }
//
//    @Override
//    public void startAnimation(Scroller scroller) {
//        isSlide = true;
//        mStartX = mCurX;
//        mStartY = mCurY;
//        if (mOrientation == AnimaOrientation.LAST) {
//            scroller.startScroll(mStartX, 0, mTotalWidth - mStartX, mTotalHeight, 1000);
//        } else if (mOrientation == AnimaOrientation.NEXT) {
//            scroller.startScroll(mStartX, 0, -mTotalWidth - mStartX, mTotalHeight, 1000);
//        }
//    }
//
//
//    @Override
//    public void cancel(Scroller scroller) {
//        if (mOrientation == AnimaOrientation.LAST) {
//            mOrientation = AnimaOrientation.NEXT;
//            Log.i(TAG, "LAST");
//        } else if (mOrientation == AnimaOrientation.NEXT) {
//            mOrientation = AnimaOrientation.LAST;
//            Log.i(TAG, "NEXT");
//        }
//        startAnimation(scroller);
//    }
//
//
//    @Override
//    public void setTouch(int curX, int curY) {
//        if (!isCheck) {
//            isCheck = true;
//            mOrientation = mCurX > mDownX ? AnimaOrientation.LAST : AnimaOrientation.NEXT;
//            if (mOrientation == AnimaOrientation.LAST) {
//                mFirstBitmap = mBitmaps[lastPosition];
//                mSecondBitmap = mBitmaps[curPosition];
//            } else {
//                mFirstBitmap = mBitmaps[curPosition];
//                mSecondBitmap = mBitmaps[nextPosition];
//            }
//        }
//        mCurX = curX;
//        mCurY = curY;
//
//        if (isSlide) {
//            if (mOrientation == AnimaOrientation.LAST) {
//                mCurY = (int) (mStartY + (mCurX - mStartX) * (mViewHeight - mStartY) / (mViewWidth - mStartX));
//            } else {
//                mCurY = (int) (mStartY + (mCurX - mStartX) * (mViewHeight - mStartY) / (-mViewWidth - mStartX));
//            }
//
//            if (mCurY >= mViewHeight) {
//                mCurY = (int) (mViewHeight - 1);
//            }
//        }
//    }
//
//
//    @Override
//    public void setOrientation(AnimaOrientation orientation) {
//
//    }
//
//    enum Ratio {
//        LONG, SHORT
//    }
}
