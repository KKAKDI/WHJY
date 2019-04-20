
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ClientLoginUI extends JFrame implements ActionListener {
	StuClient sc;
	Container cp;
	JPanel p1, p2, p3, p4, p5, p6, p7;
	JButton b1, b2;
	JTextField jt1 = new JTextField(15);
	JTextField jt2;
	JTextField jt3 = new JTextField(15);
	String ipstart, idstart;

	void init() {
		setLayout(new GridLayout(5, 1));

		b1 = new JButton("로그인");
		b2 = new JButton("닫   기");
		b1.addActionListener(this);
		b2.addActionListener(this);
		b1.setPreferredSize(new Dimension(100, 23));
		b2.setPreferredSize(new Dimension(100, 23));

		cp = getContentPane();

		cp.add(p1 = new JPanel());
		cp.add(p3 = new JPanel());
		cp.add(p5 = new JPanel());
		cp.add(p6 = new JPanel());
		cp.add(p7 = new JPanel());

		p1.setLayout(new FlowLayout());
		p3.setLayout(new FlowLayout());
		p5.setLayout(new FlowLayout());
		p6.setLayout(new GridLayout(1, 2));
		p7.setLayout(new FlowLayout());

		jt1.addActionListener(this);
		p3.add(new JLabel(" [ 서  버  I P ]"));
		p3.add(jt2 = new JTextField("localhost", 15));
		p5.add(new JLabel(" [ 닉  네  임 ]"));
		p5.add(jt3);
		p7.add(b1);
		p7.add(new JLabel("           "));
		p7.add(b2);
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
		Object obj = e.getSource();
		if (obj == jt1) {
		} else if (obj == b1) {
			ipstart = jt2.getText();
			idstart = jt3.getText();
			if (ipstart.length() == 0)// ip="127.0.0.1"; //채연 ip 203.236.209.207
			{
				JOptionPane.showMessageDialog(null, "IP를 다시입력해주세요.");
			} else {
				sc = new StuClient(this);
				sc.connect();	//메인쓰레드 호출 
				setVisible(false);
			}
		} else if (obj == b2) {
			System.exit(0);
		}
	}

	void pln(String str) {
		System.out.println(str);
	}

	public static void main(String[] args) {
		ClientLoginUI clui = new ClientLoginUI();
		clui.init();

	}
}
