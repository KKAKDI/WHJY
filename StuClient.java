import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

class StuClient extends Thread {
	// 서버정보
	String ip;
	Socket s;
	int port = 3524;

	// 게임 참가 멤버정보
	String id;
	Vector<String>scs = new Vector<String>();	
	HashMap<String, Object>hm = new HashMap<String, Object>();

	ClientLoginUI clui;
	MainFrame mf;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	InputStream is;
	DataInputStream dis;
	OutputStream os;
	DataOutputStream dos;
	Thread t1;
	int cards1[] = new int[4];
	int cards2[] = new int[4];
	int cnt1, cnt2;
	Object user1[] = new Object[4];
	Object user2[] = new Object[4];

	StuClient(ClientLoginUI clui) {
		this.clui = clui;
	}

	void connect() {
		try {
			ip = clui.ipstart;

			if (ip != null)
				ip = ip.trim();

			s = new Socket(ip, port);
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			t1 = new Thread(this);

			mf = new MainFrame();
			mf.cliIN();
			protocol("#log_서버와 연결되었습니다."); // 연결button

			name();
			t1.start();
		} catch (UnknownHostException uhe) {
			pln("해당 서버가 존재하지 않습니다.");
			connect();
		} catch (IOException ie) {
			connect();
		} catch (NumberFormatException nfe) {
			pln("존재하지 않는 포트번호입니다.");
			connect(); // 재귀호출
		}
	}

	void name() {
		try {
			id = clui.idstart;
			if (id != null)
				id.trim();
			if (id.length() == 0)
				id = "User";
			dos.writeUTF(id);
			dos.flush();
			mf.jlj4.setText(id);
		} catch (IOException ie) {
		}
	}

	// 프로토콜에 따라 메소드 실행

	void protocol(String msg) {
		String items[] = msg.split("_");
		// 첫번째 패 선택
		if (items[0].equals("#card1")) {
			cards1[cnt1] = Integer.parseInt(items[1]);
			pln("" + cards1[cnt1]);			
			cnt1++;
			// 두번째 패 선택
		} else if (items[0].equals("#card2")) {
			cards2[cnt2] = Integer.parseInt(items[1]);
			pln("" + cards2[cnt2]);		
			cnt2++;
			// 족보확인
		} else if (items[0].equals("#power")) {		
			String memInfo[] =items[1].split("//");
			int user = Integer.parseInt(memInfo[0]);
			int power = Integer.parseInt(memInfo[1]);
			//idMatch(user,power);
		} else if (items[0].equals("#judge")) {
			pln("승 리");
			// 로그
		} else if (items[0].equals("#log")) {
			mf.jta1.append(items[1] + "\n");
			mf.jta1.setCaretPosition(mf.jta1.getDocument().getLength());
			// 게임참가 유저리스트 및 순서 저장
		} else if (items[0].equals("#mem")) {		
			mem(items[1]);
			// 게임 시작시 패 뿌리기 시작
		} else if (items[0].equals("#start")) {
			//가짜 패
			idMatch("");
		} else if (items[0].equals("#batting")) {
			batting();
			//패돌리기
			idMatch("dPfladl");			
			// 게임참가 유저리스트 초기화
		} else if (items[0].equals("#end")) {
			end();
		} else {
			pln("존재하지 않는 프로토콜입니다" + msg);
		}
	}	
	void batting() {
		pln("배팅시작 올 스탑");
		// 배팅 기능 구현 필요
		try {
			Thread.sleep(1000);
		} catch (Exception e) {		
		}	
		
//		try {
//			dos.writeUTF("dhgkaak");
//		} catch (IOException e) {
//		}
	}	
	ImageIcon power(int power) { //다른 사람 족보
		pln("족 보");
		
		ImageIcon icon =null;
		switch (power) {
		case 0:		icon = mf.i102;	break; //망통
		case 1:		icon = mf.i103;	break; //한끗
		case 2:		icon = mf.i104;break; //두끗
		case 3:		icon = mf.i105;	break; //세끗
		case 4:		icon = mf.i106;	break; //네끗
		case 5:		icon = mf.i107;	break; //다섯끗
		case 6:		icon =	mf.i108;break; //여섯끗
		case 7:		icon = mf.i109;	break; //일곱끗
		case 8:		icon = mf.i110;	break; //여덟끗
		case 9:		icon = mf.i111;	break; //갑오
		case 10:	icon = mf.i112;	break; //세륙
		case 11:	icon = mf.i113;	break; //장사
		case 12:	icon = mf.i114;	break; //장삥
		case 13:	icon = mf.i115;	break; //구삥
		case 14:	icon = mf.i116;	break; //독사
		case 15:	icon = mf.i117;	break; //알리
		case 16:	icon = mf.i118;	break;//사구파토
		case 17:	icon = mf.i119;	break; //삥땡
		case 18:	icon = mf.i120;	break; //이땡
		case 19:	icon = mf.i121;	break; //삼땡
		case 20:	icon = mf.i122;	break; //사땡
		case 21:	icon = mf.i123;	break; //오땡
		case 22:	icon = mf.i124;break; //육땡
		case 23:	icon = mf.i125;	break; //칠땡
		case 24:	icon = mf.i126;	break; //팔땡
		case 25:	icon = mf.i127;	break; //구땡
		case 26:	icon = mf.i129;	break;//멍텅구리 사구		
		case 40:	icon = mf.i128;//땡잡이
						break;
		case 50:	icon = mf.i130;	break; //장땡
		case 80:	icon = mf.i131;	break; //일삼광땡
		case 90:	icon = mf.i132;	break; //일팔광땡
		case 99:	icon = mf.i133;//암행어사
						break;
		case 100:	icon = mf.i134;break; //삼팔광땡
		default : icon = mf.i99; break;
		}
		return icon;
	}
	
