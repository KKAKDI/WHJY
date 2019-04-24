import java.io.*;
import java.util.*;
import java.net.*;

//스탠드업 서버
//클라이언트 정보 저장 및 게임 레디 정보 확인 후 게임실행하기
class StuServer extends Thread {
	// 서버정보
	ServerSocket ss;
	Socket gs; // 게임소켓
	Socket cs; // 모듈소켓
	int port = 3524;
	boolean gm = true; // 게임중(game middle)인지 아닌지 확인하는 조건
	boolean acc = true; // 서버입장(accpetClient) 반복문 조건
	GameLogic gl;
	boolean isStop; //쓰레드 종료를 위한 변수설정

	// 스탠드업 모듈
	StuCM cm;

	// 클라이언트 정보 리스트
	int readyCount;
	Vector<StuCM> cv = new Vector<StuCM>();

	// 입력값
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	StuServer() {
	}

	void init() {

		try {
			// 서버오픈
			ss = new ServerSocket(port);
			pln(port + "번포트에서 서버가 대기중...");
		} catch (SocketException se) {
		} catch (IOException io) {
			pln("IO익셉션이 발생하였습니다. 시스템을 종료합니다");
			System.exit(0);
		}

		start();
	}

	void acceptClient() {
		try {
			// 최대 클라이언트 수 4명
			if (cv.size() <= 3 && gm)
				acc = true;
			if (!gm)
				acc = false;
			pln("gm : " + gm);
			while (acc) {
				pln("cv size : " + cv.size());
				cs = ss.accept();
				// gs = cs;

				pln("클라이언트(" + cs.getInetAddress().getHostName() + ")와 접속되었다");

				// 클라이언트 정보 모듈로 보내기
				cm = new StuCM(this);

				// 해당모듈정보 벡터리스트에 저장하기
				cv.add(cm);

				// 모듈시작
				cm.start();

				// 인원제한
				if (cv.size() >= 4)
					acc = false;
					setStop(false);
			}
		} catch (SocketException se) {
			pln("서버종료됨" + se + cs);
		} catch (IOException io) {
			pln("IO익셉션[acceptClient()]이 발생하였습니다" + io);
		}
	}

	// 클라이언트의 레디 받기 (2명~4명)
	void ready4Client(StuCM cli) {
		readyCount = 0;

		if(cli.ready==true){
			//사용자의 레디수 확인하기
			for (StuCM re : cv) {
				if (re.ready == true) readyCount++;
				cm.broadcast("mem","게임레디 멤버("+re.id+")정보 전달");
			}
			//레디정보 브로드캐스팅
			cm.broadcast("log",cli.id + "님이 준비 완료되었습니다! ( 준비 인원 " + readyCount + "/" + cv.size() + "명 )");
			pln(cli.id + "님이 준비 완료되었습니다. ( 준비 인원 " + readyCount + "/" + cv.size() + "명 )");
		}

		//게임을 시작할 수 있는 조건 확인
		if (cv.size() < 3) {
			cm.broadcast("log","4명의 플레이어가 있어야 게임이 시작됩니다.");
			pln("2명 이상의 플레이어가 있어야 게임이 시작됩니다.");
		} else if ( cv.size() < 5) {
			//[레디수 = 사람수]일 때, 게임 시작
			if (readyCount == cv.size() & readyCount == 4) {
				setStop(false);
				gm=false;
				cm.broadcast("log","게임을 시작합니다");
				pln("게임을 시작합니다.");
				cm.broadcast("mem", "게임참가 멤버정보 전달");	
				gl = new GameLogic(this, cv.size());
				gl.cntUser();
				//게임시작 신호
				cm.broadcast("start","게임 시작");
				
				cm.broadcast("batting","배팅 시작");
				//레디초기화
				for (StuCM re : cv) {
					re.ready = false;
				}
			} else {
				cm.broadcast("log","참가 플레이어 모두가 레디를 해야 게임이 시작됩니다.");
				pln("참가 플레이어 모두가 레디를 해야 게임이 시작됩니다.");
			}
		}

		//게임끝 초기화
		if (gm==false) {
			readyCount = 0;
			acc = true;
			gm = true;
			setStop(true);
		}
	}

	// 오버라이딩
	public void run() {
		while(!isStop){
			acceptClient();
		}
	}

	//쓰레드종료
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
