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
	Vector<String> scs = new Vector<String>();

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
			mf.jbub4.setText(id);
		} catch (IOException ie) {
		}
	}

	// 프로토콜에 따라 메소드 실행
	void protocol(String msg) {
		String items[] = msg.split("_");
		if (cnt1 > 3 || cnt2 > 3) {
			cnt1 = 0;
			cnt2 = 0;
		}

		// 첫번째 패 선택
		if (items[0].equals("#card1")) {
			cards1[cnt1] = Integer.parseInt(items[1]);
			pln("" + cards1[cnt1]);
			// mf.jbp7.setIcon(mf.cardMach(cards1[i]));
			cnt1++;
			// 두번째 패 선택
		} else if (items[0].equals("#card2")) {
			cards2[cnt2] = Integer.parseInt(items[1]);
			pln("" + cards2[cnt2]);
			// mf.jbp8.setIcon(mf.cardMach(cards2[i]));
			cnt2++;
			// 족보확인
		} else if (items[0].equals("#power")) {
			pln("족 보");
			// 승패확인
		} else if (items[0].equals("#judge")) {
			pln("승 리");
			// 로그
		} else if (items[0].equals("#log")) {
			mf.jta1.append(items[1] + "\n");
			mf.jta1.setCaretPosition(mf.jta1.getDocument().getLength());
			// 게임참가 유저리스트 및 순서 저장
		} else if (items[0].equals("#mem")) {
			String memInfo[] = items[1].split("//");
			int memCount = Integer.parseInt(memInfo[0]);
			for (int j = 1; j < (memCount + 1); j++) {
				scs.add(memInfo[j]);
			}
			// 게임 시작시 패 뿌리기 시작
		} else if (items[0].equals("#start")) {
			pln("시작");
			idMach();
			// 게임참가 유저리스트 초기화
		} else if (items[0].equals("#end")) {
			scs.removeAllElements();
		} else {
			pln("존재하지 않는 프로토콜입니다" + msg);
		}
	}


	void idMach() {		
		try {
			//내 아이디와 같을경우 6시에 패
			for (int i = 0; i < scs.size(); i++) {
				if (id.equals(scs.get(i))) {		
					Thread.sleep(300);
					mf.jbp7.setIcon(mf.cardMach(cards1[i]));
					Thread.sleep(300);
					mf.jbp8.setIcon(mf.cardMach(cards2[i]));
					Thread.sleep(300);
				} else { //그렇지 않은경우 12시에 패
					pln(scs.get(i));
					mf.jbub2.setText(scs.get(i));
					Thread.sleep(300);
					mf.jlp1.setIcon(mf.cardMach(cards1[i]));
					Thread.sleep(300);
					mf.jlp2.setIcon(mf.cardMach(cards2[i]));
					Thread.sleep(300);
				}
			}
		} catch (Exception e) {
			pln(e.getMessage());
		}
	}

	public void run() { // 읽기
		try {
			// 아이디확정
			id = dis.readUTF();
			mf.jta2.append("< 당신의 아이디는 [ " + id + " ]입니다. > \n");

			while (true) {
				String msg = dis.readUTF();
				// 순서 판별
				if (msg.contains("#mem_") || msg.contains("#log_") || msg.contains("#card1_") || msg.contains("#card2_")
						|| msg.contains("#power_") || msg.contains("#judge_") || msg.contains("#end_")
						|| msg.contains("#start_")) {
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
		JLabel jlj1, jlj2, jlj3, jlj4;
		JLabel jlb1, jlb2, jlb3, jlb4;
		JLabel jwl1, jwl4;
		JLabel jlk1, jlk2, jlk3, jlk4, jlk5, jlk6, jlk7, jlk8, jlk9, jlk10, jlk11, jlk12;
		JLabel jli1, jli2, jli3, jli4, jli5, jli6, jli7; // 로고 , 캐릭터이미지
		JLabel jbc1, jbc2, jbc3, jbc4, jbc5, jbc6, jbc7, jbc8, jbc9; // 중앙라벨
		JLabel jlp1, jlp2, jlp3, jlp4, jlp5, jlp6;
		JButton jbb1, jbb2, jbb3, jbb4; // 배팅이미지 버튼
		JButton jbp7, jbp8; // 패 버튼
		JButton jbj1, jbj2, jbj3, jbj4; // 유저 족보 나타내기
		JButton jbub1, jbub2, jbub3, jbub4; // 유저배팅
		String msg;

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
		ImageIcon i44 = new ImageIcon("./image/완성/logo.png");
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
		
		ImageIcon i100 = new ImageIcon("./image/완성/화투이미지/background.png");

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
			cm1.add(c1p9 = new JPanel());

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
			//jli7.setText(t.start() + "초남았다.");
			t.start();
			// 유저2 패널
			c1p2.add(c2p2 = new JPanel());
			c2p2.setLayout(new GridLayout(3, 1));
			c2p2.setBackground(k);
			c2p2.add(jli2 = new JLabel(i30));
			c2p2.add(jlj2 = new JLabel(i50));
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
			JScrollPane jsp1 = new JScrollPane(jta1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jta1.setBackground(k);
			jta1.setEnabled(false);
			c1p3.add(jsp1);

			// 유저1 패널
			c1p4.add(c2p4 = new JPanel());
			c2p4.setLayout(new GridLayout(3, 1));
			c2p4.setBackground(k);
			c2p4.add(jli1 = new JLabel(i31)); // 0
			c2p4.add(jlj1 = new JLabel(i50));
			c2p4.add(jlb1 = new JLabel(i47));
			c1p4.add(jlp3 = new JLabel(i100));
			c1p4.add(jlp4 = new JLabel(i100));

			// 중앙 패널
			c1p5.add(jbc1 = new JLabel());
			c1p5.add(jbc2 = new JLabel(i55));
			c1p5.add(jbc3 = new JLabel());
			c1p5.add(jbc4 = new JLabel(i54));
			c1p5.add(jbc5 = new JLabel(i53));
			c1p5.add(jbc6 = new JLabel(i56));
			c1p5.add(jbc7 = new JLabel());
			c1p5.add(jbc8 = new JLabel(i57));
			c1p5.add(jbc9 = new JLabel());

			// 유저3 패널
			c1p6.add(c2p6 = new JPanel());
			c2p6.setLayout(new GridLayout(3, 1));
			c2p6.setBackground(k);
			c2p6.add(jli3 = new JLabel(i32)); // 2;
			c2p6.add(jlj3 = new JLabel(i50));
			c2p6.add(jlb3 = new JLabel(i48));
			c1p6.add(jlp5 = new JLabel(i100));
			c1p6.add(jlp6 = new JLabel(i100));

			// 자신의 족보,배팅 패널
			c1p7.add(c3p7 = new JPanel());
			c3p7.setLayout(new GridLayout(6, 2));
			c3p7.add(jlk1 = new JLabel("삼팔광땡"));
			c3p7.add(jlk2 = new JLabel("광		 땡"));
			c3p7.add(jlk3 = new JLabel("		땡		"));
			c3p7.add(jlk4 = new JLabel("알		 리"));
			c3p7.add(jlk5 = new JLabel("독		 사"));
			c3p7.add(jlk6 = new JLabel("구		 삥"));
			c3p7.add(jlk7 = new JLabel("장		 삥"));
			c3p7.add(jlk8 = new JLabel("장		 사"));
			c3p7.add(jlk9 = new JLabel("세		 륙"));
			c3p7.add(jlk10 = new JLabel("갑		 오"));
			c3p7.add(jlk11 = new JLabel("끗/ /망통"));
			c3p7.add(jlk12 = new JLabel("구		 사"));
			c1p7.add(c2p7 = new JPanel());
			c2p7.setLayout(new GridLayout(4, 1));
			c2p7.setBackground(k);
			c2p7.add(jbb1 = new JButton(i40));
			c2p7.add(jbb2 = new JButton(i41));
			c2p7.add(jbb3 = new JButton(i42));
			c2p7.add(jbb4 = new JButton(i43));

			// 자신의 패널
			c1p8.add(jbp7 = new JButton());
			c1p8.add(jbp8 = new JButton());
			c1p8.add(c2p8 = new JPanel());
			c2p8.setLayout(new GridLayout(3, 1));
			c2p8.setBackground(k);
			c2p8.add(jli4 = new JLabel(i33)); // 2;
			c2p8.add(jlj4 = new JLabel(i50));
			c2p8.add(jlb4 = new JLabel(i49));

			// 채팅 패널

			jta2 = new JTextArea(10, 31) {
				{
					setOpaque(false);
				}

				public void paintComponent(Graphics g) {
					g.drawImage(i52, 0, 0, this);
					super.paintComponent(g);
				}
			};
			JScrollPane jsp2 = new JScrollPane(jta2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			c1p9.add(jsp2);
			c1p9.add(jwl4 = new JLabel(" 채팅입력 : "));
			c1p9.add(jtf1 = new JTextField(25));
			jta2.setBackground(k);
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
					//System.out.println(time+"초 남았습니다");
					} catch (InterruptedException e) {
				}
				mf.jli7.setText(time + "초 남았다 긴장해라");
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
