package jp.mkserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MainGUI extends JPanel {

    GUIManager gui;
    JLabel titlelabel;

    MainGUI (GUIManager gui){
        this.gui = gui;
        setBackground(Color.cyan);
        setName("MainGUI");
        URL urls=getClass().getClassLoader().getResource("Title.png");
        Icon title =new ImageIcon(urls);
        titlelabel = new JLabel(title);
        titlelabel.setOpaque(false);
        titlelabel.setLayout(new BorderLayout());
        JButton button1 = new JButton("チャットを始める");
        button1.addActionListener(new ClickAction1(this));
        JButton button2 = new JButton("アップデートを行う");
        button2.addActionListener(new ClickAction2(this));
        JButton button4 = new JButton("設定画面を開く");
        button4.addActionListener(new ClickAction4(this));
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout());
        jp.add(button2);
        jp.setOpaque(false);
        JPanel jb = new JPanel();
        jb.setLayout(new FlowLayout());
        jb.add(button1);
        jb.setOpaque(false);
        JPanel js = new JPanel();
        js.setLayout(new FlowLayout());
        js.add(button4);
        js.setOpaque(false);
        JPanel j = new JPanel();
        j.setLayout(new BorderLayout());
        j.add(js,BorderLayout.CENTER);
        j.add(jp,BorderLayout.SOUTH);
        j.setOpaque(false);
        JPanel jj = new JPanel();
        jj.setLayout(new BorderLayout());
        jj.add(jb,BorderLayout.CENTER);
        jj.add(j,BorderLayout.SOUTH);
        jj.setOpaque(false);
        JPanel jpp = new JPanel();
        jpp.setLayout(new BorderLayout());
        jpp.setOpaque(false);
        jpp.add(jj,BorderLayout.SOUTH);
        JPanel jps = new JPanel();
        jps.setLayout(new BorderLayout());
        jps.setOpaque(false);
        JLabel creator = new JLabel();
        creator.setText("Created by Mr_IK");
        creator.setOpaque(false);
        creator.setForeground(Color.WHITE);
        JLabel ver = new JLabel();
        ver.setText("MCchatManager v1.3");
        ver.setOpaque(false);
        ver.setForeground(Color.WHITE);
        jps.add(jpp,BorderLayout.NORTH);
        jps.add(ver,BorderLayout.WEST);
        jps.add(creator,BorderLayout.EAST);
        URL urlss=getClass().getClassLoader().getResource("MCchat.png");
        Icon titles =new ImageIcon(urlss);
        titlelabel.add(new JLabel(titles),BorderLayout.NORTH);
        titlelabel.add(jps,BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        setOpaque(false);
        add(titlelabel,BorderLayout.CENTER);
    }

    class ClickAction1 implements ActionListener {

        MainGUI gui;
        public ClickAction1(MainGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.playClickSound();
            gui.gui.setviewChat();
        }
    }

    class ClickAction2 implements ActionListener {

        MainGUI gui;
        public ClickAction2(MainGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.playClickSound();
            double ver = Updater.jsontoStringVersion();
            if(Updater.thisversion < ver){
                Updater.download();
                JOptionPane.showMessageDialog(gui, "ダウンロードが完了しました。");
            }else{
                JOptionPane.showMessageDialog(gui, "このMCchatManagerは最新です。");
            }

        }
    }

    class ClickAction4 implements ActionListener {

        MainGUI gui;
        public ClickAction4(MainGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.playClickSound();
            gui.gui.setviewSettings();
        }
    }


}
