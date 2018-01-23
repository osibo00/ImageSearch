package productions.darthplagueis.imagesearch.fragment.images;

/**
 * Created by oleg on 1/17/18.
 */

public interface SearchFragListener {

    void onSearchFragmentInteraction(String title);

    void inflateAdvSearchFragment();

    void inflateVideoSearchFragment();

    void defineSimpleQuery(String query, String imageType);

    void defineAdvQuery(String query);

    void defineImageType(String imageType);

    void defineCategory(String category);

    void defineEditorChoice(String editor);

    void defineOrder(String order);
}
