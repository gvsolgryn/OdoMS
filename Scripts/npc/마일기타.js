var status = -1;

var item = [
    [4310086, "자유전직 코인", 1, 300000],
    [2631834, "오토 루팅 상자", 1, 400000],
    [2430368, "메획드롭 60%상자", 1, 150000],
    [4034803, "닉네임 변경권", 1, 200000],
    [4310320, "사냥코인 9999개", 9999, 50000],
    [2430029, "일반보스초기화티켓", 1, 20000],
    [2430030, "상급 보스 초기화 티켓", 1, 40000],
    [5121060, "경험치 뿌리기 2개", 2, 5000],
    [2470018, "황금망치(업횟+3)", 1, 200000],
    [2436616, "상급 메소 상자", 1, 30000],
    [4001780, "일반돌림판 이용권", 1, 30000],
    [5060048, "골드애플 5개", 5, 100000],
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