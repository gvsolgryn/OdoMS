var enter = "\r\n";
var seld = -1;

var need = [
	{'itemid' : 4001878, 'qty' : 10},
	{'itemid' : 4001879, 'qty' : 1}
]
var tocoin = 4310218, toqty = 1;

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
		var msg = "#fs11#저희 시간의 신관들은 아케인리버 지역을 조사하고 있습니다. 혹시 에르다가 응집된 물방울석을 가지고 계신가요? 레헬른 지역의 몬스터들이 희귀하게 가지고 있다는 얘기를 들었습니다."+enter;

		for (i = 0; i < need.length; i++) {
			if (i != need.length - 1) msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개  ";
			else msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개" + enter +"를 주신다면 ";
		}
		msg += "조사에 큰 도움이 될겁니다. " + enter + enter + "대신 제가 가진 #b#t4310218##k을 드리겠습니다."+enter;
		if (haveNeed(1))
			cm.sendNext(msg);
		else {
			cm.sendOk(msg);
			cm.dispose();
		}
	} else if (status == 1) {
		temp = [];
		for (i = 0; i < need.length; i++) {
			temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
		}
		temp.sort();
		max = temp[0];
		cm.sendGetNumber("#b#z4310218##k을 몇 개나 교환 하시겠어요?\r\n(#z4001878# #c4001878#개 소지)\r\n(#z4001879# #c4001879#개 소지)", 1, 1, max);
	} else if (status == 2) {
		if (!haveNeed(sel)) {
			cm.sendOk("아이템이 모자란거 같네요.");
			cm.dispose();
			return;
		}
		for (i = 0; i < need.length; i++) {
			cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
		}
		cm.gainItem(tocoin, (toqty * sel));
		cm.dispose();
	}
}

function haveNeed(a) {
	var ret = true;
	for (i = 0; i < need.length; i++) {
		if (!cm.haveItem(need[i]['itemid'], (need[i]['qty'] * a)))
			ret = false;
	}
	return ret;
}
