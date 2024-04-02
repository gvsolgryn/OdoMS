var seld = -1;
var enter = "\r\n";
var year, month, date2, date, day
var hour, minute;
var itemid = -1;
var allstat = -1;
var atk = -1;

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
		//if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		//	cm.sendOk("길드장만이 수로 입장을 신청하실 수 있습니다.");
		//	cm.dispose();
		if (cm.getPlayerCount(940711300) >= 1) {
			cm.sendOk("이미 누군가가 지하 수로에 입장했습니다.");
			cm.dispose();
		} else {
			cm.sendYesNo("지하수로 입장을 신청하시겠습니까? 현재 맵에 존재하는 모든 길드원이 이동됩니다.");
		}
	} else if (status == 1) {
        if (cm.getParty() == null) {
            cm.sendOk("1인 이상 파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(940711300) >= 1) {
            cm.sendOk("이미 누군가가 지하 수로에 입장했습니다.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만이 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getParty().getMembers().size() > 1) {
            cm.sendOk("1인만 신청 가능합니다.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable("GuildBoss", 1)) {
            cm.sendOkS(cm.isBossString("GuildBoss"), 0x04, 9010061);
            cm.dispose();
            return;
        } 
cm.getPlayer().guildscore = 0;
		cm.dispose();
		//if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		//	return;
		//if (cm.getPlayer().getGuild().getGuildScore() > 0) {
		//	cm.sendOk("이번주에는 이미 지하수로에 참여하셨습니다.");
		//} else {
	getData();
	time = new Date();
   	year = time.getFullYear() % 100;
   	month2 = time.getMonth() + 7;
   	month = time.getMonth() + 7 < 10 ?  "0"+month2 : month2;
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
  	date = year+"/"+month+"/"+date2;

	if (cm.getClient().getKeyValue("GuildBoss" + "_" + date) != null) {	 
           	cm.sendOk("#fs11#파티원 중 #r입장 조건#k을 충족하지 못하는 파티원이 있습니다.\r\n모든 파티원이 조건을 충족해야 입장이 가능합니다.\r\n\r\n");
            	cm.dispose();
	} else {
                                    cm.addBoss("GuildBoss");
	cm.getClient().setKeyValue("GuildBoss" + "_" + date, date);
			em = cm.getEventManager("GuildBoss");
			if (em != null) {
				cm.getEventManager("GuildBoss").startInstance_Party(940711300 + "", cm.getPlayer());
			}
//		    cm.warpGuild(940721000, 0);
		}
	}
}

function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0"+month;
    }
    date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
    date = year+""+month+""+date2;
    day = time.getDay();
    hour = time.getHours();
    minute = time.getMinutes();
}