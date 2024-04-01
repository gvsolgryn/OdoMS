importPackage(Packages.constants);
importPackage(Packages.server);

별 = "#fUI/GuildMark.img/Mark/Pattern/00004001/3#"
kc = new KoreaCalendar();

var need = [
    {'itemid': 4031586, 'qty': 50},
    {'itemid': 4031938, 'qty': 50}
] // 필요한 아이템 (코드, 개수)

var item = [
    {'itemid': 4140000, 'qty': Math.floor(Math.random() * ((3)) + 1)}
];
/*	
 사랑의 초코스틱 1 ~ 5개
 */



var lovec = [
    {'itemid': 4140000, 'qty': 1}
] // 필요한 아이템 (코드, 개수)

// 사랑의 초코스틱 뽑기 아이템 리스트
var loveid = [
    {'itemid': 4031217, 'qty': 1},
    {'itemid': 4310248, 'qty': 3000},
    {'itemid': 4310308, 'qty': 100},
    {'itemid': 4001716, 'qty': 5},
    {'itemid': 4031227, 'qty': 500},
    {'itemid': 4031013, 'qty': 3}];

/*
 황금빛열쇠
 네로코인
 네오코어
 10억
 붉구
 검구
 */

var seld = -1;

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
        a = cm.getClient().getKeyValue("pepero");
        ab = a == null ? "시작가능" : "완료";
        var choose = "              " + 별 + " #e#r네로월드 빼빼로데이 이벤트#n " + 별 + "#k#fs11#\r\n\r\n\r\n";
        choose += " #r빼빼로 데이#k를 맞이하여 네로월드에서 이벤트를 준비했습니다#k\r\n\r\n";
        choose += "───────────────────────────#k\r\n";
        choose += "#L1##b빼빼로 만들기 설명듣기#l#k\r\n";
        choose += "#L0##b빼빼로 만들기 시작!  (" + ab + ") #l#k\r\n\r\n";
        choose += "#L2##b#z" + item[0]['itemid'] + "# 교환하기#l#k\r\n\r\n";
        choose += "───────────────────────────#k\r\n";
        cm.sendSimple(choose);
    } else if (status == 1) {
        if (selection == 0) {
            temp = [];
            for (i = 0; i < need.length; i++) {
                temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
            }
            temp.sort();
            if (cm.getClient().getKeyValue("pepero") == null) {
                if (haveNeed()) {
                    if (cm.canHold(item[0]['itemid'], (item[0]['qty']))) {
                        cm.getClient().setKeyValue("pepero", kc.getYears() + kc.getMonths() + kc.getDays());
                        var choose = "#fs11#휘휘 저어~ ♪ 초콜릿을 녹이고 그 곳에 맛있게 구워 낸 스틱을 퐁당~ ♬ 초콜릿을 잔뜩 묻혀내세요~ ♪";
                        choose += "\r\n";
                        choose += "과연 몇개의 #z" + item[0]['itemid'] + "#이 만들어질까!!";
                        choose += "\r\n";
                        choose += "#z" + item[0]['itemid'] + "# #r" + item[0]['qty'] + "개#k가 만들어졌어 #b#z" + item[0]['itemid'] + "##k에는 신비한 힘이 섞였으니.. #r좋은 아이템#k을 얻을 수도 있을 거야";
                        choose += "\r\n\r\n";
                        choose += "───────────────────────────#k\r\n\r\n";
                        choose += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
                        for (i = 0; i < item.length; i++) {
                            choose += "#i " + item[i]['itemid'] + "# #z " + item[i]['itemid'] + "# #r" + item[i]['qty'] + "개#k\r\n\r\n";
                        }
                        choose += "───────────────────────────\r\n\r\n";
                        cm.sendOk(choose);
                        for (i = 0; i < need.length; i++) {
                            cm.gainItem(need[i]['itemid'], -(need[i]['qty']));
                        }
                        cm.gainItem(item[0]['itemid'], (item[0]['qty']));

                    } else {
                        cm.sendOk("기타창이 여유로운지 확인해줘");
                    }
                } else {
                    cm.sendOk("엥?.. 재료가 다 있는지 다시 한 번 확인해줘");
                }
            } else {
                cm.sendOk("#h # 너는 오늘 더 이상 만들 수 없어");
            }
            cm.dispose();
        } else if (selection == 1) {
            var choose = "#fs11#이 제과사 쿠커스님이 다시 돌아왔다구! 얼마만에 다시 돌아온 거지? 으아아~~ 그동안 나는 솜씨를 뽐내고 싶어 안달이었다구!!\r\n";
            choose += "이번엔 더욱 멋진 제과 기술을 습득했지!!!";
            choose += "사랑 담긴 빼빼로를 만들어 사랑하는 이에게 선물해보는 건 어때? ";
            choose += "#b밀가루#k를 잘 반죽해서 그것으로 과자를 구워낼거고.. 잘 녹인 #b초콜릿#k을 잔뜩 묻혀서 달콤한 향이 잔뜩 나는 #r초코스틱#k을 만들어 낼거야.";
            choose += "그러기 위해선 몇 가지 재료가 필요하겠지? 몬스터를 사냥하다 보면 이 #b#z4031586##k와 #b#z4031938##k를 얻을 수 있을거야.";
            choose += "\r\n";
            choose += "#b#z4031586##k와 #b#z4031938##k. 이 2가지를 갖고 오면 내가 개발한 비법으로 사랑의 초코스틱을 만들어줄게.";
            choose += "\r\n\r\n";
            for (i = 0; i < need.length; i++) {
                choose += "#i " + need[i]['itemid'] + "# #z " + need[i]['itemid'] + "# #r" + need[i]['qty'] + "개#k  ";
            }
            cm.sendOk(choose);
            cm.dispose();
        } else if (selection == 2) {
            var choose = "#fs11#마침 #b#z" + item[0]['itemid'] + "##k이 필요했는데 나한테 팔아보는 건 어때?";
            choose += "\r\n";
            choose += "뭐.. 안 팔겠다면 상관은 없는데 나한테 팔면 좋은 아이템을 하나 줄 수 있는데 팔아볼래?";
            choose += "\r\n\r\n";
            choose += "#fc0xFFB4B4B4#";
            choose += "(※ 한꺼번에 판매할 수 있는 개수는 최소 1개부터 최대 10개야)";
            cm.sendGetNumber(choose, 1, 1, 10);
            seld = 0;
        }
    } else if (status == 2) {
        if (seld == 0) {
            if (!haveNeeda(selection)) {
                cm.sendOk("#b#z" + item[0]['itemid'] + "##k가 부족한 거 같은데?");
                cm.dispose();
                return;
            }
            cm.gainItem(lovec[0]['itemid'], -(lovec[0]['qty'] * selection));
            var choose = "#fs11#"
            choose += "너의 #b#z" + item[0]['itemid'] + "##k " + selection + "개 덕분에 다른 이에게 #z" + item[0]['itemid'] + "#를 팔 수 있었어 ";
            choose += "고마우니까 너에게 조그만한 선물을 줄게 마음에 들지는 모르겠지만 고마워\r\n\r\n";
            choose += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
            for (i = 0; i < selection; i++) {
                var a = Math.floor(Math.random() * loveid.length);
                cm.gainItem(loveid[a]['itemid'], (loveid[a]['qty']));
                choose += " #i" + loveid[a]['itemid'] + "# #z" + loveid[a]['itemid'] + "# #r" + loveid[a]['qty'] + "개#k\r\n";
            }
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

function haveNeeda(a) {
    var ret = true;
    for (i = 0; i < lovec.length; i++) {
        if (!cm.haveItem(lovec[i]['itemid'], (lovec[i]['qty'] * a)))
            ret = false;
    }
    return ret;
}