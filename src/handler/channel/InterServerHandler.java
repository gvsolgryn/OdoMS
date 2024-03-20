package handler.channel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleQuestStatus;
import constants.programs.ControlUnit;
import client.items.MapleInventoryType;
import client.skills.ISkill;
import client.skills.SkillEntry;
import client.skills.SkillFactory;
import community.BuddylistEntry;
import community.MapleGuild;
import community.MapleMultiChatCharacter;
import community.MapleUserTrade;
import constants.GameConstants;
import constants.ServerConstants;
import database.MYSQL;
import handler.admin.AdminToolPacket;
import static handler.channel.MatrixHandler.getValue;
import handler.duey.DueyHandler;
import java.util.Calendar;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import launch.AdminToolServer;
import launch.AuctionServer;
import launch.ChannelServer;
import launch.LoginServer;
import launch.helpers.ChracterTransfer;
import launch.helpers.MapleItemHolder;
import launch.helpers.MaplePlayerIdChannelPair;
import launch.world.WorldBroadcasting;
import launch.world.WorldCommunity;
import packet.creators.CashPacket;
import packet.creators.LoginPacket;
import packet.creators.MainPacketCreator;
import packet.creators.PetPacket;
import packet.creators.SLFCGPacket;
import packet.creators.SoulWeaponPacket;
import packet.creators.UIPacket;
import packet.skills.ZeroSkill;
import packet.transfer.read.ReadingMaple;
import scripting.NPCScriptManager;
import server.items.ItemInformation;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.shops.IMapleCharacterShop;

public class InterServerHandler {

    public static final void EnterMTS(final MapleClient c) {
        final MapleMap map = c.getChannelServer().getMapFactory().getMap(910000000);
        c.getPlayer().changeMap(map, map.getPortal(0));
    }

    public static final void EnterAuction(MapleClient c) {
        final MapleCharacter chr = c.getPlayer();

        if (!chr.isAlive()) {
            c.getSession().writeAndFlush(MainPacketCreator.resetActions(c.getPlayer()));
            return;
        }

        final ChannelServer ch = ChannelServer.getInstance(c.getChannel());

        String ip = ServerConstants.getServerHost(c);

        if (ip == null) {
            c.getSession().writeAndFlush(MainPacketCreator.serverNotice(5, "캐시샵을 현재 사용할 수 없습니다."));
            return;
        }

        if (chr.getTrade() != null) {
            MapleUserTrade.cancelTrade(chr.getTrade());
        }

        final IMapleCharacterShop shop = chr.getPlayerShop();
        if (shop != null) {
            shop.removeVisitor(chr);
            if (shop.isOwner(chr)) {
                shop.setOpen(true);
            }
        }

        if (chr.getMessenger() != null) {
            MapleMultiChatCharacter messengerplayer = new MapleMultiChatCharacter(chr);
            WorldCommunity.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        try {
            chr.cancelAllBuffs();
        } catch (Exception ex) {

        }
        ChannelServer.addCooldownsToStorage(chr.getId(), chr.getAllCooldowns());
        ChannelServer.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        ChannelServer.ChannelChange_Data(new ChracterTransfer(chr), chr.getId(), -20);
        ch.removePlayer(chr);
        c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());
        c.getSession().writeAndFlush(MainPacketCreator.serverMessage(""));
        c.getSession().writeAndFlush(MainPacketCreator.getChannelChange(AuctionServer.PORT, ServerConstants.getServerHost(c)));
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.setPlayer(null);
    }

