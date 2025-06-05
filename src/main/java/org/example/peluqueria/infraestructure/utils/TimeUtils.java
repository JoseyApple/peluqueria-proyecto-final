package org.example.peluqueria.infraestructure.utils;

import java.time.LocalTime;

public class TimeUtils {

    private TimeUtils() {}

    public static boolean isWithinWorkingHours(LocalTime startTime, LocalTime endTime) {
        // Tramo de mañana
        boolean inMorning = !startTime.isBefore(LocalTime.of(10, 0)) &&
                !endTime.isAfter(LocalTime.of(16, 0));

        // Tramo de tarde
        boolean inAfternoon = !startTime.isBefore(LocalTime.of(17, 0)) &&
                !endTime.isAfter(LocalTime.of(21, 0));

        return inMorning || inAfternoon;
    }
}
