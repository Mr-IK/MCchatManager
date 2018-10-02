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
        JButton button3 = new JButton("プラグインGUIを開く");
        button3.addActionListener(new ClickAction3(this));
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout());
        jp.add(button2);
        jp.add(button3);
        jp.setOpaque(false);
        JPanel jb = new JPanel();
        jb.setLayout(new FlowLayout());
        jb.add(button1);
        jb.setOpaque(false);
        JPanel j = new JPanel();
        j.setLayout(new BorderLayout());
        j.add(jb,BorderLayout.CENTER);
        j.add(jp,BorderLayout.SOUTH);
        j.setOpaque(false);
        JPanel jpp = new JPanel();
        jpp.setLayout(new BorderLayout());
        jpp.setOpaque(false);
        jpp.add(j,BorderLayout.SOUTH);
        JPanel jps = new JPanel();
        jps.setLayout(new BorderLayout());
        jps.setOpaque(false);
        JLabel creator = new JLabel();
        creator.setText("Created by Mr_IK");
        creator.setOpaque(false);
        creator.setForeground(Color.WHITE);
        JLabel ver = new JLabel();
        ver.setText("MCchatManager v1.2");
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
            double ver = Updater.jsontoStringVersion();
            if(Updater.thisversion < ver){
                Updater.download();
                JOptionPane.showMessageDialog(gui, "ダウンロードが完了しました。");
            }else{
                JOptionPane.showMessageDialog(gui, "このMCchatManagerは最新です。");
            }

        }
    }

    class ClickAction3 implements ActionListener {

        MainGUI gui;
        public ClickAction3(MainGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.setviewPlugins();
        }
    }


}
