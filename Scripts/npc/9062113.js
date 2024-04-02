importPackage(Packages.database);

function start() {
	St = -1;
	action(1, 0, 0);
}

var wdr_list = [];
var wdr_list2 = [];

var enter = "\r\n"

var reward = [
    [4310321, 20],
    [4310321, 10],
]

var Time = new Date();
var Year = Time.getFullYear() + "";
var Month = Time.getMonth() + 1 + "";
var Date = Time.getDate() + "";
if (Month < 10) {
    Month = "0" + Month;    
}
if (Date < 10) {
    Date = "0" + Date;    
}
var Today = parseInt(Year + Month + Date);

var outmap = 120043000; // 아웃

var st1 = false;
var st2 = false;

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
        var ct = 0;
        world_dmg_rank();
        world_dmg_rank2(Today);
        var charid = cm.getPlayer().getId();
		var w_damage = cm.getPlayer().WorldbossDamage;
        var msg = "나의 포인트 : "+(w_damage == 0 ? "0" : ConvertNumber(w_damage))+enter+enter
        msg += "<순위>"+enter+enter
		for(var i = 0; i<wdr_list.length; i++){
            msg += "#e#fs14##fc0xFFFF8224#"
            msg += (i+1)+"등 "+wdr_list[i][1]+enter 
            msg += "   ㄴ"
            msg += "포인트 : "+ConvertNumber(wdr_list[i][3])+enter 
        }
        msg +="#n#k#fs#"
        
        for(var a = 0; a<wdr_list2.length; a++){
            //msg2(wdr_list[0][0]+"/"+wdr_list2[a][0]+"/"+wdr_list2[a][2]+"/"+charid)
            if(wdr_list[0][0] == wdr_list2[a][0] && wdr_list2[a][2] == charid){
                st1 = true;
            } else {
                st2 = true;
            }
        }
        if(st1 == true){
            msg += "#b#L0# 1순위 보상을 받아간다.#k"
        } else if(st2 = true) {
            msg += "#b#L1# 2순위 보상 을 받아간다.#k"
        }
        if(ct == 0 || w_damage == 0){
            msg += "#b#L4# 이곳에서 나간다.#k"
        }
        cm.sendSimple(msg);
    } else if(St == 1) {
        cm.gainItem(reward[S][0], reward[S][1]);
        cm.warp(outmap);
        var msg = "#fUI/UIWindow2.img/QuestIcon/4/0#"+enter
        msg += "#i"+reward[S][0]+"# #z"+reward[S][0]+"# "+reward[S][1]+"개"
        cm.sendOk(msg);
        cm.dispose();
	}
}


function world_dmg_rank(){
    var con = null;
    var ps = null;
    var rs = null;
    var ct = 1;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("SELECT teamid, teamname, characterid, sum(damage) as dmg_sum FROM `world_boss_damage` where date = ? GROUP BY teamid ORDER BY dmg_sum DESC");
        ps.setInt(1, Today);
        rs = ps.executeQuery();
        while (rs.next()) {
            wdr_list.push([rs.getInt("teamid"), rs.getString("teamname"), rs.getInt("characterid"), rs.getLong("dmg_sum")])
            ct++
        }
        rs.close();
        ps.close();
        con.close();
    } catch (e) {
        cm.sendOk("아직 등록된 기록이 없거나 오류가 발생하였습니다.\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (rs != null) {
            try {
                rs.close();
            } catch (e) {

            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (e) {

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (e) {

            }
        }
    }
}

function world_dmg_rank2(Today){
    var con = null;
    var ps = null;
    var rs = null;
    var ct = 0;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("SELECT * FROM `world_boss_damage` where date = ?");
        ps.setInt(1, Today);
        rs = ps.executeQuery();
        while (rs.next()) {
            wdr_list2.push([rs.getInt("teamid"), rs.getString("teamname"), rs.getInt("characterid")])
        }
        rs.close();
        ps.close();
        con.close();
    } catch (e) {
        cm.sendOk("아직 등록된 기록이 없거나 오류가 발생하였습니다.\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (rs != null) {
            try {
                rs.close();
            } catch (e) {

            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (e) {

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (e) {

            }
        }
    }
}

function ConvertNumber(number) {
    var inputNumber  = number < 0 ? false : number;
    var unitWords    = ['', '만 ', '억 ', '조 ', '경 '];
    var splitUnit    = 10000;
    var splitCount   = unitWords.length;
    var resultArray  = [];
    var resultString = '';
    if (inputNumber == false) {
        resultString = '0';
        return;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0){
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if(!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}

function msg2(text){
	cm.getPlayer().dropMessage(5, text);
}