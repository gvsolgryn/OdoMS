importPackage(Packages.tools.packet);
importPackage(Packages.database);
importPackage(Packages.client.inventory);

var enter = "\r\n";

var mapid = 993059200
var outmap = 100000000

var in_hour = 19;
var in_minute = 58;

var in_hour2 = 22;
var in_minute2 = 58;

var accid, charid, charname;
var red_icon = "#fUI/UIWindow2.img/MonsterCarnival/icon0#"
var blue_icon = "#fUI/UIWindow2.img/MonsterCarnival/icon1#"


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
		getData();
		charid = cm.getPlayer().getId();
		accid = cm.getPlayer().getClient().getAccID();
		charname = cm.getPlayer().getName();
		if(hour != in_hour && hour != in_hour2){
			cm.sendOk("#r지금은 입장시간이 아니므로 입장이 불가능합니다.#k ");
			cm.dispose();
			return;
		}
		
		var msg = ""
		if(cm.World_boss_team_check(charid) != null){
			if((hour == in_hour && minute > in_minute) || (hour == in_hour2 && minute > in_minute2)){
				cm.sendOk("#r지금은 입장시간이 아니므로 입장이 불가능합니다.#k ");
				cm.dispose();
				return;
			}
			msg+= "<월드 보스>"+enter+enter
			msg+= "나의 팀 : "+(cm.World_boss_team_check(charid).equals("Red") ? red_icon+" #rRed Team#k" : blue_icon+" #bBlue Team#k")
			msg+= enter+enter
			msg+= "월 ~ 목 : 8시 || 금, 토, 일 : 8시 / 11시"+enter;
			msg+= "매일 : "+in_hour+"시 00분 ~ "+in_hour+"시 "+in_minute+"분 까지 입장가능"+enter;
			msg+= "매일 : "+in_hour2+"시 00분 ~ "+in_hour2+"시 "+in_minute2+"분 까지 입장가능"+enter;
            if((hour == in_hour && minute < in_minute) && (day == 1 || day == 2 || day == 3 || day == 4)){
                msg+= "#L0# 입장하기"+enter;
            } else if((hour == in_hour && minute < in_minute) || (hour == in_hour2 && minute < in_minute2) && (day == 5 || day == 0 || day == 6)){
                msg+= "#L0# 입장하기"+enter;
            } else {
                cm.sendOk(msg);
                cm.dispose();
                return;
            }
			
			if(checkItem() != 1){
				if(day == 4){
					msg += "#L3##r[Red  Team] 선택하기#k"+enter;
					msg += "#L4##b[Blue Team] 선택하기#k"
				}
			}
		} else {
			if(checkItem() == 1){
				cm.sendOk("곧 팀이 정해질 예정입니다 잠시만 기다려 주십쇼.");
				cm.dispose();
				return;
			} else {
				msg += "팀 선택하기"+enter;
				msg += "#L1##r[Red  Team] 선택하기#k"+enter;
				msg += "#L2##b[Blue Team] 선택하기#k"
			}
		}
		cm.sendSimple(msg);
	} else if(St == 1) {
		if(S == 0){
			if (cm.getPlayer().getParty() != null) {
				cm.sendOk("파티로는 입장불가");
				cm.dispose();
				return;
			}
			if((hour == in_hour && minute > in_minute) || (hour == in_hour2 && minute > in_minute2)){
				cm.sendOk("#r지금은 입장시간이 아니므로 입장이 불가능합니다.#k ");
				cm.dispose();
				return;
			}
			for(var i = 1; i<=10; i++){
                if(cm.getClient().getChannel() == 10 && cm.getPlayerCount(mapid) >= 5){
                    cm.sendOk("정원이 초과되어 입장이 불가능합니다.");
                    cm.dispose();
                    return
                }
                if(cm.getClient().getChannel() == i && cm.getPlayerCount(mapid) >= 5){
                    cm.getPlayer().changeChannelMap((i+1), mapid);
                    break;
                } else {
                    cm.warp(mapid);
                    break;
                }
            }
			cm.dispose();
		} else if(S == 1){
			cm.World_boss_team_insert(1, "Red", accid, charid, charname);
			cm.sendOk("#rRed Team#k으로 지정되셨습니다.");
			cm.dispose();
		} else if(S == 2){
			cm.World_boss_team_insert(2, "Blue", accid, charid, charname);
			cm.sendOk("#bBlue Team#k으로 지정되셨습니다.");
			cm.dispose();
		} else if(S == 3){
			cm.World_boss_team_update(1, "Red", accid, charid, charname);
			cm.sendOk("#rRed Team#k으로 지정되셨습니다.");
			cm.dispose();
		} else if(S == 4){
			cm.World_boss_team_update(2, "Blue", accid, charid, charname);
			cm.sendOk("#bBlue Team#k으로 지정되셨습니다.");
			cm.dispose();
		}
	}
}

function getData() {
	time = new Date();
	hour = time.getHours();
	minute = time.getMinutes();
	seconds = time.getSeconds();
	day = time.getDay();
}

function checkItem(){
	var count = 0;
	var ii = Packages.server.MapleItemInformationProvider.getInstance();
	var itemLock = cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(-11);
	for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
		if (cm.getInventory(1).getItem(i) != null && cm.getInventory(1).getItem(i).getItemId() != 1149990 && cm.getInventory(1).getItem(i).getItemId() != 1149991 && cm.getInventory(1).getItem(i).getItemId() != 1149992) {
			if (ii.getName(cm.getInventory(1).getItem(i).getItemId()).contains("HEINZ : ") == true) {
				count++;
			}
		}
	}
	if(itemLock != null){
		if(itemLock.getItemId() != 1149990 && itemLock.getItemId() != 1149991 && itemLock.getItemId() != 1149992){
			if(ii.getName(itemLock.getItemId()).contains("HEINZ : ") == true){
				count++;
			}
		}
	}
	return count;
}

function msg2(text){
	cm.getPlayer().dropMessage(5, text);
}