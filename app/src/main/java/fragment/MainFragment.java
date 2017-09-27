package fragment;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.hany.houseofcartoon.R;
import com.navercorp.volleyextensions.view.ZoomableNetworkImageView;

import customview.ZoomImgView;

/**
 * Created by HanyLuv on 2015-10-25.
 */
public class MainFragment extends Fragment {

    private ZoomImgView mapView;
    private Typeface typeface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, null);
        mapView = (ZoomImgView) view.findViewById(R.id.map_image_view);
        mapView.setImageResource(R.mipmap.map);
        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setGlobalFont(view);
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


    private ImageLoader.ImageCache imageLoader = new ImageLoader.ImageCache() {
        @Override
        public Bitmap getBitmap(String url) {
            return null;
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {

        }
    };

}
