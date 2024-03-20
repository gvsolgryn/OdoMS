package tools; //메모리 정리

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import launch.ChannelServer;

public class MemoryUsageWatcher extends Thread {

    private long minRebootUsage;
    private static int maxOverflowCount = 200;
    private int overflowedCount = 0;
    private MemoryMXBean mmb = java.lang.management.ManagementFactory.getMemoryMXBean();
    private boolean a = false;

    public MemoryUsageWatcher(double rebootPercent) {
        super("MemoryUsageWatcher");
        MemoryUsage mem = mmb.getHeapMemoryUsage();
        minRebootUsage = (long) (mem.getMax() * rebootPercent);
    }

    @Override
    public void run() {
        boolean overflow = false;
        while (!overflow) {
            try {
                MemoryUsage mem = mmb.getHeapMemoryUsage();
                System.out.println("Current Memory Usage : " + mem.getUsed() / 1024 + "K / Max : " + minRebootUsage / 1024 + "K / Connection : " + ChannelServer.getOnlineConnections());
                Thread.sleep(10000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new MemoryUsageWatcher(5).start();
    }
}