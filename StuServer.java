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

			}
		} catch (SocketException se) {

			pln("���������" + se + cs);
		} catch (IOException io) {
			pln("IO�ͼ���[acceptClient()]�� �߻��Ͽ����ϴ�" + io);
		}
	}

	// Ŭ���̾�Ʈ�� ���� �ޱ� (2��~4��)
	void ready4Client() {

		readyCount = 0;

		for (StuCM re : cv) {
			if (re.ready == true)
				readyCount++;
		}

		if (cv.size() < 2) {
			cm.broadcast("2�� �̻��� �÷��̾ �־�� ������ ���۵˴ϴ�.");
			pln("2�� �̻��� �÷��̾ �־�� ������ ���۵˴ϴ�.");
		} else if (cv.size() >= 2 & cv.size() < 5) {
			if (readyCount == cv.size() & readyCount >= 2) {
				gm = false;
				// acceptClient();
				cm.broadcast("������ �����մϴ�");
				pln("������ �����մϴ�.");
				gl = new GameLogic(this, cv.size());
				gl.start();

			} else {
				cm.broadcast("���� �÷��̾� ��ΰ� ���� �ؾ� ������ ���۵˴ϴ�.");
				pln("���� �÷��̾� ��ΰ� ���� �ؾ� ������ ���۵˴ϴ�.");
			}
		}
	}

	// �������̵�
	public void run() {
		acceptClient();
	}

	void pln(String str) {
		System.out.println(str);
	}

	void p(String str) {
		System.out.print(str);
	}

}
