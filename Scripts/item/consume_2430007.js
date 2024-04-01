/*
색변 캐시 상자
*/
검정 = "#fc0xFF191919#"
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.tools.packet);

var itemsz0 = new Array(1008000, 1008001, 1008002, 1008003, 1008004, 1008005, 1008006, 1008007, 1008008, 1008009, 1008010, 1008011, 1008012, 1008013, 1008014, 1008015, 1008016, 1008017, 1008018, 1008019, 1008020, 1008021, 1008022, 1008023, 1008024, 1008025, 1008026, 1008027, 1008028, 1008029, 1008030, 1008031, 1008032, 1008033, 1008034, 1008035, 1008036, 1008037, 1008038, 1008039, 1008040, 1008041, 1008042, 1008043, 1008044, 1008045, 1008046, 1008047, 1008048, 1008049, 1008050, 1008051, 1008052, 1008053, 1008054, 1008055, 1008056, 1008057, 1008058, 1008059, 1008060, 1008061, 1008062, 1008063, 1008064, 1008065, 1008066, 1008067, 1008068, 1008069, 1008070, 1008071, 1008072, 1008073, 1008074, 1008075, 1008076, 1008077, 1008078, 1008079, 1008080, 1008081, 1008082, 1008083, 1008084, 1008085, 1008086, 1008087, 1008088, 1008089, 1008090, 1008091, 1008092, 1008093, 1008094, 1008095, 1008096, 1008097, 1008098, 1008099, 1008100, 1008101, 1008102, 1008103, 1008104, 1008105, 1008106, 1008107, 1008108, 1008109, 1008110, 1008111, 1008112, 1008113, 1008114, 1008115, 1008116, 1008117, 1008118, 1008119, 1008120, 1008121, 1008122, 1008123, 1008124, 1008125, 1008126, 1008127, 1008128, 1008129, 1008130, 1008131, 1008132, 1008133, 1008134, 1008135, 1008136, 1008137, 1008138, 1008139, 1008140, 1008141, 1008142, 1008143, 1008144, 1008145, 1008146, 1008147, 1008148, 1008149, 1008150, 1008151, 1008152, 1008153, 1008154, 1008155, 1008156, 1008157, 1008158, 1008159, 1008160, 1008161, 1008162, 1008163, 1008164, 1008165, 1008166, 1008167, 1008168, 1008169, 1008170, 1008171, 1008172, 1008173, 1008174, 1008175, 1008176, 1008177, 1008178, 1008179, 1008180, 1008181, 1008182, 1008183, 1008184, 1008185, 1008186, 1008187, 1008188, 1008189, 1008190, 1008191, 1008192, 1008193, 1008194, 1008195, 1008196, 1008197, 1008198, 1008199, 1008200, 1008201, 1008202, 1008203, 1008204, 1008205, 1008206, 1008207, 1008208, 1008209, 1008210, 1008211, 1008212, 1008213, 1008214, 1008215, 1008216, 1008217, 1008218, 1008219, 1008220, 1008221, 1008222, 1008223, 1008224, 1008225, 1008226, 1008227, 1008228, 1008229, 1008230, 1008231, 1008232, 1008233, 1008234, 1008235, 1008236, 1008237, 1008238, 1008239, 1008240, 1008241, 1008242, 1008243, 1008244, 1008245, 1008246, 1008247, 1008248, 1008249, 1008250, 1008251, 1008252, 1008253, 1008254, 1008255, 1008256, 1008257, 1008258, 1008259, 1008260, 1008261, 1008262, 1008263, 1008264, 1008265, 1008266, 1008267, 1008268, 1008269, 1008270, 1008271, 1008272, 1008273, 1008274, 1008275, 1008276, 1008277, 1008278, 1008279, 1008280, 1008281, 1008282, 1008283, 1008284, 1008285, 1008286, 1008287, 1008288, 1008289, 1008290, 1008291, 1008292, 1008293, 1008294, 1008295, 1008296, 1008297, 1008298, 1008299, 1008300, 1008301, 1008302, 1008303, 1008304, 1008305, 1008306, 1008307, 1008308, 1008309, 1008310, 1008311, 1008312, 1008313, 1008314, 1008315, 1008316, 1008317, 1008318, 1008319, 1008320, 1008321, 1008322, 1008323, 1008324, 1008325, 1008326, 1008327, 1008328, 1008329, 1008330, 1008331, 1008332, 1008333, 1008334, 1008335, 1008336, 1008337, 1008338, 1008339, 1008340, 1008341, 1008342, 1008343, 1008344, 1008345, 1008346, 1008347, 1008348, 1008349, 1008350, 1008351, 1008352, 1008353, 1008354, 1008355, 1008356, 1008357, 1008358, 1008359, 1008360, 1008361, 1008362, 1008363, 1008364, 1008365, 1008366, 1008367, 1008368, 1008369, 1008370, 1008371, 1008372, 1008373, 1008374, 1008375, 1008376, 1008377, 1008378, 1008379, 1008380, 1008381, 1008382, 1008383, 1008384, 1008385, 1008386, 1008387, 1008388, 1008389, 1008390, 1008391, 1008392, 1008393, 1008394, 1008395, 1008396, 1008397, 1008398, 1008399, 1008400, 1008401, 1008402, 1008403, 1008404, 1008405, 1008406, 1008407, 1008408, 1008409, 1008410, 1008411, 1008412, 1008413, 1008414, 1008415, 1008416, 1008417, 1008418, 1008419, 1008420, 1008421, 1008422, 1008423, 1008424, 1008425, 1008426, 1008427, 1008428, 1008429, 1008430, 1008431, 1008432, 1008433, 1008434, 1008435, 1008436, 1008437, 1008438, 1008439, 1008440, 1008441, 1008442, 1008443, 1008444, 1008445, 1008446, 1008447, 1008448, 1008449, 1008450, 1008451, 1008452, 1008453, 1008454, 1008455, 1008456, 1008457, 1008458, 1008459, 1008460, 1008461, 1008462, 1008463, 1008464, 1008465, 1008466, 1008467, 1008468, 1008469, 1008470, 1008471, 1008472, 1008473, 1008474, 1008475, 1008476, 1008477, 1008478, 1008479, 1008480, 1008481, 1008482, 1008483, 1008484, 1008485, 1008486, 1008487, 1008488, 1008489, 1008490, 1008491, 1008492, 1008493, 1008494, 1008495, 1008496, 1008497, 1008498, 1008499, 1008500, 1008501, 1008502, 1008503, 1008504, 1008505, 1008506, 1008507, 1008508, 1008509, 1008510, 1008511, 1008512, 1008513, 1008514, 1008515, 1008516, 1008517, 1008518, 1008519, 1008520, 1008521, 1008522, 1008523, 1008524, 1008525, 1008526, 1008527, 1008528, 1008529, 1008530, 1008531, 1008532, 1008533, 1008534, 1008535, 1008536, 1008537, 1008538, 1008539, 1008540, 1008541, 1008542, 1008543, 1008544, 1008545, 1008546, 1008547, 1008548, 1008549, 1008550, 1008551, 1008552, 1008553, 1008554, 1008555, 1008556, 1008557, 1008558, 1008559, 1008560, 1008561, 1008562, 1008563, 1008564, 1008565, 1008566, 1008567, 1008568, 1008569, 1008570, 1008571, 1008572, 1008573, 1008574, 1008575, 1008576, 1008577, 1008578, 1008579, 1008580, 1008581, 1008582, 1008583, 1008584, 1008585, 1008586, 1008587, 1008588, 1008589, 1008590, 1008591, 1008592, 1008593, 1008594, 1008595, 1008596, 1008597, 1008598, 1008599, 1008600, 1008601, 1008602, 1008603, 1008604, 1008605, 1008606, 1008607, 1008608, 1008609, 1008610, 1008611, 1008612, 1008613, 1008614, 1008615, 1008616, 1008617, 1008618, 1008619, 1008620, 1008621, 1008622, 1008623, 1008624, 1008625, 1008626, 1008627, 1008628, 1008629, 1008630, 1008631, 1008632, 1008633, 1008634, 1008635, 1008636, 1008637, 1008638, 1008639, 1008640, 1008641, 1008642, 1008643, 1008644, 1008645, 1008646, 1008647, 1008648, 1008649, 1008650, 1008651, 1008652, 1008653, 1008654, 1008655, 1008656, 1008657, 1008658, 1008659, 1008660, 1008661, 1008662, 1008663, 1008664, 1008665, 1008666, 1008667, 1008668, 1008669, 1008670, 1008671, 1008672, 1008673, 1008674, 1008675, 1008676, 1008677, 1008678, 1008679, 1008680, 1008681, 1008682, 1008683, 1008684, 1008685, 1008686, 1008687, 1008688, 1008689, 1008690, 1008691, 1008692, 1008693, 1008694, 1008695, 1008696, 1008697, 1008698, 1008699, 1008700, 1008701, 1008702, 1008703, 1008704, 1008705, 1008706, 1008707, 1008708, 1008709, 1008710, 1008711, 1008712, 1008713, 1008714, 1008715, 1008716, 1008717, 1008718, 1008719, 1008720, 1008721, 1008722, 1008723, 1008724, 1008725, 1008726, 1008727, 1008728, 1008729, 1008730, 1008731, 1008732, 1008733, 1008734, 1008735, 1008736, 1008737, 1008738, 1008739, 1008740, 1008741, 1008742, 1008743, 1008744, 1008745, 1008746, 1008747, 1008748);
//모자

