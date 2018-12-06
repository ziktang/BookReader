package example.tctctc.com.tybookreader.bookstore;

import example.tctctc.com.tybookreader.bookshelf.importBook.BaseComparator;
import example.tctctc.com.tybookreader.bookstore.bean.SearchBook;

/**
 * Function:
 * Created by tanchao on 2018/10/20.
 */

public class SearchBookComparator extends BaseComparator<SearchBook> {
    @Override
    protected int realCompare(SearchBook t1, SearchBook t2) {
        return 0;
    }
}
