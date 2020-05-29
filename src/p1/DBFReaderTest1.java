package p1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;
import com.linuxense.javadbf.DBFWriter;

public class DBFReaderTest1 {
	
	public static void main(String args[]) {
//		 InputStream fis = null;  
//		 File path = new File("DBF"); 
//		 File[] files = path.listFiles();
//	for (File file:files) {
//		try {  
//	        // 读取文件的输入流  
//	         fis = new FileInputStream(file);  
//	        // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
//	        DBFReader reader = new DBFReader(fis);  
////	        reader.setCharactersetName("utf-8");
//	        // 调用DBFReader对实例方法得到path文件中字段的个数  
//	        int fieldsCount = reader.getFieldCount();  
//	        // 取出字段信息  
//	        for (int i = 0; i < fieldsCount; i++) {  
//	            DBFField field = reader.getField(i);  
//	            System.out.println(field.getName());  
//	        }  
//	        Object[] rowValues;  
//	        // 一条条取出path文件中记录  
//	        while ((rowValues = reader.nextRecord()) != null) {  
//	        	   for (int i = 0; i < rowValues.length; i++) {  
//	                    System.out.println(rowValues[i]);  
//	                }   
//
//	        }  
//	    } catch (Exception e) {  
//	        e.printStackTrace();  
//	    }			
//	}
//		
	 int zkzh=1;
	 String xm ="张三";
	 String mmnet="123456";
	 String img="";
	 
	 OutputStream fos;
     try {  
    	 fos = new FileOutputStream("2019zhongkao.dbf"); 
         // 定义DBF文件字段  
         DBFField[] fields = new DBFField[4];  
         // 分别定义各个字段信息，setFieldName和setName作用相同,  
         // 只是setFieldName已经不建议使用  
         fields[0] = new DBFField();  
         // fields[0].setFieldName("emp_code");  
         fields[0].setName("KSBMH");  
         fields[0].setType(DBFDataType.CHARACTER); 
         fields[0].setLength(20);  
         
         fields[1] = new DBFField();  
         // fields[1].setFieldName("emp_name");  
         fields[1].setName("XM");  
         fields[1].setType(DBFDataType.CHARACTER);  
         fields[1].setLength(60); 
         
         fields[2] = new DBFField();  
         // fields[2].setFieldName("salary");  
         fields[2].setName("MM");  
         fields[2].setType(DBFDataType.CHARACTER);  
         fields[2].setLength(10);    
         
         fields[3] = new DBFField();  
         // fields[2].setFieldName("salary");  
         fields[3].setName("IPLJ");  
         fields[3].setType(DBFDataType.CHARACTER);  
         fields[3].setLength(30);   
         // DBFWriter writer = new DBFWriter(new File(path));  
         // 定义DBFWriter实例用来写DBF文件  
         DBFWriter writer = new DBFWriter(fos);  

         writer.setCharactersetName("utf-8"); 
         // 把字段信息写入DBFWriter实例，即定义表结构  
         writer.setFields(fields);  
         // 一条条的写入记录  
         Object[] rowData = new Object[4];  
         for(int i=1;i<=70000;i++) {
        	 rowData = new Object[4];
        	 rowData[0] = i+"";  
             rowData[1] = "张三"+i;  
             rowData[2] = i+"";
             rowData[3] =i+".gif";
             writer.addRecord(rowData);
             System.out.println(i);
         }
      
         DBFUtils.close(writer);
     } catch (Exception e) {  
         e.printStackTrace();  
     }
 }  
	

}
