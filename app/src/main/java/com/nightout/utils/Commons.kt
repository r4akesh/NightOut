package com.nightout.utils

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Auto-generated Javadoc
/**
 * The Class Commons is a utility class which provide the basic functions
 * used in project.
 */
object Commons {
    /**
     * Checks if is empty.
     *
     * @param str the str
     * @return true, if is empty
     */
    fun isEmpty(str: String?): Boolean {
        return (str == null || str.trim { it <= ' ' }.equals("null", ignoreCase = true)
                || str.trim { it <= ' ' }.length == 0)
    }

    /**
     * Gets the date time.
     *
     * @return the date time
     */
    val dateTime2: String
        get() {
            val d = Date(Calendar.getInstance(Locale.US).timeInMillis)
            val format = "dd-MMM-yyyyy"
            return SimpleDateFormat(format).format(d)
        }
    val crntTimeAmPm: String
        get() {
            val dt = Date(System.currentTimeMillis())
            val sdf = SimpleDateFormat("hh:mm aa", Locale.US)
            return sdf.format(dt)
        }

    fun getCrntTimeAmPm2(mills: Long): String {
        val dt = Date(mills)
        val sdf = SimpleDateFormat("hh:mm aa", Locale.US)
        return sdf.format(dt)
    }

    fun getTimeAmPm(mills: Long): String {
        val dt = Date(mills)
        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        return sdf.format(dt)
    }

    val dateTimeCrntUTC: String
        get() {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            val time = cal.timeInMillis
            return "" + time
        }
    val dateTime: String
        get() {
            val d = Date(Calendar.getInstance(Locale.US).timeInMillis)
            val format = "yyyy-MM-dd"
            return SimpleDateFormat(format).format(d)
        }

    fun millsToDateStr6(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(Date(dateInMillis))
    }

    fun getCrntDateTime(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("MMM-dd-yyyy hh:mm aa")
        return formatter.format(Date(dateInMillis))
    }

    /*  public static String millsToDateEU(long dateInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(new Date(dateInMillis));
        return dateString;
    }*/
    fun millsToDateEULong(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
        return formatter.format(Date(dateInMillis))
    }

    fun millsToDateUS(dateInMillis: Long): String {
        if (dateInMillis == 0L) return ""
        val formatter = SimpleDateFormat("MM-dd-yyyy")
        return formatter.format(Date(dateInMillis))
    }

    fun millsToDateUSLong(dateInMillis: Long): String { // 24 hrs
        val formatter = SimpleDateFormat("MM-dd-yyyy HH:mm")
        return formatter.format(Date(dateInMillis))
    }

    fun millsToDateStr3(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("MM-dd-yyyy hh:mm aa")
        return formatter.format(Date(dateInMillis))
    }

    fun millsToDateStr5(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("hh:mm aa")
        return formatter.format(Date(dateInMillis))
    }

    fun millsToDateStr2(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(Date(dateInMillis))
    }

    fun millsToDate(mills: Long): String {
        val cal = Calendar.getInstance(Locale.US)
        cal.timeInMillis = mills
        val format = "MMM dd, yyyy"
        return SimpleDateFormat(format).format(cal.time)
    }
    fun millsToDateFormat(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd MMM yyyy")
        return formatter.format(Date(dateInMillis))
    }

    /**
     * String to calander.
     *
     * @param date the date
     * @param cal  the cal
     */
    fun stringToCalander(date: String, cal: Calendar) {
        val str = date.split("-".toRegex()).toTypedArray()
        if (str.size == 3) cal[str[0].toInt(), str[1].toInt() - 1] = str[2].toInt()
    }

