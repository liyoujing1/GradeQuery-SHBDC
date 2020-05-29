package p1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

import p1.FileUtils;

public class Gaokao {
	private static Configuration conf;

	public static void ConnectHbase() throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		conf = new Configuration();
		// conf.addResource(new Path("hbase-site.xml"));
		conf.set("zookeeper.znode.parent", "/hyperbase1");
		conf.set("hbase.zookeeper.quorum",
				"172.17.148.185,172.17.148.186,172.17.148.188");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		System.out.println("add Resources!");
		System.out.println(conf.get("hbase.zookeeper.quorum"));
	}

	public static void createTable(String tableName, String family)
			throws Exception {
		System.out.println("start create table!");

		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor desc = new HTableDescriptor(tableName);

		desc.addFamily(new HColumnDescriptor(family));

		if (admin.tableExists(tableName)) {
			System.out.println("table Exists!");
			admin.disableTable(tableName);
			admin.deleteTable(tableName);

			admin.createTable(desc);
			System.out.println("create table Success!");

		} else {
			admin.createTable(desc);
			System.out.println("create table Success!");
		}
	}

	public static void addData(int rowKey, String tableName,
			String familyName, String[] columName, String[] value)
			throws Exception {
		Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
		HTable table = new HTable(conf, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等//
		// 获取表
		put.add(Bytes.toBytes(familyName), Bytes.toBytes(columName[0]),
				Bytes.toBytes(value[0]));
		put.add(Bytes.toBytes(familyName), Bytes.toBytes(columName[1]),FileUtils.getFileBytes(new File(value[1])));

		table.put(put);
		System.out.println("add data Success!");
	}

	public static Result getResult(String tableName, int rowKey)
			throws IOException {
		Get get = new Get(Bytes.toBytes(rowKey+""));
		HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			//System.out.println("family:" + Bytes.toString(kv.getFamily()));
			//System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			//System.out.println("Timestamp:" + kv.getTimestamp());
			//System.out.println("-------------------------------------------");
			if ((Bytes.toString(kv.getQualifier()).equals("d"))) {
				FileUtils.getFileFromByte(kv.getValue(), "result/" +"cj_"+ rowKey+".gif");
			}

		}
		return result;
	}

	public static void main(String[] args) throws Exception {

		// 创建表
		String tableName = "gaokao";
		String familyName = "f";

		String rowkey=null;
       // int start=Integer.parseInt(args[0]);
       // int end=Integer.parseInt(args[1]);
		// 为表添加数据


		ConnectHbase();
		createTable(tableName, familyName);
		
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		
		
		
		
		 String zkzh="1";
		 String xm ="张三";
		 String mmnet="123456";
		 String img="";
		 InputStream fis = null;  
		 File path = new File("DBF"); 
		 File[] files = path.listFiles();
	for (File file:files) {
		try {  
	        // 读取文件的输入流  
	         fis = new FileInputStream(file);  
	        // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
	        DBFReader reader = new DBFReader(fis);  
	       // reader.setCharactersetName("utf-8");
	        // 调用DBFReader对实例方法得到path文件中字段的个数  
	        int fieldsCount = reader.getFieldCount();  
	        // 取出字段信息  
	        for (int i = 0; i < fieldsCount; i++) {  
	            DBFField field = reader.getField(i);  
	            System.out.println(field.getName());  
	        }  
	        Object[] rowValues;  
	        // 一条条取出path文件中记录  
	        int count=0;
	        while ((rowValues = reader.nextRecord()) != null) {  
	        	   zkzh=rowValues[0].toString();
	               xm=rowValues[1].toString();
	               mmnet=rowValues[2].toString();
	               img=rowValues[3].toString();
	                rowkey =zkzh;
	           	Put put = new Put(Bytes.toBytes(rowkey+""));// 设置rowkey和准考号一样				
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("a"),	Bytes.toBytes(zkzh));//考生准考号
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("b"),	Bytes.toBytes(xm)); //姓名
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("c"),	Bytes.toBytes(mmnet));	//密码	 		
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("d"),FileUtils.getFileBytes(new File("result/"+img))); //成绩图片
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("e"),	Bytes.toBytes(img)); //图片路径
               
				table.put(put);
				count++;
				System.out.println(count);

	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }			
	}
	
		
/*		for (int i = 1; i <=50000;i++){
			rowkey=i;
			System.out.println(i);
			Put put = new Put(Bytes.toBytes(rowkey+""));// rowkey和准考号一样				
			put.add(Bytes.toBytes(familyName), Bytes.toBytes("a"),	Bytes.toBytes(rowkey+""));  
			put.add(Bytes.toBytes(familyName), Bytes.toBytes("b"),	Bytes.toBytes("张三"+rowkey));
			put.add(Bytes.toBytes(familyName), Bytes.toBytes("c"),	Bytes.toBytes(rowkey+""));
			
			put.add(Bytes.toBytes(familyName), Bytes.toBytes("d"),FileUtils.getFileBytes(new File("picture/cj_123456789.gif")));

			table.put(put);
		}*/



	}
}
