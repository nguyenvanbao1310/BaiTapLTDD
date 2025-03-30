package vn.iotstar.recylceview;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;

public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {
    private final float DP = Resources.getSystem().getDisplayMetrics().density;

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int indicatorHeight = (int) (DP * 16);
        int indicatorWidth = (int) (DP * 8 * itemCount);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStyle(Paint.Style.FILL);

        int yOffset = parent.getHeight() - indicatorHeight;
        int xOffset = (parent.getWidth() - indicatorWidth) / 2;

        for (int i = 0; i < itemCount; i++) {
            float cx = xOffset + (i * DP * 16);
            c.drawCircle(cx, yOffset, DP * 4, paint);
        }
    }
}
