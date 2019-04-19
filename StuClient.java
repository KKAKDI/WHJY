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
			pln(ip + " �� ������ ����Ǿ����ϴ�."); // ����button
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);

			//Runnable Ŭ���� �߰� (���꾲���� ���)
			Runnable msg = new Msg(dos);
			t1 = new Thread(msg);
			t2 = new Thread(this);

			name();
			t1.start();
			t2.start();

		} catch (UnknownHostException uhe) {
			pln("�ش� ������ �������� �ʽ��ϴ�.");
			connect();
		} catch (IOException ie) {
			connect();
		} catch (NumberFormatException nfe) {
			pln("�������� �ʴ� ��Ʈ��ȣ�Դϴ�.");
			connect(); // ���ȣ��
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
			p(id + "���� �����ϼ̽��ϴ�.");
		} catch (IOException ie) {
		}

	}
	void choice() { // �� ����
		int i = 1;
		int j = 2;
		String str = (i != j) ? "��1" : "��2";
	}
	void procedure(String msg){//Ŭ���̾�Ʈ ����	
		String items[] = msg.split("/");
		int a = Integer.parseInt(items[1]);

		for(int i=2; i<(a+2); i++){
			scs.add(items[i]);
		}
	}
	public void run(){
		try{
			//���̵�Ȯ��
			id = dis.readUTF();
			pln("< ����� ���̵�� [ "+id+" ]�Դϴ�. > ");

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
			pln("������ �ٿ�ƽ��ϴ�. \n 3�� �Ŀ� ����˴ϴ�.");
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
		// ���꾲���� �������� - ����Ŭ�������� void msg �Űܿ�
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