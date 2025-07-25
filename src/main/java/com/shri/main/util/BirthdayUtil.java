package com.shri.main.util;

import com.shri.main.model.Student;
import java.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BirthdayUtil {

    private static final Logger logger = LoggerFactory.getLogger(BirthdayUtil.class);

    public static boolean shouldSendBirthdayWish(Student student) {
        if (student.getAdmissionDate() == null || student.getBirthDate() == null) {
            logger.warn("Skipping student '{}' due to null admission or birth date.", student.getName());
            return false;
        }

        LocalDate admissionDate = convertToLocalDate(student.getAdmissionDate());
        LocalDate birthDate = convertToLocalDate(student.getBirthDate());
        LocalDate today = LocalDate.now();

        MonthDay todayMonthDay = MonthDay.from(today);
        MonthDay birthdayMonthDay = MonthDay.from(birthDate);

        if (!todayMonthDay.equals(birthdayMonthDay)) return false;

        LocalDate birthdayThisYear = birthDate.withYear(admissionDate.getYear());
        LocalDate sixMonthsLater = admissionDate.plusMonths(6);

        return !birthdayThisYear.isBefore(admissionDate) && !birthdayThisYear.isAfter(sixMonthsLater);
    }

    private static LocalDate convertToLocalDate(java.util.Date date) {
        if (date instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
