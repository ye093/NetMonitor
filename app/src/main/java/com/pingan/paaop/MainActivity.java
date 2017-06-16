package com.pingan.paaop;

import android.app.Activity;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.pingan.aop.annotation.DebugLog;
import com.pingan.paaop.retrofitTest.RetrofitTest;

public class MainActivity extends Activity {
    private static final String SERVER_URL = "http://43.248.77.83:8801/post";
    private static final String BASE_URL = "http://43.248.77.83:8801/";


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Check logcat!");
        setContentView(tv);

        printArgs("The", "Quick", "Brown", "Fox");

        Log.i("Fibonacci", "fibonacci's 4th number is " + fibonacci(4));

        Greeter greeter = new Greeter("Jake");
        Log.d("Greeting", greeter.sayHello());

        Charmer charmer = new Charmer("Jake");
        Log.d("Charming", charmer.askHowAreYou());

        startSleepyThread();

        OkHttp2Test okHttp2Test = new OkHttp2Test(SERVER_URL);
        okHttp2Test.startTest();
//
//        RetrofitTest retrofitTest = new RetrofitTest(BASE_URL);
//        retrofitTest.startTest();
//
//
//        OkHttp3Test okHttp3Test = new OkHttp3Test(SERVER_URL);
//        okHttp3Test.startTest();

//        for (int i = 0; i < 10; i++) {
//            VolleyTest volleyTest = new VolleyTest(this);
//            volleyTest.startTest(SERVER_URL);
//        }

    }

    @DebugLog
    private void printArgs(String... args) {
        for (String arg : args) {
            Log.i("Args", arg);
        }
    }

    @DebugLog
    private int fibonacci(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Number must be greater than zero.");
        }
        if (number == 1 || number == 2) {
            return 1;
        }
        // NOTE: Don't ever do this. Use the iterative approach!
        return fibonacci(number - 1) + fibonacci(number - 2);
    }

    private void startSleepyThread() {
        new Thread(new Runnable() {
            private static final long SOME_POINTLESS_AMOUNT_OF_TIME = 50;

            @Override public void run() {
                sleepyMethod(SOME_POINTLESS_AMOUNT_OF_TIME);
            }

            @DebugLog
            private void sleepyMethod(long milliseconds) {
                SystemClock.sleep(milliseconds);
            }
        }, "I'm a lazy thr.. bah! whatever!").start();
    }

    @DebugLog
    static class Greeter {
        private final String name;

        Greeter(String name) {
            this.name = name;
        }

        private String sayHello() {
            return "Hello, " + name;
        }
    }

    @DebugLog
    static class Charmer {
        private final String name;

        private Charmer(String name) {
            this.name = name;
        }

        public String askHowAreYou() {
            return "How are you " + name + "?";
        }
    }
}
