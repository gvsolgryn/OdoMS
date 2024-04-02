var enter = "\r\n";
var seld = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		var msg = "\r\n#L0# #b추천인 등록#n#l";
		msg += "#L1# #b 일일 퀘스트#n#l"
		msg += "#L2# #b B어드벤처 드릴#n#l\r\n\r\n"
		msg += "#L3##b 각종 뽑기#n#l#l"	
		msg += "#L7##b 슈피게임만#n#l#l"	
		msg += "#L4# #b 레벨업 보상#n#l\r\n\r\n"
		msg += "#L5##b 새벽의 결정#n#l"
		msg += "#L6##b뽑기#n#l\r\n\r\n"
		msg += "#L8##b 티어 시스템#n#l"
		msg += "#L9##b 패스 이벤트#n#l\r\n\r\n"
		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 0:
				cm.dispose();
				cm.openNpc(3001931);
				break;	
			case 1:
				cm.dispose();
				cm.openNpc(3005900);
				break;				
			case 2:
				cm.dispose();
				cm.openNpc(9062147);
				break;
			case 3:
				cm.dispose();
				cm.openNpc(9062074);
				break;
			case 4:
				cm.dispose();
				cm.openNpc(1300007);
				break;
			case 5:
				cm.dispose();
				cm.openNpc(9062541);
				break;
			case 6:
				cm.dispose();
				cm.openNpc(9062506);
				break;
			case 7:
				cm.dispose();
				cm.openNpc(9000198);
				break;
			case 8:
				cm.dispose();
				cm.openNpc(3000103);
				break;
			case 9:
				cm.dispose();
				cm.openNpc(9010061);
				break;
		
		}
	}
}