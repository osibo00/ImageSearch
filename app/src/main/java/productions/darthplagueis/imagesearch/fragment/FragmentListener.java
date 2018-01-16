package productions.darthplagueis.imagesearch.fragment;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by oleg on 1/13/18.
 */

public interface FragmentListener {

    void inflateLoadingFragment(String title);

    void inflateAdvSearchFragment();

    void defineSimpleQuery(String query, String imageType);

    void defineAdvQuery(String query);

    void defineImageType(String imageType);

    void defineCategory(String category);

    void defineEditorChoice(String editor);

    void defineOrder(String order);

    void getLayoutManager(GridLayoutManager layoutManager);
}
