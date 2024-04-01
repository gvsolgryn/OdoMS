var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var txt = "";
        for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
            if (cm.getInventory(1).getItem(i) != null) {
                txt += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#\r\n";
            }
        }
        cm.sendSimple(txt);

    } else if (status == 1) {
        if (selection == -1) {
            cm.sendOk("존재하지 않는 아이템입니다.");
            cm.dispose();
            return;
        }

        if (!cm.haveItem(2049372)) {
            cm.sendOk("스타포스 25성 강화권 아이템을 가지고 있지 않습니다.");
            cm.dispose();
            return;
        }

        if (cm.getInventory(1).getItem(selection).getUpgradeSlots() != 0) {
            cm.sendOk("업그레이드 횟수가 남아 있는 아이템은 사용할 수 없습니다.");
            cm.dispose();
            return;
        }
        
        cm.gainItem(2049372, -1);
        cm.StarForceEnchant25(cm.getInventory(1).getItem(selection));
        cm.sendOk("스타포스 25성 강화권 아이템을 사용하여 강화를 성공하였습니다.");
        cm.dispose();
    }
}
