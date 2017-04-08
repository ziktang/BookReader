package example.tctctc.com.tybookreader.bookshelf.view;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bookshelf.adapter.FileScanAdapter;
import example.tctctc.com.tybookreader.bookshelf.contact.ScanContact;
import example.tctctc.com.tybookreader.bookshelf.model.ScanDao;
import example.tctctc.com.tybookreader.bookshelf.presenter.ScanPresenter;
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

    private List<File> mFiles;
    private FileScanAdapter mAdapter;

    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private TextView totalFile;

    private ScanPresenter mPresenter;
    private List<File> mSelectFiles;

    @Override
    protected void initView(View contextView) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mFiles = new ArrayList();
        mAdapter = new FileScanAdapter(mFiles, this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mScanRecycle.setLayoutManager(manager);
        mScanRecycle.setAdapter(mAdapter);


        mSelectFiles = new ArrayList<>();
        createScanDialog();

        String path = getIntent().getStringExtra("scanFilePath");
        File file = new File(path);
        mPresenter = new ScanPresenter();
        mPresenter.setVM(new ScanDao(),this);
        if (file.exists()){
            mPresenter.onStartScanBooks(file, ".txt");
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

    @OnClick({R.id.book_all_select,R.id.book_add})
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
        HashMap<File, Integer> fileMap = mAdapter.getFileMap();
        Iterator<Map.Entry<File, Integer>> iterator = fileMap.entrySet().iterator();
        if (mSelectFiles.size() == mFiles.size()) {
            while (iterator.hasNext()) {
                Map.Entry<File, Integer> entry = iterator.next();
                entry.setValue(1);
                selectFile(entry.getKey());
            }
        } else {
            while (iterator.hasNext()) {
                Map.Entry<File, Integer> entry = iterator.next();
                entry.setValue(2);
                unSelectFile(entry.getKey());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clickFolder(File file) {
    }

    @Override
    public void selectFile(File file) {
        if (!mSelectFiles.contains(file))
            mSelectFiles.add(file);
        mBookAdd.setText("加入书架("+mSelectFiles.size()+")");
        if (mSelectFiles.size() == mFiles.size()){
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        }
    }

    @Override
    public void unSelectFile(File file) {
        if (mSelectFiles.contains(file))
            mSelectFiles.remove(file);
        mBookAdd.setText("加入书架("+mSelectFiles.size()+")");
        if (mSelectFiles.size() < mFiles.size()){
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
    }

    @Override
    public void whenStartScan() {
        mDialog.show();
        mFileOperate.setVisibility(View.INVISIBLE);
    }

    @Override
    public void whenStopScan() {
        mDialog.dismiss();
        mFileOperate.setVisibility(View.VISIBLE);
    }

    @Override
    public void whenScan(File file, int totalNum) {
        if (file.exists()){
            mAdapter.getFileMap().put(file, 2);
            mFiles.add(file);
            targetFileNum.setText("已扫描到" + mFiles.size() + "书本");
            mAdapter.notifyItemInserted(mFiles.size());
        }
        totalFile.setText("已扫描文件" + totalNum + "个");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null)
            mPresenter.onDestroy();
        mPresenter = null;
    }
}
