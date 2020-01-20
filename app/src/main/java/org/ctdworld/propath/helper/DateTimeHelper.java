package org.ctdworld.propath.helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHelper
{
    private static final String TAG = DateTimeHelper.class.getSimpleName();

    public static final String FORMAT_DATE_TIME = "dd-MMM-yy  hh:mm a";
    public static final String FORMAT_TIME = "hh:mm a";
    public static final String FORMAT_DATE = "dd-MMM-yy";



    /*public static String getDateTime(String strDate, String format)  // passed pattern should be "yyyy-MM-dd hh:mm:ss"
    {
        try
        {
            if (strDate == null || format == null)
                return "00-00-00 00:00 am";

            SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
            Date dateFormat = parseDate.parse(strDate);

            SimpleDateFormat formatDate = new SimpleDateFormat(format, Locale.getDefault());
            return formatDate.format(dateFormat);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }*/


    // its old method is method is mentioned above in comment
    public static String getDateTime(String strDate, String format)  // passed pattern should be "yyyy-MM-dd hh:mm:ss"
    {
        try
        {
            if (strDate == null || format == null)
                return "00-00-00 00:00 am";

            SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
            Date dateFormat = parseDate.parse(strDate);


            // below code is only for date time which comes from web server
            TimeZone zoneIndia = TimeZone.getTimeZone("Asia/Kolkata"); // getting Indian time zone, to remove Indian rawOffset from server date time because Indian time zone is set on server
            if (!Calendar.getInstance().getTimeZone().getDisplayName().equalsIgnoreCase(zoneIndia.getDisplayName()))
            {
                int offset = TimeZone.getDefault().getRawOffset()-zoneIndia.getRawOffset() /*+ TimeZone.getDefault().getDSTSavings()*/;
                long finalTime = dateFormat.getTime()+ offset;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(finalTime));
                dateFormat = new Date(calendar.getTime().getTime());
            }


            SimpleDateFormat formatDate = new SimpleDateFormat(format, Locale.getDefault());
            return formatDate.format(dateFormat);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }




    // returns long of date time
    public static long getLongDateTime(String dateTime)
    {
        Date date = getDateTimeObject(dateTime, FORMAT_DATE_TIME);
        if (date != null)
            return date.getTime();
        else
            return 0;
    }


    // dateFormat is format of the date which is to be parsed
    public static Date getDateTimeObject(String strDate, String currentFormat)
    {
        try
        {
            if (strDate == null)
                return null;

            SimpleDateFormat parseDate = new SimpleDateFormat(currentFormat,Locale.getDefault());

            return parseDate.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }


    // dateFormat is format of the date which is to be parsed
    public static Date getDateObject(String strDate, String dateFormat)
    {
        try
        {
            if (strDate == null)
                return null;

            SimpleDateFormat parseDate = new SimpleDateFormat(dateFormat,Locale.getDefault());

            return parseDate.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }


    public static String getDate(String strDate, String format)  // passed pattern should be "yyyy-MM-dd hh:mm:ss"
    {
        try
        {
            if (strDate == null || format == null)
                return "00-00-00";

            SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
            Date dateFormat = parseDate.parse(strDate);

            SimpleDateFormat formatDate = new SimpleDateFormat(format, Locale.getDefault());
            return formatDate.format(dateFormat);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }




    public static String getDateTime(Date date, String format)  // passed pattern should be "yyyy-MM-dd hh:mm:ss"
    {
        SimpleDateFormat formatDate = new SimpleDateFormat(format, Locale.getDefault());
        return formatDate.format(date);
    }



    public String getDateStringDayMonthYear(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        return  dateFormat.format(date);
    }


    public String getDateStringDayMonthYear(String strDate)
    {
        try
        {
            SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
            Date dateFormat = parseDate.parse(strDate);

            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return formatDate.format(dateFormat);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }





    public String getDateTime(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm a",Locale.getDefault());
        return  dateFormat.format(date);
    }


    public String getTime(String strDate)
    {
        try
        {
            if (strDate != null && !strDate.isEmpty())
            {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
                Date dateTime = parser.parse(strDate);

                if (dateTime != null)
                {
                    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("hh:mm a",Locale.getDefault());
                    return dateTimeFormatter.format(dateTime);
                }

            }
            else
                Log.e(TAG,"strDate is null or empty in getTime(String strDate) method ");
            return "";
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }


    public String getTime(Date date)
    {
        try
        {
            if (date != null)
            {
                    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("hh:mm a",Locale.getDefault());
                    return dateTimeFormatter.format(date);
            }
            else
                Log.e(TAG,"strDate is null or empty in getTime(String strDate) method ");
            return "";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }




    public Date getDateDayMonthYear(String strDateTime)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            return  dateFormat.parse(strDateTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

            return null;
    }

    public Date getDateDayMonthYear(Date date)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            String strDate = format.format(date);

            return  format.parse(strDate);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  null;
    }



    public String getMonthNameWithYear(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("MMMM - yyyy",Locale.getDefault());
        return format.format(date);
    }




    public static String getCurrentSystemDateTime()
    {
        Date presentTime_Date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME,Locale.getDefault());
        return dateFormat.format(presentTime_Date);
    }





}
