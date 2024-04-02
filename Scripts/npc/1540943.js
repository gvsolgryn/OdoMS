


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    슈피겔만 에 의해 만들어 졌습니다.

    엔피시아이디 : 3003811

    엔피시 이름 : 로렐라이

    엔피시가 있는 맵 : The Black : Night Festival (100000051)

    엔피시 설명 : 운명의 파편 지급


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
        말 = "#fs11#붉은구슬을 파란구슬로 교환해드리고 있습니다. \r\n\r\n"
        말 += "#i4031788##z4031788#와 #i4031227##z4031227#을 \r\n#i4319994##z4319994#로 교환하시겠어요?\r\n"
        cm.sendOk(말);
    } else if (status == 1) {
        var count = cm.itemQuantity(4031227) != null ? cm.itemQuantity(4032227) : 0;
        var count = cm.itemQuantity(4031788) != null ? cm.itemQuantity(4031788) : 0;
        if (count > 0) {
            while(count > 30000) {
                cm.gainItem(4031227, -30000);
                cm.gainItem(4031788, -30000);
                cm.gainItem(4319994, 30000);
                count -= 30000;
            }
            cm.gainItem(4031227, -count);
            cm.gainItem(4031788, -count);
            cm.gainItem(4319994, count);
            cm.sendOk("#fs11##i4319994##z4319994# 아이템으로 " + count + "개를 교환했어요.");
            cm.dispose();
        } else {
            cm.sendOk("#fs11##ㅇ구슬이 없습니다");
            cm.dispose();
        }
    }
}

