package mjkarbasian.moshtarimadar.helper;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mjkarbasian.moshtarimadar.R;

import static mjkarbasian.moshtarimadar.helper.Samples.salesCode;
import static mjkarbasian.moshtarimadar.helper.Samples.setSale;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleDueDate;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleFinalAmount;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleOffTaxList;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalePaymentList;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleProductList;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleSummary;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalesAmount;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalesCode;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalesCustomer;


/**
 * Created by family on 6/24/2016.
 */
public class Utility {
    public static String getTheLastPathUri(Uri uri) {
        String[] segments = uri.getPath().split("/");
        String pathStr = segments[segments.length - 1];
        return pathStr;
    }

    public static String formatPurchase(Context context, String purcahseAmount) {

        return context.getString(R.string.format_purchase_amount, purcahseAmount);
    }

    public static String DecimalSeperation(Context context, double number) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(true);
        String decNumber = (String) format.format(number);
        return decNumber;
    }

    public static int dipConverter(int dip, Context context) {

        int dpValue = dip; // margin in dips
        float d = context.getResources().getDisplayMetrics().density;
        int marginDip = (int) (dpValue * d); // margin in pixels

        return marginDip;
    }

    public static String currentDatePicker() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
        String formattedDate = df.format(calendar.getTime());
        return formattedDate;
    }

    public static String JalaliDatePicker(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        int year = 0;
        int month = 0;
        int day = 0;
        try {
            Date dateDate = df.parse(date);
            year = dateDate.getYear() + 1900;
            month = dateDate.getMonth();
            day = dateDate.getDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String jalaliDate = JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(year, month, day)).toString();

        return jalaliDate;
    }

    public static String getLocale(Context context) {

        Locale current = context.getResources().getConfiguration().locale;
        return current.getCountry();
    }

    public static String doubleFormatter(double myNumber) {
        DecimalFormat f = (DecimalFormat) DecimalFormat.getInstance();
        f.setDecimalSeparatorAlwaysShown(false);
        f.setGroupingUsed(false);
        String myString = f.format(myNumber);
        return myString;
    }

    public static String contextDateFormatter(String date, Context context) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        String formatedDate = dateFormat.format(date);
        return formatedDate;
    }

    public static String formatDate(Calendar calendar) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(calendar.getTime());
        return formattedDate;
    }

    public static boolean isToday(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date day = df.parse(date);
        Calendar checkDay = Calendar.getInstance();
        Calendar thisDay = Calendar.getInstance();
        checkDay.setTime(day);

        int checkDayYear = checkDay.get(Calendar.YEAR);
        int checkDayMonth = checkDay.get(Calendar.MONTH);
        int checkDayDayNum = checkDay.get(Calendar.DAY_OF_MONTH);

        int thisDayNum = thisDay.get(Calendar.DAY_OF_MONTH);
        int thisDayMonth = thisDay.get(Calendar.MONTH);
        int thisDayYear = thisDay.get(Calendar.YEAR);

        return (checkDayYear == thisDayYear && checkDayMonth == thisDayMonth && checkDayDayNum == thisDayNum);
    }

    public static boolean isThisWeek(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date day = df.parse(date);
        Calendar checkDay = Calendar.getInstance();
        Calendar thisDay = Calendar.getInstance();
        checkDay.setTime(day);

        int checkDayWeek = checkDay.get(Calendar.WEEK_OF_YEAR);
        int thisDayWeek = thisDay.get(Calendar.WEEK_OF_YEAR);

        return (checkDayWeek == thisDayWeek);
    }

    public static boolean isThisMonth(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date day = df.parse(date);
        Calendar checkDay = Calendar.getInstance();
        Calendar thisDay = Calendar.getInstance();
        checkDay.setTime(day);

        int checkDayMonth = checkDay.get(Calendar.MONTH);
        int thisDayMonth = thisDay.get(Calendar.MONTH);

        return (checkDayMonth == thisDayMonth);
    }

    public static void setCustomerState(ImageView customerState, int stateVar) {
        switch (stateVar) {
            case 1:
                customerState.setColorFilter(Color.argb(255, 255, 223, 0));
                break;
            case 2:
                customerState.setColorFilter(Color.argb(255, 169, 169, 169));
                break;
            case 3:
                customerState.setColorFilter(Color.argb(255, 207, 125, 50));
                break;
            default:
                customerState.setColorFilter(Color.argb(255, 0, 0, 0));

        }
    }

    public static String changeDate(String date, int option, int amount) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date day = df.parse(date);
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(day);
        String changedDate;
        switch (option) {
            case Calendar.DATE:
                currentDate.roll(Calendar.DAY_OF_MONTH, amount);
                break;
            case Calendar.MONTH:
                currentDate.roll(Calendar.MONTH, amount);
                break;
            case Calendar.YEAR:
                currentDate.roll(Calendar.YEAR, amount);
                break;
            default:

        }

        changedDate = Utility.formatDate(currentDate);
        return changedDate;

    }

    public static void initializer(Context context) {

        if (Samples.productCode.size() == 0) {
            Samples.setProductCode();
            Samples.setProductName(context);
            Samples.setProductDate();
            Samples.setProductPrice();
            Samples.setProductPicture(context);
            Samples.setProducts();
            Samples.setProductsPriceList();

        }

        if (salesCode.size() == 0) {

            setSalesCode();
            setSaleDueDate();
            setSalesCustomer(context);
            setSalesAmount();
            setSaleProductList();
            try {
                setSalePaymentList(context);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setSaleOffTaxList(context);
            setSaleFinalAmount(context);
            setSale();
            setSaleSummary();
        }


    }

    public static void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }


}
