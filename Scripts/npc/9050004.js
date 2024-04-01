var enter = "\r\n";
var seld = -1;

var items = [
    {'itemid': 1005847, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1004811, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]
    },
    {'itemid': 1042426, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1042257, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]
    },
    {'itemid': 1062286, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1062168, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]
    },
    {'itemid': 1103365, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1102943, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]
    },
    {'itemid': 1082756, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1082698, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]
    },
    {'itemid': 1073570, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1073161, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]
    },
    {'itemid': 1152212, 'qty': 1, 'bossdmg': 0,
        'recipes': [[1152199, 1], [4310218, 10], [4021022, 1000], [4021020, 1000], [4001832, 9999], [4031574, 1]], 'price': 10000000, 'chance': 30,
        'fail': [[4310218, 1]]}, ];

var item;
var isEquip = false;
var canMake = false;

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
        var msg = "#fs11#제작하실 아이템을 선택해주세요." + enter;
        msg += "#fs11#레시피와 아이템의 정보는 선택하면 나옵니다.#fs12##b" + enter;
        for (i = 0; i < items.length; i++) {
            msg += "#fs11##L" + i + "##i" + items[i]['itemid'] + "##z" + items[i]['itemid'] + "# " + items[i]['qty'] + "개" + enter;
        }
        cm.sendSimple(msg);

    } else if (status == 1) {
        seld = sel;
        item = items[sel];
        isEquip = Math.floor(item['itemid'] / 1000000) == 1;
        canMake = checkItems(item);

        var msg = "선택하신 아이템은 다음과 같습니다.#fs11##b" + enter;
        msg += "아이템 : #i" + item['itemid'] + "##z" + item['itemid'] + "# " + item['qty'] + "개" + enter;
        if (isEquip) {
            if (item['allstat'] > 0) {
                msg += "올스탯 : +" + item['allstat'] + enter;
            }
            if (item['atk'] > 0) {
                msg += "공격력, 마력 : +" + item['atk'] + enter;
            }
            if (item['bossdmg'] > 0) {
                msg += "보스 데미지 : +" + item['bossdmg'] + enter;
            }
        }

        msg += enter;
        msg += "#fs12##k선택하신 아이템을 제작하기 위한 레시피입니다.#fs11##d" + enter + enter;
        if (item['recipes'].length > 0) {
            for (i = 0; i < item['recipes'].length; i++)
                msg += "#i" + item['recipes'][i][0] + "##z" + item['recipes'][i][0] + "# " + item['recipes'][i][1] + "개" + enter;
        }

        if (item['price'] > 0) {
            msg += "#i5300002#" + cm.Comma(item['price']) + " 메소" + enter;
        }

        msg += enter + "#fs12##e제작 성공 확률 : " + item['chance'] + "%#n" + enter + enter;
        msg += "#k제작 실패시 다음과 같은 아이템이 지급됩니다.#fs11##d" + enter + enter;
        if (item['fail'].length > 0) {
            for (i = 0; i < item['fail'].length; i++) {
                msg += "#i" + item['fail'][i][0] + "##z" + item['fail'][i][0] + "# " + item['fail'][i][1] + "개" + enter;
            }
        }

        msg += "#fs12#" + enter;
        msg += canMake ? "#b선택하신 아이템을 만들기 위한 재료들이 모두 모였습니다." + enter + "정말 제작하시려면 '예'를 눌러주세요." : "#r선택하신 아이템을 만들기 위한 재료들이 충분하지 않습니다.";

        if (canMake) {
            cm.sendYesNo(msg);
        } else {
            cm.sendOk(msg);
            cm.dispose();
        }

    } else if (status == 2) {
        canMake = checkItems(item);
        if (!canMake) {
            cm.sendOk("재료가 충분한지 다시 한 번 확인해주세요.");
            cm.dispose();
            return;
        }
        payItems(item);
        if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
            gainItem(item);
            cm.sendOk("축하드립니다!!! 각성에 성공하였습니다...!!!");
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [" + cm.getItemName(item['itemid']) + "] 제작 성공하셨습니다."));
        } else {
            cm.sendOk("안타깝지만 각성에 실패하여 아이템의 잔해만 남았습니다... R.I.P" + enter + "위로 아이템이 지급되었습니다.");
            gainFail(item);
        }
        cm.dispose();
    }
}

function checkItems(i) {
    recipe = i['recipes'];
    ret = true;
    for (j = 0; j < recipe.length; j++) {
        if (!cm.haveItem(recipe[j][0], recipe[j][1])) {
            ret = false;
            break;
        }
    }
    if (ret) {
        ret = cm.getPlayer().getMeso() >= i['price'];
    }
    return ret;
}

function payItems(i) {
    recipe = i['recipes'];
    for (j = 0; j < recipe.length; j++) {
        cm.getPlayer().removeItem(recipe[j][0], -recipe[j][1]);
        cm.gainMeso(-10000000);
    }
}

function gainItem(i) {
    ise = Math.floor(i['itemid'] / 1000000) == 1;
    if (ise) {
        vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(i['itemid']);
        if (i['allstat'] > 0) {
            vitem.setStr(i['allstat']);
            vitem.setDex(i['allstat']);
            vitem.setInt(i['allstat']);
            vitem.setLuk(i['allstat']);
        }
        if (i['atk'] > 0) {
            vitem.setWatk(i['atk']);
            vitem.setMatk(i['atk']);
        }
        if (i['bossdmg'] > 0) {
            vitem.setBossDamage(i['bossdmg']);
        }
        Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
    } else {
        cm.gainItem(i['itemid'], i['qty']);
    }
}

function gainFail(i) {
    fail = i['fail'];
    for (j = 0; j < fail.length; j++) {
        cm.gainItem(fail[j][0], fail[j][1]);
    }
}