package com.hradecek.alarm.trouble;

import com.hradecek.alarm.Alarm;
import com.hradecek.alarm.AlarmListParallelBase;

public class AlarmListThreadRace extends AlarmListParallelBase {

    @Override
    protected void notifyAlarm(final Alarm alarm) {
        if (Alarm.Severity.CLEAR == alarm.getSeverity()) {
            clearAlarm(alarm);
        } else {
            raiseAlarm(alarm);
        }
    }
}
