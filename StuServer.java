import java.io.*;
import java.util.*;
import java.net.*;

//스탠드업 서버
//클라이언트 정보 저장 및 게임 레디 정보 확인 후 게임실행하기
class StuServer extends Thread{
	//서버정보
	ServerSocket ss;
	Socket gs;	//게임소켓
	Socket cs;		//모듈소켓
	int port;
	boolean gm = true;		//게임중(game middle)인지 아닌지 확인하는 조건
	boolean acc = true;		//서버입장(accpetClient) 반복문 조건

	//스탠드업 모듈
	StuCM cm;

	//클라이언트 정보 리스트
	int userCount = 0;
	int readyCount;
	Vector<StuCM> cv = new Vector<StuCM>();

	//입력값
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	StuServer(){
		init();
	}

	void init(){
		//포트선택
		p("실행할 서비스 포트 번호(기본3524) : ");
		String line = "";

		try{
			line = br.readLine();
			line = line.trim();
		}catch(IOException io){
			pln("IO익셉션이 발생하였습니다. 시스템을 종료합니다");
			System.exit(0);
		}
		
		if(line.equals("")){
			port = 3524;
		}else{
			try{
				int i = Integer.parseInt(line);
				if(i<1024 || i>65535){
					pln("\n 1025~66535번사이의 포트를 입력해주세요 \n");
					init();
				}else{
					port = i;
				}
			}catch(NumberFormatException nfe){
				pln("\n 숫자를 입력해주세요 \n");
				init();
			}
		}

		try{
			//서버오픈	
			ss = new ServerSocket(port);
			pln(port+"번포트에서 서버가 대기중...");
		}catch(IOException io){}

		acceptClient();
	}

	void acceptClient(){
		try{
			//최대 클라이언트 수 4명
			if(cv.size()<=3 && gm) acc = true;
			if(!gm) acc = false;
			pln("gm : "+gm);
			while(acc){
				pln("cv size : "+cv.size());
				cs = ss.accept();
				gs = cs;

				pln("클라이언트("+cs.getInetAddress().getHostName()+")와 접속되었다");
				
				//클라이언트 정보 모듈로 보내기
				cm = new StuCM(this);

				//해당모듈정보 벡터리스트에 저장하기
				cv.add(cm);

				//모듈시작
				cm.start();

				//인원제한
				if(cv.size()>=4) acc = false;
			}
		}catch(IOException io){
			pln("IO익셉션[acceptClient()]이 발생하였습니다" + io);
		}
	}

	//클라이언트의 레디 받기 (2명~4명)
	void ready4Client(){
		boolean readyValue = false;
		readyCount = 0;

		for(StuCM re : cv){
			if(re.ready==true) readyCount++;
		}

		if(cv.size()<2){
			cm.broadcast("2명 이상의 플레이어가 있어야 게임이 시작됩니다.");
			pln("2명 이상의 플레이어가 있어야 게임이 시작됩니다.");
		}else if(cv.size()>=2 & cv.size()<5){
			if(readyCount==cv.size() & readyCount>=2){
				gm = false;
				//acceptClient();
				cm.broadcast("게임을 시작합니다");
				pln("게임을 시작합니다.");
			}else{
				cm.broadcast("참가 플레이어 모두가 레디를 해야 게임이 시작됩니다.");
				pln("참가 플레이어 모두가 레디를 해야 게임이 시작됩니다.");
			}
		}
	}

	//오버라이딩
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
