package example.tctctc.com.tybookreader.bookshelf.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseAdapter;
import example.tctctc.com.tybookreader.base.BasePageFragment;
import example.tctctc.com.tybookreader.bean.BookBean;

/**
 * Created by tctctc on 2017/4/7.
 * Function:
 */
public class NoteListFragment extends BasePageFragment {

    private BaseAdapter adapter;

    @BindView(R.id.directory_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    TextView mEmpty;


    private BookBean mBookBean;

    private NoteListFragment() {
    }

    public static NoteListFragment getInstance(BookBean book) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBookBean = (BookBean) getArguments().getSerializable("book");
        }
    }

    @Override
    protected void initView() {
//        mList = new ArrayList<>();
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        adapter = new BaseAdapter<>(getContext(), R.layout.mark_list_item, mList, new BaseAdapter.OnItem() {
//            @Override
//            public void onClick(int position) {
//            }
//
//            @Override
//            public void onLongClick(int position) {
//                showDeleteDialog();
//            }
//
//            @Override
//            public void onBind(BaseAdapter.BaseViewHolder mHolder, int position) {
//            }
//        });
//        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setLayoutManager(manager);
//
//        mRxManager.onEvent("updateNote", new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                judgeLoadData(true);
//            }
//        });
    }

    private void showDeleteDialog() {
        Toast.makeText(getContext(), "弹出删除", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.note_list_fragment;
    }

    @Override
    public boolean fetchData() {
        return true;
    }
}
