package edu.up.cs301.checkers;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Main program class which contains its own onTouchListener
 *
 * Implemented Features:
 * [10 points] Select one element drawn on a SurfaceView. Whenever the user taps this element, it
 * changes color or becomes highlighted in some manner. The element should not change if you tap
 * elsewhere.
 * ? [5 points] Select a second element that also responds in this way.
 * ? [5 points] If an element is tapped again, it reverts to being drawn in its original way
 * [15 points] Select four elements displayed on your surface view. Each time the user taps any one of
 * these elements, the element's name should displayed in a TextView or be drawn on a SurfaceView.
 * [10 points] Select a Spinner, RadioButton group or CheckBox on your user interface. When the user
 * adjusts this control, the game should respond in a way that is consistent with your game.
 * ? [10 points] Repeat this task for a second view which can be of the same type or a different type
 *
 * @author Sean Tollisen, Branden Vennes, Dominic Ferrari, and Brandon Sit.
 */
public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener,
        android.widget.SpinnerAdapter, AdapterView.OnItemSelectedListener {

    Doodle checkerBoard;
    RadioButton colorBlindModeOn;
    RadioButton getColorBlindModeOff;
    Spinner menuSpinner;
    Button menu;
    TextView selectedpiece;

    /**
     External Citation
     Date:       1/31/2016
     Problem:    Forgot what the method was to retrieve a view from the layout
     Resource:   In-class sample code
     https://learning.up.edu/moodle/pluginfile.php/210714/mod_resource/content/0/MainActivity.java
     Solution:   (sampleType) findViewById(R.id.sample);
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkerBoard = (Doodle) findViewById(R.id.checkerBoardView);
        checkerBoard.setupCheckerboard();
        checkerBoard.setOnTouchListener(this);

        colorBlindModeOn = (RadioButton) findViewById(R.id.colorBlindModeOn);
        colorBlindModeOn.setOnClickListener(this);
        getColorBlindModeOff = (RadioButton) findViewById(R.id.colorBlindModeOff);
        getColorBlindModeOff.setOnClickListener(this);

        menu = (Button)findViewById(R.id.menuButton);
        menu.setOnClickListener(this);

        menuSpinner = (Spinner) findViewById(R.id.menuSpinner);
        //create and add the spinner adapter to the menuSpinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.settings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuSpinner.setAdapter(adapter);
        menuSpinner.setOnItemSelectedListener(this);

        selectedpiece = (TextView) findViewById(R.id.selectedPieceText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *
     * onTouch is evoked when comeone performs an event on
     * a view that has this onTouchListener set
     *
     * @param v
     * @param event
     * @return
     */
    /**
     External Citation
     Date:       1/31/2016
     Problem:    Forgot how to register touch events
     Resource:   A website with example code found through a google search
     https://blahti.wordpress.com/2012/06/26/images-with-clickable-areas/
     Solution:   public boolean onTouch(View v, MotionEvent event)
     */
    public boolean onTouch(View v, MotionEvent event) {
    /*
        if the user touches the checkerboard, the x and y positions will be passed
        to the checkerBoard surface view's highlightChecker method to see if a checker
        should be highlighted
        also ensures only one touch is registered
    */
        if (v.getId() == R.id.checkerBoardView&&((event.getEventTime()-event.getDownTime())==0)){
            checkerBoard.highlightChecker(event.getX(), event.getY());
            //Set the selected piece display to the chess position
            //of the last highlighted piece
            selectedpiece.setText("" + (checkerBoard.getCurrHighlighted()).getChessPos());
            return true;
        }
        return false;

    }

    /**
     External Citation
     Date:       2/1/2016
     Problem:    Needed a structure for seeing if a radio button is checked
     Resource:   Android documentation
     http://developer.android.com/guide/topics/ui/controls/radiobutton.html
     Solution:   boolean checked = ((RadioButton) view).isChecked(); and
                use switch statements
     */
    public void onClick(View view) {
        // enable or disable color blind mode
        if (view.getId() == R.id.colorBlindModeOff || view.getId() == R.id.colorBlindModeOn) {
            boolean checked = ((RadioButton) view).isChecked();

            switch (view.getId()) {
                case R.id.colorBlindModeOn:
                    if (checked) {
                        checkerBoard.setColorBlindMode(true);
                    }
                    break;
                case R.id.colorBlindModeOff:
                    if (checked) {
                        checkerBoard.setColorBlindMode(false);
                    }
                    break;
            }
        }
        // open menu spinner
        else {
            menuSpinner.performClick();
        }
    }

    /**
     External Citation
     Date:       1/31/2016
     Problem:    Unsure how to access a spinner's choices
     Resource:   A website with example code found through a google search
     http://www.tutorialspoint.com/android/android_spinner_control.htm
     Solution:   public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // main menu choice located at position 1
        if (pos == 1) {
            // send user to main menu, main menu is not operational
            setContentView(R.layout.main_menu);
        }
    }

    // unneeded but required methods below
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}