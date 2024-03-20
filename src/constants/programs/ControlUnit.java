package constants.programs;

import client.MapleCharacter;
import client.MapleClient;
import client.stats.BuffStats;
import constants.ServerConstants;
import static constants.programs.ControlUnit.ChatList;
import database.MYSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import launch.ChannelServer;
import launch.world.WorldBroadcasting;
import launch.world.WorldCommunity;
import packet.creators.MainPacketCreator;
import packet.creators.UIPacket;
import packet.opcode.RecvPacketOpcode;
import packet.opcode.SendPacketOpcode;
import java.util.Timer;
import java.util.TimerTask;

public class ControlUnit extends javax.swing.JFrame {

    public ControlUnit() {
        initComponents();
        MYSQL.init();
        getServerInfo();
        mesoInfo();
        chatBan();
        userBan();
        this.setTitle("[ KMS 1.2.284 ]");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        Chat = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        party = new javax.swing.JList();
        jScrollPane9 = new javax.swing.JScrollPane();
        friend = new javax.swing.JList();
        jScrollPane12 = new javax.swing.JScrollPane();
        guild = new javax.swing.JList();
        jScrollPane13 = new javax.swing.JScrollPane();
        privChat = new javax.swing.JList();
        jScrollPane14 = new javax.swing.JScrollPane();
        pubChat = new javax.swing.JList();
        jScrollPane15 = new javax.swing.JScrollPane();
        give = new javax.swing.JList();
        jScrollPane16 = new javax.swing.JScrollPane();
        trade = new javax.swing.JList();
        jScrollPane17 = new javax.swing.JScrollPane();
        privShop = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        chatban = new javax.swing.JList();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        ban = new javax.swing.JList();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        exp = new javax.swing.JTextField();
        meso = new javax.swing.JTextField();
        jjj = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        drop = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        serverMessage = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        noticMessage = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        hottime = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        multiConnection = new javax.swing.JList();
        jLabel18 = new javax.swing.JLabel();
        connection = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        mesos = new javax.swing.JLabel();
        realcash = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(51, 51, 51));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel2.setText("후원포인트 지급 :");

        jTextField1.setText("닉네임 : ");

        jTextField2.setText("포인트량 : ");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("후원포인트 지급");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("메소 지급 :");

        jTextField3.setText("닉네임 : ");

        jTextField4.setText("포인트량 : ");

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("메소 지급");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText("GM 설정 : ");

        jTextField5.setText("닉네임 : ");

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("GM 설정 / 박탈");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel5.setText("아이템 지급 :");

        jTextField6.setText("닉네임 : ");

        jTextField7.setText("아이템코드,갯수");

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("아이템지급");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setText("벤 : ");

        jTextField8.setText("닉네임 : ");

