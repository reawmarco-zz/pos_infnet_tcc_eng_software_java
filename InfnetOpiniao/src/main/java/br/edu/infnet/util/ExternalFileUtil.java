package br.edu.infnet.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExternalFileUtil {
	
	public static void createCSVFile(StringBuilder sb, String fileName) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
						new FileOutputStream(VariablesUtil.PATH + fileName + VariablesUtil.FILE_EXTENSION), VariablesUtil.FILE_ENCODING);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String buildAnswerCSVLine(String question, String answer) {
		StringBuilder sb = new StringBuilder();
		sb.append(question).append(VariablesUtil.SEPARATOR).append(answer).append(VariablesUtil.NEW_LINE);
		return sb.toString();
	}
	
	public static String buildFileName(String name) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(sdf.format(new Date())).append("_").append(name.replaceAll(" ", "_"));
		return sb.toString();
	}

}
