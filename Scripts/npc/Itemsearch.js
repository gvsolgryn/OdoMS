importPackage(Packages.server);
importPackage(Packages.client.inventory);

var status = -1;
var sel = -1;

var banitem = [1102809, 1102864, 1103050, 1103029, 1103138, 1102385, 1102550, 1149990, 1148973, 1148963, 1149997, 1149992, 1149998, 1148966, 1148970, 1148969, 1148978, 1148964, 1148972, 1149994, 1148980, 1148968, 1148971, 1148981, 1148992, 1148993, 1148996, 1148998, 1149996, 1148979, 1148975, 1148999, 1148976, 1148977, 1149000, 1149002, 1149004, 1149006, 1149008, 1154444, 1149001, 1149009, 1149010, 1149011];

function action(mode, type, selection) {
    if (status == 3 && selection == -1 && type == 3 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendGetText("\r\n#fs11##fc0xFF000000#원하는 치장 아이템을 검색해!\r\n\r\n아이템의 정확한 명칭을 모른다면\r\n#b아이템의 이름 일부분만 입력해도 검색이 가능하니 알아두라구#fc0xFF000000#\r\n#fs11#\r\n#b예시로 치어 팡팡 → '팡팡' 으로 검색이 가능해!#fc0xFF000000#\r\n\r\n");
    } else if (status == 1) {
        if (cm.getText().equals("") || cm.getText().equals(" ")) {
            cm.sendOk("잘못 입력한거 같은데?");
            cm.dispose();
            return;
        }
        var t = cm.searchCashItem(cm.getText());
        if (t.equals("")) {
            cm.sendOk("검색된 결과가 없는걸?");
            cm.dispose();
            return;
        }
        cm.sendSimple("#fs11#입력한 [#b" + cm.getText() + "#k]의 검색 결과야\r\n\r\n" + t);
    } else if (status == 2) {
        	sel = selection;
	if (!MapleItemInformationProvider.getInstance().isCash(sel)) {
		cm.sendOk("#fs11#오류가 발생 했어");
		cm.dispose();
		return;
	}
	if (banitem.indexOf(sel) != -1) {
		cm.sendOk("#fs11#해당 아이템은 구매할 수 없어");
		cm.dispose();
		return;
	}
        cm.sendYesNo("#fs11#정말 선택한 #i" + sel + "##b#t" + sel + "##k (을)를 지급 받을거야?");
    } else if (status == 3) {
try {
        var ii = MapleItemInformationProvider.getInstance();
	if (!MapleItemInformationProvider.getInstance().isCash(sel)) {
		cm.sendOk("#fs11#오류가 발생 했어");
		cm.dispose();
		return;
	}
	if (banitem.indexOf(sel) != -1) {
		cm.sendOk("#fs11#해당 아이템은 구매할 수 없어");
		cm.dispose();
		return;
	}
	var item = ii.getEquipById(sel);
	var flag = item.getFlag();
	item.setFlag(flag | 40);
        item.setUniqueId(MapleInventoryIdentifier.getInstance());
	MapleInventoryManipulator.addbyItem(cm.getClient(), item);
        cm.sendYesNo("#fs11#선택한 아이템을 지급했어!\r\n추가로 더 원하는 게 있다면 '예'를 눌러줘");
}catch(e){
cm.sendOk(e);
}
   } else if (status == 4) {
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 3001604, "Itemsearch");
   }
}