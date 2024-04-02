var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1098009,
    1099016,
    //1099017,
    1342080,
    1352012,
    1352112,
    1352203,
    1352213,
    1352223,
    1352233,
    1352243,
    1352253,
    1352263,
    1352273,
    1352283,
    1352293,
    1352409,
    1352509,
    1352609,
    1352710,
    1352903,
    1352913,
    1352923,
    1352933,
    1352943,
    1352953,
    1352963,
    1352973,
    1353009,
    1353108,
    1353208,
    1353308,
    1353407,
    1353507,
    1353607,
    1353705,
    1353806,
    1354006,
    1354016,
    1354026,
    1354036
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
        var amed = "#fs11##d#h0#님, 원하시는 에덴 보조무기를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2630495, -1);
        cm.dispose();
    }
}