var itemsz1 = new Array(1042800, 1042801, 1042802, 1042803, 1042804, 1042805, 1042806, 1042807, 1042808, 1042809, 1042810, 1042811, 1042812, 1042813, 1042814, 1042815, 1042816, 1042817, 1042818, 1042819, 1042820, 1042821);
//상의
var itemsz2 = new Array(1062800, 1062801, 1062802, 1062803, 1062804, 1062805);
//하의

var itemsz3 = new Array(1055000, 1055001, 1055002, 1055003, 1055004, 1055005, 1055006, 1055007, 1055008, 1055009, 1055010, 1055011, 1055012, 1055013, 1055014, 1055015, 1055016, 1055017, 1055018, 1055019, 1055020, 1055021, 1055022, 1055023, 1055024, 1055025, 1055026, 1055027, 1055030, 1055031, 1055032, 1055033, 1055034, 1055035, 1055036, 1055037, 1055038, 1055039, 1055040, 1055041, 1055042, 1055043, 1055044, 1055045, 1055046, 1055047, 1055048, 1055049, 1055050, 1055051, 1055052, 1055053, 1055054, 1055055, 1055056, 1055057, 1055058, 1055059, 1055060, 1055061, 1055062, 1055063, 1055064, 1055065, 1055066, 1055067, 1055068, 1055069, 1055070, 1055071, 1055072, 1055073, 1055074, 1055075, 1055076, 1055077, 1055078, 1055079, 1055080, 1055081, 1055082, 1055083, 1055084, 1055085, 1055086, 1055087, 1055088, 1055089, 1055090, 1055091, 1055092, 1055095, 1055096, 1055097, 1055098, 1055099, 1055100, 1055101, 1055102, 1055103, 1055104, 1055105, 1055106, 1055107, 1055108, 1055109, 1055110, 1055111, 1055112, 1055113, 1055114, 1055115, 1055116, 1055117, 1055118, 1055119, 1055120, 1055121, 1055122, 1055123, 1055124, 1055125, 1055126, 1055127, 1055128, 1055129, 1055130, 1055131, 1055132, 1055133, 1055134, 1055135, 1055136, 1055137, 1055138, 1055139, 1055140, 1055141, 1055142, 1055143, 1055144, 1055145, 1055146, 1055147, 1055148, 1055149, 1055150, 1055151, 1055152, 1055153, 1055154, 1055155, 1055156, 1055157, 1055158, 1055159, 1055160, 1055161, 1055162, 1055163, 1055164, 1055165, 1055166, 1055167, 1055168, 1055169, 1055170, 1055171, 1055172, 1055173, 1055174, 1055175, 1055176, 1055177, 1055178, 1055179, 1055180, 1055181, 1055182, 1055183, 1055184, 1055185, 1055186, 1055187, 1055188, 1055189, 1055190, 1055191, 1055192, 1055193, 1055194, 1055195, 1055196, 1055197, 1055198, 1055199, 1055200, 1055201, 1055202, 1055203, 1055204, 1055205, 1055206, 1055207, 1055208, 1055209, 1055210, 1055211, 1055212, 1055213, 1055214, 1055215, 1055216, 1055217, 1055218, 1055219, 1055220, 1055221, 1055222, 1055223, 1055224, 1055225, 1055226, 1055227, 1055228, 1055229, 1055230, 1055231, 1055232, 1055233, 1055234, 1055235, 1055236, 1055237, 1055238, 1055239, 1055240, 1055241, 1055242, 1055243, 1055244, 1055245, 1055246, 1055247, 1055248, 1055249, 1055250, 1055251, 1055252, 1055253, 1055254, 1055255, 1055256, 1055257, 1055258, 1055259, 1055260, 1055261, 1055262, 1055263, 1055264, 1055265, 1055266, 1055267, 1055268, 1055269, 1055270, 1055271, 1055272, 1055273, 1055274, 1055275, 1055276, 1055277, 1055278, 1055279, 1055280, 1055281, 1055282, 1055283, 1055284, 1055285, 1055286, 1055287, 1055288, 1055289, 1055290, 1055291, 1055292, 1055293, 1055294, 1055295, 1055296, 1055297, 1055298, 1055299, 1055300, 1055301, 1055302, 1055303, 1055304, 1055305, 1055306, 1055307, 1055308, 1055309, 1055310, 1055311, 1055312, 1055313, 1055314, 1055315, 1055316, 1055317, 1055318, 1055319, 1055320, 1055321, 1055322, 1055323, 1055324, 1055325, 1055326, 1055327, 1055328, 1055329, 1055330, 1055331, 1055332, 1055333, 1055334, 1055335, 1055336, 1055337, 1055338, 1055339, 1055340, 1055341, 1055342, 1055343, 1055344, 1055345, 1055346, 1055347, 1055348, 1055349, 1055350, 1055351, 1055352, 1055353, 1055354, 1055355, 1055356, 1055357, 1055358, 1055359, 1055360, 1055361, 1055362, 1055363, 1055364, 1055365, 1055366, 1055367, 1055368, 1055369, 1055370, 1055371, 1055372, 1055373, 1055374, 1055375, 1055376, 1055377, 1055378, 1055379, 1055380, 1055381, 1055382, 1055383, 1055384, 1055385, 1055386, 1055387, 1055388, 1055389, 1055390, 1055391, 1055392, 1055393, 1055394, 1055395, 1055396, 1055397, 1055398, 1055399, 1055400, 1055401, 1055402, 1055403, 1055404, 1055405, 1055406, 1055407, 1055408, 1055409, 1055410, 1055411, 1055412, 1055413, 1055414, 1055415, 1055416, 1055417, 1055418, 1055419, 1055420, 1055421, 1055422, 1055423, 1055424, 1055425, 1055426, 1055427, 1055428, 1055429, 1055430, 1055431, 1055432, 1055433, 1055434, 1055435, 1055436, 1055437, 1055438, 1055439, 1055440, 1055441, 1055442, 1055443, 1055444, 1055445, 1055446, 1055447, 1055448, 1055449, 1055450, 1055451, 1055452, 1055453, 1055454, 1055455, 1055456, 1055457, 1055458, 1055459, 1055460, 1055461, 1055462, 1055463, 1055464, 1055465, 1055466, 1055467, 1055468, 1055469, 1055470, 1055471, 1055472, 1055473, 1055474, 1055475, 1055476, 1055477, 1055478, 1055479, 1055480, 1055481, 1055482, 1055483, 1055484, 1055485, 1055486, 1055487, 1055488, 1055489, 1055490, 1055491, 1055492, 1055493, 1055494, 1055495, 1055496, 1055497, 1055498, 1055499, 1055500, 1055501, 1055502, 1055503, 1055504, 1055505, 1055506, 1055507, 1055508, 1055509, 1055510, 1055511, 1055512, 1055513, 1055514, 1055515, 1055516, 1055517, 1055518, 1055519, 1055520, 1055521, 1055522, 1055523, 1055524, 1055525, 1055526, 1055527, 1055528, 1055529, 1055530, 1055531, 1055532, 1055533, 1055534, 1055535, 1055536, 1055537, 1055538, 1055539, 1055540, 1055541, 1055542, 1055543, 1055544, 1055545, 1055546, 1055547, 1055548, 1055549, 1055550, 1055551, 1055552, 1055553, 1055554, 1055555, 1055556, 1055557, 1055558, 1055559, 1055560, 1055561, 1055562, 1055563, 1055564, 1055565, 1055566, 1055567, 1055568, 1055569, 1055570, 1055571, 1055572, 1055573, 1055574, 1055575, 1055576, 1055577, 1055578, 1055579, 1055580, 1055581, 1055582, 1055583, 1055584, 1055585, 1055586, 1055587, 1055588, 1055589, 1055590, 1055591, 1055592, 1055593, 1055594, 1055595, 1055596, 1055597, 1055598, 1055599, 1055600, 1055601, 1055602, 1055603, 1055604, 1055605, 1055606, 1055607, 1055608, 1055609, 1055610, 1055611, 1055612, 1055613, 1055614, 1055615, 1055616, 1055617, 1055618, 1055619, 1055620, 1055621, 1055622, 1055623, 1055624, 1055625, 1055626, 1055627, 1055628, 1055629, 1055630, 1055631, 1055632, 1055633, 1055634, 1055635, 1055636, 1055637, 1055638, 1055639, 1055640, 1055641, 1055642, 1055643, 1055644, 1055645, 1055646, 1055647);
//한벌

