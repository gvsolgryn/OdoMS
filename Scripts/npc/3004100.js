var status = -1;

var nitem = [
    [4319995, 500],
    [4319996, 500],
    [4319997, 5000],
];
var allstat = 1000;
var 말 = "#fs11#"

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        말 += "#fc0xFFB677FF##e[HEINZ]제네시스 무기 제작#n  \r\n\r\n\r\n"
		말 += "#fc0xFF990033#제네시스 무기 #fc0xFF191919#제작을 위해선 아래와 같은 재료를 가져와라.\r\n"
        for(i = 0; i < nitem.length; i++) {
            말 += "#i" + nitem[i][0] + "#  #z" + nitem[i][0] + "# " + nitem[i][1] + "개\r\n"
        }
        말 += "\r\n";
        말 += "#fc0xFF990033#제네시스 무기#fc0xFF191919#는 올스탯 공/마 +" + allstat + " #fc0xFFF15F5F#강화불가 아이템#fc0xFF191919#이다.\r\n\r\n"
		말 += "#fc0xFF990033#제네시스 무기#fc0xFF191919#는 잠재 6줄 모두 선택할 수 있다.\r\n\r\n"
		말 += "#fc0xFF990033#제네시스 무기#fc0xFF191919#를 제작하려면 예 싫다면 아니오를 선택해라.\r\n\r\n"
        cm.sendYesNo(말);
    } else if (status == 1) {
        말 = "";
        if (checkitem()) {
            말 += "#fs11##z2439614#을 제작하기 위한 재료가 부족하다.\r\n\r\n"
            말 += "아래와 같은 아이템을 더 모아와라.\r\n\r\n"
            needitem();            
            cm.sendOk(말);
            cm.dispose();
            return;
        }
        for(i = 0; i < nitem.length; i++) {
            cm.gainItem(nitem[i][0], -nitem[i][1]);
        }
        cm.gainItem(2439614, 1);
        cm.sendOk("#fs11##i2439614##z2439614# 를 제작하였습니다!");
        cm.dispose();
    }
}

function checkitem() {
    for(i = 0; i < nitem.length; i++) {
        if(!cm.haveItem(nitem[i][0], nitem[i][1])) {
            return true;
        }
    }
    return false;
}

function needitem() {
    for(i = 0; i < nitem.length; i++) {
        if(!cm.haveItem(nitem[i][0], nitem[i][1])) {
            말 += "#i" + nitem[i][0] + "#  #z" + nitem[i][0] + "# " + (nitem[i][1] - cm.itemQuantity(nitem[i][0])) + "개\r\n";
        }
    }
}