// 
// Decompiled by Procyon v0.5.36
// 
package org.extalia.client;

import org.extalia.server.Randomizer;
import org.extalia.tools.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum SecondaryStat implements Serializable {
    // 1.2.366 -> 1.2.1149 [+0] OK.
    IndiePad(0),
    IndieMad(1),
    IndiePdd(2),
    IndieHp(3),
    IndieHpR(4),
    IndieMp(5),
    IndieMpR(6),
    IndieAcc(7),
    IndieEva(8),
    IndieJump(9),
    IndieSpeed(10), // 1149 sniffer 10 ok.
    IndieAllStat(11),
    IndieAllStatR(12),
    IndieDodgeCriticalTime(13),
    IndieExp(14),
    IndieBooster(15), // 1149 sniffer 15 ok.
    IndieFixedDamageR(16),
    PyramidStunBuff(17),
    PyramidFrozenBuff(18),
    PyramidFireBuff(19),
    PyramidBonusDamageBuff(20),
    IndieRelaxEXP(21),
    IndieStr(22),
    IndieDex(23),
    IndieInt(24),
    IndieLuk(25),
    IndieDamR(26),
    IndieScriptBuff(27),
    IndieMaxDamageR(28),
    IndieAsrR(29),
    IndieTerR(30),
    IndieCr(31),
    IndiePddR(32),
    IndieCD(33),
    IndieBDR(34),
    IndieStatR(35),
    IndieStance(36),
    IndieIgnoreMobPdpR(37),
    IndieEmpty(38),
    IndiePadR(39),
    IndieMadR(40),
    IndieEvaR(41),
    IndieDrainHP(42),
    IndiePmdR(43), // 1149 sniffer 43 ok.
    IndieForceJump(44),
    IndieForceSpeed(45),
    IndieDamageReduce(46),
    IndieSummon(47),
    IndieReduceCooltime(48),
    IndieNotDamaged(49), // 1143 sniffer 49 ok.
    IndieJointAttack(50),
    IndieKeyDownMoving(51),
    IndieUnkIllium(52),
    IndieEvasion(53),
    IndieShotDamage(54),
    IndieSuperStance(55),
    IndieGrandCross(56),
    IndieDamReduceR(57),
    IndieWickening1(58),
    IndieWickening2(59),
    IndieWickening3(30),
    IndieWickening4(61),
    IndieFloating(62),
    IndieUnk1(63),
    IndieUnk2(64),
    IndieDarkness(65),
    IndieBlockSkill(66),
    IndieBarrier(68),
    IndieNDR(70),
    Indie_STAT_COUNT(72),
    Pad(73),
    Pdd(74),
    Mad(75),
    Acc(76),
    Eva(77),
    Craft(78),

    // 1.2.366 -> 1.2.1149 [+0] OK.
    Speed(81), // 1149 ida 81 ok.
    Jump(82),
    MagicGaurd(83),
    DarkSight(84), // 1149 sniffer 84 ok.
    Booster(85), // 1149 sniffer 85 ok.
    PowerGaurd(86),
    MaxHP(87),
    MaxMP(88),
    Invincible(89),
    SoulArrow(90),
    Stun(91), // 1149 ida 91 ok.
    Poison(92), // 1149 ida 92 ok.
    Seal(93), // 1149 ida 93 ok.
    Darkness(94), // 1149 ida 94 ok.
    ComboCounter(95), // 1149 sniffer 95 ok.
    BlessedHammer(96), // 1149 ida 96 ok.
    BlessedHammer2(97), // 1149 ida 97 ok.
    SnowCharge(98), // 1149 ida 98 ok.
    HolySymbol(99),
    MesoUp(100),
    ShadowPartner(101), // 1149 ida 101 ok.
    Steal(102),
    PickPocket(103),
    Murderous(104),
    Thaw(105),
    Weakness(106), // 1149 ida 106 ok.
    Curse(107), // 1149 ida 107 ok.
    Slow(108), // 1149 ida 108 ok.
    Morph(109), // 1149 ida 109 ok.
    Recovery(110),
    BasicStatUp(111),
    Stance(112),
    SharpEyes(113),
    ManaReflection(114),
    Attract(115), // 1149 ida 115 ok.
    NoBulletConsume(116), // 1149 ida 116 ok.
    Infinity(117), // 1149 ida 117 ok.
    AdvancedBless(118),
    Illusion(119),
    Blind(120),
    Concentration(121),
    BanMap(122), // 1149 ida 122 ok.
    MaxLevelBuff(123),
    MesoUpByItem(124),
    WealthOfUnion(125),
    RuneOfGreed(126),
    Ghost(127), // 1149 ida 127 ok.
    Barrier(128), // 1149 ida 128 ok.
    ReverseInput(129), // 1149 ida 129 ok.
    ItemUpByItem(130),
    RespectPImmune(131), // 1149 ida 131 ok.
    RespectMImmune(132), // 1149 ida 132 ok.
    DefenseAtt(133), // 1149 ida 133 ok.
    DefenseState(134), // 1149 ida 134 ok.
    DojangBerserk(135), // 1149 ida 135 ok.
    DojangInvincible(136),
    DojangShield(137), // 1149 ida 137 ok.
    SoulMasterFinal(138),
    WindBreakerFinal(139), // 1149 ida 139 ok.
    ElementalReset(140),
    HideAttack(141), // 1149 ida 141 ok.
    EventRate(142),
    AranCombo(143),
    AuraRecovery(144),
    UnkBuffStat1(145),
    BodyPressure(146),
    RepeatEffect(147), // 1149 ida 147 ok.
    ExpBuffRate(148),
    StopPortion(149), // 1149 ida 149 ok.
    StopMotion(150), // 1149 ida 150 ok.
    Fear(151), // 1149 ida 151 ok.
    HiddenPieceOn(152), // 1149 ida 152 ok.
    MagicShield(153), // 1149 ida 153 ok.
    SoulStone(154),
    Flying(156),
    Frozen(157), // 1149 ida 157 ok.
    AssistCharge(158),
    Enrage(159),
    DrawBack(160), // 1149 ida 160 ok.
    NotDamaged(161), // 1149 ida 161 ok.
    FinalCut(162), // 1149 ida 162 ok.
    HowlingParty(163),
    BeastFormDamage(164),

    // 1.2.366 -> 1.2.1149 [+0] OK.
    Dance(165), // 1149 ida 165 ok.
    EnhancedMaxHp(166),
    EnhancedMaxMp(167),
    EnhancedPad(168), // 1149 sniffer 168 ok.
    EnhancedMad(169),
    EnhancedPdd(170), // 1149 sniffer 170 ok.
    PerfectArmor(171),
    UnkBuffStat2(172),
    IncreaseJabelinDam(173),
    PinkbeanMinibeenMove(174),
    Sneak(175),
    Mechanic(176), // 1149 sniffer 176 ok.
    BeastFormMaxHP(177),
    DiceRoll(178),
    BlessingArmor(179), // 1149 ida 179 ok.
    DamR(180),
    TeleportMastery(181), // 1149 ida 181 ok.
    CombatOrders(182),
    Beholder(183), // 1149 ida 183 ok.
    DispelItemOption(184),
    Inflation(185), // 1149 ida 185 ok.
    OnixDivineProtection(186),
    Web(187), // 1149 ida 187 ok.
    Bless(188), // 1149 sniffer 188 ok.
    TimeBomb(189), // 1149 ida 189 ok.
    DisOrder(190), // 1149 ida 190 ok.
    Thread(191), // 1149 ida 191 ok.
    Team(192), // 1149 ida 192 ok.
    Explosion(193), // 1149 ida 193 ok.
    BuffLimit(194),
    STR(195),
    INT(196),
    DEX(197),
    LUK(198),
    DispelByField(199),
    DarkTornado(200), // 1149 ida 200 ok.
    PVPDamage(201),
    PvPScoreBonus(202),
    PvPInvincible(203),
    PvPRaceEffect(204), // 1149 ida 204 ok.
    WeaknessMdamage(205), // 1149 ida 205 ok.
    Frozen2(206), // 1149 ida 206 ok.
    PvPDamageSkill(207),
    AmplifyDamage(208), // 1149 ida 208 ok.
    Shock(209), // 1149 ida 209 ok.
    InfinityForce(210),
    IncMaxHP(211),
    IncMaxMP(212),
    HolyMagicShell(213),
    KeyDownTimeIgnore(214),
    ArcaneAim(215),
    MasterMagicOn(216),
    Asr(217),
    Ter(218),
    DamAbsorbShield(219), // 1149 ida 219 ok.
    DevilishPower(220), // 1149 ida 220 ok.
    Roulette(221),
    SpiritLink(222), // 1149 ida 222 ok.
    AsrRByItem(223),
    Event(224), // 1149 ida 224 ok.
    CriticalIncrease(225), // 패시브스킬로 바뀜.
    DropItemRate(226),
    DropRate(227),
    ItemInvincible(228),
    Awake(229),
    ItemCritical(230),
    ItemEvade(231),
    Event2(232), // 1149 ida 232 ok.
    DrainHp(233),
    IncDefenseR(234),
    IncTerR(235),
    IncAsrR(236),
    DeathMark(237), // 1149 ida 237 ok.
    Infiltrate(238),
    Lapidification(239), // 1149 ida 239 ok.
    VenomSnake(240), // 1149 ida 240 ok.
    CarnivalAttack(241),
    CarnivalDefence(242),
    CarnivalExp(243),
    SlowAttack(244),
    PyramidEffect(245), // 1149 ida 245 ok.
    UnkBuffStat3(246),
    KeyDownMoving(247), // 1149 ida 247 ok.
    IgnoreTargetDEF(248), // 1149 ida 248 ok.
    UNK_249(249), // 1149 ida 249 ok.

    // 1.2.366 -> 1.2.1149 [+0] OK.
    ReviveOnce(250),
    Invisible(251), // 1149 ida 251 ok.
    EnrageCr(252),
    EnrageCrDamMin(253),
    Judgement(254), // 1149 ida 254 ok.
    DojangLuckyBonus(255),
    PainMark(256), // 1149 ida 256 ok.
    Magnet(257), // 1149 ida 257 ok.
    MagnetArea(258), // 1149 ida 258 ok.
    GuidedArrow(259), // 1149 ida 259 ok.
    UnkBuffStat4(260), // 1149 ida 260 ok.
    BlessMark(261), // 1149 ida 261 ok.
    BonusAttack(262), // 1149 ida 262 ok.
    UnkBuffStat5(263), // 1149 ida 263 ok.
    FlowOfFight(264),
    ShadowMomentum(265),
    GrandCrossSize(266), // 1149 sniffer 266 ok.
    LuckOfUnion(267),
    PinkBeanFighting(268),
    VampDeath(270), // 1149 ida 270 ok.
    BlessingArmorIncPad(271),
    KeyDownAreaMoving(272), // 1149 ida 272 ok.
    Larkness(273), // 1149 ida 273 ok.
    StackBuff(274), // 1149 ida 274 ok.

    // 1.2.366 -> 1.2.1149 [+0] OK.
    AntiMagicShell(275), // 1149 sniffer 275 ok.
    LifeTidal(276),
    HitCriDamR(277),
    SmashStack(278), // 1149 ida 278 ok.
    RoburstArmor(279),
    ReshuffleSwitch(280), // 1149 ida 280 ok.
    SpecialAction(281), // 1149 ida 281 ok.
    VampDeathSummon(282), // 1149 ida 282 ok.
    StopForceAtominfo(283), // 1149 ida 283 ok.
    SoulGazeCriDamR(284), // 1149 ida 284 ok.
    Affinity(285),
    PowerTransferGauge(286), // 1149 ida 286 ok.
    AffinitySlug(287), // 1149 ida 287 ok.
    Trinity(288),
    IncMaxDamage(289),
    BossShield(290),
    MobZoneState(291), // 1149 ida 291 ok.
    GiveMeHeal(292), // 1149 ida 292 ok.
    TouchMe(293), // 1149 ida 293 ok.
    Contagion(294), // 1149 ida 294 ok.
    ComboUnlimited(295), // 1149 ida 295 ok.
    SoulExalt(296), // 1149 ida 296 ok.
    IgnorePCounter(297), // 1149 ida 297 ok.
    IgnoreAllCounter(298), // 1149 ida 298 ok.
    IgnorePImmune(299), // 1149 ida 299 ok.
    IgnoreAllImmune(300), // 1149 ida 300 ok.
    UnkBuffStat6(301), // 1149 ida 301 ok.
    FireAura(302), // 1149 ida 302 ok.
    VengeanceOfAngel(303),
    HeavensDoor(304), // 1149 ida 304 ok.
    Preparation(305),
    BullsEye(306),
    IncEffectHPPotion(307),
    IncEffectMPPotion(308),
    BleedingToxin(309), // 1149 ida 309 ok.
    IgnoreMobDamR(310), // 1149 ida 310 ok.
    Asura(311), // 1149 ida 311 ok.
    MegaSmasher(312), // 1149 ida 312 ok.
    FlipTheCoin(313),
    UnityOfPower(314), // 1149 ida 314 ok.
    Stimulate(315), // 1149 ida 315 ok.
    ReturnTeleport(316),
    DropRIncrease(317),
    IgnoreMobPdpR(318),
    BdR(319),
    CapDebuff(320), // 1149 ida 320 ok.

    // 1.2.366 -> 1.2.1149 [+0] OK.
    Exceed(321),
    DiabloicRecovery(322),
    FinalAttackProp(323),
    ExceedOverload(324),
    OverloadCount(325), // 1149 sniffer 325 ok.
    Buckshot(326),
    FireBomb(327), // 1149 ida 327 ok.
    HalfstatByDebuff(328),
    SurplusSupply(329), // 1149 ida 329 ok.
    SetBaseDamage(330),
    EvaR(331),
    NewFlying(332), // 1149 ida 332 ok.
    AmaranthGenerator(333), // 1149 ida 333 ok.
    OnCapsule(334), // 1149 ida 334 ok.
    CygnusElementSkill(335), // 1149 ida 335 ok.
    StrikerHyperElectric(336), // 1149 ida 336 ok.
    EventPointAbsorb(337), // 1149 ida 337 ok.
    EventAssemble(338), // 1149 ida 338 ok.
    StormBringer(339),
    AccR(340),
    DexR(341),
    Translucence(342), // 1149 ida 342 ok.
    PoseType(343), // 1149 ida 343 ok.
    CosmicForge(344), // 1149 sniffer 344 ok.
    ElementSoul(345), // 1149 sniffer 345 ok.
    CosmicOrb(346),
    GlimmeringTime(347), // 1149 sniffer 347 ok.
    SolunaTime(348),
    WindWalk(349),
    SoulMP(350),
    FullSoulMP(351), // 1149 ida 351 ok.
    SoulSkillDamageUp(352),
    ElementalCharge(353), // 1149 ida 353 ok.
    Listonation(354),
    CrossOverChain(355),
    ChargeBuff(356),
    ReincarnationFull(357), // 1149 ida 357 ok.
    Reincarnation(358),
    ReincarnationAccept(359), // 1149 sniffer 359 ok.

    // 1.2.366 -> 1.2.1149 [+0] OK.
    ChillingStep(360),  // 1149 sniffer 360 ok.
    DotBasedBuff(361),
    BlessingAnsanble(362),
    ComboCostInc(363),
    NaviFlying(364), // 1149 ida 364 ok.
    QuiverCatridge(365), // 1149 ida 365 ok.
    AdvancedQuiver(366),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    ImmuneBarrier(367), // 1149 sniffer 367 ok.
    ArmorPiercing(368), // 1149 sniffer 368 ok.
    CardinalMark(369),
    QuickDraw(370), // 1149 sniffer 370 ok.
    BowMasterConcentration(371),
    TimeFastABuff(372),
    TimeFastBBuff(373),
    GatherDropR(374),
    AimBox2D(375),
    TrueSniping(376), // 1149 ida 376 ok.
    DebuffTolerance(377),
    UnkBuffStat8(378),
    DotHealHPPerSecond(379),
    DotHealMPPerSecond(380),
    SpiritGuard(381), // 1149 sniffer 381 ok.
    PreReviveOnce(382),
    SetBaseDamageByBuff(383),
    LimitMP(384),
    ReflectDamR(385),
    ComboTempest(386), // 1149 ida 386 ok.
    MHPCutR(387),
    MMPCutR(388),
    SelfWeakness(389),
    ElementDarkness(390), // 1149 sniffer 390 ok.
    FlareTrick(391),
    Ember(392),
    Dominion(393),
    SiphonVitality(394),
    DarknessAscension(395),
    BossWaitingLinesBuff(396),
    DamageReduce(397),
    ShadowServant(398), // 1149 sniffer 398 ok.
    ShadowIllusion(399),
    KnockBack(400),
    IgnisRore(401),
    ComplusionSlant(402), // 1149 ida 402 ok.
    JaguarSummoned(403), // 1149 ida 403 ok.
    JaguarCount(404),
    SSFShootingAttack(405),
    DevilCry(406),
    ShieldAttack(407),
    DarkLighting(408), // 1149 ida 408 ok.
    AttackCountX(409), // 1149 ida 409 ok.
    BMageDeath(410),
    BombTime(411), // 1149 ida 411 ok.
    NoDebuff(412),
    BattlePvP_Mike_Shield(413),
    BattlePvP_Mike_Bugle(414),
    AegisSystem(415),
    SoulSeekerExpert(416),
    HiddenPossession(417),
    ShadowBatt(418), // 1149 sniffer 418 ok.
    MarkofNightLord(419),
    WizardIgnite(420),
    FireBarrier(421), // 1149 ida 421 ok.
    ChangeFoxMan(422),
    HolyUnity(423), // 1149 ida 423 ok.
    DemonFrenzy(424), // 1149 sniffer 424 ok.
    ShadowSpear(425), // 1149 sniffer 425 ok.
    DemonDamageAbsorbShield(426), // 1149 sniffer 426 ok.
    Ellision(427), // 1149 sniffer 427 ok.
    QuiverFullBurst(428), // 1149 sniffer 428 ok.
    LuminousPerfusion(429),
    WildGrenadier(430),
    GrandCross(432), // 1149 sniffer 432 ok.

    // 1.2.366 -> 1.2.1149 [-1] 추측.
    BattlePvP_Helena_Mark(433), // 1149 ida 433 ok.
    BattlePvP_Helena_WindSpirit(434),
    BattlePvP_LangE_Protection(435), // 1149 ida 435 ok.
    BattlePvP_LeeMalNyun_ScaleUp(436),
    BattlePvP_Revive(437),
    PinkbeanAttackBuff(438),
    PinkbeanRelax(439),
    PinkbeanRollingGrade(440), // 1149 ida 440 ok.

    // 1.2.366 -> 1.2.1149 [-1] 추측.
    PinkbeanYoYoStack(442),
    RandAreaAttack(443),
    NextAttackEnhance(444),
    BeyondNextAttackProb(445),
    AranCombotempastOption(446),
    NautilusFinalAttack(447),
    ViperTimeLeap(448),
    RoyalGuardState(449),
    RoyalGuardPrepare(450),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    MichaelSoulLink(451), // 1149 sniffer 451 ok.
    MichaelProtectofLight(452),
    TryflingWarm(453),
    AddRange(454),
    KinesisPsychicPoint(455), // 1149 ida 455 ok.
    KinesisPsychicOver(456),
    KinesisIncMastery(457),
    KinesisPsychicEnergeShield(458), // 1149 ida 458 ok.
    BladeStance(459), // 1149 ida 459 ok.
    DebuffActiveHp(460),
    DebuffIncHp(461),
    MortalBlow(462),
    SoulResonance(463),
    Fever(464), // 1149 ida 464 ok.
    SikSin(465),
    TeleportMasteryRange(466),
    FixCooltime(467),
    IncMobRateDummy(468),
    AdrenalinBoost(469), // 1149 sniffer 469 ok.
   // AranSmashSwing(470), // 1149 이거 삭제된듯.

    // 1.2.366 -> 1.2.1149 [-2] OK.
    AranDrain(470), // 1149 sniffer 470 ok.
    AranBoostEndHunt(471),
    HiddenHyperLinkMaximization(472),
    RWCylinder(473),
    RWCombination(474),
    RWUnk(475), // 1149 ida 475 ok.
    RwMagnumBlow(476), // 1149 ida 476 ok.
    RwBarrier(477), // 1149 ida 477 ok.
    RWBarrierHeal(478),
    RWMaximizeCannon(479),
    RWOverHeat(480),
    UsingScouter(481),
    RWMovingEvar(482),
    Stigma(483), // 1149 ida 483 ok.
    InstallMaha(484), // 1149 sniffer 484 ok.
    CooldownHeavensDoor(485), // 1149 sniffer 485 ok.

    // 1.2.366 -> 1.2.1149 [-3] OK.
    CooldownRune(486), // 1149 sniffer 486 ok.
    PinPointRocket(487), // 1149 sniffer 487 ok.
    Transform(488), // 1149 ida 488 ok.
    EnergyBurst(489), // 1149 ida 489 ok.
    Striker1st(490), // 1149 ida 490 ok.
    BulletParty(491), // 1149 sniffer 491 ok.
    SelectDice(492), // 1149 sniffer 492 ok.
    Pray(493), // 1149 sniffer 493 ok.
    ChainArtsFury(494), // 1149 sniffer 494 ok.
    DamageDecreaseWithHP(495),
    PinkbeanYoYoAttackStack(496),
    AuraWeapon(497), // 1149 sniffer 497 ok.
    OverloadMana(498), // 1149 sniffer 498 ok.
    RhoAias(499), // 1149 ida 499 ok.
    PsychicTornado(500), // 1149 ida 500 ok.
    SpreadThrow(501),
    HowlingGale(502),
    VMatrixStackBuff(503),
    MiniCannonBall(504),
    ShadowAssult(505),
    MultipleOption(506),
    UnkBuffStat15(507),
    BlitzShield(508), // 1149 ida 508 ok.
    SplitArrow(509),
    FreudsProtection(510), // 1149 ida 510 ok.
    Overload(511), // 1149 ida 511 ok.
    Spotlight(512), // 1149 ida 512 ok.
    KawoongDebuff(513), // 1149 ida 513 ok.
    WeaponVariety(514),
    GloryWing(515), // 1149 ida 515 ok.
    ShadowerDebuff(516),
    OverDrive(517), // 1149 ida 517 ok.
    Etherealform(518), // 1149 ida 518 ok.
    ReadyToDie(519), // 1149 sniffer 519 ok.

    // 1.2.366 -> 1.2.1149 [-2] OK.
    Oblivion(520), // 1149 sniffer 520 ok.
    CriticalReinForce(521), // 1149 sniffer 521 ok.
    CurseOfCreation(522), // 1149 ida 522 ok.
    CurseOfDestruction(523), // 1149 ida 523 ok.
    BlackMageDebuff(524), // 1149 ida 524 ok.
    BodyOfSteal(525), // 1149 sniffer 525
    PapulCuss(526), // 1149 ida 526 ok.
    PapulBomb(527), // 1149 ida 527 ok.
    HarmonyLink(528), // 1149 sniffer 528 ok.
    FastCharge(529), // 1149 ida 529 ok.
    UnkBuffStat20(530),
    CrystalBattery(531),
    Deus(532),
    UnkBuffStat21(533),
    BattlePvP_Rude_Stack(534), // 1149 ida 534 ok.
    UnkBuffStat23(535),
    UnkBuffStat24(536),
    UnkBuffStat25(537),

    // 1.2.366 -> 1.2.1149 [-2] OK.
    SpectorGauge(538), // 1149 sniffer 538 ok.
    SpectorTransForm(539), // 1149 sniffer 539 ok.
    PlainBuff(540),
    ScarletBuff(541),
    GustBuff(542),
    AbyssBuff(543),
    ComingDeath(544), // 1149 ida 544 ok.
    FightJazz(545),
    ChargeSpellAmplification(546),
    InfinitySpell(547),
    MagicCircuitFullDrive(548),
    LinkOfArk(549),
    MemoryOfSource(550),
    UnkBuffStat26(551),
    WillPoison(552), // 1149 ida 552 ok.
    UnkBuffStat27(553),
    UnkBuffStat28(554),
    CooltimeHolyMagicShell(555),
    Striker3rd(556), // 1149 sniffer 556 ok.
    ComboInstict(557),
    ResonateUltimatum(558), // 1149 sniffer 558 ok.
    WindWall(559), // 1149 sniffer 559 ok.
    UnkBuffStat29(560),

    SwordOfSoulLight(561), // 1149 sniffer 561 ok.
    MarkOfPhantomStack(562), // 1149 sniffer 562 ok.
    MarkOfPhantomDebuff(563), // 1149 sniffer 563 ok.
    UnkBuffStat30(565),
    UnkBuffStat31(566),
    UnkBuffStat32(567),
    UnkBuffStat33(568),
    UnkBuffStat34(569),
    EventSpecialSkill(570),
    PmdReduce(571),
    ForbidOpPotion(572),
    ForbidEquipChange(573),
    // 여기 이상한데
    // 1.2.366 -> 1.2.1149 [-1] OK.
    YalBuff(573), // 1149 ida 573 ok.
    IonBuff(574), // 1149 ida 574 ok.
    UnkBuffStat35(575),
    DefUp(576),
    Protective(577), // 1149 ida 577 ok.
    BloodFist(578),
    BattlePvP_Wonky_ChargeA(579), // 1149 ida 579 ok.
    UNK_580(580), // 1149 ida 580 ok.
    BattlePvP_Wonky_Charge(582), // 1143 사라진듯
    BattlePvP_Wonky_Awesome(581), // 1149 ida 581 ok.
    UnkBuffStat42(582), // 1149 ida 582 ok.
    UnkBuffStat43(583),
    UnkBuffStat44(584), // 1149 ida 584 ok.
    UNK_585(585), // 1149 ida 585 ok.
    UNK_586(586),
    Bless5th(588), // 1149 sniffer 589 ok.
    Bless5th2(589), // 1149 new sniffer 589 ok.

    // 1.2.366 -> 1.2.1149 [-1] OK.
    PinkBeanMatroCyca(590), // 1149 ida 590 ok.
    UnkBuffStat46(591),
    UnkBuffStat47(592),
    UnkBuffStat48(593),
    UnkBuffStat49(594),
    UnkBuffStat50(595), // 1149 ida 595 ok.
    PapyrusOfLuck(596),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    HoyoungThirdProperty(597),
    TidalForce(598),
    Alterego(599),
    AltergoReinforce(600), // 1149 ida 600 ok.
    ButterflyDream(601),
    Sungi(602), // 1149 sniffer 602 ok.
    SageWrathOfGods(603), // 1149 sniffer 603 ok.
    EmpiricalKnowledge(604),
    UnkBuffStat52(605),
    UnkBuffStat53(606), // 1149 ida 606 ok.
    Graffiti(607), // 1149 ida 607 ok.
    DreamDowon(608),
    WillofSwordStrike(609),
    WillofSword(610),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    AdelGauge(611),
    Creation(612), // 1149 sniffer 612 ok.
    Dike(613), // 1149 sniffer 613 ok.
    Wonder(614),
    Restore(615),
    Novility(616), // 1149 ida 616 ok.
    AdelResonance(617),
    RuneOfPure(618), // 1149 ida 618 ok.
    RuneOfTransition(619), // 1149 ida 619 ok.
    DuskDarkness(620), // 1149 ida 620 ok.
    YellowAura(621), // 1149 sniffer 621 ok.
    DrainAura(622), // 1149 sniffer 622 ok.
    BlueAura(623), // 1149 sniffer 623 ok.
    DarkAura(624), // 1149 sniffer 624 ok.
    DebuffAura(625), // 1149 sniffer 625 ok.
    UnionAura(626), // 1149 sniffer 626 ok.
    IceAura(627), // 1149 ida 627 ok.
    KnightsAura(628), // 1149 ida 628 ok.
    ZeroAuraStr(629), // 1149 ida 629 ok.
    IncarnationAura(630), // 1149 sniffer 630 ok.

    // 1.2.366 -> 1.2.1149 [-1] OK.
    AdventOfGods(631),
    Revenant(632),
    RevenantDamage(633),
    SilhouetteMirage(634), // 1149 ida 634 ok.
    BlizzardTempest(635), // 1149 ida 635 ok.
    PhotonRay(636), // 1149 ida 636 ok.
    AbyssalLightning(637),
    Striker4th(638),
    RoyalKnights(639),
    SalamanderMischief(640),
    LawOfGravity(641),
    RepeatingCrossbowCatridge(642),
    CrystalGate(643), // 1149 ida 643 ok.
    ThrowBlasting(644),
    SageElementalClone(645),
    DarknessAura(646), // 1149 ida 646 ok.
    WeaponVarietyFinale(647),
    LiberationOrb(648),
    LiberationOrbActive(649), // 1149 ida 649 ok.
    EgoWeapon(650),
    RelikUnboundDischarge(651),
    MoraleBoost(652),
    AfterImageShock(653),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    Malice(654), // 1149 sniffer 654 ok.
    Possession(655),
    DeathBlessing(656),
    ThanatosDescent(657), // 1149 sniffer 657 ok.
    RemainIncense(658),
    GripOfAgony(659),
    DragonPang(660), // 1149 sniffer 660 ok.
    SerenDebuffs(661),
    SerenDebuff(662),
    SerenDebuffUnk(663),
    PriorPryperation(664),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    AdrenalinBoostActive(671), // 1149 sniffer 671 ok.
    UNK_672(672), // 1149 ida 672 ok.
    YetiAnger(673),
    YetiAngerMode(674), // 1149 ida 674 ok.
    YetiSpicy(675),
    YetiFriendsPePe(676),
    PinkBeanMagicShow(677),

    // 1.2.366 -> 1.2.1149 [-1] OK.
    용맥_읽기(679), // 1149 sniffer 679 ok.
    산의씨앗(680), // 1149 sniffer 680 ok.
    산_무등(681),
    흡수_강(682), // 1149 ida 682 ok.
    흡수_바람(683),
    흡수_해(684),
    자유로운용맥(685), // 1149 sniffer 685 ok.
    Lotus(687),
    NatureFriend(688), // 1149 sniffer 688 ok.

    // 1.2.366 -> 1.2.1149 [-2] OK.
    SeaSerpent(691), // 1149 sniffer 691 ok.
    SerpentStone(692),
    SerpentScrew(693),
    Cosmos(694),
    UNK_696(696), // 1149 ida 696 ok.
    UNK_698(698),
    HolyWater(699),
    Triumph(700),
    FlashMirage(701), // 1149 sniffer 701 ok.
    HolyBlood(702), // 1149 sniffer 702 ok.
    OrbitalExplosion(703), // 1149 sniffer 703 ok
    PhoenixDrive(704), // 1149 sniffer 704 ok.
    UNK_705(705), // 1149 ida 705 ok.
    UNK_706(706), // 1149 ida 706 ok.
    ElementalKnight(708), // 1149 sniffer 708 ok.
    EquilibriumLiberation(709), // 1149 sniffer 709 ok.
    SummonChakri(710), // 1149 sniffer 710 ok.

    VoidBurst(712), // 1149 sniffer 712 ok.

    // 1.2.366 -> 1.2.1149 [+4] OK.
    EnergyCharged(714),
    DashJump(715), // 1149 sniffer 715 ok.
    DashSpeed(716), // 1149 sniffer 716 ok.
    RideVehicle(717),
    PartyBooster(718),
    GuidedBullet(719),
    Undead(720),
    RideVehicleExpire(721),
    RelikGauge(722),
    Grave(723),
    CountPlus1(724);

    private static final long serialVersionUID = 0L;
    private int buffstat;
    private int first;
    private boolean stacked;
    private int disease;
    private int flag;
    private int x;
    private int y;

    private SecondaryStat(final int flag) {
        this.stacked = false;
        this.buffstat = 1 << 31 - flag % 32;
        this.setFirst(31 - (byte) Math.floor(flag / 32));
        this.setStacked(this.name().startsWith("Indie") || this.name().startsWith("Pyramid"));
        this.setFlag(flag);
    }

    private SecondaryStat(final int flag, final int disease) {
        this.stacked = false;
        this.buffstat = 1 << 31 - flag % 32;
        this.setFirst(31 - (byte) Math.floor(flag / 32));
        this.setStacked(this.name().startsWith("Indie") || this.name().startsWith("Pyramid"));
        this.setFlag(flag);
        this.disease = disease;
    }

    private SecondaryStat(final int flag, final int first, final int disease) {
        this.stacked = false;
        this.buffstat = 1 << 31 - flag % 32;
        this.setFirst(first);
        this.setFlag(flag);
        this.disease = disease;
    }

    public final int getPosition() {
        return this.getFirst();
    }

    public final int getPosition(final boolean stacked) {
        if (!stacked) {
            return this.getFirst();
        }
        switch (this.getFirst()) {
            case 16: {
                return 0;
            }
            case 15: {
                return 1;
            }
            case 14: {
                return 2;
            }
            case 13: {
                return 3;
            }
            case 12: {
                return 4;
            }
            case 11: {
                return 5;
            }
            case 10: {
                return 6;
            }
            case 9: {
                return 7;
            }
            case 8: {
                return 8;
            }
            case 7: {
                return 9;
            }
            case 6: {
                return 10;
            }
            case 5: {
                return 11;
            }
            case 4: {
                return 12;
            }
            case 3: {
                return 13;
            }
            case 2: {
                return 14;
            }
            case 1: {
                return 15;
            }
            case 0: {
                return 16;
            }
            default: {
                return 0;
            }
        }
    }

    public final int getValue() {
        return this.getBuffstat();
    }

    public final boolean canStack() {
        return this.isStacked();
    }

    public int getDisease() {
        return this.disease;
    }

    public int getX() {
        return this.x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public static final SecondaryStat getByFlag(final int flag) {
        for (final SecondaryStat d : values()) {
            if (d.getFlag() == flag) {
                return d;
            }
        }
        return null;
    }

    public static final SecondaryStat getBySkill(final int skill) {
        for (final SecondaryStat d : values()) {
            if (d.getDisease() == skill) {
                return d;
            }
        }
        return null;
    }

    public static final List<SecondaryStat> getUnkBuffStats() {
        final List<SecondaryStat> stats = new ArrayList<SecondaryStat>();
        for (final SecondaryStat d : values()) {
            if (d.name().startsWith("UnkBuff")) {
                stats.add(d);
            }
        }
        return stats;
    }

    public static final SecondaryStat getRandom() {
        SecondaryStat dis = null;
        Block_1:
        while (true) {
            final SecondaryStat[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                dis = values[i];
                if (Randomizer.nextInt(values().length) == 0) {
                    break Block_1;
                }
            }
        }
        return dis;
    }

    public static boolean isEncode4Byte(final Map<SecondaryStat, Pair<Integer, Integer>> statups) {
        final SecondaryStat[] array;
        final SecondaryStat[] stats = array = new SecondaryStat[]{SecondaryStat.CarnivalDefence, SecondaryStat.SpiritLink, SecondaryStat.DojangLuckyBonus, SecondaryStat.SoulGazeCriDamR, SecondaryStat.PowerTransferGauge, SecondaryStat.ReturnTeleport, SecondaryStat.ShadowPartner, SecondaryStat.SetBaseDamage, SecondaryStat.QuiverCatridge, SecondaryStat.ImmuneBarrier, SecondaryStat.NaviFlying, SecondaryStat.Dance, SecondaryStat.DotHealHPPerSecond, SecondaryStat.SetBaseDamageByBuff, SecondaryStat.MagnetArea, SecondaryStat.MegaSmasher, SecondaryStat.RwBarrier, SecondaryStat.VampDeath, SecondaryStat.RideVehicle, SecondaryStat.RideVehicleExpire, SecondaryStat.Protective, SecondaryStat.BlitzShield, SecondaryStat.UnkBuffStat2, SecondaryStat.HolyUnity, SecondaryStat.BattlePvP_Rude_Stack};
        for (final SecondaryStat stat : array) {
            if (statups.containsKey(stat)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSpecialBuff() {
        switch (this) {
            case EnergyCharged:
            case DashSpeed:
            case DashJump:
            case RideVehicle:
            case PartyBooster:
            case GuidedBullet:
            case Undead:
            case RideVehicleExpire:
            case RelikGauge:
            case Grave: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(final int flag) {
        this.flag = flag;
    }

    public boolean isItemEffect() {
        switch (this) {
            case DropItemRate:
            case ItemUpByItem:
            case MesoUpByItem:
            case ExpBuffRate:
            case WealthOfUnion:
            case LuckOfUnion: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public boolean SpectorEffect() {
        switch (this) {
            case SpectorGauge:
            case SpectorTransForm:
            case PlainBuff:
            case ScarletBuff:
            case GustBuff:
            case AbyssBuff: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int getBuffstat() {
        return this.buffstat;
    }

    public void setBuffstat(final int buffstat) {
        this.buffstat = buffstat;
    }

    public int getFirst() {
        return this.first;
    }

    public void setFirst(final int first) {
        this.first = first;
    }

    public boolean isStacked() {
        return this.stacked;
    }

    public void setStacked(final boolean stacked) {
        this.stacked = stacked;
    }
}
