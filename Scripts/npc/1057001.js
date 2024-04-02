var status = -1;

var item = [
      [2046981, "[홍보] 한손무기 공격력 주문서", 1, 10000],
      [2047810, "[홍보] 두손무기 공격력  주문서", 1, 10000],
      [2046970, "[홍보] 한손무기 마력 주문서", 1, 10000],
      [2046831, "[홍보] 악세서리 공격력 주문서", 1, 10000],
      [2046832, "[홍보] 악세서리 마력 주문서", 1, 10000],
      [2047955, "[홍보] 방어구 주문서", 1, 10000],
      [2431486, "놀장강 주문서 꾸러미", 1, 30000],
      [1802653, "루나 크리스탈 키", 1, 20000],
      [4021031, "뒤틀린 시간의 정수 100개", 100, 10000],
      [5121060, "경험치 뿌리기 1개", 1, 10000],
      [2431917, "심볼 만렙선택권", 1, 500000],
      [4034803, "닉네임 변경권", 1, 500000],
      [2633608, "홍보 제네시스 무기상자", 1, 1200000]];

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
        chat = "#fs11#";
        chat += "#e#b#h0##k 님#k 의 #b홍보 포인트#k : #r" + cm.getPlayer().getHPoint() + " P#k#n\r\n";
        for (i = 0; i < item.length; i++) {
            chat += "#L" + i + "##i" + item[i][0] + "# #d" + item[i][1] + "#k, #b홍보 포인트#k #e#r" + item[i][3] + "#k#n\r\n";
        }
        cm.sendSimple(chat);

    } else if (status == 1) {
        if (selection == 25) {
            cm.dispose();
            cm.openNpc(1031001);

        } else if (selection == 26) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 3003273, "iicashItemsearch");
        } else {
            if (cm.getPlayer().getHPoint() >= item[selection][3]) {
                cm.gainItem(item[selection][0], item[selection][2]);
                cm.getPlayer().gainHPoint(-item[selection][3]);
                cm.sendOk("아이템을 정상적으로 교환했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("홍보 포인트가 부족합니다.");
                cm.dispose();
            }
        }
    }
}