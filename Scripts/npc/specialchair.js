var status;
var select;

검정 = "#fc0xFF191919#"
var 뎀스A = new Array(301100, 3011001, 3011002, 3011003, 3011004, 3011005, 3011006, 3011007, 3011008, 3011009, 3011010, 3011011, 3011012, 3011013, 3011014, 3011015, 3011016, 3011017, 3011018, 3011019, 3011020, 3011021, 3011022, 3011023, 3011024, 3011025, 3011026, 3011027, 3011028);
var 코인 = 4310249
var 필요개수 = 50;
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
            var leaf = cm.itemQuantity(4310291);
            var text = ""
            text = "#fs11#"+검정+"저는 #fc0xFF000087#[닉스]#k "+검정+"컨텐츠 부서에서 #b스페셜 의자 뽑기#k"+검정+"를 담당하고 있는 로밍이라고 합니다."
            text += "\r\n스페셜 의자 뽑기 한번 당 #r#z4310291# 35개#k"+검정+"로 가 가능합니다.\r\n\r\n";
            text += "현재 보유중인 #d#z4310291##k 갯수 : #e#r"+leaf+"#k개#n\r\n"
            text += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            text += "#L0##fc0xFF3B36CF#스페셜 의자를 뽑고 싶습니다.\r\n"
            text += "#L12##fc0xFF3B36CF#스페셜 의자 리스트를 확인하고 싶습니다.\r\n"
            cm.sendSimple(text)
        } else if (status == 1) {
            if (selection == 12) {
                var amed = "\r\n";
                for (var i = 0; i < 뎀스A.length; i++) {
                    amed += "#i" + 뎀스A[i] + "# #fc0xFF6B66FF##z" + 뎀스A[i] + "#\r\n";
                }
                cm.sendOk(amed);
                cm.dispose();
            } else {
                item = selection;
                if (!cm.haveItem(4310291, 35)) {
		            cm.sendOk("#fs11##i4310291# #b#z4310291##k 아이템이 부족하거나 없는거 같네요.");
                    cm.dispose();
                } else {
                    cm.sendYesNo("#fs11#정말로 #b스페셜 의자#k를 뽑으시겠습니까?\r\n뽑기 진행 시 #r#z4310291# 35개#k가 소모됩니다.");
                }
            }
        } else if (status == 2) {
            switch (item) {
                case 0:
                    뎀스A = 뎀스A[Math.floor(Math.random() * 뎀스A.length)];
                    if (cm.canHold(뎀스A)) {
                        cm.gainItem(뎀스A, 1);
                        cm.gainItem(4310291, -35);
                        말 = "#fs11#축하드려요 #b#h ##k님! 원하시던 스페셜 의자는 나오셨나요? 마음에 들지 않는다면 한번 더 뽑아보시는건 어떨까요?\r\n\r\n"
                        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                        말 += "#i" + 뎀스A + "# #b#z" + 뎀스A + "##k\r\n\r\n"
                        말 += "#L0##fc0xFF3B36CF#한번 더 뽑고 싶습니다.\r\n#L1#대화창 닫기"
                        cm.sendOk(말);
                    } else {
                        cm.sendOk("#fs11#스페셜 의자를 받기 위한 공간이 부족한거 같네요.");
                        cm.dispose();
                    }
                    break;
            } 
        } else if (status ==3) {
            if (selection == 0) {
                cm.dispose();
                cm.openNpc(1540105, "SpecialChair");
            } else {
                cm.dispose();
            }
        }
    }
}