package example.tctctc.com.tybookreader.bean;

/**
 * Created by tctctc on 2017/3/18.
 */

public class Book {
    private String name;
    private String wordNum;
    private BookType mBookType;
    public Book(){
    }

    public Book(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWordNum() {
        return wordNum;
    }

    public void setWordNum(String wordNum) {
        this.wordNum = wordNum;
    }

    public BookType getBookType() {
        return mBookType;
    }

    public void setBookType(BookType bookType) {
        mBookType = bookType;
    }
}
enum BookType{
    LOCAL,
    STORE
}
