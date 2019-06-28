package kg.docplus;

import android.app.Activity;
import com.quickblox.sample.core.CoreApp;
import kg.docplus.qbwrtc.util.QBResRequestExecutor;

public class App extends CoreApp {
    private static App instance;
    public static Activity activity;
    private QBResRequestExecutor qbResRequestExecutor;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }
}