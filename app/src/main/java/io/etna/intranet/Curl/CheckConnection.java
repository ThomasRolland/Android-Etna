package io.etna.intranet.Curl;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by nextjoey on 21/04/2017.
 */

public class CheckConnection {
    public static boolean execute(Context context) {
        return  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