var itemsz4 = new Array(1088000, 1088001, 1088002, 1088003, 1088004, 1088005, 1088006, 1088007, 1088008, 1088009, 1088010, 1088011, 1088012, 1088013, 1088014, 1088015, 1088016);
//장갑

var itemsz5 = new Array(1078000, 1078001, 1078002, 1078003, 1078004, 1078005, 1078006, 1078007, 1078008, 1078009, 1078010, 1078011, 1078012, 1078013, 1078014, 1078015, 1078016, 1078017, 1078018, 1078019, 1078020, 1078021, 1078022, 1078023, 1078024, 1078025, 1078026, 1078027, 1078028, 1078029, 1078030, 1078031, 1078032, 1078033, 1078034, 1078035, 1078036, 1078037, 1078038, 1078039, 1078040, 1078041, 1078042, 1078043);
//신발

var itemsz6 = new Array(1104000, 1104001, 1104002, 1104003, 1104004, 1104005, 1104006, 1104008, 1104009, 1104010, 1104011, 1104012, 1104013, 1104014, 1104015, 1104016, 1104017, 1104018, 1104019, 1104020, 1104021, 1104022, 1104023, 1104024, 1104025, 1104026, 1104027, 1104028, 1104029, 1104030, 1104031, 1104032, 1104033, 1104034, 1104035, 1104036, 1104037, 1104038, 1104039, 1104040, 1104041, 1104042, 1104043);
//망토

