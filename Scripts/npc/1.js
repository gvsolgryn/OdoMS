


/*



*/

var status = -1;

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
        if (cm.getClient().getCustomData(101130, "shopTuto") != null) {
            cm.openShop(15);
            cm.dispose();
        } else {
            cm.sendNextS("(잠수포인트샵에 처음 온 #b#e초보 크리에이터#n#k로군...?)", 4);
        }
    } else if (status == 1) {
        cm.sendNextPrevS("하핫! 선생님은 저희 잠수포인트샵에 처음 오셨군요.\r\n#r#e#i4310312# #t4310312##n#k을 가져오시면 제가 귀한 물건을 보여드리지요.", 4)
    } else if (status == 2) {
        cm.sendNextPrevS("물론 초보 크리에이터가 살 수 있는 물건은 별로 없겠지만...\r\n하핫! 얼른 유명해져서 #r#e#i4310312# #t4310312##n#k을 많이 모아오시죠.", 4);
    } else if (status == 3) {
        //상점오픈
        cm.getClient().setCustomData(101130, "shopTuto", "1");
        cm.openShop(15);
        cm.dispose();
    }
}
