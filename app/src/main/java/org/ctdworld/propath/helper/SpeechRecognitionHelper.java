package org.ctdworld.propath.helper;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class SpeechRecognitionHelper implements RecognitionListener
{
    private static final String TAG = SpeechRecognitionHelper.class.getSimpleName();

    private Context mContext;
    private MySpeechListener mListener;

    public SpeechRecognitionHelper(Context context, MySpeechListener listener)
    {
        this.mContext = context;
        this.mListener = listener;
    }

    public interface MySpeechListener
    {
        void onReadyForSpeech();
        void onComplete(String spokenText);
        void onEndOfSpeech();
        void onError(int error);
    }


    /*public void listenVoice()
    {
        try {


            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
            speechRecognizer.setRecognitionListener(this);

            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

            speechRecognizer.startListening(recognizerIntent);
            Log.i(TAG,"Listening........");


        }
        catch (Exception e)
        {
            Log.e(TAG,"exception in listenVoice() method");
            e.printStackTrace();
        }
    }*/


    @Override
    public void onReadyForSpeech(Bundle bundle)
    {
     //   Log.i(TAG,"onReadyForSpeech() method called ");
      //  Toast.makeText(context,"Speak Now",Toast.LENGTH_SHORT).show();
        if (mListener != null)
            mListener.onReadyForSpeech();
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
        if (mListener != null)
            mListener.onEndOfSpeech();
    }

    @Override
    public void onError(int error)
    {
      //  Log.i(TAG,"onError() method called , "+error);
        if (mListener != null)
            mListener.onError(error);
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
                mListener.onComplete(String.valueOf(results.get(0)));
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
}