	void mem(String item) { // 멤버 수+레디 유무
		scs.removeAllElements();
		pln(item);
		String memInfo[] =item.split("//");
		int memCount = Integer.parseInt(memInfo[0]);	
		pln(memCount+""); 
		int userCnt=0;
		if(memCount==1)userCnt =1;
		else if(memCount==2)userCnt=3;
		else if(memCount ==3)userCnt=5;
		else if(memCount==4)userCnt=7;
										
		for (int j = 0; j <=userCnt ; j++) {				
			switch (j) {						
				case 1:	scs.add(memInfo[j]); hm.put(memInfo[j], (Object) memInfo[j+1]);break;					
				case 3:	 scs.add(memInfo[j]); hm.put(memInfo[j], (Object) memInfo[j+1]);break;					
				case 5:	 scs.add(memInfo[j]); hm.put(memInfo[j], (Object) memInfo[j+1]);break;
				case 7:	scs.add(memInfo[j]); hm.put(memInfo[j], (Object) memInfo[j+1]); break;		
			}				
		}			
	}
	void end() { // 게임 끝나고 초기화
		cnt1 = 0;
		cnt2 = 0;
		for (int i = 0; i < cards1.length; i++) {
			cards1[i] = 0;
			cards2[i] = 0;
			mf.jlp1.setIcon(mf.i100);
			mf.jlp2.setIcon(mf.i100);
			mf.jlp3.setIcon(mf.i100);
			mf.jlp4.setIcon(mf.i100);
			mf.jlp5.setIcon(mf.i100);
			mf.jlp6.setIcon(mf.i100);
			mf.jlp7.setIcon(mf.i100);
			mf.jbp8.setIcon(mf.i100);
		}
		scs.removeAllElements();
	}
	ImageIcon myTree(int power){ // 내 족보
		ImageIcon icon =null;
		switch (power) {
		case 0:		icon = mf.i81;	break;
		case 1:		icon = mf.i81;	break;
		case 2:		icon = mf.i81;	break;
		case 3:		icon = mf.i81;	break;
		case 4:		icon = mf.i81;	break;
		case 5:		icon = mf.i81;	break;
		case 6:		icon =	mf.i81;	break;
		case 7:		icon = mf.i81;	break;
		case 8:		icon = mf.i81;	break;
		case 9:		icon = mf.i80;	break;
		case 10:	icon = mf.i79;	break;
		case 11:	icon = mf.i78;	break;
		case 12:	icon = mf.i77;	break;
		case 13:	icon = mf.i76;	break;
		case 14:	icon = mf.i75;	break;
		case 15:	icon = mf.i74;	break;
		case 16:	icon = mf.i82;	break;
		case 17:	icon = mf.i73;	break;
		case 18:	icon = mf.i73;	break;
		case 19:	icon = mf.i73;	break;
		case 20:	icon = mf.i73;	break;
		case 21:	icon = mf.i73;	break;
		case 22:	icon = mf.i73;	break;
		case 23:	icon = mf.i73;	break;
		case 24:	icon = mf.i73;	break;
		case 25:	icon = mf.i73;	break;
		case 26:	icon = mf.i82;	break;
		case 27:	icon = mf.i73;	break;
		case 40:	icon = mf.i70;//땡잡이
						break;
		case 80:	icon = mf.i72;	break;
		case 90:	icon = mf.i72;	break;
		case 99:	icon = mf.i70;//암행어사
						break;
		case 100:	icon = mf.i71;	break;
		default : icon = mf.i70; break;
		}
		return icon;
	}
	
