import java.io.*;
import java.util.*;
import java.net.*;

//���ĵ�� ����
//Ŭ���̾�Ʈ ���� ���� �� ���� ���� ���� Ȯ�� �� ���ӽ����ϱ�
class StuServer extends Thread{
	//��������
	ServerSocket ss;
	Socket gs;	//���Ӽ���
	Socket cs;		//������
	int port;
	boolean gm = true;		//������(game middle)���� �ƴ��� Ȯ���ϴ� ����
	boolean acc = true;		//��������(accpetClient) �ݺ��� ����

	//���ĵ�� ���
	StuCM cm;

	//Ŭ���̾�Ʈ ���� ����Ʈ
	int userCount = 0;
	int readyCount;
	Vector<StuCM> cv = new Vector<StuCM>();

	//�Է°�
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	StuServer(){
		init();
	}

	void init(){
		//��Ʈ����
		p("������ ���� ��Ʈ ��ȣ(�⺻3524) : ");
		String line = "";

		try{
			line = br.readLine();
			line = line.trim();
		}catch(IOException io){
			pln("IO�ͼ����� �߻��Ͽ����ϴ�. �ý����� �����մϴ�");
			System.exit(0);
		}
		
		if(line.equals("")){
			port = 3524;
		}else{
			try{
				int i = Integer.parseInt(line);
				if(i<1024 || i>65535){
					pln("\n 1025~66535�������� ��Ʈ�� �Է����ּ��� \n");
					init();
				}else{
					port = i;
				}
			}catch(NumberFormatException nfe){
				pln("\n ���ڸ� �Է����ּ��� \n");
				init();
			}
		}

		try{
			//��������	
			ss = new ServerSocket(port);
			pln(port+"����Ʈ���� ������ �����...");
		}catch(IOException io){}

		acceptClient();
	}

	void acceptClient(){
		try{
			//�ִ� Ŭ���̾�Ʈ �� 4��
			if(cv.size()<=3 && gm) acc = true;
			if(!gm) acc = false;
			pln("gm : "+gm);
			while(acc){
				pln("cv size : "+cv.size());
				cs = ss.accept();
				gs = cs;

				pln("Ŭ���̾�Ʈ("+cs.getInetAddress().getHostName()+")�� ���ӵǾ���");
				
				//Ŭ���̾�Ʈ ���� ���� ������
				cm = new StuCM(this);

				//�ش������� ���͸���Ʈ�� �����ϱ�
				cv.add(cm);

				//������
				cm.start();

				//�ο�����
				if(cv.size()>=4) acc = false;
			}
		}catch(IOException io){
			pln("IO�ͼ���[acceptClient()]�� �߻��Ͽ����ϴ�" + io);
		}
	}

	//Ŭ���̾�Ʈ�� ���� �ޱ� (2��~4��)
	void ready4Client(){
		boolean readyValue = false;
		readyCount = 0;

		for(StuCM re : cv){
			if(re.ready==true) readyCount++;
		}

		if(cv.size()<2){
			cm.broadcast("2�� �̻��� �÷��̾ �־�� ������ ���۵˴ϴ�.");
			pln("2�� �̻��� �÷��̾ �־�� ������ ���۵˴ϴ�.");
		}else if(cv.size()>=2 & cv.size()<5){
			if(readyCount==cv.size() & readyCount>=2){
				gm = false;
				//acceptClient();
				cm.broadcast("������ �����մϴ�");
				pln("������ �����մϴ�.");
			}else{
				cm.broadcast("���� �÷��̾� ��ΰ� ���� �ؾ� ������ ���۵˴ϴ�.");
				pln("���� �÷��̾� ��ΰ� ���� �ؾ� ������ ���۵˴ϴ�.");
			}
		}
	}

	//�������̵�
	public void run(){

	}

	void pln(String str){
		System.out.println(str);
	}

	void p(String str){
		System.out.print(str);
	}

	public static void main(String[] args){
		new StuServer();
	}
}
