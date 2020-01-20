package org.ctdworld.propath.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCareerCreateUpdate;
import org.ctdworld.propath.activity.ActivityCareerUserPlanList;
import org.ctdworld.propath.adapter.AdapterCareerUsers;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractCareerPlan;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCareerPlan;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/* this fragment contains list of all users who have created career plan, each user object contains all career plans which will be
* shown in next activity when user clicks on any user */
public class FragmentCareerPlanUsers extends BaseFragment implements ContractCareerPlan.View
{

    private final String TAG = FragmentCareerPlanUsers.class.getSimpleName();

    // # Views
    private RecyclerView mRecycler;   // to show all users list who have created career plans
    private EditText mEditFilterList;   // to filter users list
    private ImageView mImgMicFilterList;  // mic icon to filter list by voice to text feature
    private SwipeRefreshLayout mRefreshLayout;  // to reload data from server
    private TextView mTxtErrorMessage;  // to show message like no internet connection
    private View mLayoutForDetails;  // layout to show all views except mTxtConnections
    private FloatingActionButton mFloatingButton; // floating button to create new career plan




    // # Other Variables
    private Context mContext;   // context
    private AdapterCareerUsers mAdapter;  // adapter to show user list who have created career plan
    private ContractCareerPlan.Presenter mPresenter;  // presenter to request to server for any type of data regarding career plan
    private List<CareerPlan.CareerUser> mCareerUserList = new ArrayList<>();  // contains list of users who have create career plan
    private PermissionHelper mPermissionHelper;  // permission helper to check permissions



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_athlete_list_created_career, container, false);


        init(view);  // initializing variables
        setUpAdapter(); // setting up adapter to load user list
        requestCareerPlanDataList();  // getting all users who have created career plan
        manageFloatButton();


        // # setting listeners
        mRefreshLayout.setOnRefreshListener(onRefreshingData);
        mImgMicFilterList.setOnClickListener(onImgMicFilterClicked);
        mEditFilterList.addTextChangedListener(onEditFilterListTextChanged);



        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        mPresenter = new PresenterCareerPlan(mContext, this);
        mPermissionHelper = new PermissionHelper(mContext);
        mImgMicFilterList = view.findViewById(R.id.activity_choose_user_by_role_img_search_icon);
        mRecycler = view.findViewById(R.id.activity_choose_user_by_role_recycler_view);
        mEditFilterList = view.findViewById(R.id.activity_choose_user_by_role_edit_search);
        mLayoutForDetails = view.findViewById(R.id.activity_choose_user_by_role_layout_details);
        mTxtErrorMessage = view.findViewById(R.id.activity_choose_user_by_role_txt_no_connection);
        mRefreshLayout = view.findViewById(R.id.activity_choose_user_by_role_refresh_layout);
        mFloatingButton = view.findViewById(R.id.activity_career_plan_floating_button);  // initializing toolbar edit icon

    }





    private void manageFloatButton() {
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
            mFloatingButton.setVisibility(View.VISIBLE);
        else
            mFloatingButton.setVisibility(View.GONE);


        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityCareerCreateUpdate.class);
                intent.putExtra(ConstHelper.Key.ACTION_CREATE_UPDATE, ConstHelper.Action.CREATE);
                startActivityForResult(intent, ConstHelper.RequestCode.ACTIVITY_CAREER_PLAN_CREATE_UPDATE);
            }
        });
    }



    // getting all user who have created career plan
    private void requestCareerPlanDataList()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                showLoader(getString(R.string.message_loading));;
                showDataLayout();
                mAdapter.clearOlList();

                CareerPlan.CareerUser careerUser = new CareerPlan.CareerUser();
                careerUser.setUserId(SessionHelper.getUserId(mContext));
                mPresenter.requestCareerUsers(careerUser);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for users , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            String message = getString(R.string.title_no_connection)+"\n"+getString(R.string.message_no_connection);
            showErrorMessage(message);
        }
    }



    // # setting up adapter
    private void setUpAdapter() {

        ArrayList<CareerPlan.CareerUser> list = new ArrayList<>();
        mAdapter = new AdapterCareerUsers(mContext, list, onAdapterItemClickedListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);
    }






    // filtering all registered user with by input name EditText
    private void filterList(String text)
    {
        //new array list that will hold the filtered data
        ArrayList<CareerPlan.CareerUser> filterdUser = new ArrayList<>();

        //looping through existing elements
        for (CareerPlan.CareerUser careerUser : mCareerUserList)
        {
            if (careerUser == null)
                continue;
            //if the existing elements contains the search input
            String userName = careerUser.getUserName();

            if (userName != null)
            {
                if (text != null && userName.toLowerCase().contains(text.toLowerCase()))
                {
                    //adding the element to filtered list
                    filterdUser.add(careerUser);
                }
            }

        }
        mAdapter.filterList(filterdUser);
    }





    // updating user's plan list if new plan has been created
    public void updateCareerUserList(CareerPlan.CareerUser careerUser)
    {
        if (mCareerUserList == null || careerUser == null || careerUser.getCareerData() == null)
            return;

        // getting logged in athlete's CareerUser object from mCareerUserList(contains list of all user who created career plan)
        CareerPlan.CareerUser careerUserInList = getCareerUserOfLoggedInUser();

        if (careerUserInList != null)  // adding new CareerData object in Existing CareerUser's career data list object in mCareerUserList
        {
          //  careerUserInList.setCareerData(careerUser.getCareerData());
            careerUserInList.getCareerDataList().add(careerUser.getCareerDataList().get(0));
            mAdapter.onDataUpdated(careerUserInList);
        }
        else  // if user is not present in mCareerUserList(contains list of all user who created career plan) then adding that user in list
        {
            List<CareerPlan.CareerData> careerDataList = new ArrayList<>();
            careerDataList.add(careerUser.getCareerData());
            careerUser.setCareerDataList(careerDataList);
            mCareerUserList.add(careerUser);  // adding new CareerUser object in mCareerUserList
            mAdapter.updateUserList(mCareerUserList);        }
    }



    // returns CareerPlan.CareerUser object of logged in user
    private CareerPlan.CareerUser getCareerUserOfLoggedInUser()
    {
        if (mCareerUserList == null)
            return null;

        for (CareerPlan.CareerUser careerUser : mCareerUserList)
        {
            if (SessionHelper.getUserId(mContext).equals(careerUser.getUserId()))
            {
                return careerUser;
            }
        }

        return null;
    }


    private TextWatcher onEditFilterListTextChanged = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            filterList(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //after the change calling the method and passing the search input

        }
    };






    // to filter list when user clicks on mic and speak something
    private View.OnClickListener onImgMicFilterClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            if (!mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            else
            {
                FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                    @Override
                    public void onReceiveText(String spokenText)
                    {
                        mEditFilterList.setText(spokenText);
                        mEditFilterList.requestFocus();
                    }

                    @Override
                    public void onError() {
                        mEditFilterList.requestFocus();
                    }
                });

                fragmentSpeechRecognition.show(getChildFragmentManager(),"");
            }
        }
    };



    private AdapterCareerUsers.Listener onAdapterItemClickedListener = new AdapterCareerUsers.Listener() {
        @Override
        public void onAdapterItemClicked(final CareerPlan.CareerUser careerUser)
        {

            if (careerUser.getPermissionRequestStatus().equals(Request.REQUEST_STATUS_ACCEPT))
            {
                Intent intent = new Intent(mContext, ActivityCareerUserPlanList.class);
                intent.putExtra(ConstHelper.Key.CAREER_USER, careerUser);
                startActivityForResult(intent, ConstHelper.RequestCode.FRAGMENT_CAREER_PLAN_LIST);
            }
            else if (careerUser.getPermissionRequestStatus().equals(Request.REQUEST_STATUS_REJECT))
            {
                String title = getString(R.string.permission_title_see_data);
                String message = getString(R.string.permission_message_see_data);
                DialogHelper.showCustomDialog(mContext, title, message, "Send Request", "Cancel", new DialogHelper.ShowDialogListener() {
                    @Override
                    public void onOkClicked() {
                        sendPermissionRequest(careerUser);
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
            }
        }


        @Override
        public void onSendPermissionRequestClicked(final CareerPlan.CareerUser careerUser) {
            if (isConnectedToInternet())
                sendPermissionRequest(careerUser);
            else
                DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection), getString(R.string.message_no_connection));
        }
    };


    // sending permission request to see career plan
    private void sendPermissionRequest(final CareerPlan.CareerUser careerUser)
    {
        ContractRequest.Presenter presenter = new PresenterRequest(mContext, new ContractRequest.View() {
            @Override
            public void onRequestSentSuccessfully() {
                hideLoader();
                if (careerUser != null)
                {
                    careerUser.setPermissionRequestStatus(Request.REQUEST_STATUS_PENDING);
                    mAdapter.onDataUpdated(careerUser);
                }
            }

            @Override
            public void onReceivedAllRequests(List<Request.Data> requestDataList) {
                hideLoader();
            }

            @Override
            public void onRespondedSuccessfully() {
                hideLoader();
            }

            @Override
            public void onShowMessage(String message) {
                hideLoader();
            }

            @Override
            public void onFailed(String message) {
                //showPermissionTextView(holder, "Failed..try again");
                hideLoader();
                mAdapter.showPermissionTextView(getString(R.string.try_again));
            }
        });

        showLoader(getString(R.string.send_request));
        presenter.sendRequest(careerUser.getUserId(), Request.REQUEST_FOR_CAREER_PLAN_VIEW);
    }



    // showing error message and hiding layout which contains all data
    private void showErrorMessage(String message)
    {
        mLayoutForDetails.setVisibility(View.GONE);
        mTxtErrorMessage.setVisibility(View.VISIBLE);
        mTxtErrorMessage.setText(message);
    }




    // showing layout which contains all data and hiding mTxtErrorMessage
    private void showDataLayout()
    {
        mTxtErrorMessage.setVisibility(View.GONE);
        mLayoutForDetails.setVisibility(View.VISIBLE);
    }





    // # to reload data
    private SwipeRefreshLayout.OnRefreshListener onRefreshingData = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mEditFilterList.setText("");
            mRefreshLayout.setRefreshing(false);
            requestCareerPlanDataList();
        }
    };





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getExtras() != null)
        {
            if (requestCode == ConstHelper.RequestCode.FRAGMENT_CAREER_PLAN_LIST)
            {
                // updating user list, may be data was plan was edited or removed
                CareerPlan.CareerUser careerUser = (CareerPlan.CareerUser) data.getExtras().getSerializable(ConstHelper.Key.CAREER_USER);
                if (mAdapter != null && careerUser != null)
                {
                    mAdapter.onDataUpdated(careerUser);
                }
            }



            if (requestCode == ConstHelper.RequestCode.ACTIVITY_CAREER_PLAN_CREATE_UPDATE)
            {
                CareerPlan.CareerUser careerUser = (CareerPlan.CareerUser) data.getSerializableExtra(ConstHelper.Key.CAREER_USER);
              //  FragmentCareerPlanUsers fragmentCareerPlanUsers = (FragmentCareerPlanUsers) getChildFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.CAREER_USERS_LIST);
               // if (careerUser != null)
                  //  updateCareerUserList(careerUser);
                /*if (fragmentCareerPlanUsers != null)
                    fragmentCareerPlanUsers.updateCareerUserList(careerUser);*/
            }

        }


    }





    @Override
    public void onSavedCareerPlan(CareerPlan.CareerUser careerUser) {

    }



    @Override
    public void onReceivedCareerUsers(List<CareerPlan.CareerUser> careerUserList)
    {
        hideLoader();
        mCareerUserList = careerUserList;  // data will be shown only when text is enter in search box

        /*checking if logged in user is athlete then he will be put on top that is index zero in list
         so that user can see his career plans easily */
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
        {
            CareerPlan.CareerUser careerUser = getCareerUserOfLoggedInUser();  // getting logged in user's CareerUser
            if (careerUser != null)  // CareerUser is not null then adding this user on top(index 0)
            {
                mCareerUserList.remove(careerUser);  // removing logged in user's CareerPlan from current position
                mCareerUserList.add(0, careerUser);  // adding logged in user's CareerPlan at 0 index
            }
        }

      //  mAdapter.updateUserList(mCareerUserList);
    }



    @Override
    public void onCareerPlanUpdated(CareerPlan.CareerUser careerUser) {

    }



    @Override
    public void onCareerDataDeleted(CareerPlan.CareerUser careerUser) {

    }



    @Override
    public void onFailed(String message) {
        hideLoader();
       // DialogHelper.showSimpleCustomDialog(mContext,message);
        if (message != null && !message.isEmpty())
            message = getString(R.string.failed_title)+"\n"+message;
        else
            message = getString(R.string.failed_title);

           showErrorMessage(message);
    }
}
