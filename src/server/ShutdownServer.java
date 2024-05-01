package server;

import java.sql.SQLException;

import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import server.Timer.*;
import tools.MaplePacketCreator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.shops.HiredMerchantSave;
import server.shops.MinervaOwlSearchTop;

public class ShutdownServer implements ShutdownServerMBean {

    public static ShutdownServer instance;

    public static void registerMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            instance = new ShutdownServer();
            mBeanServer.registerMBean(instance, new ObjectName("server:type=ShutdownServer"));
        } catch (Exception e) {
            System.out.println("Error registering Shutdown MBean");
            e.printStackTrace();
        }
    }

    public static ShutdownServer getInstance() {
        return instance;
    }
    public int mode = 0;

    public void shutdown() {//can execute twice
        run();
    }
    AtomicInteger FinishedThreads = new AtomicInteger(0);

    public void incrementT() {
        FinishedThreads.incrementAndGet();
    }

    @Override
    public void run() {
        if (mode == 0) {
            int ret = 0;
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, "서버가 종료됩니다."));
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.setShutdown();
                cs.setServerMessage("잠시 후 서버가 종료됩니다.");
                ret += cs.closeAllMerchant();
            }
            HiredMerchantSave.Execute(this);
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShutdownServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            while (FinishedThreads.get() != HiredMerchantSave.NumSavingThreads) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ShutdownServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            World.AdminShopItemRequest.saveDB();
            World.Guild.save();
            World.Alliance.save();
            World.Family.save();
            MedalRanking.saveAll();
            MinervaOwlSearchTop.getInstance().saveToFile();//1126
            CashItemSaleRank.cleanUp();//1126
            System.out.println("Shutdown 1 has completed. Hired merchants saved: " + ret);
            mode++;
        } else if (mode == 1) {
            mode++;
            System.out.println("Shutdown 2 commencing...");
            try {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, "서버가 종료됩니다."));
                Integer[] chs = ChannelServer.getAllInstance().toArray(new Integer[0]);

                for (int i : chs) {
                    try {
                        ChannelServer cs = ChannelServer.getInstance(i);
                        synchronized (this) {
                            cs.shutdown();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LoginServer.shutdown();
                CashShopServer.shutdown();
                CloneTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 20000L);
                synchronized (this) {
                    try {
                        wait(10 * 1000);
                    } catch (Exception e) {
                        //shutdown
                    }
                }
                DatabaseConnection.shutdown();
            } catch (Exception e) {
                System.err.println("THROW" + e);
            }
            WorldTimer.getInstance().stop();
            MapTimer.getInstance().stop();
            BuffTimer.getInstance().stop();
            EventTimer.getInstance().stop();
            EtcTimer.getInstance().stop();
            PingTimer.getInstance().stop();
            System.out.println("Shutdown 2 has finished. Server will be shutdown in 5 secs.");
        }
    }
}
