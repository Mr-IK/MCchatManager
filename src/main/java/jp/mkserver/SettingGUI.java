package jp.mkserver;

import jp.mkserver.utils.SettingSaver;
import jp.mkserver.utils.SoundSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class SettingGUI extends JPanel{

    public MainGUI gui;
    JTextField text1;
    JCheckBox checkbox;
    public SettingGUI(MainGUI gui) {
        this.gui = gui;
        setName("SettingGUI");
        URL urls=getClass().getClassLoader().getResource("PluginBack.png");
        Icon title =new ImageIcon(urls);
        JLabel titlelabel = new JLabel(title);
        titlelabel.setOpaque(false);
        titlelabel.setLayout(new BorderLayout());
        JButton button1 = new JButton("戻る");
        button1.addActionListener(new ClickAction1(this));
        text1 = new JTextField("", 20);
        SoundSetting sound = SettingSaver.loadSound();
        checkbox = new JCheckBox("ループする");
        if(sound != null){
            text1.setText((int)sound.getVold()*100+"");
            checkbox.setSelected(sound.isOn());
        }
        JButton button2 = new JButton("保存して戻る");
        button2.addActionListener(new ClickAction2(this));
        JPanel js = new JPanel();
        js.setLayout(new FlowLayout());
        js.add(button1);
        js.add(button2);
        js.setOpaque(false);
        JPanel jc = new JPanel();
        jc.setLayout(new FlowLayout());
        jc.add(text1);
        jc.add(checkbox);
        titlelabel.add(jc,BorderLayout.NORTH);
        titlelabel.add(js,BorderLayout.CENTER);
        jc.setOpaque(false);
        add(titlelabel,BorderLayout.CENTER);
    }

    class ClickAction1 implements ActionListener {

        SettingGUI gui;
        public ClickAction1(SettingGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.gui.playClickSound();
            gui.gui.gui.setviewMain();
        }
    }

    class ClickAction2 implements ActionListener {

        SettingGUI gui;
        public ClickAction2(SettingGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.gui.playClickSound();
            String text = text1.getText();
            if(text==null||text.equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(gui,"音量を入力してください！");
                return;
            }
            int vol;
            try{
                vol = Integer.parseInt(text);
            }catch (NumberFormatException ee){
                JOptionPane.showMessageDialog(gui,"音量には数字を入力してください！");
                return;
            }
            text1.setText("");
            boolean ison = checkbox.isSelected();
            checkbox.setSelected(false);
            float vols = (float)Math.log10((float)(vol/100) / 20)*20;
            gui.gui.gui.setBGMVol(((double)(vol/100)));
            gui.gui.gui.setBGMloop(ison);
            SettingSaver.saveSound(vols,ison,(vol/100));
            JOptionPane.showMessageDialog(gui,"設定を反映しました！");
            gui.gui.gui.setviewMain();
        }
    }
}
