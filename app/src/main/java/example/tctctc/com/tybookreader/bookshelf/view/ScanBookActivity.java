package example.tctctc.com.tybookreader.bookshelf.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanBook;
import example.tctctc.com.tybookreader.bookshelf.adapter.FileScanAdapter;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.bookshelf.model.BookDao;
import example.tctctc.com.tybookreader.bookshelf.presenter.ScanPresenter;
import example.tctctc.com.tybookreader.common.FileComparater;
import example.tctctc.com.tybookreader.main.MainActivity;

import static android.R.attr.path;

public class ScanBookActivity extends BaseActivity implements FileScanAdapter.OnClickCallBack, ScanContact.View {

    @BindView(R.id.scan_result_recycle)
    RecyclerView mScanRecycle;

    @BindView(R.id.target_file_num)
    TextView targetFileNum;

    @BindView(R.id.book_all_select)
    TextView mAllSelect;

    @BindView(R.id.book_add)
    TextView mBookAdd;

    @BindView(R.id.file_operate)
    LinearLayout mFileOperate;

    @BindView(R.id.scan_toolbar)
    Toolbar mToolbar;

    private boolean isSelectAll;

    private List<ScanBook> mScanBooks;
    private FileScanAdapter mAdapter;

    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private TextView totalFile;

    private ScanPresenter mPresenter;
    private List<ScanBook> mSelectFiles;
    private BookDao mBookDao;
    private List<BookBean> mBooks;
    private int importableNum;

    private boolean isScaned;

    @Override
    protected void initView(View contextView) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("扫描书籍");

        mScanBooks = new ArrayList();
        mAdapter = new FileScanAdapter(mScanBooks, this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mScanRecycle.setLayoutManager(manager);
        mScanRecycle.setAdapter(mAdapter);


        mSelectFiles = new ArrayList<>();
        createScanDialog();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isScaned) {
            isScaned = true;
            String path = getIntent().getStringExtra("scanFilePath");
            File file = new File(path);
            mPresenter = new ScanPresenter();
            mPresenter.setVM(new BookDao(), this);
            if (file.exists()) {
                mPresenter.onStartScanBooks(file, ".txt");
            }
        }
    }

    //    private void queryFiles() {
//        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
//                MediaStore.Files.FileColumns.DATA,
//                MediaStore.Files.FileColumns.SIZE
//        };
//
//
//        // 查询后缀名为txt与pdf，并且不位于项目缓存中的文档
//        Cursor cursor = getContentResolver().query(
//                Uri.parse("content://media/external/file"),
//                projection,
//                MediaStore.Files.FileColumns.DATA + " like ? ",
//                new String[]{"%"+".txt"}, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            int idindex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
//            int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
//            int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
//
//
//            do {
//                String path = cursor.getString(dataindex);
//
//                int dot = path.lastIndexOf("/");
//                String name = path.substring(dot + 1);
//                if (name.lastIndexOf(".") > 0)
//                    name = name.substring(0, name.lastIndexOf("."));
//
//                File file = new File(path);
//                mFiles.add(file);
//
//            } while (cursor.moveToNext());
//
//            cursor.close();
//            mAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_book;
    }

    @Override
    protected void initParams() {

    }

    @OnClick({R.id.book_all_select, R.id.book_add})
    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.book_all_select:
                selectAll();
                break;
            case R.id.book_add:
                addBooks();
                break;
        }
    }

    private void addBooks() {
        mPresenter.onAddBooks(mSelectFiles);
    }

    private void selectAll() {
        HashMap<ScanBook, Integer> fileMap = mAdapter.getFileMap();
        Iterator<Map.Entry<ScanBook, Integer>> iterator = fileMap.entrySet().iterator();
        if (!isSelectAll) {
            while (iterator.hasNext()) {
                Map.Entry<ScanBook, Integer> entry = iterator.next();
                if (entry.getValue() == 2) {
                    entry.setValue(1);
                    selectFile(entry.getKey());
                }
            }
        } else {
            while (iterator.hasNext()) {
                Map.Entry<ScanBook, Integer> entry = iterator.next();
                if (entry.getValue() == 1) {
                    entry.setValue(2);
                    unSelectFile(entry.getKey());
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clickFolder(ScanBook scanBook) {
    }

    @Override
    public void selectFile(ScanBook scanBook) {
        mSelectFiles.add(scanBook);
        mBookAdd.setText("加入书架(" + mSelectFiles.size() + ")");
        if (mSelectFiles.size() == importableNum) {
            isSelectAll = true;
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        }
    }

    @Override
    public void unSelectFile(ScanBook scanBook) {
        mSelectFiles.remove(scanBook);
        if (mSelectFiles.size() < importableNum) {
            isSelectAll = false;
            mAllSelect.setText(getResources().getString(R.string.all_select));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createScanDialog() {
        mBuilder = new AlertDialog.Builder(mContext, R.style.Theme_Light_Dialog);
        View view = getLayoutInflater().inflate(R.layout.scan_dialog, null);
        mBuilder.setView(view);
        mDialog = mBuilder.create();

        //获取窗体对象
        Window window = mDialog.getWindow();
        //设置位置
        window.setGravity(Gravity.BOTTOM);
        //设置动画
        window.setWindowAnimations(R.style.enter_exit_dialog);
        //设置内边距
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获取window窗口属性
        WindowManager.LayoutParams params = window.getAttributes();
        //设置宽，高
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);

        mDialog.setCanceledOnTouchOutside(false);

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    Log.i(TAG,"back");
                    mPresenter.onStopScanBooks();
                }
                return false;
            }
        });

        totalFile = (TextView) view.findViewById(R.id.total_file);

        view.findViewById(R.id.stop_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.onStopScanBooks();
            }
        });


    }

    @Override
    public void toShelf() {
        startActivity(MainActivity.class);
        this.finish();
    }

    @Override
    public void whenStartScan() {
        mDialog.show();
    }

    @Override
    public void whenStopScan() {
        if (mBookDao == null) {
            mBookDao = new BookDao();
        }
        mBooks = mBookDao.loadAll();

        importableNum = 0;
        for (ScanBook scanBook : mScanBooks) {
            for (BookBean bookBean : mBooks) {
                if (bookBean.getPath().equals(scanBook.getFile().getAbsolutePath())) {
                    scanBook.setImported(true);
                }
            }

            if(!scanBook.isImported()){
                ++importableNum;
            }
        }
        mDialog.dismiss();
        if (importableNum==0) {
            mFileOperate.setVisibility(View.INVISIBLE);
        } else {
            mFileOperate.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh(int totalNum) {
        targetFileNum.setText("已扫描到" + mScanBooks.size() + "书本");
        totalFile.setText("已扫描文件" + totalNum + "个");
    }

    @Override
    public void whenScan(File file) {
        if (file.exists()) {
            //是否已经导入
            ScanBook scanBook = new ScanBook(file, false);
            mAdapter.getFileMap().put(scanBook, 2);
            mScanBooks.add(scanBook);
            Collections.sort(mScanBooks, new FileComparater());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
