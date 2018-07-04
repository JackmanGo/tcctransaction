package org.sample.dubbo.test;

import interfaces.In;
import interfaces.In2;
import interfaces.In3;

public class TestReflect extends In3 implements In, In2 {

    public String say(String name, String age){

        return name+age;
    }
}
