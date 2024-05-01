package client.messages.commands;

import client.MapleCharacter;
import constants.ServerConstants.PlayerGMRank;
import client.MapleClient;
import client.MapleQuestStatus;
import client.MapleStat;
import client.PlayerStats;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleShop;
import server.MapleShopFactory;
import server.Randomizer;
import server.Start;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.log.LogType;
import server.log.ServerLogger;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.SavedLocationType;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.StringUtil;

public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }
    public static class ㄹ extends PlayerCommand.렉 {
    }    
    public static class 랙 extends PlayerCommand.렉 {
    }
    public static class fpr extends PlayerCommand.렉 {
    }
    public static class 렉 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            c.getPlayer().dropMessage(6, "무반응 현상이 해결되었습니다.");
            
            int removeSkillID = 35111002;
            c.getPlayer().removeCooldown(removeSkillID);
//            c.getPlayer().addCooldown(removeSkillID, System.currentTimeMillis(), 30 * 1000);
            c.getPlayer().getClient().getSession().write(MaplePacketCreator.skillCooldown(removeSkillID, 0));
            return 1;
        }
    }

/*    public static class 저장 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage(5, "캐릭터가 저장 되었습니다.");
            return 1;
        }
    }*/
    
    public static class 스탯 extends CommandExecute {
        @Override
        public int execute(MapleClient c, String[] args) {
            c.getPlayer().customizeStat(0, true);
            System.err.println(c.getSessionIPAddress());
            return 1;
        }
    }
    
    public static class 보스재입장 extends CommandExecute {
        @Override
        public int execute(MapleClient c, String[] args) {
            MapleQuestStatus emData = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(202304240));
            String instanceName = emData.getCustomData();
            if (instanceName != null) {
                EventManager em = c.getChannelServer().getEventSM().getEventManager(instanceName);
                if (em != null) {
                    EventInstanceManager eim = em.getInstance(instanceName);
                    if (eim != null) {
                        if (eim.getPlayers().size() > 0) {
                            for (MapleCharacter eimUser : eim.getPlayers()) {
                                if (eimUser != null) {
                                    int toMapID = eimUser.getMapId();
                                    if (c.getPlayer().getMapId() != toMapID) {
                                        eim.registerPlayer(c.getPlayer());
                                        c.getPlayer().dropMessage(2, "[시스템] : 데스카운트가 " + c.getPlayer().getDeathCount() + "회 남았습니다.");
                                        return 1;
                                    }
                                }
                            }
                            c.getPlayer().removeDeathCount();
                            c.getPlayer().dropMessage(2, "[시스템] : 현재 종료 된 원정입니다.");
                        }
                    } else {
                        c.getPlayer().dropMessage(2, "[시스템] : 재입장할 수 있는 원정이 없습니다. 3");
                    }
                } else {
                    c.getPlayer().dropMessage(2, "[시스템] : 재입장할 수 있는 원정이 없습니다. 2");
                }
            } else {
                c.getPlayer().dropMessage(2, "[시스템] : 재입장할 수 있는 원정이 없습니다. 1");
            }
            return 0;
        }
    }

        public static class 동접 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            java.util.Map<Integer, Integer> connected = World.getConnected();
            StringBuilder conStr = new StringBuilder("[전체 동접]");
            boolean first = true;
            for (int i : connected.keySet()) {
                if (!first) {
                    //conStr.append(", ");
                } else {
                    first = false;
                }
                if (i == 0) {
                    conStr.append(" 총 : ");
                    int connection = connected.get(i);
                    int incConnection = Start.increaseConnectionUsers;
                    if (connection < 10) {
                        incConnection = 0;
                    }
                    if (incConnection < 0) {
                        if (Math.abs(incConnection) < connection)
                            connection += incConnection;
                    } else {
                        connection += incConnection;
                    }
                    conStr.append(connection); //동접주작1
                } else {
//                    conStr.append("채널");
//                    conStr.append(i);
//                    conStr.append(": ");
//                    conStr.append(connected.get(i));
                }
            }
            
            c.getPlayer().dropMessage(6, conStr.toString());
            return 1;
        }
    }
        
        public static class 택배 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().hasBlockedInventory() || c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000 /* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                c.getPlayer().dropMessage(5, "명령어를 사용하실 수 없습니다.");
                return 0;
            } else if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000
                    || c.getPlayer().getMapId() == 390001000 || c.getPlayer().getMapId() == 950100100 || c.getPlayer().getMapId() == 950100200 || c.getPlayer().getMapId() == 950100300 || c.getPlayer().getMapId() == 950100400 || c.getPlayer().getMapId() == 950100500 || c.getPlayer().getMapId() == 950100600 || c.getPlayer().getMapId() == 950100700)
                    || c.getPlayer().getMapId() >= 951000000 && c.getPlayer().getMapId() <= 954060000 ) {
                c.getPlayer().dropMessage(5, "명령어를 사용하실 수 없습니다.");
                return 0;
            } else {
                c.getPlayer().getClient().removeClickedNPC();
                NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 9010009);
                return 1;
            }
        }
    }

    public static class ㄷㅇㅁ extends PlayerCommand.명령어 {
    }    
    public static class ㄱㅇㄷ extends PlayerCommand.명령어 {
    }    
    public static class ㅁㄹㅇ extends PlayerCommand.명령어 {
    }
    public static class 가이드 extends PlayerCommand.명령어 {
    }
    public static class 도움말 extends PlayerCommand.명령어 {
    }
    public static class 명령어 extends CommandExecute {


        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9010023);
            return 1;
        }
    }     

    public static class 업뎃 extends PlayerCommand.업데이트 {
    }       
    public static class ㅇㄷㅇㅌ extends PlayerCommand.업데이트 {
    }    
    public static class 업데이트 extends CommandExecute {


        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9010023);
            return 1;
        }
    }     
    
    public static class ㄱㅎ extends PlayerCommand.교환 {
    }    
    public static class 교환 extends CommandExecute {


        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9070002);
            return 1;
        }
    } 
    
    public static class 인벤정리 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            java.util.Map<Pair<Short, Short>, MapleInventoryType> eqs = new HashMap<Pair<Short, Short>, MapleInventoryType>();
            if (splitted[1].equals("장비")) {
                for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIP);
                }
            } else if (splitted[1].equals("소비")) {
                for (Item item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.USE);
                }
            } else if (splitted[1].equals("설치")) {
                for (Item item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.SETUP);
                }
            } else if (splitted[1].equals("기타")) {
                for (Item item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.ETC);
                }
            } else {
                c.getPlayer().dropMessage(6, "[장비/소비/설치/기타]");
            }
            for (Map.Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet()) {
                MapleInventoryManipulator.removeFromSlot(c, eq.getValue(), eq.getKey().left, eq.getKey().right, false, false);
            }
            return 1;
        }
    }
    
    public static class 오토루팅 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (int i : GameConstants.autolootblockedMaps) {
                if (c.getPlayer().getMapId() == i) {
                    c.getPlayer().dropMessage(5, "현재 맵 에서는 명령어를 사용하실 수 없습니다.");
                    return 0;
                }
            }
            c.getPlayer().setAutoStatus(c.getPlayer().getAutoStatus() ? false : true);
            c.getPlayer().dropMessage(6, "오토루팅이 " + (c.getPlayer().getAutoStatus() ? "작동" : "미작동") + "상태로 바뀌었습니다.");
            return 1;
        }
    }
    
    public static class 와협 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;

            if (splitted.length > 1) {
                int irange = Integer.parseInt(splitted[1]);
                if (splitted.length <= 2) {
                    range = irange * irange;
                } else {
                    map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
                }
            }
            if (map == null) {
                c.getPlayer().dropMessage(6, "존재하지 않는 맵입니다.");
                return 0;
            }
            MapleMonster mob;
            for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (c.getPlayer().getMapId() == 240040400) {
                    map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
                } else {
                    c.getPlayer().dropMessage(6, "와이번의 협곡이 아닙니다.");
                    return 0;
                }
            }
            return 1;
        }
    }  
    
    
    public static class 일괄판매 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (int i : GameConstants.autolootblockedMaps) {
                if (c.getPlayer().getMapId() == i) {
                    c.getPlayer().dropMessage(5, "현재 맵 에서는 명령어를 사용하실 수 없습니다.");
                    return 0;
                }
            }
            int a = 0;
            int meso = c.getPlayer().getMeso();
            MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            
            for (int i = 0; i < use.getSlotLimit(); i++) { // impose order...
                Item item = use.getItem((byte) i);
                Equip ep = (Equip) item;
                if (item != null) {
                    if (!ii.isPickupRestricted(item.getItemId())  // 고유
                            && !ii.isDropRestricted(item.getItemId()) 
                            && !ii.isCash(item.getItemId()) // 캐시
                            && !ii.isAccountShared(item.getItemId()) // 계정공유
                            && !ii.isKarmaEnabled(item.getItemId()) // 카르마
                            && !ii.isPKarmaEnabled(item.getItemId()) // 플래티넘카르마
                            && ep.getState() == 0 // 미확인
                            && c.getPlayer().haveItem(item.getItemId(), 1, true, true)
                            ) {
                        MapleShop.playersell(c, GameConstants.getInventoryType(item.getItemId()), (byte) i, (short) item.getQuantity());
                        a++;
                    } 
                }
            }
            int meso2 = c.getPlayer().getMeso();
            c.getPlayer().dropMessage(6, "일괄판매로 " + a +  "개의 아이템을 판매하였습니다. 획득한 메소 " + (meso2 - meso));
        //    c.getPlayer().dropMessage(6, "일괄판매로 " + (meso2 - meso) +  "개의 아이템을 판매하였습니다.");
            return 1;
        }
    }
    
    public static class 일괄판매2 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (int i : GameConstants.autolootblockedMaps) {
                if (c.getPlayer().getMapId() == i) {
                    c.getPlayer().dropMessage(5, "현재 맵 에서는 명령어를 사용하실 수 없습니다.");
                    return 0;
                }
            }
            int a = 0;
            int meso = c.getPlayer().getMeso();
            MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            
            for (int i = 0; i < use.getSlotLimit(); i++) { // impose order...
                Item item = use.getItem((byte) i);
                Equip ep = (Equip) item;
                if (item != null) {
                    if (!ii.isPickupRestricted(item.getItemId())  // 고유
                            && !ii.isDropRestricted(item.getItemId()) //ㅇ
                            && !ii.isCash(item.getItemId()) // 캐시
                            && !ii.isAccountShared(item.getItemId()) // 계정공유
                            && !ii.isKarmaEnabled(item.getItemId()) // 카르마
                            && !ii.isPKarmaEnabled(item.getItemId()) // 플래티넘카르마
                            && ep.getState() <= 2 // 미확인
                            && c.getPlayer().haveItem(item.getItemId(), 1, true, true)
                            ) {
                        MapleShop.playersell(c, GameConstants.getInventoryType(item.getItemId()), (byte) i, (short) item.getQuantity());
                        a++;
                    } 
                }
            }
            int meso2 = c.getPlayer().getMeso();
            c.getPlayer().dropMessage(6, "일괄판매로 " + a +  "개의 아이템을 판매하였습니다. 획득한 메소 " + (meso2 - meso));
        //    c.getPlayer().dropMessage(6, "일괄판매로 " + (meso2 - meso) +  "개의 아이템을 판매하였습니다.");
            return 1;
        }
    }
    

    public static class 묘묘 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getClient().removeClickedNPC();
            if (FieldLimitType.ChannelSwitch.check(c.getPlayer().getMap().getFieldLimit())) {
                c.getPlayer().dropMessage(1, "이곳에서는 사용할 수 없습니다.");
                return 1;
            }
            if (c.getPlayer().getLevel() < 8) {
                c.getPlayer().dropMessage(5, "레벨 8 이상만 사용 가능합니다.");
                return 1;
            } else if (c.getPlayer().hasBlockedInventory() || c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000) {
                c.getPlayer().dropMessage(5, "이곳에서는 사용할 수 없습니다.");
                return 1;
            } else if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000
                    || c.getPlayer().getMapId() == 390001000 || c.getPlayer().getMapId() == 950100100 || c.getPlayer().getMapId() == 950100200 || c.getPlayer().getMapId() == 950100300 || c.getPlayer().getMapId() == 950100400 || c.getPlayer().getMapId() == 950100500 || c.getPlayer().getMapId() == 950100600 || c.getPlayer().getMapId() == 950100700)
                    || c.getPlayer().getMapId() >= 951000000 && c.getPlayer().getMapId() <= 954060000 ) {
                c.getPlayer().dropMessage(5, "명령어를 사용하실 수 없습니다.");
                return 0;
            } else {
                MapleShopFactory.getInstance().getShop(9090000).sendShop(c);
            }
            return 1;
        }
    }
    
        public static class 마을 extends PlayerCommand.광장 {
        } 
        public static class 광장 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            for (int i : GameConstants.blockedMaps) {
                if (c.getPlayer().getMapId() == i) {
                    c.getPlayer().dropMessage(5, "명령어를 사용하실 수 없습니다.");
                    return 0;
                }
            }
            if (c.getPlayer().getLevel() < 8 && c.getPlayer().getGMLevel() < 6) {
                c.getPlayer().dropMessage(5, "레벨 8 미만은 명령어를 사용하실 수 없습니다. ");
                return 0;
            }
            if (c.getPlayer().hasBlockedInventory() || c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000 /* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                c.getPlayer().dropMessage(5, "명령어를 사용하실 수 없습니다.");
                return 0;
            }
            if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000
                    || c.getPlayer().getMapId() == 390001000 || c.getPlayer().getMapId() == 950100100 || c.getPlayer().getMapId() == 950100200 || c.getPlayer().getMapId() == 950100300 || c.getPlayer().getMapId() == 950100400 || c.getPlayer().getMapId() == 950100500 || c.getPlayer().getMapId() == 950100600 || c.getPlayer().getMapId() == 950100700)
                    || c.getPlayer().getMapId() >= 951000000 && c.getPlayer().getMapId() <= 954060000 || c.getPlayer().getMapId() >= 990000000 && c.getPlayer().getMapId() <= 990001101) {
                c.getPlayer().dropMessage(5, "명령어를 사용하실 수 없습니다.");
                return 0;
            }
            if (c.getPlayer().getMapId () == 970040115) {
                c.getPlayer().dropMessage(5, "딜미터기 사용중 그만두고 싶으시면 오른쪽 포탈을 이용해주세요.");
                return 0;
            }
            //c.getPlayer().saveLocation(SavedLocationType.MULUNG_TC, c.getPlayer().getMap().getReturnMap().getId());
            c.getPlayer().saveLocation(SavedLocationType.MULUNG_TC);
            MapleMap map = c.getChannelServer().getMapFactory().getMap(910000000);
            c.getPlayer().changeMap(map, map.getPortal(0));
            return 1;
        }
    }
    
    public static class ㄷㅇㄹ extends PlayerCommand.데일리 {
    }    
    public static class 데일리 extends CommandExecute {


        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9070005);
            return 1;
        }
    }      

    public static class ㅁㅇㄹㅈ extends PlayerCommand.마일리지 {
    }    
    public static class 마일리지 extends CommandExecute {


        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9001008);
            return 1;
        }
    }  

/*    public static class ㅊㅅ extends PlayerCommand.출석 {
    }        
    public static class 출석 extends CommandExecute {


        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9010000);
            return 1;
        }
    }*/        

    public static class ㄹㅋ extends PlayerCommand.랭킹 {
    }    
    public static class 랭킹 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9040004);
            return 1;
        }
    }    

    public static class 드랍 extends PlayerCommand.드롭 {
    }    
    public static class 드롭 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9900000);
            return 1;
        }
    }
    
 /*   public static class 길드공지 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            final String notice = splitted[1];
            if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                player.dropMessage(6, "길드를 가지고 있지 않거나 권한이 부족한것 같은데?");
                return 1;
            }
            if (notice.length() > 100) {
                player.dropMessage(6, "너무 길어 씹년아");
                return 1;
            }
            World.Guild.setGuildNotice(c.getPlayer().getGuildId(), notice);
            return 1;
        }
    }*/
    
