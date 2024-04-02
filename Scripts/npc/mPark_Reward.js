var status = -1;
var day = 0;
var item = 0;
var needitem = 4310291;
var needcount = 30;

function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
    data = year + "" + month + "" + date;
    day = time.getDay();
}

function start() {
    status = -1;
    action(1, 0, 0);
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
        getData();
        if (cm.getPlayer().getMparkexp() <= 0) {
            cm.warp(951000000, 0);
            cm.sendOk("오류가 발생 하였습니다 운영자에게 문의하여 주세요.", 9062000)
            cm.dispose();
            return;
        }
        leftslot = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1) {
            cm.sendOk("소비창의 여분의 자리가 없는 것 같구만, 보상을 받아가고 싶지 않은겐가? 그럼 나야 더 좋지 크크.");
            cm.dispose();
            return;
        }
        item = 2434745 + day;
        txt = "몬스터파크는 잘 즐겼나? 보상을 챙겨 주었네,크크.\r\n\r\n";
        txt += "#b#e요일 보상 : #i" + item + "# #z" + item + "# 1개\r\n";
        txt += "경험치 보상 : " + trans_Num(cm.getPlayer().getMparkexp()) + "";
        if (!cm.getPlayer().isMparkCharged()) {
            txt += "\r\n오늘의 무료 클리어 횟수 1회를 사용하였습니다.";
        }
        cm.sendNextS(txt, 5, 9071000);
    } else if (status == 1) {
        cm.sendNextPrevS("그럼 또 이용해 주시게나~", 5, 9071000);
    } else if (status == 2) {
        var suc = true;
        if (cm.getPlayer().isMparkCharged()) {
            if (cm.haveItem(needitem, needcount)) {
                cm.gainItem(needitem, -needcount);
            } else {
                suc = false;
            }
        }
        if (cm.getPlayer().getKeyValue(15179, day + "") < 0) {
            cm.getPlayer().setKeyValue(15179, day + "", "0");
        }
        if (cm.getQuestStatus(15196) == 1) {
            clear = (parseInt(cm.getPlayer().getKeyValue(15179, day + "")) + 1);
            cm.getPlayer().setKeyValue(15179, day + "", clear + "");
        }
        cm.gainExp(cm.getPlayer().getMparkexp());
        cm.getPlayer().removeSkillCustomInfo(9110);
        if (suc) {
            cm.gainItem(item, 1);
        } else {
            cm.getPlayer().dropMessage(5, "더 닉스 코인이 부족하여 보상을 받지 못하였습니다.");
        }
        cm.getPlayer().setMparkexp(0);
        cm.getPlayer().setMparkcount(0);
        cm.getPlayer().setMparkkillcount(0);
        cm.getPlayer().setMparkCharged(false);
        cm.warp(951000000, 0);
        cm.dispose();
    }
}

function trans_Num(str) {
    var str = String(str); //우선 스트림으로 바꾸고
    var result = "";
    var len = str.length;
    if (len > 3) { //세자리 이상일떄만
        for (i = len - 1, j = 0; i >= 0; i--) {
            result = str.substring(i, i + 1) + result; //끝자리 부터 하나씩 합치다가
            j++;
            if (j == 3 && j != 0) { //세자리 되면 콤마 추가
                result = "," + result;
                j = 0;
            }
        }
    }
    return result;
}