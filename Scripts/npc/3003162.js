importPackage(Packages.server);
importPackage(java.lang);

var quest = "츄츄";
var 제한레벨 = 210;
var enter = "\r\n";
var seld = -1;
var isitem = false;
var isclear;
var year, month, date2, date, day
var rand;

var daily = [ // 첫번째
{'mobid' : 8642000, 'qty' : 300, 't' : 1}, 
{'mobid' : 8642006, 'qty' : 300, 't' : 1},
{'mobid' : 8642004, 'qty' : 300, 't' : 1},
{'mobid' : 8642008, 'qty' : 300, 't' : 1},
{'mobid' : 8642012, 'qty' : 300, 't' : 1},
{'mobid' : 8642015, 'qty' : 300, 't' : 1}, 
{'mobid' : 8642012, 'qty' : 300, 't' : 1}
]

var daily2  = [ //두번째
	{'mobid' : 8642000, 'qty' : 300, 't' : 1}, 
{'mobid' : 8642006, 'qty' : 300, 't' : 1},
{'mobid' : 8642004, 'qty' : 300, 't' : 1},
{'mobid' : 8642008, 'qty' : 300, 't' : 1},
{'mobid' : 8642012, 'qty' : 300, 't' : 1},
{'mobid' : 8642015, 'qty' : 300, 't' : 1}, 
{'mobid' : 8642012, 'qty' : 300, 't' : 1}
]

var reward;



function start() {
	status = -1;
	action(1, 0, 0);
}

