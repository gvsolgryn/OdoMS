package handler.channel;

import client.MapleCharacter;
import client.MapleClient;
import client.skills.ISkill;
import client.skills.SkillFactory;
import client.skills.SteelSkillEntry;
import constants.GameConstants;
import packet.creators.MainPacketCreator;
import packet.skills.PhantomPacket;
import packet.transfer.read.ReadingMaple;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

public class PhantomHandler {

    public static void equippedSkill(ReadingMaple rh, MapleClient ha) {
        // B4 01 E9 39 6E 01 2D 46 0F 00
        int baseSkillId = rh.readInt();
        int skillId = rh.readInt();
        if (skillId > 0) {
            SteelSkillEntry sse = ha.getPlayer().getSteelSkills().getSkillEntryById(skillId);
            ha.getPlayer().getSteelSkills().setEquipped(SteelSkillEntry.getJobIndex(skillId), sse.getSlot() - 1, true);
            ha.send(PhantomPacket.getUpdateEquippedSkill(baseSkillId, skillId, SteelSkillEntry.getJobIndex(skillId),skillId > 0 ? true : false));
        } else {
            int index = SteelSkillEntry.getJobIndexB(baseSkillId);
            SteelSkillEntry sse = ha.getPlayer().getSteelSkills().getSkillEntryById(ha.getPlayer().getEquippedSkillId(index));
            ha.getPlayer().getSteelSkills().setEquipped(index, sse.getSlot()-1, false);
            ha.send(PhantomPacket.getUpdateEquippedSkill(baseSkillId, 0, 0, false));
        }
            ha.getPlayer().fakeRelog();
    }

    public static void steelSkillCheck(ReadingMaple rh, MapleClient ha) {
        int target = rh.readInt();
        MapleMapObject chrobj = ha.getPlayer().getMap().getMapObject(target, MapleMapObjectType.PLAYER);
        if (chrobj == null) {
            ha.send(PhantomPacket.getSteelAvailableSkills(null, false));
            ha.getPlayer().ea();
        }
        MapleCharacter targetchr = null;
        targetchr = (MapleCharacter) chrobj;
        if (targetchr != null && GameConstants.isAdventurer(targetchr.getJob())) {
            ha.send(PhantomPacket.getSteelAvailableSkills(targetchr, true));
        }

    }

    public static void steelSkill(ReadingMaple rh, MapleClient ha) {
        int skillId = rh.readInt();
        int oid = rh.readInt();
        MapleMapObject chrobj = ha.getPlayer().getMap().getMapObject(oid, MapleMapObjectType.PLAYER);
        if (chrobj == null) {
            ha.send(PhantomPacket.getSteelSkillCheck(oid, false, null, false));
            ha.getPlayer().ea();
        }
        MapleCharacter targetchr = (MapleCharacter) chrobj;
        byte action = rh.readByte();
        if (action == 0) { // 훔침
            SteelSkillEntry sse = new SteelSkillEntry(skillId, targetchr.getSkillLevel(skillId));
            if (!ha.getPlayer().getSteelSkills().isExistSkill(skillId)) {
                sse.setSlot(ha.getPlayer().getSteelSkills().getNextFreeSlot(SteelSkillEntry.getJobIndex(skillId)));
                ha.getPlayer().getSteelSkills().addSkill(SteelSkillEntry.getJobIndex(skillId), sse);
                ha.send(PhantomPacket.getSteelSkillCheck(oid, true, sse, false));
                ISkill skills = SkillFactory.getSkill(skillId);
                ha.getPlayer().changeSkillLevel(skills, (byte) sse.getSkillLevel(), (byte) skills.getMasterLevel());
            } else {
                // 원래있던 스킬의 슬롯을 저장
                // 그 슬롯에있던 데이터 지움 -> 거기에 새로운스킬 저장
                SteelSkillEntry sse2 = ha.getPlayer().getSteelSkills().getSkillEntryById(skillId);
                int slot = sse2.getSlot();
                ISkill skills = SkillFactory.getSkill(skillId);
                ha.getPlayer().changeSkillLevel(skills, (byte) 0, (byte) 0);
                ha.getPlayer().getSteelSkills().deleteSkill(sse2);

                ha.getPlayer().getSteelSkills().addSkill(SteelSkillEntry.getJobIndex(skillId), slot, sse);
                ha.send(PhantomPacket.getSteelSkillCheck(oid, true, sse, false));
                ha.getPlayer().changeSkillLevel(skills, (byte) sse.getSkillLevel(), (byte) skills.getMasterLevel());
            }

        } else if (action == 1) { // 삭제
            SteelSkillEntry sse = ha.getPlayer().getSteelSkills().getSkillEntryById(skillId);
            if (sse == null) {
                ha.getPlayer().message(1, "해당 스킬을 찾을 수 없습니다.");
                System.err.println("[오류] 스틸 스킬을 삭제하다가 오류가 발생했습니다. - 일치하는 스킬을 발견하지 못했습니다.");
                ha.getPlayer().ea();
                return;
            }
            ha.send(PhantomPacket.getSteelSkillCheck(oid, true, sse, true));
            ha.getPlayer().getSteelSkills().deleteSkill(sse);
        }
    }
}
