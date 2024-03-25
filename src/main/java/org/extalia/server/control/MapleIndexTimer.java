package org.extalia.server.control;

import org.extalia.client.MapleCharacter;
import org.extalia.constants.GameConstants;
import org.extalia.handling.channel.ChannelServer;
import org.extalia.handling.channel.handler.InventoryHandler;
import java.util.Iterator;
import java.util.List;
import org.extalia.server.MapleInventoryManipulator;
import org.extalia.server.maps.MapleMapItem;
import org.extalia.server.maps.MapleMapObject;

public class MapleIndexTimer implements Runnable {

    public long lastClearDropTime = 0;

    private MapleMapItem mapitem;

    public MapleIndexTimer() {
        lastClearDropTime = System.currentTimeMillis();
        System.out.println("[Loading Completed] MapleIndexTimer Start");
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        Iterator<ChannelServer> channels = ChannelServer.getAllInstances().iterator();
        while (channels.hasNext()) {
            ChannelServer cs = channels.next();
            Iterator<MapleCharacter> chrs = cs.getPlayerStorage().getAllCharacters().values().iterator();
            while (chrs.hasNext()) {
                MapleCharacter chr = chrs.next();
                AutoRoot(chr);
            }
        }
    }

    public void AutoRoot(MapleCharacter chr) {
        if (chr.getKeyValue(12345, "AutoRoot") > 0 && !GameConstants.보스맵(chr.getMapId())) {
            List<MapleMapObject> objs = chr.getMap().getItemsInRange(chr.getPosition(), Double.MAX_VALUE);
            for (MapleMapObject ob : objs) {
                MapleMapItem mapitem = (MapleMapItem) ob;
                if (mapitem.getItem() != null && !MapleInventoryManipulator.checkSpace(chr.getClient(), mapitem.getItemId(), mapitem.getItem().getQuantity(), "")) {
                    continue;
                }
                if (mapitem.isPickpoket()) {
                    continue;
                }
                if (!mapitem.isPlayerDrop()) {
                    InventoryHandler.pickupItem(ob, chr.getClient(), chr);
                }
            }
        }
    }
}
