package ch.heigvd.iict.sym.sym_labo4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import ch.heigvd.iict.sym.sym_labo4.widgets.CircularSliderRangeFixed;

public class MainActivityWear extends WearableActivity implements
        CircularSliderRange.OnSliderRangeMovedListener {

    private static final String TAG = MainActivityWear.class.getSimpleName();
    private static final int ANGLE_OFFSET = 90;

    private BoxInsetLayout mContainerView           = null;

    private CircularSliderRangeFixed redSlider      = null;
    private CircularSliderRangeFixed greenSlider    = null;
    private CircularSliderRangeFixed blueSlider     = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        setAmbientEnabled();

        //link to GUI
        this.mContainerView = findViewById(R.id.container);
        this.redSlider      = findViewById(R.id.circular_red);
        this.greenSlider    = findViewById(R.id.circular_green);
        this.blueSlider     = findViewById(R.id.circular_blue);

        //events
        this.redSlider.setOnSliderRangeMovedListener(this);
        this.greenSlider.setOnSliderRangeMovedListener(this);
        this.blueSlider.setOnSliderRangeMovedListener(this);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateBackgroundColor();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateBackgroundColor();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            int r = 0, g = 0, b = 0; //use real color...
            mContainerView.setBackgroundColor(Color.argb(255, r,g,b));
        }
    }

    @Override public void onStartSliderMoved(double pos) { /* NOTHING TO DO */ }
    @Override public void onEndSliderMoved(double pos) { /* NOTHING TO DO */ }
    @Override public void onStartSliderEvent(ThumbEvent event) { /* NOTHING TO DO */ }
    @Override public void onEndSliderEvent(ThumbEvent event) {
        //one of the slider was moved
        //DO SOMETHING
    }

    /**
     * Method used to convert a n angle into the corresponding RGB color component (r, g or b)
     * @param endAngle The angle in degree 0-359
     * @return The color component 0-255
     */
    private int convertEndAngleToRGBComponent(double endAngle) {
        return (int) Math.round(255 * ((endAngle + ANGLE_OFFSET) % 360) / 360.0);
    }

    /**
     *  Method used to convert a RGB color component (r, g or b) into the corresponding angle
     *  for the slider (endAngle)
     * @param colorComponent The color component 0-255
     * @return The angle in degree 0-359
     */
    private double convertRGBValueToEndAngle(int colorComponent) {
        return ((((double)colorComponent)/ 255.0) * 360.0) - ANGLE_OFFSET;
    }

}
