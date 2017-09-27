package customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.navercorp.volleyextensions.view.TwoLevelDoubleTapZoomNetworkImageView;

/**
 * Created by hany,dh on 2015. 10. 29..
 */
public class ZoomImgView extends TwoLevelDoubleTapZoomNetworkImageView {
    public ZoomImgView(Context context) {
        super(context);
    }

    public ZoomImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomImgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        restore(null);
    }

}
