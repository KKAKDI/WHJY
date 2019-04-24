import java.io.*;
import java.util.*;
import java.net.*;

//���ĵ�� ����
//Ŭ���̾�Ʈ ���� ���� �� ���� ���� ���� Ȯ�� �� ���ӽ����ϱ�
class StuServer extends Thread {
	// ��������
	ServerSocket ss;
	Socket gs; // ���Ӽ���
	Socket cs; // ������
	int port = 3524;
	boolean gm = true; // ������(game middle)���� �ƴ��� Ȯ���ϴ� ����
	boolean acc = true; // ��������(accpetClient) �ݺ��� ����
	GameLogic gl;
	boolean isStop; //������ ���Ḧ ���� ��������

	// ���ĵ�� ���
	StuCM cm;

	// Ŭ���̾�Ʈ ���� ����Ʈ
	int readyCount;
	Vector<StuCM> cv = new Vector<StuCM>();

	// �Է°�
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	StuServer() {
	}

	void init() {

		try {
			// ��������
			ss = new ServerSocket(port);
			pln(port + "����Ʈ���� ������ �����...");
		} catch (SocketException se) {
		} catch (IOException io) {
			pln("IO�ͼ����� �߻��Ͽ����ϴ�. �ý����� �����մϴ�");
			System.exit(0);
		}

		start();
	}

	void acceptClient() {
		try {
			// �ִ� Ŭ���̾�Ʈ �� 4��
			if (cv.size() <= 3 && gm)
				acc = true;
			if (!gm)
				acc = false;
			pln("gm : " + gm);
			while (acc) {
				pln("cv size : " + cv.size());
				cs = ss.accept();
				// gs = cs;

				pln("Ŭ���̾�Ʈ(" + cs.getInetAddress().getHostName() + ")�� ���ӵǾ���");

				// Ŭ���̾�Ʈ ���� ���� ������
				cm = new StuCM(this);

				// �ش������� ���͸���Ʈ�� �����ϱ�
				cv.add(cm);

				// ������
				cm.start();

				// �ο�����
				if (cv.size() >= 4)
					acc = false;
					setStop(false);
			}
		} catch (SocketException se) {
			pln("���������" + se + cs);
		} catch (IOException io) {
			pln("IO�ͼ���[acceptClient()]�� �߻��Ͽ����ϴ�" + io);
		}
	}

	// Ŭ���̾�Ʈ�� ���� �ޱ� (2��~4��)
	void ready4Client(StuCM cli) {
		readyCount = 0;

		if(cli.ready==true){
			//������� ����� Ȯ���ϱ�
			for (StuCM re : cv) {
				if (re.ready == true) readyCount++;
				cm.broadcast("mem","���ӷ��� ���("+re.id+")���� ����");
			}
			//�������� ��ε�ĳ����
			cm.broadcast("log",cli.id + "���� �غ� �Ϸ�Ǿ����ϴ�! ( �غ� �ο� " + readyCount + "/" + cv.size() + "�� )");
			pln(cli.id + "���� �غ� �Ϸ�Ǿ����ϴ�. ( �غ� �ο� " + readyCount + "/" + cv.size() + "�� )");
		}

		//������ ������ �� �ִ� ���� Ȯ��
		if (cv.size() < 3) {
			cm.broadcast("log","4���� �÷��̾ �־�� ������ ���۵˴ϴ�.");
			pln("2�� �̻��� �÷��̾ �־�� ������ ���۵˴ϴ�.");
		} else if ( cv.size() < 5) {
			//[����� = �����]�� ��, ���� ����
			if (readyCount == cv.size() & readyCount == 4) {
				setStop(false);
				gm=false;
				cm.broadcast("log","������ �����մϴ�");
				pln("������ �����մϴ�.");
				cm.broadcast("mem", "�������� ������� ����");	
				gl = new GameLogic(this, cv.size());
				gl.cntUser();
				//���ӽ��� ��ȣ
				cm.broadcast("start","���� ����");
				
				cm.broadcast("batting","���� ����");
				//�����ʱ�ȭ
				for (StuCM re : cv) {
					re.ready = false;
				}
			} else {
				cm.broadcast("log","���� �÷��̾� ��ΰ� ���� �ؾ� ������ ���۵˴ϴ�.");
				pln("���� �÷��̾� ��ΰ� ���� �ؾ� ������ ���۵˴ϴ�.");
			}
		}

		//���ӳ� �ʱ�ȭ
		if (gm==false) {
			readyCount = 0;
			acc = true;
			gm = true;
			setStop(true);
		}
	}

	// �������̵�
	public void run() {
		while(!isStop){
			acceptClient();
		}
	}

	//����������
	public void setStop(boolean isStop){
		this.isStop = isStop;
	}

	void pln(String str) {
		System.out.println(str);
	}

	void p(String str) {
		System.out.print(str);
	}

}
