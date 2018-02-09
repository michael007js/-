package com.blankj.utilcode.Service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.blankj.utilcode.activity.OnePiexlActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;



/**
 * 暂时弃用
 *
 * <service android:name="com.blankj.utilcode.Service.JobCastielService"
 * android:enabled="true"
 * android:exported="true"
 * android:permission="android.permission.BIND_JOB_SERVICE"/>
 * <p>
 * Created by leilei on 2017/8/10.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobCastielService extends JobService {
    private int kJobId = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e( "jobService启动");
        scheduleJob(getJobInfo());
        return START_REDELIVER_INTENT;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.e( "执行了onStartJob方法");
        boolean a = ActivityUtils.isRunning(this,  OnePiexlActivity.class);
        if (!a) {
            Intent it = new Intent(this, OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            ToastUtils.showShortToast(this, "保活启动");
        }
        LogUtils.e(a);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.e( "执行了onStopJob方法");
        scheduleJob(getJobInfo());
        return true;
    }

    //将任务作业发送到作业调度中去
    public void scheduleJob(JobInfo t) {
        LogUtils.e( "调度job");
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(t);
    }

    public JobInfo getJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(kJobId++, new ComponentName(this, JobCastielService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        //间隔100毫秒
        builder.setPeriodic(100);
        return builder.build();
    }

}
