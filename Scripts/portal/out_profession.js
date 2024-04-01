


/*

	���� KMS �� �ҽ��� ��ũ��Ʈ �Դϴ�.

	��Ż�� �ִ� �� : �����������

	��Ż ���� : ����������� ������


*/


function enter(pi) {
    pi.warp(pi.getPlayer().getKeyValue(7860, "returnMap") > 0 ? pi.getPlayer().getKeyValue(7860, "returnMap") : 100000000);
	pi.getPlayer().removeKeyValue(7860, "returnMap");
    return true;
}
