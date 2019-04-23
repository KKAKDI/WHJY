
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
			pln("Á¤¼ö ¸¸ ÀÔ·ÂÇÏ¼¼¿ä." + nfe.getMessage());
			cntUser();// Àç±Í È£Ãâ
		}
	}

	void roll(int userCnt) {
		this.userCnt = userCnt;
		ts = new TreeSet<String>();
		hm = new HashMap<Integer, String>();
		int i = 0;
		powers = new int[userCnt];
		while (true) { // user ¼ö ¸¸Å­ Ä«µå¸¦ »ÌÀ½
			tile = r.nextInt(20); // 0~19
			ts.add(strs[tile]); // Áßº¹ Á¦°Å
			if (ts.size() == userCnt * 2)
				break; // ÆÐ 2°³ ÀúÀå ÈÄ Á¾·á
		}
		for (String msg : ts) { // Áßº¹Á¦°Å µÈ °ªÀ» Å°°ªÀ¸·Î ÀúÀå
			hm.put(i, msg);
			i++;
		}
		play();
	}

	void play() {// ÆÐµ¹¸®±â ¸Þ¼Òµå
		card1 = new int[userCnt];
		card2 = new int[userCnt];
		for (int i = 0; i < userCnt; i++) { // À¯Àú ¼ö ÆÐ 2Àå ¼øÂ÷ µ¹¸²
			card1[i] = Integer.parseInt(hm.get(i)); // Ã¹ÆÐ
			cardMsg = hm.get(i);
			scm.broadcast("#card1_" + cardMsg);
		}
		for (int j = 0; j < userCnt; j++) {
			card2[j] = Integer.parseInt(hm.get(userCnt + j)); // µÎ¹øÂ° ÆÐ
			cardMsg = hm.get(userCnt + j);
			scm.broadcast("#card2_" + cardMsg);
		}
		for (int k = 0; k < userCnt; k++) {
			p(card1[k] + " | " + card2[k] + ">>");
			powers[k] = cardTree(card1[k], card2[k]); // ÆÄ¿ö Æ÷ÀÎÆ® return
			if (win < powers[k])
				win = powers[k];// °¡Àå Å« ¼ö ÀúÀå
		}
		if (win == 26 || win == 16) {
			reGame();
		}
		judge();
	}

	int judge() { // °øµ¿ ¿ì½Â °æ¿ì Ã³¸® ÇÊ¿ä // ½Â¸®ÆÇº° ¸Þ¼Òµå
		int draw = 0;
		int msg = 0;
		for (int i = 0; i < userCnt; i++) { // ½Â¸® ÆÇº°
			if (powers[i] == win) {
				pln("user" + (i + 1) + " ½Â¸®");
				scm.broadcast("#judge_ÀÚ¸® " + i + " ½Â¸®");
				draw++;
				msg = i;
			}
		}
		if (draw > 1) {
			pln("¹« ½Â ºÎ" + draw + "¸í");
			roll(draw);
		}
		return msg;
	}

	void reGame() { // ´Ù½Ã ÇÑÆÇ
		pln("¹¯°í ´õºí·Î °¡!");
		win = 0;
		for (int j = 0; j < userCnt; j++) { // ÆÄ¿ö Æ÷ÀÎÆ® ¹è¿­ ÃÊ±âÈ­
			powers[j] = 0;
		}
		roll(userCnt);
	}

	int cardTree(int card1, int card2) { // Á·º¸
		int cardSub = card1 - card2;
		int cardAdd = card1 + card2;
		int rest = cardAdd % 10;
		int power = 0;
		// ±¤¶¯
		if (card1 == 3 && card2 == 8) {
			pln("ß² ø¢ ±¤ ¶¯");
			scm.broadcast("#power_ß² ø¢ ±¤ ¶¯");
			power = 100;
		} else if (card1 == 1 && card2 == 8) {
			pln("ìé ø¢ ±¤ ¶¯");
			scm.broadcast("#power_ìé ø¢ ±¤ ¶¯");
			power = 90;
		} else if (card1 == 1 && card2 == 3) {
			pln("ìé ß² ±¤ ¶¯");
			scm.broadcast("#power_ìé ß² ±¤ ¶¯");
			power = 80;
		}
		// ¶¯
		else if (cardSub == 10 || cardSub == -10) {
			if (card1 == 10 || card1 == 20) {
				pln("Àå ¶¯");
				scm.broadcast("#power_Àå ¶¯");
				power = 27;
			}
			if (card1 == 9 || card1 == 19) {
				pln("±¸ ¶¯");
				scm.broadcast("#power_±¸ ¶¯");
				power = 25;
			}
			if (card1 == 8 || card1 == 18) {
				pln("ÆÈ ¶¯");
				scm.broadcast("#power_ÆÈ ¶¯");
				power = 24;
			}
			if (card1 == 7 || card1 == 17) {
				pln("Ä¥ ¶¯");
				scm.broadcast("#power_Ä¥ ¶¯");
				power = 23;
			}
			if (card1 == 6 || card1 == 16) {
				pln("À° ¶¯");
				scm.broadcast("#power_À° ¶¯");
				power = 22;
			}
			if (card1 == 5 || card1 == 15) {
				pln("¿À ¶¯");
				scm.broadcast("#power_¿À ¶¯");
				power = 21;
			}
			if (card1 == 4 || card1 == 14) {
				pln("»ç ¶¯");
				scm.broadcast("#power_»ç ¶¯");
				power = 20;
			}
			if (card1 == 3 || card1 == 13) {
				pln("»ï ¶¯");
				scm.broadcast("#power_»ï ¶¯");
				power = 19;
			}
			if (card1 == 2 || card1 == 12) {
				pln("ÀÌ ¶¯");
				scm.broadcast("#power_ÀÌ ¶¯");
				power = 18;
			}
			if (card1 == 1 || card1 == 11) {
				pln("ÀÏ ¶¯");
				scm.broadcast("#power_»æ ¶¯");
				power = 17;
			}
		}
		// Áß°£ Á·º¸
		else if ((card1 == 1 || card1 == 11||card1 == 2 || card1 == 12) && (card2 == 2 || card2 == 12||card2 == 1 || card2 == 11)) {
			pln("¾Ë ¸®");
			scm.broadcast("#power_¾Ë ¸®");
			power = 15;
		} else if ((card1 == 1 || card1 == 11||card1 == 4 || card1 == 14) && (card2 == 4 || card2 == 14||card2 == 1 || card2 == 11)) {
			pln("µ¶ »ç");
			scm.broadcast("#power_µ¶ »ç");
			power = 14;
		} else if ((card1 == 1 || card1 == 11||card1 == 9 || card1 == 19) && (card2 == 9 || card2 == 19|| card2 == 1 || card2 == 11)) {
			pln("±¸ »æ");
			scm.broadcast("#power_±¸ »æ");
			power = 13;
		} else if ((card1 == 1 || card1 == 11||card1 == 10 || card1 == 20) && (card2 == 10 || card2 == 20||card2 == 1 || card2 == 1)) {
			pln("Àå »æ");
			scm.broadcast("#power_Àå »æ");
			power = 12;
		} else if ((card1 == 4 || card1 == 14||card1 == 10 || card1 == 20) && (card2 == 10 || card2 == 20||card2 == 4 || card2 == 14)) {
			pln("Àå »ç");
			scm.broadcast("#power_Àå »ç");
			power = 11;
		} else if ((card1 == 4 || card1 == 14 || card1 == 6 || card1 == 16) && (card2 == 6 || card2 == 16 ||card2 == 4 || card2 == 14)) {
			pln("¼¼ ·ú");
			scm.broadcast("#power_¼¼ ·ú");
			power = 10;
		}
		// Æ¯¼ö Á·º¸
		else if (card1 == 4 && card2 == 7) {
			pln("¾Ï Çà ¾î »ç");
			scm.broadcast("#power_¾Ï Çà ¾î »ç");
			if (win > 80)
				power = 99;
			else
				power = 1;
		} else if (card1 == 3 && card2 == 7) {
			pln("¶¯ Àâ ÀÌ");
			scm.broadcast("#power_¶¯ Àâ ÀÌ");
			if (win < 26 && win > 16)
				power = 40;
			else
				power = 0;
		} else if (card1 == 4 && card2 == 9) {
			pln("¸Û ÅÖ ±¸ ¸® »ç ±¸");
			scm.broadcast("#power_¸Û ÅÖ ±¸ ¸® »ç ±¸");
			power = 26;
		} else if ((card1 == 4 && card2 == 19) || (card1 == 14 && card2 == 9)) {
			pln("»ç ±¸ ÆÄ Åä");
			scm.broadcast("#power_»ç ±¸ ÆÄ Åä");
			power = 16;
		}
		// ²ý
		else if (rest == 9) {
			pln("°© ¿À");
			scm.broadcast("#power_°© ¿À");
			power = 9;
		} else if (rest == 8) {
			pln("¿©´ü ²ý");
			scm.broadcast("#power_¿©´ü ²ý");
			power = 8;
		} else if (rest == 7) {
			pln("ÀÏ°ö ²ý");
			scm.broadcast("#power_ÀÏ°ö ²ý");
			power = 7;
		} else if (rest == 6) {
			pln("¿©¼¸ ²ý");
			scm.broadcast("#power_¿©¼¸ ²ý");
			power = 6;
		} else if (rest == 5) {
			pln("´Ù¼¸ ²ý");
			scm.broadcast("#power_´Ù¼¸ ²ý");
			power = 5;
		} else if (rest == 4) {
			pln("³× ²ý");
			scm.broadcast("#power_³× ²ý");
			power = 4;
		} else if (rest == 3) {
			pln("¼¼ ²ý");
			scm.broadcast("#power_¼¼ ²ý");
			power = 3;
		} else if (rest == 2) {
			pln("µÎ ²ý");
			scm.broadcast("#power_µÎ ²ý");
			power = 2;
		} else if (rest == 1) {
			pln("ÇÑ ²ý");
			scm.broadcast("#power_ÇÑ ²ý");
			power = 1;
		} else if (rest == 0) {
			pln("¸Á Åë");
			scm.broadcast("#power_¸Á Åë");
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
