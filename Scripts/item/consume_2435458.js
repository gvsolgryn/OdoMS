/*
  제작자 우비
 */
  importPackage(Packages.client.inventory);
maple = [
    "#fs11##i4319995# #z4319995# 1개 #b(조각 30개 소모)#k",
    "#fs11##i4319996# #z4319996# 1개 #b(조각 50개 소모)#k",]

function start() {
    var leaf = cm.itemQuantity(2435458);
    if (cm.itemQuantity(2435458) < 3) {
        말 = "#fs11##b#z2435458# #r3개#k를 모으면 #i4319995# #b#z4319995# (1개),\r\n"
        //말 += "#r30개#k를 모으면 #i4319996# #b#z4319996#,\r\n"
        말 += "#r30개#k를 모으면 #i4319996# #b#z4319996##k를 받을 수 있습니다.\r\n"
        cm.sendSimpleS(말, 0x04, 9010061);
        cm.dispose();
    } else {
        var text = "#fs11#골드애플 조각을 #e"+leaf+"#n개 가지고 있습니다.\r\n교환하실 아이템을 선택해 주세요."
        for (var i = 0; i < maple.length; i++)
            text += "\r\n#L" + i + "#" + maple[i] + "#l";
        cm.sendSimple(text, 9010061);
    }
}

function action(mode, type, selection) {
    cm.dispose();
    if (selection == 0) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
           cm.sendOkS("#fs11##r기타창 2칸 이상을 비워주세요.", 0x04, 9010061);
           cm.dispose();
           return;
        }
        if (cm.haveItem(2435458, 30)) {
            cm.gainItem(2435458, -30);
            cm.gainItem(4319995, 1);
            cm.dispose();
        } else {
            cm.sendOkS("#fs11#골드애플 조각이 부족합니다.", 0x04, 9010061);
            cm.dispose();;
        }
    }
    if (selection == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
        if (leftslot < 2) {
           cm.sendOkS("#fs11##r캐시창 2칸 이상을 비워주세요.", 0x04, 9010061);
           cm.dispose();
           return;
        }
        if (cm.haveItem(2435458, 50)) {
            cm.gainItem(2435458, -50);
            cm.gainItem(4319996, 1);
            cm.dispose();
        } else {
            cm.sendOkS("#fs11#골드애플 조각이 부족합니다.", 0x04, 9010061);
            cm.dispose();;
        }
    }
    if (selection == 2) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
        if (leftslot < 2) {
           cm.sendOkS("#fs11##r캐시창 2칸 이상을 비워주세요.", 0x04, 9010061);
           cm.dispose();
           return;
        }
        if (cm.haveItem(2435458, 30)) {
            cm.gainItem(2435458, -30);
            cm.gainItem(4319996, 1);
            cm.dispose();
        } else {
            cm.sendOkS("#fs11#골드애플 조각이 부족합니다.", 0x04, 9010061);
            cm.dispose();;
        }
    }
    if (selection == 3) {
        if (cm.haveItem(2435458, 30)) {
            cm.gainItem(2435458, -30);
            cm.gainItem(5060048, 3);
            cm.sendOk("#i2435458# 30개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }
    } else if (selection == 3) {
        if (cm.haveItem(2435458, 40)) {
            cm.gainItem(2435458, -40);
            cm.gainItem(5060048, 1);
            cm.sendOk("#i2435458# 40개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }
    } else if (selection == 4) {
        if (cm.haveItem(2435458, 50)) {
            cm.gainItem(2435458, -50);
            cm.gainItem(5068301, 1);
            cm.sendOk("#i2435458# 50개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }
    } else if (selection == 5) {
        if (cm.haveItem(2435458, 150)) {
            cm.gainItem(2435458, -150);
            cm.gainItem(1113055, 1);
            cm.sendOk("#i2435458# 150개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }

    } else if (selection == 6) {
        if (cm.haveItem(2435458, 30)) {
            cm.gainItem(2435458, -30);
            cm.gainMeso(2000000000);
            cm.sendOk("#i2435458# 30개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }

    } else if (selection == 7) {
        if (cm.haveItem(2435458, 10)) {
            cm.gainItem(2435458, -10);
            cm.gainItem(2041510, 1);
            cm.sendOk("#i2435458# 10개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }

    } else if (selection == 8) {
        if (cm.haveItem(2435458, 45)) {
            cm.gainItem(2435458, -45);
            cm.gainItem(2041510, 5);
            cm.sendOk("#i2435458# 45개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("골드애플 조각이 부족합니다.");
            cm.dispose();;
        }
    } else if (selection == 12) {
        if (cm.haveItem(3994745, 1)) {
            cm.gainItem(3994745, -1);
            cm.gainItem(1702363, 1);
            cm.sendOk("#i3994742# 1개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk(" 물고기가 1개가 있는지 다시 이벤토리창에서 확인하세요.");
            cm.dispose();
        }
    } else if (selection == 13) {
        if (cm.haveItem(3994745, 221)) {
            cm.gainItem(3994745, -221);
            cm.gainItem(1112663, 1);
            cm.sendOk("#i3994742# 2개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk(" 물고기가 2개가 있는지 다시 이벤토리창에서 확인하세요.");
            cm.dispose();
        }
    } else if (selection == 14) {
        if (cm.haveItem(3994745, 1500)) {
            cm.gainItem(3994745, -1500);
            cm.gainItem(1002140, 1);
            cm.sendOk("#i3994742# 개로 선택하신 아이템으로 교환하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("물고기가 개가 있는지 다시 이벤토리창에서 확인하세요.");
            cm.dispose();
        }
    }
}