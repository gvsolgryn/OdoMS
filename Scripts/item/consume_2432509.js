/*
	부산스토리 1.2.253 프로젝트 파일입니다.
	func; 전리품 상자
	모든 시간 단위는 '시(H)' 입니다.
*/

var setItem = [5500000, 5500001, 5500002, 5500005, 5500006]
var useItem = [5062009, 5062010, 5062500, 5062503, 2049400, 2049401, 2049402, 4001832, 4001864, 2470000, 2001584, 2000004, 2000005];
var minQty = 1;
var maxQty = 10;

var nItem  = 2432509;
var mPoint = 50 * (1 + Math.floor(Math.random() * 19));
var mMoney = 500 * (50 + Math.floor(Math.random() * 950));

function start()
{
	St = -1;
	action(1, 0, 0);
}

function getQty(i)
{
	switch(Math.floor(i/1000))
	{
		case 2049:
		case 5500:
		case 2470:
		return 1;

		case 5062:
		case 2711:
		return 1 + Math.floor(Math.random() * 4);

		default:
		if(i.equals(4001864))
		{
			return 1;
		}
		else
		{
			return 5 * (1 + Math.floor(Math.random() * 9));
		}
	}
}

function fianlString(i)
{
	if(i == 1)
	{
		return "";
	}
	else
	{
		return ""+i+"개";
	}
}

function Comma(i)
{
	var reg = /(^[+-]?\d+)(\d{3})/;
	i+= '';
	while (reg.test(i))
	i = i.replace(reg, '$1' + ',' + '$2');
	return i;
}

function action(M, T, S)
{
	if(M != 1)
	{
		cm.dispose();
		return;
	}

	if(M == 1)
	St++;
	else
	St--;

	if(!cm.haveItem(nItem))
	{
		cm.getPlayer().dropMessage(1, "알 수 없는 오류가 발생하였습니다.");
		cm.dispose();
		return;
	}

	/* 목록 만들기 */
	itemSelected = new Array();
	itemSelected[0] = setItem[Math.floor(Math.random() * setItem.length)];
	itemSelected[1] = useItem[Math.floor(Math.random() * useItem.length)];
	itemSelected[2] = useItem[Math.floor(Math.random() * useItem.length)];
	itemSelected[3] = useItem[Math.floor(Math.random() * useItem.length)];

	/* 대사 시작 */
	selStr = "#b전리품 상자#k에서 #r메이플포인트 #e"+mPoint+"#n점#k과 #r#e"+Comma(mMoney)+"#n메소#k 그리고 #r아이템#k이 나왔습니다! "
	if(cm.itemQuantity(nItem) != 1)
	{
		selStr += "계속해서 상자를 여시겠습니까?"
	}
	selStr += "\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#fs12#";
	for(i = 0; i < itemSelected.length; i++)
	{
		selStr += "#i"+itemSelected[i]+":# #t"+itemSelected[i]+"# "+fianlString(getQty(itemSelected[i]))+"\r\n";
	}
	if(cm.itemQuantity(nItem) != 1)
	{
		cm.askAcceptDecline(selStr);
	}
	else
	{
		cm.sendOk(selStr);
		cm.dispose();
	}

	/* 아이템 지급 */
	for(z = 0; z < itemSelected.length; z++)
	{
		cm.gainItem(itemSelected[z], getQty(itemSelected[z]));
	}
	cm.getPlayer().modifyCSPoints(2, mPoint, false);
	cm.gainMeso(mMoney);
	cm.gainItem(nItem, -1);

}