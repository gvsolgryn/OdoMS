importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.client.inventory);

var Meatra = [2630782, 2633914, 2633923, 2635400];
var magical_book = [1162080, 1162081, 1162082, 1162083];

var Callos = 4310320;
var Callos2 = 4310325;

var enter = "\r\n";

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(M, T, S) {
    if (M != 1) {
        cm.dispose();
        return;
    }

    if (M == 1)
        St++;

    if (St == 0) {
        var msg = "무엇을 도와드릴까요?#fs11#" + enter;
        msg += "#L2##i2633915# 아케인 방어구 상자를 #i" + Callos2 + "# 출석 코인으로 교환" + enter;
        msg += "#L3##i2633914# 아케인 무기 상자를 #i" + Callos2 + "# 출석 코인으로 교환" + enter;
        msg += "#L4##i2633915# 아케인 방어구 상자를 #i" + Callos + "# 사냥 코인으로 교환" + enter;
        msg += "#L0##i2633914# 아케인 무기 상자를 #i" + Callos + "# 사냥 코인으로 교환" + enter;
        msg += "#L1#저주받은 마도서를 #i" + Callos + "# #z" + Callos + "#로 교환" + enter;
        cm.sendSimple(msg);
    } else if (St == 1) {
        v_equip = Packages.client.inventory.MapleInventoryType.EQUIP;
        if (slotcheck(2) == false) {
            cm.sendOk("소비창을 2칸이상 비워주세요.");
            cm.dispose();
            return;
        } else {
            if (S == 0) {
                if (!cm.haveItem(2633914, 1)) {
                    cm.sendOk("아이템이 충분하지 않습니다.");
                    cm.dispose();
                    return;
                }
                if (cm.haveItem(2633914, 1)) {
                    cm.gainItem(Callos, 500);
                    cm.gainItem(2633914, -1);
                    cm.sendOk("#i" + Callos + "# #z" + Callos + "# 500개로 교환해드렸습니다.");
                    cm.dispose();
                }
	    }
            if (S == 2) {
                if (!cm.haveItem(2633915, 1)) {
                    cm.sendOk("아이템이 충분하지 않습니다.");
                    cm.dispose();
                    return;
                }
                if (cm.haveItem(2633915, 1)) {
                    cm.gainItem(Callos2, 5);
                    cm.gainItem(2633915, -1);
                    cm.sendOk("#i" + Callos2 + "# #z" + Callos2 + "# 5개로 교환해드렸습니다.");
                    cm.dispose();
                }
	    }
            if (S == 3) {
                if (!cm.haveItem(2633914, 1)) {
                    cm.sendOk("아이템이 충분하지 않습니다.");
                    cm.dispose();
                    return;
                }
                if (cm.haveItem(2633914, 1)) {
                    cm.gainItem(Callos2, 5);
                    cm.gainItem(2633914, -1);
                    cm.sendOk("#i" + Callos2 + "# #z" + Callos2 + "# 5개로 교환해드렸습니다.");
                    cm.dispose();
                }
	    }
            if (S == 4) {
                if (!cm.haveItem(2633915, 1)) {
                    cm.sendOk("아이템이 충분하지 않습니다.");
                    cm.dispose();
                    return;
                }
                if (cm.haveItem(2633915, 1)) {
                    cm.gainItem(Callos, 500);
                    cm.gainItem(2633915, -1);
                    cm.sendOk("#i" + Callos + "# #z" + Callos + "# 500개로 교환해드렸습니다.");
                    cm.dispose();
                }
            } else {
                var hasMagicalBook = false;
                for (var m = 0; m < magical_book.length; m++) {
                    if (cm.haveItem(magical_book[m], 1)) {
                        hasMagicalBook = true;
                        Packages.server.MapleInventoryManipulator.removeById(cm.getPlayer().getClient(), v_equip, magical_book[m], 1, true, false);
                        break;
                    }
                }
                if (!hasMagicalBook) {
                    cm.sendOk("마도서가 충분하지 않습니다.");
                    cm.dispose();
                    return;
                }
                cm.gainItem(Callos, 2000);
                cm.sendOk("#i" + Callos + "# #z" + Callos + "# 2000개로 교환해드렸습니다.");
            }
            cm.dispose();
        }
    }
}

function slotcheck(slotnum) {
    var leftslot = cm.getPlayer().getInventory(slotnum).getNumFreeSlot();
    if (leftslot < 2) {
        return false;
    } else {
        return true;
    }
}
