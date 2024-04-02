var status;
var select;

검정 = "#fc0xFF191919#"
var 뎀스A = new Array(1113098, 1113099, 1113100, 1113101, 1113102, 1113103, 1113104, 1113105, 1113106, 1113107, 1113108, 1113109, 1113110, 1113111, 1113112, 1113113, 1113114, 1113115, 1113116, 1113117, 1113118, 1113119, 1113120, 1113121, 1113122, 1113123, 1113124, 1113125, 1113126);
var 코인 = 4310249
var 필요개수 = 10;
var item = 0;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var leaf = cm.itemQuantity(4319999);
             var text = "#fs11#"+검정+"저는 #fc0xFF000087#[HEINZ]#k "+검정+"시드링 뽑기#k"+검정+"를 담당하고 있는 시에라 그레이스라고 합니다."
             text += "\r\n시드링 뽑기 한번 당 #r#z4319999# 2000개#k"+검정+"로 가 가능합니다.\r\n\r\n";
             text += "현재 보유중인 #d#z4319999##k 갯수 : #e#r"+leaf+"#k개#n\r\n"
             text += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
             text += "#L0##fc0xFF3B36CF#시드링을 뽑고 싶습니다.\r\n"
            cm.sendSimple(text)
        } else if (status == 1) {
            if (selection == 12) {
                var amed = "#fs11##fc0xFB1B66FF#데미지 스킨 리스트#k\r\n\r\n";
                for (var i = 0; i < 뎀스A.length; i++) {
                    amed += "#i" + 뎀스A[i] + "# #fc0xFF6B66FF##z" + 뎀스A[i] + "#\r\n";
                }
                cm.sendOk(amed);
                cm.dispose();
            } else {
                item = selection;
                if (!cm.haveItem(4319999, 2000)) {
		            cm.sendOk("#fs11##i4319999# #b#z4319999##k 아이템이 부족하거나 없는거 같네요.");
                    cm.dispose();
                } else {
                    cm.sendYesNo("#fs11#정말로 #b시드링#k을 뽑으시겠습니까?\r\n뽑기 진행 시 #r#z4319999# 2000개#k가 소모됩니다.");
                }
            }
        } else if (status == 2) {
            switch (item) {
                case 0:
                    뎀스A = 뎀스A[Math.floor(Math.random() * 뎀스A.length)];
                    if (cm.canHold(뎀스A)) {
                        cm.gainItem(뎀스A, 1);
                        cm.gainItem(4319999, -2000);
                        말 = "#fs11#축하드려요 #b#h ##k님! 원하시던 시드링은 나오셨나요? 마음에 들지 않는다면 한번 더 뽑아보시는건 어떨까요?\r\n\r\n"
                        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                        말 += "#i" + 뎀스A + "# #b#z" + 뎀스A + "##k\r\n\r\n"
                        말 += "#L0##fc0xFF3B36CF#한번 더 뽑고 싶습니다.\r\n#L1#대화창 닫기"
                        cm.sendOk(말);
                    } else {
                        cm.sendOk("#fs11#시드링을 받기 위한 공간이 부족한거 같네요.");
                        cm.dispose();
                    }
                    break;
            } 
        } else if (status ==3) {
            if (selection == 0) {
                cm.dispose();
                cm.openNpc(1540108);
            } else {
                cm.dispose();
            }
        }
    }
}