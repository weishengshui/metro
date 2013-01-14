package com.chinarewards.metro.sms;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Repository;

@Repository
public class TimeRangeChecker {

	public static boolean inRange(String timeRange, Date date) {
		String[] timeRanges = timeRange.split(",");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		for (String tr : timeRanges) {
			String[] times = tr.split("-");
			if (times.length == 2) {
				// check time range
				int[] t1 = parseTime(times[0]);
				c.set(Calendar.HOUR_OF_DAY, t1[0]);
				c.set(Calendar.MINUTE, t1[1]);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				Date from = c.getTime();

				int[] t2 = parseTime(times[1]);
				c.set(Calendar.HOUR_OF_DAY, t2[0]);
				c.set(Calendar.MINUTE, t2[1]);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 999);
				Date to = c.getTime();

				if (to.before(from)) {
					c.add(Calendar.DATE, 1);
					to = c.getTime();
				}

				// from <= date <= to
				if (!from.after(date) && !to.before(date)) {
					return true;
				}

			} else {
				// invalid time range
				throw new IllegalArgumentException("TimeRange Invalid: " + tr);
			}
		}
		return false;
	}

	public static int[] parseTime(String timeInHHMM) {
		String[] x = timeInHHMM.split(":");
		if (x.length != 2) {
			throw new IllegalArgumentException("Time Invalid: " + timeInHHMM);
		}
		int ret[] = new int[2];
		ret[0] = Integer.parseInt(x[0].trim());
		ret[1] = Integer.parseInt(x[1].trim());
		return ret;
	}
}
