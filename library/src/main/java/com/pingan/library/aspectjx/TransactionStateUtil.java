package com.pingan.library.aspectjx;

import com.pingan.library.Agent;

/**
 * Created by yejy on 17/5/19.
 */

public class TransactionStateUtil {

    public static void inspectAndInstrument(final TransactionState transactionState, final String url, final String httpMethod) {
        transactionState.setUrl(url);
        transactionState.setHttpMethod(httpMethod);
        transactionState.setCarrier(Agent.getNetworkCarrier());
        transactionState.setWanType(Agent.getNetworkWanType());
    }
}