    public static final void EnterCS(final MapleClient c, final MapleCharacter chr, final boolean ScriptEnter) {
        if (!chr.isAlive()) {
            c.getSession().writeAndFlush(MainPacketCreator.resetActions(c.getPlayer()));
            return;
        }
        if (ScriptEnter && ServerConstants.cshopNpc != 0) {
            if (c.getPlayer().getConversation() != 0) {
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush(MainPacketCreator.resetActions(c.getPlayer()));
            }
            NPCScriptManager.getInstance().start(c, ServerConstants.cshopNpc, null);
            c.getSession().writeAndFlush(MainPacketCreator.resetActions(c.getPlayer()));
            return;
        }
        final ChannelServer ch = ChannelServer.getInstance(c.getChannel());

        String ip = ServerConstants.getServerHost(c);

        if (ip == null) {
            c.getSession().writeAndFlush(MainPacketCreator.serverNotice(5, "캐시샵을 현재 사용할 수 없습니다."));
            return;
        }

        if (chr.getTrade() != null) {
            MapleUserTrade.cancelTrade(chr.getTrade());
        }

        final IMapleCharacterShop shop = chr.getPlayerShop();
        if (shop != null) {
            shop.removeVisitor(chr);
            if (shop.isOwner(chr)) {
                shop.setOpen(true);
            }
        }

        if (chr.getMessenger() != null) {
            MapleMultiChatCharacter messengerplayer = new MapleMultiChatCharacter(chr);
            WorldCommunity.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        try {
            chr.cancelAllBuffs();
        } catch (Exception ex) {

        }
        ChannelServer.addCooldownsToStorage(chr.getId(), chr.getAllCooldowns());
        ChannelServer.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        ChannelServer.ChannelChange_Data(new ChracterTransfer(chr), chr.getId(), -10);
        ch.removePlayer(chr);
        c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());
        c.getSession().writeAndFlush(MainPacketCreator.serverMessage(""));
        c.getSession().writeAndFlush(MainPacketCreator.getChannelChange(ServerConstants.CashShopPort, ServerConstants.getServerHost(c))); // default
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.setPlayer(null);
    }

