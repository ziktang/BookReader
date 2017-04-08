package example.tctctc.com.tybookreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import example.tctctc.com.tybookreader.R;


/**
 * Created by tctctc on 2017/3/27.
 * Function:实现圆形图片或者圆角矩形
 */

public class CircleView extends ImageView implements SelectView {

    /**
     * 需要保存在Bundle中的参数的key
     **/
    private static final String STATE_INSTANCE = "CircleImageView";
    private static final String BORDER_WIDTH = "border_width";
    private static final String BORDER_COLOR = "border_color";
    private static final String TYPE = "type";
    private static final String RX = "rx";
    private static final String RY = "ry";


    /**
     * 边界圆环的画笔
     **/
    private Paint mBorderPaint;

    /**
     * 画出需要Bitmap填充的区域的画笔
     **/
    private Paint mPaint;

    /**
     * 形状类型
     **/
    public static final String ROUND = "round";   //圆形
    public static final String CIRCLE = "circle"; //圆角矩形

    /**
     * 形状类型
     **/
    private String type = CIRCLE;
    /**
     * 圆形模式下的圆的半径
     **/
    private float mRadius;
    /**
     * 圆角矩形模式下的绘制区域
     **/
    private RectF mRoundRectF;

    /**
     * 圆角矩形模式下的默认的x方向的角度
     **/
    private static final float DEFAULT_RX = 20f;
    /**
     * 圆角矩形模式下的默认的y方向的角度
     **/
    private static final float DEFAULT_RY = 20f;
    /**
     * 圆角矩形模式下的x方向的角度
     **/
    private float rx = DEFAULT_RX;
    /**
     * 圆角矩形模式下的y方向的角度
     **/
    private float ry = DEFAULT_RY;


