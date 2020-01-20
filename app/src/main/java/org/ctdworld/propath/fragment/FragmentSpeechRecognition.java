package org.ctdworld.propath.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DialogHelper;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSpeechRecognition extends DialogFragment implements RecognitionListener
{
    private final String TAG = FragmentSpeechRecognition.class.getSimpleName();

    private Context mContext;
    private SpeechListener mListener;
    private SpeechRecognizer mSpeechRecognizer;

    public FragmentSpeechRecognition() {
        // Required empty public constructor
    }



    public interface SpeechListener
    {
        void onReceiveText(String spokenText);
        void onError();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_speech_recognition, container, false);

        mContext = getContext();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
           // listenVoice();
            listenVoiceNew();
    }

    private void listenVoiceNew() {
        try {


            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
            mSpeechRecognizer.setRecognitionListener(this);

            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

            mSpeechRecognizer.startListening(recognizerIntent);
            Log.i(TAG,"Listening........");


        }
        catch (Exception e)
        {
            Log.e(TAG,"exception in listenVoice() method");
            e.printStackTrace();
        }
    }


   /* private void listenVoice()
    {
        try
        {

            SpeechRecognitionHelper speechRecognitionHelper = new SpeechRecognitionHelper(getContext(), new SpeechRecognitionHelper.MySpeechListener() {
                @Override
                public void onReadyForSpeech()
                {
                    //   Log.i(TAG,"onReadySpeech");
                }

                @Override
                public void onComplete(String spokenText) {
                    //    Log.i(TAG,"onComplete , text = "+spokenText);
                    if (mListener != null && spokenText != null)
                        mListener.onReceiveText(spokenText);
                    closeDialog();
                }

                @Override
                public void onEndOfSpeech() {
                    //  Log.i(TAG,"onEndOfSpeech");
                    closeDialog();
                }

                @Override
                public void onError(int error) {
                    Log.e(TAG,"onError in speech , error = "+error);
                    closeDialog();
                    if (mListener != null)
                        mListener.onError();

                    String title = "Failed";
                    String message = "Try Again...";
                    switch (error)
                    {
                        *//*case 1:
                            message = "TimeOut...";
                            break;

                        case 2:
                            message = "Network Error...";
                            break;

                        case 3:
                            message = "Audio Error...";
                            break;

                        case 4:
                            message = "Server Error...";
                            break;  *//*

                        case 6:
                            message = "Speech TimeOut...";
                            break;

                        case 7:
                            message = "Please Speak Something...";
                            break;


                        case 9:
                            message = "Please give microphone permission in setting";
                            break;
                    }

                    DialogHelper.showSimpleCustomDialog(mContext, title, message);
                }
            });

            speechRecognitionHelper.listenVoice();


        }
        catch (Exception e)
        {
            Log.e(TAG,"Exception in onCreateView() method");
            e.printStackTrace();
        }
    }*/


    @Override
    public void onDestroy() {
        mSpeechRecognizer.stopListening();
        super.onDestroy();
    }


    private void closeDialog()
    {
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.dismiss();

       /* if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.stopListening();
          //  mSpeechRecognizer.cancel();
          //  mSpeechRecognizer.destroy();
        }*/

    }

    public void setListener(SpeechListener listener)
    {
        this.mListener = listener;
    }




















    @Override
    public void onReadyForSpeech(Bundle bundle)
    {
        //   Log.i(TAG,"onReadyForSpeech() method called ");
        //  Toast.makeText(context,"Speak Now",Toast.LENGTH_SHORT).show();
        /*if (mListener != null)
            mListener.onReadyForSpeech();*/
    }

    @Override
    public void onBeginningOfSpeech() {
        //   Log.i(TAG,"onBeginningOfSpeech() method called ");

    }


    @Override
    public void onRmsChanged(float v) {
        //  Log.i(TAG,"onRmsChanged() method called ");

    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        //  Log.i(TAG,"onBufferReceived() method called ");

    }

    @Override
    public void onEndOfSpeech() {
        //  Log.i(TAG,"onEndOfSpeech() method called ");
        /*if (mListener != null)
            mListener.onEndOfSpeech();*/
        closeDialog();
    }


    @Override
    public void onResults(Bundle bundle) {
        Log.i(TAG,"onResults() method called ");

        if (bundle == null)
            return;

        ArrayList results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (results != null && results.size()>0)
        {
            Log.i(TAG,"You pronounced  \""+results.get(0)+"\"");
            if (mListener != null)
                mListener.onReceiveText(String.valueOf(results.get(0)));
        }
        else
            Log.i(TAG,"results is null");
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.i(TAG,"onPartialResults() method called ");

    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i(TAG,"onEvent() method called ");

    }




    @Override
    public void onError(int error)
    {
        Log.e(TAG,"onError in speech , error = "+error);
        closeDialog();
        if (mListener != null)
            mListener.onError();

        String title = "Failed";
        String message = "Try Again...";
        switch (error)
        {
                        /*case 1:
                            message = "TimeOut...";
                            break;

                        case 2:
                            message = "Network Error...";
                            break;

                        case 3:
                            message = "Audio Error...";
                            break;

                        case 4:
                            message = "Server Error...";
                            break;  */

            case 6:
                message = "Speech TimeOut...";
                break;

            case 7:
                message = "Please Speak Something...";
                break;


            case 9:
                message = "Please give microphone permission in setting";
                break;
        }

        DialogHelper.showSimpleCustomDialog(mContext, title, message);
    }









}
