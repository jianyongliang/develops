package com.example.demo.thread;

import java.util.concurrent.Executors;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ThreadPoolDemo {

    public static void main(String[] args) {
        //启动线程的方式
        new Thread(()->{

            System.out.println("new");

        }).start();

        //启动
        IntStream.range(0,3).forEachOrdered(new IntConsumer() {
            @Override
            public void accept(int value) {

                System.out.println(value);

            }
        });

        //实现runnable接口

        Executors.newCachedThreadPool();



    }





}
