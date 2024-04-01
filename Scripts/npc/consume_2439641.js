var status = -1;
var enter = "\r\n";

function start() {
    action(1, 0 ,0);
}

function action(mode ,type, sel) {
if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var chat = "#fs11#";
        chat += "#r예#k를 누르시면 #b[권한]#k을 지급해드립니디.";
        cm.sendYesNo(chat);

    } else if (status == 1) {

    if (cm.getPlayer().getKeyValue(98199, "gm_check") > 0) {
            cm.sendOk("이미 권한이 주어졌습니다.");
            cm.dispose();
            return;
    }

	if (cm.getPlayer().isGM()) {
        cm.getPlayer().setKeyValue(98199, "gm_check", 1);
        cm.gainItem(2439641, -1);
        cm.sendOk("정상적으로 지급 되었습니다.");
        cm.dispose();
        } else {
        cm.sendOk("넌 받을 자격이 없어.");
         cm.dispose();
         }
    }
   
}