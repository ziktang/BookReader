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

import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.utils.FileUtils;

/**
 * Created by tctctc on 2017/3/19.
 */

public class FileScanAdapter extends RecyclerView.Adapter<FileScanAdapter.FileViewHolder> {

    private List<File> mFiles;
    private LayoutInflater mInflater;
    public Context mContext;

    //当有长按或点击发生时给父view的回调
    private FileScanAdapter.OnClickCallBack mBack;

    //位置和状态 1 选中状态  2未选中状态
    private HashMap<File, Integer> mFileMap;

    public interface OnClickCallBack {

        void clickFolder(File file);

        void selectFile(File file);
        void unSelectFile(File file);
    }

    public FileScanAdapter(List<File> mFiles, Context context, FileScanAdapter.OnClickCallBack mBack) {
        this.mFiles = mFiles;
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
        final File file = mFiles.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file.isFile()) {
                    select(file, position);
                } else {
                    mBack.clickFolder(file);
                }
            }
        });
        holder.setData(file);
    }

    private void select(File file, Integer position) {
        if (mFileMap.get(file) == 1) {
            mFileMap.put(file, 2);
            mBack.unSelectFile(file);
        } else {
            mFileMap.put(file, 1);
            mBack.selectFile(file);
        }
        notifyItemChanged(position);
    }

    public void dataInit() {
        mFileMap = new HashMap<>();
        Iterator<File> iterator = mFiles.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (file.isFile()) {
                mFileMap.put(file, 2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFiles==null?0:mFiles.size();
    }

    public HashMap<File, Integer> getFileMap() {
        return mFileMap;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView fileImg;
        TextView fileName;
        TextView fileInfo;
        ImageView normal;
        ImageView checked;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            fileInfo = (TextView) itemView.findViewById(R.id.fileInfo);
            fileImg = (ImageView) itemView.findViewById(R.id.file_img);
            normal = (ImageView) itemView.findViewById(R.id.normal);
            checked = (ImageView) itemView.findViewById(R.id.checked);
        }

        public void setData(File file) {
            if (file.isFile()) {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__txt_icon));
                fileInfo.setText(FileUtils.getFileSize(file));
                if (mFileMap.get(file) == 1) {
                    normal.setVisibility(View.INVISIBLE);
                    checked.setVisibility(View.VISIBLE);
                } else {
                    normal.setVisibility(View.VISIBLE);
                    checked.setVisibility(View.INVISIBLE);
                }
            } else {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__folder_icon));
                fileInfo.setText(FileUtils.listFilterOneFolder(file, ".txt").size() + "项");
                normal.setVisibility(View.INVISIBLE);
                checked.setVisibility(View.INVISIBLE);
            }
            fileName.setText(file.getName().trim());
        }
    }
}
