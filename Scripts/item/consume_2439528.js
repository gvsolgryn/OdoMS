importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
var status = -1;

function start() {
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    wList = [];
    wGain = [];
    if (status == 0) {
		   leftslot = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 1) {
            cm.sendOk("인벤토리를 비우고 다시 말을 걸어주세요.");
            cm.dispose();
            return;
        }
        getItem();
        getItem2();
        selStr = "\r\n#b#i2635931##z2635931##k입니다.\r\n #r아래에서 원하시는 장비를 선택해주세요.\r\n\r\n#r선택하면 바로 지급되니 주의해주세요.#k\r\n\r\n";
        for (i = 0; i < wList.length; i++) {
            selStr += "#L"+i+"##i" + wList[i] + ":# #t" + wList[i] + ":##l\r\n";
        }
        cm.sendYesNo(selStr);

    } else if (status == 1) {
		   getItem();
		  var selStr2 = "아래의 아이템이 수령되었습니다.\r\n\r\n";
			selStr2 += "#b#i"+ wList[selection]+"##t"+ wList[selection] + "#";
			cm.gainItem(wList[selection], 1);
			cm.gainItem(2439528, -1);
        cm.sendSimpleS(selStr2, 4, 2007);
        cm.dispose();
    }
}

function getItem() {
        wList.push(1005980),
        wList.push(1005981),
        wList.push(1005982),
		wList.push(1005983),
		wList.push(1005984),
		wList.push(1042433),
		wList.push(1042434),
		wList.push(1042435),
		wList.push(1042436),
		wList.push(1042437),
		wList.push(1062285),
		wList.push(1062286),
		wList.push(1062287),
		wList.push(1062288),
		wList.push(1062289),
                        wList.push(1152212),
                        wList.push(1152213),
                        wList.push(1152214),
                        wList.push(1152215), 
                        wList.push(1152216);
}

function getItem2() {
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1),
    wGain.push(1);
}