function getk(key) {
	return cm.getPlayer().getKeyValue(201801, date+"_"+quest+"_"+key);
}
function setk(key, value) {
	cm.getPlayer().setKeyValue(201801, date+"_"+quest+"_"+key, value);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		if (cm.getPlayer().getLevel() < 제한레벨) {
			cm.sendOk("너는 아직 임무를 받기엔 너무 나약하군~");
			cm.dispose();
			return;
		}
		getData();
		switch(day){
    			case 0:
        			var d = "일";
        			reward = [
			{'itemid' : 1712002, 'qty' : 20},
			{'itemid' : 2437760, 'qty' : 2},
			{'itemid' : 2435719, 'qty' : 20}
        			
				]
        		break;
    			case 1:
        			var d = "월";
        			reward = [
			{'itemid' : 1712002, 'qty' : 10},
			{'itemid' : 2437760, 'qty' : 1},
			{'itemid' : 2435719, 'qty' : 10}
				]
        			break;
    			case 2:
        			var d = "화";
        			reward = [
			{'itemid' : 1712002, 'qty' : 10},
			{'itemid' : 2437760, 'qty' : 1},
			{'itemid' : 2435719, 'qty' : 10}
				]
        		break;
    			case 3:
        			var d = "수";
        			reward = [
			{'itemid' : 1712002, 'qty' : 10},
			{'itemid' : 2437760, 'qty' : 1},
			{'itemid' : 2435719, 'qty' : 10}
				]
        		break;
    			case 4:
        			var d = "목";
        			reward = [
			{'itemid' : 1712002, 'qty' : 10},
			{'itemid' : 2437760, 'qty' : 1},
			{'itemid' : 2435719, 'qty' : 10}
				]
        		break;
    			case 5:
        			var d = "금";
        			reward = [
			{'itemid' : 1712002, 'qty' : 10},
			{'itemid' : 2437760, 'qty' : 1},
			{'itemid' : 2435719, 'qty' : 10}
				]
        		break;
    			case 6:
        			var d = "토";
        			reward = [
			{'itemid' : 1712002, 'qty' : 20},
			{'itemid' : 2437760, 'qty' : 2},
			{'itemid' : 2435719, 'qty' : 20}
				]
        		break;
		}

		rand = daily[Randomizer.rand(0, daily.length-1)];
		isitem = rand['t'] == 2 ? true : false;
		isclear = getk("isclear");
		if (isclear == -1) {
			setk("mobid", isitem ? rand['itemid']+"" : rand['mobid']+"");
			setk("mobq", rand['qty']+"");
			setk("isclear", "1");
			setk("isitem", isitem ? "2" : "1");
			setk("count", "0");
		}
		if (isclear == 3) {
			rand = daily2[Randomizer.rand(0, daily2.length-1)];
			isitem = rand['t'] == 2 ? true : false;
			setk("mobid", isitem ? rand['itemid']+"" : rand['mobid']+"");
			setk("mobq", rand['qty']+"");
			setk("isitem", isitem ? "2" : "1");
			setk("count", "0");
			setk("isclear", "4");
		}
		isclear = getk("isclear");
		isitem = getk("isitem") == 2 ? true : false;

		if (isclear == 2 || isclear == 5) {
			if ((isitem && (cm.haveItem(getk("mobid"), getk("mobq")))) || (!isitem && (getk("count") >= getk("mobq")))) {
				if (isitem) cm.gainItem(getk("mobid"), -getk("mobq"));
				var msg = "#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n#b"+enter;
				if (isclear == 5) {
					msg += "#k뭐 이 정도면 나쁘지 않군 이건 보상이야. 받든가 말든가~"+enter;
					msg += getReward()+enter;
				}
				msg += "#k"+enter;

				if (isclear == 2)
					msg += "생각보다 쓸만한 녀석이군.. 기왕 이렇게 된 김에 다른 녀석들도 처리해주면 보상을 주지 원한다면 다시 말을 걸어줘.";
				else 
					msg += "";

				if (isclear == 2)
					setk("isclear", "3");
				else if (isclear == 5)
					setk("isclear", "6");

				cm.sendOk(msg);
				cm.dispose();
			}
		}

		if (isclear == 5) {
			if ((isitem && (cm.haveItem(getk("mobid"), getk("mobq")))) || (!isitem && (getk("count") >= getk("mobq")))) {
				if (isitem) cm.gainItem(getk("mobid"), -getk("mobq"));
				setk("isclear", "6");
				var msg = "#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n"+enter;
				msg += "#k뭐 이 정도면 나쁘지 않군 이건 보상이야. 받든가 말든가~"+enter;
				msg += getReward()+enter;
				//msg += "#k내일 또 찾아줘"+enter;
				msg += "내 일을 하나 더 처리해줘 안그러면 화날 거 같거든..?";
				cm.sendOk(msg);
				cm.dispose();
			}
		}
		switch (isclear) {
			case 1:
				var msg = "너는 또 뭐야?! 또 멍청한 것 하나가 늘었나 보군!"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+제한레벨+") #b#e[일일 퀘스트]#n#k #d츄츄 아일랜드 : 몬스터처치";

				cm.sendSimple(msg);
			break;
			case 2:
				var msg = "아래 애들을 처치해줘 정말 골치 아픈 애들이거든~"+enter+enter;
				msg += "#r#e[임무 : 몬스터처치]#n#k"+enter;
				msg += "처치 대상 : #b#o"+getk("mobid")+"# "+getk("mobq")+"마리#k"+enter+enter;
				msg += "궁시렁 거리지 말고 얼른 다녀와";

				cm.sendOk(msg);
				cm.dispose();
			break;
			case 4:
				var msg = "너는 또 뭐야?! 또 멍청한 것 하나가 늘었나 보군!"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+제한레벨+") #b#e[일일 퀘스트]#n#k #d츄츄 아일랜드 : 몬스터처치2";

				cm.sendSimple(msg);
			break;
			case 5:
				var msg = "아래 애들을 처치해줘 정말 골치 아픈 애들이거든~"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[츄츄 아일랜드 : 몬스터처치]#n#k"+enter;
				msg += "처치 대상 : #b#o"+getk("mobid")+"# "+getk("mobq")+"마리#k"+enter+enter;
				msg += "궁시렁 거리지 말고 얼른 다녀와";

				cm.sendOk(msg);
				cm.dispose();
			break;
			case 6:
				var msg = "오늘은 더 이상 시킬게 없다구~"+enter;
				msg += ""+enter;

				cm.sendOk(msg);
				cm.dispose();
			break;
		}
	} else if (status == 1) {
		if (getk("isclear") == 1 || getk("isclear") == 4) {
			var msg = "아래 애들을 처치해줘 정말 골치 아픈 애들이거든~"+enter+enter;
			msg += isitem ? "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n\r\n#r#e[임무 : 몬스터처치]#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n\r\n#r#e[임무 : 몬스터처치]#n#k"+enter;
			msg += isitem ? "\r\n목표 전리품 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"개#k"+enter+enter : "처치 대상 : #b#o"+getk("mobid")+"# "+getk("mobq")+"마리#k"+enter+enter;
			msg += "임무를 수락하시겠습니까?";

			cm.sendYesNo(msg);
		}
	} else if (status == 2) {
		var msg = ""+enter+enter;
		msg += isitem ? "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n\r\n#r#e[임무 : 몬스터처치]#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n\r\n#r#e[임무 : 몬스터처치]#n#k"+enter;
		msg += isitem ? "\r\n목표 전리품 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"개#k"+enter+enter : "처치 대상 : #b#o"+getk("mobid")+"# "+getk("mobq")+"마리#k"+enter+enter;
		msg += "궁시렁 거리지 말고 얼른 다녀와";
		cm.sendOk(msg);
		cm.dispose();
		if (getk("isclear") == 1)
			setk("isclear", "2");
		else if (getk("isclear") == 4)
			setk("isclear", "5");
	}
}

function getReward() {
	var msg = "#b";
	//var rand1 = reward[Randomizer.rand(0, reward.length-1)];
	//var rand2 = reward[Randomizer.rand(0, reward.length-1)];
	for (i = 0; i < reward.length; i++) {
		cm.gainItem(reward[i]['itemid'], reward[i]['qty']);
		cm.getPlayer().AddStarDustPoint(20, cm.getPlayer().getTruePosition());
		msg += "#i"+reward[i]['itemid']+"##z"+reward[i]['itemid']+"# "+reward[i]['qty']+"개"+enter;
	}
	//cm.gainItem(rand1['itemid'], rand1['qty']);
	//cm.Item(rand2['itemid'], rand2['qty']);
	//msg += "#i"+rand1['itemid']+"##z"+rand1['itemid']+"# "+rand1['qty']+"개"+enter;
	//msg += "#i"+rand2['itemid']+"##z"+rand2['itemid']+"# "+rand2['qty']+"개"+enter;
	return msg;
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	date = Integer.parseInt(year+""+month+""+date2);
	day = time.getDay();
}
