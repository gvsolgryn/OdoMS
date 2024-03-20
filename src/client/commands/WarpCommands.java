package client.commands;

import client.MapleCharacter;
import client.MapleClient;
import launch.ChannelServer;
import launch.holder.MapleWhereAreYou;
import launch.world.WorldConnected;
import packet.creators.SLFCGPacket;
import server.maps.MapleMap;
import server.maps.MaplePortal;

public class WarpCommands implements Command {

    @SuppressWarnings("null")
    @Override
    public void execute(MapleClient c, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        ChannelServer cserv = c.getChannelServer();
        if (splitted[0].equals("!����")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getPosition()));
                victim.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
            } else {
                MapleCharacter chr = null;
                for (ChannelServer cserv_ : ChannelServer.getAllInstances()) {
                    chr = cserv_.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (chr != null) {
                        break;
                    }
                }
                if (chr != null) {
                    MapleWhereAreYou loc = WorldConnected.getLocation(splitted[1]);
                    c.getPlayer().dropMessage(6, "ä���� �����Ͽ� �����մϴ�. ��ø� ��ٷ��ּ���.");
                    c.getPlayer();
                    MapleCharacter.crossChannelWarp(c, loc.map, (byte) loc.channel);
                    victim.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
                } else {
                    c.getPlayer().dropMessage(6, "��� �÷��̾ �߰����� ���߽��ϴ�.");
                }
            }
        } else if (splitted[0].equals("!��ȯ")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (victim.getMapId() == c.getPlayer().getMapId()) {
                    victim.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
                } else {
                    victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(c.getPlayer().getPosition()));
                    victim.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
                }
            } else {
                MapleCharacter chr = null;
                for (ChannelServer cserv_ : ChannelServer.getAllInstances()) {
                    chr = cserv_.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (chr != null) {
                        break;
                    }
                }
                if (chr != null) {
                    MapleWhereAreYou loc = WorldConnected.getLocation(c.getPlayer().getName());
                    chr.dropMessage(6, "ä���� �����Ͽ� ��ȯ�˴ϴ�.");
                    MapleCharacter.crossChannelWarp(chr.getClient(), loc.map, (byte) loc.channel);
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
                } else {
                    c.getPlayer().dropMessage(6, "��� �÷��̾ �߰����� ���߽��ϴ�.");
                }
            }
        } else if (splitted[0].equals("!��ü��ȯ")) {
            for (MapleCharacter victim : cserv.getPlayerStorage().getAllCharacters()) {
                if (victim.getMapId() != c.getPlayer().getMapId()) {
                    victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getPosition());
                    victim.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
                } else {
                    MapleCharacter chr = null;
                    for (ChannelServer cserv_ : ChannelServer.getAllInstances()) {
                        chr = cserv_.getPlayerStorage().getAllCharacters().get(cserv_.getPlayerStorage().getAllCharacters().indexOf(chr));
                        if (chr != null) {
                            break;
                        }
                    }
                    if (chr != null) {
                        MapleWhereAreYou loc = WorldConnected.getLocation(c.getPlayer().getName());
                        chr.dropMessage(6, "ä���� �����Ͽ� ��ȯ�˴ϴ�.");
                        MapleCharacter.crossChannelWarp(chr.getClient(), loc.map, (byte) loc.channel);
                        chr.getClient().getSession().writeAndFlush(SLFCGPacket.CharReLocationPacket(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y));
                    }
                    chr.dropMessage(6, "��ü��ȯ �Ǿ����ϴ�.");
                }
            }
        } else if (splitted[0].equals("!��")) {
            MapleMap target = null;
            if (c.getPlayer().getEventInstance() != null) {
                target = c.getPlayer().getEventInstance().getMapFactory().getMap(Integer.parseInt(splitted[1]));
            } else {
                target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
            }

            MaplePortal targetPortal = null;
            if (splitted.length > 2) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                } catch (IndexOutOfBoundsException e) {
                    c.getPlayer().dropMessage(5, "Invalid portal selected.");
                } catch (NumberFormatException a) {
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
        }
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{
            new CommandDefinition("����", "<�÷��̾��̸�> (<Ÿ�ٸ�ID>)", "�ڽ��� �ش� �÷��̾�� �����մϴ�. �� ID�� �Էµ� ��� �Է��� �÷��̾ �ش� ������ �̵���ŵ�ϴ�.", 2),
            new CommandDefinition("��ȯ", "<�÷��̾��̸�>", "�ش� �÷��̾ �ڽ��� ��ġ�� ��ȯ�մϴ�.", 2),
            new CommandDefinition("��ü��ȯ", "<��ü��ȯ>", "��������� �ڽ��� ��ġ�� ��ȯ�մϴ�.", 6),
            new CommandDefinition("��", "<��ID>", "�ش� ��ID�� ������ �̵��մϴ�.", 2),};

    }
}
