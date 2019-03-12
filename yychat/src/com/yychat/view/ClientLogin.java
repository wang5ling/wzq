package com.yychat.view;//包名，作用管理类

import javax.swing.*;

public class ClientLogin extends JFrame{//类名：ClientLogin,继承

	JLabel jlbl1;
	public  ClientLogin(){//构造方法
		jlbl1=new JLabel(new ImageIcon("images/tou.gif"));//标签对象
		this.add(jlbl1,"North");//this表示对象本身
		
		this.setSize(350,240);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//用途？
		this.setVisible(true);	
		
	}
	public static void main(String[] args) {
		ClientLogin clientLogin=new ClientLogin();//新创建对象，引用变量
		//clientLogin=null;//对象就会被垃圾回收器回收

	}

}
