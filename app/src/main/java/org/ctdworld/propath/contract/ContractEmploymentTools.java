package org.ctdworld.propath.contract;


import android.content.BroadcastReceiver;

import org.ctdworld.propath.model.CareerEmploymentData;

import java.util.List;
public interface ContractEmploymentTools
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onReceivedEmploymentList(List<CareerEmploymentData> employmentToolsList);
            void onUploadSuccess();
            void onFailed(String message);
            void onFileDeleted(String message, int positionInAdapter);

        }
        void requestEmploymentList(CareerEmploymentData employmentTools);
        void uploadEmployment(CareerEmploymentData employmentTools);
        void deleteFile(String fileId, int positionInAdapter);

    }

    interface View
    {
        void onReceivedEmploymentList(List<CareerEmploymentData> employmentToolsList);
        void onUploadSuccess();
        void onFailed(String message);
        void registerReceiver(BroadcastReceiver receiver);
        void onFileDeleted(String message, int positionInAdapter);

    }

    interface Presenter
    {
        void requestEmploymentList(CareerEmploymentData employmentTools);
        void uploadEmployment(CareerEmploymentData employmentTools);
        void registerReceiver(BroadcastReceiver receiver);
        void deleteFile(String fileId, int positionInAdapter);
    }
}
