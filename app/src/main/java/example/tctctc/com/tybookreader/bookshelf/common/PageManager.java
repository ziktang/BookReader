package example.tctctc.com.tybookreader.bookshelf.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.support.annotation.DimenRes;
import android.support.annotation.TransitionRes;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.Directory;
import example.tctctc.com.tybookreader.bean.MarkBean;
import example.tctctc.com.tybookreader.bean.MarkBeanDao;
import example.tctctc.com.tybookreader.bean.ReadConfigBean;
import example.tctctc.com.tybookreader.bookshelf.model.BookDao;
import example.tctctc.com.tybookreader.bookshelf.model.MarkDao;
import example.tctctc.com.tybookreader.common.rx.RxManager;
import example.tctctc.com.tybookreader.common.rx.RxSchedulers;
import example.tctctc.com.tybookreader.utils.CustomUUId;
import example.tctctc.com.tybookreader.utils.UiUtils;
import example.tctctc.com.tybookreader.view.ReadPageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static example.tctctc.com.tybookreader.bookshelf.model.BookDao.update;


/**
 * Created by tctctc on 2017/4/3.
 * Function:阅读页面绘制管理
 */

public class PageManager {

    public static final String TAG = "PageManager";

    private static PageManager sPageManager;
    //绘制内容提供者
    private ContentManager mContentManager;
    //绘制的View
    private ReadPageView mReadPageView;
    //阅读界面的配置
    private ReadConfigBean mConfigBean;

    private Context mContext;
    //阅读状态
    private ReadStatus mReadStatus;
    //要打开的书
    private BookBean mBookBean;

    //这个页面的bitmap和下个页面的bitmap
//    private Bitmap mCurBitmap, mNextBitmap, mLastBitmap;

    private Bitmap[] mBitmaps;
    //页面的宽和高
    private int mWidth, mHeight;
    //正文内容的区域宽
    private float mBodyWidth;
    //正文内容的区域高
    private float mbodyHeight;

    //状态画笔
    private Paint mStatusPaint;

    private PageTxt mLastPageTxt;
    private PageTxt mCurPageTxt;
    private PageTxt mNextPageTxt;

    //文字的行数
    private int lineNum;
    //行间距
    private float lineSpace;
    //字间距
    private float fontSpace;
    //段间距
    private float paragraphSpace;
    //正文与上下边界的距离
    private float marginHeight;
    //正文与左右边界的距离
    private float marginWidth;

    //正文与左右边界的测量距离
    private float marginMeasureWidth;
    //边界信息距离边界的高度
    private float marginBorderHeight;
    //边界信息字体大小
    private float borderFontSize;
    //电池数据格式
    private DecimalFormat mProgressDf;
    //时间格式
    private DateFormat mDateFormat;
    //电池外框Rect
    private RectF mBtOuterRectF;
    //电池进度Rect
    private RectF mBtInnerRectF;

    //电池宽度
    private float btWidth;

    //电池高度
    private float btHeight;
    //电池Intent
    private Intent mBtIntent;
    //边界信息的画笔 书籍名,章节名,时间,电池,进度
    private Paint mBorderPaint;
    //正文画笔
    private Paint mPaint;
    //背景bitmap
    private Bitmap mBgBitmap;
    //正文字体颜色
    private int fontColor;
    //字体类型
    private Typeface mTypeface;
    //字体大小
    private float mFontSize;
    //当前章节名
    private String mCurChapterName = " ";
    //当前书名
    private String mBookName;

    //时间字符串
    private String dateStr;
    private PageEvent mPageEvent;
    private int batteryLevel;
    private int batteryScale;

    private float progressWidth;
    private float dateWidth;

    private float batterySpace;

    private RxManager mRxManager;

    private boolean isNext;

    private int mCurPosition;

    private boolean isReady;

    private Disposable mDisposable;

