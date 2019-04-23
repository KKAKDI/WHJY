
import java.io.*;
import java.util.*;

class GameLogic{

	String[] strs = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20" };
	Random r = new Random();
	int tile, price, userCnt, win;
	TreeSet<String> ts;
	HashMap<Integer, String> hm;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	int[] powers, card1, card2;
	String cardMsg;
	StuServer sts;
	StuCM scm;

	GameLogic(StuServer sts, int size) {
		this.sts = sts;
		scm = new StuCM(sts);
		userCnt = size;
	}

	void cntUser() {
		try {
			sts = new StuServer();
			roll(userCnt);
		} catch (NumberFormatException nfe) {
			pln("���� �� �Է��ϼ���." + nfe.getMessage());
			cntUser();// ��� ȣ��
		}
	}

	void roll(int userCnt) {
		this.userCnt = userCnt;
		ts = new TreeSet<String>();
		hm = new HashMap<Integer, String>();
		int i = 0;
		powers = new int[userCnt];
		while (true) { // user �� ��ŭ ī�带 ����
			tile = r.nextInt(20); // 0~19
			ts.add(strs[tile]); // �ߺ� ����
			if (ts.size() == userCnt * 2)
				break; // �� 2�� ���� �� ����
		}
		for (String msg : ts) { // �ߺ����� �� ���� Ű������ ����
			hm.put(i, msg);
			i++;
		}
		play();
	}

	void play() {// �е����� �޼ҵ�
		card1 = new int[userCnt];
		card2 = new int[userCnt];
		for (int i = 0; i < userCnt; i++) { // ���� �� �� 2�� ���� ����
			card1[i] = Integer.parseInt(hm.get(i)); // ù��
			cardMsg = hm.get(i);
			scm.broadcast("#card1_" + cardMsg);
		}
		for (int j = 0; j < userCnt; j++) {
			card2[j] = Integer.parseInt(hm.get(userCnt + j)); // �ι�° ��
			cardMsg = hm.get(userCnt + j);
			scm.broadcast("#card2_" + cardMsg);
		}
		for (int k = 0; k < userCnt; k++) {
			p(card1[k] + " | " + card2[k] + ">>");
			powers[k] = cardTree(card1[k], card2[k]); // �Ŀ� ����Ʈ return
			if (win < powers[k])
				win = powers[k];// ���� ū �� ����
		}
		if (win == 26 || win == 16) {
			reGame();
		}
		judge();
	}

	int judge() { // ���� ��� ��� ó�� �ʿ� // �¸��Ǻ� �޼ҵ�
		int draw = 0;
		int msg = 0;
		for (int i = 0; i < userCnt; i++) { // �¸� �Ǻ�
			if (powers[i] == win) {
				pln("user" + (i + 1) + " �¸�");
				scm.broadcast("#judge_�ڸ� " + i + " �¸�");
				draw++;
				msg = i;
			}
		}
		if (draw > 1) {
			pln("�� �� ��" + draw + "��");
			roll(draw);
		}
		return msg;
	}

	void reGame() { // �ٽ� ����
		pln("���� ����� ��!");
		win = 0;
		for (int j = 0; j < userCnt; j++) { // �Ŀ� ����Ʈ �迭 �ʱ�ȭ
			powers[j] = 0;
		}
		roll(userCnt);
	}

