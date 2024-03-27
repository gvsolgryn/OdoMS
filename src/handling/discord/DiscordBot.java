package handling.discord;

import handling.discord.manager.AdminCommandManager;
import handling.discord.manager.ModalManager;
import handling.discord.manager.SlashCommandManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import net.dv8tion.jda.api.JDA;

public class DiscordBot {
    //public static JDA jda;
    public void run() throws Exception {
        JDA jda = JDABuilder.createDefault(DiscordSetting.token)
                .addEventListeners(new SlashCommandManager())
                .addEventListeners(new AdminCommandManager())
                .addEventListeners(new ModalManager())
                .setActivity(Activity.playing(DiscordSetting.activity))
                .build();
        System.out.println("[알림] 디스코드 봇 로딩 완료!");
    }
}