    /**
     * Date time to millis.
     *
     * @param datetime the datetime
     * @return the long
     */
    fun dateTimeToMillis(datetime: String): Long {
        val cal = Calendar.getInstance()
        try {
            val arr = datetime.split(" ".toRegex()).toTypedArray()
            var str = arr[0].split("-".toRegex()).toTypedArray()
            if (str.size == 3) cal[str[0].toInt(), str[1].toInt() - 1] = str[2].toInt()
            if (arr[1].contains(":")) {
                str = arr[1].split(":".toRegex()).toTypedArray()
                if (str.size == 3) {
                    cal[Calendar.HOUR_OF_DAY] = str[0].toInt()
                    cal[Calendar.MINUTE] = str[1].toInt()
                    cal[Calendar.SECOND] = str[2].toInt()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal.timeInMillis
    }

    fun strToTimemills(dt: String?): Long {
        val sdf = SimpleDateFormat("dd-MM-yyyyy", Locale.US)
        var date: Date? = null
        try {
            date = sdf.parse(dt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance(Locale.US)
        calendar.time = date
        println("Calender - Time in milliseconds : " + calendar.timeInMillis)
        return calendar.timeInMillis
    }

    fun strToTimemills2(dt: String?): Long {
        val sdf = SimpleDateFormat("MMM-dd-yyyy", Locale.US)
        var date: Date? = null
        val calendar = Calendar.getInstance()
        try {
            date = sdf.parse(dt)
            calendar.time = date
            return calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    fun strToTimemills3(dt: String?): Long {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        var date: Date? = null
        val calendar = Calendar.getInstance()
        try {
            date = sdf.parse(dt)
            calendar.time = date
            return calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    fun dateTimeToMillis1(datetime: String): Long {
        val cal = Calendar.getInstance()
        try {
            val arr = datetime.split(" ".toRegex()).toTypedArray()
            var str = arr[0].split("-".toRegex()).toTypedArray()
            if (str.size == 3) cal[str[2].toInt(), str[0].toInt() - 1] = str[1].toInt()
            str = arr[1].split(":".toRegex()).toTypedArray()
            if (str.size == 3) {
                cal[Calendar.HOUR_OF_DAY] = str[0].toInt()
                cal[Calendar.MINUTE] = str[1].toInt()
                cal[Calendar.SECOND] = str[2].toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal.timeInMillis
    }

    fun datetoFormat(mytime: String?): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var myDate: Date? = null
        try {
            myDate = dateFormat.parse(mytime) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val timeFormat = SimpleDateFormat("dd MMM yyyy")
        val finalDate = timeFormat.format(myDate)
        println(finalDate)
        return finalDate
    }

    fun millsToDateTime(mills: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = mills
        val d = cal[Calendar.DAY_OF_MONTH]
        val m = cal[Calendar.MONTH] + 1
        val y = cal[Calendar.YEAR]
        return (y.toString() + "-" + (if (m < 10) "0$m" else m) + "-"
                + if (d < 10) "0$d" else d)
    }

    fun extractDate(date: String): String {
        if (!isEmpty(date) && date.contains("(") && date.contains(")")) {
            val millis = date.substring(date.indexOf("(") + 1, date.indexOf(")"))
            Log.d("millis", millis)
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis.toLong()
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            return sdf.format(cal.time)
        }
        return "" + date
    }

    /**
     * Str to double.
     *
     * @param str the str
     * @return the double
     */
    fun strToDouble(str: String): Double {
        try {
            return str.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0.0
    }

    /**
     * Str to int.
     *
     * @param str the str
     * @return the int
     */
    fun strToInt(str: String): Int {
        try {
            return strToDouble(str).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * Str to long.
     *
     * @param str the str
     * @return the long
     */
    fun strToLong(str: String): Long {
        return strToDouble(str).toLong()
    }

    /**
     * Checks if is before today.
     *
     * @param date the date
     * @return true, if is before today
     */
    fun isBeforeToday(date: String): Boolean {
        val today = Calendar.getInstance()
        val cal = Calendar.getInstance()
        stringToCalander(date, cal)
        return cal.compareTo(today) == -1
    }

    /**
     * Checks if is between today.
     *
     * @param start the start
     * @param end   the end
     * @return true, if is between today
     */
    fun isBetweenToday(start: String, end: String): Boolean {
        return try {
            val today = Calendar.getInstance()
            val calStart = Calendar.getInstance()
            stringToCalander(start, calStart)
            val calEnd = Calendar.getInstance()
            stringToCalander(end, calEnd)
            (calStart.compareTo(today) <= 0
                    && calEnd.compareTo(today) >= 0)
        } catch (e: Exception) {
            false
        }
    }

    fun jsonDateToTimeMillis(str: String): Long {
        var str = str
        try {
            str = str.substring(str.indexOf("(") + 1, str.indexOf(")"))
            val cal = Calendar.getInstance()
            if (str.contains("+")) {
                val i = str.indexOf("+")
                cal.timeInMillis = str.substring(0, i).toLong()
                cal.timeZone = TimeZone.getTimeZone(
                    "GMT+"
                            + str.substring(i + 1)
                )
            } else if (str.contains("-")) {
                val i = str.indexOf("-")
                cal.timeInMillis = str.substring(0, i).toLong()
                cal.timeZone = TimeZone.getTimeZone(
                    "GMT-"
                            + str.substring(i + 1)
                )
            } else cal.timeInMillis = str.toLong()
            return cal.timeInMillis
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun timeMillisToJsonDate(time: Long, exlcudeTimezone: Boolean): String {
        if (exlcudeTimezone) return "/Date($time)/"
        var z = (Calendar.getInstance().timeZone.rawOffset
                / (60 * 1000))
        val neg = z < 0
        if (neg) z = z * -1
        val h = z / 60
        val m = z - h * 60
        val tz = String.format(
            Locale.US, "%s%02d%02d", if (neg) "-" else "+", h,
            m
        )
        return "/Date($time$tz)/"
    }
}