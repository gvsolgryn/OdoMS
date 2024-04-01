importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.life);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world)

하트 = "#fUI/UIMiniGame.img/starPlanetRPS/heart#";
검정 = "#fc0xFF191919#"
블루 = "#fc0xFF4641D9#"

// 추천받은 사람 보상 x 추천수
추천인보상템 = 2430042

// 추천한 신입유저 일괄 보상
신입보상템 = 2430041;
신입보상템개수 = 1;

status = -1;

/* 추천인 등록 체크 */
function overlab_recom(name, name2) {
	c = DatabaseConnection.getConnection();
	con = c.prepareStatement("SELECT * FROM recom_log WHERE name LIKE '" + name + "%'").executeQuery();

	overlab = true;
	if (!con.next()) overlab = false;

	con.close();
	//c.close();
	return overlab;
}

function getAccIdFromDB(name) {
	c = DatabaseConnection.getConnection();
	con = c.prepareStatement("SELECT * FROM characters WHERE name LIKE '" + name + "%'").executeQuery();
	ret = -1;
	if (con.next()) {
		ret = con.getInt("accountid");
	}
	con.close();
	//c.close();
	return ret;
}

function join_recom(name, name2, recom) {
	con = DatabaseConnection.getConnection();
	insert = con.prepareStatement("INSERT INTO recom_log(name, recom, state, date) VALUES(?,?,?,now())");
	insert.setString(1, name + "%" + name2);
	insert.setString(2, recom);
	insert.setString(3, 0);
	insert.executeUpdate();
	insert.close();
	//con.close();
}

function recom_log() {
	txt = new StringBuilder();
	c = DatabaseConnection.getConnection();
	con = c.prepareStatement("SELECT id, recom, count(*) AS player FROM recom_log GROUP BY recom ORDER BY player DESC").executeQuery();
	rank = 0;
	while (con.next()) {
		txt.append("#L" + con.getInt("id") + "#")
			.append(rank == 0 ? "#fUI/UIWindow2.img/ProductionSkill/productPage/meister# "
				: rank == 1 ? "#fUI/UIWindow2.img/ProductionSkill/productPage/craftman# "
					: "#fUI/UIWindow2.img/ProductionSkill/productPage/hidden# ")

			.append("추천인 코드 : ").append(con.getString("recom")).append(" | ")
			.append("추천 수 : ").append(con.getString("player")).append("\r\n");
		rank++;
	}
	con.close();
	//c.close();
	return txt.toString();
}

function recom_list(id) {
	txt = new StringBuilder();
	c = DatabaseConnection.getConnection();
	idcon = c.prepareStatement("SELECT * FROM recom_log WHERE id = '" + id + "'").executeQuery();
	idcon.next(), recom_per = idcon.getString("recom");

	con = c.prepareStatement("SELECT * FROM recom_log WHERE recom = '" + recom_per + "'").executeQuery();
	txt.append("#fs11#" + recom_per + "#k 님을 추천하신 플레이어들 입니다.\r\n\r\n");
	while (con.next()) {
		var con_name = con.getString("name").split("%");
		txt.append("닉네임 : ").append(con_name[1]).append(" | ")
			.append("날짜 : ").append(con.getDate("date") + " " + con.getTime("date")).append("\r\n");
	}
	con.close();
	//c.close();
	return txt.toString();
}

function recom_num(name) {
	c = DatabaseConnection.getConnection();
	con = c.prepareStatement("SELECT COUNT(*) AS player FROM recom_log WHERE recom = '" + name + "' and state = 0").executeQuery();
	con.next();
	recoms_num = con.getString("player");
	con.close();
	//c.close();
}

