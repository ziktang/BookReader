package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.bean.Directory;

/**
 * Created by tctctc on 2017/4/7.
 * Function:
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {


    private List<Directory> mDirectoryList = new ArrayList();
    private Context mContext;
    private LayoutInflater mInflater;

    private OnDirectoryListener mDirectoryListener;

    public interface OnDirectoryListener {
        void onCLick(Directory directory);
    }


    public DirectoryAdapter(Context context, List<Directory> directories,OnDirectoryListener listener) {
        mDirectoryList = directories;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDirectoryListener = listener;
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.directory_item, null);
        return new DirectoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        holder.setData(mDirectoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDirectoryList == null ? 0 : mDirectoryList.size();
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder {

        private TextView mChapterName;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            mChapterName = (TextView) itemView.findViewById(R.id.chapterName);
        }

        public void setData(Directory directory) {
            mChapterName.setText(directory.getName());
            Log.i("bbb",directory.getName()+"  start:"+directory.getStartPosition());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDirectoryListener.onCLick(mDirectoryList.get(getAdapterPosition()));
                }
            });
        }
    }
}
