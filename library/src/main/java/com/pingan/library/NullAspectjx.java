package com.pingan.library;

/**
 * Created by yejy on 17/5/19.
 */

public class NullAspectjx implements IAspectjx{
    @Override
    public String getNetworkCarrier() {
        return null;
    }

    @Override
    public String getNetworkWanType() {
        return null;
    }
}
