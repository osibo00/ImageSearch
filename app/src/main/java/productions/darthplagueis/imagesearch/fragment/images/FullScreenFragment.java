package productions.darthplagueis.imagesearch.fragment.images;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.util.Constants;


public class FullScreenFragment extends Fragment {

    private final String TAG = "FullScreenFrag";
    private View rootView;
    private ImageView imageView;
    private FrameLayout layout;
    private String[] imageStrings;


    public FullScreenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_full_screen, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView = rootView.findViewById(R.id.full_screen_image_view);
        layout = rootView.findViewById(R.id.full_screen_parent);

        setArguments();
        setImageLayout();

        return rootView;
    }

    private void setArguments() {
        Bundle args = getArguments();
        if (args != null) {
            imageStrings = args.getStringArray(Constants.IMAGE_STRINGS);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(imageStrings[2]);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Tags " + imageStrings[1]);
        }
        Log.d(TAG, "onCreateView: " + imageStrings[0] + " " + imageStrings[1] + " " + imageStrings[2]);
    }

    private void setImageLayout() {
        Glide.with(rootView.getContext())
                .asBitmap()
                .load(imageStrings[0])
                .apply(new RequestOptions().override(rootView.getWidth(), rootView.getHeight()))
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
                            String color = Integer.toHexString(palette.getDominantColor(rootView.getResources().getColor(R.color.light_grey)));
                            String newColor = color.substring(2, color.length());
                            newColor = new StringBuilder(newColor).insert(0, "#D9").toString();
                            try {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(rootView.getResources().getColor(R.color.colorPrimary))));
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            try {
                                layout.setBackgroundColor(Color.parseColor(newColor));
                            } catch (IllegalArgumentException e) {
                                layout.setBackgroundColor(Color.parseColor(color));
                            }
                            Window window = getActivity().getWindow();
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(palette.getVibrantColor(rootView.getResources().getColor(R.color.colorPrimaryDark)));
                        }
                    }
                })
                .into(imageView);
    }

}
