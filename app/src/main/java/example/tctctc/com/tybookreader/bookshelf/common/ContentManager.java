package example.tctctc.com.tybookreader.bookshelf.common;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.Chapter;
import example.tctctc.com.tybookreader.bean.TxtCache;
import example.tctctc.com.tybookreader.bookshelf.model.BookDao;
import example.tctctc.com.tybookreader.utils.FileUtils;

/**
 * Created by tctctc on 2017/3/28.
 * Function:
 */

public class ContentManager {

    private static ContentManager sContentManager;

    public static final String TAG = "ContentManager";

    public static final String cachePath = Environment.getExternalStorageDirectory() + "/TYReader/";

    //阅读内容相关的SharedPreferences名字
    private static final String READ_CONTENT = "read_content";

    //缓存的书籍
    private static final String CACHE_BOOKS = "cache_books";


    public static final int cacheSize = 150000;

    private List<TxtCache> mTxtCaches = new ArrayList();

    private List<Chapter> mChapterList = new ArrayList();

    private String bookPath;
    private BookBean mBookBean;
    private PageManager mPageManager;

    private int mCurrentPosition;
    private int mCurrentChapter;
    private ReadUtil mReadUtil;

    private ContentManager(PageManager pageManager) {
        mPageManager = pageManager;
        initData();
    }

    public static ContentManager init(PageManager pageManager) {
        if (sContentManager == null) {
            sContentManager = new ContentManager(pageManager);
        }
        return sContentManager;
    }

    public static ContentManager getInstance() {
        return sContentManager;
    }

