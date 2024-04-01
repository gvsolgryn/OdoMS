importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var enter = "\r\n";
var seld = -1;

var need = 2439629, qty = 1;

var pot = [
	{'name' : "공격력 +12%", 'code' : 40051},
	{'name' : "마력 +12%", 'code' : 40052},
	{'name' : "보스 공격시 데미지 +40%", 'code' : 40603},
	{'name' : "몬스터 방어력 무시 +40%", 'code' : 40292},
	{'name' : "크리티컬 데미지 +8%", 'code' : 40057}
]
var a = 0;
var pots = [-1, -1, -1];
var potn = ["", "", ""];


function ItemListt() {
    status = -1;
    action(1, 0, 0);
}

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
		var chat = "#e#b[잠재 부여 시스템]#k#n\r\n\r\n#r원하는 잠재 능력을 선택해주세요.#k"+enter;

	   for (i = 1; i < cm.getInventory(1).getSlotLimit(); i++) {
                    if (cm.getInventory(1).getItem(i)) {
                        if (cm.getInventory(1).getItem(i).getOwner() != "+999강") {

                             chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "# #n\r\n"
                        }
                    }
                }
            cm.sendSimple("#fs11#" + chat);
	} else if (status == 1) {
		  seld = sel;
		  selditem =  cm.getInventory(1).getItem(sel).getItemId();

		var msg = "#e#r[잠재 설정할 무기]#n#k"+enter;
		msg += "아이템 : #b#i"+selditem+"##z"+selditem+"# 1개#k"+enter;

		msg += "선택하신 무기가 맞으신지 확인해주세요.";
		cm.sendYesNo(msg);
	} else if (status >= 2 && status <= 4) {
		if (status > 2) {
			pots[status - 3] = pot[sel]['code'];
			potn[status - 3] = pot[sel]['name'];
		}
        //잠재능력 선택
		var msg = (status - 1) + "번째 잠재능력을 선택해주세요.#b"+enter;
		for (i = 0; i < 5; i++)
		msg += "#L"+i+"#"+pot[i]['name']+enter; // 잠재능력 리스트
		cm.sendSimple(msg);
	} else if (status == 5) {

		pots[2] = pot[sel]['code'];
		potn[2] = pot[sel]['name'];
		var msg = "#r#e선택하신 잠재능력이 맞는지 확인해주세요.\r\n#n#k"+enter;
		for (i = 0; i < 3; i++)
			msg += "#b"+potn[i]+"#k"+enter;
		msg += "#r#e\r\n다시 설정 하시려면 '아니요', 지급받으시려면 '예'를 눌러주세요.#n"+enter;
		cm.sendYesNo(msg);
	} else if (status == 6) {
		vitem = cm.getInventory(1).getItem(seld);
		vitem.setPotential1(pots[0]);
		vitem.setPotential2(pots[1]);
		vitem.setPotential3(pots[2]);
//		vitem.setPotential4(pots[3]);
//		vitem.setPotential5(pots[4]);
//		vitem.setPotential6(pots[5]);
		cm.gainItem(need, -qty);
	//	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
	    cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
		cm.sendOk("지급 완료 되었습니다.");
		cm.dispose();
	}
}