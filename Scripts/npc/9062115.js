importPackage(java.lang);

var list = [ //아이템코드, 묶음갯수, 개당가격
    [5062503, 1, 1000],
    [5062005, 1, 1000],    
    [4319999, 1000, 3000], 
    [2049704, 1, 3000],
    [2049360, 1, 3000],
    [4319994, 1000, 5000], 
    [4319997, 100, 5000],
    [2049376, 1, 7000],
    [4319996, 1, 10000],
    [4319995, 1, 10000],
];

var choice = -1;
var count = -1;
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
    var say = "#fs11#"
    if (status == 0) {
        // 501368, "point"
       // cm.getPlayer().setKeyValue(501368, "point", 200);
        var huntcoin = cm.getPlayer().getStarDustCoin2();
        if (cm.getPlayer().getKeyValue(501661, "point") == null) {
            cm.getPlayer().setKeyValue(501661, "point", 0);
        }
        say += "   사냥 코인 상점에 오신 것을 환영합니다.\r\n   구입하실 아이템을 선택해 주세요.\r\n\r\n";
        say += "   현재 사냥 코인 : " + huntcoin + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
            if(list[i][0] == 5121060) {
                say += "#L" + i + "##i" + list[i][0] + "##b 경험치 뿌리기 50%#k (" + list[i][1] + "개) - #r" + list[i][2] + "P#k#l\r\n";
                continue;
            }
            say += "#L" + i + "##i" + list[i][0] + "##b #z" + list[i][0] + "##k (" + list[i][1] + "개) - #r" + list[i][2] + "P#k#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        say += "#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 선택하셨습니다.\r\n";
        say += "몇 묶음을 구입하시겠습니까? 1묶음 당 " + list[choice][2] + "사냥 코인가 필요합니다.\r\n\r\n";
        say += "현재 사냥 코인 : " + cm.getPlayer().getKeyValue(501661, "point") + "\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 0, 1, Math.floor(30000/list[choice][1]));
        } else {
            cm.sendGetNumber(say, 0, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("정말로 #i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하시겠습니까?\r\n" +
        "총 " + (list[choice][2] * count) + "사냥 코인가 필요합니다.\r\n" +
        "현재 사냥 코인 : " + cm.getPlayer().getKeyValue(501661, "point") + "");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getKeyValue(501661, "point")) >= list[choice][2] * count && cm.canHold(list[choice][0], list[choice][1] * count)) {
             cm.getPlayer().AddStarDustCoin2(- list[choice][2] * count);
            cm.gainItem(list[choice][0], list[choice][1] * count);
            cm.sendOk("#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하였습니다. \r\n\r\n" +
            "현재 사냥 코인 : " + cm.getPlayer().getKeyValue(501661, "point") + "");
            cm.dispose();
            return;
        } else {
            cm.sendOk("사냥코인가 부족하거나, 인벤토리 슬롯이 부족한 것은 아닌지 확인해 주세요.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}