	void idMatch(int us,int pr) { // 매치 오버로딩 for 족보
		try {
			int user = us;
			int power = pr;
			String user1ID = "";
			String user2ID = ""; 
			String user3ID = ""; 
			String user4ID = ""; 
			int a = 0;
			int b = 0;
			int c = 0;
			
			switch(user) {
				case 0 : user1ID = scs.get(0); break; //qq
				case 1 : user2ID = scs.get(1); break; //ww
				case 2 : user3ID = scs.get(2); break; //ee
				case 3 : user4ID = scs.get(3);break; //rr
			}
			for (int i = 0; i < scs.size(); i++) {
				if (id.equals(scs.get(i))) { //남쪽 내패 
					Thread.sleep(300);
					//mf.jlk1.setIcon(myTree(power));  //내 족보 표시 메소드 완성 요망
					//족보 라벨
				} else if (!(id.equals(scs.get(i)))&&user1ID.equals(scs.get(i))) { // 동쪽
					Thread.sleep(300);
					mf.jlj1.setIcon(power(power));
					a++;
				}  else  if (!(id.equals(scs.get(i)))&&user2ID.equals(scs.get(i))) { //북쪽
					Thread.sleep(300);
					mf.jlj2.setIcon(power(power));
					b++;
				} else if (!(id.equals(scs.get(i)))&&user3ID.equals(scs.get(i))) { //서쪽	
					Thread.sleep(300);
					mf.jlj3.setIcon(power(power));
					c++;
				} else {	//서쪽
					if(!(id.equals(scs.get(i)))&&user4ID.equals(scs.get(i))&&a==0) {//동쪽이 비었을 때 user4
						Thread.sleep(300);
						mf.jlj1.setIcon(power(power));
					}else if(!(id.equals(scs.get(i)))&&user4ID.equals(scs.get(i))&&b==0){//북쪽이 비었을 때 user4
						Thread.sleep(300);
						mf.jlj2.setIcon(power(power));
					}else {
						Thread.sleep(300);
						mf.jlj3.setIcon(power(power));
					}					
				}
			}			
		} catch (Exception e) {
			pln(e.getMessage());
		}
	}
	
