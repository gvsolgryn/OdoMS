package handling.login;

import client.MapleClient;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.login.handler.CharLoginHandler;
import tools.FileoutputUtil;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static handling.login.handler.CharLoginHandler.ServerListRequest;

public class LoginWorker {
  public static void registerClient(final MapleClient c, final String id, String pwd) {
    if (LoginServer.isAdminOnly() && !c.isGm() && !c.isLocalhost()) {
      c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "서버 점검중입니다."));
      c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21));
      return;
    }
    if (System.currentTimeMillis() - lastUpdate > 600000) { // Update once every 10 minutes
      lastUpdate = System.currentTimeMillis();
      final Map<Integer, Integer> load = ChannelServer.getChannelLoad();
      int usersOn = 0;
      if (load == null || load.size() <= 0) { // In an unfortunate event that client logged in before load
        lastUpdate = 0;
        c.getSession().writeAndFlush(LoginPacket.getLoginFailed(7));
        return;
      }
      LoginServer.setLoad(load, usersOn);
      lastUpdate = System.currentTimeMillis();
    }
    if (c.finishLogin() == 0) {
      c.getSession().writeAndFlush(LoginPacket.checkLogin());
      //c.getSession().writeAndFlush(LoginPacket.successLogin());
      c.getSession().writeAndFlush(LoginPacket.getAuthSuccessRequest(c, id, pwd));
      ServerListRequest(c, false);
    } else {
      c.getSession().writeAndFlush(LoginPacket.getLoginFailed(7));
      return;
    }
  }
  
  private static long lastUpdate = 0L;
}