var itemsz7 = new Array(1703500, 1703501, 1703502, 1703503, 1703504, 1703505, 1703506, 1703507, 1703508, 1703509, 1703510, 1703511, 1703512, 1703513, 1703514, 1703515, 1703516, 1703517, 1703518, 1703519, 1703520, 1703521, 1703522, 1703523, 1703524, 1703525, 1703526, 1703527, 1703528, 1703529, 1703530, 1703531, 1703532, 1703533, 1703534, 1703535, 1703536, 1703537, 1703538, 1703539, 1703540, 1703541, 1703542, 1703543, 1703544, 1703545, 1703546, 1703547, 1703548, 1703549, 1703550, 1703551, 1703552, 1703553, 1703554, 1703555, 1703556, 1703557, 1703558, 1703559, 1703560, 1703561, 1703562, 1703563, 1703564, 1703565, 1703566, 1703567, 1703568, 1703569, 1703570, 1703571, 1703572, 1703573, 1703574, 1703575, 1703576, 1703577, 1703578, 1703579, 1703580, 1703581, 1703582, 1703583, 1703584, 1703585, 1703586, 1703587, 1703588, 1703589, 1703590, 1703591, 1703592, 1703593, 1703594, 1703595, 1703596, 1703597, 1703598, 1703599);
//무기

