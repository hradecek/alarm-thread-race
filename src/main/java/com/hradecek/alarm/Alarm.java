package com.hradecek.alarm;

import java.util.Objects;

public class Alarm {

    public enum Severity {
        CRITICAL,
        CLEAR
    }

    public static Alarm of(String name, String resourceId, Severity severity) {
        return new Alarm(name, resourceId, severity);
    }

    private final String name;
    private final String resourceId;
    private final Severity severity;

    private Alarm(String name, String resourceId, Severity severity) {
        this.name = name;
        this.resourceId = resourceId;
        this.severity = severity;
    }

    public UniqueId getUniqueId() {
        return new UniqueId(this.resourceId);
    }

    public String getName() {
        return name;
    }

    public String getResourceId() {
        return resourceId;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return String.format("Alarm(name=%s, resourceId=%s, severity=%s)", name, resourceId, severity);
    }

    public static class UniqueId {

        private final String id;

        public UniqueId(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            UniqueId uniqueId = (UniqueId) other;
            return Objects.equals(id, uniqueId.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "UniqueId(" + id + ")";
        }
    }
}
