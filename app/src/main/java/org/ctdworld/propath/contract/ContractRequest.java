package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.Request;

import java.util.List;

public interface ContractRequest
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onRequestSentSuccessfully();
            void onReceivedAllRequests(List<Request.Data> requestDataList);
            void onRespondedSuccessfully();    // called when user has responded successfully to requestData.
            void onShowMessage(String message);
            void onFailed(String message);
        }
        void sendRequest(String userId, String requestFor);
        void getAllRequests();  // to get list of all requestData list
        void respondToRequest(Request.Data requestData);  // to update database whether user has accepted requestData or not
    }

    interface View
    {
        void onRequestSentSuccessfully();
        void onReceivedAllRequests(List<Request.Data> requestDataList);
        void onRespondedSuccessfully();    // called when user has responded successfully to requestData.
        void onShowMessage(String message);
        void onFailed(String message);
    }

    interface Presenter
    {
        void sendRequest(String userId, String requestFor);
        void getAllRequests();  // to get list of all requestData list
        void respondToRequest(Request.Data requestData);  // to update database whether user has accepted requestData or not
    }
}
