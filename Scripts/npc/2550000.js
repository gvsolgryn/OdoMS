


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	슈피겔만 에 의해 만들어 졌습니다.

	엔피시아이디 : 3003811

	엔피시 이름 : 로렐라이

	엔피시가 있는 맵 : The Black : Night Festival (100000051)

	엔피시 설명 : 운명의 파편 지급


*/

var status = -1;

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
        말 = "#fn나눔고딕#검은마법사에 입장하려면 재료가 필요합니다. \r\n\r\n"
        말 += "          #i4001894##z4001894# 1개와\r\n"
        말 += "          #i4001893##z4001893# 30개를\r\n"
        말 += "          #i4001895##z4001895# 1개로 교환하시겠어요?\r\n"
        cm.sendYesNo(말);
    } else if (status == 1) {
        if (!cm.haveItem(4001894, 1) || !cm.haveItem(4001893, 30)) {
            cm.sendOk("#fn나눔고딕##z4001895#을 교환하기 위한 재료가 부족해요.");
            cm.dispose();
            return;
        }
        cm.gainItem(4001894, -1);
        cm.gainItem(4001893, -30);
        cm.gainItem(4001895, 1);
        cm.sendOk("#fn나눔고딕##i4001895##z4001895# 아이템으로 교환했어요.");
        cm.dispose();
    }
}
