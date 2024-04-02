﻿importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);
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
		talk += "안녕하세요? 저는 #e갈매기#n의 안내를 담당하고 있는 안내원입니다. 모험가 여러분이 궁금해하실 정보들을 설명해드리고있습니다.\r\n\r\n";
        talk += "#L0# 갈매기 서버 규칙을 확인하고 싶습니다.#l\r\n";
        talk += "#L1# #e#b심볼을 받고 싶습니다.#n#k#l\r\n";
        //talk += "#L3# 진행중인 이벤트를 확인하고 싶습니다.#l\r\n";
		cm.sendSimple(talk);
    } else if (status == 1) {
        if (selection == 0) {
            talk += "#e[갈매기 서버규칙]#n\r\n\r\n";
            talk += "#fs12#1. 아주 편하게 하고싶은 것 다 하세요.\r\n#fs11##r자유를 억압하지마라 씨발#k\r\n\r\n";
            talk += "#fs12#2. 모르겠고 마음대로 하세요.\r\n#fs11##r알빠노?#k\r\n\r\n";
            talk += "#fs12#3. 비인가 프로그램 사용 (핵,매크로,변조)\r\n#fs11##r쓸거면 쓰세요;#k\r\n\r\n";
        } else if (selection == 1) {
                cm.openNpcCustom(cm.getClient(), 9062286, "심볼");
        } else if (selection == 3) {
            talk += "#fs12#현재 게시된 이벤트가 없습니다.\r\n";
        }
        cm.sendOk(talk);
        cm.dispose();
    } 
}
