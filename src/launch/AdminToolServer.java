package launch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import handler.AdminToolServerHandler;

public class AdminToolServer {

    private static int PORT = 9700;
    private static InetSocketAddress InetSocketadd;
    private static IoAcceptor acceptor;
    public static List<IoSession> session = new ArrayList<>();

    public static final void run_startup_configurations() {
        try {
            /* ���� ���� ���� */
            ByteBuffer.setUseDirectBuffers(false);
            ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

            acceptor = new SocketAcceptor();
            final SocketAcceptorConfig cfg = new SocketAcceptorConfig();
            cfg.getSessionConfig().setTcpNoDelay(true);
            cfg.setDisconnectOnUnbind(true);
            InetSocketadd = new InetSocketAddress(PORT);
            acceptor.bind(InetSocketadd, new AdminToolServerHandler(), cfg);
            /* ���� ���� ���� */
            System.out.println("[�˸�] �����⼭���� " + PORT + " ��Ʈ�� ���������� �����Ͽ����ϴ�. \r\n");
        } catch (IOException e) {
            System.err.println("[����] �����⼭���� " + PORT + " ��Ʈ�� �����ϴµ� �����߽��ϴ�. \r\n");
            e.printStackTrace();
        }
    }

    public static final void broadcastMessage(ByteBuffer buff) {
        for (IoSession se : session) {
            se.write(buff);
        }
    }
}
