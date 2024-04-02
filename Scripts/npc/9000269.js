importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;
var enter = "\r\n";
var boxmsg = enter
var NormalReward = [
    {'item': 2630782, 'min': 1, 'max': 1, 'chance': 100}, // 10000 = 100%
    {'item': 2439942, 'min': 1, 'max': 1, 'chance': 100}, // 10000 = 100%
    {'item': 2439653, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 2439952, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2046991, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2047814, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2046992, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2047950, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2046856, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2046857, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2048094, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2048095, 'min': 1, 'max': 1, 'chance': 140}, // 10000 = 100%
    {'item': 2437760, 'min': 10, 'max': 10, 'chance': 200}, // 10000 = 100%
    {'item': 2435719, 'min': 100, 'max': 100, 'chance': 180}, // 10000 = 100%
]

var AdvancedReward = [
    {'item': 2630782, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%
    {'item': 2439959, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%
    {'item': 4033450, 'min': 5, 'max': 5, 'chance': 300}, // 10000 = 100%
    {'item': 4033449, 'min': 5, 'max': 5, 'chance': 300}, // 10000 = 100%
    {'item': 1113130, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 1113131, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 1113132, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 1113133, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 1122151, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 1190303, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100%
    {'item': 5539001, 'min': 1, 'max': 1, 'chance': 400}, // 10000 = 100%
    {'item': 2633336, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%
    {'item': 2633616, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%
    {'item': 5539003, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 5539004, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 5539005, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 2046025, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 2046026, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 2046119, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 2430001, 'min': 10, 'max': 10, 'chance': 500}, // 10000 = 100%
    {'item': 2430044, 'min': 1, 'max': 1, 'chance': 600}, // 10000 = 100%
    {'item': 2430045, 'min': 1, 'max': 1, 'chance': 600}, // 10000 = 100%
    {'item': 2630128, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%
    {'item': 2630129, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%
    {'item': 2049376, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 2430034, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
]

function NormalUnboxing() {
    var ab = 0, qa = 0;
    for (i = 0; i < NormalReward.length; i++) {
        qb = Packages.server.Randomizer.rand(NormalReward[i]['min'], NormalReward[i]['max']);
        tchance = Packages.server.Randomizer.rand(1, 10000);
        if (tchance <= NormalReward[i]['chance']) {
            cm.gainItem(NormalReward[i]['item'], qb);
            ab++;
            boxmsg += "#b#i" + NormalReward[i]['item'] + "##z" + NormalReward[i]['item'] + "# " + qb + "개#k" + enter;
        }
    }
    if (ab == 0) {
        boxmsg += "아쉽게도 꽝이 나왔습니다!";
    } else {
        boxmsg += "총 " + ab + "개의 보상이 나왔습니다!";
    }
}

function AdvancedUnboxing() {
    var ab = 0, qa = 0;
    for (i = 0; i < AdvancedReward.length; i++) {
        qb = Packages.server.Randomizer.rand(AdvancedReward[i]['min'], AdvancedReward[i]['max']);
        tchance = Packages.server.Randomizer.rand(1, 10000);
        if (tchance <= AdvancedReward[i]['chance']) {
            cm.gainItem(AdvancedReward[i]['item'], qb);
            ab++;
            boxmsg += "#b#i" + AdvancedReward[i]['item'] + "##z" + AdvancedReward[i]['item'] + "# " + qb + "개#k 당첨!" + enter;
            if (AdvancedReward[i]['item'] == 2439957 || AdvancedReward[i]['item'] == 2439959 || AdvancedReward[i]['item'] == 4033450 || AdvancedReward[i]['item'] == 4033449 ||
                AdvancedReward[i]['item'] == 1113130 || AdvancedReward[i]['item'] == 1113131 || AdvancedReward[i]['item'] == 1113132 || AdvancedReward[i]['item'] == 1113133 ||
                AdvancedReward[i]['item'] == 1122151 || AdvancedReward[i]['item'] == 2430034 || AdvancedReward[i]['item'] == 2633336 || AdvancedReward[i]['item'] == 2633616 ||
				AdvancedReward[i]['item'] == 2630128 || AdvancedReward[i]['item'] == 2630129 || AdvancedReward[i]['item'] == 1190303) {
                for (ac = 0; ac < ab; ++ac) {
                    if (AdvancedReward[i]['item'] == 1190303) {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 돌림판 고급 이용권에서 STAR : 엠블렘을 획득했습니다."));
                    } else {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 돌림판 고급 이용권에서 " + cm.getItemName(AdvancedReward[i]['item']) + "을 획득했습니다."));
                    }
                    if (ab >= 2) {
                        return;
                    }
                }
            }
        }
    }
    if (ab == 0) {
        boxmsg += "아쉽게도 꽝이 나왔습니다!";
    }
}

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var chat = "#fs11#"
        chat += "#h0#님 안녕하세요? 닉스월드에 방문한걸 환영합니다!" + enter
        chat += "일반 이용권 또는 고급 이용권으로 뽑기에 도전해보세요!" + enter + enter
        chat += "#L0#일반 이용권 뽑기#l"
        chat += "#L1#고급 이용권 뽑기#l"
        cm.sendSimple(chat);
    } else if (status == 1) {
        switch (selection) {
            case 0:
                if (!cm.haveItem(4036660, 1)) {
                    cm.sendOkS("#i4036660# #z4036660# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                    break;
                } else {
                    cm.gainItem(4036660, -1);
                    NormalUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
            case 1:
                if (!cm.haveItem(4036661, 1)) {
                    cm.sendOkS("#i4036661# #z4036661# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                    break;
                } else {
                    cm.gainItem(4036661, -1);
                    AdvancedUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
        }
    }
}