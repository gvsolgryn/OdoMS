package packet.creators;

import packet.opcode.SendPacketOpcode;
import packet.transfer.write.WritingPacket;

public class AntiHackPacket {

    public static byte[] sendProcessRequest() {
        WritingPacket packet = new WritingPacket();
        packet.writeShort(SendPacketOpcode.PROCESS_CHECK.getValue());
        packet.write(1);

        return packet.getPacket();
    }
}
