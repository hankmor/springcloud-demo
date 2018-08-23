package com.belonk.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * Created by sun on 2018/8/21.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class CommandHelloWorld extends HystrixCommand<String> {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String name;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey(name));
        this.name = name;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Override
    protected String run() throws Exception {
        return "Hello, " + name + "!";
    }
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static class UnitTest {
        private CommandHelloWorld worldCommand = new CommandHelloWorld("World");
        private CommandHelloWorld belonkCommand = new CommandHelloWorld("Belonk");

        @Test
        public void testSynchronous() {
            // 同步执行
            String world = worldCommand.execute();
            String belonk = belonkCommand.execute();
            assertEquals("Hello, World!", world);
            assertEquals("Hello, Belonk!", belonk);
        }

        @Test
        public void testAsynchronous() throws ExecutionException, InterruptedException {
            // 异步执行
            Future<String> worldFuture = worldCommand.queue();
            Future<String> belonkFuture = belonkCommand.queue();

            assertEquals("Hello, World!", worldFuture.get());
            assertEquals("Hello, Belonk!", belonkFuture.get());
        }

        @Test
        public void testObservable() {
            // observe : 返回一个立即执行命令的Observable对象，Observable可以重复使用
            Observable<String> worldObservable = worldCommand.observe();
            Observable<String> belonkObservable = belonkCommand.observe();

            // 阻塞
            String world = worldObservable.toBlocking().single();
            String belonk = belonkObservable.toBlocking().single();
            assertEquals("Hello, World!", world);
            assertEquals("Hello, Belonk!", belonk);

            // 非阻塞
            worldObservable.subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                    System.out.println("onCompleted");
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("onError");
                    throwable.printStackTrace();
                }

                @Override
                public void onNext(String s) {
                    System.out.println("onNext : " + s);
                }
            });

            // 非阻塞
            belonkObservable.subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    System.out.println("call : " + s);
                }
            });
        }

        @Test
        public void testToObservable() {
            // toObservable : 返回一个不执行命令的冷Observable对象，直到订阅了Observable才会发布其结果，只能使用一次
            Observable<String> worldObservable = worldCommand.toObservable();
            Observable<String> belonkObservable = belonkCommand.toObservable();

            // 阻塞
            String world = worldObservable.toBlocking().single();
            String belonk = belonkObservable.toBlocking().single();
            assertEquals("Hello, World!", world);
            assertEquals("Hello, Belonk!", belonk);
        }
    }
}