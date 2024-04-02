
importPackage(Packages.tools.packet);

var status = -1;

donationItem = [
    // 아이템 코드, 갯수, 가격
    [2100105, 1, 100],
    [2100105, 1, 100],
]

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
    var talk = "#fs11#"
    if (status == 0) {
		talk += "안녕하세요? 저는 #eHEINZ#n의 안내를 담당하고 있는 아일린입니다. 모험가 여러분이 궁금해하실 정보들을 설명해드리고있습니다.\r\n\r\n";
        talk += "#L0# HEINZ 서버규칙을 확인하고 싶습니다.#l\r\n";
        talk += "#L1# 서버배율을 알고싶습니다.#l\r\n";
        talk += "#L3# 진행중인 이벤트를 확인하고 싶습니다.#l\r\n";
		cm.sendSimple(talk);
    } else if (status == 1) {
        if (selection == 0) {
            talk += "#e[하인즈 서버규칙]#n\r\n\r\n";
            talk += "#fs12#1. 타인에게 피해를 주는 행위 (욕설, 비방, 따돌림, 스틸)\r\n#fs11##r1회 경고, 2회 3일정지, 3회 7일정지, 4회 영구정지#k\r\n\r\n";
            talk += "#fs12#2. 현금 거래\r\n#fs11##r경고 없이 영구정지 및 아이템회수 (구매자,판매자)#k\r\n\r\n";
            talk += "#fs12#3. 비인가 프로그램 사용 (핵,매크로,변조)\r\n#fs11##r경고 없이 영구정지#k\r\n\r\n";
        } else if (selection == 1) {
            talk += "#e[하인즈 배율]#n\r\n\r\n";
            talk += "하인즈는 레벨별 커스텀 배율이 적용되어있습니다. 자세한 배율 안내는 홈페이지를 확인해주세요.";
        } else if (selection == 3) {
            talk += "#fs12#현재 게시된 이벤트가 없습니다.\r\n";
        }
        cm.sendOk(talk);
        cm.dispose();
    } 
}
