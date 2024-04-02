


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    화이트 에 의해 만들어 졌습니다.

    엔피시아이디 : 9071006

    엔피시 이름 : 슈피겔라

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

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
        talk = "몬스터파크에 오신 걸 환영합니다!\r\n"
        talk += "저는 저기 서 있는 슈피겔만의 여동생, #b슈피겔라#k랍니다.\r\n"
        talk += "잘 부탁 드려요!\r\n"
        cm.sendSimpleS(talk, 0x24);
        cm.dispose();
        return;
    }
}
