package handling.discord.manager;

import client.MapleCharacter;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.discord.DiscordSetting;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.guild.MapleGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import server.maps.MapleMap;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static handling.world.World.Guild.getGuild;

public class SlashCommandManager extends ListenerAdapter {
    private static String enter = "\r\n";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "도움말" : {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("[" + DiscordSetting.serverName_EN + " 명령어]", null);
                embed.addField("/명령어", "- 봇을 통해 사용할수 있는 명령어를 출력한다.", false);
                embed.addField("/동접", "- 현재 접속중인 유저수를 출력한다.", false);
                embed.addField("/서버정보", "- 현재 적용된 서버 정보를 확인합니다.", false);
                embed.addField("/캐릭터정보 <닉네임>", "- 해당 캐릭터의 정보를 출력한다.", false);
                embed.addField("/캐릭터찾기 <닉네임>", "- 해당 캐릭터의 위치를 출력한다.", false);
                embed.setColor(Color.gray);
                event.deferReply().setEphemeral(true).queue();
                event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }
            case "동접" : {
                int count = 0;
                for (ChannelServer csrv : ChannelServer.getAllInstances()) {
                    int a = csrv.getPlayerStorage().getAllCharacters().size();
                    count += a;
                }
                count += CashShopServer.getPlayerStorage().getAllCharacters().size();
                count += AuctionServer.getPlayerStorage().getAllCharacters().size();
                count += FarmServer.getPlayerStorage().getAllCharacters().size();
                dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", "현재 " + DiscordSetting.serverName_KR + "에 " + count + "명의 유저가 접속중입니다.", "");
                break;
            }
            case "서버정보": {
                String text = "서버 이름 : " + DiscordSetting.serverName_KR + "" + enter + enter;
                text += "서버 버전 : 1.2." + (ServerConstants.MAPLE_VERSION) + " (" + ServerConstants.MAPLE_PATCH + ")" + enter + enter;
                text += "도메인 : " + DiscordSetting.homepage + enter + enter;
                dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", text, "");
                break;
            }
            case "캐릭터정보": {
                String username = event.getOptions().listIterator().next().getAsString();
                if (username == "") {
                    dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", "입력이 올바르지 않습니다.", "");
                    return;
                }
                String name = "";
                String reborns = "";
                String exp = "";
                String job = "";
                String guild = "";
                Connection con = null;
                try {
                    con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM characters where name = ?");
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        name += "[Lv." + rs.getInt("level") + "] " + rs.getString("name") + "\r\n";
                        reborns += "" + rs.getInt("fame") + "레벨";
                        exp += "" + rs.getLong("exp") + "";
                        int d = rs.getInt("job");
                        job += "" + GameConstants.getJobNameById(d) + "";
                        final MapleGuild mga = getGuild(rs.getInt("guildid"));
                        int c = rs.getInt("guildid");
                        if (c == 0) {
                            guild += "없음";
                        } else {
                            guild += "" + mga.getName() + "";
                        }
                    } else {
                        dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", "캐릭터가 존재하지 않습니다.", "");
                        rs.close();
                        ps.close();
                        con.close();
                        return;
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
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("[" + DiscordSetting.serverName_EN + " 명령어]", null);
                embed.setTitle(name);
              //  embed.addField("인기도 : ", reborns, false);
                embed.addField("경험치 : ", exp, false);
                embed.addField("직업 : ", job, false);
                embed.addField("길드명 : ", guild, false);
                embed.setImage("https://maplestory.io/api/Character/%7B%22itemId%22%3A2000%2C%22version%22%3A%22210.1.1%22%7D%2C%7B%22itemId%22%3A12000%2C%22version%22%3A%22210.1.1%22%7D/stand1/0?showears=false&showLefEars=false&showHighLefEars=undefined&resize=1&name=&flipX=false&bgColor=0,0,0,0");
                embed.setColor(Color.gray);
                event.deferReply().setEphemeral(true).queue();
                event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }
            case "캐릭터찾기": {
                String username = event.getOptions().listIterator().next().getAsString();
                if (username == "") {
                    dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", "입력이 올바르지 않습니다.", "");
                    return;
                }
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter hp : cserv.getPlayerStorage().getAllCharacters().values()) {
                        if (username.contains(hp.getPlayer().getName()) && !hp.isGM()) {
                            MapleMap map = hp.getPlayer().getMap();
                            dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", username + "님은 현재 " + cserv.getChannel() + " 채널 <" + map.getStreetName() +" : " + map.getMapName() + ">에 접속중입니다.", "");
                            return;
                        }
                    }
                }
                dropMessage(event, "[" + DiscordSetting.serverName_EN + " 명령어]", username + "님은 현재 오프라인이거나 존재하지 않는 캐릭터입니다.", "");
                break;
            }
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
        event.getGuild().updateCommands()
                .addCommands(commandData)
                .queue();
    }

    public void dropMessage(SlashCommandInteractionEvent event, String author, String description, String foot) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(author, null);
        embed.setDescription(description);
        if (foot != "") {
            embed.setFooter(foot);
        }
        embed.setColor(Color.gray);
        event.deferReply().setEphemeral(true).queue();
        event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
