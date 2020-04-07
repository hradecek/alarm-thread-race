package com.hradecek.alarm;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.hradecek.alarm.Alarm.UniqueId;

public abstract class AlarmListParallelBase implements AlarmList {

    private static final ExecutorService EXECUTOR =
            Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    protected final Map<UniqueId, Alarm> alarmList = new ConcurrentHashMap<>();

    public void notifyAlarmParallel(final Alarm alarm) {
        EXECUTOR.submit(() -> notifyAlarm(alarm));
    }

    protected void clearAlarm(final Alarm alarm) {
        Optional.ofNullable(alarmList.remove(alarm.getUniqueId())).ifPresentOrElse(
                cleared -> System.out.printf("[%s] Cleared %s\n", Thread.currentThread().getName(), cleared),
                () -> System.out.printf("[%s] Not found raise for %s\n", Thread.currentThread().getName(), alarm));
        ;
    }

    protected void raiseAlarm(final Alarm alarm) {
        hardWorkingComputingOperationThatBlocksTheThread();
        alarmList.put(alarm.getUniqueId(), alarm);
        System.out.printf("[%s] Raised %s\n", Thread.currentThread().getName(), alarm);
    }

    public Collection<Alarm> getAlarmList() {
        return alarmList.values();
    }

    protected static void hardWorkingComputingOperationThatBlocksTheThread() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            throw new RuntimeException("I wont' do that!");
        }
    }

    protected abstract void notifyAlarm(final Alarm alarm);
}
