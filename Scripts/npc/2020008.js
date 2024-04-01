var status = -1
var enter = "\r\n";
var ArmorMaterial = [[4319999, 3000]] //방어구 재료
var 스텟 = 3;
var moon = [1] //악세
var star = [1];
var space = [
    1009984, // 파프 뚝
    1009985,
    1009986,
    1009987,
    1009988,

    1042985, // 파프 상
    1042986,
    1042987,
    1042988,
    1042989,

    1062985, // 파프 하
    1062986,
    1062987,
    1062988,
    1062989,

    1109940, // 앜 망
    1109941,
    1109942,
    1109943,
    1109944,

    1082995, // 앜글
    1082996,
    1082997,
    1082998,
    1082999,

    1079958, // 앜 슈
    1079959,
    1079960,
    1079961,
    1079962,

    1152995, // 앜 숄
    1152996,
    1152997,
    1152998,
    1152999,

    1005980, // 에테 뚝
    1005981,
    1005982,
    1005983,
    1005984,

    1009994, // h에테 뚝
    1009995,
    1009996,
    1009997,
    1009998,

    1042433, // 에테 상
    1042434,
    1042435,
    1042436,
    1042437,

    1042995, // h에테 상
    1042996,
    1042997,
    1042998,
    1042999,

    1062285, // 에테 하
    1062286,
    1062287,
    1062288,
    1062289,

    1062995, // h에테 하
    1062996,
    1062997,
    1062998,
    1062999,

    1152212, // 에테 견
    1152213,
    1152214,
    1152215,
    1152216,

    1153000, // h에테 견
    1153001,
    1153002,
    1153003,
    1153004,

    1113999, // 악세
    1012999,
    1022999,
    1032999,
    1122999,
    1132999,
    1162999,
    1189999,
];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var chat = enter
        chat += "방어구 강화는 HEINZ]파프니르, [HEINZ]아케인셰이드" + enter + enter
        chat += "에테르넬, [HEINZ]에테르넬만 가능합니다." + enter + enter
        chat += "강화는 최대 50강까지 가능하며,." + enter + enter
        chat += "강화할 때 마다 올스탯 " + 스텟 + ", 공격력 " + 스텟 + "가 증가합니다." + enter + enter
        chat += "#L0##b장비를 강화 하겠습니다.#k#l" + enter
        chat += "#L1##b강화에 필요한 재료들을 알려주세요.#k#l" + enter
        cm.sendOk("#fs11#" + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter
            chat += "#h0#님이 소유하고 계신 아이템 목록 입니다." + enter + enter
            chat += "강화하고 싶으신 아이템을 선택해 주세요!" + enter + enter
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i)) {
                    if (cm.getInventory(1).getItem(i).getOwner() != "+50강") {
                        for (mi = 0; mi < moon.length; mi++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == moon[mi]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                        for (si = 0; si < star.length; si++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == star[si]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                    }
                    if (cm.getInventory(1).getItem(i).getOwner() != "+50강") {
                        for (mi = 0; mi < space.length; mi++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == space[mi]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                    }
                }
            }
            cm.sendSimple("#fs11#" + chat);
        } else {
            var chat = enter
            chat += "장비를 강화 하기 위한 재료는 아래와 같습니다." + enter + enter
            chat += "한번 강화할때 마다 아래의 재료가 소모되는점 유의해주세요!" + enter + enter + enter
            chat += "#b[ 방어구 강화 재료 ]#k" + enter + enter
            for (i = 0; i < ArmorMaterial.length; i++) {
                chat += "#i" + ArmorMaterial[i][0] + "# #b#z" + ArmorMaterial[i][0] + "##k " + cm.itemQuantity(ArmorMaterial[i][0]) + "개 / " + ArmorMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / 30억" + enter + enter
            cm.sendOk("#fs11#" + chat);
            cm.dispose();
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId();
        if (space.indexOf(check) != -1) {
            if (!ArmorMaterialNeed() || cm.getMeso() < 3000000000) {
                cm.sendOk("#fs11#재료가 부족한거 같은데?");
                cm.dispose();
                return;
            }
            cm.gainMeso(-3000000000);
            for (i = 0; i < ArmorMaterial.length; i++) {
                cm.gainItem(ArmorMaterial[i][0], -ArmorMaterial[i][1]);
            }
            vitem = cm.getInventory(1).getItem(sel);
            vitem.setOwner(ArmorStar());
            vitem.setStr(vitem.getStr() + 스텟);
            vitem.setDex(vitem.getDex() + 스텟);
            vitem.setInt(vitem.getInt() + 스텟);
            vitem.setLuk(vitem.getLuk() + 스텟);
            vitem.setWatk(vitem.getWatk() + 스텟);
            vitem.setMatk(vitem.getMatk() + 스텟);
            cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
            if (vitem.getOwner() == "+5강" || vitem.getOwner() == "+10강" || vitem.getOwner() == "+15강" || vitem.getOwner() == "+20강" || vitem.getOwner() == "+25강" || vitem.getOwner() == "+30강" || vitem.getOwner() == "+35강" || vitem.getOwner() == "+40강" || vitem.getOwner() == "+45강" || vitem.getOwner() == "+50강") {
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));
            }
            cm.sendOk("#fs11##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[" + vitem.getOwner() + "]#k 강화에 성공하셨습니다!#k");
            cm.dispose();
        }
    }
}