    private Observer<Boolean> mObserver = new Observer<Boolean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(@NonNull Boolean result) {
            if (result) {
                mReadStatus = ReadStatus.READING;
                if (mContentManager.getDirectory().size() > 0) {
                    mRxManager.post("Directory", 1);
                }else{
                    mRxManager.post("Directory", 2);
                }

                mRxManager.post("length",1);

                PageTxt pageTxt = mContentManager.getPageTxtForBegin(mBookBean.getProgress());
                if (pageTxt == null) result = false;
                else setCurrentPage(pageTxt);
            }

            if (!result) {
                mReadStatus = ReadStatus.FAILED;
                mCurPosition = 1;
                mReadPageView.setCurPosition(mCurPosition);
                drawStatus(mBitmaps[mCurPosition]);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Intent.ACTION_BATTERY_CHANGED) {
                batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                updateBattery(batteryLevel);
            }
            if (intent.getAction() == Intent.ACTION_TIME_TICK) {
                updateTime();
            }

        }
    };
    private boolean noLast;
    private boolean noNext;
    private int lastPosition;
    private int nextPosition;
    private String progressStr;

    public static PageManager getInstance(Context context, BookBean bookBean) {
        if (sPageManager == null) {
            sPageManager = new PageManager(context, bookBean);
        }
        return sPageManager;
    }

    private PageManager(Context context, BookBean bookBean) {
        mContext = context;
        mRxManager = new RxManager();
        mBookBean = bookBean;
        mConfigBean = new ReadConfig(mContext).getConfigBean();
        mContentManager = ContentManager.init(this);
    }

    private void initData() {
        initBgBitmapAndFontColor();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        mBtIntent = mContext.registerReceiver(mReceiver, intentFilter);

        mBtOuterRectF = new RectF();
        mBtInnerRectF = new RectF();

        batterySpace = UiUtils.dpToPx(mContext, 1);

        mDateFormat = new SimpleDateFormat("HH:mm");
        mProgressDf = new DecimalFormat("#0.0");

        batteryLevel = mBtIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        batteryScale = mBtIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        dateStr = mDateFormat.format(new Date());

        borderFontSize = getResource(R.dimen.borderFontSize);
        marginBorderHeight = getResource(R.dimen.marginBorderHeight);
        marginWidth = getResource(R.dimen.marginWidth);
        marginHeight = getResource(R.dimen.marginHeight);
        btWidth = getResource(R.dimen.btWidth);
        btHeight = getResource(R.dimen.btHeight);

        mBodyWidth = mWidth - 2 * marginWidth;
        mbodyHeight = mHeight - 2 * marginHeight;

        lineSpace = getResource(R.dimen.lineSpace);
        paragraphSpace = getResource(R.dimen.paragraphSpace);

        setTypeFace(mConfigBean.getFontType());

        mFontSize = UiUtils.dpToPx(mContext, mConfigBean.getFontSize());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mFontSize);
        mPaint.setTypeface(mTypeface);
        mPaint.setColor(getFontColor());
        mPaint.setSubpixelText(true);

        float statusFontSize = getResource(R.dimen.statusFontSize);
        mStatusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStatusPaint.setTextAlign(Paint.Align.LEFT);
        mStatusPaint.setTextSize(statusFontSize);
        mStatusPaint.setTypeface(mTypeface);
        mStatusPaint.setColor(getFontColor());
        mStatusPaint.setSubpixelText(true);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setTextAlign(Paint.Align.LEFT);
        mBorderPaint.setTextSize(borderFontSize);
        mBorderPaint.setTypeface(mTypeface);
        mBorderPaint.setColor(getFontColor());
        mBorderPaint.setSubpixelText(true);

        progressWidth = mBorderPaint.measureText("99.9%");
        dateWidth = mBorderPaint.measureText("00:00");

        mBtIntent = mContext.getApplicationContext()
                .registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        calculateLineNum();
        measureMarginWidth();

        mRxManager.onEvent("chapter", new Consumer<Directory>() {
            @Override
            public void accept(@NonNull Directory directory) {
                toChapter(directory.getStartPosition());
            }
        });

        mRxManager.onEvent("mark", new Consumer<MarkBean>() {
            @Override
            public void accept(@NonNull MarkBean markBean) {
                changeProgress(markBean.getPosition());
            }
        });
    }

    private float getResource(@DimenRes int res) {
        float dp = mContext.getResources().getDimension(res);
        return dp;
    }


    public void openBook() {
        mReadStatus = ReadStatus.OPENING;
        mCurPosition = 1;
        mReadPageView.setCurPosition(mCurPosition);
        drawStatus(mBitmaps[mCurPosition]);
        Observable.just(mBookBean.getProgress()).map(new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@NonNull Integer integer) throws Exception {
                boolean result;
                try {
                    result = mContentManager.openBook(mBookBean);
                } catch (Exception e) {
                    return false;
                }
                return result;
            }
        }).compose(RxSchedulers.<Boolean>ioMain()).subscribe(mObserver);
    }

    private void onDraw(Bitmap curBitmap, PageTxt pageTxt, boolean isUpdate, int who) {

        Canvas canvas = new Canvas(curBitmap);
        canvas.drawBitmap(mBgBitmap, 0, 0, null);

        if (pageTxt.getLines().size() == 0) {
            return;
        }


        //画正文
        float height = marginHeight;
        for (String line : pageTxt.getLines()) {
            height += lineSpace + mFontSize;
            canvas.drawText(line, marginMeasureWidth, height, mPaint);
        }
        //画书名
        canvas.drawText(mBookBean.getBookName(), marginMeasureWidth, marginBorderHeight + borderFontSize, mBorderPaint);
//        canvas.drawText(pageTxt.getStart() + "", 5 * marginMeasureWidth, marginBorderHeight + borderFontSize, mBorderPaint);
//        canvas.drawText(pageTxt.getEnd() + "", 7 * marginMeasureWidth, marginBorderHeight + borderFontSize, mBorderPaint);

//        String s = who + "bitmap";
//        canvas.drawText(s, 10 * marginMeasureWidth, marginBorderHeight + borderFontSize, mBorderPaint);

        //画章节名
        float chapterNameWidth = mBorderPaint.measureText(pageTxt.getChapterName());
        canvas.drawText(pageTxt.getChapterName(), mWidth - chapterNameWidth - marginMeasureWidth, marginBorderHeight + borderFontSize, mBorderPaint);

        //画时间
        canvas.drawText(dateStr, marginMeasureWidth, mHeight - marginBorderHeight, mBorderPaint);
        //画进度
        float progress = (float) pageTxt.getStart() / (float) mBookBean.getLength();
        progressStr = mProgressDf.format(progress * 100) + "%";

        canvas.drawText(progressStr, mWidth - progressWidth - marginMeasureWidth, mHeight - marginBorderHeight, mBorderPaint);
        //画电池

        //外框
        mBtOuterRectF.left = marginMeasureWidth + marginBorderHeight + dateWidth;
        mBtOuterRectF.right = mBtOuterRectF.left + btWidth;
        mBtOuterRectF.bottom = mHeight - marginBorderHeight;
        mBtOuterRectF.top = mBtOuterRectF.bottom - btHeight;

        float curBattery = (float) batteryLevel / (float) batteryScale;
        float curBatteryWidth = (btWidth - UiUtils.dpToPx(mContext, 2)) * curBattery;

        //画外框
        mBorderPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(mBtOuterRectF, 10, 10, mBorderPaint);

        mBorderPaint.setStyle(Paint.Style.FILL);

        mBtInnerRectF.left = mBtOuterRectF.left + batterySpace;
        mBtInnerRectF.right = mBtInnerRectF.left + curBatteryWidth;
        mBtInnerRectF.bottom = mBtOuterRectF.bottom - batterySpace;
        mBtInnerRectF.top = mBtOuterRectF.top + batterySpace;

        canvas.drawRoundRect(mBtInnerRectF, 10, 10, mBorderPaint);

        if (isUpdate){
            mReadPageView.postInvalidate();
        }
        Log.d(TAG, "*****" + Thread.currentThread().getStackTrace()[2].getMethodName() + "()*****");
    }

    private void updateData() {
        //更新章节进度
        mContentManager.setCurrentChapter(mCurPageTxt.getStart());

        Log.i(TAG, "getCurrentChapter:" + mContentManager.getCurrentChapter());
        if (!mCurChapterName.equals(mCurPageTxt.getChapterName())) {
            Log.i(TAG, "getCurrentChapter---:" + mContentManager.getCurrentChapter());
            mRxManager.post("updateCurrentChapter", mContentManager.getCurrentChapter());
            mCurChapterName = mCurPageTxt.getChapterName();
        }


        //更新数据库阅读进度
        mBookBean.setProgress(mCurPageTxt.getStart());
        BookDao.update(mBookBean);
        //更新外部阅读进度
        mPageEvent.onProgressChange(mCurPageTxt.getStart());

    }


    public void setCurrentPage(PageTxt curPageTxt) {
        mCurPosition = 1;
        mCurPageTxt = curPageTxt;
        Log.i(TAG,"mContentManager:"+(mContentManager==null));
        Log.i(TAG,"mCurPageTxt:"+(mCurPageTxt==null));
        mLastPageTxt = mContentManager.getPageTxtForEnd(mCurPageTxt.getStart());
        mNextPageTxt = mContentManager.getPageTxtForBegin(mCurPageTxt.getEnd());

        onDraw(mBitmaps[mCurPosition], mCurPageTxt, true, 1);

        if (mLastPageTxt == null) {
            noLast = true;
        } else {
            noLast = false;
            onDraw(mBitmaps[mCurPosition - 1], mLastPageTxt, false, 0);
        }

        if (mNextPageTxt == null) {
            noNext = true;
        } else {
            noNext = false;
            onDraw(mBitmaps[mCurPosition + 1], mNextPageTxt, false, 2);
        }

        mReadPageView.setCurPosition(mCurPosition);
        updateData();
        isReady = true;
    }


    public void updateLastNext() {
        isReady = false;
        Observable.create(new ObservableOnSubscribe<Boolean>() {


            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                if (isNext) {
                    mCurPosition = (mCurPosition + 1) % 3;
                    lastPosition = mCurPosition == 0 ? 2 : mCurPosition - 1;
                    nextPosition = mCurPosition == 2 ? 0 : mCurPosition + 1;

                    noLast = false;
                    mLastPageTxt = mCurPageTxt;
                    mCurPageTxt = mNextPageTxt;

                    mNextPageTxt = mContentManager.getPageTxtForBegin(mNextPageTxt.getEnd());
                    if (mNextPageTxt == null) {
                        noNext = true;
                    } else {
                        noNext = false;
                        onDraw(mBitmaps[nextPosition], mNextPageTxt, false, nextPosition);
                    }
                } else {
                    mCurPosition = mCurPosition == 0 ? 2 : mCurPosition - 1;
                    lastPosition = mCurPosition == 0 ? 2 : mCurPosition - 1;
                    nextPosition = mCurPosition == 2 ? 0 : mCurPosition + 1;

                    noNext = false;
                    mNextPageTxt = mCurPageTxt;
                    mCurPageTxt = mLastPageTxt;

                    mLastPageTxt = mContentManager.getPageTxtForEnd(mCurPageTxt.getStart());

                    if (mLastPageTxt == null) {
                        noLast = true;
                    } else {
                        noLast = false;
                        onDraw(mBitmaps[lastPosition], mLastPageTxt, false, lastPosition);
                    }
                }
                mReadPageView.setCurPosition(mCurPosition);
                updateData();
                e.onNext(true);
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                isReady = true;
            }
        });
    }

    public boolean updateNextPage(boolean isNext) {
        if (isNext && noNext) {
            Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isNext && noLast) {
            Toast.makeText(mContext, "已经是第一页了", Toast.LENGTH_SHORT).show();
            return false;
        }

        this.isNext = isNext;
        return true;
    }

    public void nextChapter() {
        PageTxt pageTxt = mContentManager.getNextChapter();
        if (pageTxt == null) {
            Toast.makeText(mContext, "已经是最后一章了", Toast.LENGTH_SHORT).show();
        } else {
            setCurrentPage(pageTxt);
        }
    }

    public void lastChapter() {
        PageTxt pageTxt = mContentManager.getLastChapter();
        if (pageTxt == null)
            Toast.makeText(mContext, "已经是第一章了", Toast.LENGTH_SHORT).show();
        else
            setCurrentPage(pageTxt);

    }

    public void toChapter(int position) {
        PageTxt pageTxt = mContentManager.getPageTxtForBegin(position);
        if (pageTxt == null) {

        } else setCurrentPage(pageTxt);
    }

    public boolean bookMark() {
        QueryBuilder builder = MarkDao.getInstance().queryBuilder();
        MarkBean markBean = (MarkBean) builder.where(MarkBeanDao.Properties.BookId.eq(mBookBean.getBookId()), MarkBeanDao.Properties.Position.eq(mCurPageTxt.getStart()))
                .build().unique();

        if (markBean != null) {
            MarkDao.getInstance().deleteByKey(markBean.getId());
            return false;
        } else {
            markBean = new MarkBean();
            markBean.setId(CustomUUId.get().nextId());
            markBean.setTime(DateUtils.formatDateTime(mContext, new Date().getTime(), DateUtils.FORMAT_SHOW_YEAR));
            markBean.setBookName(mBookBean.getBookName());
            markBean.setBookId(mBookBean.getBookId());
            markBean.setPosition(mCurPageTxt.getStart());
            markBean.setChapterName(mCurPageTxt.getChapterName());
            markBean.setProgress(progressStr);
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < 3; i++) {
                buffer.append(mCurPageTxt.getLines().get(i));
                if (buffer.length() > 60)
                    break;
            }
            markBean.setDescribe(buffer.toString());
            MarkDao.getInstance().insert(markBean);
            return true;
        }
    }

    public boolean isMark() {
        QueryBuilder builder = MarkDao.getInstance().queryBuilder();
        MarkBean markBean = (MarkBean) builder.where(MarkBeanDao.Properties.BookId.eq(mBookBean.getBookId()), MarkBeanDao.Properties.Position.eq(mCurPageTxt.getStart()))
                .build().unique();
        return markBean != null;
    }

    private void drawStatus(Bitmap curBitmap) {
        mStatusPaint.setColor(BookApplication.getContext().getResources().getColor(R.color.statusColor));

        Canvas canvas = new Canvas(curBitmap);
        canvas.drawBitmap(getBgBitmap(), 0, 0, null);
        String result = "";
        if (mReadStatus == ReadStatus.FAILED) {
            result = "打开失败...";
        } else if (mReadStatus == ReadStatus.OPENING) {
            result = "正在打开...";
        }

        //设置文本的绘制是居中绘制的，就像word居中绘制一样
        mStatusPaint.setTextAlign(Paint.Align.CENTER);
        //获取字体尺寸参数
        Paint.FontMetricsInt fontMetricsInt = mStatusPaint.getFontMetricsInt();
        canvas.drawText(result, mWidth / 2, (mHeight - (fontMetricsInt.descent - fontMetricsInt.ascent)) / 2, mStatusPaint);
        mReadPageView.postInvalidate();
    }

    public void setReadPageView(ReadPageView readPageView) {
        mReadPageView = readPageView;
        mReadPageView.setPageManager(this);

        mBitmaps = mReadPageView.getBitmaps();
        mWidth = mBitmaps[0].getWidth();
        mHeight = mBitmaps[0].getHeight();

        initData();
    }

    public void initBgBitmapAndFontColor() {
        if (mConfigBean.isNight()) {
            //夜间模式...
        } else {
            setBgBitmap(mConfigBean.getBackground());
        }
    }

    private void setBgBitmap(int type) {
        if (getBgBitmap() != null) {
            getBgBitmap().recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        switch (type) {
//            case ReadConfig.BG_PAGER:
//                int bgRes = R.drawable.reading__reading_themes_vine_yellow1;
//                bitmap = BitmapUtils.decodeFromResourceResize(mContext.getResources(), bgRes, mWidth, mHeight);
//                break;
            case ReadConfig.BG_GREEN:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_green));
                fontColor = mContext.getResources().getColor(R.color.follow_read_black);
                break;
            case ReadConfig.BG_GRAY:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_gray));
                fontColor = mContext.getResources().getColor(R.color.follow_read_black);
                break;
            case ReadConfig.BG_YELLOW:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_yellow));
                fontColor = mContext.getResources().getColor(R.color.follow_read_black);
                break;
            case ReadConfig.BG_WHITE:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_white));
                fontColor = mContext.getResources().getColor(R.color.follow_read_black);
                break;

            case ReadConfig.BG_NIGHT:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_night));
                fontColor = mContext.getResources().getColor(R.color.follow_read_white);
                break;
        }
        mBgBitmap = bitmap;
    }


    public Bitmap getBgBitmap() {
        return mBgBitmap;
    }


    public Paint getPaint() {
        return mPaint;
    }

    public float getBodyWidth() {
        return mBodyWidth;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setTypeFace(String type) {
        if (type.equals(ReadConfig.DEFAULT_FONT_TYPE))
            mTypeface = Typeface.DEFAULT;
        else {
            mTypeface = Typeface.createFromAsset(mContext.getAssets(), type);
        }
    }

    public void changeFontSize(int fontSize) {
        mFontSize = UiUtils.dpToPx(mContext, fontSize);
        mPaint.setTextSize(mFontSize);
        calculateLineNum();
        measureMarginWidth();
        PageTxt pageTxt = mContentManager.getPageTxtForBegin(mCurPageTxt.getStart());
        if (pageTxt == null) {

        }
        setCurrentPage(pageTxt);
    }

    public void changeTypeface(String type) {
        setTypeFace(type);
        mPaint.setTypeface(mTypeface);
        mBorderPaint.setTypeface(mTypeface);
        calculateLineNum();
        measureMarginWidth();
        PageTxt pageTxt = mContentManager.getPageTxtForBegin(mCurPageTxt.getStart());
        if (pageTxt == null) {

        }
        setCurrentPage(pageTxt);
    }

    public void changeBackground(int type) {
        setBgBitmap(type);
//        onDraw(mBitmaps[mCurPosition], mCurPageTxt, true,mCurPosition);
        setCurrentPage(mCurPageTxt);
    }

    public void changeProgress(int progress) {
        Log.i(TAG,"progress:"+progress);
        Log.i(TAG,"lenth:"+mBookBean.getLength());
        PageTxt pageTxt = mContentManager.getPageTxtForBegin(progress);
        if (pageTxt == null) {

        }
        Log.i(TAG,"pageTxt:"+pageTxt);
        setCurrentPage(pageTxt);
    }


    public void changeDayOrNight(Boolean isNight) {
        mConfigBean.setNight(isNight);
        initBgBitmapAndFontColor();
//        onDraw(mBitmaps[mCurPosition], mCurPageTxt, true,mCurPosition);
        setCurrentPage(mCurPageTxt);
    }

    public void setPageEvent(PageEvent pageEvent) {
        mPageEvent = pageEvent;
    }

    private void updateTime() {
        if (mCurPageTxt != null && mReadPageView != null && !mReadPageView.isAnima()) {
            String date = mDateFormat.format(new Date());
            if (!dateStr.equals(date)) {
                dateStr = date;
                onDraw(mBitmaps[mCurPosition], mCurPageTxt, true, mCurPosition);
            }
        }
    }

    private void updateBattery(int level) {
        if (mCurPageTxt != null && mReadPageView != null && !mReadPageView.isAnima()) {
            if (batteryLevel != level) {
                onDraw(mBitmaps[mCurPosition], mCurPageTxt, true, mCurPosition);
            }
        }
    }

    public ReadStatus getReadStatus() {
        return mReadStatus;
    }

    public boolean isReady() {
        return isReady;
    }

    private void measureMarginWidth() {
//        Log.i(TAG, "mWidth:" + mWidth);
//        Log.i(TAG, "mHeight:" + mHeight);
//        Log.i(TAG, "mBodyWidth:" + mBodyWidth);
//        Log.i(TAG, "marginWidth:" + marginWidth);
//        Log.i(TAG, "marginHeight:" + marginHeight);
        float width = mBodyWidth % mFontSize;
        marginMeasureWidth = marginWidth + width / 2;
//        Log.i(TAG, "marginMeasureWidth:" + marginMeasureWidth);
    }

    private void calculateLineNum() {
        lineNum = (int) (mbodyHeight / (lineSpace + mFontSize));
    }

    private int getFontColor() {
        return fontColor;
    }

    public void destroy() {
        BookDao.update(mBookBean);
        mContext.unregisterReceiver(mReceiver);
        mRxManager.clear();
        mDisposable.dispose();
        mContentManager.destroy();
        mReadPageView = null;
        mPageEvent = null;
        mReceiver = null;
        mBtIntent = null;
        sPageManager = null;
    }

    public enum ReadStatus {
        OPENING, READING, FAILED
    }

    public interface PageEvent {
        void onProgressChange(int progress);
    }
}


