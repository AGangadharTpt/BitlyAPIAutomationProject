package reusables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;



public class ReusableMethods {
	String path = System.getProperty("user.dir");
	
	public HashMap getData(String testcaseName) throws IOException
	{
		//fileInputStream argument
				ArrayList<String> a=new ArrayList<String>();
				HashMap hs = new HashMap();
				
				FileInputStream fis=new FileInputStream("C:\\Users\\Sri Hari\\Downloads\\Project\\APIFramework\\APIFramework\\TestData\\DataSheet.xlsx");
				XSSFWorkbook workbook=new XSSFWorkbook(fis);
				
				int sheets=workbook.getNumberOfSheets();
							
					XSSFSheet sheet=workbook.getSheet("APITestData");
					System.out.println(sheet.getSheetName());
					
					//Identify Testcases coloum by scanning the entire 1st row
					
					 Iterator<Row>  rows= sheet.iterator();// sheet is collection of rows
					Row firstrow= rows.next();
					Iterator<Cell> ce=firstrow.cellIterator();//row is collection of cells
					int k=0;
					int coloumn = 0;
				while(ce.hasNext())
				{
					Cell value=ce.next();
					
					System.out.println(value.getStringCellValue());
					if(value.getStringCellValue().equalsIgnoreCase("Testcases"))
					{
						coloumn=k;
						break;
					}
					
					k++;
				}
				System.out.println(coloumn);
				
				while(rows.hasNext())
				{
					
					Row r=rows.next();
					
					if(r.getCell(coloumn).getStringCellValue().equalsIgnoreCase(testcaseName))
					{
						
						Iterator<Cell> HeadersColumn=firstrow.cellIterator();
						Iterator<Cell>  cv=r.cellIterator();
						while(cv.hasNext() && HeadersColumn.hasNext())
						{
						Cell c=	cv.next();
						Cell headersColumnValue = HeadersColumn.next();
						if(c.getCellTypeEnum()==CellType.STRING)
						{
							hs.put(headersColumnValue.getStringCellValue(), c.getStringCellValue());
						//a.add(c.getStringCellValue());
						}
						else{
							hs.put(headersColumnValue.getStringCellValue(), NumberToTextConverter.toText(c.getNumericCellValue()));
							//a.add(NumberToTextConverter.toText(c.getNumericCellValue()));
						
						}
						}
						break;
					}
					
				
				}
							//}
				//}
				System.out.println(hs);
				return hs;
	}
	
	
	public static JSONObject updateJson(JSONObject obj, String keyString, String newValue) throws Exception {
        JSONObject json = new JSONObject();
        // get the keys of json object
        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if the key is a string, then update the value
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyString))) {
                    // put new value
                    obj.put(key, newValue);
                    return obj;
                }
            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                updateJson(obj.getJSONObject(key), keyString, newValue);
            }

            // if it's jsonarray
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    updateJson(jArray.getJSONObject(i), keyString, newValue);
                }
            }
        }
        return obj;
    }
	
	
	public static String getJsonValue(String jsonReq, String key) {
        JSONObject json = new JSONObject(jsonReq);
        boolean exists = json.has(key);
        Iterator<?> keys;
        String nextKeys;
        String val = "";
        if (!exists) {
            keys = json.keys();
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    if (json.get(nextKeys) instanceof JSONObject) {
                        return getJsonValue(json.getJSONObject(nextKeys).toString(), key);
                    } else if (json.get(nextKeys) instanceof JSONArray) {
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        int i = 0;
                        if (i < jsonArray.length()) do {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);
                            return getJsonValue(innerJson.toString(),key);
                        } while (i < jsonArray.length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            val = json.get(key).toString();
        }
        return val;
    }
	
	public static void main(String args[]) throws IOException {
		ReusableMethods u = new ReusableMethods();
		//u.getData("CreateBitlinks","APITestData");
		
	}
}
