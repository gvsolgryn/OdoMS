importPackage(Packages.constants);

var status = 0;
var selsave = -1;
var enter = "\r\n";

var item;
var items = [2634472,
]

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 2 && sel == 1) status = -1;
        if (sel > 1000000) {
            item = sel;
            if (status == 0) status++;
        }
        status++;
    }
    else
        status--;
    var msg = "#fs11#"
    if (status == 0) {
        msg += "#b남겨진 칼로스의 의지#k에 손을 대자 강력한 힘이 느껴진다." + enter;
        msg += "강력한 힘 속에서 새로운 장비가 탄생하려 한다.#d" + enter + enter;

        if (GameConstants.isWarrior(cm.getPlayer().getJob()))
            for (i = 0; i < 4; i++) msg += "#L" + items[i] + "##i" + items[i] + "# #z" + items[i] + "#" + enter;
        /*else if (GameConstants.isMagician(cm.getPlayer().getJob()))
            for (i = 0; i < 4; i++) msg += "#L" + items[i] + "##i" + items[i] + "# #z" + items[i] + "#" + enter;
        else if (GameConstants.isArcher(cm.getPlayer().getJob()))
            for (i = 0; i < 4; i++) msg += "#L" + items[i] + "##i" + items[i] + "# #z" + items[i] + "#" + enter;
        else if (GameConstants.isThief(cm.getPlayer().getJob()))
            for (i = 0; i < 4; i++) msg += "#L" + items[i] + "##i" + items[i] + "# #z" + items[i] + "#" + enter;
        else if (GameConstants.isPirate(cm.getPlayer().getJob()))
            for (i = 0; i < 4; i++) msg += "#L" + items[i] + "##i" + items[i] + "# #z" + items[i] + "#" + enter;*/

        msg += "#b" + enter;
        msg += "#L0#교환가능 품목을 본다." + enter;
        msg += "#L1#사용을 취소한다.";
        cm.sendOk(msg, 3005267);
    }
    else if (status == 1) {
        if (sel == 1) {
            cm.dispose();
        }
        else if (sel == 0) {
            msg += "#b남겨진 칼로스의 의지#k에 손을 대자 강력한 힘이 느껴진다." + enter;
            msg += "강력한 힘 속에서 새로운 장비가 탄생하려 한다." + enter + enter;
            for (i = 0; i < items.length; i++) msg += "#L" + items[i] + "##i" + items[i] + "# #z" + items[i] + "#" + enter;

            msg += enter;
            msg += "#L0##b전체 장비 리스트를 본다." + enter;
            msg += "#L1#사용을 취소한다.";
            cm.sendOk(msg, 3005267);
        }
    }
    else if (status == 2) {
        msg += "#b남겨진 칼로스의 의지#k에서 #b에테르넬 장비#k가 탄생하려 한다." + enter + enter;

        msg += "#i" + item + "# #z" + item + "#" + enter + enter;

        msg += "#r#e※주의#k" + enter;

        msg += "교환 시 #b남겨진 칼로스의 의지#k가 #r10개 차감#k됩니다.#n#b" + enter + enter;

        msg += "#L0#해당 장비를 선택한다." + enter;
        msg += "#L1#다시 생각해 본다."
        cm.sendOk(msg, 3005267);
    }
    else if (status == 3) {
        if(sel == 0) {
            if (cm.haveItem(2634472, 10)) {
                cm.gainItem(item, 1);
                cm.gainItem(2634472, -10);
                msg += "#b남겨진 칼로스의 의지#k에서 #b에테르넬 장비#k가 탄생했다." + enter + enter;
                msg += "#i" + item + "# #z" + item + "#";
                cm.sendOk(msg, 3005267);
                cm.dispose();
            }
            else {
                msg += "#b남겨진 칼로스의 의지#k가 #r10개#k가 필요합니다."
                cm.sendOk(msg, 3005267);
                cm.dispose();
            }
        }
    }
}