package com.blog.blog.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TimeUtils {
    public final static String GREENWICH_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public final static String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String YEAR_MONTH = "yyyy-MM";
    public final static Map<Integer, String> DAY_OF_WEEK_MAP = new HashMap<Integer, String>() {
        {
            put(1, "一");
            put(2, "二");
            put(3, "三");
            put(4, "四");
            put(5, "五");
            put(6, "六");
            put(7, "日");
        }
    };


    public static LocalDateTime stringToLocalDateTime(String timeString) {
        return stringToLocalDateTime(timeString, YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN);
    }


    public static LocalDateTime stringToLocalDateTime(String timeString, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(timeString, df);
    }

    public static String localDateToString(LocalDate localDate, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(fmt);
    }

    public static String localDateToString(LocalDate localDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY);
        return localDate.format(fmt);
    }

    public static String localDateTimeToYearMonthString(LocalDate localDate) {
        return localDateToString(localDate, YEAR_MONTH);
    }

    public static String getDayOfWeekChineseString(LocalDate localDate) {
        return DAY_OF_WEEK_MAP.get(localDate.getDayOfWeek().getValue());
    }

    public static String stringToGreenwichString(String timeString) {
        DateFormat df1 = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN);
        try {
            Date date = df1.parse(timeString);
            DateFormat df2 = new SimpleDateFormat(GREENWICH_PATTERN);
            return df2.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static LocalDate getFirstDayOfMonthFromYearMonth(String yearMonth) {
        yearMonth = yearMonth + "-01";
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY);
        return LocalDate.parse(yearMonth, df);
    }

    public static Boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue() == 6 || dayOfWeek.getValue() == 7;
    }

    // 自定义上下午的概念
    public static Boolean isMorning(LocalTime time) {
        return time.isBefore(LocalTime.of(12, 30, 0));
    }

    //    public static LocalDate
    public static void createLocalGroup(String groupName) {

        String regex = "^([$][0-9]+[,]*[0-9]*)(.[0-9]{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(groupName);
        System.out.println(matcher.matches());
    }

    public static String getTimeStrBetween(LocalDateTime from, LocalDateTime to) {
        Long minutes = Duration.between(from, to).toMinutes();
        if (minutes < 60) {
            return "约" + minutes + "分钟前";
        }
        Long hours = Duration.between(from, to).toHours();
        if (hours < 24) {
            return "约" + hours + "小时前";
        }
        long days = Duration.between(from, to).toDays();
        if (days < 30) {
            return "约" + days + "天前";
        }
        long months = days / 30;
        if (months < 12) {
            return "约" + months + "个月前";
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY);
            return dateTimeFormatter.format(from);
        }
    }

    public static List<LocalDate> getBetweenDate(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(list::add);

        return list;
    }

    public static Map<String, String> getDayOfMonthAndDayOfWeekBetweenDate(LocalDate startDate, LocalDate endDate) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (LocalDate date : getBetweenDate(startDate, endDate)) {
            int day = date.getDayOfMonth();
            String dayStr = day + "";
            if (day < 10) {
                dayStr = "0" + day;
            }
            String dayOfWeekChineseString = getDayOfWeekChineseString(date);
            result.put(dayStr, dayOfWeekChineseString);
        }
        return result;
    }

    public static LocalDateTime millisecondToLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static String localTimeToString(LocalDateTime time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN);
        return time.format(fmt);
    }


    public static String localTimeToMooningString(LocalDateTime time) {
        LocalDate localDate = time.toLocalDate();
        String string = TimeUtils.localDateToString(localDate);
        Boolean morning = isMorning(time.toLocalTime());
        if (morning) {
            string = string + "上午";
        } else {
            string = string + "下午";
        }
        return string;
    }

    /**
     * 根据时间判断今天属于服务设置中的上半年或者是下半年
     *
     * @param springFestival 当前年份的春节。
     * @return 结果 1 属于去年的下半年   2 今天的上半年  3 今天的下半年
     */
    public static int getHalfYearType(LocalDate springFestival) {
        LocalDate now = LocalDate.now();
        LocalDate aug = LocalDate.of(springFestival.getYear(), 8, 1);
        // 如果今天早于今年的春节， 属于去年的下半年
        if (now.isBefore(springFestival)) {
            return 3;
        }
        // 如果今天在春节至7月31之间。属于上半年
        if (now.isAfter(springFestival.minusDays(1)) && now.isBefore(aug)) {
            return 1;
        }
        // 如果今天在8月1以及8月1之后。属于下半年
        if (now.isAfter(aug.minusDays(1))) {
            return 2;
        }
        return 0;
    }

    public static List<String> generateMonthList(Integer year) {
        year = year != null ? year : LocalDate.now().getYear();
        LocalDate from = LocalDate.of(year, 1, 1);

        LocalDate to = LocalDate.of(year, 12, 1);

        ArrayList<String> list = new ArrayList<>();
        while (!from.isAfter(to)) {
            String s = localDateToString(from, "YYYY-MM");
            list.add(s);
            from = from.plusMonths(1);
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {
        List<String> strings = generateMonthList(2020);
        System.out.println(strings);
    }
}
