package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.interactor.InteractorRequest;
import org.ctdworld.propath.model.Request;

import java.util.List;


public class PresenterRequest implements ContractRequest.Presenter, ContractRequest.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterRequest.class.getSimpleName();
    ContractRequest.Interactor interactor;
    ContractRequest.View mListener;

    public PresenterRequest(Context context, ContractRequest.View mListener)
    {
        this.interactor = new InteractorRequest(this,context);
        this.mListener = mListener;
    }



    @Override
    public void sendRequest(String userId, String requestFor) {
        interactor.sendRequest(userId, requestFor);
    }

    @Override
    public void getAllRequests() {
        interactor.getAllRequests();
    }

    @Override
    public void respondToRequest(Request.Data requestData) {
        interactor.respondToRequest(requestData);
    }




    @Override
    public void onRequestSentSuccessfully() {
        mListener.onRequestSentSuccessfully();
    }

    @Override
    public void onReceivedAllRequests(List<Request.Data> requestDataList) {
        mListener.onReceivedAllRequests(requestDataList);
    }

    @Override
    public void onRespondedSuccessfully() {
        mListener.onRespondedSuccessfully();
    }
    

    @Override
    public void onShowMessage(String message) {
        mListener.onShowMessage(message);
    }

    @Override
    public void onFailed(String message) {
        mListener.onFailed(message);
    }

}