function recom_person(name) {
	txt = new StringBuilder();
	c = DatabaseConnection.getConnection();
	con = c.prepareStatement("SELECT * FROM recom_log WHERE recom = '" + name + "' and state = 0").executeQuery();

	while (con.next()) {
		var con_name = con.getString("name").split("%");
		txt.append("[" + con_name[1] + "] ");
	}
	con.close();
	//c.close();
	return txt.toString();
}

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}

	if (status == 0) {
		if (cm.getPlayer().getLevel() < 100) {
			cm.sendOkS("#fs11#" + 검정 + "어느 정도 닉스 서버를 즐긴 뒤, 이용이 가능하다네.", 0x04, 9401232);
			cm.dispose();
			return;
		}
		var text = "#fs11#" + 검정 + "어서 오게! 자네도 " + 블루 + "닉스 세계#k" + 검정 + "에 초대받았나 보군! ";
		text += "초대받은 사람은 누구나 환영한다네! ";
		text += "여기 페스티벌에서 좋은 경험과 추억을 쌓았으면 하네! 크크.\r\n\r\n";
		text += "#fUI/UIWindow.img/UtilDlgEx/list1#\r\n";
		text += "#L0##b추천인#k" + 검정 + "을 등록하겠습니다.#l\r\n";
		text += "#L1##b추천인#k" + 검정 + " 랭킹을 확인하겠습니다.#l\r\n\r\n\r\n";
		text += "#fUI/UIWindow.img/UtilDlgEx/list0#\r\n";
		text += "#L2#" + 검정 + "내가 받은 #b추천#k" + 검정 + "을 확인하고 싶습니다.\r\n";
		cm.sendSimpleS(text, 0x04, 9401232);
	} else if (status == 1) {
		if (selection == 0) {
			if (!overlab_recom(cm.getClient().getAccID(), cm.getPlayer().getName())) {
				cm.sendGetText("#b#fs11#" + cm.getPlayer().getName() + "#k"+검정+"! 자네를 #b닉스 세계#k"+검정+"에 초대해준 사람의 #r닉네임#k"+검정+"을 적어보게! 초대해준 대상이 따로 없다면 #r'슈피겔만'#k"+검정+"을 적으면 된다네.\r\n추천인 등록은 #r'1회'#k"+검정+" 만 가능하니 신중하게 적어보게!", 9401232);

			} else {
				cm.sendOkS("#fs11#"+검정+"추천 등록은 한번만 가능하네.", 0x04, 9401232);
				cm.dispose();
			}
		} else if (selection == 1) {
			cm.sendSimpleS("#fs11#"+검정+"여긴 닉스의 세계를 알려준 용사들의 이름이지.\r\n자네도 닉스의 세계를 널리 퍼트려주게!\r\n" + recom_log(), 0x04, 9401232);
			status = 2;

		} else if (selection == 2) {
			recom_num(cm.getPlayer().getName());
			if (recoms_num == 0) cm.sendOkS("#fs11#"+검정+"아직 자네를 추천한 사람은 없는거 같네. 하지만 실망하지 말게, 앞으로 열심히 한다면 추천 받을지도 모르네! 크크.", 0x04, 9401232), cm.dispose();
			else {
				cm.sendOkS("" + cm.getPlayer().getName() + "#k 님은 " + recoms_num + "명 [ " + recom_person(cm.getPlayer().getName()) + " ] 의 추천을 받으셨습니다.\r\n감사의 의미로 #i" + 추천인보상템 + "# #z" + 추천인보상템 + "##k " + recoms_num + " 개를 지급해드렸습니다!", 0x04, 9401232);
				cm.gainItem(추천인보상템, (1 * recoms_num));
				recom_num(0);
				c = DatabaseConnection.getConnection();
				c.prepareStatement("UPDATE recom_log SET state = 1 WHERE recom = '" + cm.getPlayer().getName() + "'").executeUpdate();
				//c.close();
				cm.dispose();
			}
		}
	} else if (status == 2) {
		if (cm.getText().equals("") || cm.getText().equals(cm.getPlayer().getName()) || getAccIdFromDB(cm.getText()) == getAccIdFromDB(cm.getPlayer().getName())) {
			cm.sendOkS(cm.getText().equals("") ? "#fs11#"+검정+"입력을 잘못한거 같네." : "#fs11#"+검정+"초대한 상대의 이름을 적어야 한다네.", 0x04, 9401232);
			cm.dispose();
		} else {
			join_recom(cm.getClient().getAccID(), cm.getPlayer().getName(), cm.getText());
			cm.gainItem(신입보상템, 신입보상템개수);
			World.Broadcast.broadcastMessage(CField.getGameMessage(25, "[닉스] " + cm.getPlayer().getName() + " 님이 " + cm.getText() + " 님을 추천인으로 등록하셨습니다!"));
			//World.Broadcast.broadcastMessage(CWvsContext.serverNotice(11, "[알림] " + cm.getPlayer().getName() + " 님이 " + cm.getText() + " 님을 추천인으로 등록하셨습니다."));
			//cm.sendOkS("#fs11#이건 " + cm.getPlayer().getName() + "님에게 드리는 저의 작은 선물입니다. 앞으로의 여행에 큰 도움이 될 거예요.", 0x04, 9401232);
			말 = "#fs11#"+검정+"자네를 위해 조그마한 선물을 준비해봤다네. 마음에 들진 모르겠지만 앞으로 자네의 세상을 보여주게.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + 신입보상템 + "# #b#z" + 신입보상템 + "##k"
            cm.sendOkS(말, 0x04, 9401232);
			cm.dispose();
		}
	} else if (status == 3) {
		cm.sendOk(recom_list(selection));
		cm.dispose();
	}
}
