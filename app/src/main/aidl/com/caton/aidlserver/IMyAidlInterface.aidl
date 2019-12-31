// IMyAidlInterface.aidl
package com.caton.aidlserver;

import com.caton.aidlserver.People;

// Declare any non-default types here with import statements

interface IMyAidlInterface {

    int addOperation(int a,int b);
    String getPeopleInfo1(in People people);
    String getPeopleInfo2(out People people);
    String getPeopleInfo3(inout People people);

}
