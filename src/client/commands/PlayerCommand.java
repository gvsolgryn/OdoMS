/*
 * �׽��Ǿ� Project
 * ==================================
 * �Ҵ� spirit_m@nate.com
 * ������ raccoonfox69@gmail.com
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

        if (splitted[0].equals("@��")) {
            int str = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getStr() + str > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < str || c.getPlayer().getRemainingAp() < 0 || str < 0) {
                c.getPlayer().dropMessage(5, "������ �߻��߽��ϴ�.");
            } else {
                stat.setStr(stat.getStr() + str);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - str);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.STR, stat.getStr());
            }
        } else if (splitted[0].equals("@��Ʈ")) {
            int int_ = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getInt() + int_ > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < int_ || c.getPlayer().getRemainingAp() < 0 || int_ < 0) {
                c.getPlayer().dropMessage(5, "������ �߻��߽��ϴ�.");
            } else {
                stat.setInt(stat.getInt() + int_);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - int_);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.INT, stat.getInt());
            }
        } else if (splitted[0].equals("@����")) {
            int dex = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getDex() + dex > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < dex || c.getPlayer().getRemainingAp() < 0 || dex < 0) {
                c.getPlayer().dropMessage(5, "������ �߻��߽��ϴ�.");
            } else {
                stat.setDex(stat.getDex() + dex);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - dex);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.DEX, stat.getDex());
            }
        } else if (splitted[0].equals("@��")) {
            int luk = Integer.parseInt(splitted[1]);
            final PlayerStats stat = c.getPlayer().getStat();

            if (stat.getLuk() + luk > c.getPlayer().getMaxStats() || c.getPlayer().getRemainingAp() < luk || c.getPlayer().getRemainingAp() < 0 || luk < 0) {
                c.getPlayer().dropMessage(5, "������ �߻��߽��ϴ�.");
            } else {
                stat.setLuk(stat.getLuk() + luk);
                c.getPlayer().setRemainingAp(c.getPlayer().getRemainingAp() - luk);
                c.getPlayer().updateSingleStat(PlayerStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
                c.getPlayer().updateSingleStat(PlayerStat.LUK, stat.getLuk());
            }
        } else if (splitted[0].equals("@�κ��ʱ�ȭ")) {
            Map<Pair<Short, Short>, MapleInventoryType> eqs = new ArrayMap<Pair<Short, Short>, MapleInventoryType>();
            if (splitted[1].equals("���")) {
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    for (IItem item : c.getPlayer().getInventory(type)) {
                        eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), type);
                    }
                }
            } else if (splitted[1].equals("����")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIPPED);
                }
            } else if (splitted[1].equals("���")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIP);
                }
            } else if (splitted[1].equals("�Һ�")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.USE);
                }
            } else if (splitted[1].equals("��ġ")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.SETUP);
                }
            } else if (splitted[1].equals("��Ÿ")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.ETC);
                }
            } else if (splitted[1].equals("ĳ��")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.CASH);
                }
            } else {
                c.getPlayer().dropMessage(6, "[���/����/���/�Һ�/��ġ/��Ÿ/ĳ��]");
            }
            for (Map.Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet()) {
                InventoryManipulator.removeFromSlot(c, eq.getValue(), eq.getKey().left, eq.getKey().right, false, false);
            }
        } else if (splitted[0].equals("@����") || splitted[0].equals("@��ɾ�")) {
            c.getPlayer().dropMessage(5, "�ʿ��¶��ο��� �������� ����Ҽ��ִ� ��ɾ��Դϴ�. :");
            c.getPlayer().dropMessage(5, "@��, @����, @��Ʈ, @�� <���� ��ġ> : �ش� ������ ���콺 Ŭ�� ��� ���� �� �ֽ��ϴ�.");
            c.getPlayer().dropMessage(5, "@�� : ���� �� ä�ÿܿ� �ƹ��͵� �ȵɶ� ����ϼ���.");
            c.getPlayer().dropMessage(5, "@���� : " + ServerConstants.serverName + " �������� �̵��մϴ�.");
            c.getPlayer().dropMessage(5, "@���� : ������ ���� �̵�.");
            c.getPlayer().dropMessage(5, "@�۹� : �۹����� �̵��մϴ�.");
            c.getPlayer().dropMessage(5, "@���� : �����ͷ� �̵��մϴ�.");
            c.getPlayer().dropMessage(5, "@���� :  ���������� �̵��մϴ�.");
            c.getPlayer().dropMessage(5, "@��ų������ : ���� �ڽ��� ���� ��ų�� �������մϴ�.");
            c.getPlayer().dropMessage(5, "@���̵� : �ʿ��¶����� ��� ������ ���̵� ���ݴϴ�.");
            c.getPlayer().dropMessage(5, "@������������ : �����Ұ����� �������⸦ �����մϴ�.");
            c.getPlayer().dropMessage(5, "@���� : ĳ���� �����մϴ� ����~");
            c.getPlayer().dropMessage(5, "@���� : ������ Ȯ�ΰ���.");
            c.getPlayer().dropMessage(5, "@��õ�� : ��õ�� ��ϰ���.");  
            c.getPlayer().dropMessage(5, "~�Ҹ� : ��üä�� ����Դϴ� EX) ~�ȳ��ϼ���");
            c.getPlayer().dropMessage(5, "@�ߵ�Ȯ�� : �ڽ��� �߰��������� Ȯ���ϽǼ��ֽ��ϴ�.");
                    } else if (splitted[0].equals("@��õ��")) {
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 9010031, null);
        } else if (splitted[0].equals("@����")) {
            Map<Integer, Integer> connected = WorldConnected.getConnected(c.getWorld());
            StringBuilder conStr = new StringBuilder("���� �������� �ο� ");
            boolean first = true;
            for (int i : connected.keySet()) {
                if (!first) {
                    conStr.append(", ");
                } else {
                    first = false;
                }
                if (i == 0) {
                    conStr.append("��: ");
                    conStr.append(connected.get(i));
                }
            }
            c.getPlayer().dropMessage(6, conStr.toString());
        } else if (splitted[0].equals("@������������")) {
            IEquip equip = null;
            equip = (IEquip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -10);
            if (equip == null) {
                c.getPlayer().Message(1, "�������� �������Ⱑ �������� �ʽ��ϴ�.");
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
        } else if (splitted[0].equals("@��")) {
            c.sendPacket(MainPacketCreator.SkillUseResult((byte) 1));
            c.sendPacket(MainPacketCreator.resetActions(c.getPlayer()));
            c.getPlayer().dropMessage(5, "���� ���ŵǾ����ϴ�.");
        } else if (splitted[0].equals("@����")) {
            c.getPlayer().dropMessage(6, "������ �����մϴ�. �����̿Ϸ�Ǿ��ٴ� �������߱��� ����� ���������Ͻø�ȵ˴ϴ�.");
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage(5, "[�˸�] ������ �Ϸ�Ǿ����ϴ�.");
        } else if (splitted[0].equals("@��ų������")) {
            if (c.getPlayer().getLevel() < 10) {
                c.getPlayer().dropMessage(1, "���� 10 �̻� ���� ��� �� �� �ֽ��ϴ�.");
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
            c.getPlayer().dropMessage(5, "���� ������ ��ų������ �Ϸ�");
            
        } else if (splitted[0].equals("@����") || splitted[0].equals("@��׽ý�") || splitted[0].equals("@����")) {
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
             || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[�ý���] �ʺ��ڴ� �������� �̵� �� �� �����ϴ�.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(100000000);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "���� ��Ż�� ���� �ֽ��ϴ�.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("@����")){
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[�ý���] �ʺ��ڴ� ���������� �̵� �� �� �����ϴ�.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(970060000);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "���� ��Ż�� ���� �ֽ��ϴ�.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
        } else if (splitted[0].equals("@����")) {
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "�ʺ��ڴ� �����ͷ� �̵� �� �� �����ϴ�.");
                return;
            }
            int mapcode[] = {925060100, 925060200, 925060300, 925060400, 925060500, 925060700, 925060800, 925060900, 925061000, 925061100, 925061300, 925061400, 925061500, 925061600, 925061700, 925061900, 925062000, 925062100, 925062200, 925062300, 925062500, 925062600, 925062700, 925062800, 925062900, 925063100, 925063200, 925063300, 925063400, 925063500, 925063700, 925063800, 925063900, 925064000, 925064100, 925064300, 925064400, 925064500, 925064600, 925064700};

            for (int i = 0; i < mapcode.length; i++) {
                if (c.getPlayer().getMapId() == mapcode[i]) {
                    c.getPlayer().dropMessage(5, "[�ý���] �������� ������ �̵��Ҽ� �����ϴ�.");
                    return;
                }
                            if (c.getPlayer().getMapId() == 100000055) {
                c.getPlayer().dropMessage(5, "[�ý���] ���øʿ��� ������ �̵� �� �� �����ϴ�.");
                return;
            }
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(100000055);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "���� ��Ż�� ���� �ֽ��ϴ�.");
                } catch (NumberFormatException a) {
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("@����")){
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[�ý���] �ʺ��ڴ� ���������� �̵� �� �� �����ϴ�.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(323000101);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "���� ��Ż�� ���� �ֽ��ϴ�.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("@�۹�")){
            int jobid = c.getPlayer().getJob();
            if (jobid == 0 || jobid == 1000 || jobid == 2000 || jobid == 2001 || jobid == 2002 || jobid == 2003 || jobid == 2004
     || jobid == 3000 || jobid == 3001 || jobid == 5000 || jobid == 6000 || jobid == 6001 || jobid == 6002 || (jobid == 10112 && c.getPlayer().getMapId() == ServerConstants.startMap)) {
                c.getPlayer().dropMessage(5, "[�ý���] �ʺ��ڴ� �۹����� �̵� �� �� �����ϴ�.");
                return;
            }
            MapleMap target = c.getChannelServer().getMapFactory().getMap(100000001);
            MaplePortal targetPortal = null;
            if (splitted.length > 1) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[1]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "���� ��Ż�� ���� �ֽ��ϴ�.");
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
      } else if (splitted[0].equals("@ȯ��")) {
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 1404015, null);
       }
        		else if (splitted[0].equals("@���������"))
		{
			if (c.getPlayer().getKeyValue("magicg2") != null)
			{
				SkillFactory.getSkill(2001002).getEffect(1).applyTo(c.getPlayer(), c.getPlayer().getPosition());
			}
		}
		else if (splitted[0].equals("@�����������"))
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
            new CommandDefinition("��", "", "", 0),
            new CommandDefinition("��Ʈ", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("��", "", "", 0),
            new CommandDefinition("��", "", "", 0),
            new CommandDefinition("��", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("�۹�", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("ȯ��", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("����", "", "", 0),
            new CommandDefinition("��õ��", "", "", 0),
            new CommandDefinition("��ɾ�", "", "", 0),
            new CommandDefinition("��ų������", "", "", 0),
            new CommandDefinition("��õ��", "", "", 0),
            new CommandDefinition("�κ��ʱ�ȭ", "", "", 0),
            new CommandDefinition("������������", "", "", 0),
            new CommandDefinition("����", "", "", 0),  
            new CommandDefinition("���������", "", "", 0),         
            new CommandDefinition("�����������", "", "", 0), 
        };
        }
}
