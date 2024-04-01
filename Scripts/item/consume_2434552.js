importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var itemlist = [];

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
        cm.dispose();
    }
    if (status == 0) {
        var text = "#fs11#";
        text += "상자를 개봉하면 아래의 아이템을 획득합니다.\r\n";
        for (i = 0; i < itemlist.length; i++) {
            text += "#i" + itemlist[i][0] + "# #z" + itemlist[i][0] + "# " + itemlist[i][1] + "개\r\n";
        }
        text += "#i3994592# 후원포인트 1,500,000만";
        cm.sendYesNo(text);

    } else if (status == 1) {
        cm.gainItem(2434552, -1);
        for (i = 0; i < itemlist.length; i++) {
            cm.gainItem(itemlist[i][0], itemlist[i][1]);
        }
        cm.getPlayer().gainDonationPoint(1500000);
        //World.Broadcast.broadcastMessage(CWvsContext.serverNotice(2, "[알림] : " + cm.getPlayer().getName() + " 님이 후원 5만 패키지를 개봉 하셨습니다!"));
        cm.sendOk("아이템을 정상적으로 획득했습니다.");
        cm.dispose();
    }
}