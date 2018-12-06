package example.tctctc.com.tybookreader.bookstore.bean.site;

import android.text.TextUtils;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import example.tctctc.com.tybookreader.bookstore.bean.SearchBook;

/**
 * Function:
 * Created by tanchao on 2018/10/20.
 */

public class BiqugeSite extends BaseSite {

    public static final String SEARCH_SITE = "http://www.biquge.com.tw/modules/article/soshu.php?searchkey=";
    public static final String SITE_NAME = "笔趣阁";
    public static final String SITE = "http://www.biquge.com.tw";

    public BiqugeSite() {

    }

    @Override
    List<SearchBook> searchBooks(String keyWord) {
        List<SearchBook> searchBooks = new ArrayList<>();
        if (TextUtils.isEmpty(keyWord)) {
            return searchBooks;
        }
        try {
            String searchSite = SEARCH_SITE + keyWord;

            Document document = Jsoup.connect(searchSite).get();
            Element table = document.getElementsByTag("table").first();
            Elements bookList = table.getElementsByTag("tr");
            Log.i(TAG, "***********************  " + SITE_NAME + "  ******************************");


            for (int i = 0; i < bookList.size() && i<50; i++) {
                Element element = bookList.get(i);
                //搜索结果字段
                for (Element e : element.getAllElements()) {
                    Log.i(TAG, e.ownText());
                }

                SearchBook searchBook = new SearchBook();
                searchBook.setBookName(element.child(0).ownText());
                searchBook.setBookHome(element.child(0).child(0).attr("href"));

                searchBook.setLatestChapterName(element.child(1).ownText());
                searchBook.setLatestChapterHome(element.child(1).child(1).attr("href"));

                searchBook.setAuthor(element.child(2).ownText());
                searchBook.setUpdateStatus(element.child(5).ownText());
                searchBook.setSite(SEARCH_SITE);

                searchBooks.add(searchBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchBooks;
    }
}
