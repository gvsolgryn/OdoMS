package server.maps;

import client.stats.MonsterStatus;
import client.stats.MonsterStatusEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import launch.ChannelServer;
import packet.creators.MainPacketCreator;
import server.life.MapleMonster;

public class MapleMapObjectHandler implements Runnable {
    
    public MapleMapObjectHandler() {
        System.out.println("[알림] MapleMapObjectHandler 쓰레드가 작동 되었습니다.");
    }
    
    @Override
    public void run() {
        long time = System.currentTimeMillis();

        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            for (Iterator<MapleMap> iterator = cs.getMapFactory().getAllLoadedMaps().iterator(); iterator.hasNext(); ) {
                MapleMap map = iterator.next();
                if (map != null) {
                    for (final ArrowFlatter arrow : map.getArrowFlatter()) {
                        if (time >= arrow.getTime()) {
                            map.broadcastMessage(MainPacketCreator.cancelArrowFlatter(arrow.getObjectId(), arrow.getArrow()));
                            map.removeMapObject(arrow);
                        }
                    }
                    for (final MapleMapObject obj : map.getAllMonster()) {
                        if (((MapleMonster) obj).isAlive()) {
                            List<MonsterStatusEffect> cancelStatus = new ArrayList<MonsterStatusEffect>();
                            for (final Entry<MonsterStatus, MonsterStatusEffect> stat : ((MapleMonster) obj).getStati().entrySet()) {
                                if (stat.getValue().getPoison() != null) {
                                    if (time >= stat.getValue().getPoison().getCheckTime()) {
                                        stat.getValue().getPoison().pdamage(time);
                                    }
                                }
                                if (((MapleMonster) obj).isAlive()) {
                                    if (time >= stat.getValue().getEndTime()) {
                                        cancelStatus.add(stat.getValue());
                                    }
                                }
                            }
                            for (final MonsterStatusEffect cancelStat : cancelStatus) {
                                ((MapleMonster) obj).cancelSingleStatus(cancelStat);
                            }
                            if (cancelStatus.size() > 0) {
                                cancelStatus.clear();
                            }
                        }
                    }
                }
            }
        }
    }
}
