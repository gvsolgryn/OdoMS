var status = -1;

var item = [
    [2435795, "올스텟 150 공마 100 강화상자 ", 1, 200000],
    [5062006, " 플래티넘 미라클 큐브", 1, 20000],
    //[2630127, "자석펫 상자 뽑기", 1, 100000],
    //[5068300, "위습의 원더베리", 1, 10000],
    //[5068300, "위습의 원더베리 10개", 10, 50000],
    //[5069100, "루나 크리스탈", 1, 9000],
    //[5069100, "루나 크리스탈 5개", 5, 40000],
    [4310320, "사냥코인 9999개", 9999, 50000],
    [2439527, "칠흑 선택상자", 1, 200000],
    [2430368, "메획드롭 60%상자", 1, 150000],
    [2049392, "스타포스 20성 강화권", 1, 100000],
    [2430029, "일반보스초기화티켓", 1, 20000],
    [2430030, "상급 보스 초기화 티켓", 1, 40000],
    [4310036, "캐시 아이템 강화 코인", 1, 20000],
    [2431849, " 자유전직  코인", 1, 300000],
    //[2048049, "[홍보] 펫장비 공격력", 1, 12000],
    //[2048050, "[홍보] 펫장비 마력", 1, 12000],
    [5121060, "경험치 뿌리기 2개", 2, 5000],
    [4034803, "닉네임 변경권", 1, 200000]];
    

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