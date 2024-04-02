importPackage(Packages.constants);
importPackage(Packages.server);

별 = "#fUI/GuildMark.img/Mark/Pattern/00004001/3#"
왕관 = "#fUI/GuildMark.img/Mark/Etc/00009023/1#"
사과 = "#fUI/GuildMark.img/Mark/Plant/00003005/1#"

kc = new KoreaCalendar();
var need = [{'itemid': 3991013, 'qty': 1},
    {'itemid': 3991004, 'qty': 1},
    {'itemid': 3991017, 'qty': 1},
    {'itemid': 3991014, 'qty': 2},
    {'itemid': 3991022, 'qty': 1},
    {'itemid': 3991017, 'qty': 1},
    {'itemid': 3991011, 'qty': 1},
    {'itemid': 3991003, 'qty': 1},
] // 필요한 아이템 (코드, 개수)

var item = [{'itemid': 4031227, 'qty': Math.floor(Math.random() * ((700 - 300)) + 300)},
    {'itemid': 4031013, 'qty': Math.floor(Math.random() * ((6 - 1)) + 1)},
    {'itemid': 4310248, 'qty': Math.floor(Math.random() * ((1500 - 1000)) + 1000)},
    {'itemid': 4310308, 'qty': Math.floor(Math.random() * ((800 - 500)) + 500)}];

var bonus = 10; // 영꺼불 나올 확률

/*	
 붉은 구슬 300 ~ 700
 검은 구슬 1 ~ 6
 네로 코인 1000 ~ 1500
 네오 코어 500 ~ 800
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
        a = cm.getClient().getKeyValue("nero");
        ab = a == null ? "시작가능" : "완료";
        var choose = "                " + 별 + " #e#r네로월드 알파벳 이벤트#n " + 별 + "#k#fs11#\r\n\r\n\r\n";
        choose += "         네로월드에 새로운 #d주간 이벤트#k가 출시 됐습니다.#k\r\n\r\n";
        choose += "───────────────────────────#k\r\n";
        choose += "#L1##r[EVENT]#b 알파벳 찾기 설명듣기#l#k\r\n";
        choose += "#L0##r[EVENT]#b 알파벳 찾기 시작!  (" + ab + ") #l#k\r\n\r\n";
        choose += "───────────────────────────#k\r\n";
        cm.sendSimple(choose);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            temp = [];
            for (i = 0; i < need.length; i++) {
                temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
            }
            temp.sort();
            if (cm.getClient().getKeyValue("nero") == null) {
                if (haveNeed()) {
                    cm.getClient().setKeyValue("nero", kc.getYears() + kc.getMonths() + kc.getDays());
                    var choose = "제가 드리는 선물이에요.\r\n\r\n";
                    choose += "───────────────────────────#k\r\n\r\n";
                    for (i = 0; i < item.length; i++) {
                        choose += "#i " + item[i]['itemid'] + "# #z " + item[i]['itemid'] + "# #r" + item[i]['qty'] + "개#k\r\n\r\n";
                    }
                    if (Randomizer.isSuccess(bonus)) {
                        choose += "\r\n#r#e[보너스 아이템 등장!]#n\r\n";
                        choose += "#i" + 4023027 + "# #z" + 4023027 + "# 1개#k\r\n\r\n";
                    }
                    choose += "───────────────────────────\r\n\r\n";
                    choose += "다음 주에도 참여해 주세요.";
                    cm.sendOk(choose);
                    if (Randomizer.isSuccess(bonus)) {
                        cm.gainItem(4023027, 1);
                    }
                    for (i = 0; i < item.length; i++) {
                        cm.gainItem(item[i]['itemid'], (item[i]['qty']));
                    }
                    for (i = 0; i < need.length; i++) {
                        cm.gainItem(need[i]['itemid'], -(need[i]['qty']));
                    }
                } else {
                    cm.sendOk("아이템이 다 있는지 다시 한 번 확인해 주세요.");
                }
            } else {
                cm.sendOk("#h #님은, 이번 주에 더 이상 참여할 수 없습니다.");
            }
            cm.dispose();
        } else if (sel == 1) {
            var choose = "#fs11#안녕하세요 #b#h ##k님, 즐겁게 네로월드를 즐기고 계신가요 ? \r\n#b11월 10일부터#k #e매주 알파벳 이벤트#n을 진행하고 있답니다.\r\n";
            choose += "#e알파벳 이벤트#n는 몬스터를 통해 알파벳을 얻을 수 있는데요. \r\n";
            choose += "네로 월드 알파벳을 모아서 #eN.E.R.O.W.O.R.L.D#n 글자를 만들어 오시면 네로월드에서 유용하게 쓸 수 있는 멋진 선물을 드릴게요.\r\n\r\n";
            choose += "#i3991013#  #i3991004#  #i3991017#  #i3991014#  #i3991022#  #i3991014#  #i3991017#  #i3991011#  #i3991003#\r\n\r\n";
            choose += "───────────────────────────\r\n\r\n";
            choose += "주간 이벤트 선물 목록\r\n\r\n";
            choose += "#d#i " + item[0]['itemid'] + "# #z " + item[0]['itemid'] + "##k - #r300 ~ 700개\r\n\r\n";
            choose += "#d#i " + item[1]['itemid'] + "# #z " + item[1]['itemid'] + "##k - #r1 ~ 6개\r\n\r\n";
            choose += "#d#i " + item[2]['itemid'] + "# #z " + item[2]['itemid'] + "##k - #r1000 ~ 1500개\r\n\r\n";
            choose += "#d#i " + item[3]['itemid'] + "# #z " + item[3]['itemid'] + "##k - #r500 ~ 800개#k\r\n\r\n";
            choose += "\r\n#r#e[보너스 아이템]#n\r\n";
            choose += "#i" + 4023027 + "# #z" + 4023027 + "# 1개#k\r\n\r\n";
            choose += "───────────────────────────";
            cm.sendOk(choose);
            cm.dispose();
        }
    }
}

function haveNeed() {
    var ret = true;
    for (i = 0; i < need.length; i++) {
        if (!cm.haveItem(need[i]['itemid'], (need[i]['qty'])))
            ret = false;
    }
    return ret;
}