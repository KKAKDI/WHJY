import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

class StuClient extends Thread {
	String ip;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Socket s;
	InputStream is;
	DataInputStream dis;
	OutputStream os;
	DataOutputStream dos;
	String id;
	boolean rop = false;
	int port = 3524;
	ClientLoginUI clui;
	MainFrame mf;
	Thread t1;
	Vector<String> scs = new Vector<String>();

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
			mf.jta1.append("서버와 연결되었습니다.\n"); // 연결button
			mf.jta1.setCaretPosition(mf.jta1.getDocument().getLength());

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
			// mf.jta1.append(id + "님이 입장하셨습니다.\n");
		} catch (IOException ie) {
		}

	}

//	void choice() { // 패 선택
//	int i = 1;
//	int j = 2;
//	String str = (i != j) ? "패1" : "패2";
//}
	void procedure(String msg) {// 클라이언트 순서
		String items[] = msg.split("_");

		if (items[0].equals("#card1")) {
			pln("대충 card1 값이 잘 들어와있다는 뜻");
		} else if (items[0].equals("#card1")) {

		} else if (items[0].equals("#card2")) {

		} else if (items[0].equals("#power")) {

		} else if (items[0].equals("#judge")) {

		} else if (items[0].equals("#log")) {

		} else {
			pln("존재하지 않는 프로토콜입니다");
		}
//	int a = Integer.parseInt(items[1]);
//
//	for (int i = 2; i < (a + 2); i++) {
//		scs.add(items[i]);
//	}
	}

	public void run() { // 읽기
		try {
			// 아이디확정
			id = dis.readUTF();
			mf.jta2.append("< 당신의 아이디는 [ " + id + " ]입니다. > \n");

			while (true) {
				String msg = dis.readUTF();
				// 순서 판별
				if (msg.contains("@a_r_b_o_k")) {
					// procedure(msg);
				} else if (msg.contains("#log_")) {
					procedure(msg);
					pln("대충 로그 창에 띄워진다는 글");
				} else if (msg.contains("#card1_")) {
					procedure(msg);
					pln("대충 자리 카드1 이라는 글");
				} else if (msg.contains("#card2_")) {
					procedure(msg);
					pln("대충 자리 카드2 이라는 글");
				} else if (msg.contains("#power_")) {
					procedure(msg);
					pln("대충 족보순위 라는 글");
				} else if (msg.contains("#judge_")) {
					procedure(msg);
					pln("대충 누가 승리했다는 글");
				} else {
					mf.jta2.append(msg + "\n");
					mf.jta2.setCaretPosition(mf.jta2.getDocument().getLength());
				}
				// choice();
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
		JPanel c2p1, c2p2, c2p4, c2p6, c2p7, c2p8, c2p9;
		JButton mb1;
		JButton bp1, bp2;
		JTextArea jta1, jta2;
		JTextField jtf1;
		JLabel jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8;
		JLabel jlj1, jlj2, jlj3, jlj4;
		JLabel jwl1, jwl2, jwl3, jwl4;
		JLabel jli1, jli2, jli3, jli4, jli5; // 로고 , 캐릭터이미지
		JButton jbb1, jbb2, jbb3, jbb4; // 배팅이미지 버튼
		JButton jbp1, jbp2, jbp3, jbp4, jbp5, jbp6, jbp7, jbp8;		//패 버튼
		String msg;

		// 이미지 공유폴더에 업로드 해줄 것
		ImageIcon i1 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/1+.png");
		ImageIcon i2 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/2+.png");
		ImageIcon i3 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/3+.png");
		ImageIcon i4 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/4+.png");
		ImageIcon i5 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/5+.png");
		ImageIcon i6 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/6+.png");
		ImageIcon i7 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/7+.png");
		ImageIcon i8 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/8+.png");
		ImageIcon i9 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/9+.png");
		ImageIcon i10 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/10+.png");
		ImageIcon i11 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/1-.png");
		ImageIcon i12 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/2-.png");
		ImageIcon i13 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/3-.png");
		ImageIcon i14 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/4-.png");
		ImageIcon i15 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/5-.png");
		ImageIcon i16 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/6-.png");
		ImageIcon i17 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/7-.png");
		ImageIcon i18 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/8-.png");
		ImageIcon i19 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/9-.png");
		ImageIcon i20 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/화투이미지/10-.png");

		ImageIcon i30 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/캐릭터이미지/char1.png");
		ImageIcon i31 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/캐릭터이미지/char2.png");
		ImageIcon i32 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/캐릭터이미지/char3.png");
		ImageIcon i33 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/캐릭터이미지/char4.png");

		ImageIcon i40 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/call.png");
		ImageIcon i41 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/harf.png");
		ImageIcon i42 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/allin.png");
		ImageIcon i43 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/die.png");
		ImageIcon i44 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/logo.png");
		ImageIcon i45 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/image/완성/원하지연.png");

		ImageIcon i100 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/패 이미지/패뒷면.png");

		void cliIN() {
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
			c1p8.setLayout(new BorderLayout());
			c1p9.setLayout(new FlowLayout());

			c1p1.add(mb1 = new JButton());
			c1p1.add(jli1 = new JLabel(i45));
			c1p1.add(mb1 = new JButton());

			c1p2.add(c2p2 = new JPanel());
			c2p2.setLayout(new FlowLayout());
			c2p2.add(jli2 = new JLabel(i30));
			c2p2.add(jlj2 = new JLabel()); // jokbo(sts.gl.powers[1])
			c2p2.add(jwl2 = new JLabel()); // win(sts.gl.judge(), 1)
			c1p2.add(jbp3 = new JButton()); // inImg(sts.gl.card1[1])
			//jbp3.addActionListener(this);
			c1p2.add(jbp4 = new JButton()); // inImg(sts.gl.card2[1])
			//jbp4.addActionListener(this);

			jta1 = new JTextArea(11, 32);
			JScrollPane jsp1 = new JScrollPane(jta1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			c1p3.add(jsp1);

			c1p4.add(c2p4 = new JPanel());
			c2p4.setLayout(new FlowLayout());
			c2p4.add(jli3 = new JLabel(i31)); // 0
			c2p4.add(jwl1 = new JLabel());
			c1p4.add(jbp1 = new JButton());
			c1p4.add(jbp2 = new JButton());

			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());
			c1p5.add(mb1 = new JButton());

			c1p6.add(c2p6 = new JPanel());
			c2p6.setLayout(new FlowLayout());
			c2p6.add(jli4 = new JLabel(i32)); // 2
			c2p6.add(jlj3 = new JLabel());
			c2p6.add(jwl3 = new JLabel());
			c1p6.add(jbp5 = new JButton());
			c1p6.add(jbp6 = new JButton());

			c1p7.add(mb1 = new JButton());
			c1p7.add(c2p7 = new JPanel());
			c2p7.setLayout(new GridLayout(4, 1));
			c2p7.add(jbb1 = new JButton(i40));
			c2p7.add(jbb2 = new JButton(i41));
			c2p7.add(jbb3 = new JButton(i42));
			c2p7.add(jbb4 = new JButton(i43));

			c1p8.add(c2p8 = new JPanel(), BorderLayout.CENTER);
			c1p8.add(c2p9 = new JPanel(), BorderLayout.SOUTH);
			c2p8.setLayout(new GridLayout(1, 2));
			c2p9.setLayout(new GridLayout(1, 3));
			c2p8.add(jbp7 = new JButton());
			//jbp7.addActionListener(this);
			c2p8.add(jbp8 = new JButton());
			//jbp8.addActionListener(this);
			c2p9.add(mb1 = new JButton());
			c2p9.add(jwl4 = new JLabel()); // 3
			c2p9.add(jlj4 = new JLabel());

			// c1p9.add(jta2 = new JTextArea(10, 32));

			c1p9.add(jwl4 = new JLabel(" 채팅입력 : "));
			c1p9.add(jtf1 = new JTextField(25));
			jta2 = new JTextArea(10, 32);
			JScrollPane jsp2 = new JScrollPane(jta2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			c1p9.add(jsp2);
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
}
