package edu.illinois.dscs.mypocket.controller;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityUnitTestCase;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;

import edu.illinois.dscs.mypocket.R;

/**
 * @author Cassio
 * @version 1.0
 */
public class MainTransactionActivityTest extends ActivityUnitTestCase<MainActivity> {

    private MainActivity mainActivity;
    private Button mAddTransactionButton;

    public MainTransactionActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        mAddTransactionButton = (Button) mainActivity.findViewById(R.id.add_transaction_button);
    }

    @Test
    public void testGoingToAddTransaction() {
        // register next activity that need to be monitored.
        ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AddTransactionActivity.class.getName(), null, false);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                mAddTransactionButton.performClick();
            }
        });

        AddTransactionActivity addTransactionActivity = (AddTransactionActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(addTransactionActivity);
        addTransactionActivity.finish();
    }
}