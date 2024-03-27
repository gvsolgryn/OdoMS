
package handling.channel.handler;

import client.*;
import client.inventory.*;
import constants.GameConstants;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.StructSetItem;
import server.maps.FieldLimitType;
import server.movement.LifeMovementFragment;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PetPacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetHandler
{
    public static void SpawnPet(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.skip(4);
        final byte slot = slea.readByte();
        slea.readByte();
        final Item item = chr.getInventory(MapleInventoryType.CASH).getItem(slot);
        if (item == null) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (ItemFlag.KARMA_USE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.KARMA_USE.getValue());
        }
        final MaplePet pet = item.getPet();
        if (pet != null) {
            if (chr.getPetIndex(pet) != -1) {
                chr.unequipPet(pet, false, false);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                updatePetSkills(chr, pet);
                return;
            }
            if (item.getExpiration() > System.currentTimeMillis()) {
                final Point pos = chr.getPosition();
                pet.setPos(pos);
                if (chr.getMap().getFootholds() != null && chr.getMap().getFootholds().findBelow(pet.getPos()) != null) {
                    pet.setFh(chr.getMap().getFootholds().findBelow(pet.getPos()).getId());
                }
                pet.setStance(0);
                chr.addPet(pet);
                chr.getMap().broadcastMessage(chr, PetPacket.showPet(chr, pet, false, false), true);
                c.getSession().writeAndFlush((Object)PetPacket.updatePet(c.getPlayer(), pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false, c.getPlayer().getPetLoot()));
                updatePetSkills(chr, null);
            }
            else {
                c.getPlayer().getInventory(MapleInventoryType.CASH).removeItem(slot);
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.clearInventoryItem(MapleInventoryType.CASH, slot, false));
            }
        }
        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
    }
    
    public static final void Pet_AutoPotion(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.skip(1);
        slea.readInt();
        final short slot = slea.readShort();
        if (chr == null || !chr.isAlive() || chr.getBuffedEffect(SecondaryStat.DebuffIncHp) != null || chr.getMap() == null || chr.hasDisease(SecondaryStat.StopPortion) || chr.getBuffedValue(SecondaryStat.StopPortion) != null) {
            return;
        }
        final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != slea.readInt() || chr.getBuffedEffect(SecondaryStat.Reincarnation) != null) {
            return;
        }
        final long time = System.currentTimeMillis();
        if (chr.getNextConsume() > time) {
            return;
        }
        if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit()) && MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyTo(chr, true)) {
            if (toUse.getItemId() != 2000054) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            }
            if (chr.getMap().getConsumeItemCoolTime() > 0) {
                chr.setNextConsume(time + chr.getMap().getConsumeItemCoolTime() * 1000);
            }
        }
    }
    
    public static final void PetChat(final int petid, final short command, final String text, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null || chr.getPet(petid) == null) {
            return;
        }
        chr.getMap().broadcastMessage(chr, PetPacket.petChat(chr.getId(), command, text, (byte)petid), true);
    }
    
    public static final void PetCommand(final MaplePet pet, final PetCommand petCommand, final MapleClient c, final MapleCharacter chr) {
        if (petCommand == null) {
            return;
        }
        final byte petIndex = (byte)chr.getPetIndex(pet);
        boolean success = false;
        if (Randomizer.nextInt(99) <= petCommand.getProbability()) {
            success = true;
            if (pet.getCloseness() < 30000) {
                int newCloseness = pet.getCloseness() + petCommand.getIncrease() * c.getChannelServer().getTraitRate();
                if (newCloseness > 30000) {
                    newCloseness = 30000;
                }
                pet.setCloseness(newCloseness);
                if (newCloseness >= GameConstants.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                    pet.setLevel(pet.getLevel() + 1);
                    c.getSession().writeAndFlush((Object)CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), true));
                    chr.getMap().broadcastMessage(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), false));
                }
                c.getSession().writeAndFlush((Object)PetPacket.updatePet(chr, pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false, chr.getPetLoot()));
            }
        }
        chr.getMap().broadcastMessage(PetPacket.commandResponse(chr.getId(), (byte)petCommand.getSkillId(), petIndex, success));
    }
    
    public static void PetFood(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        int previousFullness = 100;
        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
        for (final MaplePet pet : chr.getPets()) {
            if (pet != null) {
                if (pet.getFullness() < previousFullness) {
                    previousFullness = pet.getFullness();
                    slea.skip(6);
                    final int itemId = slea.readInt();
                    boolean gainCloseness = false;
                    if (Randomizer.nextInt(99) <= 50) {
                        gainCloseness = true;
                    }
                    if (pet.getFullness() < 100) {
                        int newFullness = pet.getFullness() + 30;
                        if (newFullness > 100) {
                            newFullness = 100;
                        }
                        pet.setFullness(newFullness);
                        final int index = chr.getPetIndex(pet);
                        if (gainCloseness && pet.getCloseness() < 30000) {
                            int newCloseness = pet.getCloseness() + 1;
                            if (newCloseness > 30000) {
                                newCloseness = 30000;
                            }
                            pet.setCloseness(newCloseness);
                            if (newCloseness >= GameConstants.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                                pet.setLevel(pet.getLevel() + 1);
                                c.getSession().writeAndFlush((Object)CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), true));
                                chr.getMap().broadcastMessage(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), false));
                            }
                        }
                        c.getSession().writeAndFlush((Object)PetPacket.updatePet(chr, pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false, chr.getPetLoot()));
                        chr.getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(chr.getId(), (byte)1, (byte)index, true), true);
                    }
                    else {
                        if (gainCloseness) {
                            int newCloseness2 = pet.getCloseness() - 1;
                            if (newCloseness2 < 0) {
                                newCloseness2 = 0;
                            }
                            pet.setCloseness(newCloseness2);
                            if (newCloseness2 < GameConstants.getClosenessNeededForLevel(pet.getLevel())) {
                                pet.setLevel(pet.getLevel() - 1);
                            }
                        }
                        c.getSession().writeAndFlush((Object)PetPacket.updatePet(chr, pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false, chr.getPetLoot()));
                        chr.getMap().broadcastMessage(chr, PetPacket.commandResponse(chr.getId(), (byte)1, (byte)chr.getPetIndex(pet), false), true);
                    }
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemId, 1, true, false);
                    return;
                }
            }
        }
    }
    
    public static final void MovePet(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final int petId = slea.readInt();
        slea.skip(13);
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 3);
        if (res != null && chr != null && res.size() != 0 && chr.getMap() != null) {
            final MaplePet pet = chr.getPet(petId);
            if (pet == null) {
                return;
            }
            pet.updatePosition(res);
            chr.getMap().broadcastMessage(chr, PetPacket.movePet(chr.getId(), pet.getUniqueId(), (byte)petId, res, pet.getPos()), false);
            if (chr.getStat().pickupRange <= 0.0 || chr.inPVP()) {
                return;
            }
            chr.setScrolledPosition((short)0);
        }
    }
    
    public static void ChangePetBuff(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final int type = slea.readInt();
        int skillsize = slea.readInt();
        final int skillId = slea.readInt();
        final int mode = slea.readByte();
        final MaplePet pet = chr.getPet(type);
        if (pet == null) {
            chr.dropMessage(1, "펫이 존재하지 않습니다.");
            chr.getClient().getSession().writeAndFlush((Object)CWvsContext.enableActions(chr));
            return;
        }
        if (chr.getKeyValue(9999, "skillid") == -1L) {
        chr.setKeyValue(9999, "skillid" ,"0");
        }
        if (chr.getKeyValue(9999, "skillid2") == -1L) {
        chr.setKeyValue(9999, "skillid2" ,"0");
        }
        if (chr.getKeyValue(9999, "skillid3") == -1L) {
        chr.setKeyValue(9999, "skillid3" ,"0");
        }
        if (chr.getKeyValue(9999, "skillid4") == -1L) {
        chr.setKeyValue(9999, "skillid4" ,"0");
        }
        if (chr.getKeyValue(9999, "skillid5") == -1L) {
        chr.setKeyValue(9999, "skillid5" ,"0");
        }
        if (chr.getKeyValue(9999, "skillid6") == -1L) {
        chr.setKeyValue(9999, "skillid6" ,"0");
        }
         if (type == 0 && mode == 0 && skillsize == 0) {
             pet.setBuffSkillId(skillId);
             chr.setKeyValue(9999, "skillid" ,"" + skillId);
        } else if (type == 0 && skillsize == 1) {
            pet.setBuffSkillId2(skillId);
            chr.setKeyValue(9999, "skillid2" ,"" + skillId);
        } else if (type == 1 && skillsize == 0) {
            pet.setBuffSkillId(skillId);
            chr.setKeyValue(9999, "skillid3" ,"" + skillId);
        } else if (type == 1 && skillsize == 1) {
            pet.setBuffSkillId2(skillId);
            chr.setKeyValue(9999, "skillid4" ,"" + skillId);
        } else if (type == 2 && skillsize == 0) {
            pet.setBuffSkillId(skillId);
            chr.setKeyValue(9999, "skillid5" ,"" + skillId);
        } else if (type == 2 && skillsize == 1) {
            pet.setBuffSkillId2(skillId);
            chr.setKeyValue(9999, "skillid6" ,"" + skillId);
        }
        chr.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(0, "0=" + chr.getKeyValue(9999, "skillid") + ";1="+chr.getKeyValue(9999, "skillid2")));
        chr.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(1, "10=" + chr.getKeyValue(9999, "skillid3") + ";11="+chr.getKeyValue(9999, "skillid4")));
        chr.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(2, "20=" + chr.getKeyValue(9999, "skillid5") + ";21="+chr.getKeyValue(9999, "skillid6")));
        chr.getClient().getSession().writeAndFlush((Object)PetPacket.updatePet(chr, pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false, chr.getPetLoot()));
    }
    
    public static void petExceptionList(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int petindex = slea.readInt();
        final byte size = slea.readByte();
        final MaplePet pet = chr.getPet(petindex);
        if (pet == null) {
            chr.dropMessage(1, "펫이 존재하지 않습니다.");
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(chr));
            return;
        }
        pet.setExceptionList("");
        String list = "";
        int i = 0;
        while (i < size) {
            list = list + slea.readInt() + "";
            ++i;
            if (size > 1 && size != i) {
                list += ",";
            }
        }
        pet.setExceptionList(list);
        c.getSession().writeAndFlush((Object)PetPacket.petExceptionList(chr, pet));
    }
    
    public static void updatePetSkills(final MapleCharacter player, final MaplePet unequip) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Map<Skill, SkillEntry> newL = new HashMap<>();
        final List<Integer> petItemIds = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            final MaplePet pet = player.getPet(i);
            if (pet != null) {
                petItemIds.add(pet.getPetItemId());
            }
        }

        if (unequip != null) {
            int level = 0;
            final int unequipItemId = unequip.getPetItemId();
            final StructSetItem setItem = ii.getSetItem(ii.getSetItemID(unequipItemId));
            for (final int petId : petItemIds) {
                if (ii.getSetItemID(petId) == ii.getSetItemID(unequipItemId)) {
                    ++level;
                }
            }
            if (setItem != null) {
                for (final Map.Entry<Integer, StructSetItem.SetItem> set : setItem.items.entrySet()) {
                    final int setLevel = set.getKey();
                    for (final Map.Entry<Integer, Byte> skill : set.getValue().activeSkills.entrySet()) {
                        final int skillId = skill.getKey();
                        if (setLevel <= level) {
                            newL.put(SkillFactory.getSkill(skillId), new SkillEntry(skill.getValue(), (byte) SkillFactory.getSkill(skillId).getMasterLevel(), -1L));
                            switch (skillId) {
                                case 80000589:
                                case 80001535:
                                case 80001536:
                                case 80001537:
                                case 80001538:
                                case 80001539:
                                    player.changeSkillLevel(skillId, (byte) 1, (byte) 1);
                                    break;
                            }
                        } else {
                            newL.put(SkillFactory.getSkill(skillId), new SkillEntry(-1, (byte) 0, -1L));
                            switch (skillId) {
                                case 80000589:
                                case 80001535:
                                case 80001536:
                                case 80001537:
                                case 80001538:
                                case 80001539:
                                    player.changeSkillLevel(skillId, (byte) -1, (byte) 0);
                                    break;
                            }
                        }
                    }
                }
            }
        } else {
            for (final int petId : petItemIds) {
                int level = 0;
                final StructSetItem setItem = ii.getSetItem(ii.getSetItemID(petId));
                if (setItem != null) {
                    for (final int setItemId : setItem.itemIDs) {
                        if (petItemIds.contains(setItemId)) {
                            ++level;
                        }
                    }
                    for (final Map.Entry<Integer, StructSetItem.SetItem> set : setItem.items.entrySet()) {
                        final int setLevel = set.getKey();
                        for (final Map.Entry<Integer, Byte> skill : set.getValue().activeSkills.entrySet()) {
                            final int skillId = skill.getKey();
                            if (setLevel <= level) {
                                newL.put(SkillFactory.getSkill(skillId), new SkillEntry(skill.getValue(), (byte) SkillFactory.getSkill(skillId).getMasterLevel(), -1L));
                                switch (skillId) {
                                    case 80000589:
                                    case 80001535:
                                    case 80001536:
                                    case 80001537:
                                    case 80001538:
                                    case 80001539:
                                        player.changeSkillLevel(skillId, (byte) 1, (byte) 1);
                                        break;
                                }
                            } else {
                                newL.put(SkillFactory.getSkill(skillId), new SkillEntry(-1, (byte) 0, -1L));
                                switch (skillId) {
                                    case 80000589:
                                    case 80001535:
                                    case 80001536:
                                    case 80001537:
                                    case 80001538:
                                    case 80001539:
                                        player.changeSkillLevel(skillId, (byte) -1, (byte) 0);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!newL.isEmpty()) {
            player.getClient().getSession().writeAndFlush((Object) CWvsContext.updateSkills(newL));
        }
    }

}
