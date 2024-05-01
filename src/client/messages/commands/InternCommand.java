package client.messages.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.ServerConstants.PlayerGMRank;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import provider.MapleDataTool;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import server.ItemInformation;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.StringUtil;

public class InternCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.INTERN;
    }
    
    public static class 명령어 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "!모두저장 - 현재 서버의 정보를 저장합니다.");
            c.getPlayer().dropMessage(5, "!온라인 - 현재 서버에 접속 중인 유저를 표시됩니다.");
            c.getPlayer().dropMessage(5, "!레벨 - 원하는 레벨으로 설정할 수 있습니다.");
            c.getPlayer().dropMessage(5, "!찾기 - 맵, 몬스터, 엔피시, 퀘스트, 스킬 검색합니다.");
            c.getPlayer().dropMessage(5, "!맵 - 원하는 맵 코드를 입력하면 이동 가능합니다.");
            c.getPlayer().dropMessage(5, "!이동 - 원하는 맵 이름을 입력하면 이동 가능합니다.");            
            c.getPlayer().dropMessage(5, "!공지 - 접속 중인 유저들에게 알릴 수 있습니다.");
            c.getPlayer().dropMessage(5, "!엔피시 - 일시적인 엔피시를 불러올 수 있습니다.");
            c.getPlayer().dropMessage(5, "!엔피시삭제 - 일시적으로 불려온 엔피시를 삭제합니다.");
            //c.getPlayer().dropMessage(5, "!아이템 - 아이템 코드 입력하면 얻을 수 있습니다.");
            //c.getPlayer().dropMessage(5, "!드롭 - 아이템 코드 입력하면 드롭됩니다.");
            c.getPlayer().dropMessage(5, "!스킬마스터 - 모든 직업 스킬을 마스터합니다.");
            c.getPlayer().dropMessage(5, "!부분스킬마스터 - 현재 전직한 직업 스킬만 마스터합니다.");            
            //c.getPlayer().dropMessage(5, "!캐시 - 원하는 캐시량을 입력하세요.");
            //c.getPlayer().dropMessage(5, "!메소 - 풀메소로 지급하게 해줍니다.");
            //c.getPlayer().dropMessage(5, "!스폰 - 원하는 몬스터 코드를 입력하면 스폰됩니다.");
            //c.getPlayer().dropMessage(5, "!모든보너스 - 레인보우, PC방, 붐업 보너스 활성화됩니다.");
            return 1;
        }
    }    

    public static class 맵 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel()) {
                if (splitted.length == 2) {
                    c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getTruePosition()));
                } else {
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    if (target == null) {
                        c.getPlayer().dropMessage(6, "현재 그 맵으로 이동을 할 수 없습니다.");
                        return 0;
                    }
                    MaplePortal targetPortal = null;
                    if (splitted.length > 3) {
                        try {
                            targetPortal = target.getPortal(Integer.parseInt(splitted[3]));
                        } catch (IndexOutOfBoundsException e) {
                            // noop, assume the gm didn't know how many portals there are
                            c.getPlayer().dropMessage(5, "Invalid portal selected.");
                        } catch (NumberFormatException a) {
                            // noop, assume that the gm is drunk
                        }
                    }
                    if (targetPortal == null) {
                        targetPortal = target.getPortal(0);
                    }
                    victim.changeMap(target, targetPortal);
                }
            } else {
                try {
                    victim = c.getPlayer();
                    int ch = World.Find.findChannel(splitted[1]);
                    if (ch < 0) {
                        MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                        if (target == null) {
                            c.getPlayer().dropMessage(6, "현재 그 맵으로 이동을 할 수 없습니다.");
                            return 0;
                        }
                        MaplePortal targetPortal = null;
                        if (splitted.length > 2) {
                            try {
                                targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                            } catch (IndexOutOfBoundsException e) {
                                // noop, assume the gm didn't know how many portals there are
                                c.getPlayer().dropMessage(5, "Invalid portal selected.");
                            } catch (NumberFormatException a) {
                                // noop, assume that the gm is drunk
                            }
                        }
                        if (targetPortal == null) {
                            targetPortal = target.getPortal(0);
                        }
                        c.getPlayer().changeMap(target, targetPortal);
                    } else {
                        victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                        c.getPlayer().dropMessage(6, "현재 채널을 바꾸는 중입니다. 잠시후 시도해 주세요.");
                        if (victim.getMapId() != c.getPlayer().getMapId()) {
                            final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(victim.getMapId());
                            c.getPlayer().changeMap(mapp, mapp.findClosestPortal(victim.getTruePosition()));
                        }
                        c.getPlayer().changeChannel(ch);
                    }
                } catch (Exception e) {
                    c.getPlayer().dropMessage(6, "현재 갈 수 없는 맵의 코드를 입력하셨습니다.  " + e.getMessage());
                    return 0;
                }
            }
            return 1;
        }
    }

    public static class ID extends 검색 {
    }

    public static class LookUp extends 검색 {
    }

    public static class 찾기 extends 검색 {
    }

    public static class 검색 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length == 1) {
                c.getPlayer().dropMessage(6, splitted[0] + ": <엔피시> <몹> <아이템> <맵> <스킬> <퀘스트>");
            } else if (splitted.length == 2) {
                c.getPlayer().dropMessage(6, "제공된 코드으로만 입력이 가능합니다. 현재 가능한 코드는 <엔피시> <몹> <아이템> <맵> <스킬> <퀘스트> 입니다.");
            } else {
                String type = splitted[1];
                String search = StringUtil.joinStringFrom(splitted, 2);
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/" + "String.wz"));
                c.getPlayer().dropMessage(6, "<< 종류 : " + type + " | 검색어 : " + search + ">>");

                if (type.equalsIgnoreCase("엔피시")) {
                    List<String> retNpcs = new ArrayList<String>();
                    data = dataProvider.getData("Npc.img");
                    List<Pair<Integer, String>> npcPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData npcIdData : data.getChildren()) {
                        npcPairList.add(new Pair<Integer, String>(Integer.parseInt(npcIdData.getName()), MapleDataTool.getString(npcIdData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> npcPair : npcPairList) {
                        if (npcPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retNpcs.add(npcPair.getLeft() + " - " + npcPair.getRight());
                        }
                    }
                    if (retNpcs != null && retNpcs.size() > 0) {
                        for (String singleRetNpc : retNpcs) {
                            c.getPlayer().dropMessage(6, singleRetNpc);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 엔피시가 없습니다.");
                    }

                } else if (type.equalsIgnoreCase("맵")) {
                    List<String> retMaps = new ArrayList<String>();
                    data = dataProvider.getData("Map.img");
                    List<Pair<Integer, String>> mapPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData mapAreaData : data.getChildren()) {
                        for (MapleData mapIdData : mapAreaData.getChildren()) {
                            mapPairList.add(new Pair<Integer, String>(Integer.parseInt(mapIdData.getName()), MapleDataTool.getString(mapIdData.getChildByPath("streetName"), "NO-NAME") + " - " + MapleDataTool.getString(mapIdData.getChildByPath("mapName"), "NO-NAME")));
                        }
                    }
                    for (Pair<Integer, String> mapPair : mapPairList) {
                        if (mapPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMaps.add(mapPair.getLeft() + " - " + mapPair.getRight());
                        }
                    }
                    if (retMaps != null && retMaps.size() > 0) {
                        for (String singleRetMap : retMaps) {
                            c.getPlayer().dropMessage(6, singleRetMap);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 맵이 없습니다.");
                    }
                } else if (type.equalsIgnoreCase("몹")) {
                    List<String> retMobs = new ArrayList<String>();
                    data = dataProvider.getData("Mob.img");
                    List<Pair<Integer, String>> mobPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData mobIdData : data.getChildren()) {
                        mobPairList.add(new Pair<Integer, String>(Integer.parseInt(mobIdData.getName()), MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> mobPair : mobPairList) {
                        if (mobPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMobs.add(mobPair.getLeft() + " - " + mobPair.getRight());
                        }
                    }
                    if (retMobs != null && retMobs.size() > 0) {
                        for (String singleRetMob : retMobs) {
                            c.getPlayer().dropMessage(6, singleRetMob);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 아이템이 없습니다.");
                    }
                } else if (type.equalsIgnoreCase("아이템")) {
                    List<String> retItems = new ArrayList<String>();
                    for (ItemInformation itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (itemPair != null && itemPair.name != null && itemPair.name.toLowerCase().contains(search.toLowerCase())) {
                            retItems.add(itemPair.itemId + " - " + itemPair.name);
                        }
                    }
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            c.getPlayer().dropMessage(6, singleRetItem);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 아이템이 없습니다.");
                    }
                } else if (type.equalsIgnoreCase("퀘스트")) {
                    List<String> retItems = new ArrayList<String>();
                    for (MapleQuest itemPair : MapleQuest.getAllInstances()) {
                        if (itemPair.getName().length() > 0 && itemPair.getName().toLowerCase().contains(search.toLowerCase())) {
                            retItems.add(itemPair.getId() + " - " + itemPair.getName());
                        }
                    }
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            c.getPlayer().dropMessage(6, singleRetItem);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 스킬이 없습니다.");
                    }
                } else if (type.equalsIgnoreCase("스킬")) {
                    List<String> retSkills = new ArrayList<String>();
                    for (Skill skil : SkillFactory.getAllSkills()) {
                        if (skil.getName() != null && skil.getName().toLowerCase().contains(search.toLowerCase())) {
                            retSkills.add(skil.getId() + " - " + skil.getName());
                        }
                    }
                    if (retSkills != null && retSkills.size() > 0) {
                        for (String singleRetSkill : retSkills) {
                            c.getPlayer().dropMessage(6, singleRetSkill);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 스킬이 없습니다.");
                    }
                } else if (type.equalsIgnoreCase("헤어")) {
                    List<String> retHair = new ArrayList<String>();
                    List<Pair<Integer, String>> hairPairList = new LinkedList<Pair<Integer, String>>();
                    MapleDataProvider hairstring = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
                    MapleData hair = hairstring.getData("Eqp.img");
                    for (MapleData hairData : hair.getChildByPath("Eqp").getChildByPath("Hair")) {
                        hairPairList.add(new Pair<Integer, String>(Integer.parseInt(hairData.getName()), MapleDataTool.getString(hairData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> hairPair : hairPairList) {
                        if (hairPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retHair.add(hairPair.getLeft() + " - " + hairPair.getRight());
                        }
                    }
                    if (retHair != null && retHair.size() > 0) {
                        for (String singleRetHair : retHair) {
                            c.getPlayer().dropMessage(6, singleRetHair);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 헤어가 없습니다.");
                    }
                } else if (type.equalsIgnoreCase("얼굴") || type.equalsIgnoreCase("성형")) {
                    List<String> retface = new ArrayList<String>();
                    List<Pair<Integer, String>> facePairList = new LinkedList<Pair<Integer, String>>();
                    MapleDataProvider facestring = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
                    MapleData face = facestring.getData("Eqp.img");
                    for (MapleData faceData : face.getChildByPath("Eqp").getChildByPath("Face")) {
                        facePairList.add(new Pair<Integer, String>(Integer.parseInt(faceData.getName()), MapleDataTool.getString(faceData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> facePair : facePairList) {
                        if (facePair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retface.add(facePair.getLeft() + " - " + facePair.getRight());
                        }
                    }
                    if (retface != null && retface.size() > 0) {
                        for (String singleRetface : retface) {
                            c.getPlayer().dropMessage(6, singleRetface);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "검색된 성형이 없습니다.");
                    }
                } else {
                    c.getPlayer().dropMessage(6, "해당 검색은 처리할 수 없습니다.");
                }
            }
            return 0;
        }
    }
    
    public static class 이동 extends CommandExecute {

        private static final HashMap<String, Integer> gotomaps = new HashMap<String, Integer>();

        static {
            //KMS
            gotomaps.put("사우스페리", 2000000);
            gotomaps.put("암허스트", 1000000);         
            gotomaps.put("헤네", 100000000);
            gotomaps.put("엘리니아", 101000000);
            gotomaps.put("페리온", 102000000);
            gotomaps.put("커닝", 103000000);
            gotomaps.put("리스항구", 104000000);
            gotomaps.put("슬피", 105040300);
            gotomaps.put("플로리나비치", 120030000);
            gotomaps.put("노틸러스", 120000000);
            gotomaps.put("에레브", 130000000);
            gotomaps.put("리엔", 140000000);
            gotomaps.put("올비", 200000000);
            gotomaps.put("엘나스", 211000000);
            gotomaps.put("루디", 220000000);
            gotomaps.put("지방", 221000000);
            gotomaps.put("아랫마을", 222000000);
            gotomaps.put("아쿠아", 230000000);
            gotomaps.put("리프레", 240000000);
            gotomaps.put("무릉", 250000000);
            gotomaps.put("백초", 251000000);
            gotomaps.put("아리안트", 260000000);
            gotomaps.put("마가티아", 261000000);
            gotomaps.put("시신전", 270000000);
            gotomaps.put("엘린숲", 300000000);
            gotomaps.put("에델슈타인", 310000000);            
            gotomaps.put("황사", 950100000);

            //해외 MS
            gotomaps.put("싱가포르", 540000000);
            gotomaps.put("보트키타운", 541000000);
            gotomaps.put("태국", 500000000);
            gotomaps.put("말레이시아", 550000000);
            gotomaps.put("캄풍마을", 551000000);
            gotomaps.put("뉴리프시티", 600000000);
            gotomaps.put("웨딩빌리지", 680000000);
            gotomaps.put("중국", 701000000);
            gotomaps.put("서문정", 740000000);
            gotomaps.put("야시장", 741000000);
            gotomaps.put("일본", 800000000);
            gotomaps.put("쇼와", 801000000);

            //보스
            gotomaps.put("피아누스", 230040420);
            gotomaps.put("파풀", 220080001);
            gotomaps.put("그리프", 240020101);
            gotomaps.put("마뇽", 240020401);
            gotomaps.put("텔", 240060200);
            gotomaps.put("카텔", 240060201);
            gotomaps.put("핑크빈", 270050100);
            gotomaps.put("쿰", 280030000);
            gotomaps.put("카쿰", 280030001);

            //특수 맵
            gotomaps.put("OX퀴즈", 109020001);
            gotomaps.put("올라올라", 109030001);
            gotomaps.put("고지를향해서", 109040000);
            gotomaps.put("눈덩이굴리기", 109060000);
            gotomaps.put("영자", 180000000);
            gotomaps.put("게임방", 193000000);
            gotomaps.put("행복한마을", 209000000);
            gotomaps.put("코크", 219000000);
            //gotomaps.put("크림슨우드파퀘", 610030000);
            gotomaps.put("자시", 910000000);
            gotomaps.put("길대", 990000000);
            gotomaps.put("낚시", 741000200);    
            gotomaps.put("마빌", 910001000); 
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "사용법 : !이동 <맵 이름>");
            } else {
                if (gotomaps.containsKey(splitted[1])) {
                    MapleMap target = c.getChannelServer().getMapFactory().getMap(gotomaps.get(splitted[1]));
                    if (target == null) {
                        c.getPlayer().dropMessage(6, "존재하지 않는 맵입니다.");
                        return 0;
                    }
                    MaplePortal targetPortal = target.getPortal(0);
                    c.getPlayer().changeMap(target, targetPortal);
                } else {
                    if (splitted[1].equals("맵목록")) {
                        c.getPlayer().dropMessage(6, "!이동 <맵 이름>을 사용해 주세요. 지원되는 맵은 다음과 같습니다.");
                        StringBuilder sb = new StringBuilder();
                        for (String s : gotomaps.keySet()) {
                            sb.append(s).append(", ");
                        }
                        c.getPlayer().dropMessage(6, sb.substring(0, sb.length() - 2));
                    } else {
                        c.getPlayer().dropMessage(6, "입력 형식이 잘못되었습니다. !이동 <맵 이름>을 사용해 주세요. 지원되는 맵을 확인하시려면 '!이동 맵목록'을 사용해 주세요.");
                    }
                }
            }
            return 1;
        }
    }
    
    public static class 공지 extends CommandExecute {

        protected static int getNoticeType(String typestring) {
            if (typestring.equals("공")) {
                return 0;
            } else if (typestring.equals("팝")) {
                return 1;
            } else if (typestring.equals("갈공")) {
                return 5;
            } else if (typestring.equals("갈")) {
                return 5;
            } else if (typestring.equals("파")) {
                return 6;
            }
            return -1;
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int joinmod = 1;
            int range = -1;
            if (splitted[1].equals("맵")) {
                range = 0;
            } else if (splitted[1].equals("채널")) {
                range = 1;
            } else if (splitted[1].equals("월드")) {
                range = 2;
            }

            int tfrom = 2;
            if (range == -1) {
                range = 2;
                tfrom = 1;
            }
            int type = getNoticeType(splitted[tfrom]);
            if (type == -1) {
                type = 0;
                joinmod = 0;
            }
            StringBuilder sb = new StringBuilder();
            if (splitted[tfrom].equals("갈공")) {
                sb.append("[공지사항]");
            } else {
                sb.append("");
            }
            joinmod += tfrom;
            sb.append(StringUtil.joinStringFrom(splitted, joinmod));

            byte[] packet = MaplePacketCreator.serverNotice(type, sb.toString());
            if (range == 0) {
                c.getPlayer().getMap().broadcastMessage(packet);
            } else if (range == 1) {
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else if (range == 2) {
                World.Broadcast.broadcastMessage(packet);
            }
            return 1;
        }
    }

    public static class 노란색공지 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int range = -1;
            if (splitted[1].equals("맵")) {
                range = 0;
            } else if (splitted[1].equals("채널")) {
                range = 1;
            } else if (splitted[1].equals("월드")) {
                range = 2;
            }
            if (range == -1) {
                range = 2;
            }
            byte[] packet = MaplePacketCreator.yellowChat((splitted[0].equals("!y") ? ("[" + c.getPlayer().getName() + "] ") : "") + StringUtil.joinStringFrom(splitted, 2));
            if (range == 0) {
                c.getPlayer().getMap().broadcastMessage(packet);
            } else if (range == 1) {
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else if (range == 2) {
                World.Broadcast.broadcastMessage(packet);
            }
            return 1;
        }
    }

    public static class Y extends 노란색공지 {
    }    
    
    public static class 근처몬스터 extends InternCommand.몹체크 {
    }    
    public static class 몹체크 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getMap().getAllUniqueMonsters().size() > 0) {
                for (int i = 0; i < c.getPlayer().getMap().getAllUniqueMonsters().size(); i++) {
                    MapleMonster mob = MapleLifeFactory.getMonster(c.getPlayer().getMap().getAllUniqueMonsters().get(i));
                    c.getPlayer().dropMessage(5, mob.getStats().getName() + " (Lv. " + mob.getStats().getLevel() + ") (몬스터 코드 : " + mob.getStats().getId() + ")");
                    c.getPlayer().dropMessage(6, "체력 : " + c.getPlayer().getBanJum(mob.getStats().getHp()) + " / 마나 : " + c.getPlayer().getBanJum((long) mob.getStats().getMp()));
                    c.getPlayer().dropMessage(6, "PAD : " + mob.getStats().getPhysicalAttack() + " / MAD : " + mob.getStats().getMagicAttack());
                    c.getPlayer().dropMessage(6, "PDD : " + mob.getStats().getPDDamage() + " / MDD : " + mob.getStats().getMDDamage());
                    c.getPlayer().dropMessage(6, "EXP : " + mob.getStats().getExp() + " / ACC : " + mob.getStats().getAcc() + " / EVA " + mob.getStats().getEva() + " / PUSH " + mob.getStats().getPushed());
                    if (mob.getStats().getElements().size() > 0) {
                        c.getPlayer().dropMessage(6, "속성 : " + mob.getStats().getElements());
                    } else {
                        c.getPlayer().dropMessage(6, "무속성");
                    }
                }
            } else {
                c.getPlayer().dropMessage(5, "현재 맵에는 몬스터가 없습니다.");
            }
            return 1;
        }
    }    
 
    public static class 청소 extends InternCommand.RemoveDrops {
    }

    public static class 드랍삭제 extends InternCommand.RemoveDrops {
    }

    public static class RemoveDrops extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "아이템 " + c.getPlayer().getMap().getNumItems() + "개를 제거했습니다.");
            c.getPlayer().getMap().removeDrops();
            return 1;
        }
    }    

    public static class 현재맵 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "You are on map " + c.getPlayer().getMap().getId());
            return 1;
        }
    }    
    
    public static class 캐릭터정보 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            boolean isOnline = false;
            MapleCharacter player = null;

            for (int i = 1; i <= ChannelServer.getChannelCount(); i++) {
                for (MapleCharacter other : ChannelServer.getInstance(i).getPlayerStorage().getAllCharacters()) {
                    if (other != null && other.getName().equals(splitted[1])) {
                        other.saveToDB(false, false); //세이브
                        isOnline = true;
                        player = other;
                    }
                }
            }

            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            StringBuilder text = new StringBuilder().append("#b" + splitted[1] + " #k님의 캐릭터 정보입니다.\r\n\r\n");

            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM characters c INNER JOIN accounts a ON c.accountid = a.id WHERE c.name = ?");
                ps.setString(1, splitted[1]);
                rs = ps.executeQuery();

                if (rs.next()) {
                    text.append("#b캐릭터 ID : #k" + rs.getInt("c.id") + " #b어카운트 ID : #k" + rs.getInt("a.id") + "\r\n\r\n");
                    text.append("#e캐릭터 스탯#n\r\n#b힘 : #k" + rs.getInt("c.str") + " #b덱 : #k" + rs.getInt("c.dex") + " #b인 : #k" + rs.getInt("c.int") + " #b럭 : #k" + rs.getInt("c.luk") + "\r\n");
                    text.append("#b최대 HP : #k" + rs.getInt("c.maxhp") + " #b최대 MP : #k" + rs.getInt("c.maxmp") + "\r\n");
                    text.append("#b현재 HP : #k" + rs.getInt("c.hp") + " #b현재 MP : #k" + rs.getInt("c.mp") + "\r\n\r\n");
                    if (isOnline) {
                        text.append("#b토탈 공격력 : #k" + player.getStat().getTotalWatk() + " #b토탈 마력 : #k" + player.getStat().getTotalMagic() + "\r\n");
                        text.append("#b토탈 힘 : #k" + player.getStat().getTotalStr() + " #b토탈 덱 : #k" + player.getStat().getTotalDex() + "\r\n");
                        text.append("#b토탈 인 : #k" + player.getStat().getTotalInt() + " #b토탈 럭 : #k" + player.getStat().getTotalLuk() + "\r\n\r\n");
                        //text.append("#b스공 : #k" + (int) player.getStat().getCurrentMinBaseDamage() + "~" + (int) player.getStat().getCurrentMaxBaseDamage() + "\r\n\r\n");
                    }
                    text.append("#b직업 : #k" + c.getPlayer().getJobName(rs.getInt("c.job")) + " #d #b직업코드 : #k" + rs.getInt("c.job") + "\r\n");
                    text.append("#b레벨 : #k" + rs.getInt("c.level") + " #b경험치 : #k" + rs.getInt("c.exp") + "\r\n");
                    text.append("#b헤어 : #k" + rs.getInt("c.hair") + " #b성형 : #k" + rs.getInt("c.face") + "\r\n\r\n");
                    text.append("#b소지 중인 후원포인트 : #k" + rs.getInt("DonateCash") + "\r\n");
                    text.append("#b소지 중인 캐시 : #k" + rs.getInt("ACash") + "\r\n");

                    text.append("#b소지 중인 메소 : #k" + c.getPlayer().getBanJum((long) rs.getInt("c.meso")) + "\r\n\r\n");

                    text.append("#b현재 맵 : #k" + c.getChannelServer().getMapFactory().getMap(rs.getInt("c.map")).getStreetName() + "-" + c.getChannelServer().getMapFactory().getMap(rs.getInt("c.map")).getMapName() + " (" + rs.getInt("c.map") + ")\r\n");

                    if (isOnline) {
                        text.append("#b캐릭터 좌표 : #k (#dX : " + player.getPosition().getX() + " Y : " + player.getPosition().getY() + "#k)\r\n");
                    }

                    if (rs.getInt("c.gm") > 0) {
                        text.append("#bGM : #k" + "권한 있음 (" + rs.getInt("c.gm") + " 레벨)\r\n");
                    } else {
                        text.append("#bGM : #k권한 없음\r\n");
                    }

                    String guild = "";
                    if (rs.getInt("c.guildid") == 0) {
                        guild = "없음";
                    } else {
                        guild = World.Guild.getGuild(rs.getInt("c.guildid")).getName();
                    }
                    text.append("#b소속된 길드 : #k" + guild + "\r\n\r\n");

                    String connect = "";
                    if (rs.getInt("a.loggedin") == 0) {
                        connect = "#r오프라인";
                    } else {
                        connect = "#g온라인";
                    }
                    text.append("#b접속 현황 : #k" + connect + "\r\n");
                    text.append("#b계정 아이디 : #k" + rs.getString("a.name") + "\r\n");
                    //text.append("#b계정 비밀번호 : #k" + rs.getString("a.password") + "\r\n");//비밀번호는 의미가 없으니.. 주석
                    text.append("#b아이피 : #k" + rs.getString("SessionIP") + "\r\n\r\n");
                    text.append("#b마지막 접속 : #k" + rs.getString("lastlogin") + "\r\n");
                    text.append("#b아이디 생성 날짜 : #k" + rs.getString("createdat") + "\r\n");

                } else {
                    c.getPlayer().dropMessage(5, "[시스템] " + splitted[1] + " 닉네임을 가진 유저가 존재하지 않습니다.");
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception e) {

                }
            }
            c.getSession().write(MaplePacketCreator.getNPCTalk(9900000, (byte) 0, text.toString(), "00 00", (byte) 0));
            return 1;
        }
    } 
    
    public static class 캐릭터정보2 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final StringBuilder builder = new StringBuilder();
            final MapleCharacter other = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (other == null) {
                builder.append("존재하지 않는 캐릭터입니다.");
                c.getPlayer().dropMessage(6, builder.toString());
                return 0;
            }
            if (other.getClient().getLastPing() <= 0) {
                other.getClient().sendPing();
            }
            if (other.getGMLevel() > c.getPlayer().getGMLevel()) {
                c.getPlayer().dropMessage(6, "이 캐릭터의 정보를 볼 수 없습니다.");
                return 0;
            }
            builder.append(MapleClient.getLogMessage(other, ""));
            builder.append(" at ").append(other.getPosition().x);
            builder.append("/").append(other.getPosition().y);

            builder.append(" || HP : ");
            builder.append(other.getStat().getHp());
            builder.append(" /");
            builder.append(other.getStat().getCurrentMaxHp());

            builder.append(" || MP : ");
            builder.append(other.getStat().getMp());
            builder.append(" /");
            builder.append(other.getStat().getCurrentMaxMp());

            builder.append(" || 물리공격력 : ");
            builder.append(other.getStat().getTotalWatk());
            builder.append(" || 마법공격력 : ");
            builder.append(other.getStat().getTotalMagic());
