package constants;

import server.MapleItemInformationProvider;
import tools.Pair;
import tools.Triple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CashCalcInsertData {
  public static String parse2 = "오가닉 원더 쿠키(10개)|15.60\n고농축 프리미엄 생명의물|14.44\n멈뭄미|10.80\n먀몸미|10.80\n햄미|10.80\n식빵이|13.80\n마롱이|13.80\n펭펭이|3.32\n핑핑이|3.32\n퐁퐁이|3.32";
  
  public static String parse = "위대한 매그너스의 소울|0.04503\n위대한 핑크빈의 소울|0.18012\n위대한 아카이럼의 소울|0.54036\n위대한 플레드의 소울|0.36024\n카오스 핑크빈 마크|0.00720\n도미네이터 펜던트|0.00901\n코어 젬스톤 10개 교환권|0.19813\n인형의 집 의자|0.45030\n들꽃 축제 의자|0.45030\n이클립스 버드 라이딩 (영구) 교환권|0.27018\n이클립스 버드 라이딩 (90일) 교환권|0.90059\n케이카 라이딩 (영구) 교환권|0.27018\n케이카 라이딩 (90일) 교환권|0.90059\n매지컬 한손무기 공격력 주문서|0.01801\n매지컬 한손무기 마력 주문서|0.01801\n매지컬 두손무기 공격력 주문서|0.01801\n영원한 환생의 불꽃|0.07205\n티타늄 하트|0.00901\n리튬 하트|0.09006\n크리스탈 하트|0.27018\n골드 하트|0.27018\n기운찬 매그너스의 소울|0.04053\n날렵한 매그너스의 소울|0.04053\n총명한 매그너스의 소울|0.04053\n놀라운 매그너스의 소울|0.04053\n화려한 매그너스의 소울|0.04053\n강력한 매그너스의 소울|0.04053\n빛나는 매그너스의 소울|0.04053\n강인한 매그너스의 소울|0.04053\n영원히 꺼지지 않는 불꽃 조각|0.90059\n스페셜 소울 인챈터|0.45030\n마력의 하운드 이어링|0.25217\n샤이니 레드 매지션 마이스터 심볼|0.25217\n샤이니 레드 시프 마이스터 심볼|0.25217\n샤이니 레드 워리어 마이스터 심볼|0.25217\n베어스 퍼플 펜던트|0.25217\n레드 워리어 마이스터 심볼|0.25217\n레드 파이렛 마이스터 심볼|0.25217\n하프 이어링|0.25217\n레드 시프 마이스터 심볼|0.25217\n레드 매지션 마이스터 심볼|0.25217\n레드 아처 마이스터 심볼|0.25217\n피콕스 퍼플 펜던트|0.25217\n아울스 퍼플 펜던트|0.25217\n울프스 퍼플 펜던트|0.25217\n샤이니 레드 아처 마이스터 심볼|0.25217\n샤이니 레드 파이렛 마이스터 심볼|0.25217\n강인함의 익스트림 벨트|0.25217\n지혜의 익스트림 벨트|0.25217\n행운의 익스트림 벨트|0.25217\n님블 하운드 이어링|0.25217\n날카로운 익스트림 벨트|0.25217\n체력의 하운드 이어링|0.25217\n하이퍼 하운드 이어링|0.25217\n펫장비 공격력 스크롤 100%|0.36024\n펫장비 마력 스크롤 100%|0.36024\n악세서리 공격력 스크롤 100%|0.36024\n악세서리 마력 스크롤 100%|0.36024\n에픽 잠재능력 주문서 50%|0.36024\n에디셔널 잠재능력 부여 주문서 70%|0.27018\n황금 망치 100%|0.54036\n기운찬 핑크빈의 소울|0.54036\n날렵한 핑크빈의 소울|0.54036\n총명한 핑크빈의 소울|0.54036\n놀라운 핑크빈의 소울|0.54036\n화려한 핑크빈의 소울|0.54036\n강력한 핑크빈의 소울|0.54036\n빛나는 핑크빈의 소울|0.54036\n강인한 핑크빈의 소울|0.54036\n기운찬 아카이럼의 소울|0.54036\n날렵한 아카이럼의 소울|0.54036\n총명한 아카이럼의 소울|0.54036\n놀라운 아카이럼의 소울|0.54036\n화려한 아카이럼의 소울|0.54036\n강력한 아카이럼의 소울|0.54036\n빛나는 아카이럼의 소울|0.54036\n강인한 아카이럼의 소울|0.54036\n기운찬 플레드의 소울|0.54036\n날렵한 플레드의 소울|0.54036\n총명한 플레드의 소울|0.54036\n놀라운 플레드의 소울|0.54036\n화려한 플레드의 소울|0.54036\n강력한 플레드의 소울|0.54036\n빛나는 플레드의 소울|0.54036\n강인한 플레드의 소울|0.54036\n소울 분해기|3.60237\n소울 20칸 가방|1.35089\n식물용 20칸 가방|1.35089\n광물용 20칸 가방|1.35089\n제작물품 20칸 가방|1.35089\n레시피 20칸 가방|1.35089\n의자 20칸 가방|1.35089\n칭호 20칸 명함 지갑|1.35089\n주문서 20칸 가방|1.35089\n레전드 메이플 리프|1.35089\n현명한 피노키오 코|1.35089\n얼음결정 페이스페인팅|1.35089\n행운의 피노키오 코|1.35089\n민첩한 피노키오 코|1.35089\n블러드 마스크|1.35089\n힘센 피노키오 코|1.35089\n금빛 각인의 인장|1.80119\n긍정의 혼돈 주문서 30%|1.80119\n긍정의 혼돈 주문서 50%|1.35089\n놀라운 혼돈의 주문서 30%|1.80119\n놀라운 혼돈의 주문서 40%|1.80119\n놀라운 혼돈의 주문서 50%|1.80119\n놀라운 혼돈의 주문서 60%|1.80119\n놀라운 혼돈의 주문서 70%|1.80119\n순백의 주문서 10%|1.35089\n순백의 주문서 5%|1.80119\n은빛 각인의 인장|1.80119\n은빛 에디셔널 각인의 인장|1.80119\n혼돈의 주문서 70%|1.80119\n황금 망치 50%|1.80119\n미라클 장갑 공격력 주문서 50%|1.80119\n미라클 펫장비 공격력 주문서 50%|1.80119\n미라클 악세서리 공격력 주문서 50%|1.80119\n미라클 악세서리 마력 주문서 50%|1.80119\n미라클 방어구 공격력 주문서 50%|1.80119\n미라클 펫장비 마력 주문서 50%|1.80119\n미라클 방어구 마력 주문서 50%|1.80119\n악세서리 공격력 주문서 70%|1.80119\n방어구 공격력 주문서 70%|1.80119\n펫장비 공격력 주문서 70%|1.80119\n방어구 마력 주문서 70%|1.80119\n펫장비 마력 주문서 70%|1.80119\n악세서리 마력 주문서 70%|1.80119";
  
  public static void main(String[] args) {
    String[] strs = parse2.split("\\n");
    for (int i = 0; i < strs.length; i++) {
      List<Triple<String, String, String>> retItems = new ArrayList<>();
      String[] data = strs[i].split("\\|");
      for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
        if (((String)itemPair.getRight()).toLowerCase().equals(data[0]))
          retItems.add(new Triple<>((new StringBuilder()).append(itemPair.getLeft()).append("").toString(), itemPair.getRight(), data[1])); 
      } 
      Scanner sc = new Scanner(System.in);
      List<Triple<String, String, String>> itemcodes = new ArrayList<>();
      if (retItems != null && retItems.size() > 0)
        if (retItems.size() != 1) {
          System.out.println("중복리스트가 존재");
          for (int z = 0; z < retItems.size(); z++)
            System.out.println((z + 1) + ". " + (String)((Triple)retItems.get(z)).getLeft() + " - " + (String)((Triple)retItems.get(z)).getMid()); 
          int geti = sc.nextInt();
          if (geti != -1)
            itemcodes.add(retItems.get(geti - 1)); 
        } else {
          itemcodes.add(retItems.get(0));
        }  
    } 
    BigDecimal dd = new BigDecimal("0.0");
    for (int j = 0; j < strs.length; j++) {
      String[] data = strs[j].split("\\|");
      BigDecimal ff = new BigDecimal(data[1]);
      System.out.println(data[0] + "의 확률은 " + ff + "%");
      dd = dd.add(ff);
    } 
    System.out.println("다합친 확률은 " + dd.toString() + "%");
  }
}
