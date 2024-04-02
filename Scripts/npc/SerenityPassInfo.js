var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var NoramlPass = "Serenity_Noraml_Pass_Info";
var PrimeumPass = "Serenity_Premium_Pass_Info";
function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        giveCharacterToEventKeyValue();
		
		var chat = "안녕하세요 끼룩 패스 이벤트입니다 무엇을 도와드릴까요?" + enter;
		chat += "#L0#" + "끼룩 패스 이벤트 설명과 보상 안내"+enter;
		chat += "#L1#" + "#r끼룩 일반 패스 이용하기"+enter;
		chat += "#L2#" + "#b끼룩 프리미엄 패스 이용하기#l"+enter;
		chat +="\r\n\r\n#r프리미엄 패스를 구매하시고 더욱 더 좋은 혜택을 누려보세요!"
		cm.sendSimpleS(chat, talkType);
	} else if (status == 1) {
		switch (selection) {
			case 0:
				cm.sendNextPrevS(SerenityPassExplaination(), talkType);
                		cm.dispose();
				break;
			case 1:
                		cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9010061, NoramlPass);
				break;
			case 2:
                		cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9010061, PrimeumPass);
				break;
		}
	}
}

function SerenityPassExplaination() {
	var explain = "#fs14#끼룩 패스는 총 #r10일#k간 이용할 수 있고, 매일 레벨 범위 몬스터 #b5천마리#k를 처치해야 합니다.\r\n패스는 #r일반 패스#k와 #b프리미엄 패스#k가 있으며 #b프리미엄 패스#k는 마일리지 샵에서 구매할 수 있습니다. \r\n\r\n#b[보상 안내] 순서대로 n일차 보상#k\r\n#rN#k :#i2049704#5,#i4310320#5000,#i5060048#2,#i2437750#30,#i4310332#10,#i4001833#5,#i2439527#,#i4310330#3,#i4033825#10,#i4310332#50\r\n#bP#k :#i4310333#10,#i4310330#5,#i5060048#5,#i4033825#20,#i4310301#100,#i4310302#100,#i4001833#10,#i2643133#,#i4310312#200,#i4310333#30";
	

	return explain;
}

function giveCharacterToEventKeyValue() {
    if (cm.getPlayer().getV("Pass_kill_Monster_amount") == null) {
        cm.getPlayer().addKV("Pass_kill_Monster_amount" , "0");
    }
}