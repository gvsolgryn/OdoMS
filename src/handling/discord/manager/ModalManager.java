package handling.discord.manager;

import constants.ServerConstants;
import database.DatabaseConnection;
import handling.discord.DiscordSetting;
import handling.login.LoginServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModalManager extends ListenerAdapter {

    // todo Discord 버튼 클릭시 상호작용
    public void onButtonInteraction(ButtonInteractionEvent event) {

        switch (event.getButton().getId()) {
            case "Account_Confirm": {
                TextInput id = TextInput.create("userid", DiscordSetting.serverName_KR + " 아이디", TextInputStyle.SHORT)
                        .setPlaceholder("아이디를 입력해주세요.")
                        .setMinLength(4)
                        .setMaxLength(20)
                        .build();

                TextInput pw = TextInput.create("userpw", DiscordSetting.serverName_KR + " 비밀번호", TextInputStyle.SHORT)
                        .setPlaceholder("비밀번호를 입력해주세요.")
                        .setMinLength(4)
                        .setMaxLength(20)
                        .build();

                Modal modal = Modal.create("Account_Confirm_modmail", "" + DiscordSetting.serverName_EN + " 계정연동")
                        .addComponents(ActionRow.of(id), ActionRow.of(pw))
                        .build();

                event.replyModal(modal).queue();
                break;
            }
            case "role": {
                event.deferEdit().queue();
                Member user = event.getMember();
                Role role = event.getGuild().getRolesByName("별빛모험가",true).get(0);
                event.getGuild().addRoleToMember(user, role).queue();
                break;
            }
        }
    }

    // todo Discord 모달 양식 상호작용
    public void onModalInteraction(ModalInteractionEvent event) {
        switch (event.getModalId()) {
            case "Account_Confirm_modmail": { // 계정인증 모달
                String user_id = event.getValue("userid").getAsString();
                String user_pw = event.getValue("userpw").getAsString();
                event.deferEdit().queue();

                if (isDiscord(event)) {
                    dropMessage(event, null, event.getUser().getName() + "님은 이미 계정 연동을 하셨습니다.", "", false);
                } else if (isLogin(user_id, user_pw, event)) {
                    dropMessage(event, "[계정연동 성공!]", "<" + user_id + "> 계정을 " + event.getGuild().getName() + "와 연동하였습니다.", "", false);
                } else {
                    dropMessage(event, null, "입력하신 계정을 다시 확인해보세요.", "", false);
                }
                break;
            }
        }
    }

    public static boolean isDiscord(ModalInteractionEvent event) {
        try {
            boolean discord = false;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE discordID=?");
            ps.setString(1, event.getUser().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                discord = true;
            }
            rs.close();
            ps.close();
            con.close();
            if (discord) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean isLogin(String id, String pw, ModalInteractionEvent event) {
        try {
            boolean login = false;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE name=? AND password=?");
            ps.setString(1, id);
            ps.setString(2, pw);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PreparedStatement ps2 = con.prepareStatement("UPDATE accounts SET discordID = ?, discordName = ? WHERE name = ?");
                ps2.setString(1, event.getUser().getId());
                ps2.setString(2, event.getUser().getName());
                ps2.setString(3, id);
                ps2.executeUpdate();
                ps2.close();
                login = true;
            }
            rs.close();
            ps.close();
            con.close();
            if (login) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void dropMessage(ModalInteractionEvent event, String author, String description, String foot, boolean status) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(author, null);
        embed.setDescription(description);
        if (status) {
            embed.setThumbnail("https://i.imgur.com/Az5lufb.png");
        }
        if (foot != "") {
            embed.setFooter(foot);
        }
        embed.setColor(Color.gray);
        event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
