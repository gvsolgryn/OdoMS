var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1003990, // SPACE : 나이트햇
    1003991, // SPACE : 던위치햇
    1003992, // SPACE : 레인져베레
	1003993, // SPACE : 어새신보닛
	1003994, // SPACE : 원더러햇
];

function start() {
    status = -1;
    action(1, 1, 0);
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
        var amed = "#fs11##d#h0#님, 원하시는 SPACE 아이템을 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2439960, -1);
        cm.dispose();
    }
}
