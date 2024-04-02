


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	GM 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062112

	엔피시 이름 : 쿤

	엔피시가 있는 맵 : 최후의 해상전 : 입장 대기 (993059200)

	엔피시 설명 : 최후의 해상전 입장


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
        cm.sendOk("n0 : 토벌시작 5분전까진 참여를 해야 한다고 ! ");
        cm.dispose();
        return;
    }
}
