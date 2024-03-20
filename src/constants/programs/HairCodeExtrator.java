package constants.programs;

import java.io.File;
import java.util.Calendar;

import tools.FileoutputUtil;

public class HairCodeExtrator {

    private static Calendar cal = Calendar.getInstance();

    public static void main(String args[]) {
        File source = new File("wz/Character.wz/Hair");
        long now = cal.getTimeInMillis();
        for (File f : source.listFiles()) {
            int v1 = Integer.parseInt(f.getName().replace(".img.xml", "").substring(3, 8));
            int c1 = v1 / 1000;
            if (c1 == 40 || c1 == 43) {
                FileoutputUtil.logToFile_("남자_" + now + ".txt", v1 + ",");
            } else if (c1 == 41 || c1 == 44) {
                FileoutputUtil.logToFile_("여자_" + now + ".txt", v1 + ",");
            }
        }
    }
}
