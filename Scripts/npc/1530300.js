importPackage(Packages.server);

var status = -1
var enter = "\r\n";
var beyondMaterial = [[4310063, 1]] //악세서리 재료
var needMeso = 10000000000; // 메소
var 확률 = 100;
var allstat = 1050 // 올스텟
var atk = 1050 // 공마
var star = [
    1113316, 
    1032330,   
    1122443,
    1012757

]; // 강화 가능한템코드

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
        chat += "안녕하세요. #h0#님! 여명의 보스장신구를 강화하고싶나요?" + enter + enter
        chat += "#b여명의 보스장신구 4종#k의 강화가 가능해요. " + enter + enter
        chat += "한계 돌파시 #r올스텟 " + allstat + " 공마 " + atk + "#k가 증가해요." + enter + enter
        chat += "#L0##b장비를 강화 하겠습니다.#k#l" + enter
        chat += "#L1##b강화에 필요한 재료들을 알려주세요.#k#l" + enter
        cm.sendOk("#fs11#" + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter
            chat += "#h0#님이 소유하고 계신 아이템 목록이예요" + enter + enter
            chat += "한계 돌파하고 싶으신 아이템을 선택해 주세요!" + enter + enter
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i)) {
                    if (cm.getInventory(1).getItem(i).getOwner() != "초월") {
                        for (si = 0; si < star.length; si++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == star[si]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                    }
                }
            }
            cm.sendSimple("#fs11#" + chat);
        } else {
            var chat = enter
            chat += "한계 돌파 재료는 아래와 같아요!" + enter + enter + enter
            chat += "#b[ 한계 돌파 재료 ]#k" + enter + enter
            for (i = 0; i < beyondMaterial.length; i++) {
                chat += "#i" + beyondMaterial[i][0] + "# #b#z" + beyondMaterial[i][0] + "##k " + cm.itemQuantity(beyondMaterial[i][0]) + "개 / " + beyondMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / " + getMeso(needMeso) + enter + enter + enter
            cm.sendOk("#fs11#" + chat);
            cm.dispose();
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId();
        if (!beyondMaterialNeed() || cm.getMeso() < 10000000000) {
            cm.sendOk("#fs11#재료가 부족한거 같은데?");
            cm.dispose();
            return;
        }
        while (needMeso > 2000000000)
        {
            cm.gainMeso(-2000000000);
            needMeso -= 2000000000
        }
        cm.gainMeso(-needMeso);
        for (i = 0; i < beyondMaterial.length; i++) {
            cm.gainItem(beyondMaterial[i][0], -beyondMaterial[i][1]);
        }
        vitem = cm.getInventory(1).getItem(sel);

        r1 = Randomizer.rand(0, 100);
        if(r1 > 확률) {
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 실패하였습니다."));
            cm.sendOk("#fs11##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[초월]#k 강화에 실패하셨습니다!#k");
            cm.dispose();
        } else {
            vitem.setOwner("초월");
            vitem.setStr(vitem.getStr() + allstat);
            vitem.setDex(vitem.getDex() + allstat);
            vitem.setInt(vitem.getInt() + allstat);
            vitem.setLuk(vitem.getLuk() + allstat);
            vitem.setWatk(vitem.getWatk() + atk);
            vitem.setMatk(vitem.getMatk() + atk);
            cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
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

function beyondMaterialNeed() {
    var ret = true;
    for (i = 0; i < beyondMaterial.length; i++) {
        if (!cm.haveItem(beyondMaterial[i][0], beyondMaterial[i][1]))
            ret = false;
    }
    return ret;
}
