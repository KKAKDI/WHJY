import java.io.*;
import java.net.*;

class StuCM extends Thread{
	StuServer sts;
	Socket cs;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;

	//Ŭ���̾�Ʈ ����
	String id;
	boolean ready = false; //f�뷹��, t����
	int cash = 1000;
	String statement;
	
	
	StuCM(StuServer sts){
		this.sts = sts;
		try{
			is = sts.cs.getInputStream();
			os = sts.cs.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		}catch(IOException ie){}
	}

	public void run(){
		listen();
	}

	//���� �� ����
	void listen(){ 
		String msg = "";
		try{
			//������� ���̵� �Է¹ޱ�
			id = dis.readUTF();

			//���̵� �ߺ�Ȯ��
			while(true){
				int userCount = 0;
				String ods = "I"; //�˴޻�

				//����� ����Ʈ ��
				for(StuCM client : sts.cv){
					if(id.equals(client.id)) userCount++;
				}

				if(userCount<2){	//�ߺ����� ��
					id = id;
					break;
				}else{				//�ߺ��� ��
					id = id + ods;
				}
			}
			isYourId(id);

			broadcast(id+"���� �����ϼ̽��ϴ�. ( �� �ο� " +sts.cv.size()+"�� )");
			sts.pln(id+"���� �����ϼ̽��ϴ�. ( �� �ο� " +sts.cv.size()+"�� )");

			while(true){
				msg = dis.readUTF();
				readygo(msg);
			}

		}catch(IOException ie){
			sts.cv.remove(this);
			broadcast(id+"���� �����ϼ̽��ϴ�. ( �� �ο� " +sts.cv.size()+"�� )");
			sts.pln(id+"���� �����ϼ̽��ϴ�. ( �� �ο� " +sts.cv.size()+"�� )");
			if(sts.cv.size()<=3){
				sts.acc = true;
				sts.acceptClient();
			}
		}finally{
			closeA();
		}
	}

	//�غ����
	void readygo(String msg){ 
			//����ڰ� gidaktp(�⸶��)�� �Է��ϸ� �����
			if(msg.equals("gidaktp")){
				ready = true;
				sts.ready4Client();
				broadcast(id+"���� �غ� �Ϸ�Ǿ����ϴ�! ( �غ� �ο� " +sts.readyCount+"/"+sts.cv.size()+"�� )");
				sts.pln(id+"���� �غ� �Ϸ�Ǿ����ϴ�. ( �غ� �ο� " +sts.readyCount+"/"+sts.cv.size()+"�� )");

			//����ڰ� tpgidak(���⸶)�� �Է��ϸ� ������ҵ�
			}else if(msg.equals("tpgidak")){
				if(sts.readyCount==sts.cv.size()){
					broadcast("������ ���۵Ǿ� ���� ����� �� �����ϴ�.");
				}else{
					ready = false;
					sts.readyCount--;
					broadcast(id+"���� ��� �����Դϴ�. ( �غ� �ο� " +sts.readyCount+"/"+sts.cv.size()+"�� )");
					sts.pln(id+"���� ��� �����Դϴ�. ( �غ� �ο� " +sts.readyCount+"/"+sts.cv.size()+"�� )");
					sts.ready4Client(); 
				}
			}else{
				broadcast(id+" : "+msg);
				sts.pln(id+" : " +msg);
			}
	}

	void closeA(){
		try{
			if(dos != null) dos.close();
			if(dis != null) dis.close();
			if(os != null) os.close();
			if(is != null) is.close();
			if(cs != null) cs.close();
		}catch(IOException ie){}
	}

	void isYourId(String id){
		try{
			for(StuCM modul : sts.cv){
				if(modul.id.equals(id)){
					modul.dos.writeUTF(id);
					modul.dos.flush();
				}
			}
		}catch(IOException ie){}
	}

	void broadcast(String msg){
		try{
			for(StuCM modul : sts.cv){
				modul.dos.writeUTF(msg);
				modul.dos.flush();
			}
		}catch(IOException ie){}
	}
	
	//���ӷ��� ���� ���� �������� ��� ���� �� ���� ����
	void isYourGmMem(){
		try{
			for(StuCM modul : sts.cv){
				String token = "@a_r_b_o_k";
				String t = "/";
				String size = Integer.toString(sts.cv.size());
				token = token + t + size;

				for(StuCM gmem : sts.cv){
					token = token + t + gmem.id;
				}
				modul.dos.writeUTF(token);
				modul.dos.flush();
			}
		}catch(IOException io){
			sts.pln("isYourGmMem Exception");
		}
	}
}