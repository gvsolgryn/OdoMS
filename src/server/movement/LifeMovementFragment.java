package server.movement;

import java.awt.Point;

import packet.transfer.write.WritingPacket;

public interface LifeMovementFragment {

    void serialize(WritingPacket packet);

    Point getPosition();
}
