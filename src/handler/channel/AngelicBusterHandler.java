package handler.channel;

import client.MapleClient;
import constants.GameConstants;
import packet.creators.MainPacketCreator;
import packet.transfer.read.ReadingMaple;

public class AngelicBusterHandler {

    public static final void DressUpTime(final ReadingMaple rh, final MapleClient c) {
        byte type = rh.readByte();
        if (type == 1) {
            if (GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                c.sendPacket(MainPacketCreator.updateInfoQuest(7707, ""));
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
                        MainPacketCreator.updateCharLook(c.getPlayer(), true), false);
            }
        } else {
            c.getSession().writeAndFlush(MainPacketCreator.completeQuest(7707));
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
                    MainPacketCreator.updateCharLook(c.getPlayer(), false), false);
        }
    }
}