	void idMatch(String str) { //매치 후 카드 뿌리기
		String user1ID = scs.get(0);
		String user2ID = scs.get(1); 
		String user3ID = scs.get(2); 
		String user4ID = scs.get(3); 
		int a = 0;
		int b = 0;
		int c = 0;
		String open = str;
		try {
			for (int i = 0; i < scs.size(); i++) {
				if (id.equals(scs.get(i))) { //남쪽 내패 
					Thread.sleep(300);
					mf.jlp7.setIcon(mf.cardMach(cards1[i]));					
					//버튼 클릭시 내패 까기 (구현 못할거 같음)
//					if(open.equals("zndWKrWkr")) {
//						mf.jbp8.setIcon(mf.cardMach(cards2[i]));
//						Thread.sleep(300);
//					}
					//족보 라벨
				} else if (!(id.equals(scs.get(i)))&&user1ID.equals(scs.get(i))) { // 동쪽
					Thread.sleep(300);
					mf.jlp3.setIcon(mf.i101);					
					a++;
					if(open.equals("dPfladl")) {
						mf.jlp3.setIcon(mf.cardMach(cards1[i]));						
					}
				}  else  if (!(id.equals(scs.get(i)))&&user2ID.equals(scs.get(i))) { //북쪽
					Thread.sleep(300);
					mf.jlp1.setIcon(mf.i101);					
					b++;
					if(open.equals("dPfladl")) {
						mf.jlp1.setIcon(mf.cardMach(cards1[i]));						
					}
				} else if (!(id.equals(scs.get(i)))&&user3ID.equals(scs.get(i))) { //서쪽	
					Thread.sleep(300);
					mf.jlp5.setIcon(mf.i101);					
					c++;
					if(open.equals("dPfladl")) {
						mf.jlp5.setIcon(mf.cardMach(cards1[i]));						
					}
				} else {	//서쪽
					if(!(id.equals(scs.get(i)))&&user4ID.equals(scs.get(i))&&a==0) {//동쪽이 비었을 때 user4
						Thread.sleep(300);
						mf.jlp3.setIcon(mf.i101);						
						if(open.equals("dPfladl")) {
							mf.jlp3.setIcon(mf.cardMach(cards1[i]));						
						}
					}else if(!(id.equals(scs.get(i)))&&user4ID.equals(scs.get(i))&&b==0){//북쪽이 비었을 때 user4
						Thread.sleep(300);
						mf.jlp1.setIcon(mf.i101);						
						if(open.equals("dPfladl")) {
							mf.jlp1.setIcon(mf.cardMach(cards1[i]));							
						}
					}else {
						Thread.sleep(300);
						mf.jlp5.setIcon(mf.i101);						
						if(open.equals("dPfladl")) {
							mf.jlp5.setIcon(mf.cardMach(cards1[i]));							
						}
					}					
				}
			}for (int i = 0; i < scs.size(); i++) {
				if (id.equals(scs.get(i))) { //남쪽 내패 					
					Thread.sleep(300);
					mf.jbp8.setIcon(mf.cardMach(cards2[i]));					
					//버튼 클릭시 내패 까기 (구현 못할거 같음)
//					if(open.equals("zndWKrWkr")) {
//						mf.jbp8.setIcon(mf.cardMach(cards2[i]));
//						Thread.sleep(300);
//					}
					//족보 라벨
				} else if (!(id.equals(scs.get(i)))&&user1ID.equals(scs.get(i))) { // 동쪽					
					Thread.sleep(300);
					mf.jlp4.setIcon(mf.i101);					
					a++;
					if(open.equals("dPfladl")) {	
						mf.jlp4.setIcon(mf.cardMach(cards2[i]));	
					}
				}  else  if (!(id.equals(scs.get(i)))&&user2ID.equals(scs.get(i))) { //북쪽					
					Thread.sleep(300);
					mf.jlp2.setIcon(mf.i101);
					b++;
					if(open.equals("dPfladl")) {	
						mf.jlp2.setIcon(mf.cardMach(cards2[i]));						
					}
				} else if (!(id.equals(scs.get(i)))&&user3ID.equals(scs.get(i))) { //서쪽						
					Thread.sleep(300);
					mf.jlp6.setIcon(mf.i101);					
					c++;
					if(open.equals("dPfladl")) {
						mf.jlp6.setIcon(mf.cardMach(cards2[i]));
					}
				} else {	//서쪽
					if(!(id.equals(scs.get(i)))&&user4ID.equals(scs.get(i))&&a==0) {//동쪽이 비었을 때 user4						
						Thread.sleep(300);
						mf.jlp4.setIcon(mf.i101);						
						if(open.equals("dPfladl")) {	
							mf.jlp4.setIcon(mf.cardMach(cards2[i]));
						}
					}else if(!(id.equals(scs.get(i)))&&user4ID.equals(scs.get(i))&&b==0){//북쪽이 비었을 때 user4						
						Thread.sleep(300);
						mf.jlp2.setIcon(mf.i101);						
						if(open.equals("dPfladl")) {	
							mf.jlp2.setIcon(mf.cardMach(cards2[i]));							
						}
					}else {						
						Thread.sleep(300);
						mf.jlp6.setIcon(mf.i101);					
						if(open.equals("dPfladl")) {								
							mf.jlp6.setIcon(mf.cardMach(cards2[i]));
						}
					}					
				}
			}			
		} catch (Exception e) {
			pln(e.getMessage());
		}
	}

	public void run() { // 읽기
		try {
			// 아이디확정			
			mf.jta2.append("< 당신의 아이디는 [ " + id + " ]입니다. > \n");

			while (true) {
				String msg = dis.readUTF();
				// 순서 판별
				if (msg.contains("#mem_") || msg.contains("#log_") || msg.contains("#card1_") || msg.contains("#card2_")
						|| msg.contains("#power_") || msg.contains("#judge_") || msg.contains("#end_")
						|| msg.contains("#start_") || msg.contains("#batting_")) {
					protocol(msg);
				} else {
					mf.jta2.append(msg + "\n");
					mf.jta2.setCaretPosition(mf.jta2.getDocument().getLength());
				}
			}
		} catch (IOException ie) {
			mf.jta1.append("서버가 다운됐습니다. \n 3초 후에 종료됩니다.\n");
			try {
				Thread.sleep(3000);
				System.exit(0);
			} catch (InterruptedException ite) {
			}
		} finally {
			closeA();
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
			if (s != null)
				s.close();
		} catch (IOException ie) {
		}
	}

	void pln(String str) {
		System.out.println(str);
	}

	void p(String str) {
		System.out.print(str);
	}

	// 내부 UI클레스
	class MainFrame extends JFrame implements ActionListener {