    public static void Loggedin(final int playerid, final MapleClient c) {
        final ChannelServer channelServer = c.getChannelServer();
        MapleCharacter player;
        final ChracterTransfer transfer = channelServer.getPlayerStorage().getPendingCharacter(playerid);
        boolean checkFromDB = false;
        if (transfer == null) {
            player = MapleCharacter.loadCharFromDB(playerid, c, true);
            checkFromDB = true;
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
        }
        if (player == null) {
            System.out.println("ERROR!!!!!! CANNOT LOAD CHARACTER FROM DB!!");
            return;
        }
        c.setPlayer(player);
        c.setAccID(player.getAccountID());
        c.loadAuthData();
        c.getPlayer().setMorphGage(0);
        final int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!ChannelServer.isCharacterListConnected(c.loadCharacterNames(), true)) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            if (!ServerConstants.realese) {
            }
            return;
        }
        if (!ControlUnit.ConnectionList.contains(player.getName())) {
            ControlUnit.동접(player.getName());
            ControlUnit.connection.setText(String.valueOf((int) (Integer.parseInt(ControlUnit.connection.getText()) + 1)));
        }
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                if (chr.getName().equals(player.getName())) {
                    c.setPlayer(null);
                    c.getSession().close();
                    if (!ServerConstants.realese) {
                    }
                    return;
                }
            }
        }

        if (!LoginServer.getInstance().ip.contains(c.getSessionIPAddress())) {
            c.setPlayer(null);
            c.getSession().close();
            System.out.println("3not allow login - " + c.getAccountName() + " from " + c.getSessionIPAddress() + " / state : " + state);
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        
        
        final ChannelServer cserv = ChannelServer.getInstance(c.getChannel());
        cserv.addPlayer(player);
        c.getSession().writeAndFlush(MainPacketCreator.HeadTitle(player.HeadTitle()));
        c.getSession().writeAndFlush(MainPacketCreator.getPlayerInfo(player));
        player.getMap().addPlayer(player);
//        Calendar cal = Calendar.getInstance(); 
//        int Month = cal.get(Calendar.MONTH) + 1;
//        if (c.getPlayer().getDailyGift().checkDailyGift(c.getAccID(c.getAccountName()))) {
//            c.getPlayer().getDailyGift().loadDailyGift(c.getAccID(c.getAccountName()));
//        } else {
//            c.getPlayer().getDailyGift().InsertDailyData(c.getAccID(c.getAccountName()), 0, 0);
//            c.getPlayer().getDailyGift().loadDailyGift(c.getAccID(c.getAccountName()));
//        }
        c.getPlayer().giveCoolDowns(ChannelServer.getCooldownsFromStorage(player.getId()));
        try {
            player.expirationTask();
        } catch (Exception e) {
            if (!ServerConstants.realese) {
                e.printStackTrace();
            }
        }

        if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -27) != null && player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -28) != null) {
            if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -27).getAndroid() != null) {
                player.setAndroid(player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -27).getAndroid());
            }
        }
        try {
            player.expirationTask();
            try {
                c.getPlayer().giveSilentDebuff(ChannelServer.getDiseaseFromStorage(player.getId()));
            } catch (NullPointerException ex) {

            }
            final int buddyIds[] = player.getBuddylist().getBuddyIds();
            WorldBroadcasting.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds);
            final MaplePlayerIdChannelPair[] onlineBuddies = WorldCommunity.multiBuddyFind(player.getId(), buddyIds);
            for (MaplePlayerIdChannelPair onlineBuddy : onlineBuddies) {
                final BuddylistEntry ble = player.getBuddylist().get(onlineBuddy.getCharacterId());
                ble.setChannel(onlineBuddy.getChannel());
                player.getBuddylist().put(ble);
            }
            c.getSession().writeAndFlush(MainPacketCreator.updateBuddylist(player.getBuddylist().getBuddies(), 10, 0));
            if (player.getGuildId() > 0) {
                ChannelServer.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.getSession().writeAndFlush(MainPacketCreator.showGuildInfo(player));
                final MapleGuild gs = ChannelServer.getGuild(player.getGuildId());
                if (gs != null) {
                    final List<byte[]> packetList = ChannelServer.getAllianceInfo(gs.getAllianceId(), true);
                    if (packetList != null) {
                        for (byte[] pack : packetList) {
                            if (pack != null) {
                                c.getSession().writeAndFlush(pack);
                            }
                        }
                    }
                } else {
                    player.setGuildId(0);
                    player.setGuildRank((byte) 5);
                    player.setAllianceRank((byte) 5);
                    player.saveGuildStatus();
                }
            }
        } catch (Exception e) {
            if (!ServerConstants.realese) {
                e.printStackTrace();
            }
        }
        player.showNote();
        player.updatePartyMemberHP();

        if (c.getPlayer().matrixSkills.size() > 0) {
            c.sendPacket(MainPacketCreator.updateSkill(c.getPlayer().matrixSkills));
        }

        if (GameConstants.isPhantom(player.getJob())) {
            c.getSession().writeAndFlush(MainPacketCreator.cardAmount(c.getPlayer().getCardStack()));
        }

        player.completeQuest(34120, 0);
        if (GameConstants.isMercedes(player.getJob()))
        player.updateInfoQuest(7784, "sw=1");
        
        for (MapleQuestStatus status : player.getStartedQuests()) {
            if (status.hasMobKills()) {
                c.getSession().writeAndFlush(MainPacketCreator.updateQuestMobKills(status));
            }
        }
        String quick = player.getQuestNAdd(MapleQuest.getInstance(GameConstants.QUICK_SLOT)).getCustomData();
        if (quick != null) {
            List<Integer> quickSlot = new ArrayList<>();
            String[] sp = quick.split(",");
            for (String s : sp) {
                quickSlot.add(Integer.parseInt(s));
            }
            c.send(MainPacketCreator.getQuickSlot(quickSlot));
        }

        if (DueyHandler.DueyItemSize(player.getName()) > 0) {
            player.send(DueyHandler.DueyMessage(28));
        }

        /* 제로 젠더 겹칠시 초기화 */
        if ((player.getGender() == 0) && (player.getSecondGender() == 0) && (GameConstants.isZero(player.getJob()) || (player.getGender() == 1) && (player.getSecondGender() == 1) && (GameConstants.isZero(player.getJob())))) {
            player.setGender((byte) 1);
            player.setSecondGender((byte) 0);
        }

   /*     if (player.haveItem(1142009, 1, true, true)) { //딜미터기
            if (!isDamageMeterRanker(player.getId())) {
                player.removeAllEquip(1142009, false);
                player.send(MainPacketCreator.OnAddPopupSay(9000036, 3000, ItemInformation.getInstance().getName(1142009) + " 자격이 박탈되어, 훈장이 회수되었습니다.", ""));
            }
        }

        if (ServerConstants.loginPointAid != -1) { //로그인 포인트 랭킹
            if (player.haveItem(1142077, 1, true, true)) {
                if (player.getAccountID() != ServerConstants.loginPointAid) {
                    player.removeAllEquip(1142077, false);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142077) + " 자격이 박탈되어, 훈장이 회수되었습니다.");
                }
            }
        }

        if (ServerConstants.loginPointAid != -1) { //로그인 포인트 랭킹
            if (player.getAccountID() == ServerConstants.loginPointAid) {
                if (!player.haveItem(1142077, 1, true, true)) {
                    player.gainItem(1142077, 1);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142077) + " 훈장이 지급 되었습니다.");
                }
            }
        } 

/*        if (ServerConstants.chr != null) { //전광판
            if (player.haveItem(1142144, 1, true, true)) {
                if (!player.getName().equals(ServerConstants.chr.getName())) {
                    player.removeAllEquip(1142144, false);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142144) + " 자격이 박탈되어, 훈장이 회수되었습니다.");
                }
            }
        }

        if (ServerConstants.chr != null) { //전광판
            if (player.getName().equals(ServerConstants.chr.getName())) {
                if (!player.haveItem(1142144, 1, true, true)) {
                    player.gainItem(1142144, 1);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142144) + " 훈장이 지급 되었습니다.");
                }
            }
        }

        if (ServerConstants.prank1 != null) { //인기도 랭킹
            if (player.haveItem(1142011, 1, true, true)) {
                if (!player.getName().equals(ServerConstants.prank1)) {
                    player.removeAllEquip(1142011, false);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142011) + " 자격이 박탈되어, 훈장이 회수되었습니다.");
                }
            }
        }

        if (ServerConstants.prank1 != null) { //인기도 랭킹
            if (player.getName().equals(ServerConstants.prank1)) {
                if (!player.haveItem(1142011, 1, true, true)) {
                    player.gainItem(1142011, 1);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142011) + " 훈장이 지급 되었습니다.");
                }
            }
        } 

        if (ServerConstants.mrank1 != null) { //메소 랭킹
            if (player.haveItem(1142010, 1, true, true)) {
                if (!player.getName().equals(ServerConstants.mrank1)) {
                    player.removeAllEquip(1142010, false);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142010) + " 자격이 박탈되어, 훈장이 회수되었습니다.");
                }
            }
        }

        if (ServerConstants.mrank1 != null) { //메소 랭킹
            if (player.getName().equals(ServerConstants.mrank1)) {
                if (!player.haveItem(1142010, 1, true, true)) {
                    player.gainItem(1142010, 1);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142010) + " 훈장이 지급 되었습니다.");
                }
            }
        } */

        if (ServerConstants.crank1 != null) { //추천인 랭킹
            if (player.haveItem(1142140, 1, true, true)) {
                if (!player.getName().equals(ServerConstants.crank1)) {
                    player.removeAllEquip(1142140, false);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142140) + " 자격이 박탈되어, 훈장이 회수되었습니다.");
                }
            }
        }

        if (ServerConstants.crank1 != null) { //추천인 랭킹
            if (player.getName().equals(ServerConstants.crank1)) {
                if (!player.haveItem(1142140, 1, true, true)) {
                    player.gainItem(1142140, 1);
                    player.dropMessage(5, ItemInformation.getInstance().getName(1142140) + " 훈장이 지급 되었습니다.");
                }
            }
        } 

        /* 제로가 아닐시 젠더 사용안함 처리 */
        if (!GameConstants.isZero(c.getPlayer().getJob())) {
            c.getPlayer().setSecondGender((byte) -1);
        } else {
            c.send(ZeroSkill.Clothes(player.getBetaClothes()));
        }

        /* 소울인챈터 */
        if ((player.isEquippedSoulWeapon()) && (transfer == null)) {
            player.setSoulCount(0);
            c.send(SoulWeaponPacket.giveSoulGauge(player.getSoulCount(), player.getEquippedSoulSkill()));
        }

        /* 정령의 펜던트 */
        if (player.getInventory(MapleInventoryType.EQUIPPED).findById(1122017) != null || player.getInventory(MapleInventoryType.EQUIPPED).findById(1122155) != null) {
            player.equipPendantOfSpirit();
        }

        /* 오픈게이트 */
        c.getPlayer().setKeyValue("opengate", null);
        c.getPlayer().setKeyValue("count", null);

        /* 펜던트 슬롯 */
        if (!c.getPlayer().getStat().getJC()) {
            String[] text = {"장미칼", "관종", "유리멘탈", "부처멘탈", "주인공", "히로인", "엑스트라", "신", "어그로", "장비충", ServerConstants.serverName, "게이", "키보드워리어", "뚱보", "트롤", "미남", "미녀", "바보", "멍청이", "최강", "돼지", "잘생긴", "이쁜", "랭커", "귀여운"};
            String[] text2 = {"반갑습니다", "안녕하세요?", "오던가 말던가..", "이랏샤이 마센!!", "환영합니다!"};
            String text3 = text[(int) java.lang.Math.floor(java.lang.Math.random() * text.length)] + " " + player.getName() + " " + text2[(int) java.lang.Math.floor(java.lang.Math.random() * text2.length)];
            WorldBroadcasting.broadcastMessage(UIPacket.detailShowInfo(text3 + " 동접수 : " + ChannelServer.getOnlineConnections() + "", false));
            c.getSession().write(MainPacketCreator.serverMessage(ServerConstants.serverMessage));
            c.getPlayer().getStat().setJC(true);
        }     

        c.getSession().writeAndFlush(MainPacketCreator.serverMessage(ServerConstants.serverMessage));

        if (ServerConstants.serverHint != null) {
            if (ServerConstants.serverHint.length() > 0) {
                c.getSession().writeAndFlush(MainPacketCreator.OnAddPopupSay(9062000, 6000, ServerConstants.serverHint, ""));
                c.getSession().writeAndFlush(MainPacketCreator.serverNotice(5, ServerConstants.serverHint));
            }
        }

        if (GameConstants.isXenon(player.getJob())) {
            player.startSurPlus();
        }

        if (GameConstants.isBlaster(player.getJob())) {
            player.giveBulletGauge(0, false);
        }

        if (GameConstants.isDemonAvenger(player.getJob())) {
            c.send(MainPacketCreator.giveDemonWatk(c.getPlayer().getStat().getHp()));
        }
        c.getSession().writeAndFlush(CashPacket.pendantSlot(false));
        player.sendMacros();

        if (GameConstants.isKaiser(c.getPlayer().getJob())) {
            c.getPlayer().changeKaiserTransformKey();
        } else {
            c.getSession().writeAndFlush(MainPacketCreator.getKeymap(player.getKeyLayout()));
        }
        if (GameConstants.isKOC(player.getJob()) && player.getLevel() >= 100) {
            if (player.getSkillLevel(Integer.parseInt(String.valueOf(player.getJob() + "1000"))) <= 0) {
                player.teachSkill(Integer.parseInt(String.valueOf(player.getJob() + "1000")), (byte) 0, SkillFactory.getSkill(Integer.parseInt(String.valueOf(player.getJob() + "1000"))).getMaxLevel());
            }
        }

        if (!player.getInfoQuest(31389).equals("ex=0") && !player.getInfoQuest(31389).equals("")) {
            c.sendPacket(UIPacket.OpenUI(1151));
        }
        player.send(MainPacketCreator.resetModifyInventoryItem());
        player.send(MainPacketCreator.showMaplePoint(player.getNX()));
        //c.getSession().writeAndFlush(MainPacketCreator.OnChatLetClientConnect());       

