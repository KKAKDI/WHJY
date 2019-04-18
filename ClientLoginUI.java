package ljh.javap.TPJT1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ClientLoginUI extends JFrame implements ActionListener {
	GameLogic gl;
	Container cp;
	JPanel p1, p2, p3, p4, p5, p6, p7;
	JButton b1, b2;
	JTextField jt1 = new JTextField(15);
	JTextField jt2 = new JTextField(15);
	JTextField jt3 = new JTextField(15);

	void init() {
		setLayout(new GridLayout(6, 1));

		b1 = new JButton("로그인");
		b2 = new JButton("닫   기");
		b1.addActionListener(this);
		// b2.addActionListener(this);
		b1.setPreferredSize(new Dimension(100, 23));
		b2.setPreferredSize(new Dimension(100, 23));

		cp = getContentPane();

		cp.add(p1 = new JPanel());
		// cp.add(p2 = new JPanel());
		cp.add(p3 = new JPanel());
		// cp.add(p4 = new JPanel());
		cp.add(p5 = new JPanel());
		cp.add(p6 = new JPanel());
		cp.add(p7 = new JPanel());

		p1.setLayout(new FlowLayout());
		// p2.setLayout(new GridLayout(1, 2));
		p3.setLayout(new FlowLayout());
		// p4.setLayout(new GridLayout(1, 2));
		p5.setLayout(new FlowLayout());
		p6.setLayout(new GridLayout(1, 2));
		p7.setLayout(new FlowLayout());

		p1.add(new JLabel(" [ 포        트 ]"));
		p1.add(jt1);
		jt1.addActionListener(this);
		p3.add(new JLabel(" [ 서  버  I P ]"));
		p3.add(jt2);
		p5.add(new JLabel(" [ 닉  네  임 ]"));
		p5.add(jt3);
		// p7.add(new JLabel());
		p7.add(b1);
		p7.add(new JLabel("           "));
		p7.add(b2);
		// p7.add(new JLabel());

		setUI();
	}

	void setUI() {
		setTitle("STAND UP! Client Login");
		setSize(300, 210);
		setVisible(true);
		setLocation(300, 300);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("이벤트감지");
		gl = new GameLogic();
		MainFrame mf = new MainFrame(gl);
		mf.cliIN();
		Object obj = e.getSource();
		if(obj == jt1) {
			
		}
		
//		Object obj = e.getSource();
//		if (obj == b1) {
//			pln("로그인성공");
//			JOptionPane.showMessageDialog(null, "메세지 내용", "제목", JOptionPane.QUESTION_MESSAGE, null);

	}

	void pln(String str) {
		System.out.println(str);
	}

	
	public static void main(String[] args) {
		ClientLoginUI clui = new ClientLoginUI();
		clui.init();

	}
}
