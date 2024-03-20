package client.commands;

import client.BingoGame;
import static client.commands.CommandProcessor.getOptionalIntArg;

import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import client.MapleCharacter;
import client.MapleClient;
import client.items.Equip;
import client.items.IItem;
import client.items.Item;
import client.items.MapleInventoryType;
import client.skills.ISkill;
import client.skills.SkillFactory;
import client.skills.SkillStatEffect;
import client.stats.BuffStats;
import client.stats.PlayerStat;
import constants.GameConstants;
import constants.ServerConstants;
import constants.programs.MedalRanking;
import launch.ChannelServer;
import launch.world.WorldBroadcasting;
import packet.creators.DemianPacket;
import packet.creators.MainPacketCreator;
import packet.creators.SecondaryStat;
import server.items.InventoryManipulator;
import server.items.ItemInformation;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.shops.MapleShopFactory;
import tools.ArrayMap;
import tools.CurrentTime;
import tools.LoggerChatting;
import tools.Pair;
import tools.StringUtil;
import tools.Timer.MapTimer;

public class CharCommands implements Command {

    @Override
    public void execute(MapleClient c, String[] splitted) throws Exception, IllegalCommandSyntaxException {

        if (splitted[0].equals("!체력낮추기")) {
            c.getPlayer().getStat().setHp(1, c.getPlayer());
            c.getPlayer().getStat().setMp(500);
            c.getPlayer().updateSingleStat(PlayerStat.HP, 1);
            c.getPlayer().updateSingleStat(PlayerStat.MP, 500);
        } else if (splitted[0].equals("!체력회복")) {
            c.getPlayer().getStat().setHp(c.getPlayer().getStat().getMaxHp(), c.getPlayer());
            c.getPlayer().getStat().setMp(c.getPlayer().getStat().getMaxMp());
            c.getPlayer().updateSingleStat(PlayerStat.HP, c.getPlayer().getStat().getMaxHp());
            c.getPlayer().updateSingleStat(PlayerStat.MP, c.getPlayer().getStat().getMaxMp());
        } else if (splitted[0].equals("!인벤초기화")) {
            Map<Pair<Short, Short>, MapleInventoryType> eqs = new ArrayMap<Pair<Short, Short>, MapleInventoryType>();
            if (splitted[1].equals("모두")) {
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    for (IItem item : c.getPlayer().getInventory(type)) {
                        eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), type);
                    }
                }
            } else if (splitted[1].equals("장착")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()),
                            MapleInventoryType.EQUIPPED);
                }
            } else if (splitted[1].equals("장비")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIP);
                }
            } else if (splitted[1].equals("소비")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.USE);
                }
            } else if (splitted[1].equals("설치")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.SETUP);
                }
            } else if (splitted[1].equals("기타")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.ETC);
                }
            } else if (splitted[1].equals("캐시")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.CASH);
                }
            } else {
                c.getPlayer().dropMessage(6, "[모두/장착/장비/소비/설치/기타/캐시]");
            }
            for (Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet()) {
                InventoryManipulator.removeFromSlot(c, eq.getValue(), eq.getKey().left, eq.getKey().right, false,
                        false);
            }

        } else if (splitted[0].equals("!스킬")) {
            ISkill skill = SkillFactory.getSkill(Integer.parseInt(splitted[1]));
            byte level = (byte) getOptionalIntArg(splitted, 2, 1);
            byte masterlevel = (byte) getOptionalIntArg(splitted, 3, 1);
            if (level > skill.getMaxLevel()) {
                level = skill.getMaxLevel();
            }
            c.getPlayer().changeSkillLevel(skill, level, masterlevel);
        } else if (splitted[0].equals("!스탯포인트")) {
            c.getPlayer().setRemainingAp(Integer.parseInt(splitted[1]));
            c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
        } else if (splitted[0].equals("!직업")) {
            c.getPlayer().changeJob(Integer.parseInt(splitted[1]));
        } else if (splitted[0].equals("!현재맵")) {
            c.getPlayer().dropMessage(5, "현재 " + c.getPlayer().getMap().getId() + " 맵에 있습니다.");
        } else if (splitted[0].equals("!상점")) {
            MapleShopFactory shop = MapleShopFactory.getInstance();
            int shopId = Integer.parseInt(splitted[1]);
            if (shop.getShop(shopId) != null) {
                shop.getShop(shopId).sendShop(c);
            }
        } else if (splitted[0].equals("!메소")) {
            c.getPlayer().gainMeso((long) (9999999999L - c.getPlayer().getMeso()), true);
        } else if (splitted[0].equals("!아이템")) {
            final int itemId = Integer.parseInt(splitted[1]);
            final short quantity = (short) getOptionalIntArg(splitted, 2, 1);

            if (c.getPlayer().getGMLevel() < 6) {
                for (int i : GameConstants.itemBlock) {
                    if (itemId == i) {
                        c.getPlayer().dropMessage(5, "해당 아이템은 당신의 GM 레벨로는 생성 하실수 없습니다.");
                    }
                }
            }
            ItemInformation ii = ItemInformation.getInstance();
            if (GameConstants.isPet(itemId)) {
                c.getPlayer().dropMessage(5, "펫은 캐시샵을 이용해주시기 바랍니다.");
            } else if (!ii.itemExists(itemId)) {
                c.getPlayer().dropMessage(5, itemId + " 번 아이템은 존재하지 않습니다.");
            } else {
                IItem item;
                if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    item = ii.randomizeStats((Equip) ii.getEquipById(itemId), true);
                } else {
                    item = new Item(itemId, (byte) 0, (short) quantity, (byte) 0);
                }
                item.setOwner(c.getPlayer().getName());
                item.setGMLog(CurrentTime.getAllCurrentTime() + "에 " + c.getPlayer().getName() + "의 명령어로 얻은 아이템.");
                InventoryManipulator.addbyItem(c, item);
            }
        } else if (splitted[0].equals("!드롭")) {
            final int itemId = Integer.parseInt(splitted[1]);
            final short quantity = (short) (short) getOptionalIntArg(splitted, 2, 1);
            if (itemId == 2100106 || itemId == 2100107) {
                c.getPlayer().dropMessage(5, "Item is blocked.");
                return;
            }
            if (GameConstants.isPet(itemId)) {
                c.getPlayer().dropMessage(5, "펫은 캐시샵에서 구매해 주세요.");
            } else {
                IItem toDrop;
                if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    ItemInformation ii = ItemInformation.getInstance();
                    toDrop = ii.randomizeStats((Equip) ii.getEquipById(itemId), true);
                } else {
                    toDrop = new Item(itemId, (byte) 0, (short) quantity, (byte) 0);
                }
                toDrop.setGMLog(c.getPlayer().getName() + "이 드롭 명령어로 제작한 아이템");

                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(),
                        true, true);
            }

        } else if (splitted[0].equals("!레벨")) {
            c.getPlayer().setLevel(Short.parseShort(splitted[1]));
            c.getPlayer().levelUp();
            if (c.getPlayer().getExp() < 0) {
                c.getPlayer().gainExp(-c.getPlayer().getExp(), false, false, true);
            }

        } else if (splitted[0].equals("!온라인")) {
            c.getPlayer().dropMessage(6, "현재 채널에 접속된 유저는 다음과 같습니다. :");
            String names = "";
            for (MapleCharacter name : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                names += name.getName();
                names += ", ";
            }
            if (names.equals("")) {
                names = "현재채널에 접속중인 유저가 없습니다.";
            }
            c.getPlayer().dropMessage(6, names);
        } else if (splitted[0].equals("!총온라인")) {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                String names = "채널 "
                        + (cserv.getChannel() == 0 ? 1 : cserv.getChannel() == 1 ? "20세이상" : cserv.getChannel()) + " ("
                        + cserv.getPlayerStorage().getAllCharacters().size() + " 명) : ";

                for (MapleCharacter name : cserv.getPlayerStorage().getAllCharacters()) {
                    names += name.getName();
                    names += ", ";
                }
                c.getPlayer().dropMessage(6, names);
            }
        } else if (splitted[0].equals("!모두저장")) {
            c.getPlayer().dropMessage(6, "저장을 시작합니다.");
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.saveAllMerchant();
                for (MapleCharacter hp : cserv.getPlayerStorage().getAllCharacters()) {
                    if (hp != null) {
                        hp.saveToDB(false, false);
                    }
                }
            }
            MedalRanking.getInstance().save();
            c.getPlayer().dropMessage(6, "[시스템] 저장이 완료되었습니다.");
        } else if (splitted[0].equals("!말")) {
            if (splitted.length > 2) {
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtil.joinStringFrom(splitted, 2));
                if (!c.getPlayer().hasGmLevel((byte) 5)) {
                    if (Integer.parseInt(splitted[1]) >= 12) {
                        c.getPlayer().dropMessage(6, "사용할 수 없는 색깔코드입니다. 사용가능코드 : 0~11");
                        return;
                    }
                }
                byte[] packet = MainPacketCreator.getGMText(Integer.parseInt(splitted[1]), sb.toString());
                WorldBroadcasting.broadcastMessage(packet);
            } else {
                c.getPlayer().dropMessage(6, "사용법: !말 <색깔코드> <할말>");
            }
        } else if (splitted[0].equals("!백")) {
            if (!c.getPlayer().isHidden()) {
                c.getPlayer().dropMessage(6, "하이드 모드에서만 백을 사용할 수 있습니다.");
            } else {
                for (final MapleMapObject mmo : c.getPlayer().getMap().getAllMonster()) {
                    final MapleMonster monster = (MapleMonster) mmo;
                    monster.setPosition(c.getPlayer().getPosition());
                }
            }
        } else if (splitted[0].equals("!노래")) {
            c.getPlayer().getMap().broadcastMessage(MainPacketCreator.musicChange(splitted[1]));
        } else if (splitted[0].equals("!맥스스탯")) {
            c.getPlayer().getStat().setAmbition(32767);
            c.getPlayer().getStat().setCharm(32767);
            c.getPlayer().getStat().setDex(32767);
            c.getPlayer().getStat().setDiligence(32767);
            c.getPlayer().getStat().setEmpathy(32767);
            c.getPlayer().getStat().setInsight(32767);
            c.getPlayer().getStat().setInt(32767);
            c.getPlayer().getStat().setLuk(32767);
            c.getPlayer().getStat().setMaxHp(500000);
            if (!GameConstants.isZero(c.getPlayer().getJob())) {
                c.getPlayer().getStat().setMaxMp(500000);
                c.getPlayer().getStat().setMp(500000);
            }
            c.getPlayer().getStat().setHp(500000, c.getPlayer());
            c.getPlayer().getStat().setStr(32767);
            c.getPlayer().getStat().setWillPower(32767);
            c.getPlayer().updateSingleStat(PlayerStat.STR, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.DEX, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.INT, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.LUK, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.CHARISMA, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.WILLPOWER, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.INSIGHT, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.CHARM, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.CRAFT, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.SENSE, 32767);
            c.getPlayer().updateSingleStat(PlayerStat.MAXHP, 500000);
            if (!GameConstants.isZero(c.getPlayer().getJob())) {
                c.getPlayer().updateSingleStat(PlayerStat.MAXMP, 500000);
                c.getPlayer().updateSingleStat(PlayerStat.MP, 500000);
            }
            c.getPlayer().updateSingleStat(PlayerStat.HP, 500000);
        } else if (splitted[0].equals("!스탯초기화")) {
            c.getPlayer().getStat().setAmbition(0);
            c.getPlayer().getStat().setCharm(0);
            c.getPlayer().getStat().setDex(4);
            c.getPlayer().getStat().setDiligence(0);
            c.getPlayer().getStat().setEmpathy(0);
            c.getPlayer().getStat().setInsight(0);
            c.getPlayer().getStat().setInt(4);
            c.getPlayer().getStat().setLuk(4);
            c.getPlayer().getStat().setMaxHp(1000);
            c.getPlayer().getStat().setMaxMp(1000);
            c.getPlayer().getStat().setHp(1000, c.getPlayer());
            c.getPlayer().getStat().setMp(1000);
            c.getPlayer().getStat().setStr(4);
            c.getPlayer().getStat().setWillPower(0);
            c.getPlayer().updateSingleStat(PlayerStat.STR, 4);
            c.getPlayer().updateSingleStat(PlayerStat.DEX, 4);
            c.getPlayer().updateSingleStat(PlayerStat.INT, 4);
            c.getPlayer().updateSingleStat(PlayerStat.LUK, 4);
            c.getPlayer().updateSingleStat(PlayerStat.CHARISMA, 0);
            c.getPlayer().updateSingleStat(PlayerStat.WILLPOWER, 0);
            c.getPlayer().updateSingleStat(PlayerStat.INSIGHT, 0);
            c.getPlayer().updateSingleStat(PlayerStat.CHARM, 0);
            c.getPlayer().updateSingleStat(PlayerStat.CRAFT, 0);
            c.getPlayer().updateSingleStat(PlayerStat.SENSE, 0);
            c.getPlayer().updateSingleStat(PlayerStat.MAXHP, 1000);
            c.getPlayer().updateSingleStat(PlayerStat.MAXMP, 1000);
            c.getPlayer().updateSingleStat(PlayerStat.MP, 1000);
            c.getPlayer().updateSingleStat(PlayerStat.HP, 1000);
        } else if (splitted[0].equals("!하이드")) {
            boolean hided = c.getPlayer().isHidden();
            if (hided == true) {
                c.getPlayer().message(6, "하이드 상태가 해제되었습니다.");
            } else {
                c.getPlayer().message(6, "하이드 상태가 적용되었습니다.");
            }
            c.getPlayer().setHide(!hided);
        } else if (splitted[0].equals("!후원포인트지급")) {
            MapleCharacter who = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            final int rc = Integer.parseInt(splitted[2]);
            if (who != null) {
                if (rc > 0) {
                    LoggerChatting.writeLog(LoggerChatting.givercLog, LoggerChatting.getRcgive("후원", c.getPlayer(), who, rc));
                    who.gainRC(rc);
                    who.dropMessage(6, "[GM알림] " + c.getPlayer().getName() + "님으로부터 " + rc + "후원포인트를 획득했습니다.");
                    c.getPlayer().dropMessage(5, "[GM알림] " + splitted[1] + "님에게 " + rc + "후원포인트를 지급했습니다.");
                } else {
                    c.getSession().writeAndFlush(MainPacketCreator.getGMText(20, "[GM알림] 지급하실 포인트가 0포인트보다 작습니다."));
                }
            } else {
                c.getPlayer().dropMessage(5, "대상 플레이어를 발견하지 못했습니다.");
            }
                    } else if (splitted[0].equals("!추가데미지")) {
            String Cname = splitted[1];
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                MapleCharacter chr = (MapleCharacter) ch.getPlayerStorage().getCharacterByName(Cname);
                if (chr != null) {
                    chr.setAddDamage(chr.getAddDamage() + Long.parseLong(splitted[2]));
                    chr.dropMessage(5, "운영자로부터 " + Long.parseLong(splitted[2]) + " 추가 데미지를 지급 받았습니다.");
                    c.getPlayer().dropMessage(5, "" + chr.getName() + "님꼐 " + Long.parseLong(splitted[2]) + " 만큼의 추가데미지를 지급 하였습니다. ");
                    return;
                } else {
                    chr.dropMessage(5, chr.getName() + " 플레이어가 접속해있지 않습니다.");
                }
            }
        } else if (splitted[0].equals("!추가퍼뎀")) {
            String Cname = splitted[1];
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                MapleCharacter chr = (MapleCharacter) ch.getPlayerStorage().getCharacterByName(Cname);
                if (chr != null) {
                    chr.setDamageHit(chr.getDamageHit() + Integer.parseInt(splitted[2]));
                    chr.dropMessage(5, "운영자로부터 " + Integer.parseInt(splitted[2]) + " 추가퍼뎀를 지급 받았습니다.");
                    c.getPlayer().dropMessage(5, "" + chr.getName() + "님께 " + Long.parseLong(splitted[2]) + " 만큼의 추가퍼뎀를 지급 하였습니다. ");
                    return;
                } else {
                    chr.dropMessage(5, chr.getName() + " 플레이어가 접속해있지 않습니다.");
                }
            }
        } else if (splitted[0].equals("!유저킬")) {
            MapleCharacter who = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (who != null) {
                who.addMPHP(-who.getStat().getCurrentMaxHp(), -who.getStat().getCurrentMaxMp());
               // who.Message(5, "GM이 당신을 죽였습니다.");
                c.getPlayer().dropMessage(5, "" + splitted[1] + "플레이어를 죽였습니다.");
            } else {
                c.getPlayer().dropMessage(5, "대상 플레이어를 발견하지 못했습니다.");
		}  
        } else if (splitted[0].equals("!피버타임")) {
            if (ServerConstants.feverTime) {
                ServerConstants.feverTime = false;
                c.getPlayer().dropMessage(5, "[GM알림] 피버타임이 해제되었습니다.");
            } else {
                ServerConstants.feverTime = true;
                c.getPlayer().dropMessage(5, "[GM알림] 피버타임이 설정되었습니다.");
            }
        } else if (splitted[0].equals("!패킷출력")) {
            if (ServerConstants.showPackets) {
                ServerConstants.showPackets = false;
                c.getPlayer().dropMessage(5, "[GM알림] 패킷출력이 해제되었습니다.");
            } else {
                ServerConstants.showPackets = true;
                c.getPlayer().dropMessage(5, "[GM알림] 패킷출력이 설정되었습니다.");
            }
        } else if (splitted[0].equals("!서버점검")) {
            if (ServerConstants.serverCheck) {
                ServerConstants.serverCheck = false;
                c.getPlayer().dropMessage(5, "[GM알림] 서버점검이 해제되었습니다.");
            } else {
                ServerConstants.serverCheck = true;
                c.getPlayer().dropMessage(5, "[GM알림] 서버점검이 설정되었습니다.");
            }
        } else if (splitted[0].equals("!캐시")) {
            c.getPlayer().modifyCSPoints(1, Integer.parseInt(splitted[1]), true);
        } else if (splitted[0].equals("!스킬마스터")) {
            c.getPlayer().maxskill(Integer.parseInt(splitted[1]));
        } else if (splitted[0].equals("!스킬초기화")) {
            for (MapleMapObject ds : c.getPlayer().getMap().getAllDemianSword()) {
                c.getSession().writeAndFlush(DemianPacket.Demian_OnCorruptionChange());
            }
        } else if (splitted[0].equals("!명성치")) {
            int point = Integer.parseInt(splitted[1]);
            c.getPlayer().dropMessage(5, point + " 명성치를 획득 하였습니다.");
            c.getPlayer().addInnerExp(point);
        } else if (splitted[0].equals("!빙고")) {
            if (splitted.length > 1) {
                switch (splitted[1]) {
                    case "시작": {
                        if (c.getPlayer().getMapId() != 922290000) {
                            return;
                        }
                        if (c.getPlayer().getMap().getCharacters().size() < 5) {
                            c.getSession().writeAndFlush(MainPacketCreator.OnAddPopupSay(1052230, 3000, "#face1#빙고 최소 등록 인원 5명", ""));
                            return;
                        }
                        c.getPlayer().getMap().broadcastMessage(MainPacketCreator.getClock(30));
                        MapTimer.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                for (MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
                                    if (chr != null) {
                                        chr.changeMap(922290100, 0);
                                    }
                                }
                            }
                        }, 30 * 1000);
                        MapTimer.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (c.getPlayer().getMapId() == 922290100) {
                                    BingoGame bingo = new BingoGame(c.getPlayer().getMap().getCharacters());
                                    for (MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
                                        if (chr != null) {
                                            chr.setBingoGame(bingo);
                                            chr.getClient().getSession().writeAndFlush(MainPacketCreator.musicChange("BgmEvent/dolphin_night"));
                                            chr.getClient().getSession().writeAndFlush(MainPacketCreator.playSE("multiBingo/start"));
                                            chr.getClient().getSession().writeAndFlush(MainPacketCreator.showMapEffect("Gstar/start"));
                                        }
                                    }
                                }
                            }
                        }, 40 * 1000);
                        MapTimer.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                c.getPlayer().getBingoGame().StartGame();
                            }
                        }, 42 * 1000);
                        break;
                    }
                    case "Stop": {
                        c.getPlayer().getBingoGame().StopBingo();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{new CommandDefinition("노래", "<재생할BGM>", "Sound.wz에서 해당 경로의 BGM을 재생합니다.", 6),
            new CommandDefinition("체력낮추기", "", "자신의 HP를 1, MP를 500으로 만듭니다.", 6),
            new CommandDefinition("힐", "", "자신의 HP와 MP를 서버에서 계산된 최대 HP,MP만큼 채웁니다.", 2),
            new CommandDefinition("스킬", "<스킬id> <스킬레벨> <스킬마스터레벨>", "해당 스킬id의 스킬레벨과 마스터레벨만큼 스킬을 올립니다.", 6),
            new CommandDefinition("맥스스탯", "", "모든 스탯을 최대로 만듭니다.", 5),
            new CommandDefinition("스탯초기화", "", "모든 스탯을 초기화 합니다.", 5),
            new CommandDefinition("스탯포인트", "<스탯포인트량>", "스탯 포인트를 늘립니다.", 6),
            new CommandDefinition("직업", "<직업id>", "해당하는 직업 id로 전직합니다. id를 잘못 입력할 시 게임접속이 불가능해질 수 있습니다.", 2),
            new CommandDefinition("현재맵", "", "현재 맵 고유넘버를 출력합니다.", 2),
            new CommandDefinition("모두저장", "", "현재 접속중인 모든 플레이어를 저장합니다.", 2),
            new CommandDefinition("상점", "<상점ID>", "해당 상점 ID를 가진 상점을 엽니다.", 5),
            new CommandDefinition("메소", "", Long.MAX_VALUE + "메소를 가지게 만듭니다.", 6),
            new CommandDefinition("레벨업", "", "레벨업이 바로 가능한 만큼의 경험치를 획득합니다.", 4),
            new CommandDefinition("아이템", "<아이템ID> (<아이템갯수> 기본값:1)", "해당 아이템을 아이템 갯수만큼 가집니다.", 6),
            new CommandDefinition("드롭", "<아이템ID> (<아이템갯수> 기본값:1)", "해당 아이템을 아이템 갯수만큼 드롭합니다.", 6),
            new CommandDefinition("레벨", "<레벨>", "입력한 레벨로 올리거나 내립니다.", 2),
            new CommandDefinition("온라인", "", "현재 채널에 접속중인 유저를 모두 출력합니다.", 6),
            new CommandDefinition("총온라인", "", "모든 채널에 접속중인 유저를 모두 출력합니다.", 6),
            new CommandDefinition("인벤초기화", "모두/장착/장비/소비/설치/기타/캐시", "해당 탭의 인벤토리를 모두 비워버립니다.", 2),
            new CommandDefinition("백", "", "현재 캐릭터의 위치로 몬스터를 자석처럼 붙여버립니다.", 5),
            new CommandDefinition("말", "<색깔코드> <메시지>", "전체 월드에 GM텍스트 패킷을 이용하여 메시지를 출력합니다.", 2),
            new CommandDefinition("하이드", "", "다른 플레이어에게 보이지 않게 숨어버립니다.", 1),
            new CommandDefinition("스킬마스터", "<직업코드>", "직업코드의 스킬을 모두 최대레벨로 올립니다.", 5),
            new CommandDefinition("명성치", "", "", 6),
            new CommandDefinition("후원포인트지급", "<금액>", "후원포인트를 지급합니다.", 6),
            new CommandDefinition("추가데미지", "<닉네임> <수치>", "추가데미지를 지급합니다.", 6),
            new CommandDefinition("추가퍼뎀", "<닉네임> <수치>", "추가타수를 지급합니다.", 6),
            new CommandDefinition("유저킬", "<닉네임>", "해당 유저를 죽입니다.", 6),
            new CommandDefinition("피버타임", "", "피버타임을 설정합니다.", 5),
            new CommandDefinition("패킷출력", "", "패킷출력을 설정합니다.", 5),
            new CommandDefinition("서버점검", "", "서버점검을 설정합니다.", 5), new CommandDefinition("테스트패킷", "", "", 5),
            new CommandDefinition("캐시", "", "", 5), new CommandDefinition("상점세일", "", "", 6),
            new CommandDefinition("빙고", "", "", 6)};
    }
}