		Container cm1;
		JPanel c1p1, c1p2, c1p3, c1p4, c1p5, c1p6, c1p7, c1p8, c1p9;
		JPanel c2p2, c2p4, c2p6, c2p7, c2p8, c3p7;
		JButton mb1;
		JButton bp1, bp2;
		JTextArea jta1, jta2;
		JTextField jtf1;
		JLabel jlj1, jlj2, jlj3, jlj4, jlj5;
		JLabel jlb1, jlb2, jlb3, jlb4;
		JLabel jwl1, jwl4;
		JLabel jlk1, jlk2, jlk3, jlk4, jlk5, jlk6, jlk7, jlk8, jlk9, jlk10, jlk11, jlk12;
		JLabel jli1, jli2, jli3, jli4, jli5, jli6, jli7; // 로고 , 캐릭터이미지
		JLabel jbc1, jbc2, jbc3, jbc4, jbc5, jbc6, jbc7, jbc8, jbc9; // 중앙라벨
		JLabel jlp1, jlp2, jlp3, jlp4, jlp5, jlp6, jlp7;
		JButton jbb1, jbb2, jbb3, jbb4; // 배팅이미지 버튼
		JButton jbr1, jbr2, jbr3, jbr4;
		JButton jbp8; // 패 버튼
		JButton jbj1, jbj2, jbj3, jbj4; // 유저 족보 나타내기
		JButton jbub1, jbub2, jbub3, jbub4; // 유저배팅
		String msg;
		Font font1 = new Font("a전자시계", Font.PLAIN, 40);

		// 이미지 공유폴더에 업로드 해줄 것
		ImageIcon i1 = new ImageIcon("./image/완성/화투이미지/1+.png");
		ImageIcon i2 = new ImageIcon("./image/완성/화투이미지/2+.png");
		ImageIcon i3 = new ImageIcon("./image/완성/화투이미지/3+.png");
		ImageIcon i4 = new ImageIcon("./image/완성/화투이미지/4+.png");
		ImageIcon i5 = new ImageIcon("./image/완성/화투이미지/5+.png");
		ImageIcon i6 = new ImageIcon("./image/완성/화투이미지/6+.png");
		ImageIcon i7 = new ImageIcon("./image/완성/화투이미지/7+.png");
		ImageIcon i8 = new ImageIcon("./image/완성/화투이미지/8+.png");
		ImageIcon i9 = new ImageIcon("./image/완성/화투이미지/9+.png");
		ImageIcon i10 = new ImageIcon("./image/완성/화투이미지/10+.png");
		ImageIcon i11 = new ImageIcon("./image/완성/화투이미지/1-.png");
		ImageIcon i12 = new ImageIcon("./image/완성/화투이미지/2-.png");
		ImageIcon i13 = new ImageIcon("./image/완성/화투이미지/3-.png");
		ImageIcon i14 = new ImageIcon("./image/완성/화투이미지/4-.png");
		ImageIcon i15 = new ImageIcon("./image/완성/화투이미지/5-.png");
		ImageIcon i16 = new ImageIcon("./image/완성/화투이미지/6-.png");
		ImageIcon i17 = new ImageIcon("./image/완성/화투이미지/7-.png");
		ImageIcon i18 = new ImageIcon("./image/완성/화투이미지/8-.png");
		ImageIcon i19 = new ImageIcon("./image/완성/화투이미지/9-.png");
		ImageIcon i20 = new ImageIcon("./image/완성/화투이미지/10-.png");

		ImageIcon i30 = new ImageIcon("./image/완성/캐릭터이미지/char1.png");
		ImageIcon i31 = new ImageIcon("./image/완성/캐릭터이미지/char2.png");
		ImageIcon i32 = new ImageIcon("./image/완성/캐릭터이미지/char3.png");
		ImageIcon i33 = new ImageIcon("./image/완성/캐릭터이미지/char4.png");

		ImageIcon i40 = new ImageIcon("./image/완성/call.png");
		ImageIcon i41 = new ImageIcon("./image/완성/harf.png");
		ImageIcon i42 = new ImageIcon("./image/완성/allin.png");
		ImageIcon i43 = new ImageIcon("./image/완성/die.png");
		ImageIcon i44 = new ImageIcon("./image/완성/mainlogo.png");
		ImageIcon i45 = new ImageIcon("./image/완성/원하지연.png");
		ImageIcon i46 = new ImageIcon("./image/완성/콜.png");
		ImageIcon i47 = new ImageIcon("./image/완성/하프.png");
		ImageIcon i48 = new ImageIcon("./image/완성/다이다이.png");
		ImageIcon i49 = new ImageIcon("./image/완성/올인.png");

