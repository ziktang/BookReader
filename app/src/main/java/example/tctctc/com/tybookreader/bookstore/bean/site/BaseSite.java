package example.tctctc.com.tybookreader.bookstore.bean.site;


import java.util.List;
import example.tctctc.com.tybookreader.bookstore.bean.SearchBook;

/**
 * Function:
 * Created by tanchao on 2018/10/20.
 */

public abstract class BaseSite {

    public final String TAG = this.getClass().getSimpleName();
    public abstract List<SearchBook> searchBooks(String keyWord);
}
