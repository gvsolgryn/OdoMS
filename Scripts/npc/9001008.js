importPackage(Packages.tools.packet);
importPackage(Packages.database);
importPackage(Packages.client.inventory);

var enter = "\r\n";
var itemlist = [];

var S1 = -1, teamNum = -1;

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
		for(var h = 0; h<=9; h++){
			Item_Search(1149990+h);
		}
		var msg ="[GM]"+enter
		for(var a = 0; a<itemlist.length; a++){
			char_Search(itemlist[a][0], a);
			msg += "#L"+a+"#"+itemlist[a][2]+" "+(cm.World_boss_team_check(itemlist[a][0]) == null ? "없음" : cm.World_boss_team_check(itemlist[a][0]))+" #i"+itemlist[a][1]+"# #z"+itemlist[a][1]+"#"+enter
		}
		cm.sendSimple(msg);
	} else if(St == 1) {
		S1 = S;
		var msg =itemlist[S1][2]+"님에게 부여하실 팀을 선택해주세요."+enter
		msg += "#L0##r[Red Team] 부여하기#k"+enter
		msg += "#L1##b[Blue Team] 부여하기#k"+enter
		cm.sendSimple(msg);
	} else if(St == 2) {
		teamNum = S;
		if(cm.World_boss_team_check(itemlist[S1][0]) != null){
			if(teamNum == 0){
				cm.World_boss_team_update(teamNum, "Red", itemlist[S1][3], itemlist[S1][0], itemlist[S1][2])
			} else {
				cm.World_boss_team_update(teamNum, "Blue", itemlist[S1][3], itemlist[S1][0], itemlist[S1][2])
			}
		} else {
			if(teamNum == 0){
				cm.World_boss_team_insert(teamNum, "Red", itemlist[S1][3], itemlist[S1][0], itemlist[S1][2])
			} else {
				cm.World_boss_team_insert(teamNum, "Blue", itemlist[S1][3], itemlist[S1][0], itemlist[S1][2])
			}
		}
		cm.sendNext("설정완료");
	} else if(St == 3) {
		itemlist = [];
		start();
	}
}

function Item_Search(itemid) {
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT * FROM inventoryitems where itemid = ?");
	con.setInt(1, itemid);
	var rs = con.executeQuery();
	while(rs.next()) {
		itemlist.push([rs.getInt("characterid"), rs.getInt("itemid"), "이름 없음", 0]);
	}
	c.close();
	rs.close();
	con.close();
}

function char_Search(charid, code) {
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT * FROM characters where id = ?");
	con.setInt(1, charid);
	var rs = con.executeQuery();
	if(rs.next()) {
		itemlist[code][2] = rs.getString("name");
		itemlist[code][3] = rs.getInt("accountid");
	}
	c.close();
	rs.close();
	con.close();
}

function msg2(text){
	cm.getPlayer().dropMessage(5, text);
}