
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import java.net.*;

class ServerMainFrame extends JFrame implements ActionListener {
	Container cp1;
	JPanel p7, p8, p9, p10;
	JButton b7, b8;
	JLabel jl1, jl2;
	String port1;
	JTextField jtf1, jtf2;
	JTextArea jta1;
	StuServer sts;

	void sevIN() {

		setLayout(new FlowLayout());
		b7 = new JButton("서버  START");
		b8 = new JButton("서 버 종 료");
		// b7.setSize(10, 50);
		// b8.setSize(10, 50);
		cp1 = getContentPane();

		cp1.add(p7 = new JPanel());
		cp1.add(p8 = new JPanel());
		cp1.add(p9 = new JPanel());
		cp1.add(p10 = new JPanel());

		p7.setLayout(new FlowLayout());
		p8.setLayout(new FlowLayout());
		p9.setLayout(new FlowLayout());
		p10.setLayout(new FlowLayout());

		p7.add(jl1 = new JLabel(" [ 포      트 ]"));
		p7.add(jtf1 = new JTextField(10));
		p8.add(jl2 = new JLabel(" [ 서 버 I P ]"));
		p8.add(jtf2 = new JTextField(10));
		p9.add(b7);
		b7.addActionListener(this);
		b7.setEnabled(true);
		p9.add(b8);
		b8.setEnabled(false);
		b8.addActionListener(this);
		p10.add(new JScrollPane(jta1 = new JTextArea(13, 25)));
		sts = new StuServer();
		sevUI();

	}

	void sevUI() {
		setTitle("STAND UP! Server ver 1.0");
		setSize(300, 410);
		setVisible(true);
		setLocation(300, 300);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();
		if (obj == b7) {
			jta1.append("[서버가 오픈되었습니다.]\n");
			// JOptionPane.showMessageDialog(null, "서버가 오픈되었습니다.");
			b7.setEnabled(false);
			b8.setEnabled(true);
			sts.init();
		} else if (obj == b8) {
			jta1.append("[서버가 종료되었습니다.]\n");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		ServerMainFrame smf = new ServerMainFrame();
		smf.sevIN();
	}

}