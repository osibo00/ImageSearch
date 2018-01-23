package productions.darthplagueis.imagesearch.controller;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoBitmaps;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoHits;
import productions.darthplagueis.imagesearch.view.VideoHitsViewHolder;

/**
 * Created by oleg on 1/20/18.
 */

public class VideoHitsAdapter extends RecyclerView.Adapter<VideoHitsViewHolder> {

    private List<VideoBitmaps> bitmapsList;

    public VideoHitsAdapter() {
        bitmapsList = new ArrayList<>();
    }

    @Override
    public VideoHitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.videohits_itemview, parent, false);
        return new VideoHitsViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(VideoHitsViewHolder holder, int position) {
        holder.onBind(bitmapsList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapsList.size();
    }

    public void createBitmapsList(List<VideoBitmaps> newList) {
        bitmapsList.addAll(newList);
    }

    public void updateList(List<VideoBitmaps> newList) {
        bitmapsList.addAll(newList);
        notifyItemRangeInserted(getItemCount(), bitmapsList.size() - 1);
    }
}
