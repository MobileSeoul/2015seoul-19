package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hany.houseofcartoon.R;

import java.util.ArrayList;

import adapter.RelationExpandableListAdapter;
import db.CartoonDAOImpl;
import db.entry.Book;
import util.config.FindTarget;
import util.config.Key;

/**
 * Created by HanyLuv on 2015-10-28.
 */

public class RelationFragment extends Fragment {

    private ArrayList<String> groupList;
    private ArrayList<ArrayList<Book>> childList;

    //관련 제목
    private ArrayList<Book> childRelationTitleList;
    //관련작가
    private ArrayList<Book> childRelationAuthorList;
    //관련회사
    private ArrayList<Book> childRelationCompanyList;

    private ExpandableListView relationListView;

    private Button btnListtoTop; //리스트 상단으로 가기 버튼


    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.relation_layout, null);
        relationListView = (ExpandableListView) view.findViewById(R.id.relation_list);
        Bundle bundle = getArguments();

        Book book = (Book) bundle.getSerializable(Key.SELECT_BOOK);

        groupList = new ArrayList<>();
//        groupList.add(getContext().getResources().getString(R.string.relation_title));
        groupList.add(getContext().getResources().getString(R.string.relation_author));
        groupList.add(getContext().getResources().getString(R.string.relation_company));

//        childRelationTitleList = getRelationData(FindTarget.TITLE, book.getTitle());
        childRelationAuthorList = getRelationData(FindTarget.AUTHOR, book.getAuthor());
        childRelationCompanyList = getRelationData(FindTarget.COMPANY, book.getCompany());

        childList = new ArrayList<>();

//        childList.add(childRelationTitleList);
        childList.add(childRelationAuthorList);
        childList.add(childRelationCompanyList);
        relationListView = (ExpandableListView) view.findViewById(R.id.relation_list);
        RelationExpandableListAdapter relationExpandableListAdapter = new RelationExpandableListAdapter(getContext(), groupList, childList);
        relationListView.setAdapter(relationExpandableListAdapter);
        for (int i = 0; i < groupList.size(); i++) {
            relationListView.expandGroup(i);
        }
        btnListtoTop = (Button) view.findViewById(R.id.btn_list_to_top);

        btnListtoTop.setOnClickListener(topClickListner);
        relationListView.setOnScrollListener(onScrollListener);

        return view;
    }

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
            if (RelationFragment.this.firstVisibleItem != firstVisibleItem) {
                if (visibleItemCount < firstVisibleItem) {
                    btnListtoTop.setVisibility(View.VISIBLE);
                } else {
                    btnListtoTop.setVisibility(View.GONE);
                }
            }
            RelationFragment.this.firstVisibleItem = firstVisibleItem;
        }
    };

    /***
     * 리스트 최상단으로 가기
     */
    private View.OnClickListener topClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (relationListView != null) {
                relationListView.setSelectionAfterHeaderView();
            }
        }
    };

    private ArrayList<Book> getRelationData(FindTarget findTarget, String findVale) {
        CartoonDAOImpl.getInstance().connectDB(getContext());
        ArrayList<Book> books = CartoonDAOImpl.getInstance().findData(findTarget, findVale);
        CartoonDAOImpl.getInstance().closeDB();
        return books;
    }


}
