package productions.darthplagueis.imagesearch.fragment;

/**
 * Created by oleg on 1/17/18.
 */

public interface SearchFragListener {

    void inflateLoadingFragment(String title);

    void inflateAdvSearchFragment();

    void defineSimpleQuery(String query, String imageType);

    void defineAdvQuery(String query);

    void defineImageType(String imageType);

    void defineCategory(String category);

    void defineEditorChoice(String editor);

    void defineOrder(String order);
}