        jTextField9.setText("사유 : ");

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("벤 ");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 0, 0));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("벤 해제");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setText("채팅 금지 : ");

        jTextField10.setText("닉네임 : ");

        jButton7.setBackground(new java.awt.Color(0, 0, 0));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("채팅 금지");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 0, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("금지 해제");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 0, 0));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("채팅얼리기");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 0, 0));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("모두 저장");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("모두 로그인 서버 이동");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 0, 0));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("서버 종료");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 0, 0));
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("채팅로그 정리");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("배달의민족 한나는 열한살", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 255));

        jButton16.setBackground(new java.awt.Color(0, 0, 0));
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("데이터베이스 정리");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                    .addComponent(jTextField8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField2)
                                            .addComponent(jTextField4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton8))))
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton7)
                        .addComponent(jButton8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        jTabbedPane1.addTab("관리", jPanel2);

        Chat.setForeground(new java.awt.Color(102, 0, 102));
        Chat.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(Chat);

        jScrollPane4.setViewportView(jScrollPane2);

        jTabbedPane2.addTab("일반", jScrollPane4);

        party.setForeground(new java.awt.Color(102, 0, 102));
        party.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(party);

        jTabbedPane2.addTab("파티", jScrollPane5);

        friend.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane9.setViewportView(friend);

        jTabbedPane2.addTab("친구", jScrollPane9);

        guild.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane12.setViewportView(guild);

        jTabbedPane2.addTab("길드", jScrollPane12);

        privChat.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane13.setViewportView(privChat);

        jTabbedPane2.addTab("귓속말", jScrollPane13);

        pubChat.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane14.setViewportView(pubChat);

        jTabbedPane2.addTab("확성기", jScrollPane14);

        give.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane15.setViewportView(give);

        jTabbedPane2.addTab("뿌리기", jScrollPane15);

        trade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane16.setViewportView(trade);

        jTabbedPane2.addTab("교환", jScrollPane16);

        privShop.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane17.setViewportView(privShop);

        jTabbedPane2.addTab("개인상점", jScrollPane17);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel3);

        jTabbedPane1.addTab("채팅", jScrollPane1);

        chatban.setForeground(new java.awt.Color(255, 0, 51));
        jScrollPane7.setViewportView(chatban);

        jLabel21.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 51));
        jLabel21.setText("<채팅 금지자들>");

        ban.setForeground(new java.awt.Color(51, 0, 204));
        jScrollPane6.setViewportView(ban);

        jLabel22.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 204));
        jLabel22.setText("<벤 계정들>");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 429, Short.MAX_VALUE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        jScrollPane3.setViewportView(jPanel4);

        jTabbedPane1.addTab("벤", jScrollPane3);

        jLabel8.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N

        jButton20.setBackground(new java.awt.Color(0, 0, 0));
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setText("[ 옵코드 ] 리셋");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(51, 0, 255));
        jLabel9.setText("경험치");

        exp.setText("경험치");

        meso.setText("메소");

        jjj.setForeground(new java.awt.Color(255, 51, 51));
        jjj.setText("메소");

        jLabel10.setForeground(new java.awt.Color(51, 0, 153));
        jLabel10.setText("드롭");

        drop.setText("드롭");

        jLabel11.setText("서버 메세지 (서버 상단 메세지)");

        serverMessage.setText("서버 상단 메세지");
        serverMessage.setToolTipText("");
        serverMessage.setMaximumSize(new java.awt.Dimension(6, 21));
        serverMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverMessageActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(0, 0, 0));
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("서버정보 변경");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jLabel12.setText("<공지 보내기>");

        noticMessage.setText("공지사항");
        noticMessage.setMaximumSize(new java.awt.Dimension(6, 21));
        noticMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noticMessageActionPerformed(evt);
            }
        });

        jButton21.setForeground(new java.awt.Color(0, 51, 204));
        jButton21.setText("팝업");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setForeground(new java.awt.Color(255, 0, 204));
        jButton22.setText("분홍");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 204, 51));
        jButton23.setText("쇼인포");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setForeground(new java.awt.Color(153, 0, 204));
        jButton24.setText("엔피시");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(0, 0, 0));
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.setText("핫타임 지급");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        hottime.setText("아이템 코드,갯수");

        jLabel13.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jLabel13.setText("<핫타임>");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(meso, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(drop, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(exp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(jjj)
                                .addGap(65, 65, 65)
                                .addComponent(jLabel10))))
                    .addComponent(serverMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton24))
                    .addComponent(noticMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(hottime, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton25))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(254, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jjj)
                    .addComponent(jLabel10))
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(drop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(meso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serverMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addGap(27, 27, 27)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noticMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton21)
                    .addComponent(jButton22)
                    .addComponent(jButton23)
                    .addComponent(jButton24))
                .addGap(34, 34, 34)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hottime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton25))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jScrollPane11.setViewportView(jPanel7);

        jTabbedPane1.addTab("공지", jScrollPane11);

        multiConnection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                multiConnectionMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(multiConnection);

        jLabel18.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jLabel18.setText("접속자 수 : ");

        connection.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        connection.setText("0");

        jLabel17.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 102, 0));

        jButton13.setBackground(new java.awt.Color(0, 0, 0));
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("동접 새로고침");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N

        jLabel20.setFont(new java.awt.Font("굴림", 1, 12)); // NOI18N

        mesos.setFont(new java.awt.Font("굴림", 0, 14)); // NOI18N
        mesos.setForeground(new java.awt.Color(204, 0, 204));
        mesos.setText("서버 총 메소 : ");

        realcash.setFont(new java.awt.Font("굴림", 0, 14)); // NOI18N
        realcash.setForeground(new java.awt.Color(0, 153, 102));
        realcash.setText("서버 총 후원 포인트 : ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(realcash, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mesos, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(connection)))))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jButton13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel20)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(connection))
                        .addGap(18, 18, 18)
                        .addComponent(mesos)
                        .addGap(18, 18, 18)
                        .addComponent(realcash)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                        .addGap(26, 26, 26))))
        );

        jScrollPane8.setViewportView(jPanel5);

        jTabbedPane1.addTab("동접", jScrollPane8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter hp = null;
            hp = cs.getPlayerStorage().getCharacterByName(this.jTextField1.getText());
            if (hp == null && !check) {
                check = false;
            } else if (hp != null) {
                check = true;
                hp.gainRC(Integer.parseInt(this.jTextField2.getText()));
                hp.dropMessage(5, "서버로 부터 [" + Integer.parseInt(this.jTextField2.getText()) + "] 후원 포인트를 지급 받으셨습니다.");
                JOptionPane.showMessageDialog(null, jTextField1.getText() + "님에게 후원 포인트를 지급 하였습니다.");
                초기화();
                return;
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter hp = null;
            hp = cs.getPlayerStorage().getCharacterByName(this.jTextField3.getText());
            if (hp == null && !check) {
                check = false;
            } else if (hp != null) {
                check = true;
                hp.gainMeso(Long.parseLong(this.jTextField4.getText()), false);
                hp.dropMessage(5, "서버로 부터 [" + Long.parseLong(this.jTextField4.getText()) + "] 메소를 지급 받으셨습니다.");
                JOptionPane.showMessageDialog(null, jTextField3.getText() + "님에게 후원 메소를 지급 하였습니다.");
                초기화();
                return;
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter hp = null;
            hp = cs.getPlayerStorage().getCharacterByName(this.jTextField5.getText());
            if (hp == null && !check) {
                check = false;
            } else if (hp != null) {
                if (hp.getGMLevel() <= 0) {
                    check = true;
                    hp.setGMLevel((byte) 6);
                    hp.dropShowInfo("GM설정이 되었습니다.");
                    JOptionPane.showMessageDialog(null, "GM설정을 하였습니다.");
                    초기화();
                    return;
                } else {
                    check = true;
                    hp.setGMLevel((byte) 0);
                    hp.dropShowInfo("GM설정이 해제 되었습니다.");
                    JOptionPane.showMessageDialog(null, "GM설정이 해제 되었습니다.");
                    초기화();
                    return;
                }
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter hp : cs.getPlayerStorage().getAllCharacters()) {
                hp = cs.getPlayerStorage().getCharacterByName(this.jTextField6.getText());
                if (hp == null && !check) {
                    check = false;
                } else if (hp != null) {
                    check = true;
                    String a[] = this.jTextField7.getText().split(",");
                    int itemid = Integer.parseInt(a[0]);
                    short quantity = Short.parseShort(a[1]);
                    hp.gainItem(itemid, quantity, false, -1, null);
                    hp.dropMessage(1, "아이템을 지급 받으셨습니다.");
                    JOptionPane.showMessageDialog(null, "아이템을 지급 하였습니다.");
                    초기화();
                    return;
                }
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter hp = null;
            hp = cs.getPlayerStorage().getCharacterByName(this.jTextField8.getText());
            if (hp == null && !check) {
                check = false;
            } else if (hp != null) {
                final MapleClient c = null;
                check = true;
                hp.ban(jTextField8.getText(), true, false);
                c.getSession().write(MainPacketCreator.getNPCTalk(2007, (byte) 0, "해당 계정은 지금부터 사용 할 수 없습니다.\r\n(3초 뒤 자동적으로 종료 됩니다.)", "00 00 00 00 00 00", (byte) 2));
                BanList.addElement(hp.getClient().getAccountName());
                ban.setModel(BanList);
                c.disconnect(true, (c.getLoginState() == 4 || c.getLoginState() == 5));
                c.getSession().close();
                c.getSession().close();
                JOptionPane.showMessageDialog(null, jTextField8.getText() + "유저를 밴 하였습니다.");
                초기화();
                return;
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            Connection con = MYSQL.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE name = ?");
            ps.setInt(1, 0);
            ps.setString(2, "");
            ps.setString(3, jTextField8.getText());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        BanList.removeElement(jTextField8.getText());
        ban.setModel(BanList);
        JOptionPane.showMessageDialog(null, jTextField8.getText() + "님의 게임이용 제한을 해제 하였습니다.");
        초기화();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter hp = null;
            hp = cs.getPlayerStorage().getCharacterByName(this.jTextField10.getText());
            if (hp == null && !check) {
                check = false;
            } else if (hp != null) {
                check = true;
                hp.setChatban(String.valueOf(true));
                hp.dropMessage(1, "채팅 금지를 당하였습니다.");
                JOptionPane.showMessageDialog(null, "채팅 금지를 하였습니다.");
                ChatbanList.addElement(hp.getName());
                chatban.setModel(ChatbanList);
                초기화();
                return;
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        boolean check = false;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter hp = null;
            hp = cs.getPlayerStorage().getCharacterByName(this.jTextField10.getText());
            if (hp == null && !check) {
                check = false;
            } else if (hp != null) {
                check = true;
                hp.setChatban(String.valueOf(false));
                hp.dropMessage(1, "채팅 금지가 해제 되었습니다.");
                JOptionPane.showMessageDialog(null, "채팅 금지를 해제 하였습니다.");
                ChatList.removeElement(jTextField10.getText());
                chatban.setModel(ChatList);
                초기화();
                return;
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "플레이어가 접속 중이지 않습니다.");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
//        if (!WorldCommunity.isFreeze) {
//            WorldCommunity.isFreeze = true;
//            this.jButton9.setText("채팅 녹이기");
//            WorldBroadcasting.broadcastMessage(MainPacketCreator.serverNotice(1, "채팅이 얼었습니다."));
//            JOptionPane.showMessageDialog(null, "채팅을 얼렸습니다.");
//        } else {
//            WorldCommunity.isFreeze = false;
//            this.jButton9.setText("채팅 얼리기");
//            WorldBroadcasting.broadcastMessage(MainPacketCreator.serverNotice(1, "채팅이 녹았습니다."));
//            JOptionPane.showMessageDialog(null, "채팅을 녹였습니다.");
//        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                chr.saveToDB(false, false);
            }
        }
        JOptionPane.showMessageDialog(null, "모두 저장을 완료 하였습니다.");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter player : cs.getPlayerStorage().getAllCharacters()) {
                player.getClient().disconnect(true, false); 
                player.getClient().getSession().close();
            }
        }
        JOptionPane.showMessageDialog(null, "모든 플레이어가 로그인 서버로 이동 되었습니다.");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(10, "[알림] 1분후 서버가 종료됩니다."));
        System.out.println("\r\n[알림] 서버 저장 및 종료를 시작합니다.\r\n");
        System.out.println("[알림] Saving all player's database...");
        Timer timer = new Timer();
        timer.schedule(new 캐릭터종료(), 30000);
        System.out.println("[알림] Disconnecting all player's database...");
        timer.schedule(new 서버종료(), 60000);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void multiConnectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_multiConnectionMouseClicked
        try {
            if (multiConnection.getModel() != null) {
                String name = multiConnection.getModel().getElementAt(multiConnection.getSelectedIndex()).toString();
                jTextField1.setText(name);
                jTextField3.setText(name);
                jTextField5.setText(name);
                jTextField6.setText(name);
                jTextField8.setText(name);
                jTextField10.setText(name);
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_multiConnectionMouseClicked

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        ServerConstants.showPackets = !ServerConstants.showPackets;
        SendPacketOpcode.loadOpcode();
        RecvPacketOpcode.loadOpcode();
        System.out.println("[알림] 옵코드 재설정이 완료되었습니다.");
    }//GEN-LAST:event_jButton20ActionPerformed

    private void serverMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverMessageActionPerformed

    }//GEN-LAST:event_serverMessageActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        for (int i = 0; i < ServerConstants.serverCount; i++) {
            ChannelServer.getInstance(i).setExpRate(Integer.parseInt(this.exp.getText()));
            ChannelServer.getInstance(i).setMesoRate((byte) Integer.parseInt(this.meso.getText()));
            ChannelServer.getInstance(i).setDropRate(Integer.parseInt(this.drop.getText()));
            ChannelServer.getInstance(i).setServerMessage(this.serverMessage.getText());
        }
        JOptionPane.showMessageDialog(null, "서버 정보를 변경 하였습니다.");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void noticMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noticMessageActionPerformed

    }//GEN-LAST:event_noticMessageActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        공지사항(1, this.noticMessage.getText());
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        공지사항(5, this.noticMessage.getText());
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        공지사항(-1, this.noticMessage.getText());
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        공지사항(-2, this.noticMessage.getText());
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        String b[] = this.hottime.getText().split(",");
        int itemid = Integer.parseInt(b[0]);
        short quantity = Short.parseShort(b[1]);
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                chr.gainItem(itemid, quantity, false, -1, null);
                chr.dropMessage(1, "핫타임 아이템을 지급 하였습니다.");
            }
        }
        //WorldBroadcasting.broadcastMessage(MainPacketCreator.serverNotice(1, "핫타임 아이템을 지급 받았습니다."));
        JOptionPane.showMessageDialog(null, "핫타임 아이템을 지급 하였습니다.");
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter player : cs.getPlayerStorage().getAllCharacters()) {
                if (ConnectionList.contains(player.getName())) {
                    동접제거(player.getName());
                    connection.setText(String.valueOf((int) (Integer.parseInt(connection.getText()) - 1)));
                }
            }
        }
        Timer timer = new Timer();
        timer.schedule(new 동접새로고침(), 5000);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        ChatList.clear();
        Chat.setModel(ChatList);

        PartyChatList.clear();
        party.setModel(PartyChatList);

        FriendChatList.clear();
        friend.setModel(FriendChatList);

        GuildChatList.clear();
        guild.setModel(GuildChatList);

        PrivChatList.clear();
        privChat.setModel(PrivChatList);

        PubChatList.clear();
        pubChat.setModel(PubChatList);

        GiveChatList.clear();
        give.setModel(GiveChatList);

        TradeChatList.clear();
        trade.setModel(TradeChatList);

        PrivShopChatList.clear();
        privShop.setModel(PrivShopChatList);

        JOptionPane.showMessageDialog(null, "채팅로그 정리가 완료 되었습니다..");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        String[] args = null;
          GarbageDataBaseRemover.main(args);
    }//GEN-LAST:event_jButton16ActionPerformed

    public void 초기화() {
        String name = "닉네임";
        jTextField1.setText(name);
        jTextField3.setText(name);
        jTextField5.setText(name);
        jTextField6.setText(name);
        jTextField8.setText("닉네임(해제는 계정)");
        jTextField10.setText(name);
    }

    public void 공지사항(int i, String text) {
        if (i == -1) {
            WorldBroadcasting.broadcastMessage(UIPacket.showInfo(text));
        } else if (i == -2) {
            WorldBroadcasting.broadcastMessage(MainPacketCreator.getNPCTalk(2007, (byte) 0, text, "00 00 00 00 00 00", (byte) 0));
        } else {
            WorldBroadcasting.broadcastMessage(MainPacketCreator.serverNotice(i, text));
        }
        JOptionPane.showMessageDialog(null, "[" + text + "] 내용을 모든 플레이어에게 알렸습니다.");
    }

    public static class 캐릭터종료 extends TimerTask {

        @Override
        public void run() {
            ServerConstants.isShutdown = true;
        }
    }

    public static class 서버종료 extends TimerTask {

        @Override
        public void run() {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.closeAllMerchant();
                cs.saveAllMerchant();
            }
            System.out.println("\r\n[알림] 서버를 정상적으로 저장 및 종료 하였습니다.");
            ServerConstants.isShutdown = false;
            System.exit(0);
        }
    }

    public static class 동접새로고침 extends TimerTask {

        @Override
        public void run() {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                for (MapleCharacter player : cs.getPlayerStorage().getAllCharacters()) {
                    if (!ConnectionList.contains(player.getName())) {
                        동접(player.getName());
                        connection.setText(String.valueOf((int) (Integer.parseInt(connection.getText()) + 1)));
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "동접 초기화가 완료 되었습니다...");
        }
    }

    public static void 동접(String a) {
        try {
            ConnectionList.addElement(a);
            multiConnection.setModel(ConnectionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void 동접제거(String a) {
        ConnectionList.removeElement(a);
        multiConnection.setModel(ConnectionList);
    }

    public static void getServerInfo() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            exp.setText(cserv.getExpRate() + "");
            drop.setText(cserv.getDropRate() + "");
            meso.setText(cserv.getMesoRate() + "");
            serverMessage.setText(cserv.getServerMessage());
            break;
        }
    }

    public void mesoInfo() {
        long meso = 0;
        int realcash = 0;
        try {
            ResultSet ps = MYSQL.getConnection().prepareStatement("SELECT * FROM characters WHERE gm = 0").executeQuery();
            while (ps.next()) {
                meso += ps.getLong("meso");
            }
            ps.close();
            ps = MYSQL.getConnection().prepareStatement("SELECT * FROM storages").executeQuery();
            while (ps.next()) {
                meso += ps.getLong("meso");
            }
            mesos.setText(mesos.getText() + meso + "$");
            ps = MYSQL.getConnection().prepareStatement("SELECT * FROM accounts WHERE gm = 0").executeQuery();
            while (ps.next()) {
                realcash += ps.getInt("realcash");
            }
            ps.close();
            this.realcash.setText(this.realcash.getText() + realcash + "$");
        } catch (SQLException ex) {

        }
    }

    public void userBan() {
        try {
            ResultSet ps = MYSQL.getConnection().prepareStatement("SELECT * FROM accounts WHERE gm = 0").executeQuery();
            while (ps.next()) {
                if (ps.getInt("banned") != 0) {
                    BanList.addElement(ps.getString("name"));
                }
            }
            ps.close();
            ban.setModel(BanList);
        } catch (SQLException ex) {

        }
    }

    public void chatBan() {
        String name = "";
        try {
            ResultSet rs = MYSQL.getConnection().prepareStatement("SELECT * FROM characters WHERE gm = 0").executeQuery();
            while (rs.next()) {
                if (rs.getString("chatban").equals("true")) {
                    ChatbanList.addElement(rs.getString("name"));
                }
            }
            rs.close();
            chatban.setModel(ChatbanList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ControlUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlUnit().setVisible(true);
            }
        });
    }

    public static DefaultListModel ChatList = new DefaultListModel();
    public static DefaultListModel PartyChatList = new DefaultListModel();
    public static DefaultListModel FriendChatList = new DefaultListModel();
    public static DefaultListModel GuildChatList = new DefaultListModel();
    public static DefaultListModel PrivChatList = new DefaultListModel();
    public static DefaultListModel PubChatList = new DefaultListModel();
    public static DefaultListModel GiveChatList = new DefaultListModel();
    public static DefaultListModel TradeChatList = new DefaultListModel();
    public static DefaultListModel PrivShopChatList = new DefaultListModel();
    public static DefaultListModel ConnectionList = new DefaultListModel();
    private DefaultListModel ChatbanList = new DefaultListModel();
    private DefaultListModel BanList = new DefaultListModel();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JList Chat;
    private javax.swing.JList ban;
    private javax.swing.JList chatban;
    public static javax.swing.JLabel connection;
    private static javax.swing.JTextField drop;
    private static javax.swing.JTextField exp;
    public static javax.swing.JList friend;
    public static javax.swing.JList give;
    public static javax.swing.JList guild;
    private javax.swing.JTextField hottime;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel jjj;
    private static javax.swing.JTextField meso;
    private javax.swing.JLabel mesos;
    public static javax.swing.JList multiConnection;
    private javax.swing.JTextField noticMessage;
    public static javax.swing.JList party;
    public static javax.swing.JList privChat;
    public static javax.swing.JList privShop;
    public static javax.swing.JList pubChat;
    private javax.swing.JLabel realcash;
    private static javax.swing.JTextField serverMessage;
    public static javax.swing.JList trade;
    // End of variables declaration//GEN-END:variables
}
