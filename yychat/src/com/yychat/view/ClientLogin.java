package com.yychat.view;//���������ù�����

import javax.swing.*;

public class ClientLogin extends JFrame{//������ClientLogin,�̳�

	JLabel jlbl1;
	public  ClientLogin(){//���췽��
		jlbl1=new JLabel(new ImageIcon("images/tou.gif"));//��ǩ����
		this.add(jlbl1,"North");//this��ʾ������
		
		this.setSize(350,240);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//��;��
		this.setVisible(true);	
		
	}
	public static void main(String[] args) {
		ClientLogin clientLogin=new ClientLogin();//�´����������ñ���
		//clientLogin=null;//����ͻᱻ��������������

	}

}
