package edu.illinois.dscs.mypocket.controller;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityUnitTestCase;
import android.widget.Button;

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

    public void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        mAddTransactionButton = (Button) mainActivity.findViewById(R.id.add_transaction_button);
    }

    public void testGoingToAddTransaction() {
        // register next activity that need to be monitored.
        ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AddTransactionActivity.class.getName(), null, false);

        mainActivity.runOnUiThread(mAddTransactionButton::performClick);

        AddTransactionActivity addTransactionActivity = (AddTransactionActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(addTransactionActivity);
        addTransactionActivity.finish();
    }
}