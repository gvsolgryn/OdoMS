package handler.channel;

import java.util.Iterator;

import client.MapleCharacter;
import client.MapleClient;
import client.skills.ISkill;
import client.skills.SkillFactory;
import client.skills.SkillStatEffect;
import community.*;

import java.util.List;

import launch.ChannelServer;
import launch.holder.WideObjectHolder;
import packet.creators.MainPacketCreator;
import packet.transfer.read.ReadingMaple;
import tools.Timer.EventTimer;

public class GuildHandler {

    public static void DenyGuildRequest(ReadingMaple rh, MapleClient c) {
        rh.readByte();
        String from = rh.readMapleAsciiString();
        MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(from);
        if (cfrom != null) {
            cfrom.getClient().getSession().write(MainPacketCreator.denyGuildInvitation(c.getPlayer().getName()));
        }
    }
    
    public static void Sendrequest(ReadingMaple rh, MapleClient c) {
        final int guildId = rh.readInt();
        final MapleGuild guild = WideObjectHolder.getInstance().getGuild(guildId);
        if (guild == null) {
            System.out.println("길드에 가입을 요청하던 도중 길드가 없어졌거나, 비정상적 접근입니다. cid : " + c.getPlayer().getId() + ", name : " + c.getPlayer().getName());
            return;
        }
        guild.insertJoinRequester(c.getPlayer());
        
    }

    private static boolean isGuildNameAcceptable(String name) {
        if (name.getBytes().length < 4 || name.getBytes().length > 12) {
            return false;
        }
        return true;
    }

    private static void respawnPlayer(MapleCharacter mc) {
        mc.getMap().broadcastMessage(mc, MainPacketCreator.removePlayerFromMap(mc.getId()), false);
        mc.getMap().broadcastMessage(mc, MainPacketCreator.spawnPlayerMapobject(mc), false);
    }

    private static class Invited {

        public String name;
        public int gid;
        public long expiration;