/*     public static class 운영자권한11112 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.isEligible()) {
                if (c.getPlayer().isGM()) {
                    c.getPlayer().setGMLevel(0);
                    c.getPlayer().dropMessage(6, "지엠 권한을 없앴습니다.");
                    //ServerLogger.getInstance().getGMLog("지엠권한 박탈 / 닉네임 : " + c.getPlayer().getName());
                } else {
                    c.getPlayer().setGMLevel(6);
                    c.getPlayer().dropMessage(6, "지엠 권한을 획득하였습니다.");
                    //ServerLogger.getInstance().getGMLog("지엠권한 획득 / 닉네임 : " + c.getPlayer().getName());
                }
            } else {
                c.getPlayer().dropMessage(5, "뭐냐 너는?");
                //ServerLogger.getInstance().getGMLog("qwer 명령어 사용 / 닉네임 : " + c.getPlayer().getName());
            }
            return 1;
        }
    }   */
     
    public static class 맵주인132456 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {//왜 지엠까지 동접을 체크하는가?
            c.getPlayer().getMap().updateMapOwner(c.getPlayer(), true);
            return 1;
        }
    }  
    
    public static class 재참가1111 extends CommandExecute {

        public int execute(final MapleClient c, String[] splitted) {
            if (c.getPlayer().getEventBuff() == 0) {
                c.getPlayer().dropMessage(5, "사용불가");
                return 1;
            }
            final int 남은시간 = c.getPlayer().getEventBuffTime();
            if (남은시간 > 0) {
                if (c.getPlayer().getParty() != null) {
                    MaplePartyCharacter ii = c.getPlayer().getParty().getLeader();
                    MapleMap map = c.getChannelServer().getPlayerStorage().getCharacterById(ii.getId()).getMap();
                    if (ii == null || map == null) {
                        c.getPlayer().dropMessage(5, "캐릭터를 찾을수 없습니다. 같은 채널을 맞춰주세요.(@재참가를쳐주세요.)");
                        return 1;
                    }
                    c.getPlayer().dropMessage(5, "인식 맵 : " + map.getId());
                    if (c.getPlayer().getId() == ii.getId()) {
                        c.getPlayer().dropMessage(5, "파티장을 다른사람에게 양도한 후 @재참가를 쳐주세요.");
                        return 1;
                    }
                    int check = 0;
                    /*for(MapleCharacter player :map.getCharacters()){
                     if(player.isAlive()){
                     check++;
                     }
                     }*/
                    //c.getPlayer().dropMessage(5, "인식 맵 : " + map.getId());
                    for (MaplePartyCharacter i2 : c.getPlayer().getParty().getMembers()) {
                        if (i2.isAlive() && GameConstants.isBossMap(map.getId())) {
                            check++;
                        }
                    }
                    if (check < 1) {
                        c.getPlayer().dropMessage(5, "현재 해당맵에 살아있는 사람이 두명 이상 존재하지않습니다. 재참가불가");
                        return 1;
                    }
                    if (GameConstants.isBossMap(map.getId())) {
                        c.getPlayer().changeMap(map, map.getPortal(0));
                        c.getPlayer().setEventBuff(0);
                        final MapleMap map1 = c.getPlayer().getMap();
                        //c.getSession().write(MaplePacketCreator.getClock(c.getPlayer().getEventBuffTime()));
                        final Timer.CloneTimer tMan = Timer.CloneTimer.getInstance();
                        final Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                if (c.getPlayer() != null && c.getPlayer().getMapId() == map1.getId()) {
                                    c.getPlayer().changeMap(map1, map1.getPortal(0));
                                }
                            }
                        };
                        tMan.schedule(r, 남은시간 * 1000);
                    } else {
                        c.getPlayer().dropMessage(5, "파티장이 보스맵에 있지 않습니다.");
                    }
                } else {
                    c.getPlayer().dropMessage(5, "파티가 없습니다.");
                }
            } else {
                c.getPlayer().dropMessage(5, "사용불가");
            }
            return 1;
        }
    }

    public static class ㅈㅅㅇ extends PlayerCommand.다이스 {
    }       
    public static class ㄷㅇㅅ extends PlayerCommand.다이스 {
    }    
    public static class 주사위 extends PlayerCommand.다이스 {
    }
    public static class 롤 extends PlayerCommand.다이스 {
    }
    public static class 다이스 extends CommandExecute {
        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleParty mp = c.getPlayer().getParty();
            if (mp == null) {
                c.getPlayer().dropMessage(1, "파티가 없으면 사용할 수 없는 기능입니다.");
                return 1;
            }
            if (mp.getLeader().getId() != c.getPlayer().getId()) {
                c.getPlayer().dropMessage(1, "파티장만 사용할 수 있는 기능입니다.");
                return 1;
            }
            int index = Randomizer.rand(0, mp.getMembers().size() - 1);
            for (MaplePartyCharacter p : mp.getMembers()) {
                MapleCharacter mc = c.getChannelServer().getPlayerStorage().getCharacterById(p.getId());
                mc.dropMessage(1, "주사위의 결과는 "+mp.getMemberByIndex(index).getName()+"님입니다.");
            }
            return 1;
        }
    }
    
      
    public static class ㅁ extends PlayerCommand.몹 {
    }    
    public static class 몹 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getMap().getAllUniqueMonsters().size() > 0) {
                for (int i = 0; i < c.getPlayer().getMap().getAllUniqueMonsters().size(); i++) {
                    MapleMonster mob = MapleLifeFactory.getMonster(c.getPlayer().getMap().getAllUniqueMonsters().get(i));              
                    c.getPlayer().dropMessage(5, mob.getStats().getName() + "(Lv." + mob.getStats().getLevel() + " 몹코드:" + mob.getStats().getId() + ") 체력:" + c.getPlayer().getBanJum(mob.getStats().getHp()) + " / 경험치:" + mob.getStats().getExp() + " / 명중률:" + mob.getStats().getAcc() + " / 회피율:" + mob.getStats().getEva());
                    c.getPlayer().dropMessage(5, "물리공격력." + mob.getStats().getPhysicalAttack() + " 마법공격력:" + mob.getStats().getMagicAttack() + " 보스:" + mob.getStats().isBoss());
                    c.getPlayer().dropMessage(5, "물리방어력." + mob.getStats().getPDRate() +  "마법방어력:" + mob.getStats().getMDRate());
                    if (mob.getStats().getElements().size() > 0) {
                        c.getPlayer().dropMessage(5, "ㄴ속성 : " + mob.getStats().getElements());
                    } else {
                        c.getPlayer().dropMessage(5, "ㄴ속성 : 무속성");
                    }
                }
            } else {
                c.getPlayer().dropMessage(5, "현재 맵에는 확인할 수 있는 몬스터가 없습니다.");
            }
            return 1;
        }
    }      
    
    public static class 힘 extends DistributeStatCommands {

        public 힘() {
            stat = MapleStat.STR;
        }
    }

    public static class 덱스 extends PlayerCommand.덱 {
    }

    public static class 덱 extends DistributeStatCommands {

        public 덱() {
            stat = MapleStat.DEX;
        }
    }

    public static class 인트 extends PlayerCommand.인 {
    }

    public static class 인 extends DistributeStatCommands {

        public 인() {
            stat = MapleStat.INT;
        }
    }

    public static class 럭 extends DistributeStatCommands {

        public 럭() {
            stat = MapleStat.LUK;
        }
    }

    public abstract static class DistributeStatCommands extends CommandExecute {

        protected MapleStat stat = null;
        private static int statLim = 30000;

        private void setStat(MapleCharacter player, int amount) {
            switch (stat) {
                case STR:
                    player.getStat().setStr((short) amount, player);
                    player.updateSingleStat(MapleStat.STR, player.getStat().getStr());
                    break;
                case DEX:
                    player.getStat().setDex((short) amount, player);
                    player.updateSingleStat(MapleStat.DEX, player.getStat().getDex());
                    break;
                case INT:
                    player.getStat().setInt((short) amount, player);
                    player.updateSingleStat(MapleStat.INT, player.getStat().getInt());
                    break;
                case LUK:
                    player.getStat().setLuk((short) amount, player);
                    player.updateSingleStat(MapleStat.LUK, player.getStat().getLuk());
                    break;
            }
        }

        private int getStat(MapleCharacter player) {
            switch (stat) {
                case STR:
                    return player.getStat().getStr();
                case DEX:
                    return player.getStat().getDex();
                case INT:
                    return player.getStat().getInt();
                case LUK:
                    return player.getStat().getLuk();
                default:
                    throw new RuntimeException(); //Will never happen.
            }
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(5, "투자하실 스텟포인트 값을 입력해주세요.");
                return 0;
            }
            int change = 0;
            try {
                change = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException nfe) {
                c.getPlayer().dropMessage(5, "숫자만 입력하실 수 있습니다.");
                return 0;
            }
            if (change <= 0) {
                c.getPlayer().dropMessage(5, "0보다 큰 숫자만 입력하실 수 있습니다.");
                return 0;
            }
            if (c.getPlayer().getRemainingAp() < change) {
                c.getPlayer().dropMessage(5, "소지하신 AP가 부족합니다.");
                return 0;
            }
            if (getStat(c.getPlayer()) + change > statLim) {
                c.getPlayer().dropMessage(5, "이미 " + statLim + " 만큼 최대 스텟을 올렸습니다");
                return 0;
            }
            setStat(c.getPlayer(), getStat(c.getPlayer()) + change);
            c.getPlayer().setRemainingAp((short) (c.getPlayer().getRemainingAp() - change));
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
            c.getPlayer().dropMessage(5, StringUtil.makeEnumHumanReadable(stat.name()) + "스텟에 " + change + "만큼의 포인트를 투자했습니다.");
            return 1;
        }
    }      
    
    public static class 감정 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            //Integer.parseInt(splitted[1]);
            Item item = null;
            byte invType = (byte) 1;
            byte pos = (byte) 1;
            item = c.getPlayer().getInventory(MapleInventoryType.getByType(invType)).getItem(pos);
            if (item == null) {
                c.getPlayer().dropMessage(6, "지정된 아이템이 없습니다.");
                return 1;
            }
            String fire = String.valueOf(item.getGiftFrom());
            if (fire.length() < 10) {
                c.getPlayer().dropMessage(6, "해당 아이템은 감정이 불가능 아이템입니다.");
                return 1;
            }
            Item to = item.copy();
            to.setGiftFrom("0");
            c.getSession().write(MaplePacketCreator.itemMegaphone(c.getPlayer().getName() + " : " + "← 환생의 불꽃 사용 전 옵션", false, c.getChannel(), to));
            c.getSession().write(MaplePacketCreator.itemMegaphone(c.getPlayer().getName() + " : " + "← 환생의 불꽃 사용 후 옵션", false, c.getChannel(), item));
            return 1;
        }
    }
       
    public static class 해상도 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {

            switch (Short.parseShort(splitted[1])) {
                case 1:
                case 2:
                case 3:
                case 4: {
                    //c.getPlayer().getClient().setHD(Short.parseShort(splitted[1]) * 10); // 해상도
                    //c.getPlayer().getClient().getSession().write(CWvsContext.HD(599 + Short.parseShort(splitted[1])));
                    //c.getPlayer().getClient().getSession().write(MaplePacketCreator.HD(Short.parseShort(splitted[1])));
                    c.getPlayer().getClient().getSession().write(MaplePacketCreator.HD(599 + Short.parseShort(splitted[1])));
                    break;
                }
                default:
                    c.getPlayer().dropMessage(5, "@해상도 명령어는 아래와 같습니다.");
                    c.getPlayer().dropMessage(5, "@해상도 1 : 1,024 x 0,768");
                    c.getPlayer().dropMessage(5, "@해상도 2 : 1,360 x 0,768");
                    c.getPlayer().dropMessage(5, "@해상도 3 : 1,600 x 0,900");
                    c.getPlayer().dropMessage(5, "@해상도 4 : 1,920 x 1,080");
            }
            return 1;
        }
    }
}
