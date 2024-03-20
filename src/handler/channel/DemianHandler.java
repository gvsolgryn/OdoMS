package handler.channel;

import client.MapleClient;
import constants.DemianPattern;
import packet.creators.DemianPacket;
import packet.transfer.read.ReadingMaple;

public class DemianHandler {

    public static void CDemianFlyingSword_EncodeMakeEnterInfo(ReadingMaple rm, MapleClient c) {
        c.getPlayer().getMap().broadcastMessage(DemianPacket.Demian_OnFlyingSwordNode(
                c.getPlayer().getMap().getDemianSword(rm.readInt()), DemianPattern.patternList));
    }
}
