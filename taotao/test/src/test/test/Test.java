package test.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) throws ParseException {
		//testSimpleDateFormat2();
		//testCalendar();
		//testArrayCopy();
		//测试String用的时间
		/*
		 * long millis = System.currentTimeMillis(); testString(); long millis2 =
		 * System.currentTimeMillis();
		 * System.out.println("Stirng所用的时间是"+(millis2-millis)); long millis3 =
		 * System.currentTimeMillis(); testStringBuilder(); long millis4 =
		 * System.currentTimeMillis();
		 * System.out.println("StirngBuilder所用的时间是"+(millis4-millis3));
		 */
		testStringBuilerAsReverse();
		
	}
	public static void testSimpleDateFormat() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String format = dateFormat.format(date);
		System.out.println(format);
	}
	public static void testSimpleDateFormat2() throws ParseException {
		String value = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = simpleDateFormat.parse("2018-03-04");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
		String format = simpleDateFormat.format(parse);
		System.out.println(format);
	}
	public static void testCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.YEAR,2018);
		calendar.set(calendar.MONTH,1);
		calendar.set(calendar.DATE,14);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int date = calendar.get(Calendar.DATE);
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println(year+"年"+month+"月"+date+"日是星期"+i);
	}
	public static void  testArrayCopy() {
		char [] c = {'i','t','c','a','s','a'};
		System.arraycopy(c, 1, c, 5, 1);
		for (int i = 0;i <c.length;i++) {
			System.out.print(c[i]);
		}
	}
	private static void testString() {
		String string = "";
		for (int i = 0; i < 100000; i++) {
			string+="a";
		}
	}
	
	private static void testStringBuilder() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 100000; i++) {
			stringBuilder.append("a");
		}
	}
	public static void testStringBuilerAsReverse() {
		String[] strs = {"010","3223","666","7890987","123123"};
		int count =0;
		for (String string : strs) {
			StringBuilder stringBuilder = new StringBuilder(string);
			if(stringBuilder.reverse().toString().equals(string)) {
				count ++;
				System.out.println(string+"是对称的");
			}
		}
		System.out.println("对称的有"+count+"个");
	}
}
