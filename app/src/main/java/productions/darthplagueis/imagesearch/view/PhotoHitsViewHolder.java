package productions.darthplagueis.imagesearch.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;

/**
 * Created by oleg on 1/11/18.
 */

public class PhotoHitsViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private ImageView imageView;

    public PhotoHitsViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imageView = itemView.findViewById(R.id.item_view_image);
    }

    public void onBind(PhotoHits hits) {
        Glide.with(context)
                .load(hits.getPreviewURL())
                .apply(new RequestOptions().override(hits.getPreviewWidth(), hits.getPreviewHeight()))
                .into(imageView);
    }
}
