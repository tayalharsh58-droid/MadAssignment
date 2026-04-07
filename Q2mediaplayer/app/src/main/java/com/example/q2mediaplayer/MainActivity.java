package com.example.q2mediaplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.q2mediaplayer.R;

public class MainActivity extends AppCompatActivity {

    Button openFile, openURL, play, pause, stop, restart;
    VideoView videoView;
    MediaPlayer mediaPlayer;
    Uri audioUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openFile = findViewById(R.id.openFile);
        openURL = findViewById(R.id.openURL);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        restart = findViewById(R.id.restart);
        videoView = findViewById(R.id.videoView);

        // Load Audio
        openFile.setOnClickListener(v -> {
            audioUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample);

            // 🔹 Fix: release old player before creating new one
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, audioUri);
            Toast.makeText(this, "Audio Loaded", Toast.LENGTH_SHORT).show();
        });

        // Load Video URL
        openURL.setOnClickListener(v -> {
            String url = "https://cdn.pixabay.com/video/2026/03/08/338904_large.mp4";
            videoView.setVideoURI(Uri.parse(url));
            Toast.makeText(this, "Video Loaded", Toast.LENGTH_SHORT).show();
        });

        // Play
        play.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.start();

            // 🔹 Fix: start video only if loaded
            if (videoView != null) videoView.start();
        });

        // Pause
        pause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
            if (videoView.isPlaying()) videoView.pause();
        });

        // Stop
        stop.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            videoView.stopPlayback();
        });

        // Restart
        restart.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.seekTo(0);
            if (videoView != null) videoView.seekTo(0);
        });
    }
}