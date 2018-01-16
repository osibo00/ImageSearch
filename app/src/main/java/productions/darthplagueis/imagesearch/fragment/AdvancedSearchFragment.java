package productions.darthplagueis.imagesearch.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import productions.darthplagueis.imagesearch.MainActivity;
import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.util.CustomSpinner;


public class AdvancedSearchFragment extends Fragment {

    FragmentListener listener;
    private final String TAG = "Advanced Search";
    private EditText advQueryText;
    private Button advSearchBtn;
    private CustomSpinner imageTypeSpnr;
    private CustomSpinner categorySpnr;
    private CustomSpinner editorSpnr;
    private CustomSpinner orderSpnr;
    private String imageTypeChoice;
    private String categoryChoice;
    private String editorChoice;
    private String orderChoice;


    public AdvancedSearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_advanced_search, container, false);

        ((AppCompatActivity) getActivity()).setSupportActionBar(MainActivity.getToolbar());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        advQueryText = rootView.findViewById(R.id.adv_query_text);
        advSearchBtn = rootView.findViewById(R.id.adv_search_btn);
        imageTypeSpnr = rootView.findViewById(R.id.spinner_imagetype);
        categorySpnr = rootView.findViewById(R.id.spinner_category);
        editorSpnr = rootView.findViewById(R.id.spinner_editors_choice);
        orderSpnr = rootView.findViewById(R.id.spinner_order);

        setImageTypeSpinner();
        setCategorySpinner();
        setEditorSpinner();
        setOrderSpinner();
        setAdvSearchButton();


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentListener");
        }
    }

    private void setImageTypeSpinner() {
        String[] data = {"Image Type", "All", "Photo", "Illustration", "Vector"};

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        imageTypeSpnr.setAdapter(adapter);
        imageTypeSpnr.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                imageTypeSpnr.setSelected(true);
            }

            public void onSpinnerClosed() {
                imageTypeChoice = imageTypeSpnr.getSelectedItem().toString();
                imageTypeSpnr.setSelected(false);
            }
        });
    }

    private void setCategorySpinner() {
        String[] data = {"Category", "Nature", "science", "Places", "Food", "People"};

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        categorySpnr.setAdapter(adapter);
        categorySpnr.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                categorySpnr.setSelected(true);
            }

            public void onSpinnerClosed() {
                categoryChoice = categorySpnr.getSelectedItem().toString();
                categorySpnr.setSelected(false);
            }
        });
    }

    private void setEditorSpinner() {
        String[] data = {"Editors Choice", "Yes", "No"};

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        editorSpnr.setAdapter(adapter);
        editorSpnr.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                editorSpnr.setSelected(true);
            }

            public void onSpinnerClosed() {
                editorChoice = editorSpnr.getSelectedItem().toString();
                editorSpnr.setSelected(false);
            }
        });
    }

    private void setOrderSpinner() {
        String[] data = {"Order", "Popular", "Latest"};

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        orderSpnr.setAdapter(adapter);
        orderSpnr.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                orderSpnr.setSelected(true);
            }

            public void onSpinnerClosed() {
                orderChoice = orderSpnr.getSelectedItem().toString();
                orderSpnr.setSelected(false);
            }
        });
    }

    private void setAdvSearchButton() {
        advSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = advQueryText.getText().toString();
                String queryString = "";
                String[] splitter = searchString.split(" ");
                if (splitter.length > 1) {
                    for (int i = 0; i < splitter.length; i++) {
                        if (i == splitter.length - 1) {
                            queryString += splitter[i];
                        } else {
                            queryString += splitter[i] + "+";
                        }
                    }
                } else {
                    queryString = splitter[0];
                }
                if (imageTypeChoice != null) {
                    listener.defineImageType(makeLowerCaseFirstLetter(imageTypeChoice));
                }
                if (categoryChoice != null) {
                    listener.defineCategory(makeLowerCaseFirstLetter(categoryChoice));
                }
                if (editorChoice != null) {
                    listener.defineEditorChoice(editorChoice);
                }
                if (orderChoice != null) {
                    listener.defineOrder(makeLowerCaseFirstLetter(orderChoice));
                }
                listener.defineAdvQuery(queryString);
                listener.inflateLoadingFragment(capitalizeFirstLetter(searchString));
            }
        });
    }

    private String makeLowerCaseFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toLowerCase() + original.substring(1);
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}
