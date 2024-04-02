


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	키네시스 에 의해 만들어 졌습니다.

	엔피시아이디 : 9071005

	엔피시 이름 : 슈피겔만

	엔피시가 있는 맵 : 꿈의 도시 레헬른 : 1단계 : 무법자들의 거리 (954090000)

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
        if (status == 0) {
            cm.sendNext("크크. 좋아 좀 더 즐겨보라구.");
            cm.dispose();
        } else {
            status --;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendYesNo("왜? 벌써 나가려고? 아직 재미있는 일이 많이 남았는데?");
    } else if (status == 1) {
        cm.sendNext("근성이 부족하군. 뭐 가겠다면 말리진 않겠어. 잘가게.")
    } else if (status == 2) {
        cm.getPlayer().removeSkillCustomInfo(9110);
        cm.getPlayer().setMparkexp(0);
        cm.getPlayer().setMparkcount(0);
        cm.getPlayer().setMparkkillcount(0);
        cm.getPlayer().setMparkCharged(false);
        cm.warp(951000000, 0);
        cm.dispose();
    }
}
