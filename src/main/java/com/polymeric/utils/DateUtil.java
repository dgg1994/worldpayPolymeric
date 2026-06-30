package com.polymeric.utils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateUtil {

	/** 年-月-日 时:分:秒 显示格式 */
	public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** 年-月-日 显示格式 */
	public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";

	private static SimpleDateFormat simpleDateFormat;

	/**
	 * Date类型转为指定格式的String类型
	 * 
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static String DateToString(Date source, String pattern) {
		simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(source);
	}

	public static String DateToString(Date source) {
		simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
		return simpleDateFormat.format(source);
	}

	public static String DateTimeToString(Date source) {
		simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
		return simpleDateFormat.format(source);
	}

	public static Date StringToDateTime(String source) {
		simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date StringToDetailDateTime(String source) {
		simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 判读时间差距，两个时间相差多少天，时，分，秒
	 */
	public static Integer getDay(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Integer days = null;
		try {
			Date currentTime = dateFormat.parse(dateFormat.format(new Date()));// 现在系统当前时间
			Date pastTime = dateFormat.parse(date);// 过去时间
			long diff = currentTime.getTime() - pastTime.getTime();
			days = (int) (diff / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/*
	 * 判读时间差距，两个时间相差多少天，时，分，秒
	 */
	public static Integer getDayNum(String begdate, String endDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Integer days = null;
		try {
			Date currentTime = dateFormat.parse(endDate);// 现在系统当前时间
			Date pastTime = dateFormat.parse(begdate);// 过去时间
			long diff = currentTime.getTime() - pastTime.getTime();
			days = (int) (diff / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	public static Integer getDayNum(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Integer days = null;
		try {
			Date currentTime = dateFormat.parse(dateFormat.format(new Date()));// 现在系统当前时间
			Date pastTime = dateFormat.parse(date);// 过去时间
			long diff = pastTime.getTime() - currentTime.getTime();
			days = (int) (diff / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	public static String addDate(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();// 获取一个Calendar对象
		try {
			cd.setTime(sdf.parse(date));// 设置calendar日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cd.add(Calendar.DATE, n);// 增加n天
//        cd.add(Calendar.YEAR,n);//增加n年
//        cd.add(Calendar.MONTH,n);//增加n个
		return sdf.format(cd.getTime());
	}
	
	public static Date addDays(Date date, int days) {
		if (date == null) {
			throw new IllegalArgumentException("date 不能为 null");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	public static String addMonthDate(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar cd = Calendar.getInstance();// 获取一个Calendar对象
		try {
			cd.setTime(sdf.parse(date));// 设置calendar日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
//        cd.add(Calendar.DATE,n);//增加n天
//        cd.add(Calendar.YEAR,n);//增加n年
		cd.add(Calendar.MONTH, n);// 增加n个
		return sdf.format(cd.getTime());
	}

	public static String addYear(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar cd = Calendar.getInstance();// 获取一个Calendar对象
		try {
			cd.setTime(sdf.parse(date));// 设置calendar日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
//        cd.add(Calendar.DATE,n);//增加n天
		cd.add(Calendar.YEAR, n);// 增加n年
//        cd.add(Calendar.MONTH,n);//增加n个

		return sdf.format(cd.getTime());
	}

	/**
	 * 判断该对象是否所有属性为空 返回ture表示所有属性为null，返回false表示不是所有属性都是null
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isAllFieldNull(Object object) {
		boolean flag = true;

		Class clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// 设置属性是可以访问的(私有的也可以)
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(object);
				// 只要有1个属性不为空,那么就不是所有的属性值都为空
				if (value != null) {
					flag = false;
					break;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return flag;
	}

	/**
	 * 获取两个日期之间的所有日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> dateRange = new ArrayList<>();
		LocalDate currentDate = startDate;

		while (!currentDate.isAfter(endDate)) {
			dateRange.add(currentDate);
			currentDate = currentDate.plus(1, ChronoUnit.DAYS);
		}

		return dateRange;
	}

	/**
	 * @category 获取两个日期之间的所有日期
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDateRangeAsString(String startDate, String endDate) {
		List<String> dateRange = new ArrayList<>();
		LocalDate currentDate = LocalDate.parse(startDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		while (!currentDate.isAfter(LocalDate.parse(endDate))) {
			String dateStr = currentDate.format(formatter);
			dateRange.add(dateStr);
			currentDate = currentDate.plusDays(1);
		}
		return dateRange;
	}

	/**
	 * @category 获取两个月份间所有的月份
	 * @param startDateStr 开始月份
	 * @param endDateStr   结束月份
	 * @return
	 */
	public static List<String> getMonthRange(String startDateStr, String endDateStr) {
		List<String> monthRange = new ArrayList<>();
		YearMonth startYearMonth = YearMonth.parse(startDateStr);
		YearMonth endYearMonth = YearMonth.parse(endDateStr);
		YearMonth currentYearMonth = startYearMonth;
		while (!currentYearMonth.isAfter(endYearMonth)) {
			monthRange.add(currentYearMonth.toString());
			currentYearMonth = currentYearMonth.plusMonths(1);
		}
		return monthRange;
	}

	/**
	 * @category 计算两个日期时间是否超过8小时
	 * @param begString 开始时间 2023-10-15 15:00:00
	 * @param endString 结束时间 2023-10-15 18:00:00
	 * @return
	 */
	public static boolean dayEqual8Hours(String begString, String endString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime time1 = LocalDateTime.parse(begString, formatter);
		LocalDateTime time2 = LocalDateTime.parse(endString, formatter);

		Duration duration = Duration.between(time1, time2);
		boolean isGreaterThanOrEqual8Hours = duration.toHours() >= 8;
		return isGreaterThanOrEqual8Hours;
	}

	/**
	 * @category 计算两个时间是否超过8小时
	 * @param startTimeString 15:00:00
	 * @param endTimeString   23:00:00
	 * @return
	 */
	public static boolean timeEqual8Hours(String startTimeString, String endTimeString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime startTime = LocalTime.parse(startTimeString, formatter);
		LocalTime endTime = LocalTime.parse(endTimeString, formatter);

		Duration duration = Duration.between(startTime, endTime);
		boolean isGreaterThanOrEqual8Hours = duration.toHours() >= 8;
		return isGreaterThanOrEqual8Hours;
	}

	public static String getLastMonth() {
		LocalDate currentDate = LocalDate.now();
		LocalDate lastMonth = currentDate.minusMonths(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		String formattedLastMonth = lastMonth.format(formatter);
		return formattedLastMonth;
	}

	/**
	 * @category 两个日期相差几天
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 */
	public static Integer getDayDisparityNum(Date dateOne, Date dateTwo) {
		// 将java.util.Date转换为java.time.LocalDate
		LocalDate localDate1 = dateOne.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localDate2 = dateTwo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		// 计算相差的天数，包含第一天
		Integer daysDifference = (int) (ChronoUnit.DAYS.between(localDate1, localDate2));
		return daysDifference;
	}

	/**
	 * @category 计算两个时间相距几个小时
	 * @return
	 */
	public static double getTimeDistance(String begTimeStr, String endTimeStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime startTime = LocalTime.parse(begTimeStr, formatter);
		LocalTime endTime = LocalTime.parse(endTimeStr, formatter);
		// 计算时间差
		Duration duration = Duration.between(startTime, endTime);
		// 获取小时差
		long hoursDifference = duration.toHours();
		// 获取分钟差
		long minutesDifference = duration.toMinutes() % 60;
		// 如果分钟差大于30，向上取整为0.5小时，否则不添加
		double decimalHoursDifference = hoursDifference + (minutesDifference >= 30 ? 0.5 : 0);
		return decimalHoursDifference;
	}

	/**
	 * @category 比较两个时间大小
	 * @return
	 */
	public static boolean compareTime(String begTime, String endTime) {
		// 定义时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		// 将时间字符串解析为LocalTime对象
		LocalTime startTime = LocalTime.parse(begTime, formatter);
		LocalTime stopTime = LocalTime.parse(endTime, formatter);
		// 比较两个时间
		if (startTime.isBefore(stopTime)) {
			return true;
		} else if (startTime.isAfter(stopTime)) {
			return false;
		} else {
			return false;
		}
	}

	// 检查时间是否在时间段内
	public static boolean isTimeWithinRange(String signBegTime, String signEndTime, String applyTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime signBeg = LocalTime.parse(signBegTime, formatter);
		LocalTime signEnd = LocalTime.parse(signEndTime, formatter);
		LocalTime apply = LocalTime.parse(applyTime, formatter);
		return !apply.isBefore(signBeg) && !apply.isAfter(signEnd);
	}

	/**
	 * @category 当前时间加减几个小时
	 * @param hourNum
	 * @return
	 */
	public static String addHour(Integer hourNum) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 获取当前时间
		LocalDateTime now = LocalDateTime.now();
		// 将当前时间往后推几个小时（例如，推后2小时）
		LocalDateTime later = now.plusHours(hourNum);
		return later.format(formatter);
	}

	public static String formatUtc(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		// 设置时区为 UTC
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		// 格式化日期
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	// 获取某一天的开始时间
	public static String getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return formatUtc(calendar.getTime());
	}

	// 获取某一天的结束时间
	public static String getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return formatUtc(calendar.getTime());
	}

	// 获取七天前的时间
	public static String getSevenDaysAgo(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -7); // 减去7天
		return formatUtc(calendar.getTime());
	}

	// 获取本周的开始时间
	public static String getStartOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return formatUtc(calendar.getTime());
	}

	// 获取本周的结束时间
	public static String getEndOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return formatUtc(calendar.getTime());
	}

	// 获取本月的开始时间
	public static String getStartOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return formatUtc(calendar.getTime());
	}

	// 获取某月的开始时间
	public static String getMonthStart(String month) {
		try {
			// 日期格式化工具
			SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
			// 解析输入的月份
			Date parsedDate = monthFormat.parse(month);
			// 设置为月份开始
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(parsedDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return formatUtc(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 获取本月的结束时间
	public static String getEndOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return formatUtc(calendar.getTime());
	}

	// 获取某月的开始时间
	public static String getMonthEnd(String month) {
		try {
			// 日期格式化工具
			SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
			// 解析输入的月份
			Date parsedDate = monthFormat.parse(month);
			// 设置为月份开始
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(parsedDate);

			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);

			return formatUtc(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 获取本年的开始时间
	public static String getStartOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return formatUtc(calendar.getTime());
	}

	// 获取本年的结束时间
	public static String getEndOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return formatUtc(calendar.getTime());
	}

	/**
	 * 当前天数+termDays的时间戳
	 * @param termDays 天数
	 * @return
	 */
	public static LocalDateTime addDaysFromNowWithTime(Integer termDays) {
		if (termDays == null) {
			return null;
		}
		// 获取当前时间
		LocalDateTime now = LocalDateTime.now();
		// 计算T+1 直接+1
		LocalDateTime valueDateTime = now.plusDays(1);
		// 添加产品期限
        return valueDateTime.plusDays(termDays);
	}

	/**
	 * 获取起息时间
	 * @param payTime 购买订单时间
	 * @return LocalDateTime
	 */
	public static String valueTime(Date payTime) {
		if (payTime == null) {
			return null;
		}
		// 转换时间
		LocalDateTime payLdt = LocalDateTime.ofInstant(payTime.toInstant(), ZoneId.systemDefault());
		// 计算 T+1
		LocalDateTime valueLdt = payLdt.plusDays(1);
		// 格式化输出 (返回 String 类型)
		return valueLdt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}


	/**
	 * 当前时间和起息日比较
	 * @param valueTime 起息日
	 * @return
	 */
	public static boolean compareToTime(String valueTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String getNow =  sdf.format(new Date());
		return valueTime.compareTo(getNow) > 0;
	}

	
	/**
	 * @category 前一天日期
	 * @return
	 */
	public static String getYesterday() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String temp = dateFormat.format(new Date());
		String temp2 = DateUtil.addDate(temp, -1);
		return temp2;
	}

	/**
	 * @category 当天日期
	 * @return
	 */
	public static String getNowDay() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String temp = dateFormat.format(new Date());
		return temp;
	}

	/*
	 * 判断时间差距，计算两个 LocalDateTime 之间相差的天数
	 */
	public static Integer getDay(LocalDateTime expireTime) {
		if (expireTime == null) {
			return null;
		}
		// 获取当前时间
		LocalDateTime now = LocalDateTime.now();
		// 使用 ChronoUnit 计算两个时间之间的完整天数差
		// 这会返回一个 long 类型的值
		long daysBetween = ChronoUnit.DAYS.between(now, expireTime);
		// 转换为 Integer 并返回
		// 如果是过去的时间，daysBetween 会是负数
		return (int) daysBetween;
	}
	
	public static Long parsePayTime(String payTimeStr) {
	    if (payTimeStr == null || payTimeStr.isEmpty()) {
	        return System.currentTimeMillis();
	    }
	    
	    // 支持多种格式
	    String[] patterns = {
	        "yyyy/MM/dd HH:mm:ss",
	        "yyyy-MM-dd HH:mm:ss",
	        "yyyy/MM/dd HH:mm",
	        "yyyy-MM-dd HH:mm"
	    };
	    
	    for (String pattern : patterns) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	            return sdf.parse(payTimeStr).getTime();
	        } catch (ParseException e) {
	            continue;
	        }
	    }
	    return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String temp = dateFormat.format(new Date());
		System.out.println(temp);*/
		System.out.println(DateTimeToString(new Date()));
	}
	
}
