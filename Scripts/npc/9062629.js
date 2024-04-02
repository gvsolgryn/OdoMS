var enter = "\r\n";
var seld = -1;
var seld2 = -1;

var 뱃지후포 = 100000;
var 포켓후포 = 100000;

var need = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var msg = "#fs11#안녕하세요 #b#h 0##k님! 잠재능력 부여할 수 없는 아이템에 포인트를 통해 잠재능력을 부여가 가능하다는 사실을 알고 계시나요? #fs11##b" + enter;
        msg += "#b#L1#뱃지 아이템 잠재능력 부여 #Cgray#(" + Comma(뱃지후포) + "P 필요)" + enter;
        msg += "#b#L2#포켓 아이템 잠재능력 부여 #Cgray#(" + Comma(포켓후포) + "P 필요)" + enter;
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        need = (seld == 1) ? 뱃지후포 : 포켓후포;
        seldi = seld == 1 ? "뱃지를" : "포켓을";
        ic = seld == 1 ? 118 : 116;

        if (cm.getPlayer().getDonationPoint() < need) {
            cm.sendOk("#fs11#포인트가 부족합니다.");
            cm.dispose();
            return;
        }

        var msg = "#fs11#안녕하세요 #b#h 0##k님! " + seldi + " 선택하셨군요!" + enter;
        msg += "원하시는 #b아이템#k을 골라주세요. #r(에디셔널은 개방되지 않습니다)#k" + enter + enter;
        msg += "#fs11#현재 #b#h 0##k님의 #d후원포인트#k는 #r" + Comma(cm.getPlayer().getDonationPoint()) + "P#k 입니다.#fs12#" + enter + enter;
        switch (seld) {
            case 1:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                    if (item == null)
                        continue;
                    if (Math.floor(item.getItemId() / 10000) == ic)
                        msg += "#L" + i + "# #i" + item.getItemId() + "# #b#z" + item.getItemId() + "#\r\n";
                }
                break;
            case 2:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                    if (item == null)
                        continue;
                    if (Math.floor(item.getItemId() / 10000) == ic)
                        msg += "#L" + i + "# #i" + item.getItemId() + "# #b#z" + item.getItemId() + "#\r\n";
                }
                break;
        }
        cm.sendSimple(msg);
    } else if (status == 2) {
        if (cm.getPlayer().getDonationPoint() < need) {
            cm.sendOk("#fs11#포인트가 부족합니다.");
            cm.dispose();
            return;
        }
        if (seld >= 1) {
            item = cm.getInventory(1).getItem(sel);
        }
        if (item == null) {
            cm.sendOk("#fs11#선택한 아이템이 없습니다, 다시 시도해 주세요.");
            cm.dispose();
            return;
        }
        item.setState(17);
        item.setLines(3);
        item.setPotential1(10041);
        item.setPotential2(10042);
        item.setPotential3(10043);
        cm.getPlayer().gainDonationPoint(-need);
        cm.sendOk("#fs11##b아이템 정보#k\r\n\r\n"
                + "#Cgray##fs11#――――――――――――――――――――――――――――――――――――――――#k"
                + "#r선택 아이템 : #i" + item.getItemId() + "# #z" + item.getItemId() + "#\r\n\r\n"
                + "#Cgray##fs11#――――――――――――――――――――――――――――――――――――――――#k"
                + "#k아이템에 잠재능력을 부여했습니다. 이용해 주셔서 감사합니다.");
        cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
        cm.dispose();
    }
}

function Comma(i)
{
    var reg = /(^[+-]?\d+)(\d{3})/;
    i += '';
    while (reg.test(i))
        i = i.replace(reg, '$1' + ',' + '$2');
    return i;
}