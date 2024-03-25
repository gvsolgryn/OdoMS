package org.extalia.server.movement;

import org.extalia.tools.data.MaplePacketLittleEndianWriter;

import java.awt.*;

public class ChangeEquipSpecialAwesome implements LifeMovementFragment {
  private final int type;
  
  private final int wui;
  
  public ChangeEquipSpecialAwesome(int type, int wui) {
    this.type = type;
    this.wui = wui;
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(this.type);
    packet.write(this.wui);
  }
  
  public Point getPosition() {
    return new Point(0, 0);
  }
}
