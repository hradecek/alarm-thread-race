package com.hradecek.alarm;

import com.hradecek.alarm.solution.AlarmListSolution;
import javafx.util.Pair;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlarmProducer {

    // Boolean denotes whether alarm will be cleared immediately
    private static final Stream<Pair<String, Boolean>> ALARMS = Stream.of(
            new Pair<>("PowerSupplyRedundancyLost", true),
            new Pair<>("FanFailure", true),
            new Pair<>("MemoryFailure", true),
            new Pair<>("ChassisIntrusion", true),
            new Pair<>("TemperatureAlarm", false)
    );

//    private final AlarmList alarmList = new AlarmListThreadRace();
    private final AlarmList alarmList = new AlarmListSolution();

    public static void main(String[] args) {
        final var producer = new AlarmProducer();

        ALARMS.forEach(alarm -> {
            final var resourceId = UUID.randomUUID().toString();
            producer.raiseAlarm(alarm.getKey(), resourceId);
            if (alarm.getValue()) {
                producer.clearAlarm(alarm.getKey(), resourceId);
            }
        });
        producer.clearAlarm("FanFailure1", UUID.randomUUID().toString());

        waitBeforeGet();
        printActiveAlarms(producer.alarmList);
        System.out.println("Clear cache size: " + ((AlarmListSolution) producer.alarmList).clearLatches.size());
        System.exit(0);
    }

    public void raiseAlarm(final String name, final String resourceId) {
        alarmList.notifyAlarmParallel(Alarm.of(name, resourceId, Alarm.Severity.CRITICAL));
    }

    public void clearAlarm(final String name, final String resourceId) {
        alarmList.notifyAlarmParallel(Alarm.of(name, resourceId, Alarm.Severity.CLEAR));
    }

    private static void printActiveAlarms(final AlarmList alarmList) {
        final var actives = alarmList.getAlarmList();
        System.out.printf("Active alarms (%s): %s\n", actives.size(), actives.stream().map(Alarm::getName).collect(Collectors.toList()));
    }

    public static void waitBeforeGet() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Whoohoa!");
        }
    }
}

