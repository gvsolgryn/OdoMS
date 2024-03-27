package handling.discord.manager;

import client.MapleCharacter;
import client.Skill;
import client.SkillFactory;
import constants.KoreaCalendar;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.auction.AuctionServer;
import handling.channel.ChannelServer;
import handling.discord.DiscordSetting;
import handling.world.World;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.internal.utils.Checks;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleItemInformationProvider;
import server.ShutdownServer;
import server.Timer;
import server.quest.MapleQuest;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CWvsContext;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class AdminCommandManager extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        event.getGuild().getVoiceChannelById(DiscordSetting.member_count)
                .getManager()
                .setName("Total Users: " + event.getGuild().getMemberCount())
                .queue();

        /*if (event.getMember().isOwner()) {
            String text = event.getMessage().getContentRaw();
            if (event.getMessage().getContentRaw().contains("~")) {
                event.getMessage().delete().queue();
                text = text.replace("~", "");
                event.getMessage().getChannel().sendMessage(text).queue();
                return;
            }
            adminCommand(event);
        } else {
            return;
        } */
    }

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "명령어": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("[" + DiscordSetting.serverName_EN + " 명령어]", null);
                    embed.addField("/명령어", "- 봇을 통해 사용할수 있는 명령어를 출력한다.", false);
                    embed.addField("/후원포인트지급 <닉네임> <포인트>", "- 해당 유저에게 후원포인트를 지급합니다.", false);
                    embed.addField("/홍보포인트지급 <닉네임> <포인트>", "- 해당 유저에게 후원포인트를 지급합니다.", false);
                    embed.addField("/지엠권한 <닉네임>", "- 플레이어에게 지엠권한을 부여합니다.", false);
                    embed.addField("/계정생성 <아이디> <비밀번호>", "- 새로운 계정을 생성합니다.", false);
                    embed.addField("/닉네임변경 <닉네임> <변경할닉네임>", "-닉네임 변경을 합니다", false);
                    embed.addField("/메소지급 <닉네임> <메소량>", "- 새로운 계정을 생성합니다.", false);
                    embed.addField("/모두종료", "- 모든 유저를 로그인서버로 팅굽니다.", false);
                    embed.addField("/연결끊기 <닉네임>", "- 해당 유저를 로그인서버로 팅굽니다.", false);
                    //embed.addField("!검색 <타입> <검색어>", "- 해당 검색어의 코드를 출력합니다.", false);
                    embed.addField("/핫타임지급 <아이템 코드> <갯수>", "- 접속중인 모든 유저에게 핫타임 보상을 보냅니다.", false);
                    //embed.addField("!리붓 <시간>", "- 설정한 시간에 서버가 리붓됩니다.", false);
                    embed.addField("/모두저장", "- 현재 접속중인 모든 유저의 정보를 저장합니다.", false);
                    embed.addField("/계정인증생성", "- 디스코드에 계정인증 생성 양식을 생성합니다.", false);
                    embed.addField("/약관생성", "- 디스코드에 약관 생성 양식을 생성합니다.", false);
                    //embed.addField("!임베드 <타이틀> <내용> <하단>", "디스코드에 임베드 텍스트를 생성합니다.", false);
                    //embed.addField("!링크생성 <URL> <버튼내용>", "- 디스코드에 URL 이동 버튼을 생성합니다.", false);
                    embed.setColor(Color.PINK);
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    break;
                }
            }
            case "후원포인트지급": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        MapleCharacter player = null;
                        player = cserv.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                        if (player != null) {
                            player.gainDonationPoint((event.getOption("액수").getAsInt()));
                            embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", event.getOption("닉네임").getAsString() + "님에게 " + event.getOption("액수").getAsInt() + " 후원포인트를 지급하셨습니다.", "", event);
                            return;
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                    break;
                }
            }
            case "홍보포인트지급": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        MapleCharacter player = null;
                        player = cserv.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                        if (player != null) {
                            player.gainHPoint(event.getOption("액수").getAsInt());
                            embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", event.getOption("닉네임").getAsString() + "님에게 " + event.getOption("액수").getAsInt() + " 홍보포인트를 지급하셨습니다.", "", event);
                            return;
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                    break;
                }
            }
            /*
            case "임베드": {
                embedMessage(message[1], message[2], message[3], event);
                break;
            } */
            case "약관생성": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("[" + DiscordSetting.serverName_KR + " 약관안내]", null);
                    embed.setDescription(DiscordSetting.serverName_EN + "에 오신것을 진심으로 환영합니다.\r\n디스코드를 시작전 아래 약관을 확인해주세요.");
                    embed.setFooter("[동의시 모든 약관을 확인하신걸로 간주합니다]\r\n\r\n- 타인을 비방,욕설등 불편하게 만드는 행위 (경고 -> 강퇴)\r\n- 타서버 언급 자제 (경고 -> 강퇴)\r\n- 홍보 행위 금지 (강퇴)\r\n- 서버에 대한 비방 및 비판 (강퇴)\r\n- 비인가 프로그램 사용 (영구정지)\r\n- 클라이언트 변조 행위 (영구정지)");
                    embed.setColor(Color.PINK);
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/1133445369397792848/1133451448571678740/9.jpg");
                    net.dv8tion.jda.api.interactions.components.buttons.Button button = net.dv8tion.jda.api.interactions.components.buttons.Button.success("role", "약관동의");
                    event.getChannel().sendMessageEmbeds(embed.build())
                        .addActionRow(button)
                        .queue();
                    break;
                }
            }
            case "계정인증생성": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("[" + DiscordSetting.serverName_KR + " 계정연동]", null);
                    embed.setDescription(DiscordSetting.serverName_EN + "에 오신것을 진심으로 환영합니다.\r\n처음 회원가입하신 분들은 계정연동을 진행하셔야 정상적인 게임 로그인이 가능합니다.\r\n접속기를 통해 가입하신 게임 아이디를 연동해주세요.");
                    embed.setFooter("※ 계정을 한번 등록하면 변경할수없습니다.");
                    embed.setColor(Color.PINK);
                    embed.setThumbnail("https://i.imgur.com/LtBvWHq.png");

                    net.dv8tion.jda.api.interactions.components.buttons.Button button = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("Account_Confirm", "연동하기");
                    event.getChannel().sendMessageEmbeds(embed.build())
                        .addActionRow(button)
                        .queue();
                    break;
                }
            }
            
            case "링크생성": {
                net.dv8tion.jda.api.interactions.components.buttons.Button button = Button.link(event.getOption("링크").getAsString(), event.getOption("이름").getAsString());
                event.getChannel().sendMessage("")
                        .addActionRow(button)
                        .queue();
                break;
            }
            case "지엠권한": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        MapleCharacter player = null;
                        player = cserv.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                        if (player != null) {
                            if (!player.isGM()) {
                                player.setGMLevel((byte) 10);
                                player.dropMessage(-1, "[알림] GM설정 되었습니다.");
                                embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", event.getOption("닉네임").getAsString() + "님을 GM으로 임명하셨습니다.", "", event);
                            } else {
                                player.setGMLevel((byte) 0);
                                player.dropMessage(-1, "[알림] GM해제 되었습니다.");
                                embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", event.getOption("닉네임").getAsString() + "님의 GM 권한을 박탈하였습니다.", "", event);
                            }
                            return;
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                    break;
                }
            }
            case "닉네임변경": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                }

                String user_id = event.getOption("닉네임").getAsString();
                String user_pw = event.getOption("변경할닉네임").getAsString();

                try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE characters SET name = ? WHERE name = ?")) {
                    ps.setString(1, user_pw);
                    ps.setString(2, user_id);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "기존 닉네임 : " + user_id + "\r\n변경후 닉네임 : " + user_pw + "\r\n정상적으로 닉네임이 변경 완료 되었습니다", "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "해당하는 인게임 닉네임의 캐릭터를 찾을 수 없습니다.", "", event);
                    }
                } catch (SQLException ex) {
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "닉네임 변경에 실패하였습니다.", "", event);
                }
                break;
            }

            case "계정생성": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    boolean register = false;
                    String user_id = event.getOption("아이디").getAsString();
                    String user_pw = event.getOption("비밀번호").getAsString();
                    Connection con = null;
                    try {
                        con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE name= ?");
                        ps.setString(1, user_id);
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next()) {
                            PreparedStatement ps2 = con.prepareStatement("INSERT INTO accounts (name, password) VALUES (?, ?)");
                            ps2.setString(1, user_id);
                            ps2.setString(2, user_pw);
                            ps2.executeUpdate();
                            ps2.close();
                            register = true;
                        }
                        rs.close();
                        ps.close();
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (register) {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "아이디 : " + user_id + "\r\n비밀번호 : " + user_pw + "\r\n\r\n아이디 생성을 완료하였습니다.", "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "이미 존재하는 아이디입니다.", "", event);
                    }
                    break;
                }
            }
            case "메소지급": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                    MapleCharacter hp = null;
                    hp = cs.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                    int meso = event.getOption("메소").getAsInt();
                    if (hp != null) {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", event.getOption("닉네임").getAsString() + "님에게 " + meso + "메소 를 지급하셨습니다.", "", event);
                        hp.gainMeso(meso, true);
                        return;
                    }
                }
                embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                break;
                }
            }
            case "아이템지급": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        MapleCharacter hp = null;
                        hp = cs.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                        int itemCode = event.getOption("아이템코드").getAsInt();
                        int itemNum = event.getOption("갯수").getAsInt();
                        if (hp != null) {
                            embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", event.getOption("닉네임").getAsString() + "님에게 " + itemCode + "를" + itemNum + " 개 지급하셨습니다.", "", event);
                            hp.gainItem(itemCode, itemNum);
                            return;
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                    break;
                }
            }
            case "캐비넷템지급": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        MapleCharacter player = null;
                        player = cserv.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                        int itemid = event.getOption("아이템코드").getAsInt();
                        int itemQ = event.getOption("갯수").getAsInt();
                        if (player != null) {
                            player.gainCabinetItem(itemid, itemQ);
                            embedMessage("[" + DiscordSetting.serverName_EN + " 명령어] ", event.getOption("닉네임").getAsString() + "님에게 " + itemid + "를 " + itemQ + "개 지급하셨습니다.", "", event);
                            return;
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                    break;
                }
            }
            case "핫타임지급": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    String players = "";
                    int itemid = Integer.valueOf(event.getOption("아이템코드").getAsInt());
                    int itemQ = Integer.valueOf(event.getOption("갯수").getAsInt());
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        for (MapleCharacter player : cs.getPlayerStorage().getAllCharacters().values()) {
                            player.gainCabinetItem(itemid, itemQ);
                            player.dropMessage(1, "[HOT] 핫타임 - 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
                            if (player != null) {
                                players += ", ";
                            }
                            players += player.getName();
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "핫타임이 지급되었습니다.\r\n아래는 핫타임을 지급받은 유저의 닉네임입니다.\n\n" + players, "", event);
                    break;
                }
            }
            case "모두종료": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.getPlayerStorage().disconnectAll();
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "모든 유저를 로그인서버로 연결을 종료하였습니다.", "", event);
                    break;
                }
            }
            case "연결끊기": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        MapleCharacter hp = null;
                        hp = cs.getPlayerStorage().getCharacterByName(event.getOption("닉네임").getAsString());
                        if (hp != null) {
                            hp.getClient().disconnect(true, false, true);
                            hp.getClient().getSession().close();
                            embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", hp + "님의 연결을 강제 종료하였습니다.", "", event);
                            return;
                        }
                    }
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "접속 중이 아니거나 존재하지 않는 닉네임입니다.", "", event);
                }
            }
            /*
            case "검색": {
                String type = message[1];
                String search = StringUtil.joinStringFrom(message, 2);
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File("wz/" + "String.wz"));
                embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "<<타입: " + type + " | 검색어: " + search + ">>", "", event);

                if (type.equalsIgnoreCase("엔피시")) {
                    java.util.List<String> retNpcs = new ArrayList<String>();
                    data = dataProvider.getData("Npc.img");
                    java.util.List<Pair<Integer, String>> npcPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData npcIdData : data.getChildren()) {
                        npcPairList.add(new Pair<Integer, String>(Integer.parseInt(npcIdData.getName()), MapleDataTool.getString(npcIdData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> npcPair : npcPairList) {
                        if (npcPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retNpcs.add(npcPair.getLeft() + " - " + npcPair.getRight());
                        }
                    }
                    String text = "";
                    if (retNpcs != null && retNpcs.size() > 0) {
                        for (String singleRetNpc : retNpcs) {
                            text += "" + singleRetNpc + "\r\n";
                        }
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", text, "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "입력한 엔피시코드를 찾을 수 없습니다.", "", event);
                    }

                } else if (type.equalsIgnoreCase("맵")) {
                    java.util.List<String> retMaps = new ArrayList<String>();
                    data = dataProvider.getData("Map.img");
                    java.util.List<Pair<Integer, String>> mapPairList = new LinkedList<Pair<Integer, String>>();
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
                    String text = "";
                    if (retMaps != null && retMaps.size() > 0) {
                        for (String singleRetMap : retMaps) {
                            text += "" + singleRetMap + "\r\n";
                        }
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", text, "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "입력한 맵코드를 찾을 수 없습니다.", "", event);
                    }
                } else if (type.equalsIgnoreCase("몹")) {
                    java.util.List<String> retMobs = new ArrayList<String>();
                    data = dataProvider.getData("Mob.img");
                    java.util.List<Pair<Integer, String>> mobPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData mobIdData : data.getChildren()) {
                        mobPairList.add(new Pair<Integer, String>(Integer.parseInt(mobIdData.getName()), MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> mobPair : mobPairList) {
                        if (mobPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMobs.add(mobPair.getLeft() + " - " + mobPair.getRight());
                        }
                    }
                    String text = "";
                    if (retMobs != null && retMobs.size() > 0) {
                        for (String singleRetMob : retMobs) {
                            text += "" + singleRetMob + "\r\n";
                        }
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", text, "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "입력한 몹코드를 찾을 수 없습니다.", "", event);
                    }
                } else if (type.equalsIgnoreCase("아이템")) {
                    java.util.List<String> retItems = new ArrayList<String>();
                    for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (itemPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retItems.add(itemPair.getLeft() + " - " + itemPair.getRight());
                        }
                    }
                    String text = "";
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            text += "" + singleRetItem + "\r\n";
                        }
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", text, "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "입력한 아이템코드를 찾을 수 없습니다.", "", event);
                    }
                } else if (type.equalsIgnoreCase("퀘스트")) {
                    java.util.List<String> retItems = new ArrayList<String>();
                    for (MapleQuest itemPair : MapleQuest.getAllInstances()) {
                        if (itemPair.getName().length() > 0 && itemPair.getName().toLowerCase().contains(search.toLowerCase())) {
                            retItems.add(itemPair.getId() + " - " + itemPair.getName());
                        }
                    }
                    String text = "";
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            text += "" + singleRetItem + "\r\n";
                        }
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", text, "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "입력한 퀘스트코드를 찾을 수 없습니다.", "", event);
                    }
                } else if (type.equalsIgnoreCase("스킬")) {
                    List<String> retSkills = new ArrayList<String>();
                    for (Skill skil : SkillFactory.getAllSkills()) {
                        if (skil.getName() != null && skil.getName().toLowerCase().contains(search.toLowerCase())) {
                            retSkills.add(skil.getId() + " - " + skil.getName());
                        }
                    }
                    String text = "";
                    if (retSkills != null && retSkills.size() > 0) {
                        for (String singleRetSkill : retSkills) {
                            text += "" + singleRetSkill + "\r\n";
                        }
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", text, "", event);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "입력한 스킬코드를 찾을 수 없습니다.", "", event);
                    }
                } else {
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "검색을 할 수 없습니다, 검색타입을 확인후 다시시도 해주세요.", "", event);
                }
                break;
            }
            */
            case "모두저장": {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("운영자 전용 명령어 입니다").queue();
                    return;
                } else {
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "게임 데이터 저장을 시작합니다.", "", event);
                    World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "게임 데이터 저장을 시작합니다. 잠시 렉이 걸려도 나가지 말아주세요."));
                    int saved = 0;
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                            chr.saveToDB(false, false);
                            chr.dropMessage(5, "저장되었습니다.");
                            saved++;
                        }
                    }
                    World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "총  " + saved + "명의 데이터가 저장되었습니다."));
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "총  " + saved + "명의 데이터가 저장되었습니다.", "", event);
                    break;
                }
            }
            /*
            case "리붓": {
                int time = Integer.parseInt(message[1]);

                try {
                    ServerConstants.reboottime = time;
                    KoreaCalendar kc = new KoreaCalendar();
                    int hour = kc.getHour();
                    int min = kc.getMin() + ServerConstants.reboottime;
                    if (min >= 60) {
                        hour++;
                        min -= 60;
                        if (hour >= 24) {
                            hour = 0;
                        }
                    }
                    String am = (hour >= 12) ? "오후" : "오전";
                    String type = "점검이";
                    if (ServerConstants.ts == null && (ServerConstants.t == null || !ServerConstants.t.isAlive())) {
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            cserv.setServerMessage("안녕하세요! " + DiscordSetting.serverName_KR + " 입니다. 잠시 후 " + am + " " + hour + "시 " + min + "분 부터 서버" + type + " 진행됩니다. 원활한 진행을 위해 지금 바로 접속을 종료해주시기 바랍니다. 이용에 불편을 끼쳐 드려 죄송합니다.");
                        }
                        AuctionServer.saveItems();
                        ServerConstants.t = new Thread(ShutdownServer.getInstance());
                        ServerConstants.ts = Timer.EventTimer.getInstance().register(new Runnable() {
                            public void run() {
                                if (ServerConstants.reboottime == 0) {
                                    ShutdownServer.getInstance().shutdown();
                                    ServerConstants.t.start();
                                    ServerConstants.ts.cancel(false);
                                    return;
                                }
                                ServerConstants.reboottime--;
                            }
                        }, 60000L);
                    } else {
                        embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "이미 리붓 시간이 지정되어 있습니다.\r\n남은 리붓 시간 : " + ServerConstants.reboottime + "분", "", event);
                    }
                } catch (Exception e) {
                    embedMessage("[" + DiscordSetting.serverName_EN + " 명령어]", "리붓 시간을 숫자로 입력해주세요.", "", event);
                }
                break;
            }
            */
        }
    }
    
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("도움말", "봇을 통해 사용할수 있는 명령어를 출력한다."));
        commandData.add(Commands.slash("동접", "현재 접속중인 유저수를 출력한다."));
        commandData.add(Commands.slash("서버정보", DiscordSetting.serverName_KR + " 서버 정보를 확인합니다."));
        commandData.add(Commands.slash("캐릭터정보", "해당 플레이어의 캐릭터정보를 불러옵니다.").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true));
        commandData.add(Commands.slash("캐릭터찾기", "해당 플레이어의 접속위치를 불러옵니다.").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true));
        commandData.add(Commands.slash("명령어", "명령어 확인 (운영자 전용)"));
        commandData.add(Commands.slash("후원포인트지급", "후원 포인트 지급 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true).addOption(OptionType.INTEGER, "액수", "후원 포인트 액수", true));
        commandData.add(Commands.slash("홍보포인트지급", "홍보 포인트 지급 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true).addOption(OptionType.INTEGER, "액수", "후원 포인트 액수", true));
        commandData.add(Commands.slash("메소지급", "메소 지급 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true).addOption(OptionType.INTEGER, "메소", "보낼 메소양을 입력해주세요", true));
        commandData.add(Commands.slash("아이템지급", "아이템 지급 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true).addOption(OptionType.INTEGER, "아이템코드", "아이템 코드를 입력해주세요", true).addOption(OptionType.INTEGER, "갯수", "아이템 갯수를 입력해주세요", true));
        commandData.add(Commands.slash("캐비넷템지급", "아이템 지급 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요.", true).addOption(OptionType.INTEGER, "아이템코드", "캐릭터 닉네임을 입력해주세요.", true).addOption(OptionType.INTEGER, "갯수", "지급할 아이템의 갯수를 입력 해주세요", true));
        commandData.add(Commands.slash("핫타임지급", "핫타임 아이템 지급 (운영자 전용)").addOption(OptionType.INTEGER, "아이템코드", "아이템 코드를 입력해주세요.", true).addOption(OptionType.INTEGER, "갯수", "지급할 아이템의 갯수를 입력 해주세요", true));
        commandData.add(Commands.slash("계정생성", "계정 생성 (운영자 전용)").addOption(OptionType.STRING, "아이디", "아이디를 입력해주세요", true).addOption(OptionType.STRING, "비밀번호", "비밀번호를 입력해주세요.", true)); 
        commandData.add(Commands.slash("지엠권한", "유저 지엠 권한 설정 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "GM ON/OFF", true));
        commandData.add(Commands.slash("연결끊기", "유저 연결 끊기 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "유저 연결 강제로 끊기", true));
        commandData.add(Commands.slash("닉네임변경", "유저 닉네임 변경 (운영자 전용)").addOption(OptionType.STRING, "닉네임", "캐릭터 닉네임을 입력해주세요", true).addOption(OptionType.STRING, "변경할닉네임", "변경할 닉네임을 적어주세요", true));
        commandData.add(Commands.slash("모두저장", "채널 모두 저장 (운영자 전용)"));
        commandData.add(Commands.slash("약관생성", "채널 모두 저장 (운영자 전용)")); 
        commandData.add(Commands.slash("계정인증생성", "채널 모두 저장 (운영자 전용)"));
        commandData.add(Commands.slash("링크생성", "링크 생성 (운영자 전용)").addOption(OptionType.STRING, "링크", "링크를 넣어주세요", true).addOption(OptionType.STRING, "이름", "이름을 적어주세요", true));
        event.getGuild().updateCommands()
                .addCommands(commandData)
                .queue();
    }
    
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("role")) {    
            Member member = event.getMember();
            Guild guild = event.getGuild();
            Role role = guild.getRoleById(1133429138766700565L);
            guild.addRoleToMember(member, role).queue();     
        }
    }

    public AuditableRestAction<Void> kick(String userId, String reason, MessageReceivedEvent event) {
        Member member = event.getGuild().getMemberById(userId);
        Checks.check(member != null, "The provided userId does not correspond to a member in this guild! Provided userId: %s", userId);
        return null;
    }
    
    public void embedMessage(String title, String talk, String foot, SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(title, null);
        embed.setDescription(talk);
        embed.setFooter(foot);
        embed.setColor(Color.gray);
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
    
}
