var status = -1;

var item = [
    [2633608, "제네시스 무기(25성, 스텟+400)", 1,  1000000],
    [2439527, "칠흑 선택상자", 1, 200000],
    [2633913, "앱솔랩스 방어구 선택상자 ", 1, 5000],
    [2633912, "앱솔랩스 무기 선택 상자", 1, 5000],
    [2633915, "아케인 방어구 선택상자 ", 1, 100000],
    [2630782, "아케인 무기 선택 상자", 1, 100000],
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
        chat += "#fs11##b#h0##k 님#k 의 #b일반 마일리지#k : #r" + cm.getPlayer().getHPoint() + " P#k#n\r\n";
        for (i = 0; i < item.length; i++) {
            chat += "#fs11##L" + i + "##i" + item[i][0] + "# #d" + item[i][1] + "#k, #b마일리지#k #r" + item[i][3] + "#k#n\r\n";
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