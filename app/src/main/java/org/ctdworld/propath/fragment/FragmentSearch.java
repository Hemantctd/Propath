package org.ctdworld.propath.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.PermissionHelper;

/* this fragment is being used to filter any type of list. it uses interface to send searched text*/
public class FragmentSearch extends Fragment
{
    // # views
    private EditText mEditSearch;
    private PermissionHelper mPermissionHelper;

    // # listener
    private SearchListener mListener;


    public FragmentSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (getParentFragment() != null)
                mListener = (SearchListener) getParentFragment();
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // # initializing
        ImageView imgMic = view.findViewById(R.id.img_mic);
        mEditSearch = view.findViewById(R.id.edit_search);
        mPermissionHelper = new PermissionHelper(getContext());

        // # setting listener
        mEditSearch.addTextChangedListener(onTextChanged);
        imgMic.setOnClickListener(onMicClicked);
    }




        private TextWatcher onTextChanged = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mListener != null)
                    mListener.onSearchToFilter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input

            }
        };



    private View.OnClickListener onMicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String permMessage = getString(R.string.message_microphone_permission);
            if (!mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, permMessage);
            else
                searchByVoice();
        }
    };



    // for converting voice to text, and putting spoken text in search EditText
    private void searchByVoice()
    {
        FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
        fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
            @Override
            public void onReceiveText(String spokenText)
            {
                mEditSearch.setText(spokenText);
                mEditSearch.requestFocus(View.FOCUS_RIGHT);
            }

            @Override
            public void onError() {
                mEditSearch.requestFocus(View.FOCUS_RIGHT);
            }
        });

        fragmentSpeechRecognition.show(getChildFragmentManager(),"");
    }



     // clearing text from EditText
      public void clearSearchedText()
      {
          if (mEditSearch != null)
              mEditSearch.setText("");
      }


      // initializing listener when SearchListener is implemented by Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchListener)
            mListener = (SearchListener) context;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // # listener to send searched text
    public interface SearchListener {
        void onSearchToFilter(String searchedText);
    }


}
