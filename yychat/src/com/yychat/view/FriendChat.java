package com.yychat.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FriendChat extends JFrame implements ActionListener{
	JScrollPane jsp;
	JTextArea jta;
	
	JPanel jp;
	JTextField jtf;
	JButton jb;
	
	
	public FriendChat(){
		jta=new JTextArea();
		jta.setEditable(false);
		jta.setForeground(Color.red);
		jsp=new JScrollPane(jta);
		this.add(jsp,"Center");
		
		
		jp=new JPanel();
		jtf=new JTextField(15);
		jb=new JButton("·¢ËÍ");
		jb.addActionListener(this);
		jp.add(jtf);jp.add(jb);
		this.add(jp,"South");
		
		
		this.setSize(350,350);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	

	public static void main(String[] args) {
		FriendChat friendChat=new FriendChat();
		

	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==jb)
			jta.append(jtf.getText()+"\r\n");
		
	}

}