	int cardTree(int card1, int card2) { // ����
		int cardSub = card1 - card2;
		int cardAdd = card1 + card2;
		int rest = cardAdd % 10;
		int power = 0;
		// ����
		if (card1 == 3 && card2 == 8) {
			pln("߲ �� �� ��");
			scm.broadcast("#power_߲ �� �� ��");
			power = 100;
		} else if (card1 == 1 && card2 == 8) {
			pln("�� �� �� ��");
			scm.broadcast("#power_�� �� �� ��");
			power = 90;
		} else if (card1 == 1 && card2 == 3) {
			pln("�� ߲ �� ��");
			scm.broadcast("#power_�� ߲ �� ��");
			power = 80;
		}
		// ��
		else if (cardSub == 10 || cardSub == -10) {
			if (card1 == 10 || card1 == 20) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 27;
			}
			if (card1 == 9 || card1 == 19) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 25;
			}
			if (card1 == 8 || card1 == 18) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 24;
			}
			if (card1 == 7 || card1 == 17) {
				pln("ĥ ��");
				scm.broadcast("#power_ĥ ��");
				power = 23;
			}
			if (card1 == 6 || card1 == 16) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 22;
			}
			if (card1 == 5 || card1 == 15) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 21;
			}
			if (card1 == 4 || card1 == 14) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 20;
			}
			if (card1 == 3 || card1 == 13) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 19;
			}
			if (card1 == 2 || card1 == 12) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 18;
			}
			if (card1 == 1 || card1 == 11) {
				pln("�� ��");
				scm.broadcast("#power_�� ��");
				power = 17;
			}
		}
		// �߰� ����
		else if ((card1 == 1 || card1 == 11||card1 == 2 || card1 == 12) && (card2 == 2 || card2 == 12||card2 == 1 || card2 == 11)) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 15;
		} else if ((card1 == 1 || card1 == 11||card1 == 4 || card1 == 14) && (card2 == 4 || card2 == 14||card2 == 1 || card2 == 11)) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 14;
		} else if ((card1 == 1 || card1 == 11||card1 == 9 || card1 == 19) && (card2 == 9 || card2 == 19|| card2 == 1 || card2 == 11)) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 13;
		} else if ((card1 == 1 || card1 == 11||card1 == 10 || card1 == 20) && (card2 == 10 || card2 == 20||card2 == 1 || card2 == 1)) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 12;
		} else if ((card1 == 4 || card1 == 14||card1 == 10 || card1 == 20) && (card2 == 10 || card2 == 20||card2 == 4 || card2 == 14)) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 11;
		} else if ((card1 == 4 || card1 == 14 || card1 == 6 || card1 == 16) && (card2 == 6 || card2 == 16 ||card2 == 4 || card2 == 14)) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 10;
		}
		// Ư�� ����
		else if (card1 == 4 && card2 == 7) {
			pln("�� �� �� ��");
			scm.broadcast("#power_�� �� �� ��");
			if (win > 80)
				power = 99;
			else
				power = 1;
		} else if (card1 == 3 && card2 == 7) {
			pln("�� �� ��");
			scm.broadcast("#power_�� �� ��");
			if (win < 26 && win > 16)
				power = 40;
			else
				power = 0;
		} else if (card1 == 4 && card2 == 9) {
			pln("�� �� �� �� �� ��");
			scm.broadcast("#power_�� �� �� �� �� ��");
			power = 26;
		} else if ((card1 == 4 && card2 == 19) || (card1 == 14 && card2 == 9)) {
			pln("�� �� �� ��");
			scm.broadcast("#power_�� �� �� ��");
			power = 16;
		}
		// ��
		else if (rest == 9) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 9;
		} else if (rest == 8) {
			pln("���� ��");
			scm.broadcast("#power_���� ��");
			power = 8;
		} else if (rest == 7) {
			pln("�ϰ� ��");
			scm.broadcast("#power_�ϰ� ��");
			power = 7;
		} else if (rest == 6) {
			pln("���� ��");
			scm.broadcast("#power_���� ��");
			power = 6;
		} else if (rest == 5) {
			pln("�ټ� ��");
			scm.broadcast("#power_�ټ� ��");
			power = 5;
		} else if (rest == 4) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 4;
		} else if (rest == 3) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 3;
		} else if (rest == 2) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 2;
		} else if (rest == 1) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 1;
		} else if (rest == 0) {
			pln("�� ��");
			scm.broadcast("#power_�� ��");
			power = 0;
		}
		return power;
	}

	void p(String str) {
		System.out.print(str);
	}

	void pln(String str) {
		System.out.println(str);
	}
}
