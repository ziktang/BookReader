package example.tctctc.com.tybookreader.bookshelf.view;

import android.content.Context;
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
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bean.ScanFile;
import example.tctctc.com.tybookreader.bookshelf.adapter.FileScanAdapter;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.bookshelf.model.BookDao;
import example.tctctc.com.tybookreader.bookshelf.presenter.ScanPresenter;
import example.tctctc.com.tybookreader.bookshelf.importBook.ScanFileComparater;
import example.tctctc.com.tybookreader.main.MainActivity;

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

    private List<ScanFile> mScanFiles;
    private FileScanAdapter mAdapter;

    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;

    private ScanPresenter mPresenter;
    private List<ScanFile> mSelectFiles;
    private int importableNum;
    private boolean isScaned;

    @Override
    protected void initView(View contextView) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("扫描书籍");

        mScanFiles = new ArrayList();
        mAdapter = new FileScanAdapter(mScanFiles, this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mScanRecycle.setLayoutManager(manager);
        mScanRecycle.setAdapter(mAdapter);
        mSelectFiles = new ArrayList<>();
        String text = String.format(getString(R.string.scan_book_num),mScanFiles.size()+"");
        targetFileNum.setText(text);
        createScanDialog();
        Log.i(TAG,"thread id  main :"+Thread.currentThread().getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isScaned) {
            isScaned = true;
            String path = getIntent().getStringExtra("scanFilePath");
            File file = new File(path);
            if (!file.exists()||file.isFile()){
                Log.i(TAG,"file is not a directory,file path:"+file.getAbsolutePath());
                return;
            }
            mPresenter = new ScanPresenter();
            mPresenter.setVM(new BookDao(), this);
            mPresenter.onStartScanBooks(file, getString(R.string.file_type_txt));
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

    /**
     * 全选或全不选
     */
    private void selectAll() {
        if (mSelectFiles.size() >= importableNum){
            for (ScanFile scanFile : mScanFiles) {
                scanFile.setChecked(false);
            }
            mSelectFiles.clear();
        }else{
            mSelectFiles.clear();
            for (ScanFile scanFile : mScanFiles) {
                if (scanFile.getFile()!=null && scanFile.getFile().isFile()){
                    scanFile.setChecked(true);
                    mSelectFiles.add(scanFile);
                }
            }
        }
        mAdapter.notifyDataSetChanged();

        updateSelectView();
    }

    private void updateSelectView(){
        String text = String.format(getString(R.string.add_to_bookshelf),mSelectFiles.size()+"");
        mBookAdd.setText(text);
        if (mSelectFiles.size() >= importableNum){
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        }else{
            mAllSelect.setText(getResources().getString(R.string.all_select));
        }
        if (mSelectFiles.size() == 0){
            mBookAdd.setClickable(false);
        }else {
            mBookAdd.setClickable(true);
        }
    }

    @Override
    public void clickFolder(ScanFile scanFile) {
    }

    @Override
    public void selectFile(ScanFile scanFile) {
        mSelectFiles.add(scanFile);
        updateSelectView();
    }

    @Override
    public void unSelectFile(ScanFile scanFile) {
        mSelectFiles.remove(scanFile);
        updateSelectView();
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
                    mPresenter.onStopScanBooks();
                }
                return false;
            }
        });
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
        importableNum = 0;
        mScanFiles.clear();
        mDialog.show();
    }

    @Override
    public void whenStopScan() {
        mDialog.dismiss();
        if (importableNum==0) {
            mFileOperate.setVisibility(View.INVISIBLE);
        } else {
            mFileOperate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void whenScan(ScanFile scanFile) {
        if (scanFile!=null) {
            Log.i(TAG,"whenScan"+scanFile);
            //是否已经导入
            mScanFiles.add(scanFile);
            if (!scanFile.isImported()){
                importableNum++;
            }
            Collections.sort(mScanFiles, new ScanFileComparater());
            mAdapter.notifyDataSetChanged();
            String text = String.format(getString(R.string.scan_book_num),mScanFiles.size()+"");
            targetFileNum.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private boolean icContainScanFile(List<ScanFile> mScanFiles,ScanFile scanFile){
        if (mScanFiles == null || mScanFiles.size() == 0 || scanFile == null){
            return false;
        }

        for (ScanFile s:mScanFiles){
            if (s.getFile() != null && s.getFile().exists() && scanFile.getFile()!=null && s.getFile().getAbsolutePath().equals(scanFile.getFile().getAbsolutePath())){
                return true;
            }
        }
        return false;
    }

}
