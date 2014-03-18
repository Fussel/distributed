/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author kiefer
 */
public class DateUtils {

    public static String getTimeFormatAsString(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(d);
    }

    public static String getDateFormatAsString(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(d);
    }
}
