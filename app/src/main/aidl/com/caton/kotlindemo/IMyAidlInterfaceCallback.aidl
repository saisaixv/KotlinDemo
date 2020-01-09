// IMyAidlInterfaceCallback.aidl
package com.caton.kotlindemo;

// Declare any non-default types here with import statements

interface IMyAidlInterfaceCallback {
    oneway void stateChanged(int state,String profileName,String msg);
    oneway void trafficUpdated(long txRate,long rxRate,long txTotal, long rxTotal);
}
