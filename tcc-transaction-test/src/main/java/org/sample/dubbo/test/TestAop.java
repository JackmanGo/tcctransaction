package org.sample.dubbo.test;

import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestAop {


    @Autowired
    TestAop other;


    @TccTransaction
    public String methodFirst(String aaa){
        System.out.println(aaa);
        return "methodFirst";
    }

    @TccTransaction
    public String methodSecone(){
        System.out.println("methodSecone");
        return "methodSecone";
    }
}
