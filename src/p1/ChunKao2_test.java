package p1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
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

import io.netty.util.internal.SystemPropertyUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;

import p1.FileUtils;

public class ChunKao2_test {
	private static Configuration conf;
	private static Configuration conf1;
	protected static final String CHAR_SET = "UTF-8";

	public static void ConnectHbase() {
		
		//引入krb5.conf文件
		System.setProperty("java.security.krb5.conf", "D:\\sjp\\myhbase\\renzheng\\krb5.conf");
		conf = new Configuration();
		//conf.addResource(new Path("D:\\\\sjp\\\\myhbase\\\\renzheng\\\\hbase-site.xml"));
		
		conf.set("hbase.zookeeper.quorum",
				"172.17.148.185,172.17.148.186,172.17.148.188");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");
		conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
		conf.set("hbase.security.authentication", "kerberos");
		conf.set("hadoop.security.authentication", "kerberos");
		conf.set("zookeeper.znode.parent", "/hyperbase1");
		
		conf1 = HBaseConfiguration.create(conf);
		UserGroupInformation.setConfiguration(conf1);
		try {
			
			//验证keytab
			UserGroupInformation.loginUserFromKeytab("hbase/kundb4", "D:\\sjp\\myhbase\\renzheng\\hyperbase.keytab");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("add Resources!");
		System.out.println(conf1.get("hbase.zookeeper.quorum"));
	}

	public static void createTable(String tableName, String family)
			throws Exception {
		System.out.println("start create table!");
		HBaseAdmin admin = new HBaseAdmin(conf1);

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
	public static void dropTable(String tableName)
			throws Exception {
		System.out.println("start drop table!");
		HBaseAdmin admin = new HBaseAdmin(conf1);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
	}

	public static void addData(int rowKey, String tableName,
			String familyName, String[] columName, String[] value)
			throws Exception {
		Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
		HTable table = new HTable(conf1, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等//
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
		HTable table = new HTable(conf1, Bytes.toBytes(tableName));// 获取表
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			//System.out.println("family:" + Bytes.toString(kv.getFamily()));
			//System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			//System.out.println("Timestamp:" + kv.getTimestamp());
			//System.out.println("-------------------------------------------");
			if ((Bytes.toString(kv.getQualifier()).equals("e"))) {
				FileUtils.getFileFromByte(kv.getValue(), "result/" + rowKey+".gif");
			}

		}
		return result;
	}

	public static void main(String[] args) throws Exception {

		// 设置HBase表名以及列族
		String tableName = "test_CK2";
		String familyName = "f";

		String rowkey=null;
       // int start=Integer.parseInt(args[0]);
       // int end=Integer.parseInt(args[1]);
		// 为表添加数据


		ConnectHbase();
		
		//删除HBase表方法慎用
		//dropTable(tableName);
		createTable(tableName, familyName);
		
		HTable table = new HTable(conf1, Bytes.toBytes(tableName));
		
		
		
		 String bmh="";
		 String zkzh="";
		 String xm ="";
		 String mm="";
		 String IPTL="";
		 InputStream fis = null;
		 
		 //扫描所有DBP下所有的文件
		 File path = new File("D:\\sjp\\myhbase\\test_ck"); 
		 
		 File[] files = path.listFiles();
	for (File file:files) {
		try {  
	        // 读取文件的输入流  
	         fis = new FileInputStream(file);  
	        // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
	        DBFReader reader = new DBFReader(fis);  
	        //乱码的话进行调整，UTF-8,GBK,GB18030
	        reader.setCharactersetName("GBK");
	        
	        // 调用DBFReader对实例方法得到path文件中字段的个数  
	        int fieldsCount = reader.getFieldCount();  
	        // 取出字段信息  
	        for (int i = 0; i < fieldsCount; i++) {  
	            DBFField field = reader.getField(i);  
	            //System.out.println(field.getName());  
	        }  
	        Object[] rowValues;  
	        // 一条条取出path文件中记录  
	        int count=0;
	        while ((rowValues = reader.nextRecord()) != null) { 
	        	   bmh=rowValues[0].toString();
	        	   
	        	   zkzh=rowValues[1].toString();
	        	   //System.out.println(zkzh);
	               xm=rowValues[2].toString();
	               //byte[] xm1=new byte[1024];
	               //xm1=Bytes.toBytes(xm);
	               //String str = new String(xm1, "UTF-8");
	               //System.out.println(str);
	               
	               mm=rowValues[3].toString();
	               IPTL=rowValues[4].toString();
	                rowkey =bmh;
	           	Put put = new Put(Bytes.toBytes(rowkey+""));// 设置rowkey和报名号一样	
	           	put.add(Bytes.toBytes(familyName), Bytes.toBytes("a"),	Bytes.toBytes(bmh));//考生报名号
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("b"),	Bytes.toBytes(zkzh));//考生准考号
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("c"), Bytes.toBytes(xm)); //姓名
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("d"),	Bytes.toBytes(mm));	//密码	 		
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("e"),FileUtils.getFileBytes(new File("D:\\sjp\\myhbase\\test_picture\\"+IPTL))); //成绩图片
				put.add(Bytes.toBytes(familyName), Bytes.toBytes("f"),	Bytes.toBytes(IPTL)); //图片路径
               
				table.put(put);
				count++;
				//System.out.println(count);

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
