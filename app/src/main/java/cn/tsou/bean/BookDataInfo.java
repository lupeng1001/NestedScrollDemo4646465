package cn.tsou.bean;

/**
 * Created by Administrator on 2018/10/17.
 */

public class BookDataInfo extends BaseInfo{
    private String bookName;
    private String bookPrice;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }
}
