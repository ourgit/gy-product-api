package utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2017/3/21.
 */
public class IdGenerator {
    /**
     * 实现不重复的时间
     */

    public static long getId() {
        AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());
        return lastTime.incrementAndGet();
    }
}
