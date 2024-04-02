


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	화이트 에 의해 만들어 졌습니다.

	엔피시아이디 : 9071002

	엔피시 이름 : 메리

	엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

	엔피시 설명 : 티켓 교환원


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
        cm.sendNextS("안녕~ 혹시 티켓 사려고 왔어?\r\n#b몬스터파크 REBORN#k은 더 이상 입장에 티켓이 필요하지 않게 되었어~", 0x24);
    } else if (status == 1) {
        cm.sendOkS("#b하루 2번 무료#k로 이용할수 있고,\r\n이 후 부터는 #b더 닉스 코인 30개#k로 이용할 수 있어~", 0x24, 9071002);
        cm.dispose();
    }
}
