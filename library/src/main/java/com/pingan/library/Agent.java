package com.pingan.library;

import java.util.Objects;

/**
 * Created by yejy on 17/5/19.
 */

public class Agent{

    protected static IAspectjx impl;

    private static Object implLock = new Object();

    public static void setImpl(final IAspectjx impl) {
        synchronized (Agent.implLock) {
            if (impl == null) {
                Agent.impl = new NullAspectjx();
            } else {
                Agent.impl = impl;
            }
        }
    }

    public static IAspectjx getImpl() {
        synchronized (Agent.implLock) {
            return Agent.impl;
        }
    }

    public static String getNetworkCarrier() {
        return impl.getNetworkCarrier();
    }

    public static String getNetworkWanType() {
        return impl.getNetworkWanType();
    }
}
