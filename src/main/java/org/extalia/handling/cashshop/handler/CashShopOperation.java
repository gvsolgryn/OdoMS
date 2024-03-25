package org.extalia.handling.cashshop.handler;

import org.extalia.client.*;
import org.extalia.client.inventory.Item;
import org.extalia.client.inventory.ItemFlag;
import org.extalia.client.inventory.MapleInventory;
import org.extalia.client.inventory.MapleInventoryType;
import org.extalia.constants.GameConstants;
import org.extalia.database.DatabaseConnection;
import org.extalia.handling.cashshop.CashShopServer;
import org.extalia.handling.channel.ChannelServer;
import org.extalia.handling.login.LoginServer;
import org.extalia.handling.world.CharacterTransfer;
import org.extalia.handling.world.PlayerBuffStorage;
import org.extalia.handling.world.World;
import org.extalia.server.CashItemFactory;
import org.extalia.server.CashItemInfo;
import org.extalia.server.MapleInventoryManipulator;
import org.extalia.server.MapleItemInformationProvider;
import org.extalia.server.quest.MapleQuest;
import org.extalia.tools.CurrentTime;
import org.extalia.tools.FileoutputUtil;
import org.extalia.tools.StringUtil;
import org.extalia.tools.Triple;
import org.extalia.tools.data.LittleEndianAccessor;
import org.extalia.tools.packet.CField;
import org.extalia.tools.packet.CSPacket;
import org.extalia.tools.packet.CWvsContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.extalia.handling.channel.handler.InterServerHandler.sendOpcodeEncryption;

public class CashShopOperation {

    public static final int R = 3;

