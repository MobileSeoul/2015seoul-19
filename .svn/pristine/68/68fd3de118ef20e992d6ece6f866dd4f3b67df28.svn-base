package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hany.houseofcartoon.R;

import java.util.ArrayList;

import db.entry.Book;

/**
 * Created by HanyLuv on 2015-10-23.
 */
public class CartoonAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Book> books;
    private LayoutInflater inflater;
    private int serchMode;

    private static final int SERCH_TITLE = 1010;
    private static final int SERCH_AUTHOR = 1020;
    private static final int SERCH_COMPANY = 1030;
    private Typeface typeface;

    public CartoonAdapter(Context context, ArrayList<Book> books, int serchMode) {
        this.context = context;
        this.books = books;
        this.serchMode = serchMode;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }


    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int position) {
        return books.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item, null);
            viewHolder.author = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.company = (TextView) convertView.findViewById(R.id.tv_company);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.published = (TextView) convertView.findViewById(R.id.tv_published);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String author = book.getAuthor();
        String company = book.getCompany();
        viewHolder.title.setText(book.getTitle());
        viewHolder.title.setTypeface(typeface);

        viewHolder.author.setText(author);
        viewHolder.author.setTypeface(typeface);

        viewHolder.company.setText(company);
        viewHolder.company.setTypeface(typeface);

        viewHolder.published.setText(book.getPublished());
        viewHolder.published.setTypeface(typeface);

        switch (serchMode) {
            case SERCH_TITLE:
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.select_color));
                viewHolder.author.setTextColor(context.getResources().getColor(R.color.f333333));
                viewHolder.company.setTextColor(context.getResources().getColor(R.color.f333333));
                break;
            case SERCH_COMPANY:
                viewHolder.company.setTextColor(context.getResources().getColor(R.color.select_color));
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.f333333));
                viewHolder.author.setTextColor(context.getResources().getColor(R.color.f333333));
                break;
            case SERCH_AUTHOR:
                viewHolder.author.setTextColor(context.getResources().getColor(R.color.select_color));
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.f333333));
                viewHolder.company.setTextColor(context.getResources().getColor(R.color.f333333));
                break;
        }

        return convertView;
    }

    private class ViewHolder {

        TextView title;
        TextView author;
        TextView company;
        TextView published;


    }

    public void setSerchMode(int mode) {
        serchMode = mode;

    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        this.notifyDataSetChanged();
    }
}