		ImageIcon i50 = new ImageIcon("./image/완성/족보이미지/1삼팔광땡.png");
		Image i51 = new ImageIcon("./image/완성/1시.png").getImage();
		Image i52 = new ImageIcon("./image/완성/5시.png").getImage();
		ImageIcon i53 = new ImageIcon("./image/완성/뼈.png");
		ImageIcon i54 = new ImageIcon("./image/완성/9시레디.png");
		ImageIcon i55 = new ImageIcon("./image/완성/12시레디.png");
		ImageIcon i56 = new ImageIcon("./image/완성/3시레디.png");
		ImageIcon i57 = new ImageIcon("./image/완성/6시레디.png");
		ImageIcon i58 = new ImageIcon("./image/완성/승리2.png");
		ImageIcon i59 = new ImageIcon("./image/완성/패배2.png");

		ImageIcon i70 = new ImageIcon("./image/완성/기본족보판.png");
		ImageIcon i71 = new ImageIcon("./image/완성/삼팔광땡.png");
		ImageIcon i72 = new ImageIcon("./image/완성/광땡.png");
		ImageIcon i73 = new ImageIcon("./image/완성/땡.png");
		ImageIcon i74 = new ImageIcon("./image/완성/알리.png");
		ImageIcon i75 = new ImageIcon("./image/완성/독사.png");
		ImageIcon i76 = new ImageIcon("./image/완성/구삥.png");
		ImageIcon i77 = new ImageIcon("./image/완성/장삥.png");
		ImageIcon i78 = new ImageIcon("./image/완성/장사.png");
		ImageIcon i79 = new ImageIcon("./image/완성/세륙.png");
		ImageIcon i80 = new ImageIcon("./image/완성/갑오.png");
		ImageIcon i81 = new ImageIcon("./image/완성/끗망통.png");
		ImageIcon i82 = new ImageIcon("./image/완성/구사.png");
		ImageIcon i100 = new ImageIcon("./image/완성/화투이미지/background.png");
		//패 뒷면 추가		
		ImageIcon i101 = new ImageIcon("./image/완성/화투이미지/back.png");
		ImageIcon i99 = new ImageIcon("./image/완성/신족보/족보기본.png");
		ImageIcon i102 = new ImageIcon("./image/완성/신족보/0.png");
		ImageIcon i103 = new ImageIcon("./image/완성/신족보/1.png");
		ImageIcon i104 = new ImageIcon("./image/완성/신족보/2.png");
		ImageIcon i105 = new ImageIcon("./image/완성/신족보/3.png");
		ImageIcon i106 = new ImageIcon("./image/완성/신족보/4.png");
		ImageIcon i107 = new ImageIcon("./image/완성/신족보/5.png");
		ImageIcon i108 = new ImageIcon("./image/완성/신족보/6.png");
		ImageIcon i109 = new ImageIcon("./image/완성/신족보/7.png");
		ImageIcon i110 = new ImageIcon("./image/완성/신족보/8.png");
		ImageIcon i111 = new ImageIcon("./image/완성/신족보/갑오.png");
		ImageIcon i112 = new ImageIcon("./image/완성/신족보/세륙.png");
		ImageIcon i113 = new ImageIcon("./image/완성/신족보/장사.png");
		ImageIcon i114 = new ImageIcon("./image/완성/신족보/장삥.png");
		ImageIcon i115 = new ImageIcon("./image/완성/신족보/구삥.png");
		ImageIcon i116 = new ImageIcon("./image/완성/신족보/독사.png");
		ImageIcon i117 = new ImageIcon("./image/완성/신족보/알리.png");
		ImageIcon i118 = new ImageIcon("./image/완성/신족보/사구파토.png");
		ImageIcon i119 = new ImageIcon("./image/완성/신족보/삥땡.png");
		ImageIcon i120 = new ImageIcon("./image/완성/신족보/이땡.png");
		ImageIcon i121 = new ImageIcon("./image/완성/신족보/삼땡.png");
		ImageIcon i122 = new ImageIcon("./image/완성/신족보/사땡.png");
		ImageIcon i123 = new ImageIcon("./image/완성/신족보/오땡.png");
		ImageIcon i124 = new ImageIcon("./image/완성/신족보/육땡.png");
		ImageIcon i125 = new ImageIcon("./image/완성/신족보/칠땡.png");
		ImageIcon i126 = new ImageIcon("./image/완성/신족보/팔땡.png");
		ImageIcon i127 = new ImageIcon("./image/완성/신족보/구땡.png");
		ImageIcon i128 = new ImageIcon("./image/완성/신족보/땡잡이.png");
		ImageIcon i129 = new ImageIcon("./image/완성/신족보/멍텅구리사구.png");
		ImageIcon i130 = new ImageIcon("./image/완성/신족보/장땡.png");
		ImageIcon i131 = new ImageIcon("./image/완성/신족보/일삼광땡.png");
		ImageIcon i132 = new ImageIcon("./image/완성/신족보/일팔광땡.png");
		ImageIcon i133 = new ImageIcon("./image/완성/신족보/암행어사.png");
		ImageIcon i134 = new ImageIcon("./image/완성/신족보/삼팔광땡.png");
		
