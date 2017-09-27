package db;

/**
 * Created by HanyLuv on 2015-10-24.
 */
public interface ICartoonDAO {
    String DATABASE_NAME = "cartoondb";
    String TABLE_NAME = "cartoon";
    String PACKAGE_DIR = "/data/data/com.hany.houseofcartoon/databases/";
    int DATABASE_VERSION = 1;


    String _ID = "_id";
    String BID = "bid";
    String TITLE = "title";
    String AUTHOR = "author";
    String COMPANY = "company";
    String PUBLISHED = "published";
    String IBOOK_NUMBER = "InternationalBookNumber";

}
