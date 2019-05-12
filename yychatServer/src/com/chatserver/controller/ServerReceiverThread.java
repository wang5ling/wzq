package com.chatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{//必须要有run()方法
	Socket s;
	HashMap hmSocket;
	String sender;
	
	public ServerReceiverThread(Socket s,HashMap hmSocket){
		this.s=s;
		this.hmSocket=hmSocket;		
	}
	
	public void run(){		
		ObjectInputStream ois;
		while(true){
			try {
				//接收Message信息
				ois=new ObjectInputStream(s.getInputStream());
				Message mess=(Message)ois.readObject();//接收用户发送来的聊天信息，阻塞，10个用户，100毫秒
				sender=mess.getSender();
				System.out.println("等待用户发送聊天信息");
				System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说:"+mess.getContent());
				
				//转发Message信息
				if(mess.getMessageType().equals(Message.message_Common)){
					Socket s1=(Socket)hmSocket.get(mess.getReceiver());
					sendMessage(s1,mess);
					System.out.println("服务器转发了信息"+mess.getSender()+"对"+mess.getReceiver()+"说:"+mess.getContent());
				}
				
				//第2步骤，返回在线好友信息到客户端
				if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
					Socket s2=(Socket)hmSocket.get(mess.getReceiver());

					Set friendSet=StartServer.hmSocket.keySet();
					Iterator it=friendSet.iterator();
					String friendName;
					String friendString="";
					while(it.hasNext()){
						friendName=(String)it.next();
						if(!friendName.equals(mess.getSender()))
							friendString=friendName+" "+friendString;							
				    }
					System.out.println("全部好友名字:"+friendString);
					
					mess.setContent(friendString);
					mess.setReceiver(sender);
					mess.setSender("Server");
					mess.setMessageType(Message.message_OnlineFriend);
					
					Socket s1=(Socket)hmSocket.get(sender);
					sendMessage(s1,mess);	
					
				}
			
				
//				//好友上线时的信息转发
//				
//				//Message mess1=new Message();
//				//String userName;
//				if(mess.getMessageType().equals(Message.message_LoginSignal)){
//					Socket s2=(Socket)hmSocket.get(mess.getReceiver());
//				}		
//				Set friendSet1=StartServer.hmSocket.keySet();
//				Iterator it1=friendSet1.iterator();
//				System.out.println(mess);
//				
//				mess1.setReceiver(sender);
//				mess1.setContent(userName);
//				mess1.setSender("Server");
//				mess1.setMessageType(Message.message_LoginSignalForWard);
//				
//				Socket s2=(Socket)hmSocket.get(sender);
//				try {
//					sendMessage(s2,mess1);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			  } 
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			  }
			
			
			
		}							
	}

	public void sendMessage( Socket s1,Message mess) throws IOException {
		ObjectOutputStream oos=new ObjectOutputStream(s1.getOutputStream());
		oos.writeObject(mess);//转发Message
	}

	
}
