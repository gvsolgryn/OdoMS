importPackage(Packages.constants);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
var enter = "\r\n";
var seld = -1;

var allstat = 500, atk = 500; // 1회당 올스텟, 공마 증가치

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
		var txt = "#fs11##fc0xFF000000#장비 아이템을 #b#z2635903##fc0xFF000000#로 강화할 수 있다는 사실을 알고 계시나요? #b원하시는 장비아이템#fc0xFF000000#을 골라주세요.\r\n#r(최대 1회 올스텟 500/ 공,마 500)#k\r\n";
		
		for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {

			if (cm.getInventory(1).getItem(i) != null &&
				!GameConstants.isArcaneSymbol(cm.getInventory(1).getItem(i).getItemId()) &&
				!GameConstants.isAuthenticSymbol(cm.getInventory(1).getItem(i).getItemId())
				)
				  {
					txt += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#\r\n";
			}
		}
		cm.sendSimple(txt);
	} else if (status == 1) {
		if (!cm.haveItem(2437733, 1)) {
			cm.sendOk("#fs11##fc0xFF000000#강화하기 위해선 #b" + price + " #z2635903##fc0xFF000000#가 필요합니다.");
			cm.dispose();
			return;
		}

		item = cm.getInventory(1).getItem(sel);
		if (item == null) {
			return;
		}
		if(item.getCoption2() == 10) {
			cm.sendOk("이 아이템은 이미 스텟 부여를 완료하였습니다.");
			cm.dispose();
			return;
		}

		item.setStr(item.getStr() + allstat);
		item.setDex(item.getDex() + allstat);
		item.setInt(item.getInt() + allstat);
		item.setLuk(item.getLuk() + allstat);
		item.setWatk(item.getWatk() + atk);
		item.setMatk(item.getMatk() + atk);
		item.setCoption2(item.getCoption2() + 1);
		cm.gainItem(2437733, -1);
		var chat = "";
		chat += "#fs15#스탯부여에 성공했습니다. \r\n\r\n";

		chat += "#b현재 강화 횟수 : " +item.getCoption2()+ "#k\r\n";
		chat += "#bSTR : " +item.getStr() + "\r\n";
		chat += "#bDEX : " +item.getDex() + "\r\n";
		chat += "#bINT : " +item.getInt() + "\r\n";
		chat += "#bLUK  : " +item.getLuk() + "\r\n";
		chat += "#b공격력 : " + item.getWatk()+ "\r\n";
		chat += "#b마력 : " +item.getMatk()+ "\r\n";
		a = new Date();
		 fFile1 = new File("Log/ELYSIA강화권.log");
            if (!fFile1.exists()) {
                fFile1.createNewFile();
            }
        out1 = new FileOutputStream("Log/ELYSIA강화권.log", true);
        var msg = "캐릭터 : " + cm.getPlayer().getName() + "\r\n";
		msg += "사용 아이템 : " + item.getItemId()+ "\r\n";
        msg += "사용 시각 : "+a.getFullYear()+"년 "+Number(a.getMonth() + 1)+"월 "+a.getDate()+"일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초\r\n";
        out1.write(msg.getBytes());
        out1.close();
		cm.sendOk(chat);
		
		cm.dispose();
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
	}
}
