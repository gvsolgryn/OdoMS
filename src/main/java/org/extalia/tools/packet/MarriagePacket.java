package org.extalia.tools.packet;

import org.extalia.tools.data.MaplePacketLittleEndianWriter;

public class MarriagePacket {
  public static byte[] onMarriage(int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(1571);
    return mplew.getPacket();
  }
}
