//package com.pingan.library.util;
//
//import com.pingan.greendao.bean.IDRequestInfoEntity;
//import com.pingan.greendao.gen.IDRequestInfoEntityDao;
//import com.pingan.library.common.NetMonitorEntity;
//import com.pingan.octopussdk.sdk.IDDBHelper;
//import com.pingan.octopussdk.sdk.SDKSetting;
//
//import pingan.greenrobot.greendao.async.AsyncSession;
//
///**
// * Created by yejy on 2017/6/7.
// */
//public class NetMonitorTask {
//
//    public static void execute(final NetMonitorEntity entity) {
//        final IDDBHelper iddbHelper = IDDBHelper.getInstance(SDKSetting.appContext);
//        AsyncSession asyncSession = new AsyncSession(iddbHelper.getDaoSession());
//        asyncSession.runInTx(new Runnable() {
//            @Override
//            public void run() {
//                IDRequestInfoEntityDao dao = iddbHelper.getIDRequestInfoEntityDao();
//                IDRequestInfoEntity idRequestInfoEntity = new IDRequestInfoEntity();
//                idRequestInfoEntity.setBusinessContent(entity.getBusinessContent());
//                idRequestInfoEntity.setResponseTimeTag(entity.getResponseTimeTag());
//                idRequestInfoEntity.setSendTimeTag(entity.getSendTimeTag());
//                idRequestInfoEntity.setFinishTimeTag(entity.getFinishTimeTag());
//                idRequestInfoEntity.setCode(entity.getCode());
//                idRequestInfoEntity.setErrorMsg(entity.getErrorMsg());
//                idRequestInfoEntity.setHostIpAddress(entity.getHostIpAddress());
//                idRequestInfoEntity.setMethod(entity.getMethod());
//                idRequestInfoEntity.setParams(entity.getParams());
//                idRequestInfoEntity.setRequestHeaders(entity.getRequestHeaders());
//                idRequestInfoEntity.setStatusCode(entity.getStatusCode());
//                idRequestInfoEntity.setTimeout(entity.getTimeout());
//                idRequestInfoEntity.setUrl(entity.getUrl());
//                dao.insertOrReplace(idRequestInfoEntity);
//            }
//        });
//    }
//}
