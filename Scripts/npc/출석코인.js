var itmelist = [

[5060048, 3, 1], //골드애플
[2049376, 50, 1], // 화이트 에디셔널 큐브
[5062005, 50, 5], // 어매이징 미라클 큐브
[5062503, 50, 5], // 보스초기화
[5062503, 50, 5], // 보스초기화
[4319999, 100, 3000] // 사냥코인
[4319997, 100, 200] // 하인즈 코어
[4319995, 400, 100] // 악마 부적
[4319996, 400, 100] // 호부
[4319994, 300, 100] // 파란구슬

];

function start() {
status = -1;
action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == -1) {
    cm.dispose();
} else {
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
}
if (status == 0) {
    var a = "어서오세요. [Heinz] 출석 코인 상점 입니다.\n#fs11##fc0xFFFF3366##h0# #fc0xFF000000#님의 출석 코인 : #fc0xFFFF3366#" + cm.itemQuantity(4319999) + " 개#k#n\r\n";
    for (var i = 0; i < itmelist.length; i++) {
        a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#출석 코인#k #e#fc0xFFFF3366#" + itmelist[i][1] + " 개#k#n\r\n";
    }
 /*   for (var i = 18; i < 24; i++) {
        a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#출석 코인#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " 개#k#n\r\n               #r \r\n";
    }
    for (var i = 24; i < itmelist.length; i++) {
        a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#출석 코인#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " 개#k#n\r\n               #r\r\n";
    }*/
    cm.sendSimple(a);

} else if (status == 1) {
    sel = selection;
    cm.sendGetNumber("몇개를 구매?", 1, 1, 100);
    cm.sendOk("#fs11##b출석 코인#k 이 부족합니다.");

} else if (status == 2) {
    count = selection;
    if (sel >= 0 && sel <= itmelist.length) {
        if (cm.itemQuantity(4319999) >= itmelist[sel][1] * count) {
            if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
                cm.sendOk("#b출석 코인#k 으로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 개#k 를 구입 하셨습니다.");
                cm.dispose();
            }
            if (cm.canHold(itmelist[sel][0])) {
                cm.sendOk("#b출석 코인#k 으로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 개#k 를 구입 하셨습니다.");
                cm.dispose();
            }
            cm.itemQuantity(4319999)(-(itmelist[sel][1] * count));
            cm.gainItem(itmelist[sel][0], itmelist[sel][2] * count);
            cm.sendOk("#b출석 코인#k 으로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 개#k 를 구입 하셨습니다.");
            cm.dispose();
        } else {
            cm.sendOk("#fs11##b출석 코인#k 이 부족합니다.");
            cm.dispose();
        }
    }
}
}

