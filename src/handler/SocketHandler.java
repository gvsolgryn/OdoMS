package handler;

import client.MapleClient;
import packet.transfer.read.ReadingMaple;

public class SocketHandler {

    public static void handleClientExceptionInfo(ReadingMaple iPacket, MapleClient c) {
        String sFileName = iPacket.readMapleAsciiString();
        int nLine = iPacket.readInt();

        System.out.println("[ERROR] Client Exception : FILE " + sFileName + ", LINE " + nLine);
    }
}
