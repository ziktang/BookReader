package example.tctctc.com.tybookreader.base;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tctctc on 2017/4/7.
 * Function:
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {


    private List<T> mList = new ArrayList();
    private Context mContext;
    private LayoutInflater mInflater;
    private int resId;
    public OnItem mOnItem;

    public interface OnItem<T> {
        void onClick(int position);

        void onLongClick(int position);

        void onBind(BaseAdapter.BaseViewHolder mHolder, int position);
    }

    public BaseAdapter(Context context, int resId, List<T> list, OnItem onItem) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mOnItem = onItem;
        this.resId = resId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(resId, null);
        return new BaseViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.BaseViewHolder holder, int position) {
        mOnItem.onBind(holder, position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SparseArray<View> mArray;

        private Context mContext;
        private View mView;


        public BaseViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mView = itemView;
            mArray = new SparseArray<>();

            mView.setOnClickListener(this);
        }

        public View getView(int ResId) {
            View view = mArray.get(ResId);
            if (view == null) {
                view = mView.findViewById(ResId);
                mArray.put(ResId, view);
            }
            return view;
        }

        public void setText(int resId, String content) {
            TextView textView = (TextView) getView(resId);
            textView.setText(content);
        }

        public void setText(int resId, String content, Typeface typeface) {
            TextView textView = (TextView) getView(resId);
            textView.setTypeface(typeface);
            textView.setText(content);
        }

        public void setText(int resId, String content, int color) {
            TextView textView = (TextView) getView(resId);
            textView.setTextColor(color);
            textView.setText(content);
        }


        @Override
        public void onClick(View v) {
            mOnItem.onClick(getAdapterPosition());
        }
    }
}
