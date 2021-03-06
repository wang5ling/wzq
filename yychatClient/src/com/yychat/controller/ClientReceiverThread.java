package com.yychat.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;
import com.yychat.view.ClientLogin;
import com.yychat.view.FriendChat1;
import com.yychat.view.FriendList;

public class ClientReceiverThread extends Thread{
	Socket s;
	
	public ClientReceiverThread(Socket s){
		this.s=s;
	}
	
	public void run(){
		ObjectInputStream ois;
		Message mess;
		while(true){
			try {
				//接收服务器转发过来的Message
				ois = new ObjectInputStream(s.getInputStream());				
				mess=(Message)ois.readObject();//等待Server发送Message,阻塞	
				String chatMessageString=(mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getContent()+"\r\n");
				System.out.println(chatMessageString);
				
				if(mess.getMessageType().equals(Message.message_Common)){
					//希望聊天信息在好友的聊天界面上显示出来，该怎么实现的问题？
					//1、拿到要显示聊天信息的friendChat对象
					FriendChat1 friendChat1=(FriendChat1)FriendList.hmFriendChat1.get(mess.getReceiver()+"to"+mess.getSender());
					//2、把聊天信息在JTextArea上显示
					friendChat1.appendJta(chatMessageString);	
				}
			
				
				//第3步接收到服务器发送来的在线好友信息，激活对应图标
				if(mess.getMessageType().equals(Message.message_OnlineFriend)){
					System.out.println("在线好友:"+mess.getContent());
				if(!mess.getContent().equals("")){	
					FriendList friendList=(FriendList)((HashMap) ClientLogin.hmFriendList).get(mess.getReceiver());
					friendList.setEnabledOnlineFriend(mess.getContent());}
				}
				
				if(mess.getMessageType().equals(Message.message_LoginSignal)){
					System.out.println("好友登陆:"+mess.getContent());
					FriendList friendList=(FriendList)((HashMap) ClientLogin.hmFriendList).get(mess.getReceiver());
					friendList.setEnabledNewOnlineFriend(mess.getContent());
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}	
	}
}
