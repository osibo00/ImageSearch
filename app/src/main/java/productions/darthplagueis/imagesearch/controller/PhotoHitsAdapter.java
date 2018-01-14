package productions.darthplagueis.imagesearch.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;
import productions.darthplagueis.imagesearch.view.PhotoHitsViewHolder;

/**
 * Created by oleg on 1/11/18.
 */

public class PhotoHitsAdapter extends RecyclerView.Adapter<PhotoHitsViewHolder> {

    private List<PhotoHits> photoHitsList;

    public PhotoHitsAdapter() {
        photoHitsList = new ArrayList<>();
    }

    @Override
    public PhotoHitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photohits_itemview, parent, false);
        return new PhotoHitsViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(PhotoHitsViewHolder holder, int position) {
        holder.onBind(photoHitsList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoHitsList.size();
    }

    public void createList(List<PhotoHits> newList) {
        photoHitsList.addAll(newList);
    }

    public void updateList(List<PhotoHits> newList) {
        photoHitsList.addAll(newList);
        notifyItemRangeInserted(getItemCount(), photoHitsList.size() - 1);
    }

    public void clearList() {
        photoHitsList.clear();
    }
}
