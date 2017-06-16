package com.pingan.library;

import android.app.Application;
import android.content.Context;

import com.pingan.library.common.Connectivity;

/**
 * Created by yejy on 17/5/19.
 */
public class AndroidAspectjx implements IAspectjx {

    private Context context;
    private EelConfiguration configuration;

    public AndroidAspectjx(Context ctx,EelConfiguration configuration) {
        this.context = appContext(ctx);
        this.configuration = configuration;
    }

    private static Context appContext(final Context context) {
        if (!(context instanceof Application)) {
            return context.getApplicationContext();
        }
        return context;
    }

    public String getNetworkCarrier() {
        return Connectivity.carrierNameFromContext(this.context);
    }

    public String getNetworkWanType() {
        return Connectivity.wanType(this.context);
    }

    public static void init(Context context, EelConfiguration configuration) {
        Agent.setImpl(new AndroidAspectjx(context, configuration));
    }
}
