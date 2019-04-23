import java.io.*;
import java.net.*;

class StuCM extends Thread {
	StuServer sts;
	Socket cs;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;

	// 클라이언트 정보
	String id;
	boolean ready = false; // f노레디, t레디
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

	// 입장 및 퇴장
	void listen() {
		String msg = "";
		try {
			// 사용자의 아이디값 입력받기
			id = dis.readUTF();

			// 아이디 중복확인
			while (true) {
				int userCount = 0;
				String ods = "I"; // 옹달샘

				// 사용자 리스트 비교
				for (StuCM client : sts.cv) {
					if (id.equals(client.id))
						userCount++;
				}

				if (userCount < 2) { // 중복없을 때
					id = id;
					break;
				} else { // 중복일 때
					id = id + ods;
				}
			}
			isYourId(id);

			broadcast("log", id + "님이 입장하셨습니다. ( 총 인원 " + sts.cv.size() + "명 )");
			sts.pln(id + "님이 입장하셨습니다. ( 총 인원 " + sts.cv.size() + "명 )");

			while (true) {
				msg = dis.readUTF();
				readygo(msg);
			}

		} catch (IOException ie) {
			//레디상태에서 나갔을 때 값 초기화
			if(ready==true){
				sts.readyCount--;
				sts.ready4Client(this);
			}
			//나간 사용자 정보 삭제
			sts.cv.remove(this);
			broadcast("log", id + "님이 퇴장하셨습니다. ( 총 인원 " + sts.cv.size() + "명 )");
			sts.pln(id + "님이 퇴장하셨습니다. ( 총 인원 " + sts.cv.size() + "명 )");
			//현사용자가 3명이하면 새 사용자를 받는다.
			if (sts.cv.size() <= 3) {
				sts.acc = true;
				sts.setStop(true);
			}
		} finally {
			closeA();
		}
	}

	// 준비상태
	void readygo(String msg) {
		// 사용자가 gidaktp(향마세)라 입력하면 레디됨
		if (msg.equals("gidaktp")) {
			if(ready==false){
				ready = true;
				sts.ready4Client(this);
			}
			// 사용자가 tpgidak(세향마)라 입력하면 레디취소됨
		} else if (msg.equals("tpgidak")) {
			if (sts.cv.size()>=2 & sts.readyCount == sts.cv.size()) {
				broadcast("log","게임이 시작되어 레디를 취소할 수 없습니다.");
			}else{
				if(ready==true){
					ready = false;
					sts.readyCount--;
					broadcast("log", id + "님이 대기 상태입니다. ( 준비 인원 " + sts.readyCount + "/" + sts.cv.size() + "명 )");
					sts.pln(id + "님이 대기 상태입니다. ( 준비 인원 " + sts.readyCount + "/" + sts.cv.size() + "명 )");
					sts.ready4Client(this);
				}		
			}
		}else if(msg.equals("dhgkaak")) {
			broadcast("end","게임 끝");
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

	//닉넴설정
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

	//일반채팅
	void broadcast(String msg){
		try {
			for (StuCM modul : sts.cv) {
				modul.dos.writeUTF(msg);
				modul.dos.flush();
			}
		} catch (IOException ie) {}
	}

	//프로토콜정보
	//flag ~ 로그(log), 카드(card1,2), 족보(power), 승리(judge), 배팅(batting), 참가인원(mem), 게임끝(end)
	void broadcast(String flag, String msg) {
		for (StuCM modul : sts.cv) {
			try {
				//로그
				if(flag.equals("log")){
					modul.dos.writeUTF("#log_"+msg);
				//참가인원
				}else if(flag.equals("mem")){
					sts.pln(modul.id+"클라이언트에 " + msg);
					String token = "#mem_";
					String t = "//";
					String size = Integer.toString(sts.cv.size());
					token = token + size;

					for(StuCM gmem : sts.cv){
						token = token + t + gmem.id;
					}
					modul.dos.writeUTF(token);
					//현재 게임로직에서 수행 중
//				//족보
//				}else if(flag.equals("power")){
//					modul.dos.writeUTF("#power_"+msg);
//				//승리
//				}else if(flag.equals("judge")){
//					modul.dos.writeUTF("#judge_"+msg);
//				//배팅
				}else if(flag.equals("batting")){
					modul.dos.writeUTF("#batting_"+msg);
				//게임끝
				}else if(flag.equals("end")){
					modul.dos.writeUTF("#end_"+msg);
				}else if(flag.equals("start")) { //게임시작 프로토콜
					modul.dos.writeUTF("#start_"+msg);
				}
				else{
					sts.pln("존재않는 프로토콜");
				}

				modul.dos.flush();
			}catch (IOException ie) {}
		}
	}

}