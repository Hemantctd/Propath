package org.ctdworld.propath.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentFooter;

public class ActivitySearch extends AppCompatActivity
{
    private final String TAG = ActivityContactAdd.class.getSimpleName();

    //RecyclerView mRecycler;
    //EditText mEditSearch;
    // ImageView mImgSearch;

    LinearLayoutManager layoutManager;
    Toolbar mToolbar;
    TextView mToolbarTitle;
  //  ArrayList<String> items=new ArrayList<>();

 //   PermissionHelper mPermissionHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        setToolbar();
      //  setContactsAdapter();
      //  setListeners();
        getSupportFragmentManager().beginTransaction().add(R.id.container_footer,new FragmentFooter()).commit();

    }
    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
       /* mPermissionHelper = new PermissionHelper(this);
        mImgSearch = findViewById(R.id.contact_search_img_search);
        contactsSearchRecyclerView = findViewById(R.id.contactsSearchRecyclerView);
        mEditSearchContacts = findViewById(R.id.searchContacts);*/

    }
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Search");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    /*private void setContactsAdapter() {

        items.add("Patrick Mailata");
        items.add("Rachael Tuwhangi");
        items.add("Kimiora");
        items.add("Gaurav Sharma");
        items.add("Amorangi");
        items.add("College Group");
        items.add("NZ Group");
        items.add("Friends Group");
        items.add("Sakshi Kumari");
        items.add("Malesala");
        items.add("Ranginui");
        items.add("Iulieta");
        items.add("Syed Irshad");

        adapter = new AdapterContactAdd(ActivityContactAdd.this,items);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);

    }*/

    /*private  void setListeners()
    {
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            }
        });

        mEditSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }

    private void filter(String text)
    {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (String s : items) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        adapter.filterList(filterdNames);
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

  /*  private void voiceToText()
    {
        Log.i(TAG,"voiceToText() method called ");
        FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
        fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
            @Override
            public void onReceiveText(String spokenText)
            {
                mEditSearchContacts.setText(spokenText);
                mEditSearchContacts.requestFocus();
            }

            @Override
            public void onError() {
                mEditSearchContacts.requestFocus();
            }
        });

        fragmentSpeechRecognition.show(getSupportFragmentManager(),"");
    }
*/

}
