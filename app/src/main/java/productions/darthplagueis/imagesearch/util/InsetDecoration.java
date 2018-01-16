package productions.darthplagueis.imagesearch.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import productions.darthplagueis.imagesearch.R;

/**
 * Created by oleg on 1/15/18.
 */

public class InsetDecoration extends RecyclerView.ItemDecoration {

    private int insets;

    public InsetDecoration(Context context) {
        insets = context.getResources().getDimensionPixelSize(R.dimen.insets);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = insets;
        outRect.bottom = insets;

        // Adds top margins only for the top two childViews to avoid double space between items.
        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = insets;
        }

        // Adds right margin to only the odd childViews since the odd ones are placed on the right.
        // This prevents double spacing in between the left and right childView.
        if (parent.getChildLayoutPosition(view) % 2 == 1) {
            outRect.right = insets;
        }
    }
}