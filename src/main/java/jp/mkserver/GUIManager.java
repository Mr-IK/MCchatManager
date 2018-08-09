package jp.mkserver;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIManager extends JFrame implements ActionListener {
    JTextField text1;
    JLabel label;
    ChatClient client;

    GUIManager (ChatClient client){
        this.client = client;
        setTitle("チャット用GUI");
        setBounds(100, 100, 300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        text1 = new JTextField("チャット内容", 20);
        JButton button = new JButton("送信");
        button.addActionListener(this);
        label = new JLabel();

        p.add(text1);
        p.add(button);

        Container contentPane = getContentPane();
        contentPane.add(p, BorderLayout.CENTER);
        contentPane.add(label, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        String message = text1.getText();
        if(message.equalsIgnoreCase("!end")) {
            System.out.println("See you!");
            client.disconnect();
            return;
        }else if(message.equalsIgnoreCase("!server info")){
            System.out.println("IP: "+client.getSession().getHost()+" PORT: "+client.getSession().getPort());
            text1.setText("");
            return;
        }
        client.getSession().send(new ClientChatPacket(message));
        text1.setText("");
    }

    public void destroy(){
        dispose();
    }

}
