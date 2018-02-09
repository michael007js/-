package com.blankj.utilcode.Service;

import android.app.Service;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by leilei on 2017/8/2.
 */
public abstract class BaseService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        create();
    }

    /**
     * START_STICKY：如果service进程被kill掉，
     * 保留service的状态为开始状态，
     * 但不保留递送的intent对象。
     * 随后系统会尝试重新创建service，
     * 由于服务状态为开始状态，
     * 所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
     * 如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
     * <p>
     * START_NOT_STICKY：“非粘性的”。
     * 使用这个返回值时，
     * 如果在执行完onStartCommand后，
     * 服务被异常kill掉，
     * 系统不会自动重启该服务。
     * <p>
     * START_REDELIVER_INTENT：
     * 重传Intent。
     * 使用这个返回值时，
     * 如果在执行完onStartCommand后，
     * 服务被异常kill掉，
     * 系统会自动重启该服务，
     * 并将Intent的值传入。
     * <p>
     * START_STICKY_COMPATIBILITY：
     * START_STICKY的兼容版本，
     * 但不保证服务被kill后一定能重启。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        start(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        bind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean b = super.onUnbind(intent);
        unbind(intent);
        return b;
    }

    /**
     * 在service和旧的client之间的所有捆绑联系在onUnbind里面全都结束之后，
     * 如果有一个新的client用bind连接上service，
     * 就会调用onRebind()
     *
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        rebind(intent);
    }

    /**
     * 所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，
     * 系统会调用OnLowMemory。
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        lowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /**
                 * 回调只有当我们程序中的所有UI组件全部不可见的时候才会触发，
                 * 这和onStop()方法还是有很大区别的，
                 * 因为onStop()方法只是当一个Activity完全不可见的时候就会调用，
                 * 比如说用户打开了我们程序中的另一个Activity。因此，
                 * 我们可以在onStop()方法中去释放一些Activity相关的资源，
                 * 比如说取消网络连接或者注销广播接收器等，
                 * 但是像UI相关的资源应该一直要等到onTrimMemory(TRIM_MEMORY_UI_HIDDEN)这个回调之后才去释放，
                 * 这样可以保证如果用户只是从我们程序的一个Activity回到了另外一个Activity，
                 * 界面相关的资源都不需要重新加载，
                 * 从而提升响应速度。
                 * */

                TRIM_MEMORY_UI_HIDDEN();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                /**
                 * 表示应用程序正常运行，
                 * 并且不会被杀掉。
                 * 但是目前手机的内存已经有点低了，
                 * 系统可能会开始根据LRU缓存规则来去杀死进程了。
                 * */

                TRIM_MEMORY_RUNNING_MODERATE();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                /**
                 * 表示应用程序正常运行，
                 * 并且不会被杀掉。
                 * 但是目前手机的内存已经非常低了，
                 * 我们应该去释放掉一些不必要的资源以提升系统的性能，
                 * 同时这也会直接影响到我们应用程序的性能。
                 * */

                TRIM_MEMORY_RUNNING_LOW();
                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /**
                 * 表示应用程序仍然正常运行，
                 * 但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。
                 * 这个时候我们应当尽可能地去释放任何不必要的资源，
                 * 不然的话系统可能会继续杀掉所有缓存中的进程，
                 * 并且开始杀掉一些本来应当保持运行的进程，
                 * 比如说后台运行的服务。
                 * */
                TRIM_MEMORY_RUNNING_CRITICAL();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                /**
                 *  表示手机目前内存已经很低了，
                 *  系统准备开始根据LRU缓存来清理进程。
                 *  这个时候我们的程序在LRU缓存列表的最近位置，
                 *  是不太可能被清理掉的，
                 *  但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，
                 *  从而让我们的程序更长时间地保留在缓存当中，
                 *  这样当用户返回我们的程序时会感觉非常顺畅，
                 *  而不是经历了一次重新启动的过程。
                 * */
                TRIM_MEMORY_BACKGROUND();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                /**
                 * 表示手机目前内存已经很低了，
                 * 并且我们的程序处于LRU缓存列表的中间位置，
                 * 如果手机内存还得不到进一步释放的话，
                 * 那么我们的程序就有被系统杀掉的风险了。
                 * */
                TRIM_MEMORY_MODERATE();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /**
                 * 表示手机目前内存已经很低了，
                 * 并且我们的程序处于LRU缓存列表的最边缘位置，
                 * 系统会最优先考虑杀掉我们的应用程序，
                 * 在这个时候应当尽可能地把一切可以释放的东西都进行释放。
                 * */
                TRIM_MEMORY_COMPLETE();
                break;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        taskRemoved(rootIntent);
    }


    protected abstract void create();

    protected abstract IBinder bind(Intent intent);

    protected abstract int startCommand(Intent intent, int flags, int startId);

    protected abstract void start(Intent intent, int startId);

    protected abstract void destroy();

    protected abstract boolean unbind(Intent intent);

    protected abstract void rebind(Intent intent);

    protected abstract void lowMemory();

    protected abstract void taskRemoved(Intent rootIntent);

    protected abstract void TRIM_MEMORY_UI_HIDDEN();

    protected abstract void TRIM_MEMORY_RUNNING_MODERATE();

    protected abstract void TRIM_MEMORY_RUNNING_LOW();

    protected abstract void TRIM_MEMORY_RUNNING_CRITICAL();

    protected abstract void TRIM_MEMORY_BACKGROUND();

    protected abstract void TRIM_MEMORY_MODERATE();

    protected abstract void TRIM_MEMORY_COMPLETE();
}