var itemsz8 = new Array(1012900, 1012901, 1012902, 1012903, 1012904, 1012905, 1012906, 1012907, 1012908, 1012909, 1012910, 1012911, 1012912, 1012913);
//얼굴장식

var itemsz9 = new Array(1022800, 1022801, 1022802, 1022803, 1022804, 1022805, 1022806, 1022807, 1022808, 1022809, 1022810);
//눈장식

/*for (var cap = 1008000; cap < 1009000; cap++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(cap) != null) {
        if (MapleItemInformationProvider.getInstance().getName(cap).contains("[블랙]")) {
            itemsz0.push(cap);
        }
    }
}

for (var coat = 1042800; coat < 1042900; coat++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(coat) != null) {
        if (MapleItemInformationProvider.getInstance().getName(coat).contains("[블랙]")) {
            itemsz1.push(coat);
        }
    }
}

for (var pants = 1062800; pants < 1062900; pants++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(pants) != null) {
        if (MapleItemInformationProvider.getInstance().getName(pants).contains("[블랙]")) {
            itemsz2.push(pants);
        }
    }
}

for (var longcoat = 1055000; longcoat < 1057000; longcoat++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(longcoat) != null) {
        if (MapleItemInformationProvider.getInstance().getName(longcoat).contains("[블랙]")) {
            itemsz3.push(longcoat);
        }
    }
}

for (var glove = 1088000; glove < 1089000; glove++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(glove) != null) {
        if (MapleItemInformationProvider.getInstance().getName(glove).contains("[블랙]")) {
            itemsz4.push(glove);
        }
    }
}

for (var shose = 1078000; shose < 1079000; shose++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(shose) != null) {
        if (MapleItemInformationProvider.getInstance().getName(shose).contains("[블랙]")) {
            itemsz5.push(shose);
        }
    }
}

for (var cape = 1104000; cape < 1105000; cape++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(cape) != null) {
        if (MapleItemInformationProvider.getInstance().getName(cape).contains("[블랙]")) {
            itemsz6.push(cape);
        }
    }
}

for (var weapon = 1703500; weapon < 1703800; weapon++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(weapon) != null) {
        if (MapleItemInformationProvider.getInstance().getName(weapon).contains("[블랙]")) {
            itemsz7.push(weapon);
        }
    }
}

for (var Accessory = 1012900; Accessory < 1012990; Accessory++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(Accessory) != null) {
        if (MapleItemInformationProvider.getInstance().getName(Accessory).contains("[블랙]")) {
            itemsz8.push(Accessory);
        }
    }
}

for (var glasses = 1022800; glasses < 1022890; glasses++) {
    if (MapleItemInformationProvider.getInstance().getItemInformation(glasses) != null) {
        if (MapleItemInformationProvider.getInstance().getName(glasses).contains("[블랙]")) {
            itemsz9.push(glasses);
        }
    }
}*/

