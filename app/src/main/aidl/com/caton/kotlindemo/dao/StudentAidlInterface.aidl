// StudentAidlInterface.aidl
package com.caton.kotlindemo.dao;

// Declare any non-default types here with import statements
import com.caton.kotlindemo.dao.Student;
import com.caton.kotlindemo.IMyAidlInterfaceCallback;

interface StudentAidlInterface {
    String getName(inout Student stu);
    void study(inout Student stu);

    oneway void registerCallback(IMyAidlInterfaceCallback cb);
    oneway void unregisterCallback(IMyAidlInterfaceCallback cb);
}
