
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
        cm.sendOk("#s80002633# 창조의 아이온 스킬을 획득했습니다.");
        cm.teachSkill(80002633, 1, 1);
        cm.gainItem(2436341, -1);
        cm.dispose();
    }
}