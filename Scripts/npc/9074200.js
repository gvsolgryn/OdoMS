var status = -1;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        NullKeyValue();
        var chat = "#fs11#";
        chat += "안녕하세요? 멜로디의 보스코인 관련된 업무를\r\n";
        chat += "맡고있는 #e슈피겔만#n이라고 합니다. 무엇을 이용하시겠어요?\r\n\r\n";
        chat += "#d보스코인 갯수 : #r" + cm.itemQuantity(4310225) + "개#k\r\n";
        chat += "#d보스헌터 레벨 : #b" + cm.getPlayer().getKeyValue(0, "Boss_Level") + "\r\n";
        chat += "#L1##r보스레벨 강화하기#k (보스 입장횟수 증가)\r\n";
        chat += "#L2##r캐시장비 강화하기#k (캐시장비 스탯부여)\r\n";
        cm.sendSimple(chat);

    } else if (status == 1) {
        if (selection == 1) {
            cm.dispose();
            cm.openNpc(9074400);

        } else if (selection == 2) {
            cm.dispose();
            cm.openNpc(9062118);
        }
    }
}

function NullKeyValue() {
    if (cm.getPlayer().getKeyValue(0, "Boss_Level") == -1) {
        cm.getPlayer().setKeyValue(0, "Boss_Level", "0");
    }
}