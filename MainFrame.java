package ljh.javap.TPJT1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

class MainFrame extends JFrame implements ActionListener {

	GameLogic gl;
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
	// iageIcon i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15,
	// i16, i17, i18, i19, i20;

	ImageIcon i1 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/1+.png");
	ImageIcon i2 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/2+.png");
	ImageIcon i3 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/3+.png");
	ImageIcon i4 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/4+.png");
	ImageIcon i5 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/5+.png");
	ImageIcon i6 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/6+.png");
	ImageIcon i7 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/7+.png");
	ImageIcon i8 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/8+.png");
	ImageIcon i9 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/9+.png");
	ImageIcon i10 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/10+.png");
	ImageIcon i11 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/1.png");
	ImageIcon i12 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/2.png");
	ImageIcon i13 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/3.png");
	ImageIcon i14 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/4.png");
	ImageIcon i15 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/5.png");
	ImageIcon i16 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/6.png");
	ImageIcon i17 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/7.png");
	ImageIcon i18 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/8.png");
	ImageIcon i19 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/9.png");
	ImageIcon i20 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/10.png");
	ImageIcon i30 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/harf.png");
	ImageIcon i100 = new ImageIcon("C:/Users/BITSC-6-20/Desktop/TPJT1/ÆÐ ÀÌ¹ÌÁö/ÆÐµÞ¸é.png");

	MainFrame(GameLogic gl) {
		this.gl = gl;
	}

	String win(int msg, int user) {
		String winMsg = "";
		if (msg == user) {
			winMsg = "½Â ¸®";
		} else {
			winMsg = "ÆÐ ¹è";
		}
		return winMsg;
	}

	String jokbo(int power) {
		String str = "";
		switch (power) {
		case 0:
			str = "	¸Á Åë	";
			break;
		case 1:
			str = "	ÇÑ ²ý	";
			break;
		case 2:
			str = "	µÎ ²ý	";
			break;
		case 3:
			str = "	¼¼ ²ý	";
			break;
		case 4:
			str = "	³× ²ý	";
			break;
		case 5:
			str = "	´Ù¼¸ ²ý	";
			break;
		case 6:
			str = "	¿©¼¸ ²ý	";
			break;
		case 7:
			str = "	ÀÏ°ö ²ý	";
			break;
		case 8:
			str = "	¿©´ü ²ý	";
			break;
		case 9:
			str = "	°© ¿À	";
			break;
		case 10:
			str = "	¼¼ ·ú	";
			break;
		case 11:
			str = "	Àå »ç	";
			break;
		case 12:
			str = "	Àå »æ	";
			break;
		case 13:
			str = "	±¸ »æ	";
			break;
		case 14:
			str = "	µ¶ »ç	";
			break;
		case 15:
			str = "	¾Ë ¸®	";
			break;
		case 16:
			str = "	»ç ±¸ ÆÄ Åä	";
			break;
		case 17:
			str = "	ÀÏ ¶¯	";
			break;
		case 18:
			str = "	ÀÌ ¶¯	";
			break;
		case 19:
			str = "	»ï ¶¯	";
			break;
		case 20:
			str = "	»ç ¶¯	";
			break;
		case 21:
			str = "	¿À ¶¯	";
			break;
		case 22:
			str = "	À° ¶¯	";
			break;
		case 23:
			str = "	Ä¥ ¶¯	";
			break;
		case 24:
			str = "	ÆÈ ¶¯	";
			break;
		case 25:
			str = "	±¸ ¶¯	";
			break;
		case 26:
			str = "	¸Û ÅÖ ±¸ ¸® »ç ±¸	";
			break;
		case 27:
			str = "	Àå ¶¯	";
			break;
		case 40:
			str = "	¶¯ Àâ ÀÌ	";
			break;
		case 80:
			str = "	ÀÏ »ï ±¤ ¶¯ ! !	";
			break;
		case 90:
			str = "	ÀÏ ÆÈ ±¤ ¶¯ ! !	";
			break;
		case 99:
			str = "	¾Ï Çà ¾î »ç	";
			break;
		case 100:
			str = "	»ï ÆÈ ±¤ ¶¯ ! !	";
			break;

		}
		return str;
	}

