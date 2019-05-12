package com.chatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;

public class StartServer {
	ServerSocket ss;
	Socket s;
	String userName,passWord;
	ObjectOutputStream oos;
	String username,ID,friendname,friendrelation;

	
	public static HashMap hmSocket=new HashMap<String,Socket>();//���ͣ�ͨ����
	
	public StartServer() throws SQLException{
		try {
			ss=new ServerSocket(3456);//�������˿ڼ���3456
			System.out.println("�������Ѿ�����������3456�˿�...");
			while(true){//?���߳�����
				s=ss.accept();//�ȴ��ͻ��˽�������
				System.out.println(s);//�������Socket����
				
				//�ֽ������� ��װ�� ����������
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User user=(User)ois.readObject();//�����û���¼����user
				this.userName=user.getUserName();
				this.passWord=user.getPassWord();
				System.out.println(user.getUserName());
				System.out.println(user.getPassWord());
				
				Class.forName("com.mysql.jdbc.Driver");
			
				
				String url="jdbc:mysql://127.0.0.1:3306/yychat?useUnicode=true&characterEncoding=UTF-8";
				String dbuser="root";
				String dbpass="";
				Connection conn=DriverManager.getConnection(url, dbuser, dbpass);
				
				String user_Login_Sql="select * from user where username=? and password=?";
				PreparedStatement ptmtuser=conn.prepareStatement(user_Login_Sql);
				ptmtuser.setString(1, userName);
				ptmtuser.setString(2, passWord);
								
			    ResultSet rs=ptmtuser.executeQuery();
			    
			    boolean loginSuccess=rs.next();
				
				
				//Server����֤�����Ƿ�123456��
				Message mess=new Message();
				mess.setSender("Server");
				mess.setReceiver(user.getUserName());
				if(loginSuccess){//������"==",����Ƚ�
					//��Ϣ���ݣ�����һ��Message����				
					mess.setMessageType(Message.message_LoginSuccess);//��֤ͨ��	
	
					String friendListRelation_Sql="select friendname from friendList where username=? and friendrelation='1'";
					PreparedStatement ptmtfriendListRelation=conn.prepareStatement(friendListRelation_Sql);
					ptmtfriendListRelation.setString(1, userName);
					
					rs=ptmtfriendListRelation.executeQuery();		
					
					String friendListRelation="";
					while(rs.next()){
						friendListRelation=friendListRelation+rs.getString("friendname")+" ";
					}			
					mess.setContent(friendListRelation);
					mess.setMessageType(MessageType.message_Friendrelation);;
					System.out.println(userName+"��ȫ������"+friendListRelation);
				}
				else{				
					mess.setMessageType(Message.message_LoginFailure);//��֤��ͨ��	
				}				
				sendMessage(s,mess);
				
				if(loginSuccess){
					mess.setMessageType(Message.message_LoginSignal);
					mess.setSender("Server");
					mess.setContent(this.userName);
					Set friendSet=hmSocket.keySet();
					Iterator it=friendSet.iterator();
					String friendName;
					while(it.hasNext()){
						friendName=(String)it.next();
						mess.setReceiver(friendName);
						Socket s1=(Socket) hmSocket.get(friendName);
						sendMessage(s1,mess);

					}
										
					//����ÿһ���û���Ӧ��Socket
					hmSocket.put(userName,s);
					System.out.println("�����û���Socket"+userName+s);
					//��ν��տͻ��˵�������Ϣ����һ���߳�������������Ϣ
					new ServerReceiverThread(s,hmSocket).start();//�����̣߳������߳̾���
					System.out.println("�����̳߳ɹ�");
					
					
				
				}
				
			}			
			
		} catch (IOException | ClassNotFoundException e) {			
			e.printStackTrace();
		}
	}

	public void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);
	}
}
