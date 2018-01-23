package productions.darthplagueis.imagesearch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.VideoActivity;
import productions.darthplagueis.imagesearch.fragment.videos.VideoScreenFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoBitmaps;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoHits;
import productions.darthplagueis.imagesearch.util.Constants;

/**
 * Created by oleg on 1/20/18.
 */

public class VideoHitsViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView imageView;

    public VideoHitsViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imageView = itemView.findViewById(R.id.item_view_video);
    }

//    public void onBindUrlString(final VideoHits hits) {
//        setOnClick(hits);
//    }

//    public void onBindBitmaps(Bitmap bitmap) {
//        imageView.setImageBitmap(bitmap);
//    }

    public void onBind(VideoBitmaps bitmaps) {
        imageView.setImageBitmap(bitmaps.getVideoBitmap());
        setOnClick(bitmaps);
    }

//    private void setOnClick(final VideoHits hits) {
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.VIDEO_LINK, hits.getVideos().getTiny().getUrl());
//                VideoScreenFragment fragment = new VideoScreenFragment();
//                FragmentTransaction transaction = ((VideoActivity) context).getSupportFragmentManager().beginTransaction();
//                fragment.setArguments(bundle);
//                transaction.replace(R.id.video_fragment_container, fragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
//    }

    private void setOnClick(final VideoBitmaps bitmaps) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.VIDEO_LINK, bitmaps.getVideoUrl());
                VideoScreenFragment fragment = new VideoScreenFragment();
                FragmentTransaction transaction = ((VideoActivity) context).getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                transaction.replace(R.id.video_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