	ImageIcon inImg(int card) {
		switch (card) {
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
		c1p1.add(mb1 = new JButton());

		c1p2.add(c2p2 = new JPanel());
		c2p2.setLayout(new GridLayout(2, 1));
		c2p2.add(mb1 = new JButton());
		c2p2.add(mb1 = new JButton());
		c2p2.add(jlj2 = new JLabel(jokbo(gl.powers[1])));
		c2p2.add(jwl2 = new JLabel(win(gl.judge(), 1)));
		c1p2.add(jl3 = new JLabel(inImg(gl.card1[1])));
		c1p2.add(jl4 = new JLabel(inImg(gl.card2[1])));

		c1p3.add(jta1 = new JTextArea(11, 32));

		c1p4.add(c2p4 = new JPanel());
		c2p4.setLayout(new GridLayout(2, 1));
		c2p4.add(mb1 = new JButton());
		c2p4.add(mb1 = new JButton());
		c2p4.add(jlj1 = new JLabel(jokbo(gl.powers[0])));
		c2p4.add(jwl1 = new JLabel(win(gl.judge(), 0)));
		c1p4.add(jl1 = new JLabel(inImg(gl.card1[0])));
		c1p4.add(jl2 = new JLabel(inImg(gl.card2[0])));

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
		c2p6.setLayout(new GridLayout(2, 1));
		c2p6.add(mb1 = new JButton());
		c2p6.add(mb1 = new JButton());
		c2p6.add(jlj3 = new JLabel(jokbo(gl.powers[2])));
		c2p6.add(jwl3 = new JLabel(win(gl.judge(), 2)));
		c1p6.add(jl5 = new JLabel(inImg(gl.card1[2])));
		c1p6.add(jl6 = new JLabel(inImg(gl.card2[2])));

		c1p7.add(mb1 = new JButton());
		c1p7.add(c2p7 = new JPanel());
		c2p7.setLayout(new GridLayout(4, 1));
		c2p7.add(mb1 = new JButton());
		c2p7.add(mb1 = new JButton(i30));
		c2p7.add(mb1 = new JButton());
		c2p7.add(mb1 = new JButton("¿Ã~~~~~ÀÎ!!"));

		c1p8.add(c2p8 = new JPanel(), BorderLayout.CENTER);
		c1p8.add(c2p9 = new JPanel(), BorderLayout.SOUTH);
		c2p8.setLayout(new GridLayout(1,2));
		c2p9.setLayout(new GridLayout(1, 3));
		c2p8.add(bp1 = new JButton(i100));
		bp1.addActionListener(this);
		c2p8.add(bp2 = new JButton(i100));
		bp2.addActionListener(this);
		c2p9.add(mb1 = new JButton());
		c2p9.add(jwl4 = new JLabel(win(gl.judge(), 3)));
		c2p9.add(jlj4 = new JLabel(jokbo(gl.powers[3])));

		// c1p9.add(jta2 = new JTextArea(10, 32));

		c1p9.add(jwl4 = new JLabel(" Ã¤ÆÃÀÔ·Â : "));
		c1p9.add(jtf1 = new JTextField(25));
		c1p9.add(new JScrollPane(jta2 = new JTextArea(10, 32)));
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
		Object obj = e.getSource(); // ÀÌº¥Æ® °¨ÁöÇØÁÜ
		if (obj == bp1) {
			bp1.setIcon(inImg(gl.card1[3]));
		} else if (obj == bp2) {
			bp2.setIcon(inImg(gl.card2[3]));
		} else if (obj == jtf1) {
			jta2.append("user >>" + jtf1.getText() + "\n");
			jtf1.setText("");
		}
	}

	void pln(String str) {
		System.out.println(str);
	}

}
