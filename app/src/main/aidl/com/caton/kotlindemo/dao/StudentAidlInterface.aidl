// StudentAidlInterface.aidl
package com.caton.kotlindemo.dao;

// Declare any non-default types here with import statements
import com.caton.kotlindemo.dao.Student;

interface StudentAidlInterface {
    String getName(inout Student stu);
    void study(inout Student stu);
}
