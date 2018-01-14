package productions.darthplagueis.imagesearch.fragment;

/**
 * Created by oleg on 1/13/18.
 */

public interface FragmentListener {

    void inflateLoadingFragment();

    void defineSearchQuery(String query);

    void pageListener(int page);
}
