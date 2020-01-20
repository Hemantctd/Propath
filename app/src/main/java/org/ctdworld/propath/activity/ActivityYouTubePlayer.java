package org.ctdworld.propath.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.YouTubeConfig;

public class ActivityYouTubePlayer extends YouTubeBaseActivity
{
    Toolbar mToolbar;
    TextView mToolbarTitle;
    YouTubePlayerView playerView;
    String mYouTubeUrl;
    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected void onStop() {
        super.onStop();
       // playerView.stop
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);
        playerView = findViewById(R.id.you_tube_link);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);


        setToolbar();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null)
            mYouTubeUrl = mBundle.getString(ConstHelper.Key.URL);




        playerView.initialize(YouTubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                if (!b) {
                    try {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        if (UtilHelper.getYoutubeVideoID(mYouTubeUrl) != null)
                            youTubePlayer.loadVideo(UtilHelper.getYoutubeVideoID(mYouTubeUrl));
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Something Went Wrong with this video ",Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(ActivityYouTubePlayer.this, RECOVERY_REQUEST).show();

                } else {

                    Toast.makeText(ActivityYouTubePlayer.this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void setToolbar()
    {
        mToolbarTitle.setText("You Tube");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
