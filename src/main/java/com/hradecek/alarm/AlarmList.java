package com.hradecek.alarm;

import java.util.Collection;

public interface AlarmList {

    void notifyAlarmParallel(final Alarm alarm);

    Collection<Alarm> getAlarmList();
}