var itemCategorys = new Array(
    "#L0##fs11##b모자 선택하기#k",
    "#fs11##b상의 선택하기#k",
    "#fs11##b하의 선택하기#k",
    "#fs11##b한벌 선택하기#k",
    "#fs11##b장갑 선택하기#k",
    "#fs11##b신발 선택하기#k",
    "#fs11##b망토 선택하기#k",
    "#fs11##b무기 선택하기#k",
    "#fs11##b얼굴장식 선택하기",
    "#fs11##b눈장식 선택하기#k\r\n\r\n#l");


var status = -1;
var menuSelect = -1;
var select = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        var leaf = cm.itemQuantity(2430007);
        var trade = "#fs11#" + 검정 + "블랙 페스티벌에서 #fc0xFF4641D9#특별한 의상#k" + 검정 + "을 #fc0xFFF15F5F#선택#k" + 검정 + "해서 받을 수 있다네.\r\n원하는 아이템을 선택해보게!\r\n"
        trade += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        for (var i = 0; i < itemCategorys.length; i++) {
            if (i == 0) {
                trade += itemCategorys[i] + "#l\r\n";
            } else {
                trade += "#L" + i + "#" + itemCategorys[i] + "#l\r\n";

            }
        }
        cm.sendSimple(trade);
    } else if (status == 1) {
        menuSelect = selection;
        if (selection == 50) {
            for (var i = 0; i < itemCategorys.length; i++) {
                trade += "#L" + i + "#" + itemCategorys[i] + "#l";
            }
        } else {
            var trade = "\r\n";
            var itemsArray = getArray(selection);
            for (var i = 0; i < itemsArray.length; i++) {
                trade += "#L" + i + "##i" + itemsArray[i] + "##l";
                if (i % 5 == 4) {
                    trade += "\r\n";
                }
            }
            cm.sendSimple(trade);
        }
    } else if (status == 2) {
        select = selection;
        var itemsArray = getArray(menuSelect);
        inz = MapleItemInformationProvider.getInstance().getEquipById(itemsArray[select]);
        var check = 0;
        for (b = 0; b < itemsz0.length; b++) {
            if (itemsArray[select] == itemsz0[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz1.length; b++) {
            if (itemsArray[select] == itemsz1[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz2.length; b++) {
            if (itemsArray[select] == itemsz2[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz3.length; b++) {
            if (itemsArray[select] == itemsz3[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz4.length; b++) {
            if (itemsArray[select] == itemsz4[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz5.length; b++) {
            if (itemsArray[select] == itemsz5[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz6.length; b++) {
            if (itemsArray[select] == itemsz6[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz7.length; b++) {
            if (itemsArray[select] == itemsz7[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz8.length; b++) {
            if (itemsArray[select] == itemsz8[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz9.length; b++) {
            if (itemsArray[select] == itemsz9[b]) {
                check = 1;
                break;
            }
        }
        if (check == 0) {
            a = new Date();
            temp = Randomizer.rand(0, 9999999);
            cn = cm.getPlayer().getName();
            fFile1 = new File("Log/Item/" + a.getDate() + "_" + a.getHours() + "_" + a.getMinutes() + "_" + a.getSeconds() + "_" + cn + ".log");
            if (!fFile1.exists()) {
                fFile1.createNewFile();
            }
            out1 = new FileOutputStream("Log/Item/" + a.getDate() + "_" + a.getHours() + "_" + a.getMinutes() + "_" + a.getSeconds() + "_" + cn + ".log", false);
            msg = "'" + cm.getPlayer().getName() + "'이(가) 의심됨.\r\n";
            msg = "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 " + a.getHours() + "시 " + a.getMinutes() + "분 " + a.getSeconds() + "초'\r\n";
            msg += "복사 시도 스크립트 : consume_2430007\r\n";
            msg += "사용자 캐릭터 아이디 : " + cm.getPlayer().getId() + "\r\n";
            msg += "사용자 어카운트 아이디 : " + cm.getPlayer().getAccountID() + "\r\n";
            out1.write(msg.getBytes());
            out1.close();
            cm.getPlayer().getWorldGMMsg(cm.getPlayer(), "스캐상에서 복사를 시도.");
            cm.sendOk(itemsArray[select] + "#fs11#비정상적인 접근입니다.", 9062004);
            cm.dispose();
            return;
        }
        var itemsArray = getArray(menuSelect);
        말 = "#fs11#" + 검정 + "정말로 아래의 아이템을 받을텐가?\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n\r\n";
        말 += "#i" + itemsArray[select] + "##b#z" + itemsArray[select] + "##k"
        cm.sendYesNo(말);
    } else if (status == 3) {
        var itemsArray = getArray(menuSelect);
        if (cm.haveItem(2430007, 1) && cm.canHold(itemsArray[select])) {
            cm.gainItem(2430007, -1);
            cm.gainItem(itemsArray[select], 1)
            말 = "#fs11#" + 검정 + "어떤가 자네 마음에 드나? 다양한 의상이 많이 준비되어 있으니 또 들러주게~\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + itemsArray[select] + "##b#z" + itemsArray[select] + "##k"
            cm.sendOk(말);
            cm.dispose();
        } else {
            cm.sendOk("#fs11#자네는 #z2430007# 아이템이 없는거 같네.");
            cm.dispose();
            return;
        }
    }
}

function getArray(sel) {
    if (sel == 0) return itemsz0;
    if (sel == 1) return itemsz1;
    if (sel == 2) return itemsz2;
    if (sel == 3) return itemsz3;
    if (sel == 4) return itemsz4;
    if (sel == 5) return itemsz5;
    if (sel == 6) return itemsz6;
    if (sel == 7) return itemsz7;
    if (sel == 8) return itemsz8;
    if (sel == 9) return itemsz9;
}