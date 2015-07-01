package survey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.junit.Test;

public class TestPoi {

	@Test
	public void testPost() throws FileNotFoundException, IOException
	{
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		//工作表
		HSSFSheet sheet1 = hssfWorkbook.createSheet("s1");
		HSSFSheet sheet2 = hssfWorkbook.createSheet("s2");
		//行
		HSSFRow row1 = sheet1.createRow(0);
		//单元格
		HSSFCell cell = row1.createCell(0);
		cell.setCellValue(false);
		row1.createCell(1).setCellValue(Calendar.getInstance());
		row1.createCell(2).setCellValue(new Date());
		row1.createCell(3).setCellValue(1114444.11212121);
		row1.createCell(4).setCellValue(new HSSFRichTextString("aaaaaaaaaaa"));
		
		//创建数据他见格式
		HSSFDataFormat format = hssfWorkbook.createDataFormat();
	
		
		//格式化数据
		//日期格式化
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		
		cellStyle.setDataFormat(format.getFormat("yyyy-mm-dd hh:mm:ss"));
		//设置样式
		row1.getCell(1).setCellStyle(cellStyle);
		row1.getCell(2).setCellStyle(cellStyle);
		//doubule格式化
		cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setDataFormat(format.getFormat("#,###.000"));
		row1.getCell(3).setCellStyle(cellStyle);
		//设置列款
		sheet1.setColumnWidth(1, 200);
		
		//自动列款,(单位:1/20点)
		sheet1.autoSizeColumn(2);	
		
		
		sheet1.setColumnWidth(4, 7000);
		
		//自动会绕文本
		cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setWrapText(true);
		row1.getCell(4).setCellStyle(cellStyle);
		//设置文本对齐方式
		row1 = sheet1.createRow(1);
		row1.createCell(0).setCellValue("左上");
		row1.createCell(1).setCellValue("中间");
		row1.createCell(2).setCellValue("右下");
		//设置行高
		row1.setHeightInPoints(50);
		sheet1.setColumnWidth(0, 5000);
		sheet1.setColumnWidth(1, 5000);
		sheet1.setColumnWidth(2, 5000);
		
		//设置其他对其方式
		cellStyle = hssfWorkbook.createCellStyle();
		//左对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		//上对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
		
		row1.getCell(0).setCellStyle(cellStyle);
		//字体颜色
		cellStyle = row1.getCell(1).getCellStyle();
		HSSFFont font = hssfWorkbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 20);
		font.setItalic(true);
		font.setColor(HSSFColor.RED.index);
		cellStyle.setFont(font);
		//字体旋转
		cellStyle = row1.getCell(1).getCellStyle();
		//顺时针都是负数，整数逆时针
		cellStyle.setRotation((short) -20);
		//边框
		row1 = sheet1.createRow(2);
		cell = row1.createCell(0);
		cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		cellStyle.setBottomBorderColor(HSSFColor.BLUE.index);
		cellStyle.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
		cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		
		cell.setCellStyle(cellStyle);
		
		//计算
		row1 = sheet1.createRow(3);
		row1.createCell(0).setCellValue(12);
		row1.createCell(1).setCellValue(22);
		row1.createCell(2).setCellValue(44);
		
		row1.createCell(4).setCellFormula("sum(A4:C3)");
		//移动行 2 往上是是-2，往下是正2
		sheet1.shiftRows(1,	 3, 2);
		
		//拆分窗格,下册窗格第4个开始
		//1000左侧窗格的宽度
		//2000上册窗格的高度
		//3右侧窗格开始显示列的索引
		//4下册窗格开始显示行的索引
		//0激活的面板区
		//sheet1.createSplitPane(1000, 2000, 3, 4, 0);
		
		//冻结窗口
		//1左侧 冻结的列数
		//2上册冻结的行数
		//3右侧窗格开始显示列的索引
		//4下册窗格开始显示行的索引
		//sheet1.createFreezePane(1, 2, 3, 4);
		//保存
		//和并列
		//开始行开始列，结束列结束列
		CellRangeAddress address = new CellRangeAddress(1, 3, 1, 3);
		sheet1.addMergedRegion(address);
		
		
		hssfWorkbook.write(new FileOutputStream(new File("1.xls")));
		
	}
}
