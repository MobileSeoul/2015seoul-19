package fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hany.houseofcartoon.MainActivity;
import com.hany.houseofcartoon.R;

import java.util.ArrayList;
import java.util.Collections;

import adapter.CartoonAdapter;
import db.CartoonDAOImpl;
import db.entry.Book;
import util.comparator.AuthorComparator;
import util.comparator.CompanyComparator;
import util.comparator.DateComparator;
import util.comparator.TitleComparator;
import util.config.FindTarget;
import util.config.Key;

/**
 * Created by HanyLuv on 2015-10-25.
 */
public class SerchFragment extends Fragment {
    private ListView listView;
    private ArrayList<Book> books;
    private LinearLayout serchResultLayout;
    private TextView noSerchData;

    private CartoonAdapter cAdapter;
    private Button findTitle;
    private Button findAuthor;
    private Button findCompany;
    private String serchKey;

    private TextView txtSerchResultCout; // 총 검색 결과
    private TextView toggleText; // 가나다 정렬토글
    private TextView toggleData; // 최신순 정렬토글

    private Button btnListtoTop; //리스트 상단으로 가기 버튼

    private static final int TOGGLE_TEXT = 2010;
    private static final int TOGGLE_DATA = 2020;
    private static int TOGGLE_MODE = TOGGLE_TEXT;

