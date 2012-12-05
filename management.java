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

public class management {

	static class Application {
		int Rin; 	// Application parameter
		int Rout; 	// Application parameter
		int Rin2; 	// Application parameter
		int Rs; 	// Application parameter
		int type; 	// parameter representing the decision of distribution

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
		public Application(byte[] Bytes) {
			String str = new String(Bytes);
			int flag = 0;
			int num = 0;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '/') {
					switch (num) {
					case 0:
						Rin = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i + 1;
						break;
					case 1:
						Rout = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i + 1;
						break;
					case 2:
						Rin2 = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i + 1;
						break;
					case 3:
						Rs = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i + 1;
						break;
					case 4:
						type = Integer.parseInt(str.substring(flag, i));
						num += 1;
						flag = i + 1;
						break;
					}
				}
			}
		}

		//Convert type Application into type type[]
		//Used before the transmission of socket communication
		byte[] getBytes() {
			String str1 = Integer.toString(Rin);
			String str2 = Integer.toString(Rout);
			String str3 = Integer.toString(Rin2);
			String str4 = Integer.toString(Rs);
			String str5 = Integer.toString(type);
			str1 = str1.concat("/");	//separate parameters from each other
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

	//Calculate the length of sending string based on input number
	static int strlen(int tmp) {
		float result, temp;
		temp = (float) tmp / 100000;
		result = ((3 * temp) / (1 - temp)) * 100000 / 8;
		return (int) result;
	}

	//Callable interface for returning the result to the main function
	//while performing multiple threads
	public static class transmission implements Callable {
		long result;

		public transmission() {
		}

		//Return type is long
		public Long call() throws Exception {
			//Class datagrampacket for UDP reception
			DatagramPacket packet = new DatagramPacket(new byte[100], 100);
			DatagramPacket packet2 = new DatagramPacket(new byte[100], 100);
			DatagramPacket packet3 = new DatagramPacket(new byte[100], 100);
			DatagramPacket packet4 = new DatagramPacket(new byte[100], 100);
			DatagramPacket packet5 = new DatagramPacket(new byte[100], 100);
			DatagramPacket packet6 = new DatagramPacket(new byte[100], 100);
			DatagramPacket packet7 = new DatagramPacket(new byte[100], 100);
			//Class datagramsocket for UDP socket communication
			DatagramSocket socket = new DatagramSocket(4703);
			DatagramSocket socket2 = new DatagramSocket(4704);
			DatagramSocket socket3 = new DatagramSocket(4705);
			DatagramSocket socket4 = new DatagramSocket(4706);
			DatagramSocket socket5 = new DatagramSocket(4707);
			DatagramSocket socket6 = new DatagramSocket(4708);
			DatagramSocket socket7 = new DatagramSocket(4709);

			for (;;) {
				//listen to UDP port
				socket.receive(packet);
				socket2.receive(packet2);
				socket3.receive(packet3);
				socket4.receive(packet4);
				socket5.receive(packet5);
				socket6.receive(packet6);
				socket7.receive(packet7);
				
				//Convert type byte[] into type long
				String str = new String(packet.getData());
									//Construct string from type byte
				str = str.trim();	//Delete additional spaces
				long clnt_dr = Long.parseLong(str);
				
				String str2 = new String(packet2.getData());
				str2 = str2.trim();
				long clnt_dr2 = Long.parseLong(str2);
				
				String str3 = new String(packet3.getData());
				str3 = str3.trim();
				long clnt_dr3 = Long.parseLong(str3);
				
				String str4 = new String(packet4.getData());
				str4 = str4.trim();
				long clnt_dr4 = Long.parseLong(str4);
				
				String str5 = new String(packet5.getData());
				str5 = str5.trim();
				long clnt_dr5 = Long.parseLong(str5);
				
				String str6 = new String(packet6.getData());
				str6 = str6.trim();
				long clnt_dr6 = Long.parseLong(str6);
				
				String str7 = new String(packet7.getData());
				str7 = str7.trim();
				long clnt_dr7 = Long.parseLong(str7);
				
				result = (clnt_dr + clnt_dr2 + clnt_dr3 + clnt_dr4 + clnt_dr5
				+ clnt_dr6 + clnt_dr7)/100000;
				System.out.println(clnt_dr);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		//Used for collect application information from client, router and server
		DatagramSocket socket = new DatagramSocket(4700);
		DatagramSocket socket2 = new DatagramSocket(4701);
		DatagramSocket socket3 = new DatagramSocket(4702);
		
		//UDP socket for transmission
		DatagramSocket sndsocket1 = new DatagramSocket();
		DatagramSocket sndsocket2 = new DatagramSocket();
		DatagramSocket sndsocket3 = new DatagramSocket();
		DatagramSocket sndsocket4 = new DatagramSocket();
		DatagramSocket sndsocket5 = new DatagramSocket();
		
		//Bind IP address
		InetAddress cliAddress = InetAddress.getByName("192.168.85.2");
		InetAddress rtrAddress = InetAddress.getByName("192.168.85.3");
		InetAddress srvAddress = InetAddress.getByName("192.168.85.4");
		InetAddress outrtrAddress = InetAddress.getByName("192.168.85.5");
		InetAddress outcliAddress = InetAddress.getByName("192.168.85.6");
		
		//Class datagrampacket used in sending and receiving information
		DatagramPacket packet = new DatagramPacket(new byte[100], 100);
		DatagramPacket rpacket = new DatagramPacket(new byte[100], 100);
		DatagramPacket spacket = new DatagramPacket(new byte[100], 100);
		DatagramPacket packet2 = new DatagramPacket(new byte[100], 100);
		
		//Symbol '*' for end signal
		byte[] byteBuffer = "*".getBytes();
		
		//Bind to desired transmission address
		DatagramPacket clipacket = new DatagramPacket(byteBuffer,
				byteBuffer.length, cliAddress, 4700);
		DatagramPacket rtrpacket = new DatagramPacket(byteBuffer,
				byteBuffer.length, rtrAddress, 4700);
		DatagramPacket srvpacket = new DatagramPacket(byteBuffer,
				byteBuffer.length, srvAddress, 4700);
		DatagramPacket outrtrpacket = new
		DatagramPacket(byteBuffer,byteBuffer.length,outrtrAddress,4700);
		DatagramPacket outclipacket = new
		DatagramPacket(byteBuffer,byteBuffer.length,outcliAddress,4700);
		
		//Variable array for storing applications
		ArrayList total_app = new ArrayList();

		//Receiving application information from client
		socket.receive(packet);
		String lencli = new String(packet.getData());//Construct string from type byte
		lencli = lencli.trim();						 //Delete additional spaces
		int lc = Integer.parseInt(lencli);			 //number of applications at client
		int count = 0;
		while (count < lc) {
			socket.receive(packet);
			Application tmp = new Application(packet.getData());
			total_app.add(tmp);
			count += 1;
		}

		//Receiving application information from router
		socket2.receive(rpacket);
		String lencli2 = new String(rpacket.getData());
		lencli2 = lencli2.trim();
		int lc2 = Integer.parseInt(lencli2);
		int count2 = 0;
		while (count2 < lc2) {
			socket2.receive(rpacket);
			Application tmp = new Application(rpacket.getData());
			total_app.add(tmp);
			count2 += 1;
		}

		//Receiving application information from server
		socket3.receive(spacket);
		String lencli3 = new String(spacket.getData());
		lencli3 = lencli3.trim();
		int lc3 = Integer.parseInt(lencli3);
		int count3 = 0;
		while (count3 < lc3) {
			socket3.receive(spacket);
			Application tmp = new Application(spacket.getData());
			total_app.add(tmp);
			count3 += 1;
		}

		//Distribution algorithms
		//Distribute applications based on their parameters
		int appnum = total_app.size();
		int[] comp = new int[appnum];
		int[] seq = new int[appnum];
		Application[] App = new Application[appnum];
		for (int i = 0; i < appnum; i++) {
			App[i] = (Application) total_app.get(i);
			seq[i] = i;
			comp[i] = App[i].Rin + App[i].Rout - App[i].Rin2 - App[i].Rs;
		}
		for (int i = appnum - 1; i > 0; i--) {
			for (int j = appnum - 1; j > appnum - 1 - i; j--) {
				if (comp[j - 1] < comp[j]) {
					int temp;
					temp = comp[j - 1];
					comp[j - 1] = comp[j];
					comp[j] = temp;
					temp = seq[j - 1];
					seq[j - 1] = seq[j];
					seq[j] = temp;
				}
			}
		}
		int flg = 0;
		for (int i = 0; i < appnum; i++) {
			if (comp[i] <= 0) {
				break;
			} else {
				flg += 1;
			}
		}
		switch (flg) {
		case 0:
			for (int i = 0; i < appnum; i++) {
				App[i].type = 3;
			}
			break;
		case 1:
			for (int i = 0; i < appnum; i++) {
				if (i == seq[0]) {
					App[i].type = 1;
				} else {
					App[i].type = 3;
				}
			}
			break;
		case 2:
			for (int i = 0; i < appnum; i++) {
				if (i == seq[0]) {
					App[i].type = 1;
				} else if (i == seq[1]) {
					App[i].type = 2;
				} else {
					App[i].type = 3;
				}
			}
			break;
		case 3:
			for (int i = 0; i < appnum; i++) {
				if (i == seq[0]) {
					App[i].type = 1;
				} else if (i == seq[1] || i == seq[2]) {
					App[i].type = 2;
				} else {
					App[i].type = 3;
				}
			}
			break;
		default:
			for (int i = 0; i < appnum; i++) {
				if (i == seq[0]) {
					App[i].type = 1;
				} else if (i == seq[1] || i == seq[2] || i == seq[3]) {
					App[i].type = 2;
				} else {
					App[i].type = 3;
				}
			}
			break;
		}
		
		//Control and information transmission for different applications
		//Number of iterations is total number of applications
		//Each application has its unique iteration
		for (int i = 0; i < appnum; i++) {
			
			//Control parameters for client, router and server
			int flag1, flag2, flag3;
			String flagstr1, flagstr2, flagstr3;
			
			//Used for the manipulation of data
			int length1, length2, length3, length4;
			String str1, str2, str3, str4;
			String endflag = "*";
			
			//Execution of threads
			transmission task1 = new transmission();
			ExecutorService es = Executors.newFixedThreadPool(1);
			Future future1 = es.submit(task1);
			
			switch (App[i].type) {
			case 1:	//For applications placed at client
				//Sending Control information to other devices
				flag1 = 1;
				flag2 = 1;
				flag3 = 1;
				flagstr1 = Integer.toString(flag1);
				flagstr2 = Integer.toString(flag2);
				flagstr3 = Integer.toString(flag3);
				clipacket.setData(flagstr1.getBytes());
				rtrpacket.setData(flagstr2.getBytes());
				srvpacket.setData(flagstr3.getBytes());
				sndsocket1.send(clipacket);
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				
				//Sending data information to other devices
				length1 = strlen(App[i].Rs);
				length2 = strlen(App[i].Rin2);
				str1 = Integer.toString(length1);
				str2 = Integer.toString(length2);
				rtrpacket.setData(str1.getBytes());
				srvpacket.setData(str1.getBytes());
				outrtrpacket.setData(str2.getBytes());
				outclipacket.setData(str2.getBytes());
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				sndsocket4.send(outrtrpacket);
				sndsocket5.send(outclipacket);
				rtrpacket.setData(str2.getBytes());
				srvpacket.setData(str2.getBytes());
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				break;
			case 2:	//for applications placed at router
				//Sending Control information to other devices
				flag1 = 2;
				flag2 = 2;
				flag3 = 2;
				flagstr1 = Integer.toString(flag1);
				flagstr2 = Integer.toString(flag2);
				flagstr3 = Integer.toString(flag3);
				clipacket.setData(flagstr1.getBytes());
				rtrpacket.setData(flagstr2.getBytes());
				srvpacket.setData(flagstr3.getBytes());
				sndsocket1.send(clipacket);
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				
				//Sending Control information to other devices
				length1 = strlen(App[i].Rin);
				length2 = strlen(App[i].Rout);
				length3 = strlen(App[i].Rin2);
				length4 = strlen(App[i].Rs);
				str1 = Integer.toString(length1);
				str2 = Integer.toString(length2);
				str3 = Integer.toString(length3);
				str4 = Integer.toString(length4);
				clipacket.setData(str1.getBytes());
				rtrpacket.setData(str2.getBytes());
				srvpacket.setData(str3.getBytes());
				outrtrpacket.setData(str3.getBytes());
				outclipacket.setData(str3.getBytes());
				sndsocket1.send(clipacket);
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				sndsocket4.send(outrtrpacket);
				sndsocket5.send(outclipacket);
				srvpacket.setData(str4.getBytes());
				sndsocket3.send(srvpacket);
				break;
			case 3:	//for applications placed at server
				//Sending Control information to other devices
				flag1 = 3;
				flag2 = 3;
				flag3 = 3;
				flagstr1 = Integer.toString(flag1);
				flagstr2 = Integer.toString(flag2);
				flagstr3 = Integer.toString(flag3);
				clipacket.setData(flagstr1.getBytes());
				rtrpacket.setData(flagstr2.getBytes());
				srvpacket.setData(flagstr3.getBytes());
				sndsocket1.send(clipacket);
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				
				//Sending Control information to other devices
				length1 = strlen(App[i].Rin);
				length2 = strlen(App[i].Rout);
				length3 = strlen(App[i].Rin2);
				str1 = Integer.toString(length1);
				str2 = Integer.toString(length2);
				str3 = Integer.toString(length3);
				clipacket.setData(str1.getBytes());
				rtrpacket.setData(str1.getBytes());
				srvpacket.setData(str2.getBytes());
				outrtrpacket.setData(str3.getBytes());
				outclipacket.setData(str3.getBytes());
				sndsocket1.send(clipacket);
				sndsocket2.send(rtrpacket);
				sndsocket3.send(srvpacket);
				sndsocket4.send(outrtrpacket);
				sndsocket5.send(outclipacket);
				srvpacket.setData(str2.getBytes());
				sndsocket3.send(rtrpacket);
				break;
			default:
				System.out.println("Error");
				break;
			}
			
			Thread.sleep(10000);	//Buffer time to avoid errors
			
			//Sending end signals to end this iteration
			clipacket.setData(endflag.getBytes());
			rtrpacket.setData(endflag.getBytes());
			srvpacket.setData(endflag.getBytes());
			outrtrpacket.setData(endflag.getBytes());
			outclipacket.setData(endflag.getBytes());
			sndsocket1.send(clipacket);
			sndsocket2.send(rtrpacket);
			sndsocket3.send(srvpacket);
			sndsocket4.send(outrtrpacket);
			sndsocket5.send(outclipacket);
			
			//Close multiple threads
			es.shutdownNow();
		}
	}
}
