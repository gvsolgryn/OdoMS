importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.client.inventory);

var Meatra = [1190555, 1190556, 1190557, 1190558, 1190559];
var magical_book = [1162080, 1162081, 1162082, 1162083];

var Callos = 2634472;

var enter = "\r\n"

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		var msg = "무엇을 도와드릴까요?#fs11#"+enter
		msg += "#b아이템-직업 STR, DEX, INT, LUK 순으로 아이템이 교환됩니다.#k"+enter
		msg += "#L0#미트라의 분노를 #i"+Callos+"# #z"+Callos+"#로 교환한다."+enter
		msg += "#L1#마도서를 #i"+Callos+"# #z"+Callos+"#로 교환한다"+enter
		cm.sendSimple(msg);
	} else if(St == 1) {
		v_equip = Packages.client.inventory.MapleInventoryType.EQUIP;
		if(slotcheck(2) == false){
			cm.sendOk("소비창을 2칸이상 비워주세요.");
			cm.dispose();
			return;
		} else {
			if(S == 0){
    			var hasMeatra = false;
    				for(var m = 0; m<Meatra.length; m++){
       					 if(cm.haveItem(Meatra[m], 1)){
            				hasMeatra = true;
            				MapleInventoryManipulator.removeById(cm.getPlayer().getClient(), MapleInventoryType.EQUIP, Meatra[m], 1, true, false);
            				break;
        			}
    			}
    			if(!hasMeatra) {
        			cm.sendOk("미트라분노 아이템이 충분하지 않습니다.");
        			cm.dispose();
        			return;
    			}
    			cm.gainItem(Callos, 2);
    			cm.sendOk("#i"+Callos+"# #z"+Callos+"# 2개로 교환해드렸습니다.");
		} else {
    			var hasMagicalBook = false;
    				for(var m = 0; m<magical_book.length; m++){
        				if(cm.haveItem(magical_book[m], 1)){
           					hasMagicalBook = true;
            					MapleInventoryManipulator.removeById(cm.getPlayer().getClient(), MapleInventoryType.EQUIP, magical_book[m], 1, true, false);
            					break;
       					}
    				}
    				if(!hasMagicalBook) {
        				cm.sendOk("마도서가 충분하지 않습니다.");
        				cm.dispose();
        				return;
    				}
    				cm.gainItem(Callos, 1);
    				cm.sendOk("#i"+Callos+"# #z"+Callos+"# 1개로 교환해드렸습니다.");
			}
			cm.dispose();
		}
	}
}

function slotcheck(slotnum){
	leftslot = cm.getPlayer().getInventory(slotnum).getNumFreeSlot();
	if (leftslot < 2) {
		return false;
	} else {
		return true;
	}
}