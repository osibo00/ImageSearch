package productions.darthplagueis.imagesearch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.ImagesActivity;
import productions.darthplagueis.imagesearch.fragment.images.FullScreenFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoHits;
import productions.darthplagueis.imagesearch.util.Constants;

/**
 * Created by oleg on 1/11/18.
 */

public class PhotoHitsViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView imageView;
    private LinearLayout layout;

    public PhotoHitsViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imageView = itemView.findViewById(R.id.item_view_image);
        layout = itemView.findViewById(R.id.image_view_parent);
    }

    public void onBind(final PhotoHits hits) {
        setImages(hits);
        setOnClick(hits);
    }

    private void setImages(PhotoHits hits) {
        Glide.with(context)
                .asBitmap()
                .load(hits.getPreviewURL())
                .apply(new RequestOptions().override(hits.getPreviewWidth(), hits.getPreviewHeight()))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        onPalette(Palette.from(resource).generate());
                        imageView.setImageBitmap(resource);
                        return false;
                    }

                    private void onPalette(Palette palette) {
                        if (palette != null) {
                            String color = Integer.toHexString(palette.getDominantColor(context.getResources().getColor(R.color.light_grey)));
                            String newColor = color.substring(2, color.length());
                            newColor = new StringBuilder(newColor).insert(0, "#a6").toString();
                            try {
                                layout.setBackgroundColor(Color.parseColor(newColor));
                            } catch (IllegalArgumentException e) {
                                layout.setBackgroundColor(Color.parseColor(color));
                            }

                        }
                    }
                })
                .into(imageView);
    }

    private void setOnClick(final PhotoHits hits) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] strings = new String[]{hits.getWebformatURL(), hits.getTags(), hits.getType()};
                Bundle bundle = new Bundle();
                bundle.putStringArray(Constants.IMAGE_STRINGS, strings);
                FullScreenFragment fragment = new FullScreenFragment();
                FragmentTransaction transaction = ((ImagesActivity) context).getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                transaction.add(R.id.results_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
