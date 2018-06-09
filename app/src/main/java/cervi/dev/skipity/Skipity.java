package cervi.dev.skipity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;



public class Skipity extends Service {

    class SettingsContentObserver extends ContentObserver {
        int previousVolume;
        Context context;

        public SettingsContentObserver(Context c, Handler handler) {
            super(handler);
            context=c;

            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

            int delta=previousVolume-currentVolume;
            if(prevent_onChange) {
                prevent_onChange=false;
                if (delta > 0) {
                    Intent nextSongIntent = new Intent();
                    KeyEvent nextSong;
                    nextSong = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
                    nextSongIntent.putExtra(Intent.EXTRA_KEY_EVENT, nextSong);
                    nextSongIntent.setAction(android.content.Intent.ACTION_MEDIA_BUTTON);
                    Skipity.this.sendBroadcast(nextSongIntent);
                    Log.w("CERV1", "DOWN");
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 1, AudioManager.FLAG_VIBRATE);
                    previousVolume = currentVolume;
                }

                else if (delta < 0) {
                    Intent nextSongIntent = new Intent();
                    KeyEvent nextSong;
                    nextSong = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
                    nextSongIntent.putExtra(Intent.EXTRA_KEY_EVENT, nextSong);
                    nextSongIntent.setAction(android.content.Intent.ACTION_MEDIA_BUTTON);
                    Skipity.this.sendBroadcast(nextSongIntent);
                    Log.w("CERV1", "UP");
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, AudioManager.FLAG_VIBRATE);
                    previousVolume = currentVolume;
                }
            }
            else{
                previousVolume = currentVolume;
                prevent_onChange=true;
            }
            Log.w("CERV1", "Current volume: " + audio.getStreamVolume(AudioManager.STREAM_MUSIC));
        }

    }

    private AudioManager am;
    public Intent nextSongIntent = new Intent();
    private KeyEvent nextSong;
    SettingsContentObserver mSettingsContentObserver;
    Boolean prevent_onChange;

    public Skipity() {
        super();
        prevent_onChange = true;
    }

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver offReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
                prevent_onChange=true;
                Log.w("CERV1", "LOCKED");
            }
        }
    };

    // BroadcastReceiver for handling ACTION_SCREEN_ON.
    private BroadcastReceiver onReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
                Log.w("CERV1", "UNLOCKED");
            }
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
        am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        nextSongIntent.setAction(android.content.Intent.ACTION_MEDIA_BUTTON);
        nextSong = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);

        IntentFilter filterScreenOff = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        IntentFilter filterScreenOn = new IntentFilter(Intent.ACTION_SCREEN_ON);

        registerReceiver(offReceiver, filterScreenOff);
        registerReceiver(onReceiver, filterScreenOn);

        startService();
    }


    private void startService() {

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(offReceiver);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}