    /**
     * 外层圆环默认的宽度
     **/
    private static final float DEFAULT_BORDER_WIDTH = 0;
    /**
     * 外层圆环默认的颜色
     **/
    private static final int DEFAULT_BORDER_COLOR = 0xFFFFFFFF;
    /**
     * 外层圆环的宽度
     **/
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;
    /**
     * 外层圆环的颜色
     **/
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mSize;
    private int color = 0xFFFFFFFF;
    private boolean isColor;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mBorderWidth = array.getDimension(R.styleable.CircleView_mBorderWidth, 0);
        mBorderColor = array.getColor(R.styleable.CircleView_mBorderColor, Color.WHITE);
        if (array.hasValue(R.styleable.CircleView_type)){
            type = array.getString(R.styleable.CircleView_type);
        }else{
            type = CIRCLE;
        }
        if (array.hasValue(R.styleable.CircleView_innerColor)){
            color = array.getColor(R.styleable.CircleView_innerColor,Color.WHITE);
            isColor = true;
        }else
            isColor = false;
        array.recycle();
        init();
    }

    /**
     * 保存状态，以便恢复
     *
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putFloat(BORDER_WIDTH, mBorderWidth);
        bundle.putInt(BORDER_COLOR, mBorderColor);
        bundle.putString(TYPE, type);
        bundle.putFloat(RX, rx);
        bundle.putFloat(RY, ry);
        return bundle;
    }


    /**
     * 恢复保存的额状态
     *
     * @return
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
            this.mBorderWidth = bundle.getFloat(BORDER_WIDTH);
            this.mBorderColor = bundle.getInt(BORDER_COLOR);
            this.type = bundle.getString(TYPE);
            this.rx = bundle.getFloat(RX);
            this.ry = bundle.getFloat(RY);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private void init() {
        mPaint = new Paint();
        //设置是否去掉锯齿
        mPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setDither(true);

        //dp值转化为px
        rx = dp2px(rx);
        ry = dp2px(ry);
        mBorderWidth = dp2px(mBorderWidth);
    }


    //参数会变化，重新初始化
    public void initAttr() {
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        //圆形模式，计算半径
        if (CIRCLE.equals(type)) {
            mRadius = (mSize - 2 * mBorderWidth) / 2;
        } else if (ROUND.equals(type)) {
            //圆角矩形模式，记录需要绘制的区域
            mRoundRectF = new RectF(mBorderWidth, mBorderWidth, getWidth() - mBorderWidth, getHeight() - mBorderWidth);
        }
    }


    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //圆形模式，强制另宽高相等，取小者
        if (CIRCLE.equals(type)) {
            mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(mSize, mSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initAttr();
        //如果是填充颜色模式或者没有设置图片，则填充颜色
        if (isColor||getDrawable()==null){
            mPaint.clearShadowLayer();
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
        }else{
            setShaderBitmap();
        }

        if (type.equals(CIRCLE)) {
            canvas.drawCircle(mRadius + mBorderWidth, mRadius + mBorderWidth, mRadius, mPaint);
            canvas.drawCircle(mRadius + mBorderWidth, mRadius + mBorderWidth, mRadius, mBorderPaint);
        } else if (type.equals(ROUND)) {
            canvas.drawRoundRect(mRoundRectF, 20, 20, mPaint);
            canvas.drawRoundRect(mRoundRectF, 20, 20, mBorderPaint);
        }
    }

    /**
     * 设置需要的Shader
     */
    private void setShaderBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Bitmap bitmap = getBitmap(drawable);
        if (bitmap == null) {
            return;
        }

        //设置bitmap做渲染材料，CLAMP模式是当不够一个bitmap图像填充时，其他地方用bitmap边界颜色渲染
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //需要被渲染的矩形区域
        float needWidth = getWidth() - 2 * mBorderWidth;
        float needHeight = getHeight() - 2 * mBorderWidth;

        //用做渲染材料的bitmap
        float dx = 0, dy = 0;

        float scaleX = needWidth / bitmap.getWidth();
        float scaleY = needHeight / bitmap.getHeight();

        //总原则，缩放后bitmap的宽高必须要不小于view的宽高，只需取缩放程度小的即可
        float scale = Math.max(scaleX, scaleY);


        //因为经过缩放后，bitmap至少在一个方向的边界上与需要被渲染的区域边界重合，除非bitmap的宽高比例完全和显示区域的比例相同，否则会有一个方向尺寸大与显示区域
        // 比较X,Y方向上谁的缩放程度大，将bitmap在另一个方向向中心移动
        if (scaleY > scaleX) {
            dx = (needWidth - bitmap.getWidth() * scale) * 0.5f;
        } else {
            dy = (needHeight - bitmap.getHeight() * scale) * 0.5f;
        }

        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        //将bitmap移到画布的中间，这样就可以用最中心的画面来渲染
        matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        shader.setLocalMatrix(matrix);
        mPaint.setShader(shader);
    }

    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }

        //获取显示出来的宽高，不是图片实际的宽高
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(new Canvas(bitmap));
        return bitmap;
    }

    /**
     * 圆角矩形模式下的圆角角度，
     *
     * @param rx 单位是dp
     * @param ry
     * @return
     */
    public CircleView setRoundRadius(float rx, float ry) {
        if (rx >= 0 && ry >= 0) {
            this.rx = dp2px(rx);
            this.ry = dp2px(ry);
        }
        return this;
    }

    /**
     * 设置边界颜色
     *
     * @param borderColor
     * @return
     */
    public CircleView setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        return this;
    }

    /**
     * 设置边界宽度
     *
     * @param borderWidth
     * @return
     */
    public CircleView setBorderWidth(float borderWidth) {
        if (borderWidth >= 0 && 2 * dp2px(borderWidth) < getWidth() && 2 * dp2px(borderWidth) < getHeight()) {
            this.mBorderWidth = dp2px(borderWidth);
        }
        return this;
    }

    public CircleView setType(String type) {
        Log.d("aaa", "type:" + type);
        if (type.equals(CIRCLE) || type.equals(ROUND)) {
            if (!this.type.equals(type)) {
                Log.d("aaa", "type2:" + type);
                this.type = type;
                requestLayout();
            }
        }
        return this;
    }

    public void refresh() {
        invalidate();
    }

    public void setColor(int color){
        this.color = color;
        isColor = true;
        invalidate();
    }

    @Override
    public void select() {
        setBorderColor(getResources().getColor(R.color.orange)).refresh();
    }

    @Override
    public void unSelect() {
        setBorderColor(getResources().getColor(R.color.white)).refresh();
    }
}


