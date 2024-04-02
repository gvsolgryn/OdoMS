/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    험난가 에 의해 만들어 졌습니다.

    엔피시아이디 : 3004091

    엔피시 이름 : 라니아

    엔피시가 있는 맵 :  :  (0)

    엔피시 설명 : MISSINGNO


*/
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#fs11#2분 동안 최대한 많은 데미지를 누적 시키는 컨텐츠 입니다.\r\n"
        talk += "#L0#데미지 측정 시작\r\n"
        talk += "#L1#데미지 랭킹\r\n"
        cm.sendSimple(talk);
    } else if (status = 1) {
        switch (selection) {
            case 0:
                cm.startDamageMeter();
                cm.dispose();
                break;
            case 1:
                cm.sendSimple(cm.DamageMeterRank());
                cm.dispose()
                break;
        }
    } else if (status == 2) {
        cm.sendOk("관리자에게 문의해주세요.");
    }
}
