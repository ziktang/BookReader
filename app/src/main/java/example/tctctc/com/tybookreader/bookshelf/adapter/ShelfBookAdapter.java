package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.bean.Book;

/**
 * Created by tctctc on 2017/3/18.
 */

public class ShelfBookAdapter extends RecyclerView.Adapter<ShelfBookAdapter.ViewHolder> {

    private List<Book> mBooks;
    private LayoutInflater mInflater;
    public Context mContext;

    //当有长按或点击发生时给父view的回调
    private OnClickCallBack mBack;

    //位置和状态 1 选中状态  2未选中状态
    private HashMap<Book, Integer> mHashMap;

    //是否处于长按选择状态
    private boolean isLongClick;
    //是否处于全选状态
    private boolean isAllSelect;
    //已选择的数量
    private int selectedNum;

    public interface OnClickCallBack {
        //长按回调
        void OnLongClick();

        //点击选择，恰好达到全不选或者全选
        void changeStatus(boolean isAllSelect);
    }

    public ShelfBookAdapter(List<Book> mBooks, Context context, OnClickCallBack mBack) {
        this.mBooks = mBooks;
        this.mContext = context;
        this.mBack = mBack;
        mInflater = LayoutInflater.from(context);
        dataInit();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shelf_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isLongClick) {
                    mHashMap = new HashMap<>();
                    for (int i = 0; i < mBooks.size(); i++) {
                        mHashMap.put(mBooks.get(i), 2);
                    }
                    notifyDataSetChanged();
                    select(holder, position);
                    isLongClick = true;
                    mBack.OnLongClick();
                }
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLongClick) {
                    //打开图书
                } else {
                    select(holder, position);
                }
            }
        });
        holder.setData(mBooks.get(position));
    }

    private void select(ViewHolder holder, Integer position) {
        Book book = mBooks.get(position);
        if (mHashMap.get(book) == 1) {
            mHashMap.put(book, 2);
            holder.mNormal.setVisibility(View.VISIBLE);
            holder.mCheck.setVisibility(View.INVISIBLE);
            selectedNum--;
        } else {
            mHashMap.put(book, 1);
            holder.mNormal.setVisibility(View.INVISIBLE);
            holder.mCheck.setVisibility(View.VISIBLE);
            selectedNum++;
        }

        if (selectedNum == mBooks.size()) {
            isAllSelect = true;
            mBack.changeStatus(isAllSelect);
        } else if (selectedNum == 0) {
            isAllSelect = false;
            mBack.changeStatus(isAllSelect);
        }
        notifyItemChanged(position);
    }

    private void show(ViewHolder holder, Integer position) {
        Book book = mBooks.get(position);
        if (mHashMap.get(book) == 1) {
            holder.mNormal.setVisibility(View.INVISIBLE);
            holder.mCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mNormal.setVisibility(View.VISIBLE);
            holder.mCheck.setVisibility(View.INVISIBLE);
        }
    }

    public void setAllSelect(boolean allSelect) {
        isAllSelect = allSelect;
        if (allSelect) selectedNum = mBooks.size();
        else selectedNum = 0;
    }

    public void dataInit() {
        isLongClick = false;
        isAllSelect = false;
        selectedNum = 0;
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public boolean isAllSelect() {
        return isAllSelect;
    }

    public HashMap<Book, Integer> getBookMap() {
        return mHashMap;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mBookName;
        ImageView mNormal;
        ImageView mCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            mBookName = (TextView) itemView.findViewById(R.id.bookName);
            mCheck = (ImageView) itemView.findViewById(R.id.checked);
            mNormal = (ImageView) itemView.findViewById(R.id.normal);
        }

        public void setData(Book book) {
            mBookName.setText(book.getName());
            if (isLongClick) {
                show(this, getAdapterPosition());
            } else {
                mNormal.setVisibility(View.INVISIBLE);
                mCheck.setVisibility(View.INVISIBLE);
            }
        }
    }
}


