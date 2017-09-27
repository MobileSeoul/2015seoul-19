package util.comparator;

import java.util.Comparator;

import db.entry.Book;

/**
 * Created by HanyLuv on 2015-10-26.
 */
public class CompanyComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return b1.getCompany().compareTo(b2.getCompany());
    }
}
