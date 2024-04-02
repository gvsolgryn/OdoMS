


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	카오 에 의해 만들어 졌습니다.

	엔피시아이디 : 1064002

	엔피시 이름 : 알리샤

	엔피시가 있는 맵 : 루타비스 : 거대한 뿌리 (105200000)

	엔피시 설명 : 생명의 근원


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
        cm.sendOk("봉인을 해제하기 위해서는 봉인의 수호자를 처치해야만 해.");
        cm.dispose();
        return;
    }
}
