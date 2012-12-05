/*
Java code of my research

Copyright (C) Zhenwei Ding Kyoto University

August 2012

This is the code of the implementation of my
research.  If you have any questions regarding 
the code, please contact the following address:

Email: dzw777@gmail.com
*/
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class server {
	
	static class Application {
		int Rin;	// Application parameter
		int Rout;	// Application parameter
		int Rin2;	// Application parameter
		int Rs;		// Application parameter
		int type;	// parameter representing the decision of distribution

		// Basic Constructor
		public Application(int rin, int rout, int rin2, int rs) {
			Rin = rin;
			Rout = rout;
			Rin2 = rin2;
			Rs = rs;
			type = 0;
		}
		
		// Constructor Overloading
		// Construct from type byte[]
		// Used after reception of byte through socket communication
		public Application(byte[]Bytes) {
			String str = new String(Bytes);
			int flag = 0;
			int num = 0;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '/') {
					switch (num) {
					case 0:
						Rin = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i+1;
						break;
					case 1:
						Rout = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i+1;
						break;
					case 2:
						Rin2 = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i+1;
						break;
					case 3:
						Rs = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i+1;
						break;
					case 4:
						type = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i+1;
						break;
					}
				}
			}
		}
		
		//Convert type Application into type type[]
		//Used before the transmission of socket communication	
		byte[] getBytes(){
			String str1 = Integer.toString(Rin);
			String str2 = Integer.toString(Rout);
			String str3 = Integer.toString(Rin2);
			String str4 = Integer.toString(Rs);
			String str5 = Integer.toString(type);
			str1 = str1.concat("/");
			str1 = str1.concat(str2);
			str1 = str1.concat("/");
			str1 = str1.concat(str3);
			str1 = str1.concat("/");
			str1 = str1.concat(str4);
			str1 = str1.concat("/");
			str1 = str1.concat(str5);
			str1 = str1.concat("/");
			byte[] Bytes = str1.getBytes();
			return Bytes;
		}
	}
	
	//Output strings with dummy data
	static String gstr (int tmp){
		char[] str = new char[tmp];
		for(int i = 0; i < tmp; i++){
			str[i] = '*';
		}
		return new String(str);
	}
	
	//Callable interface for returning the result to the main function
	//while performing multiple threads
	public static class transmission implements Callable{
		long length;
		int sequence;	//Indicator of different target
		int flag;		//Indicator of different distribution
		
		public transmission (int flg,int seq,int tmp){     
            length = tmp;
            sequence = seq;
            flag = flg;
        } 
		
		//Return type is long
		public Long call() throws Exception{
			//Bind the IP address of router
			InetAddress routerAddress = InetAddress.getByName("192.168.85.3");
			DatagramSocket socket2 = new DatagramSocket();
			byte[] byteBuffer = gstr((int)length).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(byteBuffer, byteBuffer.length,routerAddress,4701);
			
			long num = 0;					//Counter for iteration
			long[] sendtime = new long[2];	//Variables for recording system time
			long result = 0;				//Final output
			
			//Bind the IP address of management server
			InetAddress magAddress = InetAddress.getByName("192.168.85.1");
			DatagramSocket socket = new DatagramSocket();
			
			String rslt;	//String used for sending result
			
			//For applications placed at client
			//Sending information of Rin2 to router
			if(flag == 1 && sequence == 1){
				while(true){
					while(num < 101){
						if (num == 1){
							sendtime[0] = System.nanoTime();
						}
						socket2.send(sendPacket);
						if (num == 100){
							sendtime[1] = System.nanoTime();
						}
						num += 1;
						Thread.sleep(3);
					}
					result = (length*100*1000000*8)/(sendtime[1]-sendtime[0]);
					System.out.println(result);
					
					//Reporting result to management server
					rslt = Long.toString(result);
					DatagramPacket ctnlPacket = new DatagramPacket(rslt.getBytes(),rslt.getBytes().length,magAddress,4706);
					socket.send(ctnlPacket);
					
					//Prepare for the next iteration
					num = 0;
				}
			}
			
			//For applications placed at client
			//Sending information of Rs to router
			if(flag == 1 && sequence == 2){
				while(true){
					while(num < 101){
						if (num == 1){
							sendtime[0] = System.nanoTime();
						}
						socket2.send(sendPacket);
						if (num == 100){
							sendtime[1] = System.nanoTime();
						}
						num += 1;
						Thread.sleep(3);
					}
					result = (length*100*1000000*8)/(sendtime[1]-sendtime[0]);
					System.out.println(result);
					
					//Reporting result to management server
					rslt = Long.toString(result);
					DatagramPacket ctnlPacket = new DatagramPacket(rslt.getBytes(),rslt.getBytes().length,magAddress,4707);
					socket.send(ctnlPacket);
					
					//Prepare for the next iteration
					num = 0;
				}
			}
			
			//For applications placed at router
			//Sending information of Rin2 to router
			if(flag == 2 && sequence == 1){
				while(true){
					while(num < 101){
						if (num == 1){
							sendtime[0] = System.nanoTime();
						}
						socket2.send(sendPacket);
						if (num == 100){
							sendtime[1] = System.nanoTime();
						}
						num += 1;
						Thread.sleep(3);
					}
					result = (length*100*1000000*8)/(sendtime[1]-sendtime[0]);
					System.out.println(result);
					
					//Reporting result to management server
					rslt = Long.toString(result);
					DatagramPacket ctnlPacket = new DatagramPacket(rslt.getBytes(),rslt.getBytes().length,magAddress,4706);
					socket.send(ctnlPacket);
					
					//Prepare for the next iteration
					num = 0;
				}
			}
			
			//For applications placed at router
			//Sending information of Rs to router
			if(flag == 2 && sequence == 2){
				while(true){
					while(num < 101){
						if (num == 1){
							sendtime[0] = System.nanoTime();
						}
						socket2.send(sendPacket);
						if (num == 100){
							sendtime[1] = System.nanoTime();
						}
						num += 1;
						Thread.sleep(3);
					}
					result = (length*100*1000000*8)/(sendtime[1]-sendtime[0]);
					System.out.println(result);
					
					//Reporting result to management server
					rslt = Long.toString(result);
					DatagramPacket ctnlPacket = new DatagramPacket(rslt.getBytes(),rslt.getBytes().length,magAddress,4707);
					socket.send(ctnlPacket);
					
					//Prepare for the next iteration
					num = 0;
				}
			}
			
			//For applications placed at server
			//Sending information to router
			if(flag == 3){
				while(true){
					while(num < 101){
						if (num == 1){
							sendtime[0] = System.nanoTime();
						}
						socket2.send(sendPacket);
						if (num == 100){
							sendtime[1] = System.nanoTime();
						}
						num += 1;
						Thread.sleep(3);
					}
					result = (length*100*1000000*8)/(sendtime[1]-sendtime[0]);
					System.out.println(result);
					rslt = Long.toString(result);
					
					//Reporting result to management server
					DatagramPacket ctnlPacket = new DatagramPacket(rslt.getBytes(),rslt.getBytes().length,magAddress,4706);
					DatagramPacket ctnlPacket2 = new DatagramPacket("0".getBytes(),"0".getBytes().length,magAddress,4707);
					socket.send(ctnlPacket);
					socket.send(ctnlPacket2);
					
					//Prepare for the next iteration
					num = 0;
				}
			}
			return result;
		}
	}
	
	public static void main(String[] args) throws Exception {
		//Bind the IP address of management server
		InetAddress magAddress = InetAddress.getByName("192.168.85.1");
		DatagramSocket socket = new DatagramSocket();
		DatagramSocket ssocket = new DatagramSocket(4700);
		DatagramPacket packet = new DatagramPacket(new byte[100],100);
		
		//Variable array for storing applications
		ArrayList total_app = new ArrayList();
		
		//Generate applications at server
		Application e = new Application(400, 400, 2000, 0);
		total_app.add(e);
		Application f = new Application(2000, 200, 200, 100);
		total_app.add(f);
		
		//Sending total number of applications to management server
		String tmpstr = Integer.toString(total_app.size());
		DatagramPacket ctnlPacket = new DatagramPacket(tmpstr.getBytes(),tmpstr.getBytes().length,magAddress,4702);
		socket.send(ctnlPacket);
		
		//Sending information of each application to management server
		for(int i=0;i<total_app.size();i++){
			Application tmp = (Application)total_app.get(i);
			ctnlPacket = new DatagramPacket(tmp.getBytes(),tmp.getBytes().length,magAddress,4702);
			socket.send(ctnlPacket);
		}
				
		for(;;){
			ssocket.receive(packet);
			
			//Construct string from type byte
			String strflag = new String(packet.getData());
			strflag = strflag.trim();	//Delete additional spaces
			int mode = Integer.parseInt(strflag);
			
			if(mode == 1){	//For applications placed at client
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				int trlen1 = Integer.parseInt(strflag);
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				int trlen2 = Integer.parseInt(strflag);
				transmission task1 = new transmission(mode,1,trlen1);
				transmission task2 = new transmission(mode,2,trlen2);
				ExecutorService es = Executors.newFixedThreadPool(2);
				Future future1 = es.submit(task1);
				Future future2 = es.submit(task2);
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				es.shutdownNow(); 
			}
			else if (mode == 2){	//For applications placed at router
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				int trlen1 = Integer.parseInt(strflag);
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				int trlen2 = Integer.parseInt(strflag);
				transmission task1 = new transmission(mode,1,trlen1);
				transmission task2 = new transmission(mode,2,trlen2);
				ExecutorService es = Executors.newFixedThreadPool(2);
				Future future1 = es.submit(task1);
				Future future2 = es.submit(task2);
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				es.shutdownNow();   
			}
			else{	//For applications placed at server
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				int trlen1 = Integer.parseInt(strflag);
				transmission task1 = new transmission(mode,0,trlen1);
				ExecutorService es = Executors.newFixedThreadPool(1);
				Future future1 = es.submit(task1);
				ssocket.receive(packet);
				strflag = new String(packet.getData());
				strflag = strflag.trim();
				es.shutdownNow(); 
			}
		}
	
	}
}
