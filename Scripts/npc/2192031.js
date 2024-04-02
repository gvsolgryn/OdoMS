var status = -1
var enter = "\r\n";
var ArmorMaterial = [[4310248, 1000], [4310308, 30]] //방어구 재료
var AccessoriesMaterial = [[4310248, 1000], [4310308, 30]] //악세서리 재료
var moon = [1] //악세
var star = [
    1012632, //루컨마
    1132308, //벨트
    1022278, //안대
    1162080, //마도서
    1162081,
    1162082,
    1162083,
    1122430, //고근
    1032316, //커포
    1113306, //거공
    1182285, //창뱃
    1190555, //미트라
    1190556,
    1190557,
    1190558,
    1190559,
    1113132,
    1113133,
    1113130,
    1113131,
    1190303,
    1162084,
    1132309,
    1122151,
    1032317,
    1012635,
    1029999,
    1162085,
    1162086,
    1122431,
    1162087,
];
var space = [1];

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
        chat += "장신구 강화는 칠흑의 보스 장신구와 특화악세만 가능합니다. " + enter + enter
        chat += "포켓류와 엠블렘도 가능합니다. " + enter + enter
        chat += "강화는 최대 50강까지 가능하며,." + enter + enter
        chat += "강화할 때 마다 올스탯 10, 공격력 5가 증가합니다." + enter + enter
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
            chat += "#b[ 칠흑의 보스 장신구 강화 재료 ]#k" + enter + enter
            for (i = 0; i < AccessoriesMaterial.length; i++) {
                chat += "#i" + AccessoriesMaterial[i][0] + "# #b#z" + AccessoriesMaterial[i][0] + "##k " + cm.itemQuantity(AccessoriesMaterial[i][0]) + "개 / " + AccessoriesMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / 30억" + enter + enter + enter
            /* chat += "#b[ 아케인셰이드 방어구 강화 재료 ]#k" + enter + enter
            for (i = 0; i < ArmorMaterial.length; i++) {
                chat += "#i" + ArmorMaterial[i][0] + "# #b#z" + ArmorMaterial[i][0] + "##k " + cm.itemQuantity(ArmorMaterial[i][0]) + "개 / " + ArmorMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / 30억" + enter + enter */
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
            vitem.setStr(vitem.getStr() + 10);
            vitem.setDex(vitem.getDex() + 10);
            vitem.setInt(vitem.getInt() + 10);
            vitem.setLuk(vitem.getLuk() + 10);
            vitem.setWatk(vitem.getWatk() + 5);
            vitem.setMatk(vitem.getMatk() + 5);
            cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
            if(vitem.getOwner() == "+5강" || vitem.getOwner() == "+10강" || vitem.getOwner() == "+15강" || vitem.getOwner() == "+20강" || vitem.getOwner() == "+25강" || vitem.getOwner() == "+30강" || vitem.getOwner() == "+35강" || vitem.getOwner() == "+40강" || vitem.getOwner() == "+45강" || vitem.getOwner() == "+50강")
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));
            cm.sendOk("#fs11##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[" + vitem.getOwner() + "]#k 강화에 성공하셨습니다!#k");
            cm.dispose();
        } else {
            if (!AccessoriesMaterialNeed() || cm.getMeso() < 3000000000) {
                cm.sendOk("#fs11#재료가 부족한거 같은데?");
                cm.dispose();
                return;
            }
            cm.gainMeso(-3000000000);
            for (i = 0; i < AccessoriesMaterial.length; i++) {
                cm.gainItem(AccessoriesMaterial[i][0], -AccessoriesMaterial[i][1]);
            }
            vitem = cm.getInventory(1).getItem(sel);
            vitem.setOwner(AccessoriesStar());
            vitem.setStr(vitem.getStr() + 10);
            vitem.setDex(vitem.getDex() + 10);
            vitem.setInt(vitem.getInt() + 10);
            vitem.setLuk(vitem.getLuk() + 10);
            vitem.setWatk(vitem.getWatk() + 5);
            vitem.setMatk(vitem.getMatk() + 5);
            cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
            if(vitem.getOwner() == "+5강" || vitem.getOwner() == "+10강" || vitem.getOwner() == "+15강" || vitem.getOwner() == "+20강" || vitem.getOwner() == "+25강" || vitem.getOwner() == "+30강" || vitem.getOwner() == "+35강" || vitem.getOwner() == "+40강" || vitem.getOwner() == "+45강" || vitem.getOwner() == "+50강")
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));
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

function AccessoriesMaterialNeed() {
    var ret = true;
    for (i = 0; i < AccessoriesMaterial.length; i++) {
        if (!cm.haveItem(AccessoriesMaterial[i][0], AccessoriesMaterial[i][1]))
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
