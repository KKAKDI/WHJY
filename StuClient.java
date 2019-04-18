import java.net.*;
import java.io.*;

class  StuClient extends Thread {
	String ip;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Socket s;
	InputStream is;
	DataInputStream dis;
	OutputStream os;
	DataOutputStream dos;
	String id;
	boolean rop = false;

	StuClient(){
		connect();
	}
	void connect(){
		try{
			p("���� IP : "); // (�⺻ IP : 127.0.0.1)
			ip=br.readLine();
			if(ip != null) ip=ip.trim();
			if(ip.length() == 0) ip="127.0.0.1"; //ä�� ip 203.236.209.207

			p("��Ʈ : "); //(�⺻ port : 3524) 
			String portP = br.readLine();
			if(portP != null) portP=portP.trim();
			if(portP.length() == 0) portP="3524";
			int port = Integer.parseInt(portP);

			s = new Socket(ip,port);
			pln("������ ����Ǿ����ϴ�."); //����button
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			
			start();
			name();
		}catch(UnknownHostException uhe){ 
			pln("�ش� ������ �������� �ʽ��ϴ�.");
			connect();
		}catch(IOException ie){
			connect();
		}catch(NumberFormatException nfe){
			pln("�������� �ʴ� ��Ʈ��ȣ�Դϴ�.");
			connect(); //���ȣ��
		}
	}
	void name(){
		p( "�г��� :  ");
		try{
			id = br.readLine();
			if(id != null) id.trim();
			if(id.length() ==0) id="User";
			dos.writeUTF(id);
			dos.flush();
			msg();

		}catch(IOException ie){}
	}
	void choice(){ //�� ����
		int i = 1;
		int j = 2;
		String str = (i!=j)? "��1" : "��2";
	}
	public void run(){
		try{
			while(true){
				String msg = dis.readUTF();
				pln(msg);
				//choice();
			}
		}catch(IOException ie){
			pln("������ �ٿ�ƽ��ϴ�. \n 3�� �Ŀ� ����˴ϴ�.");
			try{
				Thread.sleep(3000);
				System.exit(0);
			}catch(InterruptedException ite){}
		}finally{
			closeA();
		}
	}
	void msg(){
		String msg = "";
		try{
			while(true){
				msg = br.readLine();
				dos.writeUTF(msg);
				dos.flush();
			}
		}catch(IOException ie){
			closeA();
		}
	}
	void closeA(){
		try{
			if(dos != null) dos.close();
			if(dis != null) dis.close();
			if(os != null) os.close();
			if(is != null) is.close();
			if(s != null) s.close();
		}catch(IOException ie){}
	}
	void pln(String str){
		System.out.println(str);
	}
	void p(String str){
		System.out.print(str);
	}
	public static void main(String[] args) {
		new StuClient();
	}
}