//        if (!c.getPlayer().getDailyGift().getDailyData().equals(cal.get(Calendar.YEAR) + (Month == 10 || Month == 11 || Month == 12 ? "" : "0") + Month + (cal.get(Calendar.DATE) < 10 ? "0" : "") + cal.get(Calendar.DATE))) {
//            c.getPlayer().getDailyGift().setDailyCount(0);
//            c.getPlayer().getDailyGift().saveDailyGift(c.getAccID(c.getAccountName()), c.getPlayer().getDailyGift().getDailyDay(), c.getPlayer().getDailyGift().getDailyCount(), c.getPlayer().getDailyGift().getDailyData());
//        }
//        c.getSession().writeAndFlush(MainPacketCreator.getDailyGiftRecord("count=" + c.getPlayer().getDailyGift().getDailyCount() + ";day=" + c.getPlayer().getDailyGift().getDailyDay() + ";date=" + GameConstants.getCurrentDate_NoTime()));
//        c.getSession().writeAndFlush(UIPacket.OnDailyGift((byte) 0, 2, 0));
//        if (c.getPlayer().cores.size() == 0) {
//            for (int b = 0; b < 28; b++) {
//                if (getValue(c, "core" + b) != -1) {
//                    c.getPlayer().setKeyValue("core" + b, "-1");
//                }
//            }
//            for (final Map.Entry<ISkill, SkillEntry> s : c.getPlayer().matrixSkills.entrySet()) {
//                c.getPlayer().changeSkillLevel(s.getKey(), (byte) 0, (byte) 0);
//            }
//        }

         for (MapleCharacter ch : player.getMap().getCharacters()) {
            if (player != ch) {
                player.send(MainPacketCreator.spawnPlayerMapobject(ch));
                AdminToolServer.broadcastMessage(AdminToolPacket.Info());
            }
        }
    }


	public boolean isDamageMeterRanker(int cid, boolean no)
	{
		boolean value = false;
		try
		{
			Connection con = MYSQL.getConnection();
			PreparedStatement ps = null;
                        if (!no)
                            ps = con.prepareStatement("SELECT * FROM damagerank ORDER BY damage DESC LIMIT 1");
                        else
                            ps = con.prepareStatement("SELECT * FROM damagerank2 ORDER BY damage DESC LIMIT 1");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				if (rs.getInt("cid") == cid)
				{
					value = true;
				}
			}
			rs.close();
			ps.close();
			con.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return value;
	}

    public static final void ChangeChannel(final ReadingMaple rh, final MapleClient c, final MapleCharacter chr) {
        if (!chr.isAlive()) {
            c.getSession().writeAndFlush(MainPacketCreator.resetActions(c.getPlayer()));
            return;
        }
        final int channel = rh.readByte();

        if (c.getPlayer().getStat().ChDelay > System.currentTimeMillis()) {
            c.getPlayer().dropMessage(1, "채널 변경은 3초에 한번 가능합니다.");
            c.getPlayer().send(MainPacketCreator.resetActions(c.getPlayer()));
            return;
        }
        c.getPlayer().getStat().ChDelay = System.currentTimeMillis() + 3000;
        final ChannelServer toch = ChannelServer.getInstance(channel);

        if (FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit()) || channel == c.getChannel()) {
            c.getSession().close();
            return;
        } else if (toch == null || toch.isShutdown()) {
            c.getSession().writeAndFlush(MainPacketCreator.serverNotice(5, "현재 접근할 수 없습니다."));
            return;
        }
        if (chr.getTrade() != null) {
            MapleUserTrade.cancelTrade(chr.getTrade());
        }

        final IMapleCharacterShop shop = chr.getPlayerShop();
        if (shop != null) {
            shop.removeVisitor(chr);
            if (shop.isOwner(chr)) {
                shop.setOpen(true);
            }
        }

        final ChannelServer ch = ChannelServer.getInstance(c.getChannel());
        if (chr.getMessenger() != null) {
            WorldCommunity.silentLeaveMessenger(chr.getMessenger().getId(), new MapleMultiChatCharacter(chr));
        }
        try {
            chr.cancelAllBuffs();
        } catch (Exception ex) {
        }
        ChannelServer.addCooldownsToStorage(chr.getId(), chr.getAllCooldowns());
        ChannelServer.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        ChannelServer.ChannelChange_Data(new ChracterTransfer(chr), chr.getId(), channel);
        MapleItemHolder.registerInv(chr.getId(), chr.getInventorys());
        ch.removePlayer(chr);
        c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());
        c.getSession().writeAndFlush(MainPacketCreator.getChannelChange(ServerConstants.basePorts + (channel), ServerConstants.getServerHost(c)));
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.setPlayer(null);
    }

    /*
    public static void getGameQuitRequest(ReadingMaple rh, MapleClient c) { //게임종료시 로그인창으로
        String account = rh.readMapleAsciiString();
        c.getSession().writeAndFlush(MainPacketCreator.GameEnd());
    }
}
     */
    public static void getGameQuitRequest(ReadingMaple rh, MapleClient c) { //게임종료시 캐릭선택창으로
        String account = rh.readMapleAsciiString();
        if (account.equals("")) {
            account = c.getAccountName();
        }
        if (!c.isLoggedIn() && !c.getAccountName().equals(account)) {
            c.getSession().close();
            return;
        }
        c.getSession().writeAndFlush(MainPacketCreator.serverNotice(4, ""));
        c.getSession().writeAndFlush(LoginPacket.getKeyGuardResponse((account) + "," + (c.getPassword(account))));
    }
}
