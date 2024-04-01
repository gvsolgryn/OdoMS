package server;

import client.DreamBreakerRank;
import client.MapleCharacter;
import client.SkillFactory;
import client.inventory.MapleInventoryIdentifier;
import connector.ConnectorServer;
import constants.GameConstants;
import constants.ServerConstants;
import constants.programs.AdminTool;
import constants.programs.GarbageDataBaseRemover;
import database.DatabaseBackup;
import database.DatabaseConnection;
import handling.MapleSaveHandler;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.channel.handler.MatrixHandler;
import handling.channel.handler.UnionHandler;
import handling.farm.FarmServer;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import server.control.MapleEtcControl;
import server.control.MapleIndexTimer;
import server.events.MapleOxQuizFactory;
import server.field.boss.FieldSkillFactory;
import server.field.boss.lucid.Butterfly;
import server.field.boss.will.SpiderWeb;
import server.life.*;
import server.marriage.MarriageManager;
import server.quest.MapleQuest;
import server.quest.QuestCompleteStatus;
import tools.CMDCommand;
import tools.CommodityItemUpdate;
import tools.packet.BossRewardMeso;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Start {

    public static transient ScheduledFuture<?> boss;
    public static long startTime = System.currentTimeMillis();

    public static final Start instance = new Start();

    public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);

    public void run() throws InterruptedException {
        System.setProperty("nashorn.args", "--no-deprecation-warning"); //자바 14 이용시

        DatabaseConnection.init();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM auth_server_channel_ip");
                rs = ps.executeQuery();
                while (rs.next())
                    ServerProperties.setProperty(rs.getString("name") + rs.getInt("channelid"), rs.getString("value"));
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.exit(0);
            } finally {
                try {
                    if (con != null)
                        con.close();
                    if (ps != null)
                        ps.close();
                    if (rs != null)
                        rs.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            if (Boolean.parseBoolean(ServerProperties.getProperty("world.admin"))) {
                ServerConstants.Use_Fixed_IV = false;
                System.out.println("[!!! Admin Only Mode Active !!!]");
            }
            System.setProperty("wz", "wz");
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("UPDATE accounts SET loggedin = 0, allowed = 0");
                ps.executeUpdate();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.");
            } finally {
                try {
                    if (con != null)
                        con.close();
                    if (ps != null)
                        ps.close();
                    if (rs != null)
                        rs.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            World.init();
            Timer.WorldTimer.getInstance().start();
            Timer.EtcTimer.getInstance().start();
            Timer.MapTimer.getInstance().start();
            Timer.MobTimer.getInstance().start();
            Timer.CloneTimer.getInstance().start();
            Timer.EventTimer.getInstance().start();
            Timer.BuffTimer.getInstance().start();
            Timer.PingTimer.getInstance().start();
            Timer.ShowTimer.getInstance().start();

            Date date = new Date();
            //월요일 마다
            if (date.getDay() == 1 ) {
                GarbageDataBaseRemover.main();
            }

            ServerConstants.mirrors.add(new DimensionMirrorEntry("자유 전직", "", 200, 0, 0, "1541032", List.of(4310086)));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("닉네임변경", "", 10, 1, 1, "9062010", List.of(4034803)));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("계승 시스템", "", 300, 2, 2, "9062116", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("무릉도장", "", 10, 3, 3, "9900004", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("몬스터파크", "", 10, 4, 4, "9071003", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("티어시스템", "", 100, 5, 5, "2007", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("추천인시스템", "", 10, 6, 6, "3001931", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("제작 및 강화", "", 100, 7, 7, "2400003", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("안개 수련장", "", 200, 8, 8, "9062318", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("커플 컨텐츠", "", 10, 9, 9, "9201000", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("유니온 시스템", "", 109, 10, 10, "9010106", new ArrayList<>()));
            ServerConstants.mirrors.add(new DimensionMirrorEntry("룰렛 시스템", "", 109, 11, 11, "9000155", new ArrayList<>()));

            ServerConstants.quicks.add(new QuickMoveEntry(1, 2000, 0, 10, "주요 지역으로 캐릭터를 이동시켜 주는 #c<워프 시스템>#을 이용한다."));
            ServerConstants.WORLD_UI = ServerProperties.getProperty("login.serverUI");
            ServerConstants.ChangeMapUI = Boolean.parseBoolean(ServerProperties.getProperty("login.ChangeMapUI"));
            DreamBreakerRank.LoadRank();
            JamsuPoint();
            Butterfly.load();
            SpiderWeb.load();
            System.out.println("[1q2w3e4r! 해병] 뽀로삐뽑 뽀로삐뽑 악!!!\r\n1.2.373 포신항문오도기합해병특별자치스토리 서버 구동을 시작하겠습니다.");
            Setting.CashShopSetting();
            AllLoding allLoding = new AllLoding();
            allLoding.start();
            //System.out.println("[Loading LOGIN]");
            LoginServer.run_startup_configurations();
            //System.out.println("[Loading CHANNEL]");
            ChannelServer.startChannel_Main();
            //System.out.println("[Loading CASH SHOP]");
            CashShopServer.run_startup_configurations();
            //System.out.println("[Loading Farm]");
            FarmServer.run_startup_configurations();
            Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
            PlayerNPC.loadAll();
            LoginServer.setOn();
            Timer.WorldTimer.getInstance().register(new MapleEtcControl(), 1000);
            EliteMonsterGradeInfo.loadFromWZData();
            AffectedOtherSkillInfo.loadFromWZData();
            InnerAbillity.getInstance().load();
            Setting.setting();
            Setting.setting2();
            Setting.settingGoldApple();
            Setting.settingNeoPos();
            BossRewardMeso.Setting();
            Timer.WorldTimer.getInstance().register(new MapleIndexTimer(), 1000);
            Timer.WorldTimer.getInstance().register(new MapleSaveHandler(), 10000L);
            new AdminTool().setVisible(true);
        } catch (Exception ex2) {
            System.err.println("[Server.Start] 찐빠 : " + ex2.getMessage());
        }
    }

    private class AllLoding extends Thread {
        private AllLoding() {
        }

        public void run() {
            LoadingThread SkillLoader = new LoadingThread(() -> SkillFactory.load(), "SkillLoader", this);
            LoadingThread QuestLoader = new LoadingThread(() -> {
                MapleQuest.initQuests();
                MapleLifeFactory.loadQuestCounts();
            }, "QuestLoader", this);
            LoadingThread QuestCustomLoader = new LoadingThread(() -> {
                MapleLifeFactory.loadNpcScripts();
                QuestCompleteStatus.run();
            }, "QuestCustomLoader", this);
            LoadingThread ItemLoader = new LoadingThread(() -> {
                MapleInventoryIdentifier.getInstance();
                CashItemFactory.getInstance().initialize();
                MapleItemInformationProvider.getInstance().runEtc();
                MapleItemInformationProvider.getInstance().runItems();
                AuctionServer.run_startup_configurations();
            }, "ItemLoader", this);
            LoadingThread GuildRankingLoader = new LoadingThread(() -> MapleGuildRanking.getInstance().load(), "GuildRankingLoader", this);
            LoadingThread EtcLoader = new LoadingThread(() -> {
                LoginInformationProvider.getInstance();
                RandomRewards.load();
                MapleOxQuizFactory.getInstance();
                UnionHandler.loadUnion();
            }, "EtcLoader", this);
            LoadingThread MonsterLoader = new LoadingThread(() -> {
                MobSkillFactory.getInstance();
                FieldSkillFactory.getInstance();
                MobAttackInfoFactory.getInstance();
            }, "MonsterLoader", this);
            LoadingThread EmoticonLoader = new LoadingThread(() -> ChatEmoticon.LoadEmoticon(), "EmoticonLoader", this);
            LoadingThread MatrixLoader = new LoadingThread(() -> MatrixHandler.loadCore(), "MatrixLoader", this);
            LoadingThread MarriageLoader = new LoadingThread(() -> MarriageManager.getInstance(), "MarriageLoader", this);
            LoadingThread[] LoadingThreads = {SkillLoader, QuestLoader, QuestCustomLoader, ItemLoader, GuildRankingLoader, EtcLoader, MonsterLoader, MatrixLoader, MarriageLoader, EmoticonLoader};
            for (Thread t : LoadingThreads)
                t.start();
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (Start.CompletedLoadingThreads.get() != LoadingThreads.length) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            World.Guild.load();
            GameConstants.isOpen = true;
            if (ServerConstants.ConnectorSetting)
                ConnectorServer.run();
            if (!ServerConstants.ConnectorSetting)
                System.out.println("[완료] 1.2.373 포신항문오도기합해병특별자치스토리 서버 구동 완료.");
                CMDCommand.main();
            System.out.println("[Fully Initialized in " + ((System.currentTimeMillis() - Start.startTime) / 1000L) + " seconds]");
        }
    }

    private static class LoadingThread extends Thread {
        protected String LoadingThreadName;

        private LoadingThread(Runnable r, String t, Object o) {
            super(new NotifyingRunnable(r, o, t));
            this.LoadingThreadName = t;
        }

        public synchronized void start() {
            //System.out.println("[Loading...] Started " + this.LoadingThreadName + " Thread");
            super.start();
        }
    }

    private static class NotifyingRunnable implements Runnable {
        private String LoadingThreadName;

        private long StartTime;

        private Runnable WrappedRunnable;

        private final Object ToNotify;

        private NotifyingRunnable(Runnable r, Object o, String name) {
            this.WrappedRunnable = r;
            this.ToNotify = o;
            this.LoadingThreadName = name;
        }

        public void run() {
            this.StartTime = System.currentTimeMillis();
            this.WrappedRunnable.run();
            //System.out.println("[Loading Completed] " + this.LoadingThreadName + " | Completed in " + (System.currentTimeMillis() - this.StartTime) + " Milliseconds. (" + (Start.CompletedLoadingThreads.get() + 1) + "/10)");
            synchronized (this.ToNotify) {
                Start.CompletedLoadingThreads.incrementAndGet();
                this.ToNotify.notify();
            }
        }
    }

    public static class Shutdown implements Runnable {
        public void run() {
            ShutdownServer.getInstance().run();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        instance.run();
        DatabaseBackup.main(args);
        CommodityItemUpdate.main(args);
        // if (false) {
        //     CommodityItemUpdate.main(args);
        // }
    }

    public static void JamsuPoint() {
        Timer.WorldTimer tMan = Timer.WorldTimer.getInstance();
        Runnable r = new Runnable() {
            public void run() {
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters().values()) {
                        if (mch.getMapId() == ServerConstants.warpMap || mch.getMapId() == 993215603){
                            if (!mch.isFirst) {
                                mch.getClient().send((CField.UIPacket.detailShowInfo("해병취침 포인트(잠수 포인트) 적립을 시작하겠다 아쎄이!", 3, 20, 20)));
                                mch.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/14thTerra/reward"));
                                mch.isFirst = true;
                            }
                            if (mch.getClient().getKeyValue("jamsupoint") == null) {
                                mch.getClient().setKeyValue("jamsupoint", "0");
                            }
                            mch.JamsuTime++;
                            if (mch.JamsuTime >= 60) {//900 = sec
                                mch.JamsuTime = 0;
                                mch.Jamsu5m++;
                                long point = mch.getPlayer().getKeyValue(501368, "point");
                                point += 2;
                                mch.getPlayer().setKeyValue(501368, "point", point + "");
                                if (mch.Jamsu5m >= 5) {
                                    mch.getClient().send((CField.UIPacket.detailShowInfo("해병취침 포인트가 지급되었다 아쎄이! 해병취침 포인트 : " + mch.getPlayer().getKeyValue(501368, "point"), 3, 20, 20)));
                                    mch.Jamsu5m = 0;
                                }
                            }
                        } else {
                            mch.JamsuTime = 0;
                            mch.isFirst = false;

                        }
                    }
                }
            }
        };
        tMan.register(r, 1000);
    }


    private String getMachineIp() {

        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        }
        catch ( UnknownHostException e ) {
            e.printStackTrace();
        }

        if( local == null ) {
            return "";
        }
        else {
            String ip = local.getHostAddress();
            return ip;
        }

    }
}
