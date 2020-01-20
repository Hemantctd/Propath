package org.ctdworld.propath.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;


import org.ctdworld.propath.contract.ContractEmploymentTools;
import org.ctdworld.propath.interactor.InteracterEmploymentTools;
import org.ctdworld.propath.model.CareerEmploymentData;

import java.util.List;


public class PresenterEmploymentTools implements ContractEmploymentTools.Presenter, ContractEmploymentTools.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterEmploymentTools.class.getSimpleName();
    ContractEmploymentTools.Interactor interactor;
    ContractEmploymentTools.View listenerView;

    public PresenterEmploymentTools(Context context, ContractEmploymentTools.View view)
    {
        this.interactor = new InteracterEmploymentTools(this,context);
        this.listenerView = view;
    }

    @Override
    public void deleteFile(String fileId, int positionInAdapter) {
        interactor.deleteFile(fileId, positionInAdapter);
    }

    @Override
    public void onReceivedEmploymentList(List<CareerEmploymentData> employmentToolsList) {
        listenerView.onReceivedEmploymentList(employmentToolsList);
    }

    @Override
    public void onUploadSuccess() {
        listenerView.onUploadSuccess();
    }

    @Override
    public void onFailed(String message) {
        listenerView.onFailed(message);
    }

    @Override
    public void onFileDeleted(String message, int positionInAdapter) {
        listenerView.onFileDeleted(message, positionInAdapter);
    }

    @Override
    public void requestEmploymentList(CareerEmploymentData employmentTools) {
        interactor.requestEmploymentList(employmentTools);
    }

    @Override
    public void uploadEmployment(CareerEmploymentData employmentTools) {
        interactor.uploadEmployment(employmentTools);
    }

    @Override
    public void registerReceiver(BroadcastReceiver receiver) {
        listenerView.registerReceiver(receiver);
    }



}
