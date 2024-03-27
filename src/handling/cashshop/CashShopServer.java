package handling.cashshop;

import constants.ServerType;
import handling.channel.PlayerStorage;
import handling.netty.MapleNettyDecoder;
import handling.netty.MapleNettyEncoder;
import handling.netty.MapleNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.ServerProperties;

public class CashShopServer {
    private static String ip;
    private static final int PORT = Integer.parseInt(ServerProperties.getProperty("ports.cashshop"));

    private static PlayerStorage players;
    private static boolean finishedShutdown = false;
    private static ServerBootstrap bootstrap;

    public static final void run_startup_configurations() {
        players = new PlayerStorage();
        ip = ServerProperties.getProperty("world.host") + ":" + PORT;
        NioEventLoopGroup nioEventLoopGroup1 = new NioEventLoopGroup();
        NioEventLoopGroup nioEventLoopGroup2 = new NioEventLoopGroup();

        try {
            bootstrap = new ServerBootstrap();
            ((ServerBootstrap) ((ServerBootstrap) bootstrap.group((EventLoopGroup) nioEventLoopGroup1, (EventLoopGroup) nioEventLoopGroup2).channel(NioServerSocketChannel.class)).childHandler((ChannelHandler) new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", (ChannelHandler) new MapleNettyDecoder());
                            ch.pipeline().addLast("encoder", (ChannelHandler) new MapleNettyEncoder());
                            ch.pipeline().addLast("handler", (ChannelHandler) new MapleNettyHandler(ServerType.CASHSHOP, -1));
                        }
                    }).option(ChannelOption.SO_BACKLOG, Integer.valueOf(128))).childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true));
            ChannelFuture f = bootstrap.bind(PORT).sync();
            System.out.println("[알림] 캐시샵서버가 " + PORT + " 포트를 성공적으로 개방하였습니다.");
        } catch (InterruptedException e) {
            System.err.println("[오류] 캐시샵서버가 " + PORT + " 포트를 개방하는데 실패했습니다.");
        }
    }

    public static final String getIP() {
        return ip;
    }

    public static final PlayerStorage getPlayerStorage() {
        return players;
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("Saving all connected clients (CS)...");
        players.disconnectAll();
        System.out.println("Shutting down CS...");
        finishedShutdown = true;
    }

    public static boolean isShutdown() {
        return finishedShutdown;
    }
}