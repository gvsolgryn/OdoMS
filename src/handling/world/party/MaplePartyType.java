/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.world.party;

public enum MaplePartyType {
    LogState(26), // 로그인 상태
    Created(27), // 파티 생성
    Disband(29), // 탈퇴,해체,강퇴
    Join(31), // 파티 가입
    LeaderUpdate(50), // 리더 위임
    PartyTitle(61) // 파티 타이틀 변경
    ;

    private final int type;

    private MaplePartyType(int i) {
        this.type = i;
    }

    public final int getType() {
        return type;
    }

}
