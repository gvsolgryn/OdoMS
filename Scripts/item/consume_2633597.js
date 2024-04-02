


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	★ 에 의해 만들어 졌습니다.

	엔피시아이디 : 9000210

	엔피시 이름 : 병아리

	엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

	엔피시 설명 : MISSINGNO


*/


검정 = "#fc0xFF191919#"
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
        cm.gainItem(1662175, 1);
        cm.gainItem(1672007, 1);
        cm.gainItem(2633597, -1);
        cm.dispose();
    }
}
