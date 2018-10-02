package jp.mkserver;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

public class PluginsGUI extends JPanel implements ActionListener {

    private static ArrayList<String> guilist = new ArrayList<>();
    private static PluginsGUI guis;

    public static boolean addGUI(JPanel ui,String uiname){
        if(guilist.contains(uiname)){
            return false;
        }
        guilist.add(uiname);
        guis.add(ui,uiname);
        return true;
    }

    public static void removeGUI(JPanel ui,String uiname){
        guilist.remove(uiname);
        guis.remove(ui);
    }

    public static void showMainPlugin(){
        layout.show(guis,"main");
    }

    public MainGUI gui;
    public JPanel p2;
    JPanel p3;
    JTextField text1;
    JLabel titlelabel;
    JTextArea area;
    private static final CardLayout layout = new CardLayout();
    public PluginsGUI(MainGUI gui){
        this.gui = gui;
        setName("PluginsGUI");
        URL urls=getClass().getClassLoader().getResource("PluginBack.png");
        Icon title =new ImageIcon(urls);
        titlelabel = new JLabel(title);
        titlelabel.setOpaque(false);
        titlelabel.setLayout(new BorderLayout());
        URL urlss=getClass().getClassLoader().getResource("Plugins.png");
        Icon titles =new ImageIcon(urlss);
        titlelabel.add(new JLabel(titles),BorderLayout.NORTH);
        JButton button1 = new JButton("戻る");
        button1.addActionListener(new ClickAction1(this));
        titlelabel.add(button1,BorderLayout.WEST);
        text1 = new JTextField("", 20);
        JButton button = new JButton("検索");
        button.addActionListener(this);
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(text1);
        p.add(button);
        p3 = new JPanel();
        p3.setOpaque(false);
        p3.setLayout(new BorderLayout());
        p3.add(p,BorderLayout.NORTH);
        titlelabel.add(p3,BorderLayout.CENTER);
        p2= new JPanel();
        p2.setLayout(new BorderLayout());
        p2.setOpaque(false);
        p2.add(titlelabel,BorderLayout.CENTER);
        setOpaque(false);
        setLayout(layout);
        add(p2,"main");
        layout.show(this,"main");
        guis = this;
    }

    class ClickAction1 implements ActionListener {

        PluginsGUI gui;
        public ClickAction1(PluginsGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.gui.gui.setviewMain();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(area != null) {
            p3.remove(area);
        }
        area = new JTextArea();
        PluginsGUI gui = this;
        area.setForeground(Color.WHITE);
        area.setOpaque(false);
        area.setEditable(false);
        area.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        for(String name : guilist){
            if(name.contains(text1.getText())){
                text1.setText("");
                area.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        layout.show(gui,name);
                    }
                });
                area.append(name+" GUIを開く");
                area.append("クリックすると開きます！");
                p3.add(area,BorderLayout.CENTER);
                this.updateUI();
                return;
            }
        }
        area.append(text1.getText()+" GUIは");
        area.append("見つかりませんでした…");
        p3.add(area,BorderLayout.CENTER);
        text1.setText("");
        this.updateUI();
    }
}
