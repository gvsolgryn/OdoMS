var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1062190, // SPACE : 워리어팬츠
    1062191, // SPACE : 던위치팬츠
    1062192, // SPACE : 레인져팬츠
	1062193, // SPACE : 어새신팬츠
	1062194, // SPACE : 원더러팬츠
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
        cm.gainItem(2439962, -1);
        cm.dispose();
    }
}
