import java.io.*;
import java.net.*;

class StuCM extends Thread{
	StuServer sts;
	Socket cs;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;

	//클라이언트 정보
	String id;
	boolean ready = false; //f노레디, t레디
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

	//입장 및 퇴장
	void listen(){ 
		String msg = "";
		try{
			//사용자의 아이디값 입력받기
			id = dis.readUTF();

			//아이디 중복확인
			while(true){
				int userCount = 0;
				String ods = "I"; //옹달샘

				//사용자 리스트 비교
				for(StuCM client : sts.cv){
					if(id.equals(client.id)) userCount++;
				}

				if(userCount<2){	//중복없을 때
					id = id;
					break;
				}else{				//중복일 때
					id = id + ods;
				}
			}
			isYourId(id);

			broadcast(id+"님이 입장하셨습니다. ( 총 인원 " +sts.cv.size()+"명 )");
			sts.pln(id+"님이 입장하셨습니다. ( 총 인원 " +sts.cv.size()+"명 )");

			while(true){
				msg = dis.readUTF();
				readygo(msg);
			}

		}catch(IOException ie){
			sts.cv.remove(this);
			broadcast(id+"님이 퇴장하셨습니다. ( 총 인원 " +sts.cv.size()+"명 )");
			sts.pln(id+"님이 퇴장하셨습니다. ( 총 인원 " +sts.cv.size()+"명 )");
			if(sts.cv.size()<=3){
				sts.acc = true;
				sts.acceptClient();
			}
		}finally{
			closeA();
		}
	}

	//준비상태
	void readygo(String msg){ 
			//사용자가 gidaktp(향마세)라 입력하면 레디됨
			if(msg.equals("gidaktp")){
				ready = true;
				sts.ready4Client();
				broadcast(id+"님이 준비 완료되었습니다! ( 준비 인원 " +sts.readyCount+"/"+sts.cv.size()+"명 )");
				sts.pln(id+"님이 준비 완료되었습니다. ( 준비 인원 " +sts.readyCount+"/"+sts.cv.size()+"명 )");

			//사용자가 tpgidak(세향마)라 입력하면 레디취소됨
			}else if(msg.equals("tpgidak")){
				if(sts.readyCount==sts.cv.size()){
					broadcast("게임이 시작되어 레디를 취소할 수 없습니다.");
				}else{
					ready = false;
					sts.readyCount--;
					broadcast(id+"님이 대기 상태입니다. ( 준비 인원 " +sts.readyCount+"/"+sts.cv.size()+"명 )");
					sts.pln(id+"님이 대기 상태입니다. ( 준비 인원 " +sts.readyCount+"/"+sts.cv.size()+"명 )");
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
	
	//게임로직 실행 전에 게임참가 멤버 순서 및 정보 전달
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