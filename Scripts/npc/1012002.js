var status = 0;
var beauty = 0;
var select = -1;
var mhair = new Array(30000,30020,30030,30040,30050,30060,30100,30110,30120,30130,30140,30150,30160,30170,30180,30190,30200,30210,30220,30230,30240,30250,30260,30270,30280,30290,30300,30310,30320,30330,30340,30350,30360,30370,30400,30410,30420,30440,30450,30460,30470,30480,30490,30510,30520,30530,30540,30560,30570,30590,30610,30620,30630,30640,30650,30660,30670,30680,30700,30710,30730,30760,30770,30790,30800,30810,30820,30830,30840,30850,30860,30870,30880,30910,30930,30940,30950,33030,33060,33070,33080,33090,33110,33120,33130,33150,33170,33180,33190,33210,33220,33250,33260,33270,33280,33310,33330,33350,33360,33370,33380,33390,33400,33410,33430,33440,33450,33460,33480,33500,33510,33520,33530,33550,33580,33590,33600,33610,33620,33630,33640,33660,33670,33680,33690,33700,33710,33720);
var mface = new Array(20000,20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015,20016,20017,20018,20019,20020,20021,20022,20024,20025,20027,20028,20029,20030,20031,20032,20036,20037,20040,20041,20042,20043,20044,20045,20046,20047,20048,20049,20050,20051,20052,20053,20055,20056,20057,20058,20059,20060,20061,20062,20063,20064,20065,20066,20067,20068,20069,20070,20074,20075,20076,20077,20080,20081,20082,20083,20084,20085,20086,20087,20088,20089,20090,20093,20094,20095,20097,20098,23000,23001,23002,23003,23005,23006,23008,23010,23011,23012,23015,23016,23017,23018,23019,23020,23023,23024,23025,23026,23027,23028,23029,23031,23032,23033,23034,23035,23038,23039,23040,23041,23042,23043,23044,23053,23054,23056,23057,23060,23061,23062,23063,23067,23068);
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (status >= 0 && mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		cm.askAvatarAndroid("",mface);
	} else if (status == 1) {
		var an = cm.getPlayer().getAndroid();
		an.setFace(mface[selection]);
		cm.getPlayer().setAndroid(an);
		cm.dispose();

	}
}