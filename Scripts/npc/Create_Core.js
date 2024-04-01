importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    닉스 에 의해 만들어 졌습니다.

    엔피시아이디 : 9110007

    엔피시 이름 : 로보

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : 라면 요리사


*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
var sel = -1;
var a = 0;
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
        if (cm.getPlayer().getKeyValue(1477, "count") < 35) {
            cm.sendOkS("코어젬스톤을 제작하기 위해서는 35개의 V 코어 조각이 필요하오. 필요 없는 코어를 분해해서 V 코어 조각을 모은 뒤 시도하시오.", 4, 1540945);
            cm.dispose();
            return;
        }
        cm.sendYesNoS("V 코어 조각을 사용하여 코어젬스톤을 제작하시겠소?\r\n\r\n#b코어젬스톤 제작에 필요한 V 코어 조각 개수 : 35\r\n현재 보유한 V 코어 조각 개수 : "+cm.getPlayer().getKeyValue(1477, "count")+"", 4, 1540945);
    } else if (status == 1) {
        cm.sendOkS("V 코어 조각을 #b35개#k 사용하여 코어젬스톤을 만들었소. 인벤토리를 확인해 보시오.", 4, 1540945)
        cm.getPlayer().setKeyValue(1477, "count", (cm.getPlayer().getKeyValue(1477, "count") - 35) + "");
        cm.gainItem(2435719, 1);
        cm.dispose();
    }
}


function statusplus(millsecond) {
    cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
