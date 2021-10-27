package com.ffmpeg.panel.util;

import android.os.AsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AsyncTool {


    public enum ETaskType {
        DEFAULT,
        IO_BOUND,
        CPU_BOUND,
        SCHEDULED_BOUND,
    }

    private static AsyncTool mAsyncTool = null;
    private ExecutorService mIOBoundThreadPool = Executors.newCachedThreadPool();
    private ExecutorService mCPUBoundThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(2);

    private AsyncTool() {
    }

    public synchronized static AsyncTool instance() {
        if (mAsyncTool == null) {
            mAsyncTool = new AsyncTool();
        }
        return mAsyncTool;
    }

    private ExecutorService getExecutorService(ETaskType type) {
        switch (type) {
            case DEFAULT:
                return mCPUBoundThreadPool;
            case IO_BOUND:
                return mIOBoundThreadPool;
            case CPU_BOUND:
                return mCPUBoundThreadPool;
            case SCHEDULED_BOUND:
                return mScheduledExecutorService;
            default:
                throw new RuntimeException("Forgot add case for new task type?");
        }
    }


    public void addScheduledTask(ETaskType type, long delay, Runnable runnable) {
        if (getExecutorService(type) instanceof ScheduledExecutorService) {
            ((ScheduledExecutorService) getExecutorService(type)).schedule(runnable, delay, TimeUnit.SECONDS);
        }
    }

    public Future<?> addTask(Runnable runnable) {
        return addTask(ETaskType.IO_BOUND, runnable);
    }

    public Future<?> addTask(ETaskType type, Runnable runnable) {
        return getExecutorService(type).submit(runnable);
    }

    public <T> Future<T> addTask(ETaskType type, Runnable runnable, T result) {
        return getExecutorService(type).submit(runnable, result);
    }

    public <T> Future<T> addTask(ETaskType type, Callable<T> task) {
        return getExecutorService(type).submit(task);
    }

    public <Params, Progress, Result>
    AsyncTask<Params, Progress, Result> addTask(ETaskType type,
                                                AsyncTask<Params, Progress, Result> task,
                                                Params... params) {
        if (task != null) {
            task.executeOnExecutor(getExecutorService(type), params);
        }
        return task;
    }
}
