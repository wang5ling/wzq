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

public class ServerReceiverThread extends Thread{//����Ҫ��run()����
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
				//����Message��Ϣ
				ois=new ObjectInputStream(s.getInputStream());
				Message mess=(Message)ois.readObject();//�����û���������������Ϣ��������10���û���100����
				sender=mess.getSender();
				System.out.println("�ȴ��û�����������Ϣ");
				System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵:"+mess.getContent());
				
				//ת��Message��Ϣ
				if(mess.getMessageType().equals(Message.message_Common)){
					Socket s1=(Socket)hmSocket.get(mess.getReceiver());
					sendMessage(s1,mess);
					System.out.println("������ת������Ϣ"+mess.getSender()+"��"+mess.getReceiver()+"˵:"+mess.getContent());
				}
				
				//��2���裬�������ߺ�����Ϣ���ͻ���
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
					System.out.println("ȫ����������:"+friendString);
					
					mess.setContent(friendString);
					mess.setReceiver(sender);
					mess.setSender("Server");
					mess.setMessageType(Message.message_OnlineFriend);
					
					Socket s1=(Socket)hmSocket.get(sender);
					sendMessage(s1,mess);	
					
				}
			
				
//				//��������ʱ����Ϣת��
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
		oos.writeObject(mess);//ת��Message
	}

	
}