    private static final int SERCH_TITLE = 1010;
    private static final int SERCH_AUTHOR = 1020;
    private static final int SERCH_COMPANY = 1030;
    private static int SERCH_MODE = SERCH_TITLE;
    private Typeface typeface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serch_result_layout, null);
        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setGlobalFont(view);
        SERCH_MODE = SERCH_TITLE;
        serchKey = getArguments().getString(Key.SERCH_KEY);
        initView(view);
        setSerchButton(SERCH_MODE);
        /**검색 결과에 따라 리스트뷰 set 해쥼.*/
        books = serch(FindTarget.TITLE, serchKey);
        updateAdapter(books);
        return view;
    }

    /**
     * 폰트 일괄 적용
     */
    private void setGlobalFont(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int vgCnt = vg.getChildCount();
                for (int i = 0; i < vgCnt; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(typeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.serch_result_listview);
        serchResultLayout = (LinearLayout) view.findViewById(R.id.serch_result_layout);
        noSerchData = (TextView) view.findViewById(R.id.no_serch_data);

        findTitle = (Button) view.findViewById(R.id.bt_title_serch);
        findAuthor = (Button) view.findViewById(R.id.bt_author_serch);
        findCompany = (Button) view.findViewById(R.id.bt_company_serch);
        txtSerchResultCout = (TextView) view.findViewById(R.id.tv_serch_result_count);

        toggleText = (TextView) view.findViewById(R.id.sort_toggle_txt_sort);
        toggleData = (TextView) view.findViewById(R.id.sort_toggle_data_sort);
        btnListtoTop = (Button) view.findViewById(R.id.btn_list_to_top);

        btnListtoTop.setOnClickListener(topClickListner);
        toggleText.setOnClickListener(toggleClickListener);
        toggleData.setOnClickListener(toggleClickListener);

        findTitle.setOnClickListener(clickListener);
        findAuthor.setOnClickListener(clickListener);
        findCompany.setOnClickListener(clickListener);
        listView.setOnScrollListener(onScrollListener);

    }

    /**
     * 리스트뷰 맨 처음으로 옮기기
     */
    int firstVisibleItem;
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                if (btnListtoTop.getVisibility() == View.VISIBLE) {
                    btnListtoTop.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnListtoTop.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //상단에 보여지는 항목의 인덱스, 현재리스트뷰에서 보여지는 항목의 수 리스트뷰의 총수
            if (SerchFragment.this.firstVisibleItem != firstVisibleItem) {
                if (visibleItemCount < firstVisibleItem) {
                    btnListtoTop.setVisibility(View.VISIBLE);
                } else {
                    btnListtoTop.setVisibility(View.GONE);
                }
            }
            SerchFragment.this.firstVisibleItem = firstVisibleItem;
        }
    };

    private void setSerchButton(int serchType) {
        switch (serchType) {
            case SERCH_AUTHOR:
                findAuthor.setBackgroundResource(R.color.select_color);
                findTitle.setBackgroundResource(R.color.no_select_color);
                findCompany.setBackgroundResource(R.color.no_select_color);
                break;
            case SERCH_TITLE:
                findTitle.setBackgroundResource(R.color.select_color);
                findAuthor.setBackgroundResource(R.color.no_select_color);
                findCompany.setBackgroundResource(R.color.no_select_color);
                break;
            case SERCH_COMPANY:
                findCompany.setBackgroundResource(R.color.select_color);
                findAuthor.setBackgroundResource(R.color.no_select_color);
                findTitle.setBackgroundResource(R.color.no_select_color);
                break;
        }
    }

    private void setView(boolean hasResult) {
        if (!hasResult) {
            listView.setVisibility(View.GONE);
            noSerchData.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            noSerchData.setVisibility(View.GONE);
            if (cAdapter != null) {
                listView.setAdapter(cAdapter);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Key.SELECT_BOOK, books.get(position));
                    RelationFragment relationFragment = new RelationFragment();
                    relationFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().add(R.id.fragment_layout, relationFragment).addToBackStack("").commit();
                    ((MainActivity) getActivity()).getEtSerch().setEnabled(false);
                    ((MainActivity) getActivity()).getClearEtSerch().setEnabled(false);
                }
            });
        }
    }

    private ArrayList<Book> serch(FindTarget findTarget, String key) {
        ArrayList<Book> books = null;
        CartoonDAOImpl.getInstance().connectDB(getContext());
        books = CartoonDAOImpl.getInstance().findData(findTarget, key);
        CartoonDAOImpl.getInstance().closeDB();
        return books;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TOGGLE_MODE = TOGGLE_DATA;
            setToggle();
            switch (v.getId()) {
                /**도서명 검색*/
                case R.id.bt_title_serch:
                    SERCH_MODE = SERCH_TITLE;
                    books = serch(FindTarget.TITLE, serchKey);
                    updateAdapter(books);
                    break;
                /**작가 이름 검색*/
                case R.id.bt_author_serch:
                    SERCH_MODE = SERCH_AUTHOR;
                    books = serch(FindTarget.AUTHOR, serchKey);
                    updateAdapter(books);
                    break;
                /**출판사 검색*/
                case R.id.bt_company_serch:
                    SERCH_MODE = SERCH_COMPANY;
                    books = serch(FindTarget.COMPANY, serchKey);
                    updateAdapter(books);
                    break;
            }
            setSerchButton(SERCH_MODE);
        }
    };

    /**
     * 토글 리스너
     */
    private View.OnClickListener toggleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            setToggle();

            switch (v.getId()) {
                //가나다
                case R.id.sort_toggle_txt_sort:
                    if (books != null && books.size() != 0) {
                        sortList(books);
                    }
                    break;
                //최신순
                case R.id.sort_toggle_data_sort:
                    if (books != null && books.size() != 0) {
                        sortList(books);
                    }
                    break;
            }

        }
    };

    /***
     * 리스트 최상단으로 가기
     */
    private View.OnClickListener topClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listView != null) {
                listView.setSelectionAfterHeaderView();
            }
        }
    };

    private void setToggle() {
        switch (TOGGLE_MODE) {
            case TOGGLE_TEXT:
                TOGGLE_MODE = TOGGLE_DATA;
                toggleData.setVisibility(View.VISIBLE);
                toggleText.setVisibility(View.GONE);
                break;

            case TOGGLE_DATA:
                TOGGLE_MODE = TOGGLE_TEXT;
                toggleData.setVisibility(View.GONE);
                toggleText.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void sortList(ArrayList<Book> books) {
        switch (SERCH_MODE) {
            case SERCH_AUTHOR:
                if (TOGGLE_MODE == TOGGLE_TEXT) {
                    Collections.sort(books, new AuthorComparator());
                } else if (TOGGLE_MODE == TOGGLE_DATA) {
                    Collections.sort(books, new DateComparator());
                }
                break;
            case SERCH_COMPANY:
                if (TOGGLE_MODE == TOGGLE_TEXT) {
                    Collections.sort(books, new CompanyComparator());
                } else if (TOGGLE_MODE == TOGGLE_DATA) {
                    Collections.sort(books, new DateComparator());
                }
                break;
            case SERCH_TITLE:
                if (TOGGLE_MODE == TOGGLE_TEXT) {
                    Collections.sort(books, new TitleComparator());
                } else if (TOGGLE_MODE == TOGGLE_DATA) {
                    Collections.sort(books, new DateComparator());
                }
                break;
        }
        updateAdapter(books);
    }

    private void updateAdapter(ArrayList<Book> books) {
        String totalCount = "0";
        if (books.size() != 0) {
            totalCount = Integer.toString(books.size());
            if (cAdapter == null) {
                cAdapter = new CartoonAdapter(getContext(), books, SERCH_MODE);
            } else {
                cAdapter.setSerchMode(SERCH_MODE);
                cAdapter.setBooks(books);
                cAdapter.notifyDataSetChanged();
            }
            setView(true);
        } else {
            setView(false);
        }
        txtSerchResultCout.setText(totalCount);
    }

}