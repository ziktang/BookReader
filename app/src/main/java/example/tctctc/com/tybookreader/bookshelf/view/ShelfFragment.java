package example.tctctc.com.tybookreader.bookshelf.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseFragment;
import example.tctctc.com.tybookreader.bean.Book;
import example.tctctc.com.tybookreader.bookshelf.adapter.ShelfBookAdapter;
import example.tctctc.com.tybookreader.common.view.GridSpacingItemDecoration;

/**
 * Created by tctctc on 2017/3/18.
 */

public class ShelfFragment extends BaseFragment implements ShelfBookAdapter.OnClickCallBack {



    @BindView(R.id.bookRecycle)
    RecyclerView mBookRecycle;

    @BindView(R.id.add_book)
    FloatingActionButton mAddBookBt;

    @BindView(R.id.book_operate)
    LinearLayout mBookOperate;

    @BindView(R.id.book_delete)
    TextView mBookDelete;

    @BindView(R.id.book_all_select)
    TextView mBookAllSelect;
    private ShelfBookAdapter adapter;
    private List<Book> books;

    @Override
    protected void initView() {
        mAddBookBt.setOnClickListener(this);
        mBookDelete.setOnClickListener(this);
        mBookAllSelect.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        books = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            books.add(new Book("书本" + i));
        }
        adapter = new ShelfBookAdapter(books, getActivity(), this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mBookRecycle.setLayoutManager(layoutManager);
        mBookRecycle.setAdapter(adapter);
        mBookRecycle.setLongClickable(true);
        mBookRecycle.addItemDecoration(new GridSpacingItemDecoration(3, 80, true));
    }

    @Override
    public void OnLongClick() {
        mBookOperate.setVisibility(View.VISIBLE);
        mAddBookBt.setVisibility(View.INVISIBLE);
    }

    @Override
    public void changeStatus(boolean isAllSelect) {
        if (adapter.isAllSelect()) {
            mBookAllSelect.setText("全不选");
        } else {
            mBookAllSelect.setText("全选");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_delete:
                deleteBook(adapter.getBookMap());
                showToast("删除");
                break;
            case R.id.book_all_select:
                allSelectBook(adapter.getBookMap());
                showToast("全选");
                break;
            case R.id.add_book:
                Intent intent = new Intent(getActivity(),ImportBookActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void allSelectBook(HashMap<Book, Integer> bookMap) {
        //本来是全不选状态,此时按钮应该显示的是全选，所以点击之后变成全不选
        if (!adapter.isAllSelect()) {
            for (Map.Entry<Book, Integer> entry : bookMap.entrySet()) {
                if (entry.getValue() == 2) {
                    entry.setValue(1);
                }
            }
            mBookAllSelect.setText("全不选");
            adapter.setAllSelect(true);
        }
        //本来是全选状态,此时按钮应该显示的是全不选，所以点击之后变成全选
        else if (adapter.isAllSelect()) {
            for (Map.Entry<Book, Integer> entry : bookMap.entrySet()) {
                if (entry.getValue() == 1) {
                    entry.setValue(2);
                }
            }
            mBookAllSelect.setText("全选");
            adapter.setAllSelect(false);
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteBook(HashMap<Book, Integer> bookMap) {
        //map在普通遍历时不能执行修改，删除等操作，会引起线程不安全，因此想要达到目的需要用Iterator遍历
        Iterator<Map.Entry<Book,Integer>> iterator = bookMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Book,Integer> entry = iterator.next();
            if (entry.getValue() == 1) {
                books.remove(((Book)entry.getKey()));
                iterator.remove();
            }
        }
        adapter.dataInit();
        adapter.notifyDataSetChanged();
        mBookOperate.setVisibility(View.INVISIBLE);
        mBookAllSelect.setText("全选");
        mAddBookBt.setVisibility(View.VISIBLE);
    }
}
