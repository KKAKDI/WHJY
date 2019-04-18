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
			p("서버 IP : "); // (기본 IP : 127.0.0.1)
			ip=br.readLine();
			if(ip != null) ip=ip.trim();
			if(ip.length() == 0) ip="127.0.0.1"; //채연 ip 203.236.209.207

			p("포트 : "); //(기본 port : 3524) 
			String portP = br.readLine();
			if(portP != null) portP=portP.trim();
			if(portP.length() == 0) portP="3524";
			int port = Integer.parseInt(portP);

			s = new Socket(ip,port);
			pln("서버와 연결되었습니다."); //연결button
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			
			start();
			name();
		}catch(UnknownHostException uhe){ 
			pln("해당 서버가 존재하지 않습니다.");
			connect();
		}catch(IOException ie){
			connect();
		}catch(NumberFormatException nfe){
			pln("존재하지 않는 포트번호입니다.");
			connect(); //재귀호출
		}
	}
	void name(){
		p( "닉네임 :  ");
		try{
			id = br.readLine();
			if(id != null) id.trim();
			if(id.length() ==0) id="User";
			dos.writeUTF(id);
			dos.flush();
			msg();

		}catch(IOException ie){}
	}
	void choice(){ //패 선택
		int i = 1;
		int j = 2;
		String str = (i!=j)? "패1" : "패2";
	}
	public void run(){
		try{
			while(true){
				String msg = dis.readUTF();
				pln(msg);
				//choice();
			}
		}catch(IOException ie){
			pln("서버가 다운됐습니다. \n 3초 후에 종료됩니다.");
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