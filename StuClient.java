import java.net.*;
import java.io.*;
import java.util.*;

class StuClient extends Thread {
	String ip;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Socket s;
	InputStream is;
	DataInputStream dis;
	OutputStream os;
	DataOutputStream dos;
	String id;
	boolean rop = false;
	int port = 3524;
	ClientLoginUI clui = new ClientLoginUI();
	Thread t1, t2;
	Vector<String> scs = new Vector<String>();

	StuClient(ClientLoginUI clui) {
		this.clui = clui;
	}

	void connect() {
		try {
			ip = clui.ipstart;
			if (ip != null)
				ip = ip.trim();

			s = new Socket(ip, port);
			pln(ip + " 가 서버와 연결되었습니다."); // 연결button
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);

			//Runnable 클래스 추가 (서브쓰레드 사용)
			Runnable msg = new Msg(dos);
			t1 = new Thread(msg);
			t2 = new Thread(this);

			name();
			t1.start();
			t2.start();

		} catch (UnknownHostException uhe) {
			pln("해당 서버가 존재하지 않습니다.");
			connect();
		} catch (IOException ie) {
			connect();
		} catch (NumberFormatException nfe) {
			pln("존재하지 않는 포트번호입니다.");
			connect(); // 재귀호출
		}
	}

	void name() {
		try {
			id = clui.idstart;
			if (id != null)
				id.trim();
			if (id.length() == 0)
				id = "User";
			dos.writeUTF(id);
			dos.flush();
			p(id + "님이 입장하셨습니다.");
		} catch (IOException ie) {
		}

	}
	void choice() { // 패 선택
		int i = 1;
		int j = 2;
		String str = (i != j) ? "패1" : "패2";
	}
	void procedure(String msg){//클라이언트 순서	
		String items[] = msg.split("/");
		int a = Integer.parseInt(items[1]);

		for(int i=2; i<(a+2); i++){
			scs.add(items[i]);
		}
	}
	public void run(){
		try{
			//아이디확정
			id = dis.readUTF();
			pln("< 당신의 아이디는 [ "+id+" ]입니다. > ");

			while(true){
				String msg = dis.readUTF();
				if(msg.contains("@a_r_b_o_k")){
					procedure(msg);
				}else{
					pln(msg);
				}
				//choice();
			}
		} catch (IOException ie) {
			pln("서버가 다운됐습니다. \n 3초 후에 종료됩니다.");
			try {
				Thread.sleep(3000);
				System.exit(0);
			} catch (InterruptedException ite) {
			}
		} finally {
			closeA();
		}
	}
	void closeA() {
		try {
			if (dos != null)
				dos.close();
			if (dis != null)
				dis.close();
			if (os != null)
				os.close();
			if (is != null)
				is.close();
			if (s != null)
				s.close();
		} catch (IOException ie) {
		}
	}
	void pln(String str) {
		System.out.println(str);
	}
	void p(String str) {
		System.out.print(str);
	}
}
		// 서브쓰레드 선언해줌 - 메인클래스에서 void msg 옮겨옴
class Msg implements Runnable {
	DataOutputStream dos;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	Msg(DataOutputStream dos) {
		this.dos = dos;
	}

	public void run() {
		msg();
	}
	void msg() {
		String msg = "";

		try {
			while (true) {
				msg = br.readLine();
				dos.writeUTF(msg);
				dos.flush();
			}
		} catch (IOException ie) {
			try {
				if (dos != null)
					dos.close();
			} catch (Exception e) {
			}
		}
	}
}

/*class Tmsg implements Runnable{

	void msg(){
	}
}
*/