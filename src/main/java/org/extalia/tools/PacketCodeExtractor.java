package org.extalia.tools;

import org.extalia.client.SkillFactory;
import org.extalia.server.SecondaryStatEffect;
import org.extalia.tools.data.ByteArrayByteStream;
import org.extalia.tools.data.LittleEndianAccessor;
import org.extalia.tools.data.MaplePacketLittleEndianWriter;
import org.extalia.tools.packet.CField;

import java.util.List;
import java.util.Scanner;

public class PacketCodeExtractor {
    public static void main(String[] args) {
        long hair = 0;
        Scanner sc = new Scanner(System.in);
        System.out.print("추출할 패킷 입력 : ");
        byte[] data = HexTool.getByteArrayFromHexString(sc.nextLine());
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        byte count = slea.readByte();

        for (int i = 0; i < 100; i++) {
            hair = slea.readInt();
            System.err.print(hair + ", ");
        }
        System.err.println("/");
        for (int i = 0; i < 28; i++) {
            hair = slea.readInt();
            System.err.print(hair + ", ");
        }
        sc.nextLine();
    }
}
