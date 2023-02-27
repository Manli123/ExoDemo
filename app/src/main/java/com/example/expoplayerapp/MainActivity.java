package com.example.expoplayerapp;

import static com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // creating a variable for exoplayerview.
    PlayerView exoPlayerView;

    // creating a variable for exoplayer
    ExoPlayer exoPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exoPlayerView = findViewById(R.id.idExoPlayerVIew);

//
//        ExoPlayer player = new ExoPlayer.Builder(MainActivity.this).build();
//
    }

    @Override
    protected void onResume() {
        initilisePlayer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        exoPlayer.stop();
        exoPlayer.release();
        super.onPause();
    }

    @Override
    protected void onStop() {
//        exoPlayer.stop();
//        exoPlayer.release();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        exoPlayer.stop();
        exoPlayer.release();
        super.onDestroy();
    }

    public void initilisePlayer()
    {
        DefaultRenderersFactory rf = new DefaultRenderersFactory(MainActivity.this)
                .setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER).setMediaCodecSelector(
                        new MediaCodecSelector() {
                            @Override
                            public List<MediaCodecInfo> getDecoderInfos(String mimeType,
                                                                        boolean requiresSecureDecoder, boolean requiresTunnelingDecoder)
                                    throws MediaCodecUtil.DecoderQueryException {
                                List<MediaCodecInfo> decoderInfos = MediaCodecSelector.DEFAULT
                                        .getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder);
                                if (MimeTypes.VIDEO_H264.equals(mimeType)) {
                                    // copy the list because MediaCodecSelector.DEFAULT returns an unmodifiable list
                                    decoderInfos = new ArrayList<>(decoderInfos);
                                    Collections.reverse(decoderInfos);
                                }
                                return decoderInfos;
                            }
                        });
     
        //sorry i can not share url.
//     
        //for rotate issue on android 8.1 video 1811
      //  String videoURL = "https://thegoatappvideos.s3.us-east-2.amazonaws.com/post_video/man_video.mp4";
        
        //for MediaCodecVideoRenderer: Video codec error
    //  java.lang.IllegalStateException
      //  at android.media.MediaCodec.native_dequeueOutputBuffer(Native Method)
        
        //for resolution issue
         String videoURL = "https://thegoatappvideos.s3.us-east-2.amazonaws.com/post_video/productio.mp4 ";


        DefaultLoadControl loadControl = new DefaultLoadControl
                .Builder()
                .setBufferDurationsMs(32*1024, 64*1024, 1024, 1024)
                .createDefaultLoadControl();

        exoPlayer = new ExoPlayer.Builder(MainActivity.this,rf).setLoadControl(loadControl).build();

//

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);


            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                Player.Listener.super.onMediaMetadataChanged(mediaMetadata);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
                Log.e("play_error",""+error.getMessage().toString()+""+error.errorCode);
//                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show();
            }
        });



//
        exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        exoPlayerView.setShutterBackgroundColor(Color.TRANSPARENT);
        exoPlayerView.setKeepContentOnPlayerReset(true);
        exoPlayer.seekTo(0);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);



        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(
                new CacheDataSource.Factory()
                        .setCache(com.axat.starbarn.model.SimpleMediaPlayer.getInstance(MainActivity.this))
                        .setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory()
                                .setUserAgent(MainActivity.this.getString(R.string.app_name)))
                        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        ).createMediaSource(MediaItem.fromUri(videoURL));


        exoPlayer.setMediaSource(mediaSource,true);
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();
        exoPlayer.play();
    }
}
