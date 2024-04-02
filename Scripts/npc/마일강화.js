var status = -1;

var item = [
    [2435795, "올스텟 150 공마 100 강화상자 ", 1, 400000],
    [5062006, "플래티넘 미라클 큐브", 1, 10000],
    [5062005, "어메이징 미라클 큐브", 1, 10000],
    [5062503, "화이트 에디셔널 큐브", 1, 15000],
    [2049392, "스타포스 20성 강화권", 1, 100000],
    [2049360, "놀라운 장비강화 주문서", 1, 1000],
    [2049704, "레전드리 잠재주문서", 1, 1000], 
    [2470018, "황금망치(업횟+3)", 1, 200000],
    [2702003, "#z2702003#", 1, 50000], 
    [2046025, "한손무기(공) 주문서", 1, 20000], // 홍보 한손공
    [2046026, "한손무기(마) 주문서", 1, 20000], // 홍보 한손마
    [2046119, "두손무기(공) 주문서", 1, 20000], // 홍보 두손공
    [2046251, "방어구 강화 주문서", 1, 1000], // 홍보 방강
    [2046340, "악세서리(공) 주문서", 1, 30000], // 홍보 악공
    [2046341, "악세서리(마) 주문서", 1, 30000], // 홍보 악마
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
        chat = "#fs11#";
        chat += "#e#b#h0##k 님#k 의 #b일반 마일리지#k : #r" + cm.getPlayer().getHPoint() + " P#k#n\r\n";
        for (i = 0; i < item.length; i++) {
            chat += "#L" + i + "##i" + item[i][0] + "# #d" + item[i][1] + "#k, #b마일리지#k #e#r" + item[i][3] + "#k#n\r\n";
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
                cm.sendOk("마일리지가 부족합니다.");
                cm.dispose();
            }
        }
    }
}