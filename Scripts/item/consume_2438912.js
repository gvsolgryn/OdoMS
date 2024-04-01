
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

var need = 2438912, qty = 1;

var items = [1352208, 1352218, 1352228, 1352937, 1099014, 1098008, 1352508, 1352278, 1352268, 1352011, 1352969, 1352238, 1352248, 1352258, 1352947, 1352408, 1352959, 1352288, 1352298, 1352111, 1353008, 1352908, 1352918, 1352980, 1353107, 1352608, 1352709, 1352977];

var pot = [
	{'name' : "공격력 +12%", 'code' : 40051},
	{'name' : "마력 +12%", 'code' : 40052},
	{'name' : "보스 공격시 데미지 +40%", 'code' : 40603},
	{'name' : "몬스터 방어력 무시 +40%", 'code' : 40292},
	{'name' : "아이템 획득 확률 +20%", 'code' : 40656}
]
var a = 0;
var pots = [-1, -1, -1, -1, -1, -1];
var potn = ["", "", "", "", "", ""];
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
		var msg = "#e#b[트레져 모조무기 지급]#k#n\r\n\r\n#r지급받으실 트레져 보조무기를 선택해주세요.#k"+enter;
		for (i = 0; i < items.length; i++)
			msg += "#L"+i+"##i"+items[i]+"##z"+items[i]+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		selditem = items[seld];
		var msg = "#e#r[선택하신 무기의 정보입니다.]#n#k"+enter;
		msg += "아이템 : #b#i"+selditem+"##z"+selditem+"# 1개#k"+enter;
		msg += "올스탯 : #b+200#k"+enter;
		msg += "공격력 : #b+100#k"+enter;
		msg += "마　력 : #b+100#k"+enter;
		msg += "스타포스 #b12성#k 강화 적용"+enter;
		msg += "선택하신 무기가 맞으신지 확인해주세요.";
		cm.sendYesNo(msg);
	} else if (status == 2) {
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selditem);
		item.setStr(200);
		item.setDex(200);
		item.setInt(200);
		item.setLuk(200);
		item.setWatk(100);
		item.setMatk(100);
		item.setLevel(item.getUpgradeSlots());
		item.setUpgradeSlots(0);
		item.setEnhance(12);
		cm.gainItem(need, -qty);
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
		cm.sendOk("지급 완료 되었습니다.");
		cm.dispose();
	}
}