        public Invited(String n, int id) {
            name = n.toLowerCase();
            gid = id;
            expiration = System.currentTimeMillis() + 60 * 60 * 1000;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Invited)) {
                return false;
            }
            Invited oth = (Invited) other;
            return (gid == oth.gid && name.equals(oth));
        }
    }

    private static java.util.List<Invited> invited = new java.util.LinkedList<Invited>();
    private static long nextPruneTime = System.currentTimeMillis() + 20 * 60 * 1000;

    public static void GuildOpertion(ReadingMaple rh, MapleClient c) {
        if (System.currentTimeMillis() >= nextPruneTime) {
            Iterator<Invited> itr = invited.iterator();
            Invited inv;
            while (itr.hasNext()) {
                inv = itr.next();
                if (System.currentTimeMillis() >= inv.expiration) {
                    itr.remove();
                }
            }
            nextPruneTime = System.currentTimeMillis() + 20 * 60 * 1000;
        }
        byte action = rh.readByte();
        switch (action) {
            case 0x04:
                if (c.getPlayer().getGuildId() > 0) {
                    c.getPlayer().dropMessage(1, "이미 길드에 가입되어 있어 길드를 만들 수 없습니다.");
                    return;
                } else if (c.getPlayer().getMeso() < 10000000) {
                    c.getPlayer().dropMessage(1, "길드 제작에 필요한 메소 [1000만 메소] 가 충분하지 않습니다.");
                    return;
                }
                String guildName = rh.readMapleAsciiString();

                if (!isGuildNameAcceptable(guildName)) {
                    c.getPlayer().dropMessage(1, "해당 길드 이름은 만들 수 없습니다.");
                    return;
                }
                int guildId;
                guildId = ChannelServer.createGuild(c.getPlayer().getId(), guildName);
                if (guildId == 0) {
                    c.getSession().writeAndFlush(MainPacketCreator.genericGuildMessage((byte) 0x1C));
                    return;
                }
                c.getPlayer().gainMeso(-10000000, true, false, true);
                c.getPlayer().setGuildId(guildId);
                c.getPlayer().setGuildRank(1);
                c.getPlayer().saveGuildStatus();
                c.getSession().writeAndFlush(MainPacketCreator.createGuildInfo(c.getPlayer()));
                c.getSession().writeAndFlush(MainPacketCreator.guildMemberOnline(guildId, c.getPlayer().getId(), true));
                ChannelServer.setGuildMemberOnline(c.getPlayer().getMGC(), true, c.getChannel());
                respawnPlayer(c.getPlayer());
                invited.clear();
                break;
            case 0x02: {
                final int guildid = rh.readInt();
                final MapleGuild guild = WideObjectHolder.getInstance().getGuild(guildid);
                if (guild == null) {
                    System.out.println(String.format("guild가 null입니다. (name : %s, cid : %d)", c.getPlayer().getName(), c.getPlayer().getId()));
                }
                c.getSession().writeAndFlush(MainPacketCreator.visitGuild(guild));
                break;
            }
            case 0x01: {
                final int guildid = rh.readInt();
                final MapleGuild guild = WideObjectHolder.getInstance().getGuild(guildid);
                if (guild != null) {
                    c.getSession().writeAndFlush(MainPacketCreator.visitGuild(guild));
                } else {
                    System.out.println(String.format("guild가 null입니다. (guildid : %d)", guildid));
                }
                break;
            }
            case 0x07:
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                    return;
                }
                String name = rh.readMapleAsciiString();
                MapleGuildResponse mgr = MapleGuild.sendInvite(c, name);

                if (mgr != null) {
                    c.getSession().writeAndFlush(mgr.getPacket());
                } else {
                    Invited inv = new Invited(name, c.getPlayer().getGuildId());
                    if (!invited.contains(inv)) {
                        invited.add(inv);
                    }
                }
                break;
            case 0x0B:
                int cid = rh.readInt();
                name = rh.readMapleAsciiString();

                if (cid != c.getPlayer().getId() || !name.equals(c.getPlayer().getName()) || c.getPlayer().getGuildId() <= 0) {
                    return;
                }
                ChannelServer.leaveGuild(c.getPlayer().getMGC());
                c.getSession().writeAndFlush(MainPacketCreator.showGuildInfo(null));
                c.getPlayer().setGuildId(0);
                c.getPlayer().saveGuildStatus();
                respawnPlayer(c.getPlayer());
                break;
            case 0x0C:
                cid = rh.readInt();
                name = rh.readMapleAsciiString();

                if (c.getPlayer().getGuildRank() > 2 || c.getPlayer().getGuildId() <= 0) {
                    return;
                }
                ChannelServer.expelMember(c.getPlayer().getMGC(), name, cid);
                break;
            case 0x12:
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
                    return;
                }
                String ranks[] = new String[5];
                for (int i = 0; i < 5; i++) {
                    ranks[i] = rh.readMapleAsciiString();
                }
                ChannelServer.changeRankTitle(c.getPlayer().getGuildId(), ranks);
                break;
            case 0x13:
                cid = rh.readInt();
                byte newRank = rh.readByte();

                if ((newRank <= 1 || newRank > 5) || c.getPlayer().getGuildRank() > 2 || (newRank <= 2 && c.getPlayer().getGuildRank() != 1) || c.getPlayer().getGuildId() <= 0) {
                    return;
                }
                ChannelServer.changeRank(c.getPlayer().getGuildId(), cid, newRank);
                break;
            case 0x14:
                short bg = rh.readShort();
                byte bgcolor = rh.readByte();
                short logo = rh.readShort();
                byte logocolor = rh.readByte();
                ChannelServer.setGuildEmblem(c.getPlayer().getGuildId(), bg, bgcolor, logo, logocolor);
                respawnPlayer(c.getPlayer());
                break;
            case 0x23:
                int SkilliD = rh.readInt();
                ISkill skilli = SkillFactory.getSkill(SkilliD);
                byte type = rh.readByte();
                final SkillStatEffect skillid = skilli.getEffect(ChannelServer.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId()));
                ChannelServer.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(), c.getPlayer().getId(), c.getPlayer(), type == 0 ? true : false);
                break;
            case 0x25:
                SkilliD = rh.readInt();
                skilli = SkillFactory.getSkill(SkilliD);
                if (SkilliD == 91001019) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("< " + c.getPlayer().getGuild().getName() + " > " + c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(rh.readMapleAsciiString());
                    c.getPlayer().getMap().broadcastMessage(MainPacketCreator.serverNotice(3, sb.toString()));
                } else if (SkilliD == 91001018) {
                    for (MapleGuildCharacter chr : c.getPlayer().getGuild().getMembers()) {
                        if (chr.getChannel() == c.getPlayer().getClient().getChannel()) {
                            chr.warp(c.getPlayer().getMapId());
                        }
                    }
                } else if (SkilliD == 91001017) {
                    int charId = rh.readInt();
                    MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterById(charId);
                    chr.Message(22, c.getPlayer().getName() + "님께서 길드스킬로 워프신청을 하셨습니다. [수락은 Y / 거절은 N]");
                    chr.GuildWarp = true;
                    chr.GuildMap = c.getPlayer().getMapId();
                    EventTimer.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            if (chr.GuildWarp) {
                                chr.GuildWarp = false;
                                chr.Message(22, "시간 초과로 워프신청이 자동으로 취소되었습니다.");
                                c.getPlayer().Message(22, "시간 초과로 워프신청이 자동으로 취소되었습니다.");
                            }
                        }
                    }, 10000);
                    break;
                } else if (SkilliD == 91001016) {
                    int charId = rh.readInt();
                    MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterById(charId);
                    c.getPlayer().warp(chr.getMapId());
                }
                break;
            case 0x28:
                cid = rh.readInt();
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 1) {
                    return;
                }
                ChannelServer.setGuildLeader(c.getPlayer().getGuildId(), cid);
                c.getPlayer().dropMessage(1, "길드마스터가 [" + c.getPlayer().getName() + "] 님에서 [" + cid + "]님으로 변경되었습니다.");
                break;
            case 44: {
                c.getPlayer().send(MainPacketCreator.showGuildNoblessSkill(30));
                break;
            }
            case 0x2D: {
                boolean textSearch = rh.readByte() == 0;
                if (textSearch) {
                    byte mode = rh.readByte();
                    rh.skip(1);
                    boolean likeSearch = rh.readByte() == 0;
                    String text = rh.readMapleAsciiString();
                    final List<MapleGuild> guilds = ChannelServer.findGuild(c.getWorld(), mode, text, likeSearch);
                    c.send(MainPacketCreator.guildSearchResult(guilds));
                } else {
                    int minGuildLevel = (byte) rh.readByte() & 0xFF;
                    int maxGuildLevel = (byte) rh.readByte() & 0xFF;
                    int minMemberSize = (byte) rh.readByte() & 0xFF;
                    int maxMemberSize = (byte) rh.readByte() & 0xFF;
                    int minAvgLevel = (byte) rh.readByte() & 0xFF;
                    int maxAvgLevel = (byte) rh.readByte() & 0xFF;
                    final List<MapleGuild> guilds = ChannelServer.findGuild(c.getWorld(), minGuildLevel, maxGuildLevel, minMemberSize, maxMemberSize, minAvgLevel, maxAvgLevel);
                    c.send(MainPacketCreator.guildSearchResult(guilds));
                }
                break;
            }
        }
    }

    public static void acceptJoinRequest(ReadingMaple rh, MapleCharacter chr) {
        byte count = rh.readByte();
        for (int next = 0; next < count; ++next) {
            int playerId = rh.readInt();
            MapleGuild guild = chr.getGuild();
            if (guild == null) {
                System.out.println("길드 가입 신청을 수락하는 도중에 길드가 null입니다. cid : " + chr.getId() + ", name : " + chr.getName());
                return;
            }
            MapleCharacter target = null;
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                target = cs.getPlayerStorage().getCharacterById(playerId);
                if (target != null) {
                    break;
                }
            }
            if (target != null) {
                target.setGuildId(guild.getId());
                target.setGuildRank(5);
            }

            int s;

            s = ChannelServer.addGuildMember(target.getMGC(), target.getClient());
            
            if (s == 0 && target != null) {
                target.dropMessage(1, "가입하려는 길드는 이미 정원이 꽉 찼습니다.");
                target.setGuildId(0);
                return;
            }
            if (target != null) {
                target.send(MainPacketCreator.showGuildInfo(target));
                target.saveGuildStatus();
                respawnPlayer(target);
                guild.removeJoinRequester(playerId, true);
            }
        }
    }

    public static void declineJoinRequest(ReadingMaple rh, MapleCharacter chr) {
        byte count = rh.readByte();
        for (int next = 0; next < count; ++next) {
            int playerId = rh.readInt();
            MapleGuild guild = chr.getGuild();
            if (guild == null) {
                System.out.println("길드 가입 신청을 거절하는 도중에 길드가 null입니다. cid : " + chr.getId() + ", name : " + chr.getName());
                return;
            }
            guild.removeJoinRequester(playerId, false);
        }
    }

    public static void joinRequest(ReadingMaple rh, MapleCharacter chr) {
        String removeTime = chr.getOneInfoQuest(26015, "remove_time");
        if (removeTime != null && !removeTime.isEmpty()) {
            if (Long.valueOf(removeTime) + 60 * 1000 > System.currentTimeMillis()) {
                chr.send(MainPacketCreator.genericGuildMessage((byte) 73));
                return;
            }
            chr.updateOneInfoQuest(26015, "remove_time", "");
        }
        final int guildId = rh.readInt();
        final MapleGuild guild = WideObjectHolder.getInstance().getGuild(guildId);
        if (guild == null) {
            System.out.println("길드에 가입을 요청하던 도중 길드가 없어졌거나, 비정상적 접근입니다. cid : " + chr.getId() + ", name : " + chr.getName());
            return;
        }
        guild.insertJoinRequester(chr);
    }

    public static void removeJoinRequest(ReadingMaple rh, MapleCharacter chr) {
        String guildid = chr.getOneInfoQuest(26015, "guild");
        if (guildid != null && guildid.isEmpty()) {
            return;
        }
        final MapleGuild guild = WideObjectHolder.getInstance().getGuild(Integer.parseInt(guildid));
        if (guild == null) {
            System.out.println("길드에 가입신청을 취소하는 도중 길드가 사라졌거나, 비정상적 접근입니다. cid : " + chr.getId() + ", name : " + chr.getName());
            return;
        }
        guild.removeJoinRequester(chr.getId(), false);
    }
}
