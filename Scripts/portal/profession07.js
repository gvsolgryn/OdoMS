


/*

    오딘 KMS 팀 소스의 스크립트 입니다.

    포탈이 있는 맵 : 헤네시스

    포탈 설명 : 전문기술마을 입장


*/


function enter(pi) {
    if (pi.getPlayer().getLevel() < 30) {
        pi.getPlayer().dropMessage(5, "레벨 30이상만 입장 가능합니다.");
        return false;
    }
    pi.getPlayer().setKeyValue(7860, "returnMap", pi.getPlayer().getMapId()+"");
    pi.warp(910001000, 0);
    return true;
}
