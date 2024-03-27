package client;

public class MapleSkillManager {



    public static boolean isKhaliVoydSkills (int skillId){
        switch (skillId) {
            case 154121003: // 보이드 블리츠
            case 154121009: // 보이드 러쉬
            case 154121011: // 보이드 블리츠

                return true;
            default:
                return false;
        }
    }
    public static boolean isKhaliHexSkills(int skillId){
        switch (skillId) {
            case 154111006: // 헥스 : 차크람 스윔
            case 154120033: // 헥스 - 보스 킬러
            case 154120032: // 헥스 - 이그노어 가드
            case 154120031: // 헥스 - 리인포스
            case 400041082: // 헥스 : 판데모니움
            case 400041083: // 헥스 : 판데모니움
            case 154121002: // 헥스 : 차크람 퓨리
            case 154121001: // 헥스 : 차크람 스플릿
                return true;
            default:
                return false;
        }
    }

    public static boolean isKhaliAttackSkills(int skillId) {
        switch (skillId) {

            case 154001000: // 아츠 : 크로스 컷
            case 154101000: // 아츠 : 듀얼 엣지
            case 154111002: // 아츠 : 트리블 배쉬
            case 154121000: // 아츠 : 플러리
                return true;
            default:
                return false;
        }
    }

    public static boolean isUnstableMemorizeSkills (int skillId){
        switch (skillId) {
            /* 썬콜 */
            case 2221004: // 인피니티
            case 2221011: // 프리징 브레스
            case 2221006: // 체인라이트닝
            case 2221007: // 블리자드
                //    case 2221012: // 프로즌 오브
            case 2211011: // 썬더 스피어
                /* 불독 */
            case 2121004: // 인피니티
            case 2121006: // 플레임 스윕
            case 2121003: // 미스트 이럽션
            case 2121007: // 메테오
            case 2111013: // 포이즌 리전
            case 2121011: // 플레임 헤이즈
                /* 비숍 */
            case 2321004: // 인피니티
            case 2321007: // 엔젤레이
            case 2321001: // 빅뱅
            case 2321008: // 제네시스
            case 2311011: // 홀리 파운틴
            case 2311012: // 디바인 프로테션

                return true;
            default:
                return false;
        }
    }

}
