import java.io.*;
import java.net.*;

class StuCM extends Thread {
	StuServer sts;
	Socket cs;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;

	// Ŭ���̾�Ʈ ����
	String id;
	boolean ready = false; // f�뷹��, t����
	int cash = 1000;
	String statement;

	StuCM(StuServer sts) {
		this.sts = sts;
		try {
			is = sts.cs.getInputStream();
			os = sts.cs.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		} catch (IOException ie) {
		}
	}

	public void run() {
		listen();
	}

	// ���� �� ����
	void listen() {
		String msg = "";
		try {
			// ������� ���̵� �Է¹ޱ�
			id = dis.readUTF();

			// ���̵� �ߺ�Ȯ��
			while (true) {
				int userCount = 0;
				String ods = "I"; // �˴޻�

				// ����� ����Ʈ ��
				for (StuCM client : sts.cv) {
					if (id.equals(client.id))
						userCount++;
				}

				if (userCount < 2) { // �ߺ����� ��
					id = id;
					break;
				} else { // �ߺ��� ��
					id = id + ods;
				}
			}
			isYourId(id);

			broadcast("log", id + "���� �����ϼ̽��ϴ�. ( �� �ο� " + sts.cv.size() + "�� )");
			sts.pln(id + "���� �����ϼ̽��ϴ�. ( �� �ο� " + sts.cv.size() + "�� )");

			while (true) {
				msg = dis.readUTF();
				readygo(msg);
			}

		} catch (IOException ie) {
			//������¿��� ������ �� �� �ʱ�ȭ
			if(ready==true){
				sts.readyCount--;
				sts.ready4Client(this);
			}
			//���� ����� ���� ����
			sts.cv.remove(this);
			broadcast("log", id + "���� �����ϼ̽��ϴ�. ( �� �ο� " + sts.cv.size() + "�� )");
			sts.pln(id + "���� �����ϼ̽��ϴ�. ( �� �ο� " + sts.cv.size() + "�� )");
			//������ڰ� 3�����ϸ� �� ����ڸ� �޴´�.
			if (sts.cv.size() <= 3) {
				sts.acc = true;
				sts.setStop(true);
			}
		} finally {
			closeA();
		}
	}

	// �غ����
	void readygo(String msg) {
		// ����ڰ� gidaktp(�⸶��)�� �Է��ϸ� �����
		if (msg.equals("gidaktp")) {
			if(ready==false){
				ready = true;
				sts.ready4Client(this);
			}
			// ����ڰ� tpgidak(���⸶)�� �Է��ϸ� ������ҵ�
		} else if (msg.equals("tpgidak")) {
			if (sts.cv.size()>=2 & sts.readyCount == sts.cv.size()) {
				broadcast("log","������ ���۵Ǿ� ���� ����� �� �����ϴ�.");
			}else{
				if(ready==true){
					ready = false;
					sts.readyCount--;
					broadcast("log", id + "���� ��� �����Դϴ�. ( �غ� �ο� " + sts.readyCount + "/" + sts.cv.size() + "�� )");
					sts.pln(id + "���� ��� �����Դϴ�. ( �غ� �ο� " + sts.readyCount + "/" + sts.cv.size() + "�� )");
					sts.ready4Client(this);
				}		
			}
		}else if(msg.equals("dhgkaak")) {
			broadcast("end","���� ��");
		}
		else{
			broadcast(id + " : " + msg);
			sts.pln(id + " : " + msg);
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
			if (cs != null)
				cs.close();
		} catch (IOException ie) {
		}
	}

	//�гۼ���
	void isYourId(String id) {
		try {
			for (StuCM modul : sts.cv) {
				if (modul.id.equals(id)) {
					modul.dos.writeUTF(id);
					modul.dos.flush();
				}
			}
		} catch (IOException ie) {
		}
	}

	//�Ϲ�ä��
	void broadcast(String msg){
		try {
			for (StuCM modul : sts.cv) {
				modul.dos.writeUTF(msg);
				modul.dos.flush();
			}
		} catch (IOException ie) {}
	}

	//������������
	//flag ~ �α�(log), ī��(card1,2), ����(power), �¸�(judge), ����(batting), �����ο�(mem), ���ӳ�(end)
	void broadcast(String flag, String msg) {
		for (StuCM modul : sts.cv) {
			try {
				//�α�
				if(flag.equals("log")){
					modul.dos.writeUTF("#log_"+msg);
				//�����ο�
				}else if(flag.equals("mem")){
					sts.pln(modul.id+"Ŭ���̾�Ʈ�� " + msg);
					String token = "#mem_";
					String t = "//";
					String size = Integer.toString(sts.cv.size());
					token = token + size;

					for(StuCM gmem : sts.cv){
						token = token + t + gmem.id;
					}
					modul.dos.writeUTF(token);
					//���� ���ӷ������� ���� ��
//				//����
//				}else if(flag.equals("power")){
//					modul.dos.writeUTF("#power_"+msg);
//				//�¸�
//				}else if(flag.equals("judge")){
//					modul.dos.writeUTF("#judge_"+msg);
//				//����
				}else if(flag.equals("batting")){
					modul.dos.writeUTF("#batting_"+msg);
				//���ӳ�
				}else if(flag.equals("end")){
					modul.dos.writeUTF("#end_"+msg);
				}else if(flag.equals("start")) { //���ӽ��� ��������
					modul.dos.writeUTF("#start_"+msg);
				}
				else{
					sts.pln("����ʴ� ��������");
				}

				modul.dos.flush();
			}catch (IOException ie) {}
		}
	}

}