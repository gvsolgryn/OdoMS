


/*

   * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

   * (Guardian Project Development Source Script)

   일리움 에 의해 만들어 졌습니다.

   엔피시아이디 : 9076135

   엔피시 이름 : 보상 2배

   엔피시가 있는 맵 : 헤네시스 : 리나의 집 (100000003)

   엔피시 설명 : MISSINGNO


*/

var status = -1;

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
      talk = "안녕하세요 용사님! #r프리셋 쿠폰#k을 사용하셨군요?\r\n\r\n"
      talk += "#b프리셋#k은 #r공격대 배치상태#k와 #r내부 능력치 배치상태#k를 저장해 두고 #b전투지도에 언제든 불러올 수 있는 기능#k으로, 기본으로 제공되는 2개의 무제한 프리셋과 추가로 #r최대 3개를 더, 영구적으로#k 사용 할 수 있어요.\r\n\r\n"

      talk += "#b어떤 프리셋을 잠금해제#k 해 드릴까요?\r\n\r\n#b"
      if (cm.getPlayer().getKeyValue(500021, "endDate") <= 0)
      talk += "#L3#<3번 프리셋> 잠금해제\r\n"
      if (cm.getPlayer().getKeyValue(500022, "endDate") <= 0)
      talk += "#L4#<4번 프리셋> 잠금해제\r\n"
      if (cm.getPlayer().getKeyValue(500023, "endDate") <= 0)
      talk += "#L5#<5번 프리셋> 잠금해제#r\r\n"
      talk += "#L6#쿠폰을 사용하지 않는다.\r\n"
      cm.sendSimpleS(talk,0x04,9010061);
   } else if (status == 1) {
      if (selection <= 5) {
         cm.getPlayer().SetUnionPriset(selection);
         cm.gainItem(2436884, -1);
         cm.sendOkS("짝짝! #r"+selection+"번 프리셋#k이 영구적으로 사용 가능하게 변경 되었어요!",0x04,9010061);
         cm.dispose();
      } else {
         cm.dispose();
      }
      
   }
}