importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

var status = -1;
var enter = "\r\n";
var guild = ["디즈니"];
var guildId = [8];
var guildEffect = [1150000];
var cg = "";
var cgi = 0;
var 기여도 = 0;
var i = 0;

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
        for (i = 0; i < guildId.length; i++) {
            if (cm.getPlayer().getGuildId() == guildId[i]) {
                cg = guild[i];
                cgi = guildId[i];
                break;
            }
        }

        if (cgi != 0) {
            var chat = "";
            chat += "#fs11#안녕하세요. 저는 HEINZ 서버 길드 이펙트 교환 엔피시 입니다." + enter;
            chat += "#h0#님의 길드 정보입니다." + enter + enter;
            chat += "길드명 : " + cg + enter;
            chat += "기여도 : " + cm.getPlayer().getGuildContribution() + " / " + 기여도 + enter + enter;
            chat += cm.getPlayer().getGuildContribution() >= 기여도 ? "#L0# 지급 받기#l" : "#r기여도가 부족합니다.#k";
            cm.sendOk(chat);
        } else {
            var chat = "";
            chat += "#fs11#길드 이펙트 교환 대상이 아닙니다.#r#e" + enter + enter;
            for(i = guild.length; i >= 0; i--) {
                if(i == guild.length) continue;
                chat += i == 0 ? guild[i] : guild[i] + ", ";
            }
            chat += enter + enter + "#k#n위 길드만 지급 가능합니다."
            cm.sendOk(chat);
            cm.dispose();
        }
    } else if (status == 1) {
        if (selection == 0) {
            var itemId = cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(-123).getItemId();
            if (!cm.haveItem(guildEffect[i]) && itemId != guildEffect[i]) {
                cm.gainItem(guildEffect[i], 1);
                cm.sendOk("지급 되었습니다.");
                cm.dispose();
            } else {
                cm.sendOk("#fs11#이미 지급 받았습니다.");
                cm.dispose();
            }
        } else {
            cm.dispose();
        }
    }
}
