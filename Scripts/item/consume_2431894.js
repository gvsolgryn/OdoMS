/*
  제작자 우비
 */
  maple = [
	"#fs11##i5062010# #z5062010# 10개 #b(조각 50개 소모)#k",
	"#fs11##i5062010# #z5062010# 50개 #b(조각 250개 소모)#k",
	"#fs11##i5062010# #z5062010# 100개 #b(조각 500개 소모)#k",]
function start() {
	var leaf = cm.itemQuantity(2431894);
	var text = "#fs11##r블랙 큐브 조각#k을 #b" + leaf + "개#k 가지고 있습니다.\r\n교환하실 아이템을 선택해 주세요."

	for (var i = 0; i < maple.length; i++)
		text += "\r\n#L" + i + "#" + maple[i] + "#l";
	cm.sendSimpleS(text, 0x04, 9010061);
}

function action(mode, type, selection) {
	cm.dispose();
	if (selection == 0) {
		if (cm.haveItem(2431894, 50)) {
			cm.gainItem(2431894, -50);
			cm.gainItem(5062010, 10);
			cm.sendOkS("#fs11##i2431894# #z2431894# #r50개#k로 선택하신 아이템으로 교환하셨습니다.", 0x04, 9010061);
			cm.dispose();
		} else {
			cm.sendOkS("#fs11##r블랙 큐브 조각#k이 있는지 인벤토리를 확인해주세요.", 0x04, 9010061);
			cm.dispose();;
		}
	} else if (selection == 1) {
		if (cm.haveItem(2431894, 250)) {
			cm.gainItem(2431894, -250);
			cm.gainItem(5062010, 50);
			cm.sendOkS("#fs11##i2431894# #z2431894# #r250개#k로 선택하신 아이템으로 교환하셨습니다.", 0x04, 9010061);
			cm.dispose();
		} else {
			cm.sendOkS("#fs11##r블랙 큐브 조각#k이 있는지 인벤토리를 확인해주세요.", 0x04, 9010061);
			cm.dispose();;
		}
	} else if (selection == 2) {
		if (cm.haveItem(2431894, 500)) {
			cm.gainItem(2431894, -500);
			cm.gainItem(5062010, 100);
			cm.sendOkS("#fs11##i2431894# #z2431894# #r500개#k로 선택하신 아이템으로 교환하셨습니다.", 0x04, 9010061);
			cm.dispose();
		} else {
			cm.sendOkS("#fs11##r블랙 큐브 조각#k이 있는지 인벤토리를 확인해주세요.", 0x04, 9010061);
			cm.dispose();;
		}
	} else if (selection == 3) {
		if (cm.haveItem(2431894, 500)) {
			cm.gainItem(2431894, -500);
			cm.gainItem(5062010, 50);
			cm.sendOk("#fs11##i2431894# 500개로 선택하신 아이템으로 교환하셨습니다.");
			cm.dispose();
		} else {
			cm.sendOk("#fs11##r블랙 큐브 조각#k이 있는지 다시 인벤토리를 확인해주세요.");
			cm.dispose();
		}
	}
}