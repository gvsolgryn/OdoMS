/*
 * 테스피아 Project
 * ==================================
 * 팬더 spirit_m@nate.com
 * 배지훈 raccoonfox69@gmail.com
 * ==================================
 * 
 */
package client.commands;

import client.MapleCharacter;
import client.PlayerStats;
import client.MapleClient;
import client.items.IEquip;
import client.items.IItem;
import client.items.MapleInventoryType;
import client.skills.SkillFactory;
import client.stats.BuffStats;
import client.stats.DiseaseStats;
import client.stats.PlayerStat;
import constants.GameConstants;
import constants.ServerConstants;
import java.util.Map;
import launch.ChannelServer;
import launch.world.WorldConnected;
import packet.creators.MainPacketCreator;
import packet.creators.UIPacket;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCScriptManager;
import scripting.ReactorScriptManager;
import server.items.InventoryManipulator;
import server.life.MapleMonsterProvider;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MaplePortal;
import server.maps.SavedLocationType;
import tools.ArrayMap;
import tools.Pair;
import tools.StringUtil;

public class PlayerCommand implements Command {

    @Override
    public void execute(final MapleClient c, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        ChannelServer cserv = c.getChannelServer();

        if (splitted[0].equals("@힘")) {
            int str = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getStr() + str > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < str || c.getPlayer().getRemainingAp() < 0 || str < 0) {
                c.getPlayer().dropMessage(5, "오류가 발생했습니다.");
            } else {
                stat.setStr(stat.getStr() + str);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - str);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.STR, stat.getStr());
            }
        } else if (splitted[0].equals("@인트")) {
            int int_ = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getInt() + int_ > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < int_ || c.getPlayer().getRemainingAp() < 0 || int_ < 0) {
                c.getPlayer().dropMessage(5, "오류가 발생했습니다.");
            } else {
                stat.setInt(stat.getInt() + int_);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - int_);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.INT, stat.getInt());
            }
        } else if (splitted[0].equals("@덱스")) {
            int dex = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getDex() + dex > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < dex || c.getPlayer().getRemainingAp() < 0 || dex < 0) {
                c.getPlayer().dropMessage(5, "오류가 발생했습니다.");
            } else {
                stat.setDex(stat.getDex() + dex);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - dex);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.DEX, stat.getDex());
            }
        } else if (splitted[0].equals("@럭")) {
            int luk = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getLuk() + luk > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < luk || c.getPlayer().getRemainingAp() < 0 || luk < 0) {
                c.getPlayer().dropMessage(5, "오류가 발생했습니다.");
            } else {
                stat.setLuk(stat.getLuk() + luk);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - luk);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.LUK, stat.getLuk());
            }
        } else if (splitted[0].equals("@인벤초기화")) {
            Map<Pair<Short, Short>, MapleInventoryType> eqs = new ArrayMap<Pair<Short, Short>, MapleInventoryType>();
            if (splitted[1].equals("모두")) {
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    for (IItem item : c.getPlayer().getInventory(type)) {
                        eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), type);
                    }
                }
            } else if (splitted[1].equals("장착")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIPPED);
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
            for (Map.Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet()) {
                InventoryManipulator.removeFromSlot(c, eq.getValue(), eq.getKey().left, eq.getKey().right, false, false);
            }
        } else if (splitted[0].equals("@도움말") || splitted[0].equals("@명령어")) {
            c.getPlayer().dropMessage(5, "초원온라인에서 유저들이 사용할수있는 명령어입니다. :");
            c.getPlayer().dropMessage(5, "@힘, @덱스, @인트, @럭 <찍을 수치> : 해당 스탯을 마우스 클릭 대신 찍을 수 있습니다.");
            c.getPlayer().dropMessage(5, "@렉 : 공격 등 채팅외에 아무것도 안될때 사용하세요.");
            c.getPlayer().dropMessage(5, "@마을 : " + ServerConstants.serverName + " 광장으로 이동합니다.");
            c.getPlayer().dropMessage(5, "@보스 : 보스방 으로 이동.");
            c.getPlayer().dropMessage(5, "@작방 : 작방으로 이동합니다.");
            c.getPlayer().dropMessage(5, "@낚시 : 낚시터로 이동합니다.");
            c.getPlayer().dropMessage(5, "@도박 :  도박장으로 이동합니다.");
            c.getPlayer().dropMessage(5, "@스킬마스터 : 현재 자신의 직업 스킬을 마스터합니다.");
            c.getPlayer().dropMessage(5, "@가이드 : 초원온라인의 운영자 누나가 가이드 해줍니다.");
            c.getPlayer().dropMessage(5, "@보조무기해제 : 해제불가능한 보조무기를 해제합니다.");
            c.getPlayer().dropMessage(5, "@저장 : 캐릭을 저장합니다 기모띵~");
            c.getPlayer().dropMessage(5, "@동접 : 동접자 확인가능.");
            c.getPlayer().dropMessage(5, "@추천인 : 추천인 등록가능.");  
            c.getPlayer().dropMessage(5, "~할말 : 전체채팅 기능입니다 EX) ~안녕하세요");
            c.getPlayer().dropMessage(5, "@추뎀확인 : 자신의 추가데미지를 확인하실수있습니다.");
                    } else if (splitted[0].equals("@추천인")) {
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 9010031, null);
        } else if (splitted[0].equals("@동접")) {
            Map<Integer, Integer> connected = WorldConnected.getConnected(c.getWorld());
            StringBuilder conStr = new StringBuilder("현재 접속중인 인원 ");
            boolean first = true;
            for (int i : connected.keySet()) {
                if (!first) {
                    conStr.append(", ");
                } else {
                    first = false;
                }
                if (i == 0) {
                    conStr.append("명: ");
                    conStr.append(connected.get(i));
                }
            }
            c.getPlayer().dropMessage(6, conStr.toString());
        } else if (splitted[0].equals("@보조무기해제")) {
            IEquip equip = null;
            equip = (IEquip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -10);
            if (equip == null) {
                c.getPlayer().Message(1, "장착중인 보조무기가 존재하지 않습니다.");
                c.getSession().writeAndFlush(MainPacketCreator.resetActions(c.getPlayer()));
                return;
            }
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot((byte) -10);
            c.getPlayer().equipChanged();
            InventoryManipulator.addFromDrop(c, equip, false);
            c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
            c.getPlayer().send(MainPacketCreator.getPlayerInfo(c.getPlayer()));
            MapleMap currentMap = c.getPlayer().getMap();
            currentMap.removePlayer(c.getPlayer());
            currentMap.addPlayer(c.getPlayer());
        } else if (splitted[0].equals("@렉")) {
            c.sendPacket(MainPacketCreator.SkillUseResult((byte) 1));
            c.sendPacket(MainPacketCreator.resetActions(c.getPlayer()));
            c.getPlayer().dropMessage(5, "렉이 제거되었습니다.");
        } else if (splitted[0].equals("@저장")) {
            c.getPlayer().dropMessage(6, "저장을 시작합니다. 저장이완료되었다는 문구가뜨기전 절대로 게임종료하시면안됩니다.");
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage(5, "[알림] 저장이 완료되었습니다.");
        } else if (splitted[0].equals("@스킬마스터")) {
            if (c.getPlayer().getLevel() < 10) {
                c.getPlayer().dropMessage(1, "레벨 10 이상 부터 사용 할 수 있습니다.");
                return;
            }
            for (int i = 0; i < (c.getPlayer().getJob() % 10) + 1; i++) {
                c.getPlayer().maxskill(((i + 1) == ((c.getPlayer().getJob() % 10) + 1)) ? c.getPlayer().getJob() - (c.getPlayer().getJob() % 100) : c.getPlayer().getJob() - (i + 1));
            }
            c.getPlayer().updateSingleStat(PlayerStat.CHARM, 32767);
            c.getPlayer().maxskill(c.getPlayer().getJob());
            if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                c.getPlayer().maxskill(3101);
            }
            c.getPlayer().dropMessage(5, "현재 직업군 스킬마스터 완료");
            
        } else if (splitted[0].equals("@광장") || splitted[0].equals("@헤네시스") || splitted[0].equals("@마을")) {
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
             || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[시스템] 초보자는 광장으로 이동 할 수 없습니다.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(100000000);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "없는 포탈의 값이 있습니다.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("@보스")){
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[시스템] 초보자는 보스방으로 이동 할 수 없습니다.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(970060000);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "없는 포탈의 값이 있습니다.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
        } else if (splitted[0].equals("@낚시")) {
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "초보자는 낚시터로 이동 할 수 없습니다.");
                return;
            }
            int mapcode[] = {925060100, 925060200, 925060300, 925060400, 925060500, 925060700, 925060800, 925060900, 925061000, 925061100, 925061300, 925061400, 925061500, 925061600, 925061700, 925061900, 925062000, 925062100, 925062200, 925062300, 925062500, 925062600, 925062700, 925062800, 925062900, 925063100, 925063200, 925063300, 925063400, 925063500, 925063700, 925063800, 925063900, 925064000, 925064100, 925064300, 925064400, 925064500, 925064600, 925064700};

            for (int i = 0; i < mapcode.length; i++) {
                if (c.getPlayer().getMapId() == mapcode[i]) {
                    c.getPlayer().dropMessage(5, "[시스템] 무릉도원 에서는 이동할수 없습니다.");
                    return;
                }
                            if (c.getPlayer().getMapId() == 100000055) {
                c.getPlayer().dropMessage(5, "[시스템] 낚시맵에선 에서는 이동 할 수 없습니다.");
                return;
            }
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(100000055);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "없는 포탈의 값이 있습니다.");
                } catch (NumberFormatException a) {
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("@도박")){
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[시스템] 초보자는 도박장으로 이동 할 수 없습니다.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(323000101);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "없는 포탈의 값이 있습니다.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("@작방")){
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[시스템] 초보자는 작방으로 이동 할 수 없습니다.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(100000001);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "없는 포탈의 값이 있습니다.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
      } else if (splitted[0].equals("@환생")) {
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 1404015, null);
       }
        		else if (splitted[0].equals("@매직가드온"))
		{
			if (c.getPlayer().getKeyValue("magicg2") != null)
			{
				SkillFactory.getSkill(2001002).getEffect(1).applyTo(c.getPlayer(), c.getPlayer().getPosition());
			}
		}
		else if (splitted[0].equals("@매직가드오프"))
		{
			if (c.getPlayer().getKeyValue("magicg2") != null)
			{
				c.getPlayer().cancelEffectFromBuffStat(BuffStats.MagicGuard, 2001002);
			}
		}
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{
            new CommandDefinition("힘", "", "", 0),
            new CommandDefinition("인트", "", "", 0),
            new CommandDefinition("덱스", "", "", 0),
            new CommandDefinition("럭", "", "", 0),
            new CommandDefinition("렉", "", "", 0),
            new CommandDefinition("랙", "", "", 0),
            new CommandDefinition("광장", "", "", 0),
            new CommandDefinition("작방", "", "", 0),
            new CommandDefinition("마을", "", "", 0),
            new CommandDefinition("낚시", "", "", 0),
            new CommandDefinition("보스", "", "", 0),
            new CommandDefinition("도박", "", "", 0),
            new CommandDefinition("환생", "", "", 0),
            new CommandDefinition("저장", "", "", 0),
            new CommandDefinition("도움말", "", "", 0),
            new CommandDefinition("추천인", "", "", 0),
            new CommandDefinition("명령어", "", "", 0),
            new CommandDefinition("스킬마스터", "", "", 0),
            new CommandDefinition("추천인", "", "", 0),
            new CommandDefinition("인벤초기화", "", "", 0),
            new CommandDefinition("보조무기해제", "", "", 0),
            new CommandDefinition("동접", "", "", 0),  
            new CommandDefinition("매직가드온", "", "", 0),         
            new CommandDefinition("매직가드오프", "", "", 0), 
        };
        }
}