function getMeso(aa) {
    var msg = "";
    bb = aa;
    억 = (Math.floor(bb / 100000000) > 0) ? Math.floor(aa / 100000000) + "억 " : "";
    bb = aa % 100000000;
    msg += 억;
    if (bb > 0) {
        만 = (Math.floor(bb / 10000) > 0) ? Math.floor(bb / 10000) + "만 " : "";
        msg += 만;
    }
    return msg;
}

function ArmorMaterialNeed() {
    var ret = true;
    for (i = 0; i < ArmorMaterial.length; i++) {
        if (!cm.haveItem(ArmorMaterial[i][0], ArmorMaterial[i][1]))
            ret = false;
    }
    return ret;
}

function ArmorStar() {
    switch (vitem.getOwner()) {
        case "+1강":
            return "+2강";
        case "+2강":
            return "+3강";
        case "+3강":
            return "+4강";
        case "+4강":
            return "+5강";
        case "+5강":
            return "+6강";
        case "+6강":
            return "+7강";
        case "+7강":
            return "+8강";
        case "+8강":
            return "+9강";
        case "+9강":
            return "+10강";
        case "+10강":
            return "+11강";
        case "+11강":
            return "+12강";
        case "+12강":
            return "+13강";
        case "+13강":
            return "+14강";
        case "+14강":
            return "+15강";
        case "+15강":
            return "+16강";
        case "+16강":
            return "+17강";
        case "+17강":
            return "+18강";
        case "+18강":
            return "+19강";
        case "+19강":
            return "+20강";
        case "+20강":
            return "+21강";
        case "+21강":
            return "+22강";
        case "+22강":
            return "+23강";
        case "+23강":
            return "+24강";
        case "+24강":
            return "+25강";
        case "+25강":
            return "+26강";
        case "+26강":
            return "+27강";
        case "+27강":
            return "+28강";
        case "+28강":
            return "+29강";
        case "+29강":
            return "+30강";
        case "+30강":
            return "+31강";
        case "+31강":
            return "+32강";
        case "+32강":
            return "+33강";
        case "+33강":
            return "+34강";
        case "+34강":
            return "+35강";
        case "+35강":
            return "+36강";
        case "+36강":
            return "+37강";
        case "+37강":
            return "+38강";
        case "+38강":
            return "+39강";
        case "+39강":
            return "+40강";
        case "+40강":
            return "+41강";
        case "+41강":
            return "+42강";
        case "+42강":
            return "+43강";
        case "+43강":
            return "+44강";
        case "+44강":
            return "+45강";
        case "+45강":
            return "+46강";
        case "+46강":
            return "+47강";
        case "+47강":
            return "+48강";
        case "+48강":
            return "+49강";
        case "+49강":
            return "+50강";
        default:
            return "+1강";
    }
}

function AccessoriesStar() {
    switch (vitem.getOwner()) {
        case "+1강":
            return "+2강";
        case "+2강":
            return "+3강";
        case "+3강":
            return "+4강";
        case "+4강":
            return "+5강";
        case "+5강":
            return "+6강";
        case "+6강":
            return "+7강";
        case "+7강":
            return "+8강";
        case "+8강":
            return "+9강";
        case "+9강":
            return "+10강";
        case "+10강":
            return "+11강";
        case "+11강":
            return "+12강";
        case "+12강":
            return "+13강";
        case "+13강":
            return "+14강";
        case "+14강":
            return "+15강";
        case "+15강":
            return "+16강";
        case "+16강":
            return "+17강";
        case "+17강":
            return "+18강";
        case "+18강":
            return "+19강";
        case "+19강":
            return "+20강";
        case "+20강":
            return "+21강";
        case "+21강":
            return "+22강";
        case "+22강":
            return "+23강";
        case "+23강":
            return "+24강";
        case "+24강":
            return "+25강";
        case "+25강":
            return "+26강";
        case "+26강":
            return "+27강";
        case "+27강":
            return "+28강";
        case "+28강":
            return "+29강";
        case "+29강":
            return "+30강";
        case "+30강":
            return "+31강";
        case "+31강":
            return "+32강";
        case "+32강":
            return "+33강";
        case "+33강":
            return "+34강";
        case "+34강":
            return "+35강";
        case "+35강":
            return "+36강";
        case "+36강":
            return "+37강";
        case "+37강":
            return "+38강";
        case "+38강":
            return "+39강";
        case "+39강":
            return "+40강";
        case "+40강":
            return "+41강";
        case "+41강":
            return "+42강";
        case "+42강":
            return "+43강";
        case "+43강":
            return "+44강";
        case "+44강":
            return "+45강";
        case "+45강":
            return "+46강";
        case "+46강":
            return "+47강";
        case "+47강":
            return "+48강";
        case "+48강":
            return "+49강";
        case "+49강":
            return "+50강";
        default:
            return "+1강";
    }
}