    public static void LeaveCS(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        c.updateLoginState(1, c.getSessionIPAddress());
        try {
            PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
            PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
            c.getSession().writeAndFlush(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        } finally {
            FileoutputUtil.log(FileoutputUtil.캐시샵퇴장로그, "[퇴장] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  캐시샵 퇴장");
            String s = c.getSessionIPAddress();
            LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
            chr.saveToDB(true, true);
            c.setPlayer(null);
            c.setReceiving(false);
            c.setCashShop(false);
        }
    }

    public static void EnterCS(int playerid, MapleClient c) {
        CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) {
            c.getSession().close();
            return;
        }
        c.loadKeyValues();
        c.loadCustomDatas();
        updateCharge(c);
        c.setCashShop(true);
        chr.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(chr.getId()));
        World.isCharacterListConnected(c.getPlayer().getName(), c.loadCharacterNames(c.getWorld()));
        c.updateLoginState(2, c.getSessionIPAddress());
        CashShopServer.getPlayerStorage().registerPlayer(chr);
        c.getSession().writeAndFlush(CSPacket.warpCS(c));
        //sendOpcodeEncryption(c);
        FileoutputUtil.log(FileoutputUtil.캐시샵입장로그, "[입장] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  캐시샵 입장");
        int i;
        for (i = 120000000; i < 120000100; i++) {
            if (c.getKeyValue(i + "") != null) {
                int value = Integer.parseInt(c.getKeyValue(i + ""));
                c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            } else {
                c.getSession().writeAndFlush(CSPacket.showCount(i, 0));
            }
        }
        for (i = 120100000; i < 120100100; i++) {
            if (c.getKeyValue(i + "") != null) {
                int value = Integer.parseInt(c.getKeyValue(i + ""));
                c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            } else {
                c.getSession().writeAndFlush(CSPacket.showCount(i, 0));
            }
        }
        c.getSession().writeAndFlush(CSPacket.getCSInventory(c));
        c.getSession().writeAndFlush(CSPacket.coodinationResult(0, 0, chr));
        doCSPackets(c);
    }

    public static void updateCharge(MapleClient c) {
        String lastEnter = c.getCustomData(6, "enter");
        String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2);

        if (lastEnter == null || !lastEnter.equals(date)) {
            c.setCustomData(6, "enter", date);
            if (lastEnter != null) {
                int grade = c.getMVPGrade();
                c.setCustomData(6, "sp_" + grade, date);
            }
        }
    }

    public static void csCharge(MapleClient c) {
        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        c.getSession().writeAndFlush(CSPacket.addCashPoint("."));
    }

    public static void CSUpdate(MapleClient c) {

        c.getSession().writeAndFlush(CSPacket.getCSGifts(c));
        int i;
        for (i = 120000000; i < 120000100; i++) {
            if (c.getKeyValue(i + "") != null) {
                int value = Integer.parseInt(c.getKeyValue(i + ""));
                c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            }
        }

        for (i = 120100000; i < 120100100; i++) {
            if (c.getKeyValue(i + "") != null) {
                int value = Integer.parseInt(c.getKeyValue(i + ""));
                c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            }
        }
        doCSPackets(c);
        c.getSession().writeAndFlush(CSPacket.sendWishList(c.getPlayer(), false));
    }

    public static void updateCharge(MapleClient c, int value) {
        String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2);
        int grade = c.getMVPGrade();
        String charge = c.getCustomData(4, date);
        int add = (charge == null) ? 0 : Integer.parseInt(charge);
        c.setCustomData(4, date, String.valueOf(add + value));

        if (grade != c.getMVPGrade()) {
            c.setCustomData(6, "sp_" + c.getMVPGrade(), date);
        }
    }

    public static void mvpSpecialPack(int grade, MapleClient c) {
        int mvpGrade = c.getMVPGrade();
        if (grade <= mvpGrade) {
            String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2);
            c.setCustomData(6, "sp_" + grade, "R" + date);
        } else {
            c.getPlayer().dropMessage(1, "스페셜팩 수령이 불가능합니다.");
        }
    }

    public static void mvpGiftPack(MapleClient c) {
        String data = c.getCustomData(6, "gp");
        System.out.println("MVP 등급 : " + c.getMVPGrade());
        System.out.println("최근 3달 충전 내역 : " + c.chargePoint());
        System.out.println("총 충전 내역 : " + c.allChargePoint());

        String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getDate()), '0', 2) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getHour()), '0', 2);

        if (data == null || (data != null && !data.contains(date))) {
            c.setCustomData(6, "gp", date);
        } else {
            c.getPlayer().dropMessage(1, "기프트팩 수령이 불가능합니다.");
        }
        System.out.println("String : " + date);
    }

    public static void coodinationResult(LittleEndianAccessor slea, MapleClient c) throws IOException {
        int type = slea.readInt();
        MapleCharacter chr = c.getPlayer();
        if (chr == null) {
            c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 1, chr));
            return;
        }
        AvatarLook a = new AvatarLook();
        if (type == 1) {
            AvatarLook.decodeAvatarLook(a, slea);

            for (AvatarLook aL : chr.getCoodination()) {
                if (aL != null && aL.compare(a)) {
                    c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 3, chr));
                    return;
                }
            }

            if (chr.getCoodination().size() < 6) {
                chr.getCoodination().add(a);
                c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 0, chr));
            } else {
                c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 4, chr));
            }
        } else if (type == 2) {
            AvatarLook.decodeUnpackAvatarLook(a, slea);
            AvatarLook target = null;
            for (AvatarLook aL : chr.getCoodination()) {
                if (aL != null && aL.compare(a)) {
                    target = aL;
                    break;
                }
            }
            if (target != null) {
                chr.getCoodination().remove(target);
                c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 0, chr));
            } else {
                c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 6, chr));
            }
        }
    }

    public static void csGift(LittleEndianAccessor slea, MapleClient c) {
        c.getSession().writeAndFlush(CSPacket.resultGiftItem(true, "", 0));
    }

    public static void CouponCode(String code, MapleClient c) {
        if (code.length() <= 0) {
            return;
        }
        Triple<Boolean, Integer, Integer> info = null;
        try {
            info = MapleCharacterUtil.getNXCodeInfo(code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (info != null && ((Boolean) info.left).booleanValue()) {
            CashItemInfo itez;
            byte slot;
            int type = ((Integer) info.mid).intValue(), item = ((Integer) info.right).intValue();
            try {
                MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Map<Integer, Item> itemz = new HashMap<>();
            int maplePoints = 0, mesos = 0;

            switch (type) {
                case 1:
                case 2:
                    c.getPlayer().modifyCSPoints(type, item, false);
                    maplePoints = item;
                    break;
                case 3:
                    itez = CashItemFactory.getInstance().getItem(item);
                    if (itez == null) {
                        c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                        return;
                    }
                    slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", "Cash shop: coupon code on " + FileoutputUtil.CurrentReadable_Date());
                    if (slot <= -1) {
                        c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                        return;
                    }
                    itemz.put(Integer.valueOf(item), c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem((short) slot));
                    break;
                case 4:
                    c.getPlayer().gainMeso(item, false);
                    mesos = item;
                    break;
            }
            c.getSession().writeAndFlush(CSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
        } else {
            c.getSession().writeAndFlush(CSPacket.sendCSFail((info == null) ? 167 : 165));
        }
        doCSPackets(c);
    }

    public static final void BuyCashItem(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        boolean useMaplePoint;
        int wishlist[], j;
        Item item1;
        short slot;
        String pwd;
        int toCharge;
        CashItemInfo item;
        boolean useMileage;
        int i;
        boolean coupon;
        CashItemInfo cashItemInfo1;
        int sn;
        long uniqueId;
        int id;
        boolean bool1, useAllMileage;
        MapleInventoryType type;
        int slots;
        CashItemInfo cashItemInfo2, cashItemInfo3;
        MapleQuestStatus marr;
        int itemId;
        List<Integer> ccc;
        int k;
        byte b;
        int quantity;
        boolean bool;
        MapleInventory inv;
        Map<Integer, Item> ccz;
        Item item2;
        int a, action = slea.readByte();
        switch (action) {
            case 0:
                slea.skip(2);
                CouponCode(slea.readMapleAsciiString(), c);
                break;

            case 3:
                useMaplePoint = slea.readByte() == 1;
                useMileage = slea.readByte() == 1;
                useAllMileage = slea.readByte() == 1;
                item = CashItemFactory.getInstance().getItem(slea.readInt());
                quantity = slea.readInt();
                if (useMaplePoint) {
                    toCharge = 2;
                } else if (useAllMileage) {
                    toCharge = 4;
                } else {
                    toCharge = 1;
                }
                if ((item.getSN() >= 130400000 && item.getSN() <= 130599999) || (item.getSN() >= 130000000 && item.getSN() <= 130000500) || (item.getSN() >= 130002000 && item.getSN() <= 130002500)) {
                    toCharge = 2;
                    if (!useMaplePoint) {
                        c.send(CWvsContext.serverNotice(1, "", "홍보 아이템은 홍보 포인트로만 구입이 가능합니다."));
                        doCSPackets(c);
                        return;
                    }
                } else if (item.getPrice() > 0) {
                    if (useMaplePoint) {
                        c.send(CWvsContext.serverNotice(1, "", "후원 아이템은 후원 포인트로만 구입이 가능합니다."));
                        doCSPackets(c);
                        return;
                    }
                    toCharge = 1;
                }
                if (item.getLimitMax() > 0) {
                    if (c.getKeyValue(item.getSN() + "") != null && item.getLimitMax() <= Integer.parseInt(c.getKeyValue(item.getSN() + ""))) {
                        return;
                    }
                    final int value = (c.getKeyValue(item.getSN() + "") != null) ? (Integer.parseInt(c.getKeyValue(item.getSN() + "")) + 1) : 1;
                    c.setKeyValue(item.getSN() + "", value + "");
                    c.getSession().writeAndFlush((Object) CSPacket.showCount(item.getSN(), value));
                }
                if (item == null) {
                    c.getSession().writeAndFlush((Object) CSPacket.sendCSFail(0));
                    break;
                }
                if (!item.genderEquals(c.getPlayer().getGender())) {
                    c.getSession().writeAndFlush((Object) CSPacket.sendCSFail(166));
                    doCSPackets(c);
                    return;
                }
                if (c.getPlayer().getCashInventory().getItemsSize() >= 500) {
                    c.getSession().writeAndFlush((Object) CSPacket.sendCSFail(177));
                    doCSPackets(c);
                    return;
                }
                if (Arrays.asList(GameConstants.cashBlock).contains(item.getId())) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
                final int paybackRate = useAllMileage ? 0 : 30;
                final int discountRate = useMileage ? 30 : 0;
                int price = item.getPrice();
                if (discountRate > 0) {
                    price = price * (100 - discountRate) / 100;
                }
                chr.modifyCSPoints(toCharge, -price, false);
                if (useMileage) {
                    chr.modifyCSPoints(4, -(price * discountRate / 100), false);
                }
                if (toCharge == 1) {
                    updateCharge(c, price);
                }
                final Item itemz = chr.getCashInventory().toItem(item);
                if (itemz != null && itemz.getUniqueId() > 0L && itemz.getItemId() == item.getId()) {
                    if (toCharge == 1) {
                        itemz.setFlag(itemz.getFlag() | ((itemz.getType() == 1) ? ItemFlag.KARMA_EQUIP.getValue() : ItemFlag.KARMA_USE.getValue()));
                    }
                    itemz.setPosition((short) (chr.getCashInventory().getItemsSize() + 1));
                    chr.getCashInventory().addToInventory(itemz);
                    final String amount = "amount=900;";
                    final String given = "given=-1;";
                    final String per = "per=9";
                    c.getSession().writeAndFlush((Object) CSPacket.showBoughtCSItem(itemz, c, item.getSN(), paybackRate, discountRate));
                    save(c);
                    FileoutputUtil.log(FileoutputUtil.캐시샵구매로그, "[아이템 구입] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(itemz.getItemId()) + "(" + itemz.getItemId() + ")를 [" + itemz.getQuantity() + "]개 구입");
                    break;
                }
                c.getSession().writeAndFlush((Object) CSPacket.sendCSFail(0));
                break;
            case 5:
                chr.clearWishlist();
                slea.skip(1);
                if (slea.available() < 48L) {
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                wishlist = new int[12];
                for (i = 0; i < 12; i++) {
                    wishlist[i] = slea.readInt();
                }
                chr.setWishlist(wishlist);
                c.getSession().writeAndFlush(CSPacket.sendWishList(chr, true));
                break;
            case 6:
                slea.skip(1);
                j = 1;
                coupon = (slea.readByte() > 0);
                if (coupon) {
                    MapleInventoryType mapleInventoryType = getInventoryType(slea.readInt());
                    if (chr.getCSPoints(1) >= 12000 && chr.getInventory(mapleInventoryType).getSlotLimit() < 89) {
                        chr.modifyCSPoints(1, -12000, false);
                        chr.getInventory(mapleInventoryType).addSlot((short) 8);
                        chr.dropMessage(1, "인벤토리 공간을 늘렸습니다. 현재 " + chr.getInventory(mapleInventoryType).getSlotLimit() + " 슬롯이 되었습니다.\r\n\r\n캐시샵에서 늘려진 슬롯이 바로 보이지 않아도 실제로는 늘려졌으니, 캐시샵에서 나가시면 정상적으로 슬롯이 늘어난걸 볼 수 있습니다.");
                        break;
                    }
                    chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                    break;
                }
                type = MapleInventoryType.getByType(slea.readByte());
                if (chr.getCSPoints(1) >= 8000 && chr.getInventory(type).getSlotLimit() < 93) {
                    chr.modifyCSPoints(1, -8000, false);
                    chr.getInventory(type).addSlot((short) 4);
                    chr.dropMessage(1, "인벤토리 공간을 늘렸습니다. 현재 " + chr.getInventory(type).getSlotLimit() + " 슬롯이 되었습니다.\r\n\r\n캐시샵에서 늘려진 슬롯이 바로 보이지 않아도 실제로는 늘려졌으니, 캐시샵에서 나가시면 정상적으로 슬롯이 늘어난걸 볼 수 있습니다.");
                    break;
                }
                chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                break;
            case 7:
                if (chr.getCSPoints(1) >= 8000 && chr.getStorage().getSlots() < 48) {
                    chr.modifyCSPoints(1, -8000, false);
                    chr.getStorage().increaseSlots((byte) 4);
                    chr.dropMessage(1, "창고슬롯을 늘렸습니다. 현재 창고 슬롯은 " + chr.getStorage().getSlots() + "칸 입니다.");
                    break;
                }
                chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                break;
            case 8:
                slea.skip(1);
                j = 1;
                cashItemInfo1 = CashItemFactory.getInstance().getItem(slea.readInt());
                slots = c.getCharacterSlots();
                if (cashItemInfo1 == null || c.getPlayer().getCSPoints(1) < cashItemInfo1.getPrice() || slots > 15 || cashItemInfo1.getId() != 5430000) {
                    doCSPackets(c);
                    return;
                }
                if (c.gainCharacterSlot()) {
                    c.getPlayer().modifyCSPoints(1, -cashItemInfo1.getPrice(), false);
                    c.getSession().writeAndFlush(CSPacket.buyCharacterSlot());
                    break;
                }
                chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                break;
            case 9:
                j = slea.readByte() + 1;
                sn = slea.readInt();
                cashItemInfo2 = CashItemFactory.getInstance().getItem(sn);

                if (cashItemInfo2 == null || c.getPlayer().getCSPoints(j) < cashItemInfo2.getPrice() || cashItemInfo2.getId() / 10000 != 555) {
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                marr = c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));
                chr.dropMessage(1, "이미 펜던트 늘리기가 적용중입니다.");
                doCSPackets(c);
                break;

            case 13:
                item1 = chr.getCashInventory().findByCashId(slea.readLong(), slea.readInt(), slea.readByte());
                if (item1 != null && item1.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item1.getItemId(), item1.getQuantity(), item1.getOwner())) {
                    Item item_ = item1.copy();
                    short pos = MapleInventoryManipulator.addbyItem(c, item_, false, true);

                    if (pos >= 0) {
                        if (item_.getPet() != null) {
                            item_.getPet().setInventoryPosition(pos);
                        }
                        chr.getCashInventory().removeFromInventory(item1);
                        c.getSession().writeAndFlush(CSPacket.confirmFromCSInventory(item_, c, pos));
                        save(c);
                        FileoutputUtil.log(FileoutputUtil.캐시샵인벤로그, "[캐시템 꺼내기] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(item_.getItemId()) + "(" + item_.getItemId() + ")를 [" + item_.getQuantity() + "] 인벤으로 옮김");
                        break;
                    }
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(177));
                    break;
                }
                c.getSession().writeAndFlush(CSPacket.sendCSFail(177));
                break;
            case 14:
                slot = -1;
                uniqueId = slea.readLong();
                itemId = slea.readInt();
                b = slea.readByte();
                inv = chr.getInventory(b);
                item2 = inv.findByUniqueId(uniqueId);

                for (MapleInventory iv : c.getPlayer().getInventorys()) {
                    item2 = iv.findByUniqueId(uniqueId);
                    if (item2 != null) {
                        slot = item2.getPosition();
                        inv = iv;
                        break;
                    }
                }
                if (GameConstants.isPet(item2.getItemId())) {
                    for (int abc = 0; abc < (c.getPlayer()).pets.length; abc++) {
                        if ((c.getPlayer()).pets[abc] != null && slot == (c.getPlayer()).pets[abc].getInventoryPosition()) {
                            c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "장착 중인 펫은 캐시 보관함에 넣으실 수 없습니다."));
                            c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                            c.getSession().writeAndFlush(CSPacket.sendCSFail(3));
                            c.getSession().writeAndFlush(CSPacket.showNXMapleTokens(c.getPlayer()));
                            c.getSession().writeAndFlush(CSPacket.enableCSUse());
                            c.getPlayer().getCashInventory().checkExpire(c);
                            return;
                        }
                    }
                }
                if (item2 != null && item2.getItemId() == itemId) {
                    c.getPlayer().getCashInventory().addToInventory(item2);
                    c.getSession().writeAndFlush(CSPacket.confirmToCSInventory(item2, c, -1));
                    FileoutputUtil.log(FileoutputUtil.캐시샵인벤로그, "[캐시템 넣기] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(item2.getItemId()) + "(" + item2.getItemId() + ")를 [" + item2.getQuantity() + "] 캐시샵에 넣음");
                    if (item2.getPet() != null) {
                        c.getPlayer().removePet(item2.getPet(), false);
                    }
                    save(c);
                    inv.removeSlot(slot);
                    break;
                }
                c.getSession().writeAndFlush(CSPacket.sendCSFail(177));
                break;
            case 31:
                slea.skip(5);
                pwd = slea.readMapleAsciiString();
                id = slea.readInt();
                break;
            case 34:
                toCharge = slea.readByte() + 1;
                bool1 = (toCharge == 2);
                cashItemInfo2 = CashItemFactory.getInstance().getItem(slea.readInt());
                ccc = null;

                if (cashItemInfo2 != null) {
                    ccc = CashItemFactory.getInstance().getPackageItems(cashItemInfo2.getId());
                }
                if (cashItemInfo2 == null || ccc == null || ccc.isEmpty()) {
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(3));
                    doCSPackets(c);
                    return;
                }
                if ((cashItemInfo2.getSN() >= 130400000 && cashItemInfo2.getSN() <= 130599999) || (cashItemInfo2.getSN() >= 130000000 && cashItemInfo2.getSN() <= 130000500) || (cashItemInfo2.getSN() >= 130002000 && cashItemInfo2.getSN() <= 130002500)) {
                    toCharge = 2;
                    if (!bool1) {
                        c.send(CWvsContext.serverNotice(1, "", "홍보 아이템은 홍보 포인트로만 구입이 가능합니다."));
                        doCSPackets(c);
                        return;
                    }
                } else if (cashItemInfo2.getPrice() > 0) {
                    if (bool1) {
                        c.send(CWvsContext.serverNotice(1, "", "후원 아이템은 후원 포인트로만 구입이 가능합니다."));
                        doCSPackets(c);
                        return;
                    }
                    toCharge = 1;
                }
                if (cashItemInfo2.getLimitMax() > 0) {
                    if (c.getKeyValue(cashItemInfo2.getSN() + "") != null && cashItemInfo2.getLimitMax() <= Integer.parseInt(c.getKeyValue(cashItemInfo2.getSN() + ""))) {
                        return;
                    }
                    int value = (c.getKeyValue(cashItemInfo2.getSN() + "") != null) ? (Integer.parseInt(c.getKeyValue(cashItemInfo2.getSN() + "")) + 1) : 1;
                    c.setKeyValue(cashItemInfo2.getSN() + "", value + "");
                    c.getSession().writeAndFlush(CSPacket.showCount(cashItemInfo2.getSN(), value));
                }
                quantity = slea.readInt() / ccc.size();

                if (c.getPlayer().getCSPoints(toCharge) < cashItemInfo2.getPrice() * quantity) {
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(3));
                    doCSPackets(c);
                    return;
                }
                if (!cashItemInfo2.genderEquals(c.getPlayer().getGender())) {
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(11));
                    doCSPackets(c);
                    return;
                }
                if (c.getPlayer().getCashInventory().getItemsSize() >= 500 - ccc.size() * quantity) {
                    c.getSession().writeAndFlush(CSPacket.sendCSFail(24));
                    doCSPackets(c);
                    return;
                }
                if (Arrays.<Integer>asList(GameConstants.cashBlock).contains(Integer.valueOf(cashItemInfo2.getId()))) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(cashItemInfo2.getId()));
                    doCSPackets(c);
                    return;
                }
                ccz = new HashMap<Integer, Item>();
                chr.modifyCSPoints(toCharge, -cashItemInfo2.getPrice() * quantity, false);
                save(c);

                if (toCharge == 1) {
                    updateCharge(c, cashItemInfo2.getPrice() * quantity);
                }
                break;
            case 48:
                item = CashItemFactory.getInstance().getItem(slea.readInt());
                c.getSession().writeAndFlush(CSPacket.updatePurchaseRecord(item.getSN()));
                break;
            default:
                System.out.println("New Action: " + action + " Remaining: " + slea.toString());
                c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                break;
        }
        doCSPackets(c);
    }

    private static final MapleInventoryType getInventoryType(int id) {
        switch (id) {
            case 50200093:
                return MapleInventoryType.EQUIP;
            case 50200094:
                return MapleInventoryType.USE;
            case 50200197:
                return MapleInventoryType.SETUP;
            case 50200095:
                return MapleInventoryType.ETC;
        }
        return MapleInventoryType.UNDEFINED;
    }

    public static final void doCSPackets(MapleClient c) {
        c.getSession().writeAndFlush(CSPacket.showNXMapleTokens(c.getPlayer()));
        c.getSession().writeAndFlush(CSPacket.enableCSUse());
        c.getPlayer().getCashInventory().checkExpire(c);
    }

    public static void save(MapleClient c) {
        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();
            c.getPlayer().getCashInventory().save(con);
            con.close();
        } catch (Exception exception) {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception1) {
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
    }
}