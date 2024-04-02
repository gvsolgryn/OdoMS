


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	GM하인즈★ 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062547

	엔피시 이름 : 메인 캐스터 주황버섯

	엔피시가 있는 맵 : HEINZ : 하인즈 중심지 (120043000)

	엔피시 설명 : MISSINGNO


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
        cm.sendOk("상점임다");
        cm.dispose();
        return;
    }
}
