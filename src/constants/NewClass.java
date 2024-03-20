package constants;

import tools.RandomStream.Randomizer;

public class NewClass {

    public static void main(String args[]) {
        String date = "2019-08-05 15:53:46";
        System.err.println(check(date));
        
    }
    
    public static boolean check(String date) {
        if (date.length() > 0) {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8, 10));
            int time = Integer.parseInt(date.substring(11, 13));
            if (year == 2019 && month <= 8)
                if (month == 8) {
                    if (day < 5)
                        return true;
                    else if (day == 5)
                        if (time <= 14)
                            return true;
                } else
                    return true;
        }
        return false;
    }
}
