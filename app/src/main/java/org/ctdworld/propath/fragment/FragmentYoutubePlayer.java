package org.ctdworld.propath.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.ctdworld.propath.AppController;
import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.YouTubeConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYoutubePlayer extends Fragment {

    private YouTubePlayerView playerView;
    private ImageView mPlay;
    String mYouTubeUrl;
    Context mContext;

    public FragmentYoutubePlayer() {
        // Required empty public constructor
    }


    public static FragmentYoutubePlayer newInstance(String youTubeUrl) {

        Bundle args = new Bundle();

        args.putString(ConstHelper.Key.URL, youTubeUrl);
        FragmentYoutubePlayer fragment = new FragmentYoutubePlayer();
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mContext = getActivity();
        mYouTubeUrl = bundle.getString(ConstHelper.Key.URL);
        
       // youTube.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_youtube_player, container, false);
        init(view);
     //   youTubePlayerInitialize();
        playYouTubeVideo(mYouTubeUrl);
        return view;
    }

    private void init(View view) {

        playerView = view.findViewById(R.id.you_tube_link);
        mPlay = view.findViewById(R.id.play_btn);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            youTubePlayerInitialize();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)) {
            if (playerView == null) {
                youTubePlayerInitialize();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (playerView != null) {
                //playerView.release();
                playerView = null;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (playerView != null) {
                //playerView.release();
                playerView = null;
            }
        }
    }


    private void youTubePlayerInitialize() {

        final YouTubePlayer.OnInitializedListener onInitializedListener;

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                youTubePlayer.loadVideo(UtilHelper.getYoutubeVideoID(mYouTubeUrl));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                youTubeInitializationResult.getErrorDialog(getActivity(), 1).show();
            }
        };

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playerView.initialize(YouTubeConfig.getApiKey(),onInitializedListener);
            }
        });
    }





    private void playYouTubeVideo(final String link)
    {
        playerView.initialize(YouTubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                if (!b) {
                    try {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        if (UtilHelper.getYoutubeVideoID(link) != null)
                        {
                            youTubePlayer.loadVideo(UtilHelper.getYoutubeVideoID(link));
                            youTubePlayer.play();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AppController.getContext(),"Something Went Wrong with this video ",Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(getActivity(), 1).show();

                } else {

                    Toast.makeText(mContext, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