    private void initData() {
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdir();
        }
        mReadUtil = new ReadUtil();
    }
    public synchronized boolean openBook(BookBean bookBean) {
        this.mBookBean = bookBean;
        if (isCacheBook()) {
            return openCacheBook();
        } else {
            return openNewBook();
        }
    }

    private boolean isCacheBook() {
//        String cacheBook = mSPUtils.getString(CACHE_BOOKS, "");
//        List<String> cacheBooks = new ArrayList(Arrays.asList(cacheBook.split(",")));
//        if (cacheBooks.isEmpty() || !cacheBooks.contains(bookBean.getPath())) {
//            try {
//                openNewBook();
//            } catch (IOException e) {
//                e.printStackTrace();
//                //不能打开
//            }
//            if (cacheBooks.size() < 3) {
//                cacheBook = cacheBook + bookPath + ",";
//            } else {
//                cacheBook = cacheBook.substring(cacheBook.indexOf(",") + 1, cacheBook.length() - 1) + bookPath + ",";
//            }
//            mSPUtils.saveString(CACHE_BOOKS, cacheBook);
//        } else {
//            openCacheBook();
//        }
        return false;
    }

    private boolean openNewBook() {
        this.bookPath = mBookBean.getPath();
        String charset = null;
        if (TextUtils.isEmpty(mBookBean.getCharset())) {
            try {
                charset = FileUtils.getCharset(bookPath);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "获取文件编码出错");
                return false;
            }
            if (charset == null) {
                charset = "utf-8";
            }
            //保存文件编码
            mBookBean.setCharset(charset);
            //此处写入数据库....
            BookDao.update(mBookBean);
        } else {
            charset = mBookBean.getCharset();
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(mBookBean.getPath()), charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(TAG, "创建读取流出错");
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        char[] buffer = new char[cacheSize];
        int index = 0;
        int bookLength = 0;

        mChapterList.clear();
        mTxtCaches.clear();
        String last = "";
        while (true) {
            try {
                int num = reader.read(buffer);
                if (num == -1) {
                    last =  ("a" + last).trim().substring(1);
                    bookLength += last.length();
                    writeToFile(index, last);
                    reader.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "读取出错");
                return false;
            }
            String str = new String(buffer);
            str = str.replaceAll("(\r\n)+\\s*", "\r\n\u3000\u3000");
            str = str.replaceAll("\u0000", "");

            str = last + str;

            if (str.length() >= cacheSize) {
                String content = str.substring(0, cacheSize);
                last = str.substring(cacheSize, str.length());
                bookLength += content.length();
                writeToFile(index, content);
                index++;
            } else {
                last = str;

            }
        }
        mBookBean.setLength(bookLength);
        BookDao.update(mBookBean);
        getChapter();
        return true;
    }

    private void writeToFile(int index, String content) {
        TxtCache txtCache = new TxtCache(new WeakReference<>(content), content.length());
        mTxtCaches.add(txtCache);
        try {
            File file = new File(cachePath(index));
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write(content.toCharArray());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提取目录
     */
    private void getChapter() {
        //目录正则表达式
        String reg = "\\s\\s第.{0,9}[章节回卷篇].{0,15}\r\n";
        Pattern pattern = Pattern.compile(reg);
        String pre = "";
        int last = 0;
        for (int i = 0; i < mTxtCaches.size(); i++) {
            int lastContent = 0;
            String s = new String(block(i));
            //考虑到章节名可能恰好被两个块分成两半,因此要把上一个块的尾巴加到这个快的前面
            String content = pre + s;
            Matcher matcher = pattern.matcher(content);
            last = cacheSize * i - pre.length();

            while (matcher.find()) {
                if (mChapterList.size() == 0) {
                    String s1 = content.substring(0, matcher.start());
                    if (s1.length() > 0) {
                        mChapterList.add(new Chapter("序", 0));
                    }
                }
                String name = matcher.group();
                name = name.substring(2, name.length() - 2);
                Chapter chapter = new Chapter(name, last + matcher.start());
                chapter.setPath(mBookBean.getPath());
                mChapterList.add(chapter);
                lastContent = matcher.end();
            }

            //记录上一个块尾部内容
            pre = content.substring(lastContent, content.length());
        }
    }

    /**
     * 获取缓存块，因为内存中的缓存串是弱引用，所以可能为空，这时就去缓存文件中重新读取
     *
     * @param
     * @return 一个缓存块大小的字符数组
     */
    private char[] block(int index) {
        if (mTxtCaches.size() == 0) {
            throw new RuntimeException("本书为空");
        }
        if (index < 0 || index > mTxtCaches.size() - 1) {
            throw new RuntimeException("超出缓存块序号");
        }
        //内存中获取
        String content = mTxtCaches.get(index).getContent().get();
        //若内存中没有
        char[] blocks = new char[cacheSize];
        if (content == null) {
            //读取缓存文件
            Log.i(TAG, "读取缓存文件");
            File file = new File(cachePath(index));
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("没找到书籍缓存:" + cachePath(index));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            try {
                //重新添加进内存
                reader.read(blocks);
                TxtCache txtCache = mTxtCaches.get(index);
                txtCache.setContent(new WeakReference<>(new String(blocks)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return content.toCharArray();
        }
        return blocks;
    }

    private boolean openCacheBook() {
        return false;
    }

    /**
     * 根据序号获取缓存块的路径
     *
     * @param index
     * @return
     */
    public String cachePath(int index) {
        return cachePath + mBookBean.getBookName() + index;
    }


    private List<String> getNextLines(int progress) {
        mReadUtil.setProgress(progress);

        LinkedList<String> lines = new LinkedList<>();
        LinkedList<Character> paragraph = new LinkedList<>();

        char lastWord = '.';
        while (lines.size() < mPageManager.getLineNum()) {
            if (mReadUtil.getProgress() >= mBookBean.getLength()) {
                reMeasure(lines, paragraph, false);
                mReadUtil.setProgress((int) mBookBean.getLength() - 1);
                break;
            }

            char word = mReadUtil.readNext();
            paragraph.add(word);

            //遇到换行,此段结束
            if (word == '\n' && lastWord == '\r') {
                reMeasure(lines, paragraph, false);
            }
            lastWord = word;
        }

        //页面内容已凑齐，记录总进度
        mCurrentPosition = mReadUtil.getProgress();
        return lines;
    }

    private List<String> getLastLines(int progress) {
        mReadUtil.setProgress(progress);

        LinkedList<String> lines = new LinkedList<>();
        LinkedList<Character> paragraph = new LinkedList<>();

        char lastWord = '.';
        while (lines.size() < mPageManager.getLineNum()) {
            if (mReadUtil.getProgress() < 0) {
                reMeasure(lines, paragraph, true);
                mReadUtil.setProgress(0);
                break;
            }

            char word = mReadUtil.readLast();
            paragraph.add(word);

            //遇到换行,此段结束
            if (word == '\r' && lastWord == '\n') {
                reMeasure(lines, paragraph, true);
            }
            lastWord = word;
        }

        //页面内容已凑齐，记录总进度
        mCurrentPosition = mReadUtil.getProgress();
        return lines;
    }

    /**
     * 向下翻页
     *
     * @param progress
     * @return
     */
    public PageTxt getPageTxtForBegin(int progress) {
        List<String> pageLines = getNextLines(progress);
        if (pageLines.size()==0) return null;
        PageTxt pageTxt = new PageTxt();
        pageTxt.setStart(progress);
        pageTxt.setLines(pageLines);
        pageTxt.setEnd(getCurrentPosition());
        pageTxt.setChapterName(getChapterName(pageTxt.getStart()));
        return pageTxt;
    }

    /**
     * 向下翻页
     *
     * @param progress
     * @return
     */
    public PageTxt getPageTxtForEnd(int progress) {
        List<String> pageLines = getLastLines(progress - 1);
        if (pageLines.size()==0) return null;
        PageTxt pageTxt = new PageTxt();
        pageTxt.setLines(pageLines);
        pageTxt.setStart(getCurrentPosition());
        pageTxt.setEnd(progress);
        pageTxt.setChapterName(getChapterName(pageTxt.getStart()));
        return pageTxt;
    }


    /**
     * 根据每段的字符串将其排版成行
     *
     * @param paCharacters
     * @param isReverse
     * @return
     */
    private void reMeasure(LinkedList<String> lines, LinkedList<Character> paCharacters, boolean isReverse) {
        if (isReverse) Collections.reverse(paCharacters);
        LinkedList<String> shortLines = new LinkedList();
        StringBuffer line = new StringBuffer();
        float width = 0;

        for (Character word : paCharacters) {
            //遇到换行就跳过，因为我们主动换行了
            if (word == '\r' || word == '\n') continue;

            //测量字符宽度
            float measureWidth = mPageManager.getPaint().measureText(word + "");
            //累积宽度大于可用宽度，结束此行，否则继续添加
            if (measureWidth + width > mPageManager.getBodyWidth()) {
                shortLines.add(line.toString());
                line = new StringBuffer();
                width = measureWidth;
            } else {
                width = measureWidth + width;
            }
            line.append(word);
        }
        paCharacters.clear();

        if (line.length() > 0)
            shortLines.add(line.toString());

        while (lines.size() < mPageManager.getLineNum() && shortLines.size() > 0) {
            if (!isReverse)
                lines.add(shortLines.removeFirst());
            else
                //将底部的shortLines往lines的头部加，，因为是shortLines需要取倒叙的内容而且要倒插到lines
                lines.addFirst(shortLines.removeLast());
        }

        //计算剩余字符
        int num = 0;
        for (String s : shortLines) {
            num += s.length();
        }

        if (!isReverse) {
            //需要加上前面消掉的 \r\n字符
            if (num > 0) num += 2;
            mReadUtil.setProgress(mReadUtil.getProgress() - num);
        } else {
            //需要加上前面消掉的 \r\n字符  还要加一是因为position要移到pagetxt的start位置去，不需要提前准备一位了
            if (num>0||lines.size()>=mPageManager.getLineNum()){
                num += 3;
            }
            mReadUtil.setProgress(mReadUtil.getProgress() + num);
        }
    }


    /**
     * 获取当前总进度
     *
     * @return
     */
    private int getCurrentPosition() {

        return mCurrentPosition;
    }

    public int getCurrentChapter() {
        return mCurrentChapter;
    }

    public String getChapterName(int progress) {
        int chapter = -1;
        for (int i = 0; i < mChapterList.size(); i++) {
            if (progress >= mChapterList.get(i).getStartPosition()) {
                chapter = i;
            }
        }
        return chapter == -1?"": mChapterList.get(chapter).getName();
    }

    public void setCurrentChapter(int progress){
        for (int i = 0; i < mChapterList.size(); i++) {
            if (progress >= mChapterList.get(i).getStartPosition()) {
                mCurrentChapter = i;
            }
        }
    }

    public PageTxt getNextChapter() {
        if (mCurrentChapter >= mChapterList.size() - 1) return null;
        return getPageTxtForBegin(mChapterList.get(++mCurrentChapter).getStartPosition());
    }

    public PageTxt getLastChapter() {
        if (mCurrentChapter <= 0) return null;
        return getPageTxtForBegin(mChapterList.get(--mCurrentChapter).getStartPosition());
    }

    public List<Chapter> getDirectory() {
        return mChapterList;
    }


    class ReadUtil {
        //总进度
        private int mProgress;
        private char[] block;
        //根据总进度，确定处于哪个块
        private int index = -1;
        //总进度处于已确定块的位置
        private int mPosition;

        public ReadUtil() {
            block = new char[cacheSize];
        }

        public char readNext() {
            char word = read();
            mProgress++;
            return word;
        }

        public char readLast() {
            char word = read();
            mProgress--;
            return word;
        }

        private char read() {
            int countIndex = mProgress / cacheSize;
            mPosition = mProgress % cacheSize;
            if (countIndex != index) {
                index = countIndex;
                block = block(index);
            }
            return block[mPosition];
        }

        public List<Chapter> getDirectoryList() {
            return mChapterList;
        }

        public void setProgress(int progress) {
            mProgress = progress;
        }

        public int getProgress() {
            return mProgress;
        }

        public String getUnicode(String source) {
            String returnUniCode = null;
            String uniCodeTemp = null;
            for (int i = 0; i < source.length(); i++) {
                uniCodeTemp = "\\u" + Integer.toHexString((int) source.charAt(i));//使用char类的charAt()的方法
                returnUniCode = returnUniCode == null ? uniCodeTemp : returnUniCode + uniCodeTemp;
            }
            System.out.print(source + "unicode = " + returnUniCode);
            return returnUniCode;
            //返回一个字符的unicode的编码值
        }
    }

    public void destroy() {
        sContentManager = null;
        mPageManager = null;
    }
}
