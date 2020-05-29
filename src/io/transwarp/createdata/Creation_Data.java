package io.transwarp.createdata;

public class Creation_Data {
	String bmh="";                //报名号
	String zkzh="";               //准考证号
	String xm ="";            //姓名
	String mmnet="";          //密码
	String img="";            //gif图片
	
	public static void main(String[] args) {
		Creation_Data cData = new Creation_Data();
		cData.get_mm();
	}
	
	public String get_xm() {
		String[] xingshi = {"张","李","杜","欧阳","赵","山本","罗","朱","任","徐","房","刘","冯","田"};
		String[] ming = {"一郎","雨","峰","贵","俞","新一","小新","文","丽","中华","玉溪","黎明","云龙","建国"};
		xm=xingshi[(int)(Math.random()*(14))]+ming[(int)(Math.random()*(14))];
		//System.out.println(xm);
		return xm;
	}
	public String get_bmh() {
		String bmh = "17310937"+(int)(Math.random()*(999999-100000)+100000);
		//System.out.println(bmh);
		return bmh;
	}
	public String get_mm() {
		String zkzh = ""+(int)(Math.random()*(999999-100000)+100000);
		//System.out.println(zkzh);
		return zkzh;
	}
}
