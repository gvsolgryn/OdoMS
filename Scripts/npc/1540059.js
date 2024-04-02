importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;

var connect = null;
var prepared = null;
var getsafe = null;

var item = 0;
var amount = 0;
var id = 0;
var code = 0;
var unlocker = null;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
        cm.dispose();
    }
    if (status == 0) {
        var date = new Date();
        var tyear = date.getFullYear();
        var tmonth = new String(date.getMonth() + 1);
        var tday = new String(date.getDate());
        var c = 0;

        var connect = DatabaseConnection.getConnection();
        var prepared = connect.prepareStatement("SELECT * FROM safe");
        var getsafe = prepared.executeQuery();
        try {
            while (getsafe.next()) {
                var 년 = getsafe.getTimestamp("date").getYear() + 1900;
                var 월 = getsafe.getTimestamp("date").getMonth() + 1;
                var 일 = getsafe.getTimestamp("date").getDate();
                if (년 == tyear) {
                    if (월 == tmonth) {
                        if (일 == tday) {
                            c++;
                            id = getsafe.getInt("id");
                            code = getsafe.getInt("count");
                            item = getsafe.getInt("item");
                            amount = getsafe.getInt("amount");
                            if (getsafe.getString("unlocker") != null) {
                                unlocker = getsafe.getString("unlocker");
                            }
                        }
                    }
                }
            }
            connect.close();
            prepared.close();
            getsafe.close();
        } catch (e) {

        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
                if (prepared != null) {
                    prepared.close();
                }
                if (getsafe != null) {
                    getsafe.close();
                }
            } catch (e) {

            }
        }

        var iron = "";
        if (mode == 0) {
            cm.dispose();
        } else {
            iron += "안녕하세요 #b#h ##k님  #b#i4009239##z4009239##k를 저에게 가져다 주시면 금고를 열 기회를 드립니다.\r\n\r\n#e오늘의 아이템:#n #i" + item + "# #z" + item + "# " + amount + "개\r\n\r\n#Cgray#금고는 매일 초기화 되며 유저분들 중 한명이라도 금고를 열 시 그 날 이벤트는 종료됩니다.\r\n\r\n골드리치 피규어는 모든맵을 통해 획득가능합니다.#k\r\n";
            if (c == 0) {
                iron += "#r#e오늘은 금고이벤트가 없습니다.#n#k";
            } else {
                if (unlocker == null) {
                    iron += "#b#L0#골드리치 피규어를 가져왔습니다.#l"
                } else {
                    iron += "\r\n#b오늘의 금고는 이미 #e" + unlocker + "#n님이 열었습니다.#k";
                }
            }
            iron += "\r\n#L10##b역대 금고 개봉자가 궁금합니다.#n#l";
            cm.sendSimple(iron);
        }

    } else if (status == 1) {
        if (selection == 0) {
            if (cm.haveItem(4009239, 1)) {
                cm.sendGetNumber("1~1000사이의 숫자를 입력해주세요\r\nex) 정답이 23일 경우 23 #b(O)#k 023 #r(X)#k", 0, 1, 1000);
            } else {
                cm.sendOk("금고를 열기 위해선 #b#i4009239##z4009239##k가 필요합니다.");
                getsafe.close();
                cm.dispose();
            }
        } else if (selection == 10) {
            var list = "#fs14##e금고 개봉자들 입니다.#n#fs12#\r\n\r\n";
            var connect = DatabaseConnection.getConnection();
            var prepared = connect.prepareStatement("SELECT * FROM safe");
            var getsafe = prepared.executeQuery();
            try {
                while (getsafe.next()) {
                    list += "[#d" + [getsafe.getTimestamp("date").getMonth() + 1] + "월 " + getsafe.getTimestamp("date").getDate() + "일#k] #b" + getsafe.getString("unlocker") + "#k님이 #b#z" + getsafe.getInt("item") + "##k " + getsafe.getInt("amount") + "개 획득\r\n";
                }
                connect.close();
                prepared.close();
                getsafe.close();
            } catch (e) {

            } finally {
                try {
                    if (connect != null) {
                        connect.close();
                    }
                    if (prepared != null) {
                        prepared.close();
                    }
                    if (getsafe != null) {
                        getsafe.close();
                    }
                } catch (e) {

                }
            }
            cm.sendOk(list);
            cm.dispose();
        }

    } else if (status == 2) {
        if (selection == code) {
            unlocker = cm.getPlayer().getName();
            var connect = DatabaseConnection.getConnection();
            var prepared = connect.prepareStatement("UPDATE safe SET `unlocker`='" + unlocker + "' WHERE `id`=" + id + "");
            try {
                prepared.executeUpdate();
                cm.gainItem(4009239, -1);
                cm.gainItem(item, amount);
                World.Broadcast.broadcastSmega(CField.getGameMessage(12, "[금고] " + unlocker + "님이 금고를 열어 오늘의 금고 이벤트를 종료합니다."));
                cm.sendOk("금고를 열어서  #i" + item + "##b#z" + item + "##k을(를) 획득하셨습니다.");
                connect.close();
                prepared.close();
            } catch (e) {

            } finally {
                try {
                    if (connect != null) {
                        connect.close();
                    }
                    if (prepared != null) {
                        prepared.close();
                    }
                } catch (e) {

                }
            }
        } else {
            cm.getPlayer().dropMessage(1, "비밀번호를 틀리셨습니다.");
            cm.gainItem(4009239, -1);
        }
        cm.dispose();
    }
}