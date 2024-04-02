var enter = "\r\n";
var seld = -1, seld2 = -1;
var max = -1;
var item = 0;
var itemArray = [ 1053063, 1053064, 1053065, 1053066, 1053067, 1004808, 1004809, 1004810, 1004811, 1004812, 1004808, 1004809, 1004810, 1004811, 1004812, 1102940, 1102941, 1102942, 1102943, 1102944, 1082695, 1082696, 1082697, 1082698, 1082699, 1053063, 1053064, 1053065, 1053066, 1053067, 1073158, 1073159, 1073160, 1073161, 1073162, //아케인방어구
 1212120, 1213018, 1214018, 1222113, 1232113, 1242122, 1242121, 1262039, 1272017, 1282017, 1292018, 1302343, 1312203, 1322255, 1332279, 1342104, 1362140, 1372228, 1382265, 1402259, 1412181, 1422189, 1432218, 1442274, 1452257, 1462243, 1472265, 1482221, 1492235, 1522143, 1532150, 1582023, 1592020,]; //아케인무기
var 네오코어 = 4319997;
var 코어개수 = 10;
var noItem = true;

enter = "\r\n";

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg = "#fs11##b#h0##k님! 안녕하세요!\r\n보스에서 획득한 아이템을 특별한 재화로 바꿔드리고 있습니다.\r\n\r\n현재 가지고 있는 아이템 물품입니다." + enter + enter;
		for (i = 0; i < itemArray.length; i++) {
			if(cm.haveItem(itemArray[i])) {
			msg += "#L" + i + "##i" + itemArray[i] + "#  #z" + itemArray[i] + "#" + enter;
			noItem = !noItem;
			}
		}
		if(noItem) {
			msg += "#r판매할 수 있는 아이템이 없습니다.";
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		max = itemArray[seld] < 2000000 ? 1 : 999;
		var msg = "#fs11#선택하신 아이템은 #i"+itemArray[seld]+"##b#z"+itemArray[seld]+"##k입니다."+enter;
		msg += "최대 #b"+max+"#k개 판매하실 수 있습니다."+enter+enter;
		msg += "몇 개 판매하시겠습니까?"+enter+enter;
		msg += "#r※판매하실 아이템을 반드시 다시 확인해주세요. 어떤 경우에서도 복구해드리지 않습니다."+enter;
		cm.sendGetNumber(msg, 1, 1, max);
	} else if (status == 2) {
		seld2 = sel;
		if (seld2 > max) {
			cm.dispose();
			return;
		}

		var msg = "#fs11#선택하신 아이템은 #i"+itemArray[seld]+"##b#z"+itemArray[seld]+"##k입니다."+enter;
		msg += "판매하려고 하는 개수가 #b"+seld2+"#k개 맞다면 '예'를 눌러주세요."+enter+enter;
		msg += "총 #i" + 네오코어 + "##z" + 네오코어 + "#  #b"+(코어개수 * seld2)+"#k 개가 지급됩니다."+enter+enter;
		msg += "#r※판매하실 아이템을 반드시 다시 확인해주세요. 어떤 경우에서도 복구해드리지 않습니다."+enter;
		cm.sendYesNo(msg);
	} else if (status == 3) {
		if (seld2 > max) {
			cm.dispose();
			return;
		}

		if (!cm.haveItem(itemArray[seld], seld2)) {
			cm.sendOk("가지고 있는 아이템보다 많은 수를 입력했습니다.");
			cm.dispose();
			return;
		}

		if(max == 1) {
			cm.getPlayer().removeItem(itemArray[seld], -1);
		} else {
			cm.gainItem(itemArray[seld], -seld2);
		}
		cm.gainItem(네오코어,코어개수 * seld2);
		cm.sendOk("교환이 완료되었습니다.");
		cm.dispose();
	}
}