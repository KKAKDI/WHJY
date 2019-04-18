import java.io.*;
import java.net.*;

//스탠드업 채팅 및 레디모듈  //서버의 로그 및 채팅 브로드캐스팅 및 레디입력 받기
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
		//readygo();
	}
	void listen(){
		String msg = "";
		try{
			id = dis.readUTF();
			if(id.equals("User")){
				sts.userCount ++;
				String uc = String.valueOf(sts.userCount);
				id = id+uc;
			}
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
				sts.a = true;
				sts.acceptClient();
			}
		}finally{
			closeA();
		}
	}

	void readygo(String msg){
			if(msg.equals("gidaktp")){
				ready = true;
				sts.ready4Client();
				broadcast(id+"님이 준비 완료되었습니다! ( 준비 인원 " +sts.readyCount+"/"+sts.cv.size()+"명 )");
				sts.pln(id+"님이 준비 완료되었습니다. ( 준비 인원 " +sts.readyCount+"/"+sts.cv.size()+"명 )");
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
	void broadcast(String msg){
		try{
			for(StuCM modul : sts.cv){
				modul.dos.writeUTF(msg);
				modul.dos.flush();
			}
		}catch(IOException ie){}
	}
}