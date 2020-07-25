package com.simonhu.common.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadXml {
	
	
	public static void main(String[] args) throws ParseException {
		//创建DOM4J解析器对象
		SAXReader saxReader = new SAXReader();
		int i = 0;
		List<Map> list = new ArrayList<>();
		try {
			//读取xml文件，并生成document对象 现可通过document来操作文档
			Document document = saxReader.read("C:\\Users\\Administrator\\Desktop\\CNBlogs_BlogBackup_1_201706_202007.xml");
			//获取到文档的根节点
			Element rootElement = document.getRootElement();
			System.out.println("根节点的名字是:" + rootElement.getName());
			//获取子节点列表
			Iterator it = rootElement.elementIterator();
			while (it.hasNext()) {
				Element fistChild = (Element) it.next();
				//获取节点的属性值
				System.out.println("第1个子节点--" + fistChild.getName());
				//获取子节点的下一级节点
				Iterator iterator = fistChild.elementIterator();
				while (iterator.hasNext()) {
					Element element = (Element) iterator.next();
					System.out.println("第2个子节点--" + element.getName());
					if (element.getName().equalsIgnoreCase("item")) {
						Map map = new HashMap();
						i++;
						Iterator itemElements = element.elementIterator();
						while (itemElements.hasNext()) {
							Element ielement = (Element) itemElements.next();
							String elemetName = ielement.getName();
							String elemetValue = ielement.getStringValue();
							if ("pubDate".equals(elemetName)) {
								elemetValue = getTimestampTimeV17(elemetValue);
							}
							System.out.println("-------item----name---" + elemetName);
//							System.out.println("-------item-----value--" + elemetValue);
							map.put(elemetName, elemetValue);
						}
						list.add(map);
					}
				}
			}
			System.out.println("-----总共有多少文章---------" + i);
			System.out.println("-----总共有多少文章---------" + list.size());
			writeFile(list);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 格式化GMT时间到北京时间
	 *
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static String getTimestampTimeV17(String str) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		Date date = dateFormat.parse(str);
		//加8个时区
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
		date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatTime = sdf.format(date);
		System.out.println(formatTime);
		return formatTime;
	}
	
	/**
	 * 写入sql文件
	 *
	 * @param mapList
	 * @throws IOException
	 */
	public static void writeFile(List<Map> mapList) throws IOException {
		//写入中文字符时解决中文乱码问题
		FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\wp.sql"));
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		int y = 180;
		for (int i = 0; i < mapList.size(); i++) {
			//所有内容进行json格式化，防止"\n"在sql文件中格式化
			//获取标题
			String post_title = JsonUtils.toJson(String.valueOf(mapList.get(i).get("title")));
			//获取发布时间
			String post_modified = JsonUtils.toJson(String.valueOf(mapList.get(i).get("pubDate")));
			//获取发布内容
			String post_content = JsonUtils.toJson(String.valueOf(mapList.get(i).get("description")));
			//获取文章路径 y根据文章的id来定义
			String guid = JsonUtils.toJson("https://www.simonjia.top/?p=" + y);
			y++;
			String sql = "INSERT INTO `word_press`.`wp_posts`(id,guid,post_author,post_date,post_date_gmt,post_content,post_title,`post_excerpt`," +
					"`post_status`, `comment_status`, `ping_status`, `post_name`,`to_ping`, `pinged`,   `post_modified`, `post_modified_gmt`, `post_content_filtered`,`post_parent`,`menu_order`, `post_type`, `post_mime_type`, `comment_count` )VALUES(" +
					+y + "," + guid + ",1," + post_modified + "," + post_modified + "," + post_content + "," + post_title + ",'','publish','open', 'open'," + post_title + ",'', '', " + post_modified + "," + post_modified + "," + post_content + ",0," +
					"0, 'post', '', 0);";
			//默认给文章分到新目录
//			String sql = "insert into wp_term_relationships values("+y+",1,0);";
			sql = sql.replaceAll("\t", "");
			System.out.println("--------sq;------" + sql);
			bw.write(sql + "\t\n");
		}
		//注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
		bw.close();
		osw.close();
		fos.close();
	}
}

