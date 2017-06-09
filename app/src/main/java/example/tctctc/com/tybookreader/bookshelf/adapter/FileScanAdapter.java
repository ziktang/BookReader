package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.bean.ScanBook;
import example.tctctc.com.tybookreader.utils.FileUtils;

/**
 * Created by tctctc on 2017/3/19.
 */

public class FileScanAdapter extends RecyclerView.Adapter<FileScanAdapter.FileViewHolder> {

    private List<ScanBook> mScanBooks;
    private LayoutInflater mInflater;
    public Context mContext;

    //当有长按或点击发生时给父view的回调
    private FileScanAdapter.OnClickCallBack mBack;

    //位置和状态 1 选中状态  2未选中状态
    private HashMap<ScanBook, Integer> mFileMap;

    public interface OnClickCallBack {

        void clickFolder(ScanBook scanBook);

        void selectFile(ScanBook scanBook);

        void unSelectFile(ScanBook scanBook);
    }

    public FileScanAdapter(List<ScanBook> mScanBooks, Context context, FileScanAdapter.OnClickCallBack mBack) {
        this.mScanBooks = mScanBooks;
        this.mContext = context;
        this.mBack = mBack;
        mInflater = LayoutInflater.from(context);
        dataInit();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.file_item, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {
        final ScanBook scanBook = mScanBooks.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (scanBook.getFile().isFile()) {
                    select(scanBook, position);
                } else {
                    mBack.clickFolder(scanBook);
                }
            }
        });
        holder.setData(scanBook);
    }

    private void select(ScanBook scanBook, Integer position) {
        if(scanBook.isImported()) return;
        if (mFileMap.get(scanBook) == 1) {
            mFileMap.put(scanBook, 2);
            mBack.unSelectFile(scanBook);
        } else {
            mFileMap.put(scanBook, 1);
            mBack.selectFile(scanBook);
        }
        notifyItemChanged(position);
    }

    public void dataInit() {
        mFileMap = new HashMap<>();
        Iterator<ScanBook> iterator = mScanBooks.iterator();
        while (iterator.hasNext()) {
            ScanBook scanBook = iterator.next();
            if (scanBook.getFile().isFile()) {
                mFileMap.put(scanBook, 2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mScanBooks == null ? 0 : mScanBooks.size();
    }

    public HashMap<ScanBook, Integer> getFileMap() {
        return mFileMap;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.file_img)
        ImageView fileImg;

        @BindView(R.id.fileName)
        TextView fileName;
        @BindView(R.id.fileInfo)
        TextView fileInfo;
        @BindView(R.id.normal)
        ImageView normal;
        @BindView(R.id.checked)
        ImageView checked;
        @BindView(R.id.isImport)
        TextView isImported;

        public FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ScanBook scanBook) {
            File file = scanBook.getFile();
            fileName.setText(file.getName().trim());
            if (file.isFile()) {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__txt_icon));
                fileInfo.setText(FileUtils.getFileSize(file));
                if (scanBook.isImported()) {
                    show(View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                    return;
                }
                if (mFileMap.get(scanBook) == 1) {
                    show(View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                } else {
                    show(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                }
            } else {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__folder_icon));
                fileInfo.setText(FileUtils.listFilterOneFolder(file, ".txt").size() + "项");
                show(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
            }
        }

        private void show(int checkedVisible, int normalVisible, int isImportedVisible) {
            checked.setVisibility(checkedVisible);
            normal.setVisibility(normalVisible);
            isImported.setVisibility(isImportedVisible);
        }
    }

}
