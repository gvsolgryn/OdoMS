package database;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tontontaki
 */

public class DatabaseBackup {
    public static String dbpath;
    public static String dbuser;
    public static String dbpass;
    public static String dbname;
    public static String encoding;
    public static int savetime;
    public static int removetime;

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();

        System.out.println("[1q2w3e4r! 해병] 뽀로삐뽑 뽀로삐뽑 악! 현재 운행중인 오도봉고는 " + os + "입니다!");

        try {
            Properties props = new Properties();
            FileReader fr = null;
            fr = new FileReader("Properties/database.properties");
            props.load(fr);
            if (os.contains("win")) {
                dbpath = props.getProperty("query.wPath");
            }
            else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                dbpath = props.getProperty("query.lPath");
            }
            else if (os.contains("mac")) {
                dbpath = props.getProperty("query.mPath");
            }
            else {
                dbpath = props.getProperty("query.path");
            }
            dbuser = props.getProperty("query.user");
            dbpass = props.getProperty("query.password");
            dbname = props.getProperty("query.schema");
            encoding = props.getProperty("encoding");
            savetime = Integer.parseInt(props.getProperty("query.savetime"));
            removetime = Integer.parseInt(props.getProperty("query.removetime"));
            Timer timer = new Timer();
            timer.schedule(new TimerTask()
                           {
                               public void run() {
                                   DatabaseBackup.newSave();
                                   DatabaseBackup.delete();
                               }
                           },
                    1000L, savetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void newSave() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    dbpath,
                    "--user=" + dbuser,
                    "-p" + dbpass,
                    "--default-character-set=" + encoding,
                    "--lock-all-tables",
                    "--opt",
                    dbname
            );

            pb.redirectErrorStream(true);

            Process p = pb.start();

            String date = (new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초")).format(new Date());
            File backupFile = new File("sql/backup_" + date + ".sql");
            FileWriter fw = new FileWriter(backupFile);

            InputStream inputStream = p.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                fw.write(line + "\n");
            }

            fw.close();
            reader.close();
            inputStreamReader.close();
            inputStream.close();

            int exitCode = p.waitFor();
            if (exitCode == 0) {
                System.out.println("[1q2w3e4r! 해병] 악!" + date + " 데이터베이스 저장이 완료되었습니다!!!");
            } else {
                System.err.println("[DB Backup Err] 따흐앙! 백업 프로세스가 오류로 인해 종료되었습니다!!! Exit code: " + exitCode);
            }
        }
        catch (IOException | InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void delete() {
        final Calendar cal = Calendar.getInstance();
        long todayMil = cal.getTimeInMillis();
        Calendar fileCal = Calendar.getInstance();
        Date fileDate = null;

        File path = new File("sql/");
        File[] list = path.listFiles();
        for (File file : list) {
            fileDate = new Date(file.lastModified());
            fileCal.setTime(fileDate);
            long diffMil = todayMil - fileCal.getTimeInMillis();
            if (diffMil > removetime && file.exists()) {
                System.out.println("[1q2w3e4r! 해병] 악! " + file.getName() + " 파일을 삭제하였습니다.");
                boolean delete = file.delete();
            }
        }
    }
}
