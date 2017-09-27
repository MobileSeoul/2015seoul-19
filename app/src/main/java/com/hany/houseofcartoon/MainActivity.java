package com.hany.houseofcartoon;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import db.CartoonDAOImpl;
import fragment.MainFragment;
import fragment.SerchFragment;
import util.config.Key;
import util.textwatcher.SerchTextWatcher;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView etSerch;
    private TextView clearEtSerch;
    //    private Button btnSerch;
    private FragmentManager fManger;

    private ArrayList<String> allBooks;
    private ProgressBar progressBar;
    private FrameLayout blindLayout;
    private Typeface typeface;

    public AutoCompleteTextView getEtSerch() {
        return etSerch;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Light.ttf");
        fManger = getSupportFragmentManager();
        fManger.beginTransaction().add(R.id.fragment_layout, new MainFragment()).commit();
        init();
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        blindLayout = (FrameLayout) findViewById(R.id.blind_layout);
        clearEtSerch = (TextView) findViewById(R.id.clear_ed_view);
        clearEtSerch.setTypeface(typeface);
        clearEtSerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSerch.setText("");
            }
        });
        etSerch = (AutoCompleteTextView) findViewById(R.id.et_serch);
        etSerch.setTypeface(typeface);
        etSerch.setEnabled(false);
        asyncTask.execute();
    }


    private void doSerch() {
        String serchKey = etSerch.getText().toString();
        if (TextUtils.isEmpty(serchKey) || serchKey.length() < 2) {
            Toast.makeText(getApplicationContext(), "2자 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (spaceCheck(serchKey)) {
            Toast.makeText(getApplicationContext(), "공백만 검색 할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Key.SERCH_KEY, serchKey);
            SerchFragment serchFragment = new SerchFragment();
            serchFragment.setArguments(bundle);

            for (Fragment fragment : fManger.getFragments()) {
                if (fragment instanceof SerchFragment) {
                    fManger.beginTransaction().remove(fragment).commit();
                    fManger.popBackStack();
                }
            }

            fManger.beginTransaction().add(R.id.fragment_layout, serchFragment).addToBackStack("").commit();
            hideKeyBoard();
        }
    }

    private boolean spaceCheck(String string) {
        char aChar[] = string.toCharArray();
        int count = 0;
        for (char ch : aChar) {
            if (ch == ' ') {
                count++;
            }
        }
        return aChar.length == count ? true : false;
    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            etSerch.dismissDropDown();
            doSerch();
        }
    };


    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            /** 키보드 액션도 KEY_DOWN 이벤트와 KEY_UP 이벤트 두가지가 발생한다.
             * 때문에 키보드를 엔터를 눌렀으면서 키보드에서 터치 업을 할때 앤드 연산을 해줘야 onKey가 2번 먹는 현상을 방지할수있다.*/

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                etSerch.dismissDropDown();
                doSerch();
            }

            return false;
        }
    };

    /**
     * 키보드 숨기기
     */
    private void hideKeyBoard() {
        ((InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etSerch.getWindowToken(), 0);
    }

    /**
     * 자동완성을 위한 타이틀을가져온다.
     */
    private ArrayList<String> getSerchTitleList() {
        ArrayList<String> allBooks = null;
        CartoonDAOImpl.getInstance().connectDB(this);
        allBooks = CartoonDAOImpl.getInstance().getDataAll();
        CartoonDAOImpl.getInstance().closeDB();
        return allBooks;
    }

    /**
     * 검색완성 리스트 셋해줌.
     */
    private AsyncTask<Void, Void, ArrayList<String>> asyncTask = new AsyncTask<Void, Void, ArrayList<String>>() {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            allBooks = getSerchTitleList();
            return allBooks;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressBar.setVisibility(View.GONE);
            blindLayout.setVisibility(View.GONE);
            etSerch.setEnabled(true);
            etSerch.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, allBooks) {
                                   @Override
                                   public View getView(int position, View convertView, ViewGroup parent) {
                                       View view = null;
                                       TextView textView = null;
                                       if (convertView == null) {
                                           view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                                       } else {
                                           view = convertView;
                                       }
                                       textView = (TextView) view.findViewById(android.R.id.text1);
                                       textView.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Light.ttf"));
                                       textView.setTextColor(Color.GRAY);
                                       return super.getView(position, view, parent);
                                   }
                               }

            );
            etSerch.setOnKeyListener(onKeyListener);
            etSerch.addTextChangedListener(new SerchTextWatcher());
            etSerch.setTextColor(Color.BLACK);
            etSerch.setOnItemClickListener(clickListener);
            ( (TextView) findViewById(R.id.txt_02)).setText(getResources().getString(R.string.total_book_count));
            super.onPostExecute(strings);
        }
    };

    public TextView getClearEtSerch() {
        return clearEtSerch;
    }

    @Override
    public void onBackPressed() { /**검색 메인으로 왔을때 필드 값 지움*/
        super.onBackPressed();
        if (!getEtSerch().isEnabled() && !getClearEtSerch().isEnabled()) {
            getEtSerch().setEnabled(true);
            getClearEtSerch().setEnabled(true);
        }
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            if (fragments.get(getSupportFragmentManager().getBackStackEntryCount()) instanceof MainFragment) {
                etSerch.setText("");
            }
        }
    }

}
