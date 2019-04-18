

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

class ServerLoginUI extends JFrame implements ActionListener {
	JFrame f;
	Container cp;
	JPanel p1, p2, p3, p4, p5, p6;
	JButton b1;
	ImageIcon i1, i2;

	void init() {
		try {
			i1 = new ImageIcon(ImageIO.read(new File("C:/Users/BITSC-6-20/Desktop/TPJT1/로고이미지/로고.png")));
			i2 = new ImageIcon(ImageIO.read(new File("C:/Users/BITSC-6-20/Desktop/TPJT1/로고이미지/원하지연.png")));
			// i1 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/로고이미지/로고.png");
			// i2 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/로고이미지/원하지연.png");
		} catch (IOException ie) {

		}
		Color b = new Color(255, 255, 224);
		setLayout(new GridLayout(6, 1));

		b1 = new JButton("START");
		b1.addActionListener(this);

		cp = getContentPane();

		cp.add(p1 = new JPanel());
		cp.add(p2 = new JPanel());
		cp.add(p3 = new JPanel());
		cp.add(p4 = new JPanel());
		cp.add(p5 = new JPanel());

		p1.setLayout(new FlowLayout());
		p2.setLayout(new FlowLayout());
		p3.setLayout(new FlowLayout());
		p5.setLayout(new GridLayout(1, 3));

		cp.setBackground(b);
		p1.setBackground(b);
		p2.setBackground(b);
		p3.setBackground(b);
		p4.setBackground(b);
		p5.setBackground(b);

		p1.add(new JLabel("포트 와 서버IP를 입력해주세요"));
		p2.add(new JLabel(" [ 포       트 ]"));
		p2.add(new JTextField(12));
		p3.add(new JLabel(" [ 서 버 I P ]"));
		p3.add(new JTextField(12));
		p5.add(new JLabel(i1));
		p5.add(b1);
		p5.add(new JLabel(i2));

		setUI();
	}

	void setUI() {
		setTitle("STAND UP! Server Login");
		setSize(250, 200);
		setVisible(true);
		setLocation(300, 300);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
//		System.out.println("이벤트감지");
		ServerFrame sf = new ServerFrame();
		sf.sevIN();
//		Object obj = e.getSource();
//		if (obj == b1) {
//			pln("로그인성공");
//			JOptionPane.showMessageDialog(null, "메세지 내용", "제목", JOptionPane.QUESTION_MESSAGE, null);

	}

	void pln(String str) {
		System.out.println(str);
	}

	public static void main(String[] args) {
		ServerLoginUI slui = new ServerLoginUI();
		slui.init();
	}
}
