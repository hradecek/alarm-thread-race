package com.hradecek.alarm.solution;

import com.google.common.util.concurrent.Uninterruptibles;
import com.hradecek.alarm.Alarm;
import com.hradecek.alarm.Alarm.UniqueId;
import com.hradecek.alarm.AlarmListParallelBase;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class AlarmListSolution extends AlarmListParallelBase {

    public final Map<UniqueId, CountDownLatch> clearLatches = new ConcurrentHashMap<>();

    @Override
    protected void notifyAlarm(Alarm alarm) {
        if (Alarm.Severity.CLEAR == alarm.getSeverity()) {
            if (!alarmList.containsKey(alarm.getUniqueId())) {
                var latch = new CountDownLatch(1);
                clearLatches.put(alarm.getUniqueId(), latch);
                Uninterruptibles.awaitUninterruptibly(latch, Duration.ofSeconds(5));
            }
            clearAlarm(alarm);
        } else {
            raiseAlarm(alarm);
            if (clearLatches.containsKey(alarm.getUniqueId())) {
                clearLatches.remove(alarm.getUniqueId()).countDown();
            }
        }
    }
}
