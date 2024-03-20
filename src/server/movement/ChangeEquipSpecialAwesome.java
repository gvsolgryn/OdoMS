package server.movement;

import java.awt.Point;

import packet.transfer.write.WritingPacket;

public class ChangeEquipSpecialAwesome implements LifeMovementFragment {

    private int type, wui;

    public ChangeEquipSpecialAwesome(int type, int wui) {
        this.type = type;
        this.wui = wui;
    }

    @Override
    public void serialize(WritingPacket packet) {
        packet.write(type);
        packet.write(wui);
    }

    @Override
    public Point getPosition() {
        return new Point(0, 0);
    }
}
