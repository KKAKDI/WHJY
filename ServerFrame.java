
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ServerFrame extends JFrame implements ActionListener {

	Container cp1;
	JPanel p7, p8, p9, p10;
	JButton b7;

	void sevIN() {
		setLayout(new GridLayout(6, 1));

		b7 = new JButton("����STOP");
		// b1.addActionListener(this);
		cp1 = getContentPane();

		cp1.add(p7 = new JPanel());
		cp1.add(p8 = new JPanel());
		cp1.add(p9 = new JPanel());
		cp1.add(p10 = new JPanel());

		p7.setLayout(new FlowLayout());
		p8.setLayout(new FlowLayout());
		p9.setLayout(new FlowLayout());
		p10.setLayout(new GridLayout(1, 2));

		p7.add(new JLabel(" [ ��       Ʈ ]"));
		p7.add(new JTextArea("��Ʈ ��ȣ �ֱ�"));
		p8.add(new JLabel(" [ �� �� I P ]"));
		p8.add(new JTextArea("����IP �ֱ�"));
		p9.add(b7);
		p10.add(new JScrollPane(new JTextArea()));

		sevUI();
	}

	void sevUI() {
		setTitle("STAND UP! Server ver 1.0");
		setSize(400, 500);
		setVisible(true);
		setLocation(300, 300);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("�̺�Ʈ����");
	}
}