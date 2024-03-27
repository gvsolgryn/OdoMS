package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import constants.GameConstants;
import constants.KoreaCalendar;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuildResponse;
import server.SecondaryStatEffect;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuildHandler {

    public static final void DenyGuildRequest(String from, MapleClient c) {
        MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(from);
        if (cfrom != null) {
            cfrom.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.denyGuildInvitation(c.getPlayer().getName()));
        }
    }

    private static boolean isGuildNameAcceptable(String name) throws UnsupportedEncodingException {
        if ((name.getBytes("EUC-KR")).length < 2 || (name.getBytes("EUC-KR")).length > 12) {
            return false;
        }
        return true;
    }

    private static void respawnPlayer(MapleCharacter mc) {
        if (mc.getMap() == null) {
            return;
        }
        mc.getMap().broadcastMessage(CField.loadGuildIcon(mc));
    }

    public static final void GuildCancelRequest(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        MapleGuild guild = World.Guild.getGuild(slea.readInt());
        if (c == null || chr == null || guild == null) {
            return;
        }
        chr.setKeyValue(26015, "name", "");
        chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
        guild.removeRequest(chr.getId());
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(chr, guild));
        List<MapleGuild> g = new ArrayList<>();
        for (MapleGuild guilds : World.Guild.getGuilds()) {
            if (guilds.getRequest(c.getPlayer().getId()) != null) {
                g.add(guilds);
            }
        }
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
    }

    public static final void GuildJoinRequest(LittleEndianAccessor slea, MapleCharacter chr) {
        int gid = slea.readInt();
        String requestss = slea.readMapleAsciiString();
        slea.skip(10);
        if (chr == null || gid <= 0) {
            return;
        }
        if (chr.getKeyValue(26015, "time") + 60000L >= System.currentTimeMillis()) {
            chr.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.DelayRequest());
            return;
        }
        MapleGuild g = World.Guild.getGuild(gid);
        MapleGuildCharacter mgc2 = new MapleGuildCharacter(chr);
        mgc2.setGuildId(gid);
        mgc2.setRequest(requestss);
        if (request.get(Integer.valueOf(chr.getId())) == null) {
            if (g.addRequest(mgc2)) {
                g.broadcast(CWvsContext.GuildPacket.addRegisterRequest(mgc2));
                chr.dropMessage(5, "[" + g.getName() + "] 길드 가입 요청이 성공 하였습니다.");
            }
            chr.setKeyValue(26015, "name", g.getName());
            chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
        } else {
            request.remove(Integer.valueOf(chr.getId()));
            if (g.addRequest(mgc2)) {
                chr.dropMessage(5, "[" + g.getName() + "] 길드 가입 요청이 성공 하였습니다.");
                g.broadcast(CWvsContext.GuildPacket.addRegisterRequest(mgc2));
            }
            chr.setKeyValue(26015, "name", "");
            chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
        }
        List<MapleGuild> gs = new ArrayList<>();
        for (MapleGuild guilds : World.Guild.getGuilds()) {
            if (guilds.getRequest(chr.getId()) != null) {
                gs.add(guilds);
            }
        }
        chr.getClient().send(CWvsContext.GuildPacket.RequestListGuild(gs));
        chr.getClient().send(CWvsContext.GuildPacket.RecruitmentGuild(chr));
        g.writeToDB(false);
    }

    public static final void GuildJoinDeny(LittleEndianAccessor slea, MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        byte action = slea.readByte();
        for (int i = 0; i < action; i++) {
            int cid = slea.readInt();
            if (chr.getGuildId() > 0 && chr.getGuildRank() <= 2) {
                MapleGuild g = chr.getGuild();
                if (chr.getGuildRank() <= 2) {
                    g.removeRequest(cid);
                    int ch = World.Find.findChannel(cid);
                    if (ch < 0) {
                        return;
                    }
                    MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
                    c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(c, g));
                    chr.setKeyValue(26015, "name", "");
                    request.put(Integer.valueOf(cid), Long.valueOf(System.currentTimeMillis()));
                } else {
                    chr.dropMessage(6, "길드 권한이 부족합니다.");
                }
            }
        }
    }

    public static final void GuildRegisterAccept(LittleEndianAccessor slea, MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        byte action = slea.readByte();
        for (int i = 0; i < action; i++) {
            int cid = slea.readInt();
            if (chr.getGuildId() > 0 && chr.getGuildRank() <= 2) {
                MapleGuild g = chr.getGuild();
                if (chr.getGuildRank() <= 2 && g != null) {
                    MapleCharacter c = null;
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        c = cs.getPlayerStorage().getCharacterById(cid);
                        if (c != null) {
                            MapleGuildCharacter temp = g.getRequest(cid);
                            g.addGuildMember(temp);
                            c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(chr));
                            g.removeRequest(cid);
                            c.setGuildId(g.getId());
                            c.setGuildRank((byte) 5);
                            c.saveGuildStatus();
                            c.setKeyValue(26015, "name", "");
                            c.getClient().send(CWvsContext.GuildPacket.guildLoadAattendance());
                            c.dropMessage(5, "`" + g.getName() + "` 길드에 가입 되었습니다.");
                            for (MapleGuild guilds : World.Guild.getGuilds()) {
                                if (guilds.getRequest(c.getId()) != null) {
                                    guilds.removeRequest(c.getId());
                                }
                            }
                            respawnPlayer(c);
                            break;
                        }
                    }
                    if (c == null) {
                        MapleGuildCharacter temp = OfflineMapleGuildCharacter(cid, chr.getGuildId());
                        if (temp != null) {
                            temp.setOnline(false);
                            g.addGuildMember(temp);
                            MapleGuild.setOfflineGuildStatus(g.getId(), (byte) 5, 0, (byte) 5, cid);
                            g.removeRequest(cid);
                            for (MapleGuild guilds : World.Guild.getGuilds()) {
                                if (guilds.getRequest(temp.getId()) != null) {
                                    guilds.removeRequest(temp.getId());
                                }
                            }
                        } else {
                            chr.dropMessage(5, "존재하지 않는 캐릭터입니다.");
                        }
                    }
                } else {
                    chr.dropMessage(6, "길드 권한이 부족합니다.");
                }
            }
        }
    }

    public static final MapleGuildCharacter OfflineMapleGuildCharacter(int cid, int gid) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM characters where id = ?");
            ps.setInt(1, cid);
            rs = ps.executeQuery();
            if (rs.next()) {
                byte gRank = rs.getByte("guildrank"), aRank = rs.getByte("alliancerank");
                return new MapleGuildCharacter(cid, rs.getShort("level"), rs.getString("name"), (byte) -1, rs.getInt("job"), gRank, rs.getInt("guildContribution"), aRank, gid, false, 0);
            }
            ps.close();
            rs.close();
        } catch (SQLException se) {
            System.err.println("Error Laod Offline MapleGuildCharacter");
            se.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GuildHandler.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GuildHandler.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static final void GuildRequest(int guildid, MapleCharacter player) {
        player.dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
    }

    public static void cancelGuildRequest(MapleClient c, MapleCharacter player) {
        player.dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
    }

    public static void SendGuild(LittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
    }

    private static class Invited {
        public String name;
        public int gid;
        public long expiration;

        public Invited(String n, int id) {
            this.name = n.toLowerCase();
            this.gid = id;
            this.expiration = System.currentTimeMillis() + 3600000L;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Invited)) {
                return false;
            }
            Invited oth = (Invited) other;
            return (this.gid == oth.gid && this.name.equals(oth));
        }
    }

    private static List<Invited> invited = new LinkedList<>();

    private static Map<Integer, Long> request = new LinkedHashMap<>();

    private static long nextPruneTime = System.currentTimeMillis() + 300000L;

    public static final void Guild(LittleEndianAccessor slea, MapleClient c) {
        if (System.currentTimeMillis() >= nextPruneTime) {
            Iterator<Invited> itr = invited.iterator();

            while (itr.hasNext()) {
                Invited inv = itr.next();
                if (System.currentTimeMillis() >= inv.expiration) {
                    itr.remove();
                }
            }
            nextPruneTime += 300000L;
        }
        try {
            String str1;
            int cid;
            String[] arrayOfString1;
            int[] roles;
            String ranks[], notice;
            MapleGuild mapleGuild1;
            List<MapleGuild> g;
            String name;
            MapleGuild guild;
            short mode;
            Skill skilli;
            int sid;
            String guildName, str2;
            byte newRank;
            int i, arrayOfInt1[];
            byte isCustomImage;
            MapleGuildResponse mgr;
            int size;
            String text;
            int eff, guildId, j;
            Invited inv;
            int option;
            SecondaryStatEffect skillid;
            MapleGuild mapleGuild2;
            String[] arrayOfString2;
            int a, k;
            KoreaCalendar kc;
            int action = slea.readByte();
            switch (action) {
                case 1:
                    str1 = slea.readMapleAsciiString();
                    c.getPlayer().setGuildName(str1);
                    c.getSession().writeAndFlush(CWvsContext.GuildPacket.genericGuildMessage(42, str1));
                    break;

                case 6:
                    cid = slea.readInt();
                    str2 = slea.readMapleAsciiString();

                    if (cid != c.getPlayer().getId() || !str2.equals(c.getPlayer().getName()) || c.getPlayer().getGuildId() <= 0) {
                        return;
                    }
                    World.Guild.leaveGuild(c.getPlayer().getMGC());
                    break;

                case 7:
                    cid = slea.readInt();
                    str2 = slea.readMapleAsciiString();

                    if (c.getPlayer().getGuildRank() > 2 || c.getPlayer().getGuildId() <= 0) {
                        return;
                    }
                    World.Guild.expelMember(c.getPlayer().getMGC(), str2, cid);
                    respawnPlayer(c.getPlayer());
                    break;

                case 10:
                    cid = slea.readInt();
                    newRank = slea.readByte();

                    if (newRank <= 1 || newRank > 5 || c.getPlayer().getGuildRank() > 2 || (newRank <= 2 && c.getPlayer().getGuildRank() != 1) || c.getPlayer().getGuildId() <= 0) {
                        return;
                    }

                    World.Guild.changeRank(c.getPlayer().getGuildId(), cid, newRank);
                    break;

                case 12: //361 ok
                    if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
                        return;
                    }
                    byte type = (byte) (slea.readByte() - 1);
                    String ranks1 = slea.readMapleAsciiString();
                    int role = type != 0 ? slea.readInt() : -1;
                    World.Guild.changeRankTitleRole(c.getPlayer(), ranks1, role, type);
                    break;
                case 17: // 362 ok
                    notice = slea.readMapleAsciiString();
                    if (notice.length() > 100 || c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                        return;
                    }
                    World.Guild.setGuildNotice(c.getPlayer(), notice);
                    break;

                case 16: // 355 +1
                    mapleGuild1 = World.Guild.getGuild(c.getPlayer().getGuildId());
                    if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
                        c.getPlayer().dropMessage(1, "길드가 없거나 마스터가 아닙니다.");

                        return;
                    }
                    isCustomImage = slea.readByte();
                    if (isCustomImage == 0) {
                        /*
                        if (mapleGuild1.getGP() >= 150000 && mapleGuild1.getLevel() >= 2) {
                            mapleGuild1.setGuildGP(mapleGuild1.getGP() - 150000);
                        } else {
                            c.getPlayer().getClient().send(CWvsContext.GuildPacket.genericGuildMessage(136));
                            return;
                        }
                         */
                        short bg = slea.readShort();
                        byte bgcolor = slea.readByte();
                        short logo = slea.readShort();
                        byte logocolor = slea.readByte();

                        World.Guild.setGuildEmblem(c.getPlayer(), bg, bgcolor, logo, logocolor);
                    } else {
                        if (mapleGuild1.getGP() >= 250000) {
                            mapleGuild1.setGuildGP(mapleGuild1.getGP() - 250000);
                        } else {
                            c.getPlayer().dropMessage(1, "[알림] GP가 부족합니다.");
                            c.getPlayer().getClient().send(CWvsContext.enableActions(c.getPlayer()));
                            return;
                        }
                        int m = slea.readInt();
                        byte[] imgdata = new byte[m];
                        for (int n = 0; n < m; n++) {
                            imgdata[n] = slea.readByte();
                        }

                        World.Guild.setGuildCustomEmblem(c.getPlayer(), imgdata);
                    }

                    respawnPlayer(c.getPlayer());
                    break;

                case 29: //아직
                    mapleGuild1 = World.Guild.getGuild(c.getPlayer().getGuildId());
                    if (mapleGuild1 == null) {
                        return;
                    }
                    if (mapleGuild1.getGP() < 50000) {
                        c.getPlayer().dropMessage(1, "GP가 부족합니다.");
                        c.send(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    mapleGuild1.setGuildGP(mapleGuild1.getGP() - 50000);
                    mapleGuild1.getSkills().clear();
                    mapleGuild1.broadcast(CWvsContext.GuildPacket.showGuildInfo(mapleGuild1));
                    c.getPlayer().dropMessage(1, "길드스킬 초기화가 완료 되었습니다. 길드창을 닫았다 열어주세요.");
                    break;
                case 30: //355 OK
                    cid = slea.readInt();
                    if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 1) {
                        return;
                    }
                    World.Guild.setGuildLeader(c.getPlayer().getGuildId(), cid);
                    break;
                case 32:
                    mapleGuild1 = World.Guild.getGuild(slea.readInt());
                    c.getSession().writeAndFlush(CWvsContext.GuildPacket.LooksGuildInformation(mapleGuild1));
                    break;

                case 33:
                    g = new ArrayList<>();
                    for (MapleGuild mapleGuild : World.Guild.getGuilds()) {
                        if (mapleGuild.getRequest(c.getPlayer().getId()) != null) {
                            g.add(mapleGuild);
                        }
                    }
                    c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
                    break;

                case 34: //355 -3
                    if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                        return;
                    }
                    name = slea.readMapleAsciiString();
                    mgr = MapleGuild.sendInvite(c, name);

                    if (mgr != null) {
                        c.getSession().writeAndFlush(mgr.getPacket());
                        break;
                    }
                    inv = new Invited(name, c.getPlayer().getGuildId());
                    if (!invited.contains(inv)) {
                        invited.add(inv);
                    }
                    break;

                case 35: // 362 ok
                    guild = World.Guild.getGuild(c.getPlayer().getGuildId());
                    size = 0;
                    for (MapleGuildCharacter member : guild.getMembers()) {
                        if (member.getLastAttendance(member.getId()) == GameConstants.getCurrentDateday()) {
                            size++;
                        }
                    }
                    c.getPlayer().setLastAttendance(GameConstants.getCurrentDateday());
                    guild.setAfterAttance(guild.getAfterAttance() + 30);
                    if (size == 10 || size == 30 || size == 60 || size == 100) {
                        int point = (size == 100) ? 2000 : ((size == 60) ? 1000 : ((size == 30) ? 100 : 50));
                        guild.setAfterAttance(guild.getAfterAttance() + point);
                        guild.setGuildFame(guild.getFame() + point);
                        guild.setGuildGP(guild.getGP() + point / 100 * 30);
                    }
                    c.getPlayer().saveGuildStatus();
                    World.Guild.gainContribution(guild.getId(), 30, c.getPlayer().getId());
                    GuildBroadCast(CWvsContext.GuildPacket.guildAattendance(guild, c.getPlayer()), guild);
                    if (guild.getFame() >= GameConstants.getGuildExpNeededForLevel(guild.getLevel())) {
                        guild.setGuildLevel(guild.getLevel() + 1);
                        GuildBroadCast(CWvsContext.serverNotice(5, "", "<길드> 길드의 레벨이 상승 하였습니다."), guild);
                    }
                    break;

                case 39: //355 -10
                    mode = slea.readShort();
                    text = slea.readMapleAsciiString();
                    if (mode == 4) {
                        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
                        break;
                    }
                    option = slea.readShort();
                    slea.skip(2);
                    c.getSession().writeAndFlush(CWvsContext.GuildPacket.showSearchGuildInfo(c.getPlayer(), World.Guild.getGuildsByName(text, (option == 1), (byte) mode), text, (byte) mode, option));
                    break;

                case 44: // 355 -10
                    skilli = SkillFactory.getSkill(slea.readInt());
                    if (c.getPlayer().getGuildId() <= 0 || skilli == null || skilli.getId() < 91000000) {
                        return;
                    }
                    eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId()) + 1;
                    if (eff > skilli.getMaxLevel()) {
                        return;
                    }
                    skillid = skilli.getEffect(eff);
                    if (skillid.getReqGuildLevel() < 0 || c.getPlayer().getMeso() < skillid.getPrice()) {
                        return;
                    }
                    if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(), c.getPlayer().getId()))
                        ;
                    break;

                case 46:
                    if (c.getPlayer().getGuildId() <= 0) {
                        return;
                    }
                    sid = slea.readInt();
                    eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), sid);
                    SkillFactory.getSkill(sid).getEffect(eff).applyTo(c.getPlayer());
                    c.getSession().writeAndFlush(CField.skillCooldown(sid, 3600000));
                    c.getPlayer().addCooldown(sid, System.currentTimeMillis(), 3600000L);
                    break;

                case 59: // 355 -10
                    if (c.getPlayer().getGuildId() > 0) {
                        c.getPlayer().dropMessage(1, "이미 길드에 가입되어 있어 길드를 만들 수 없습니다.");
                        return;
                    }
                    if (c.getPlayer().getMeso() < 5000000L) {
                        c.getPlayer().dropMessage(1, "길드 제작에 필요한 메소 [500만 메소] 가 충분하지 않습니다.");
                        return;
                    }
                    guildName = c.getPlayer().getGuildName();
                    if (!isGuildNameAcceptable(guildName)) {
                        c.getPlayer().dropMessage(1, "해당 길드 이름은 만들 수 없습니다.");
                        return;
                    }
                    guildId = World.Guild.createGuild(c.getPlayer().getId(), guildName);
                    if (guildId == 0) {
                        c.getPlayer().dropMessage(1, "잠시후에 다시 시도 해주세요.");
                        return;
                    }
                    c.getPlayer().gainMeso(-5000000L, true, true);
                    c.getPlayer().setGuildId(guildId);
                    c.getPlayer().setGuildRank((byte) 1);
                    c.getPlayer().saveGuildStatus();
                    mapleGuild2 = World.Guild.getGuild(guildId);
                    arrayOfString2 = new String[5];
                    arrayOfString2[0] = "마스터";
                    arrayOfString2[1] = "부마스터";
                    a = 1;
                    for (k = 2; k < 5; k++) {
                        arrayOfString2[k] = "길드원" + a;
                        a++;
                    }
                    mapleGuild2.changeRankTitle(c.getPlayer(), arrayOfString2);
                    mapleGuild2.setLevel(1);
                    kc = new KoreaCalendar();
                    mapleGuild2.setLastResetDay(Integer.parseInt(kc.getYears() + kc.getMonths() + kc.getDays()));
                    World.Guild.setGuildMemberOnline(c.getPlayer().getMGC(), true, c.getChannel());
                    c.getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildInfo(c.getPlayer()));
                    c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(c.getPlayer()));

                    respawnPlayer(c.getPlayer());
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void guildRankingRequest(byte type, MapleClient c) {
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildRanks((byte) type, c, MapleGuildRanking.getInstance()));
    }

    public static void GuildBroadCast(byte[] packet, MapleGuild guild) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                if (chr.getGuildId() == guild.getId()) {
                    chr.getClient().getSession().writeAndFlush(packet);
                }
            }
        }
    }
}
