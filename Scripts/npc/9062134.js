importPackage(java.lang);
importPackage(java.io);
var status = -1;
var aa = 0;
var setting = [];
var setting_1 = [ 
		["Hard_Magnus_1", 1, 401060100, 175],
        ["Normal_Magnus_1", 1, 401060200, 155],
        ["Easy_Magnus_1", 1, 401060300, 115]
	]
var setting_2 = [
		["Hard_Magnus_2", 1, 401060100, 175],
        ["Normal_Magnus_2", 1, 401060200, 155],
        ["Easy_Magnus_2", 1, 401060300, 115]]
var setting_3 = [
		["Hard_Magnus_3", 1, 401060100, 175],
        ["Normal_Magnus_3", 1, 401060200, 155],
        ["Easy_Magnus_3", 1, 401060300, 115]]
var setting_4 = [
		["Hard_Magnus_4", 1, 401060100, 175],
        ["Normal_Magnus_4", 1, 401060200, 155],
        ["Easy_Magnus_4", 1, 401060300, 115]]
var setting_5 = [
		["Hard_Magnus_5", 1, 401060100, 175],
        ["Normal_Magnus_5", 1, 401060200, 155],
        ["Easy_Magnus_5", 1, 401060300, 115]]
var setting_6 = [
		["Hard_Magnus_6", 1, 401060100, 175],
        ["Normal_Magnus_6", 1, 401060200, 155],
        ["Easy_Magnus_6", 1, 401060300, 115]]
var setting_7 = [
		["Hard_Magnus_7", 1, 401060100, 175],
        ["Normal_Magnus_7", 1, 401060200, 155],
        ["Easy_Magnus_7", 1, 401060300, 115]]
var name = ["하드", "노멀", "이지"]

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    /*setting = [
        ["Hard_Magnus", 1, 401060100, 175],
        ["Normal_Magnus", 1, 401060200, 155],
        ["Easy_Magnus", 1, 401060300, 115]
    ]*/

    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
		getData();
		if (day == 1) {
			aa = setting_1;
		} else if (day == 2) {
			aa = setting_2;
		} else if (day == 3) {
			aa = setting_4;
		} else if (day == 4) {
			aa = setting_4;
		} else if (day == 5) {
			aa = setting_5;
		} else if (day == 6) {
			aa = setting_6;
		} else if (day == 7) {
			aa = setting_7;
		}
		setting.push([aa[0][0], aa[0][1], aa[0][2], aa[0][3]]);
		setting.push([aa[1][0], aa[1][1], aa[1][2], aa[1][3]]);
		setting.push([aa[2][0], aa[2][1], aa[2][2], aa[2][3]]);
		st = 0;
		bb = setting[st][0];
		cc = setting[st][1];
		dd = setting[st][2];
		ee = setting[st][3];
        talk = "매그너스 퇴치를 위해 폭군의 왕좌로 이동 하시겠습니까??\r\n";
        talk += "#L0##b폭군의 왕좌(하드)로 이동 한다. (레벨 175이상)#l\r\n"
        talk += "#L1#폭군의 왕좌(노멀)로 이동 한다. (레벨 155이상)#l\r\n"
        talk += "#L2#폭군의 왕좌(이지)로 이동 한다. (레벨 115이상)#l\r\n"
        talk += "#L3#이동하지 않는다.#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("1인 이상의 파티에 속해야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        //} else if (cm.getPlayerCount(setting[st][2]) >= 1) {
		} else if (cm.getPlayerCount(dd) >= 1) {
            cm.sendOk("이미 누군가가 매그너스에 도전하고 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(bb, cc)) {
            c = 1;
            cm.sendOkS(cm.isBossString(bb), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(ee)) {
            c = 2;
            cm.sendNext("폭군의 왕좌(" + name[st] + " 모드)는 레벨 " + ee + "이상만 입장이 가능합니다.");
            cm.dispose();
            return;
        } else {
            cm.addBoss(bb);
            em = cm.getEventManager(bb);
            if (em != null) {
                cm.getEventManager(bb).startInstance_Party(dd + "", cm.getPlayer());
            }
            cm.dispose();
        }
    } else if (status == 2) {
        talk = "파티원 중 #b#e"
        if (c == 1) {
            for (i = 0; i < cm.BossNotAvailableChrList(bb, cc).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(bb, cc)[i] + ""
            }
        } else {
            for (i = 0; i < cm.LevelNotAvailableChrList(dd).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(ee)[i] + ""
            }
        }
        talk += "#k#n 님이 들어갈 수 있는 자격이 없습니다.";
        cm.sendNext(talk);
        cm.dispose();
    }
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	date2 = time.getDate();
	date = year * 10000 + month * 100 + date2;
	day = time.getDay();
	hour = time.getHours();
	minute = time.getMinutes();
	second = time.getSeconds();
}