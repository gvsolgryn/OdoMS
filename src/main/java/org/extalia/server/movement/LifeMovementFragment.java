package org.extalia.server.movement;

import org.extalia.tools.data.MaplePacketLittleEndianWriter;

import java.awt.*;

public interface LifeMovementFragment {
  void serialize(MaplePacketLittleEndianWriter paramMaplePacketLittleEndianWriter);
  
  Point getPosition();
}