//            builder.append(" || DAMAGE% : ");
//            builder.append(other.getStat().dam_r);
//            builder.append(" || BOSSDAMAGE% : ");
//            builder.append(other.getStat().bossdam_r);
            builder.append(" || STR : ");
            builder.append(other.getStat().getStr());
            builder.append(" || DEX : ");
            builder.append(other.getStat().getDex());
            builder.append(" || INT : ");
            builder.append(other.getStat().getInt());
            builder.append(" || LUK : ");
            builder.append(other.getStat().getLuk());

            builder.append(" || 총합 STR : ");
            builder.append(other.getStat().getTotalStr());
            builder.append(" || 총합 DEX : ");
            builder.append(other.getStat().getTotalDex());
            builder.append(" || 총합 INT : ");
            builder.append(other.getStat().getTotalInt());
            builder.append(" || 총합 LUK : ");
            builder.append(other.getStat().getTotalLuk());

            builder.append(" || EXP : ");
            builder.append(other.getExp());
            builder.append(" || 메소 : ");
            builder.append(other.getMeso());

            builder.append(" || party : ");
            builder.append(other.getParty() == null ? -1 : other.getParty().getId());

            builder.append(" || hasTrade : ");
            builder.append(other.getTrade() != null);
            builder.append(" || 딜레이 : ");
            builder.append(other.getClient().getLatency());
            builder.append(" || PING : ");
            builder.append(other.getClient().getLastPing());
            builder.append(" || PONG : ");
            builder.append(other.getClient().getLastPong());
            builder.append(" || 접속한 IP 주소 : ");

            other.getClient().DebugMessage(builder);

            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }
    }    
    
    public static class 온라인 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "CH." + c.getChannel() + "에 접속 중인 캐릭터:");
            c.getPlayer().dropMessage(5, c.getChannelServer().getPlayerStorage().getOnlinePlayers(true));
            return 1;
        }
    }

    public static class 연결 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            StringBuilder text = new StringBuilder();
            int count = 0, value = 0;
            for (int i = 1; i <= ChannelServer.getChannelCount(); i++) {
                if (i < 2) {
                    text.append(i + "채널");
                } else if (i == 2) {
                    text.append("20세이상");
                } else {
                    text.append((i - 1) + "채널");
                }
                for (MapleCharacter chr : ChannelServer.getInstance(i).getPlayerStorage().getAllCharacters()) {
                    if (!chr.isGM()) {
                        count++;
                        value++;
                        if (value == 1) {
                            text.append(" : " + chr.getName());
                        } else {
                            text.append(", " + chr.getName());
                        }
                    }
                }
                c.getPlayer().dropMessage(5, text.toString());
                text.setLength(0);
                value = 0;
            }
            c.getPlayer().dropMessage(5, "총 접속자 : " + count + "명");
            return 1;
        }
    }

    public static class 사냥동접 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            StringBuilder text = new StringBuilder();
            int count = 0, value = 0;
            for (int i = 1; i <= ChannelServer.getChannelCount(); i++) {
                if (i < 2) {
                    text.append(i + "채널");
                } else if (i == 2) {
                    text.append("20세이상");
                } else {
                    text.append((i - 1) + "채널");
                }
                for (MapleCharacter chr : ChannelServer.getInstance(i).getPlayerStorage().getAllCharacters()) {
                    if (!chr.isGM() && chr.getMap().getAllMonster().size() > 0) {
                        count++;
                        value++;
                        if (value == 1) {
                            text.append(" : " + chr.getName());
                        } else {
                            text.append(", " + chr.getName());
                        }
                    }
                }
                c.getPlayer().dropMessage(5, text.toString());
                text.setLength(0);
                value = 0;
            }
            c.getPlayer().dropMessage(5, "사냥 중인 유저 : " + count + "명");
            return 1;
        }
    }

    public static class 아이템소지 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3 || splitted[1] == null || splitted[1].equals("") || splitted[2] == null || splitted[2].equals("")) {
                c.getPlayer().dropMessage(6, "!아이템소지 닉네임 아이템코드");
                return 0;
            } else {
                int item = Integer.parseInt(splitted[2]);
                MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                int itemamount = chr.getItemQuantity(item, true);
                if (itemamount > 0) {
                    c.getPlayer().dropMessage(5, chr.getName() + "는 " + itemamount + " (" + item + ")를 소지중입니다.");
                } else {
                    c.getPlayer().dropMessage(5, chr.getName() + "는 (" + item + ")를 소지중이지 않습니다.");
                }
            }
            return 1;
        }
    }    
    
    public static class xy extends InternCommand.현재좌표 {
    }    

    public static class 현재좌표 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Point pos = c.getPlayer().getPosition();
            final String format = "[포지션] Map : %09d  X : %d  Y : %d  RX0 : %d  RX1 : %d  FH : %d";
            c.getPlayer().dropMessage(5, String.format(format, c.getPlayer().getMap().getId(), pos.x, pos.y, (pos.x - 50), (pos.x + 50), c.getPlayer().getFH()));
            return 1;
        }
    }   
    
    public static class 채팅금지 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "채팅금지 [닉네임] [기간]");
                return 0;
            }
            final int numDay = Integer.parseInt(splitted[2]);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, numDay);
            final DateFormat df = DateFormat.getInstance();
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("UPDATE accounts SET `chatblocktime` = ? WHERE id = ?");
                ps.setTimestamp(1, new java.sql.Timestamp(cal.getTimeInMillis()));
                ps.setInt(2, MapleCharacterUtil.getAccIdByName(splitted[1]));
                ps.executeUpdate();
            } catch (Exception e) {
                c.getPlayer().dropMessage(5, "오류입니다 : " + e);
                return 0;
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                    }
                }
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    victim.dropMessage(1, "대화가 금지되었습니다.");
                    victim.canTalk(false);
                }
            }
            c.getPlayer().dropMessage(6, "해당 캐릭터는 " + splitted[1] + " 일정 기간동안 채팅금지가 성립되었습니다. " + df.format(cal.getTime()));
            return 1;
        }
    }

    public static class 채팅금지헤제 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "채팅금지 [닉네임] [기간]");
                return 0;
            }
            final int numDay = Integer.parseInt(splitted[2]);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            final DateFormat df = DateFormat.getInstance();
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("UPDATE accounts SET `chatblocktime` = ? WHERE id = ?");
                ps.setTimestamp(1, new java.sql.Timestamp(cal.getTimeInMillis()));
                ps.setInt(2, MapleCharacterUtil.getAccIdByName(splitted[1]));
                ps.executeUpdate();
            } catch (Exception e) {
                c.getPlayer().dropMessage(5, "오류입니다 : " + e);
                return 0;
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                    }
                }
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    victim.dropMessage(1, "대화 금지가 해제 되었습니다. 재접속을 해주세요^^");
                    victim.canTalk(false);
                }
            }
            c.getPlayer().dropMessage(6, "해당 캐릭터는 대화 금지가 헤제 되었습니다.");
            return 1;
        }
    }    
    
    
    
    public static class 펫 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            boolean allowed = c.getPlayer().getMap().togglePetPick();
            c.getPlayer().dropMessage(6, "Current Map's Pet Pickup allowed : " + allowed);
            if (!allowed) {
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.yellowChat("현재 맵에서 펫 줍기 기능이 비활성화 되었습니다."));
            } else {
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.yellowChat("현재 맵에서 펫 줍기 기능이 활성화 되었습니다."));
            }
            return 1;
        }

    }    
    
    public static class 맵온라인 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "현재 이 맵에 있는 유저:");
            StringBuilder builder = new StringBuilder();
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (builder.length() > 150) { // wild guess :o
                    builder.setLength(builder.length() - 2);
                    c.getPlayer().dropMessage(6, builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                builder.append(", ");
            }
            builder.setLength(builder.length() - 2);
            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }
    }

    public static class TempBanIP extends TempBan {

        public TempBanIP() {
            ipBan = true;
        }
    }

    public static class BanIP extends Ban {

        public BanIP() {
            ipBan = true;
        }
    }
    
    public static class 정지 extends TempBan {
    }

    public static class TempBan extends CommandExecute {

        protected boolean ipBan = false;
        private String[] types = {"핵 사용", "매크로 사용", "광고", "욕설 / 비난 / 비방", "도배", "GM 괴롭힘 / 욕", "공개 욕설/비난/비방", "현금거래", "임시 정지 처분", "사칭", "관리자 사칭", "불법 / 비인가 프로그램 사용 (감지)", "계정 도용"};

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) {
                c.getPlayer().dropMessage(5, "정지 [닉네임] [정지사유] [정지 일 수]");
                StringBuilder s = new StringBuilder("정지사유: ");
                for (int i = 0; i < types.length; i++) {
                    s.append(i).append(" - ").append(types[i]).append(", ");
                }
                c.getPlayer().dropMessage(6, s.toString());
                return 0;
            }
            final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            final int reason = Integer.parseInt(splitted[2]);
            final int numDay = Integer.parseInt(splitted[3]);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, numDay);
            final DateFormat df = DateFormat.getInstance();

            if (reason < 0 || reason >= types.length) {
                c.getPlayer().dropMessage(5, "캐릭터 이름이 잘못됐거나 정지 사유가 짧습니다.");
                return 0;
            }
            if (victim == null) {
                boolean res = MapleCharacter.tempban(types[reason], cal, reason, c.getPlayer().getName(), splitted[1]);
                if (!res) {
                    c.getPlayer().dropMessage(5, "캐릭터 이름이 잘못됐거나 정지 사유가 짧습니다.");
                    return 0;
                }
                c.getPlayer().dropMessage(5, "" + splitted[1] + "캐릭터가 성공적으로 정지됐습니다. 정지기간:" + df.format(cal.getTime()));
                return 1;
            }
            victim.tempban(types[reason], cal, reason, ipBan, c.getPlayer().getName());
            victim.getClient().disconnect(true, false);
            victim.getClient().getSession().close();
            victim.getClient().getSocketChannel().close();
            c.getPlayer().dropMessage(5, "" + splitted[1] + "캐릭터가 성공적으로 정지됐습니다. 정지기간:" + df.format(cal.getTime()));
            return 1;
        }
    }    
    
    public static class HellB extends HellBan {
    }

    public static class HellBan extends Ban {

        public HellBan() {
            hellban = true;
        }
    }

    public static class UnHellB extends UnHellBan {
    }

    public static class UnHellBan extends UnBan {

        public UnHellBan() {
            hellban = true;
        }
    }    
    
    public static class UnB extends UnBan {
    }

    public static class UnBan extends CommandExecute {

        protected boolean hellban = false;

        private String getCommand() {
            if (hellban) {
                return "UnHellBan";
            } else {
                return "UnBan";
            }
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "[Syntax] !" + getCommand() + " <IGN>");
                return 0;
            }
            byte ret;
            if (hellban) {
                ret = MapleClient.unHellban(splitted[1]);
            } else {
                ret = MapleClient.unban(splitted[1]);
            }
            if (ret == -2) {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] SQL error.");
                return 0;
            } else if (ret == -1) {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] The character does not exist.");
                return 0;
            } else {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully unbanned!");

            }
            byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
            if (ret_ == -2) {
                c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
            } else if (ret_ == -1) {
                c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
            } else if (ret_ == 0) {
                c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
            } else if (ret_ == 1) {
                c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
            } else if (ret_ == 2) {
                c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
            }
            return ret_ > 0 ? 1 : 0;
        }
    }

    public static class UnbanIP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "[Syntax] !unbanip <IGN>");
                return 0;
            }
            byte ret = MapleClient.unbanIPMacs(splitted[1]);
            if (ret == -2) {
                c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
            } else if (ret == -1) {
                c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
            } else if (ret == 0) {
                c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
            } else if (ret == 1) {
                c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
            } else if (ret == 2) {
                c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
            }
            if (ret > 0) {
                return 1;
            }
            return 0;
        }
    }

    public static class Ban extends CommandExecute {

        protected boolean hellban = false, ipBan = false;

        private String getCommand() {
            if (hellban) {
                return "HellBan";
            } else {
                return "Ban";
            }
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(5, "[Syntax] !" + getCommand() + " <IGN> <Reason>");
                return 0;
            }
            if (StringUtil.joinStringFrom(splitted, 2).length() < 10) {
                c.getPlayer().dropMessage(5, "밴 사유가 너무 짧습니다. 상세하게 적어주세요.");
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("밴 캐릭터 : " + splitted[1]).append("\r\n사유 : ").append(StringUtil.joinStringFrom(splitted, 2));
            MapleCharacter target = World.getCharacterByName(splitted[1]);
            if (target != null) {
                if (c.getPlayer().getGMLevel() > target.getGMLevel() || c.getPlayer().isAdmin()) {
                    sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
                    if (target.ban(sb.toString(), hellban || ipBan, false, hellban, c.getPlayer().getName())) {
                        c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully banned " + splitted[1] + ".");
                        return 1;
                    } else {
                        c.getPlayer().dropMessage(6, "[" + getCommand() + "] Failed to ban.");
                        return 0;
                    }
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] May not ban GMs...");
                    return 1;
                }
            } else {
                if (MapleCharacter.ban(splitted[1], sb.toString(), false, c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), hellban, c.getPlayer().getName())) {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully offline banned " + splitted[1] + ".");
                    return 1;
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] Failed to ban " + splitted[1]);
                    return 0;
                }
            }
        }
    }    

    public static class 맵디버그 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, c.getPlayer().getMap().spawnDebug());
            return 1;
        }
    }
}
