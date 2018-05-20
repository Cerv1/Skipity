package cervi.dev.skipity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;


public class Skipity extends Service {

    private AudioManager am;
    public Intent nextSongIntent = new Intent();
    private KeyEvent nextSong;


    public Skipity() {
        super();
    }

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                nextSongIntent.putExtra(Intent.EXTRA_KEY_EVENT, nextSong);
                Skipity.this.sendBroadcast(nextSongIntent);
                Log.w("CERV1", "Next Song");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // registerReceiver(screenOffBroadcast, filter);

        am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        nextSongIntent.setAction(android.content.Intent.ACTION_MEDIA_BUTTON);
        nextSong = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        startService();
    }

    private void startService() {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // handleCommand(intent);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Log.w("CERV1", "Service stopped");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null; // no IPC used
    }
}
