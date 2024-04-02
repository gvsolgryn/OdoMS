importPackage(Packages.database);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.launch.world);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.packet);
importPackage(java.util);
importPackage(java.lang);

var status = -1;
var Question = "";
var Answer = "";
var itemid = 0;
var count = 0;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (cm.getPlayer().getGMLevel() >= 6) {
            var say = "#L1#문제 출제하기#l #L2#문제 풀기#l";
            cm.sendSimple(say);
        } else {
            var say = "#L2#문제 풀기#l";
            cm.sendSimple(say);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getGMLevel() >= 6 && selection == 1) {
            cm.sendGetText("출제하실 문제를 입력해 주세요.");    
        } else if (selection == 2) { 
            status = 6;
            try {
                var con = null;
                var ps = null;
                var con = null;
                var c = 0;
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `speedquiz`");
                rs = ps.executeQuery();
                while (rs.next()) {
                    Question = rs.getString("question");
                    Answer = rs.getString("answer");
                    itemid = rs.getInt("itemid");
                    count = rs.getInt("count");
                    c++;
                }
                rs.close();
                ps.close();
                con.close();
                if (c > 0) {
                    cm.sendGetText("문제 : " + Question + "\r\n보상 아이템 : #i" + itemid + "##z" + itemid + "# " + count + "개");
                } else {
                    cm.sendOk("아직 출제된 문제가 없습니다.");
                    cm.dispose();
                    return;
                }
            } catch (e) {
                cm.sendOk("오류가 발생하였습니다.\r\n\r\n" + e);
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
        } else {
            cm.dispose();
            return;
        }
    } else if (status == 2) {
        Question = cm.getText();
        cm.sendGetText("문제 : " + Question + "\r\n\r\n문제에 맞는 답을 입력해 주세요.");
    } else if (status == 3) {
        Answer = cm.getText();
        cm.sendGetNumber("문제 : " + Question + "\r\n정답 : " + Answer + "\r\n\r\n" + "보상으로 지급하실 아이템코드를 입력해주세요.", 0, 1000000, 5999999);
    } else if (status == 4) {
        itemid = selection;
        cm.sendGetNumber("문제 : " + Question + "\r\n정답 : " + Answer + "\r\n보상 아이템 : #i" + itemid + "##z" + itemid + "#\r\n" +
        "보상아이템을 지급할 갯수를 입력해주세요.", 0, 1, 30000);
    } else if (status == 5) {
        count = selection;
        cm.sendYesNo("정말로 이 문제를 출제하시겠습니까?\r\n\r\n" +
        "문제 : " + Question + "\r\n정답 : " + Answer + "\r\n보상 아이템 : #i" + itemid + "##z" + itemid + "# " + count + "개");
    } else if (status == 6) {
        try {
            var con = null;
            var ps = null;
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO `speedquiz` (`question`, `answer`, `itemid`, `count`) VALUES (?, ?, ?, ?)");
            ps.setString(1, Question);
            ps.setString(2, Answer);
            ps.setInt(3, itemid);
            ps.setInt(4, count);           
            ps.executeUpdate();
            cm.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "[문제] : " + Question));
            cm.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "[보상] : " + Packages.server.MapleItemInformationProvider.getInstance().getName(itemid) + " " + count + "개"));
            cm.sendOk("질문 등록이 완료되었습니다.\r\n\r\n" + "문제 : " + Question + "\r\n정답 : " + Answer + "\r\n보상 아이템 : #i" + itemid + "##z" + itemid + "# " + count + "개");
            cm.dispose();
            return;
        } catch (e) {
            cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n\r\n" + e);
            cm.dispose();
            return;
        } finally {
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
    } else if (status == 7) {
        if (Answer == cm.getText()) {
            try {
                var con = null;
                var ps = null;
                var rs = null;
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `speedquiz`");
                rs = ps.executeQuery();
                if (rs.next()) {
                    rs.close();
                    ps.close();
                    ps = con.prepareStatement("DELETE FROM `speedquiz`");         
                    ps.executeUpdate();
                    ps.close();
                    con.close();
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "[정답] : " + Answer + ", " + cm.getPlayer().getName() + "님이 맞추셨습니다."));
                    cm.gainItem(itemid, count);
                    cm.sendOk("정답입니다.");
                    cm.dispose();
                    return;
                } else {
                    rs.close();
                    ps.close();
                    con.close();
                    cm.sendOk("이미 다른 유저가 문제를 맞추었습니다.");
                    cm.dispose();
                    return;
                }
            } catch (e) {
                cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n\r\n" + e);
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
        } else {
            cm.sendOk("오답입니다.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}    