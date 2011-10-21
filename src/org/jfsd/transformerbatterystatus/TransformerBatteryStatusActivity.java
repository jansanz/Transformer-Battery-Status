package org.jfsd.transformerbatterystatus;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class TransformerBatteryStatusActivity extends Activity {

    private static final String TAG = "TransformerBatteryStatus";
    private static final Boolean DEBUG = true;

    // Themes
    // 0 - default Holo
    // 1 - Holo.Light
    private static final int THEME = 0;

    private int colorGreen = Color.GREEN;
    private int colorOrange = Color.rgb(255, 165, 0);
    private int colorRed = Color.RED;

    private TextView mDeviceBattery;
    private TextView mDockBattery;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupViews();
    }

    private void setupViews() {
        mDeviceBattery = (TextView) findViewById(R.id.tablet_battery_level);
        mDockBattery = (TextView) findViewById(R.id.dock_battery_level);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Get the ACTION_BATTERY_CHANGED data. Register a null broadcast
        // receiver since we only want the latest update and this intent is
        // sticky
        Intent batteryStatusIntent = registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

        // Use level, scale and maybe status to display if device is charging
        int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if (DEBUG)
            Log.v(TAG, "Level:" + level);

        int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryLevel = (int) (100.0 * level / scale);

        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if (DEBUG)
            Log.v(TAG, "Status:" + status);

        int dockLevel = batteryStatusIntent.getIntExtra("dock_level", -1);

        // Call our setTextAndColor function to set colors depending on battery
        // level
        setTextAndColor(mDeviceBattery, batteryLevel);
        setTextAndColor(mDockBattery, dockLevel);

        int dockStatus = batteryStatusIntent.getIntExtra("dock_status", -1);
        if (DEBUG)
            Log.v(TAG, "Dock status:" + dockStatus);

        // To check for all the keys that the ACTION_BATTERY_CHANGED intent has
        /*
         * Set<String> keys = batteryStatusIntent.getExtras().keySet(); for
         * (String key: keys) { Log.v(TAG, "key: " + key); }
         */
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        // We get the bounds of the current activity
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        // Detect taps outside activity's window
        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            if (DEBUG)
                Log.v(TAG, "tapped outside dialog activity");
            // We finish the activity
            this.finish();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Function to set the Text and color of the battery indicators
     * 
     * @param aTextView TextView target
     * @param aValue int indicating the battery level. -1 if disabled
     */
    private void setTextAndColor(TextView aTextView, int aValue) {

        // Check whether battery level is available (i.e. it won't show up when
        // dock is not connected)
        if (aValue < 0) {
            aTextView.setText("-");
        } else {
            aTextView.setText(aValue + "%");
        }

        // Changing color codes if theme different than default
        changeColorCodes(THEME);

        // Choose colors depending on battery live
        if (aValue >= 50) {
            aTextView.setTextColor(colorGreen);
        } else if (aValue >= 15) {
            aTextView.setTextColor(colorOrange);
        } else if (aValue >= 0) {
            aTextView.setTextColor(colorRed);
        }
    }

    /**
     * Change Battery level color codes based on the theme
     * 
     * @param THEME
     */
    private void changeColorCodes(int THEME) {

        if (THEME == 1) {
            // Olive Drab RGB: 107-142-35
            colorGreen = Color.rgb(107, 142, 35);
        }
    }

}
