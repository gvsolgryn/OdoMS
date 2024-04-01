var status = -1;

var itemlist = new Array(1012632, 1022278, 1132308, 1672076, 1122430, 1182285, 1032316, 1113306, 1162080, 1162081, 1162082, 1162083, 1190555, 1190556, 1190557, 1190558, 1190559);

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
        var chat = "#fs11#";
        chat += "#L1#상자에서 나오는 아이템 리스트를 확인하고 싶습니다.#l\r\n";
        chat += "#L2#상자를 개봉하겠습니다.#l\r\n";
        cm.sendSimple(chat);

    } else if (status == 1) {
        switch (selection) {
            case 1:
                var chat = "#fs11#";
                for (var i = 0; i < itemlist.length; i++) {
                    chat += "#i" + itemlist[i] + "# #b#z" + itemlist[i] + "##k\r\n\r\n";
                }
                cm.sendOk(chat);
                cm.dispose();
                break;
            case 2:
                var randItem = itemlist[Math.floor(Math.random() * itemlist.length)];
                cm.gainItem(randItem, 1);
                cm.gainItem(2630614, -1);
                cm.sendOk("#fs11##i2630614# #z2630614#에서 #i" + randItem + "# #z" + randItem + "# 아이템을 획득하였습니다.");
                cm.dispose();
                break;
        }
    }
}