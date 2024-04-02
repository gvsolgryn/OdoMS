package database;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatabaseBackup {
    public static String dbPath;
    public static String dbUser;
    public static String dbPass;
    public static String dbName;
    public static String encoding;
    public static int saveTime;
    public static int removeTime;

    public static String os = System.getProperty("os.name").toLowerCase();

    public static class BackupTask extends TimerTask {
        @Override
        public void run() {
            DatabaseBackup.newSave();
            DatabaseBackup.newDelete();
        }
    }

    public static void main(String[] args) {
        System.out.println("[1q2w3e4r! 해병] 뽀로삐뽑 뽀로삐뽑 악! 현재 운행중인 오도봉고는 " + os + "입니다!");

        try {
            Properties props = new Properties();
            try (FileReader fileReader = new FileReader("Properties/database.properties")) {
                props.load(fileReader);
                if (os.contains("win")) {
                    dbPath = props.getProperty("query.wPath");
                }
                else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                    dbPath = props.getProperty("query.lPath");
                }
                else if (os.contains("mac")) {
                    dbPath = props.getProperty("query.mPath");
                }
                else {
                    dbPath = props.getProperty("query.path");
                }
                dbUser = props.getProperty("query.user");
                dbPass = props.getProperty("query.password");
                dbName = props.getProperty("query.schema");
                encoding = props.getProperty("encoding");
                saveTime = Integer.parseInt(props.getProperty("query.savetime"));
                removeTime = Integer.parseInt(props.getProperty("query.removetime"));
            }

            Timer timer = new Timer();
            timer.schedule(new BackupTask(), 1000L, saveTime);
        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println("[DB Backup Err] 따흐앙! Properties 해병 해석 중 문제 발생!\r\n" + e.getMessage());
        }
    }

    public static void newSave() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    dbPath,
                    "--user=" + dbUser,
                    "-p" + dbPass,
                    "--default-character-set=" + encoding,
                    "--lock-all-tables",
                    "--opt",
                    dbName
            );

            pb.redirectErrorStream(true);

            Process p = pb.start();

            String date = (new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초")).format(new Date());
            File backupFile = new File("sql/backup_" + date + ".sql");
            File backupDirectory = backupFile.getParentFile();
            FileWriter fw = new FileWriter(backupFile);

            if (!backupDirectory.exists()) {
                if (backupDirectory.mkdirs()) {
                    System.out.println("[1q2w3e4r! 해병] 뽀로삐뽑 뽀로삐뽑 악! 주계장 생성 완료!");
                } else {
                    System.out.println("[mkdir Err] 따흐앙! 주계장 생성에 실패하였다!!! 역돌격 실시!!!!");

                    return;
                }
            }

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

    public static void newDelete() {
        final Calendar calendar = Calendar.getInstance();
        long todayMillis = calendar.getTimeInMillis();
        Calendar fileCalendar = Calendar.getInstance();
        Date fileDate;

        File path = new File("sql/");
        File[] list = path.listFiles();

        if (list != null) {
            for (File file : list) {
                fileDate = new Date(file.lastModified());
                fileCalendar.setTime(fileDate);

                long diffMillis = todayMillis - fileCalendar.getTimeInMillis();

                if (diffMillis > removeTime && file.exists()) {
                    boolean checkDeleteFile = file.delete();

                    if (checkDeleteFile) {
                        System.out.println("[1q2w3e4r! 해병] 악! " + file.getName() + " 파일을 삭제하였습니다.");
                    }
                    else {
                        System.err.println("[DB Delete Err] 따흐앙! 비열한 DB새끼들의 습격이다!! 전군 역돌격 실시!!!!!");
                    }
                }
            }
        }
    }
}
