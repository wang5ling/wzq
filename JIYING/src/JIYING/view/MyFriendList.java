package JIYING.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MyFriendList extends JFrame implements ActionListener{
	CardLayout cardLayout;
	
	//第一张卡片
	JPanel  myFriendPanel,
	        myFriendListJPanel,
	        myStrangerBlackListPanel;
	JButton myFriendButton,
	        myStrangerButton,
	        myBlackListButton;
	JScrollPane myFriendListJScrollPane;
	public static final int MYFRIENDCOUNT=51;
	JLabel[] myFriendJLabel=new JLabel[MYFRIENDCOUNT];
	
	//第二张卡片
	JPanel  myStrangerPanel,
	        myFriendStrangerListJPanel,
	        myStrangerListJPanel;
	JButton myFriendButton_1,
            myStrangerButton_1,
            myBlackListButton_1;
	JScrollPane myStrangerListJScrollPane;
	public static final int MYSTRANGERCOUNT=21;
	JLabel[] myStangerJLabel=new JLabel[MYSTRANGERCOUNT];
	
	
	public MyFriendList(){
		
		
/*	*/	//第一张卡片
		myFriendPanel=new JPanel(new BorderLayout());
		//北部
		myFriendButton=new JButton("我的好友");
		myFriendPanel.add(myFriendButton,"North");
		
		//中部
		myFriendListJPanel=new JPanel(new GridLayout(MYFRIENDCOUNT-1,1));
		for(int i=1;i<MYFRIENDCOUNT;i++){
			myFriendJLabel[i]=new JLabel(i+"",new ImageIcon("images/yy3.gif"),JLabel.LEFT);
			myFriendListJPanel.add(myFriendJLabel[i]);
		}
		myFriendListJScrollPane=new JScrollPane(myFriendListJPanel);
		myFriendPanel.add(myFriendListJScrollPane,"Center");
		
		//南部
		myStrangerBlackListPanel=new JPanel(new GridLayout(2,1));
		myStrangerButton=new JButton("陌生人");
		myStrangerButton.addActionListener(this);
		myBlackListButton=new JButton("黑名单");
		myStrangerBlackListPanel.add(myStrangerButton);
		myStrangerBlackListPanel.add(myBlackListButton);
		myFriendPanel.add(myStrangerBlackListPanel,"South");
		
		//第二张卡片
		
/* 	*/	myStrangerPanel=new JPanel(new BorderLayout());
		//南部
        myBlackListButton_1=new JButton("黑名单");
        myStrangerPanel.add(myBlackListButton_1,"South");
        
        //中部
        myStrangerListJPanel=new JPanel(new GridLayout(MYSTRANGERCOUNT-1,1));
		for(int i=1;i<MYSTRANGERCOUNT;i++){
			myStangerJLabel[i]=new JLabel(i+"",new ImageIcon("images/yy2.gif"),JLabel.LEFT);
			myStrangerListJPanel.add(myStangerJLabel[i]);
		}
		myStrangerListJScrollPane=new JScrollPane(myStrangerListJPanel);
		myStrangerPanel.add(myStrangerListJScrollPane);
		 
        //北部
		myFriendStrangerListJPanel=new JPanel(new GridLayout(2,1));
        myFriendButton_1=new JButton("我的好友");
        myFriendButton_1.addActionListener(this);
        myStrangerButton_1=new JButton("陌生人");
        myFriendStrangerListJPanel.add(myFriendButton_1);
        myFriendStrangerListJPanel.add(myStrangerButton_1);
        myStrangerPanel.add(myFriendStrangerListJPanel,"North");
        
        
        
        
        cardLayout=new CardLayout();
        this.add(myFriendPanel,"1");
		this.add(myStrangerPanel,"2");
		
		this.setSize(200, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyFriendList myfriendList=new MyFriendList();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==myStrangerButton)cardLayout.show(this.getContentPane(),"2");
		if(e.getSource()==myFriendButton_1)cardLayout.show(this.getContentPane(),"1");
	}

}
