importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var itemlist = [[5068304, 10], [4319999, 300], [4031156, 300], [4001716, 15]];

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
        text += "상자를 개봉하면 아래의 아이템을 획득합니다.\r\n\r\n";
        for (i = 0; i < itemlist.length; i++) {
            text += "#i" + itemlist[i][0] + "# #z" + itemlist[i][0] + "# " + itemlist[i][1] + "개\r\n";
        }
        cm.sendYesNo(text);

    } else if (status == 1) {
        cm.gainItem(2439989, -1);
        for (i = 0; i < itemlist.length; i++) {
            cm.gainItem(itemlist[i][0], itemlist[i][1]);
        }
        //cm.getPlayer().gainDonationPoint(6000000);
        World.Broadcast.broadcastMessage(CWvsContext.serverNotice(2, "[알림] : " + cm.getPlayer().getName() + " 님이 후원 50만 패키지를 개봉 하셨습니다!"));
        cm.sendOk("아이템을 정상적으로 획득했습니다.");
        cm.dispose();
    }
}