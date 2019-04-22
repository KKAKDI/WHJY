import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

class StuClient extends Thread {
	// ��������
	String ip;
	Socket s;
	int port = 3524;

	// ���� ���� �������
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
	int i;

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
			protocol("#log_������ ����Ǿ����ϴ�."); // ����button

			name();
		
			t1.start();
			idMach();
		} catch (UnknownHostException uhe) {
			pln("�ش� ������ �������� �ʽ��ϴ�.");
			connect();
		} catch (IOException ie) {
			connect();
		} catch (NumberFormatException nfe) {
			pln("�������� �ʴ� ��Ʈ��ȣ�Դϴ�.");
			connect(); // ���ȣ��
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

	// �������ݿ� ���� �޼ҵ� ����
	void protocol(String msg) {
		String items[] = msg.split("_");
		if (i > 3)
			i = 0;
		// ù��° �� ����
		if (items[0].equals("#card1")) {
			cards1[i] = Integer.parseInt(items[1]);
			pln("" + cards1[i]);
			// mf.jbp7.setIcon(mf.cardMach(cards1[i]));
			i++;
			// �ι�° �� ����
		} else if (items[0].equals("#card2")) {
			cards2[i] = Integer.parseInt(items[1]);
			pln("" + cards2[i]);
			// mf.jbp8.setIcon(mf.cardMach(cards2[i]));
			i++;
			// ����Ȯ��
		} else if (items[0].equals("#power")) {
			pln("�� ��");
			// ����Ȯ��
		} else if (items[0].equals("#judge")) {
			pln("�� ��");
			// �α�
		} else if (items[0].equals("#log")) {
			mf.jta1.append(items[1] + "\n");
			mf.jta1.setCaretPosition(mf.jta1.getDocument().getLength());
			// �������� ��������Ʈ �� ���� ����
		} else if (items[0].equals("#mem")) {
			String memInfo[] = items[1].split("//");
			int memCount = Integer.parseInt(memInfo[0]);
			for (int j = 1; j < (memCount + 1); j++) {
				scs.add(memInfo[j]);
			}			
			// �������� ��������Ʈ �ʱ�ȭ
		} else if (items[0].equals("#end")) {
			scs.removeAllElements();
		} else {
			pln("�������� �ʴ� ���������Դϴ�" + msg);
		}
	}
	//���ӽ��� ��ȣ �ʿ�
	void idMach() {
		try {
			for (int k = 0; k < scs.size();k++) {
				if (id == scs.get(k)) {
					// mf.jbub4.setText(scs.get(j));
					Thread.sleep(500);
					mf.jbp7.setIcon(mf.cardMach(cards1[k]));
					Thread.sleep(500);
					mf.jbp8.setIcon(mf.cardMach(cards2[k]));

				} else {
					Thread.sleep(500);
					mf.jbp5.setIcon(mf.cardMach(cards1[k]));
					Thread.sleep(500);
					mf.jbp6.setIcon(mf.cardMach(cards2[k]));
				}
			}
		} catch (Exception e) {
			pln(e.getMessage());
		}
	}

	public void run() { // �б�
		try {
			// ���̵�Ȯ��
			id = dis.readUTF();
			mf.jta2.append("< ����� ���̵�� [ " + id + " ]�Դϴ�. > \n");

			while (true) {
				String msg = dis.readUTF();
				// ���� �Ǻ�
				if (msg.contains("#mem_") || msg.contains("#log_") || msg.contains("#card1_") || msg.contains("#card2_")
						|| msg.contains("#power_") || msg.contains("#judge_") || msg.contains("#end_")) {
					protocol(msg);
				} else {
					mf.jta2.append(msg + "\n");
					mf.jta2.setCaretPosition(mf.jta2.getDocument().getLength());
				}
			}
		} catch (IOException ie) {
			mf.jta1.append("������ �ٿ�ƽ��ϴ�. \n 3�� �Ŀ� ����˴ϴ�.\n");
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

	// ���� UIŬ����
	class MainFrame extends JFrame implements ActionListener {

		Container cm1;
		JPanel c1p1, c1p2, c1p3, c1p4, c1p5, c1p6, c1p7, c1p8, c1p9;
		JPanel c2p2, c2p4, c2p6, c2p7, c2p8;
		JButton mb1;
		JButton bp1, bp2;
		JTextArea jta1, jta2;
		JTextField jtf1;
		JLabel jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8;
		JLabel jlj1, jlj2, jlj3, jlj4;
		JLabel jwl1, jwl2, jwl3, jwl4;
		JLabel jli1, jli2, jli3, jli4, jli5; // �ΰ� , ĳ�����̹���
		JLabel jbc1, jbc2, jbc3, jbc4, jbc5, jbc6, jbc7, jbc8, jbc9; // �߾Ӷ�
		JLabel jlp1, jlp2, jlp3, jlp4, jlp5, jlp6;
		JButton jbb1, jbb2, jbb3, jbb4; // �����̹��� ��ư
		JButton jbp1, jbp2, jbp3, jbp4, jbp5, jbp6, jbp7, jbp8; // �� ��ư
		JButton jbj1, jbj2, jbj3, jbj4; // ���� ���� ��Ÿ����
		JButton jbub1, jbub2, jbub3, jbub4; // ��������
		String msg;

		// �̹��� ���������� ���ε� ���� ��
		ImageIcon i1 = new ImageIcon("./image/�ϼ�/ȭ���̹���/1+.png");
		ImageIcon i2 = new ImageIcon("./image/�ϼ�/ȭ���̹���/2+.png");
		ImageIcon i3 = new ImageIcon("./image/�ϼ�/ȭ���̹���/3+.png");
		ImageIcon i4 = new ImageIcon("./image/�ϼ�/ȭ���̹���/4+.png");
		ImageIcon i5 = new ImageIcon("./image/�ϼ�/ȭ���̹���/5+.png");
		ImageIcon i6 = new ImageIcon("./image/�ϼ�/ȭ���̹���/6+.png");
		ImageIcon i7 = new ImageIcon("./image/�ϼ�/ȭ���̹���/7+.png");
		ImageIcon i8 = new ImageIcon("./image/�ϼ�/ȭ���̹���/8+.png");
		ImageIcon i9 = new ImageIcon("./image/�ϼ�/ȭ���̹���/9+.png");
		ImageIcon i10 = new ImageIcon("./image/�ϼ�/ȭ���̹���/10+.png");
		ImageIcon i11 = new ImageIcon("./image/�ϼ�/ȭ���̹���/1-.png");
		ImageIcon i12 = new ImageIcon("./image/�ϼ�/ȭ���̹���/2-.png");
		ImageIcon i13 = new ImageIcon("./image/�ϼ�/ȭ���̹���/3-.png");
		ImageIcon i14 = new ImageIcon("./image/�ϼ�/ȭ���̹���/4-.png");
		ImageIcon i15 = new ImageIcon("./image/�ϼ�/ȭ���̹���/5-.png");
		ImageIcon i16 = new ImageIcon("./image/�ϼ�/ȭ���̹���/6-.png");
		ImageIcon i17 = new ImageIcon("./image/�ϼ�/ȭ���̹���/7-.png");
		ImageIcon i18 = new ImageIcon("./image/�ϼ�/ȭ���̹���/8-.png");
		ImageIcon i19 = new ImageIcon("./image/�ϼ�/ȭ���̹���/9-.png");
		ImageIcon i20 = new ImageIcon("./image/�ϼ�/ȭ���̹���/10-.png");

		ImageIcon i30 = new ImageIcon("./image/�ϼ�/ĳ�����̹���/char1.png");
		ImageIcon i31 = new ImageIcon("./image/�ϼ�/ĳ�����̹���/char2.png");
		ImageIcon i32 = new ImageIcon("./image/�ϼ�/ĳ�����̹���/char3.png");
		ImageIcon i33 = new ImageIcon("./image/�ϼ�/ĳ�����̹���/char4.png");

		ImageIcon i40 = new ImageIcon("./image/�ϼ�/call.png");
		ImageIcon i41 = new ImageIcon("./image/�ϼ�/harf.png");
		ImageIcon i42 = new ImageIcon("./image/�ϼ�/allin.png");
		ImageIcon i43 = new ImageIcon("./image/�ϼ�/die.png");
		ImageIcon i44 = new ImageIcon("./image/�ϼ�/logo.png");
		ImageIcon i45 = new ImageIcon("./image/�ϼ�/��������.png");

		ImageIcon i100 = new ImageIcon("./image/�ϼ�/ȭ���̹���/background.png");

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

			// ���� ����
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

			// �ΰ�,�ð� �г�
			c1p1.add(jli5 = new JLabel(i44));
			c1p1.add(mb1 = new JButton());

			// ����2 �г�
			c1p2.add(c2p2 = new JPanel());
			c2p2.setLayout(new BoxLayout(c2p2, BoxLayout.Y_AXIS));
			c2p2.setBackground(k);
			c2p2.add(jli2 = new JLabel(i30));
			c2p2.add(jbub2 = new JButton(i41));
			c2p2.add(jbj2 = new JButton(i40));
			c1p2.add(jlp1 = new JLabel(i100));
			c1p2.add(jlp2 = new JLabel(i100));

			// �ý��۷α� �г�
			jta1 = new JTextArea(11, 31);
			JScrollPane jsp1 = new JScrollPane(jta1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jta1.setBackground(k);
			c1p3.add(jsp1);

			// ����1 �г�
			c1p4.add(c2p4 = new JPanel());
			c2p4.setLayout(new GridLayout(3, 1));
			c2p4.setBackground(k);
			c2p4.add(jli1 = new JLabel(i31)); // 0
			c2p4.add(jbub1 = new JButton());
			c2p4.add(jbj1 = new JButton());
			c1p4.add(jlp3 = new JLabel(i100));
			c1p4.add(jlp4 = new JLabel(i100));

			// �߾� �г�
			c1p5.add(jbc1 = new JLabel());
			c1p5.add(jbc2 = new JLabel());
			c1p5.add(jbc3 = new JLabel());
			c1p5.add(jbc4 = new JLabel());
			c1p5.add(jbc5 = new JLabel());
			c1p5.add(jbc6 = new JLabel());
			c1p5.add(jbc7 = new JLabel());
			c1p5.add(jbc8 = new JLabel());
			c1p5.add(jbc9 = new JLabel());

			// ����3 �г�
			c1p6.add(c2p6 = new JPanel());
			c2p6.setLayout(new GridLayout(3, 1));
			c2p6.setBackground(k);
			c2p6.add(jli3 = new JLabel(i32)); // 2;
			c2p6.add(jbub3 = new JButton());
			c2p6.add(jbj3 = new JButton());
			c1p6.add(jlp5 = new JLabel(i100));
			c1p6.add(jlp6 = new JLabel(i100));

			// �ڽ��� ����,���� �г�
			c1p7.add(mb1 = new JButton());
			c1p7.add(c2p7 = new JPanel());
			c2p7.setLayout(new GridLayout(4, 1));
			c2p7.setBackground(k);
			c2p7.add(jbb1 = new JButton(i40));
			c2p7.add(jbb2 = new JButton(i41));
			c2p7.add(jbb3 = new JButton(i42));
			c2p7.add(jbb4 = new JButton(i43));

			// �ڽ��� �г�
			c1p8.add(jbp7 = new JButton());
			c1p8.add(jbp8 = new JButton());
			c1p8.add(c2p8 = new JPanel());
			c2p8.setLayout(new GridLayout(3, 1));
			c2p8.setBackground(k);
			c2p8.add(jli4 = new JLabel(i33)); // 2;
			c2p8.add(jbub4 = new JButton());
			c2p8.add(jbj4 = new JButton());

			// ä�� �г�
			c1p9.add(jwl4 = new JLabel(" ä���Է� : "));
			c1p9.add(jtf1 = new JTextField(25));
			jta2 = new JTextArea(10, 31);
			JScrollPane jsp2 = new JScrollPane(jta2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			c1p9.add(jsp2);
			jta2.setBackground(k);
			jta2.setEnabled(false);
			jta1.setEnabled(false);
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
			Object obj = e.getSource(); // �̺�Ʈ ��������
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
