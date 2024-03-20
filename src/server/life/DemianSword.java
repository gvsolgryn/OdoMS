package server.life;

import client.MapleClient;
import packet.creators.DemianPacket;
import server.maps.AbstractHinaMapObject;
import server.maps.MapleMapObjectType;

public class DemianSword extends AbstractHinaMapObject {

    private int attackIdx;
    private int mobId;
    private int cid;
    private int sworldOid;

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.DemianSword;
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.sendPacket(DemianPacket.Demian_OnFlyingSwordCreat(true, this));
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.sendPacket(DemianPacket.Demian_OnFlyingSwordCreat(false, this));
    }

    public void setSwordOid(int i) {
        this.sworldOid = i;
    }

    public int getSwordOid() {
        return sworldOid;
    }

    public int getAttackIdx() {
        return this.attackIdx;
    }

    public void setAttackIdx(int i) {
        this.attackIdx = i;
    }

    public int getMobId() {
        return this.mobId;
    }

    public void setMobId(int i) {
        this.mobId = i;
    }

    public int getCid() {
        return this.cid;
    }

    public void setCid(int i) {
        this.cid = i;
    }

}
