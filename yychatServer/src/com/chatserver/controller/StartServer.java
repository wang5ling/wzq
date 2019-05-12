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

	
	public static HashMap hmSocket=new HashMap<String,Socket>();//泛型，通用类
	
	public StartServer() throws SQLException{
		try {
			ss=new ServerSocket(3456);//服务器端口监听3456
			System.out.println("服务器已经启动，监听3456端口...");
			while(true){//?多线程问题
				s=ss.accept();//等待客户端建立连接
				System.out.println(s);//输出连接Socket对象
				
				//字节输入流 包装成 对象输入流
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User user=(User)ois.readObject();//接收用户登录对象user
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
				
				
				//Server端验证密码是否“123456”
				Message mess=new Message();
				mess.setSender("Server");
				mess.setReceiver(user.getUserName());
				if(loginSuccess){//不能用"==",对象比较
					//消息传递，创建一个Message对象				
					mess.setMessageType(Message.message_LoginSuccess);//验证通过	
	
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
					System.out.println(userName+"的全部好友"+friendListRelation);
				}
				else{				
					mess.setMessageType(Message.message_LoginFailure);//验证不通过	
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
										
					//保存每一个用户对应的Socket
					hmSocket.put(userName,s);
					System.out.println("保存用户的Socket"+userName+s);
					//如何接收客户端的聊天信息？另建一个线程来接收聊天信息
					new ServerReceiverThread(s,hmSocket).start();//创建线程，并让线程就绪
					System.out.println("启动线程成功");
					
					
				
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