		ImageIcon cardMach(int cards) {
			switch (cards) {
			case 1:
				return i1;
			case 2:
				return i2;
			case 3:
				return i3;
			case 4:
				return i4;
			case 5:
				return i5;
			case 6:
				return i6;
			case 7:
				return i7;
			case 8:
				return i8;
			case 9:
				return i9;
			case 10:
				return i10;
			case 11:
				return i11;
			case 12:
				return i12;
			case 13:
				return i13;
			case 14:
				return i14;
			case 15:
				return i15;
			case 16:
				return i16;
			case 17:
				return i17;
			case 18:
				return i18;
			case 19:
				return i19;
			case 20:
				return i20;
			}
			return i100;
		}

		void cliIN() {
			Color k = new Color(80, 120, 32);
			Color b = new Color(44, 66, 18);
			cm1 = getContentPane();
			setLayout(new GridLayout(3, 3));

			cm1.add(c1p1 = new JPanel());
			cm1.add(c1p2 = new JPanel());
			cm1.add(c1p3 = new JPanel());
			cm1.add(c1p4 = new JPanel());
			cm1.add(c1p5 = new JPanel());
			cm1.add(c1p6 = new JPanel());
			cm1.add(c1p7 = new JPanel());
			cm1.add(c1p8 = new JPanel());
			// cm1.add(c1p9 = new JPanel());

			c1p9 = new JPanel() {
				{
					setOpaque(false);
				}

				public void paintComponent(Graphics g) {
					g.drawImage(i52, 0, 0, this);
					super.paintComponent(g);
				}
			};
			cm1.add(c1p9);

			c1p1.setLayout(new GridLayout(2, 1));
			c1p2.setLayout(new GridLayout(1, 3));
			c1p3.setLayout(new FlowLayout());
			c1p4.setLayout(new GridLayout(1, 3));
			c1p5.setLayout(new GridLayout(3, 3));
			c1p6.setLayout(new GridLayout(1, 3));
			c1p7.setLayout(new GridLayout(1, 2));
			c1p8.setLayout(new GridLayout(1, 3));
			c1p9.setLayout(new FlowLayout());

			// 배경색 지정
			cm1.setBackground(k);
			c1p1.setBackground(k);
			c1p2.setBackground(k);
			c1p3.setBackground(k);
			c1p4.setBackground(k);
			c1p5.setBackground(k);
			c1p6.setBackground(k);
			c1p7.setBackground(k);
			c1p8.setBackground(k);
			c1p9.setBackground(k);

			// 로고,시간 패널
			Time t = new Time();
			c1p1.add(jli5 = new JLabel(i44));
			c1p1.add(jli6 = new JLabel());
			c1p1.add(jli7 = new JLabel());
			jli7.setFont(font1);
			// jli7.setText(t.start() + "초남았다.");
			t.start();
			// 유저2 패널
			c1p2.add(c2p2 = new JPanel());
			c2p2.setLayout(new GridLayout(3, 1));
			c2p2.setBackground(k);
			c2p2.add(jli2 = new JLabel(i30));
			c2p2.add(jlj2 = new JLabel(i99));
			c2p2.add(jlb2 = new JLabel(i46));
			c1p2.add(jlp1 = new JLabel(i100));
			c1p2.add(jlp2 = new JLabel(i100));

			// 시스템로그 패널
			jta1 = new JTextArea(11, 31) {
				{
					setOpaque(false);
				}

				public void paintComponent(Graphics g) {
					g.drawImage(i51, 0, 0, this);
					super.paintComponent(g);
				}
			};
			// jta1.setFont(font1);
			JScrollPane jsp1 = new JScrollPane(jta1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			// jta1.setBackground(k);
			jta1.setEnabled(false);
			c1p3.add(jsp1);

			// 유저1 패널
			c1p4.add(c2p4 = new JPanel());
			c2p4.setLayout(new GridLayout(3, 1));
			c2p4.setBackground(k);
			c2p4.add(jli1 = new JLabel(i31)); // 0
			c2p4.add(jlj1 = new JLabel(i99));
			c2p4.add(jlb1 = new JLabel(i47));
			c1p4.add(jlp3 = new JLabel(i100));
			c1p4.add(jlp4 = new JLabel(i100));

			// 중앙 패널
			c1p5.add(jbc1 = new JLabel(i59));
			c1p5.add(jbr1 = new JButton(i55));
			c1p5.add(jbc3 = new JLabel(i59));
			c1p5.add(jbr2 = new JButton(i54));
			c1p5.add(jbc5 = new JLabel(i53));
			c1p5.add(jbr3 = new JButton(i56));
			c1p5.add(jbc7 = new JLabel(i59));
			c1p5.add(jbr4 = new JButton(i57));
			c1p5.add(jbc9 = new JLabel(i58));

			// 유저3 패널
			c1p6.add(jlp5 = new JLabel(i100));
			c1p6.add(jlp6 = new JLabel(i100));
			c1p6.add(c2p6 = new JPanel());
			c2p6.setLayout(new GridLayout(3, 1));
			c2p6.setBackground(k);
			c2p6.add(jli3 = new JLabel(i32)); // 2;
			c2p6.add(jlj3 = new JLabel(i99));
			c2p6.add(jlb3 = new JLabel(i48));

			// 자신의 족보,배팅 패널
			c1p7.add(jlk1 = new JLabel(i70));
			c1p7.add(c2p7 = new JPanel());
			c2p7.setLayout(new GridLayout(4, 1));
			c2p7.setBackground(k);
			c2p7.add(jbb1 = new JButton(i40));
			c2p7.add(jbb2 = new JButton(i41));
			c2p7.add(jbb3 = new JButton(i42));
			c2p7.add(jbb4 = new JButton(i43));
			jbb4.addActionListener(this);

			// 자신의 패널
			c1p8.add(jlp7 = new JLabel());
			jlp7.setBackground(k);
			c1p8.add(jbp8 = new JButton());
			jbp8.setBackground(k);
			c1p8.add(c2p8 = new JPanel());
			c2p8.setLayout(new FlowLayout());
			c2p8.setBackground(k);
			c2p8.add(jli4 = new JLabel(i33)); // 2;
			c2p8.add(jlj4 = new JLabel());
			c2p8.add(jlj5 = new JLabel("자금 : " + ip));
			c2p8.add(jlb4 = new JLabel(i49));

			// 채팅 패널
			// jta2 = new JTextArea(10, 31) {
//				{
//					setOpaque(false);
//				}
//
//				public void paintComponent(Graphics g) {
//					g.drawImage(i52, 0, 0, this);
//					super.paintComponent(g);
//				}
//			};
			// jta2.setBackground(b);
			jta2 = new JTextArea(10, 31);
			JScrollPane jsp2 = new JScrollPane(jta2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			c1p9.add(jsp2);
			// jta2.setOpaque(false);
			c1p9.add(jwl4 = new JLabel(" 채팅입력 : "));
			c1p9.add(jtf1 = new JTextField(25));
			// jta2.setBackground(k);
			jta2.setEnabled(false);
			jtf1.addActionListener(this);

			CliUI();
		}

		void CliUI() {
			setTitle("STAND UP! Ver 1.0");
			setSize(1100, 700);
			setVisible(true);
			setLocation(50, 30);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		}

		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource(); // 이벤트 감지해줌
			if (obj == jtf1) {
				msg = jtf1.getText();
				jtf1.setText("");
				try {
					dos.writeUTF(msg);
					dos.flush();
				} catch (IOException ie) {
				}
			} else if (obj == jbb1) {
				// 콜
			} else if (obj == jbb2) {
				// 하프
			} else if (obj == jbb3) {
				// 올인
			} else if (obj == jbb4) {
				// st.die();
			} else if (obj == jbb4) {
			} else if (obj == jbb4) {
			} else if (obj == jbb4) {
			} 
			//패 까는 모션 추가
			else if (obj == jbp8) {
				
			}
		}
	}

	class Time extends Thread {

		public void run() {
			int time = 10;
			while (true) {
				try {
					MainFrame mf = new MainFrame();
					Thread.sleep(1000);
					// System.out.println(time+"초 남았습니다");
				} catch (InterruptedException e) {
				}
				mf.jli7.setText("   " + time + "sec.");
				time--;
				// if (time == 0) {
				// time1.setText("Game Over..."); //시간초과로 다이
				// off_button(); //다이 만들기
				// break;
			}
			// time1.setText("시간 => 0:"+time);
		}
	}
}
