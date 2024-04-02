var status = -1;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "창고를 이용할시 아이템 1개당 \r\n후원포인트가 30000원씩 차감됩니다.  \r\n※본인 실수는 복구가 불가합니다. \r\n";
        talk += "#b#L0# 창고에서 아이템 꺼내기\r\n";
        talk += "#L1# 창고에 아이템 넣기";
        cm.sendSimple(talk);

    } else if (status == 1) {
        st = selection;
        if (selection == 0) {
            count = 0;
            storage = cm.getPlayer().getStorage();
            arr = storage.getItems();
            talk = "꺼낼 아이템을 선택해 주세요.\r\n\r\n";
            for (i = 0; i < arr.length; i++) {
                count++;
                talk += "#L" + i + "# #i" + arr[i].getItemId() + "##l";
                if (i % 5 == 4) {
                    talk += "\r\n";
                }
            }
            if (count == 0) {
                cm.sendOk("꺼낼 아이템이 존재하지 않습니다.");
                cm.dispose();
                return;
            } else {
                cm.sendSimple(talk);
            }
        } else {
            talk = "아이템을 선택해 주세요.\r\n\r\n";
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i) != null && cm.getInventory(1).getItem(i).getItemId()) {
                    talk += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "##k#l\r\n";
                }
            }
            cm.sendSimple(talk);
        }

    } else if (status == 2) {
        if (st == 0) {
            Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), cm.getPlayer().getStorage().getItems().get(selection));
            cm.getPlayer().getStorage().takeOut(selection);
            cm.getPlayer().gainDonationPoint(-15000);
            cm.sendOk("창고에서 성공적으로 아이템을 꺼냈습니다.");
            cm.dispose();
            return;
        } else {
	if (cm.getPlayer().getDonationPoint() >= 30000) {
	    
                cm.getPlayer().getStorage().store(cm.getInventory(1).getItem(selection));
                Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selection, cm.getInventory(1).getItem(selection).getQuantity(), false);
                cm.getPlayer().gainDonationPoint(-15000);
                cm.sendOk("창고에 성공적으로 저장되었습니다.");
                cm.dispose();
                return;
	} else {
                cm.sendOk("후원포인트가 부족합니다.");
                cm.dispose();
                return;
	}
        }
    }
}