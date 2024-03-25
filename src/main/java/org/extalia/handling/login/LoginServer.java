package org.extalia.handling.login;

import org.extalia.constants.ServerType;
import org.extalia.handling.netty.MapleNettyDecoder;
import org.extalia.handling.netty.MapleNettyEncoder;
import org.extalia.handling.netty.MapleNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferAllocator;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.extalia.server.ServerProperties;
import org.extalia.tools.Pair;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LoginServer {
  public static final int PORT = Integer.parseInt(ServerProperties.getProperty("ports.login"));
  
  private static InetSocketAddress InetSocketadd;
  
  private static IoAcceptor acceptor;
  
  private static Map<Integer, Integer> load = new HashMap<>();
  
  private static String serverName;
  
  private static String eventMessage;
  
  private static byte flag;
  
  private static int maxCharacters;
  
  private static int userLimit;
  
  private static int usersOn = 0;
  
  private static boolean finishedShutdown = true;
  
  private static boolean adminOnly = false;
  
  private static HashMap<Integer, Pair<String, String>> loginAuth = new HashMap<>();
  
  private static HashSet<String> loginIPAuth = new HashSet<>();
  
  private static ServerBootstrap bootstrap;

  public static HashMap<String, Channel> Channels = new HashMap<String, Channel>();  
  public static void putLoginAuth(int chrid, String ip, String tempIP) {
    loginAuth.put(Integer.valueOf(chrid), new Pair<>(ip, tempIP));
    loginIPAuth.add(ip);
  }
  
  public static Pair<String, String> getLoginAuth(int chrid) {
    return loginAuth.remove(Integer.valueOf(chrid));
  }
  
  public static boolean containsIPAuth(String ip) {
    return loginIPAuth.contains(ip);
  }
  
  public static void removeIPAuth(String ip) {
    loginIPAuth.remove(ip);
  }
  
  public static void addIPAuth(String ip) {
    loginIPAuth.add(ip);
  }
  
  public static final void addChannel(int channel) {
    load.put(Integer.valueOf(channel), Integer.valueOf(0));
  }
  
  public static final void removeChannel(int channel) {
    load.remove(Integer.valueOf(channel));
  }
  
  public static final void run_startup_configurations() {
    userLimit = Integer.parseInt(ServerProperties.getProperty("login.userlimit"));
    serverName = ServerProperties.getProperty("login.serverName");
    eventMessage = ServerProperties.getProperty("login.eventMessage");
    flag = Byte.parseByte(ServerProperties.getProperty("login.flag"));
    adminOnly = Boolean.parseBoolean(ServerProperties.getProperty("world.admin", "false"));
    maxCharacters = Integer.parseInt(ServerProperties.getProperty("login.maxCharacters"));
    IoBuffer.setUseDirectBuffer(false);
    IoBuffer.setAllocator((IoBufferAllocator)new SimpleBufferAllocator());
    NioEventLoopGroup nioEventLoopGroup1 = new NioEventLoopGroup();
    NioEventLoopGroup nioEventLoopGroup2 = new NioEventLoopGroup();
    try {
      bootstrap = new ServerBootstrap();
      ((ServerBootstrap)((ServerBootstrap)bootstrap.group((EventLoopGroup)nioEventLoopGroup1, (EventLoopGroup)nioEventLoopGroup2)
        .channel(NioServerSocketChannel.class))
        .childHandler((ChannelHandler)new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast("decoder", (ChannelHandler)new MapleNettyDecoder());
              ch.pipeline().addLast("encoder", (ChannelHandler)new MapleNettyEncoder());
              ch.pipeline().addLast("idleStateHandler", (ChannelHandler)new IdleStateHandler(60, 30, 0));
              ch.pipeline().addLast("handler", (ChannelHandler)new MapleNettyHandler(ServerType.LOGIN, -1));
              // 찾기로 ServerType.LOGIN 핸들링하는 함수 잠시 찾아가보시겠어요?
            }
          }).option(ChannelOption.SO_BACKLOG, Integer.valueOf(128)))
        .childOption(ChannelOption.SO_SNDBUF, Integer.valueOf(4194304))
        .childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true));
      // 이렇게 해보시겠어요?
      // 제가 이러한 종류의 라이브러리는 써보질 않아서, 검색좀 해보았는데
      // 아이피를 bind 하지 않아서 생기는게 아닐까해서 IP 임의로 붙여봤습니다.
      // 컴파일 한번 해보실 수 있으실까요
      ChannelFuture f = bootstrap.bind(PORT).sync();
      System.out.println("[알림] 로그인서버가 " + PORT + " 포트를 성공적으로 개방하였습니다.");
    } catch (InterruptedException e) {
      System.err.println("[오류] 로그인서버가 " + PORT + " 포트를 개방하는데 실패했습니다.");
      e.printStackTrace();
    } 
  }
  
  public static final void shutdown() {
    if (finishedShutdown)
      return; 
    System.out.println("Shutting down login...");
    finishedShutdown = true;
  }
  
  public static final String getServerName() {
    return serverName;
  }
  
  public static final String getEventMessage() {
    return eventMessage;
  }
  
  public static final byte getFlag() {
    return flag;
  }
  
  public static final int getMaxCharacters() {
    return maxCharacters;
  }
  
  public static final Map<Integer, Integer> getLoad() {
    return load;
  }
  
  public static void setLoad(Map<Integer, Integer> load_, int usersOn_) {
    load = load_;
    usersOn = usersOn_;
  }
  
  public static final void setEventMessage(String newMessage) {
    eventMessage = newMessage;
  }
  
  public static final void setFlag(byte newflag) {
    flag = newflag;
  }
  
  public static final int getUserLimit() {
    return userLimit;
  }
  
  public static final int getUsersOn() {
    return usersOn;
  }
  
  public static final void setUserLimit(int newLimit) {
    userLimit = newLimit;
  }
  
  public static final boolean isAdminOnly() {
    return adminOnly;
  }
  
  public static final boolean isShutdown() {
    return finishedShutdown;
  }
  
  public static final void setOn() {
    finishedShutdown = false;
  }
}
