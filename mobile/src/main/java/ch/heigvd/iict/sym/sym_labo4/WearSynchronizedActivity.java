package ch.heigvd.iict.sym.sym_labo4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class WearSynchronizedActivity extends AppCompatActivity implements
        DataClient.OnDataChangedListener,
        OnSuccessListener<DataItemBuffer> {

    private static final String TAG = WearSynchronizedActivity.class.getSimpleName();

    private SeekBar seekBarRed = null;
    private SeekBar seekBarGreen = null;
    private SeekBar seekBarBlue = null;

    private DataClient mDataClient;
    private static final String RED = "RED";
    private static final String GREEN = "GREEN";
    private static final String BLUE = "BLUE";
    private static final String TIME = "TIME";

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearsynchronized);
        mDataClient = Wearable.getDataClient(this);

        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);

        seekBarBlue.setMax(255);
        seekBarGreen.setMax(255);
        seekBarRed.setMax(255);

        red = seekBarRed.getProgress();
        green = seekBarGreen.getProgress();
        blue = seekBarBlue.getProgress();
        updateColor(red, green, blue);

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateColor(red, green, blue);
                updateWearable();
            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateColor(red, green, blue);
                updateWearable();
            }
        });

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateColor(red, green, blue);
                updateWearable();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataClient.getDataItems().addOnSuccessListener(this);
        mDataClient.addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataClient.removeListener(this);
    }

    private void updateWearable() {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/color");
        putDataMapReq.getDataMap().putInt(RED, red);
        putDataMapReq.getDataMap().putInt(GREEN, green);
        putDataMapReq.getDataMap().putInt(BLUE, blue);
        putDataMapReq.getDataMap().putLong(TIME, System.currentTimeMillis());
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        mDataClient.putDataItem(putDataReq);
    }

    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/color") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    updateColor(dataMap.getInt(RED), dataMap.getInt(GREEN), dataMap.getInt(BLUE));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onSuccess(DataItemBuffer dataItems) {
        // récupérer la dernière entrèe de du device de la montre.
        DataItem mostRecent = null;
        for (DataItem dataItem : dataItems) {
            if (mostRecent == null) {
                mostRecent = dataItem;
                continue;
            }
            if (DataMapItem.fromDataItem(dataItem).getDataMap().getLong(TIME) < DataMapItem.fromDataItem(mostRecent).getDataMap().getLong(TIME)) {
                mostRecent = dataItem;
            }
        }
        if (mostRecent != null) {
            // DataItem changed
            DataItem item = mostRecent;
            if (item.getUri().getPath().compareTo("/color") == 0) {
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                updateColor(dataMap.getInt(RED), dataMap.getInt(GREEN), dataMap.getInt(BLUE));
            }
        }
    }

    /*
     *  Code utilitaire fourni
     */

    /**
     * Method used to update the background color of the activity
     *
     * @param r The red composant (0...255)
     * @param g The green composant (0...255)
     * @param b The blue composant (0...255)
     */
    private void updateColor(int r, int g, int b) {
        seekBarRed.setProgress(r);
        seekBarGreen.setProgress(g);
        seekBarBlue.setProgress(b);
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(Color.argb(255, r, g, b));
    }

}
