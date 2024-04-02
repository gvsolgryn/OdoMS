importPackage(Packages.database);
importPackage(java.lang);

var items = [ // 목록 일련번호, 구입할 아이템, 계정당 구입 제한 갯수, 재료코드, 구입할 아이템 1개당 요구 재료 갯수
    [1, 2630130, 1, 4310261, 1],
    [2, 2049370, 5, 4310261, 100],
    [3, 2049372, 5, 4310261, 250],
    [4, 2049371, 5, 4310261, 750],
    [5, 2049376, 3, 4310261, 2500],
    [6, 2023337, 2, 4310261, 200],
    [7, 2023338, 2, 4310261, 500],
    [8, 2023339, 2, 4310261, 1500],
    [9, 2023340, 1, 4310261, 5000],
    [10, 2049360, 100, 4310261, 300],
    [11, 5060048, 10, 4310261, 300],
    [12, 5068300, 10, 4310261, 300],
    [13, 4310269, 20, 4310261, 300],
    [14, 2435932, 2, 4310261, 5000],
    [15, 2439301, 50, 4310261, 100],
];

function getCount(ListNumber) {
    var con = null;
    var ps = null;
    var rs = null;
    var q = 0;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("SELECT * FROM `LimitedShop` WHERE `AccountID` = ? AND `ListNumber` = ?");
        ps.setInt(1, cm.getPlayer().getAccountID());
        ps.setInt(2, ListNumber);
        rs = ps.executeQuery();
        while (rs.next()) {
            q = rs.getInt("Count");
        }
        rs.close();
        ps.close();
        con.close();
        return q;
    } catch (e) {
        cm.sendOk("#fs11#오류가 발생하였습니다.\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }
}

function setCount(ListNumber, Number) {
    var con = null;
    var ps = null;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("INSERT INTO `LimitedShop` (`AccountID`, `ListNumber`, `Count`) " +
                "SELECT " + cm.getPlayer().getAccountID() + ", " + ListNumber + ", 0 " +
                "FROM DUAL WHERE NOT EXISTS (SELECT * FROM `LimitedShop` WHERE `AccountID` = " + cm.getPlayer().getAccountID() + " AND `ListNumber` = " + ListNumber + ")");
        ps.executeUpdate();
        ps = con.prepareStatement("UPDATE `LimitedShop` SET `Count` = ? WHERE `AccountID` = " + cm.getPlayer().getAccountID() + " AND `ListNumber` = " + ListNumber + "");
        ps.setInt(1, getCount(ListNumber) + Number);
        ps.executeUpdate();
        ps.close();
        con.close();
    } catch (e) {
        cm.sendOk("#fs11#오류가 발생하였습니다.\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }
}

var choice = -1;
var count = -1;
var limit = -1;
var get = -1;

var status = -1;

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
        var Lcoin = cm.itemQuantity(4310261);
        var say = "#fs11#";
        say += "          #i3800452##fn나눔고딕##b 안녕하세요 로스트 이벤트상점입니다#i3800452#\r\n";
        say += "각 아이템당 계정당 구입할 수 있는 횟수가 제한되어 있습니다.\r\n";
        say += "구입하기 원하시는 아이템을 선택해 주세요\r\n\r\n";
        say += "매달 1일마다 스페셜 아이템으로 리셋!\r\n\r\n";
        say += "#i4310261# 아이템으로 구입가능\r\n\r\n";
        say += "#b현재 #b#h0#님의#r#i4310261##z4310261# 개수 #b" + Lcoin + " \r\n"

        for (var i = 0; i < items.length; i++) {
            if (items[i][2] - getCount(i) > 0) {
                say += "#L" + i + "##i" + items[i][1] + "##z" + items[i][1] + "# - 계정 당 " + (items[i][2] - getCount(i)) + "개 더 구입 가능\r\n";
            }
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        limit = items[selection][2] - getCount(selection);
        count = Math.floor(cm.itemQuantity(items[selection][3]) / items[selection][4]);
        if (count < 0 || limit < 0) {
            cm.dispose();
            return;
        }
        if (count > 30000) {
            count = 30000;
        }
        if (count >= limit) {
            count = limit;
        }
        var say = "";
        say += "#b#fn나눔고딕##i" + items[selection][1] + "##z" + items[selection][1] + "#을 몇 개 구입하시겠습니까?\r\n";
        say += "#b#fn나눔고딕#구입하실 아이템 1개당 #i" + items[selection][3] + "##z" + items[selection][3] + "# " + items[selection][4] + "개가 필요합니다.\r\n";
        say += "#b#fn나눔고딕#현재 계정의 #i" + items[selection][1] + "##z" + items[selection][1] + "# 구입 가능 갯수 : " + limit;
        cm.sendGetNumber(say, 0, 1, count);
    } else if (status == 2) {
        get = selection;
        if (selection < 0 || selection > count || selection > 30000) {
            cm.dispose();
            return;
        }
        cm.sendYesNo("정말로 #i" + items[choice][1] + "##z" + items[choice][1] + "#을 " + selection + "개 구입하시겠습니까?\r\n" +
                "#i" + items[choice][3] + "##z" + items[choice][3] + "# " + (items[choice][4] * selection) + "개가 필요합니다.");
    } else if (status == 3) {
        if (get < 0 || get > count || get > 30000) {
            cm.dispose();
            return;
        }
        if (cm.haveItem(items[choice][3], items[choice][4] * get)) {
            setCount(choice, get);
            cm.gainItem(items[choice][3], -items[choice][4] * get);
            cm.gainItem(items[choice][1], get);
            cm.sendOk("#i" + items[choice][3] + "##z" + items[choice][3] + "# " + (items[choice][4] * get) + "개 구입이 완료되었습니다.\r\n" +
                    "인벤토리를 확인해 주세요.");
            cm.dispose();
            return;
        } else {
            cm.sendOk("#b #fn나눔고딕#재료가 부족하거나 인벤토리 슬롯이 부족한 것은 아닌지 확인해 주세요.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}