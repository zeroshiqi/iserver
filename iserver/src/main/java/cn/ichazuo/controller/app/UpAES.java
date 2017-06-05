package cn.ichazuo.controller.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UpAES {

	protected static AESCipher cipherWithIv;
	protected java.security.MessageDigest md;
	
	public void init()
	{
		//init
		String key = "483322d1ae220826ea76f2689";
		if(key.length()<32)
			key = paddingZero(key, 32);
		
		String iv = "";
		for (int i = 0; i < 16; i++) {
			iv += (char)0;
		}
		cipherWithIv = new AESCipher(key.getBytes(), iv.getBytes());
	}
	
	public String paddingZero(String key, int size)
	{
		for (int i = key.length(); i < size; i++) {
			key += (char)0;
		}
		return key;
	}
	
	public static String encryp(String value)
	{
		return AESCipher.getEncryptedMessage(value);
	}
	
	public static String decryp(String value)
	{
		return cipherWithIv.getDecryptedMessage(value);
	}
	
	public synchronized String getMD5(byte[] source) 
	{
	        String s = null;
	        char hexDigits[] = { 
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',   'e', 'f' };
	        try {
	        		if(md == null)
	        			md = java.security.MessageDigest.getInstance("MD5");
	        	
	                md.update(source);
	                byte tmp[] = md.digest();
	                char str[] = new char[16 * 2];
	                int k = 0; 
	                for (int i = 0; i < 16; i++) { 
	                        byte byte0 = tmp[i]; 
	                        str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
	                        str[k++] = hexDigits[byte0 & 0xf];
	                }
	                s = new String(str); 
	        } catch (Exception e) {
	                e.printStackTrace();
	        }
	        return s;
	}
	
	/*
	 * For test
	 */
	
	public static void main(String[] args) throws Exception 
	{
		UpAES upAES = new UpAES();
		upAES.init();
		upAES.disEntry("vZP9uAi6onxcXCQFWwRM+Q==");
		System.out.println(upAES.disEntry("vZP9uAi6onxcXCQFWwRM+Q=="));
		//96e79218965eb72c92a549dd5a330112
		//96e79218965eb72c92a549dd5a330112
//		System.out.println("getMD5:["+upAES.getMD5("111111".getBytes()));
//		String decryptedMessage = upAES.decryp("HLOy7Xh4KFE+AQyiwDd9wg==");
//		System.out.println("--decryptedMessage:["+decryptedMessage+"]");
//		decryptedMessage = "111111";
//		System.out.println("--encryptedMessage:["+upAES.encryp(decryptedMessage)+"]");	
//		upAES.test2("http://www.baidu.com");
//		upAES.test2("43");
//		
//		long time = 1437720229273l;
//		Date date = new Date(time);
//		System.out.println("date:["+date+"]");
//		
//		
//		System.out.println("000001:["+upAES.getMD5("000001".getBytes())+"]");
//		System.out.println("111111:["+upAES.getMD5("111111".getBytes())+"]");

		/*
		upAES.test2("1006161");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("dateFormat:["+dateFormat.format(new Date())+"]");
		String decryptedMessage1 = new String(upAES.decryp("4WccQwJs2imSECI0KMOvxQ==").getBytes(), "UTF-8");
		System.out.println("decryp1:["+decryptedMessage1+"]");
		*/
		
		//127.0.0.1:8080/UpWebServer
	
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/setSystemRecommendActivityImage?position=%s&activity_id=%s&sign=%s", "0", "157073");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/getThemeMeetTag?user_id=%s&login_uuid=%s&sign=%s", "1012837", "573");
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/getThemeMeetActivities?user_id=%s&login_uuid=%s&sign=%s", "1012837", "573");
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/getRecommendMeetActivities?user_id=%s&login_uuid=%s&sign=%s", "100283", "573");
		//upAES.createSign("http://s.upok.com.cn/service_v223.php/list/gethotlist?user_id=%s&page_num=%s&sign=%s", "100205", "1");
		//upAES.createSign("http://s.upok.com.cn/f/activity/getActivityShareImage?user_id=%s&activity_id=%s&sign=%s", "0", "5521");
		//upAES.createSign("http://s.upok.com.cn/f/activity/getActivity?user_id=%s&spot_news_id=%s&sign=%s", "100199", "179288");
		//upAES.createSign("http://s.upok.com.cn/f/activity/getThemeMeetActivities?user_id=%s&login_uuid=%s&sign=%s", "1012837", "573");
		//upAES.createSign("http://s2.upok.com.cn/service.php/review/getreviewlist?user_id=%s&review_minid=%s&sign=%s", "1025265", "0");
		//upAES.createSign("http://s2.upok.com.cn/f/user/setUserTalent?user_id=%s&talent=%s&sign=%s", "1025200", "-1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/getMagazinePage?user_id=%s&number=%s&sign=%s", "100283", "-1");
		//upAES.createSign("http://test.upok.com.cn:8017/f/Topic/getHotTopics?v=1&user_id=%s&login_uuid=%s&sign=%s", "100009", "-1");
		//
		//upAES.createSign("http://test.upok.com.cn:8017/service_v223.php/manage/adminmanage?user_id=%s&operatekey=%s&sign=%s", "1012837", "pu");
		//upAES.createSign("http://test.upok.com.cn:8017/f/follow/getFollowList?user_id=%s&login_uuid=%s&sign=%s", "100283", "123");
		
		//upAES.createSign("http://s.upok.com.cn/f/activity/getActivity?user_id=%s&spot_news_id=%s&sign=%s", "100199", "258925");
		//upAES.createSign("http://test.upok.com.cn:8017/f/Topic/getTextTopicInfos?user_id=%s&login_uuid=%s&sign=%s", "100009", "-1");
		
		//
		//upAES.createSign("http://test.upok.com.cn:8017/f/Topic/findTextTopicInfos?user_id=%s&login_uuid=%s&sign=%s", "100009", "-1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/applyForMagazineSuccess?user_id=%s&activity_id=%s&sign=%s", "1013807", "159058");
		//upAES.createSign("http://test.upok.com.cn:8017/f/user/onHeadfigureSuccess?user_id=%s&type=%s&sign=%s", "1025629", "1");
		//Date now = new Date();
		//System.out.println("now:["+now.getTime()+"]");
		
		//upAES.createSign("http://s.upok.com.cn/service_v223.php/set/cancelBindingSns?user_id=%s&push_type=%s&sign=%s", "1142170", "4");
		//upAES.createSign("http://test.upok.com.cn:8017/f/user/getGetRecommendUsers?user_id=%s&category=%s&sign=%s", "1145589", "0");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/crawlActivities?user_id=%s&login_uuid=%s&sign=%s", "100009", "-1");
		//upAES.createSign("http://s.upok.com.cn/f/activity/crawlActivities?user_id=%s&login_uuid=%s&sign=%s", "100009", "-1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/score/add?user_id=%s&login_uuid=%s&sign=%s&category=0", "1145621", "-1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/getRecommendActivityImages?user_id=%s&login_uuid=%s&sign=%s&v=2", "1038334", "0");
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/getRecommendActivityImages?user_id=%s&login_uuid=%s&sign=%s&v=2", "100283", "0");
		

		//upAES.createSign("http://s.upok.com.cn/f/activity/getActivity?user_id=%s&spot_news_id=%s&sign=%s", "1012837", "290475");
		//upAES.createSign("http://test.upok.com.cn:8017/f/activity/viewMagazineInfo?user_id=%s&magazine_id=%s&sign=%s&v=2", "1012837", "1");
		//upAES.createSign("http://test.upok.com.cn:8017/f/watermark/getActivityImagesByWid?wid=%s&type=%s&sign=%s", "45", "2");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/user/getBeautyValue?gender=%s&age=%s&sign=%s", "0", "20");

		//upAES.createSign("http://test.upok.com.cn:8017/f/user/getGetRecommendUsers?user_id=%s&category=%s&sign=%s&relation_type=3", "1145633", "0");
		//upAES.createSign("http://test.upok.com.cn:8017/f/Topic/getHotTopics?user_id=%s&login_uuid=%s&sign=%s&v=3", "100009", "-1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/Topic/viewTextTopic?user_id=%s&text_topic_id=%s&sign=%s", "1", "18");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/user/getSuperUsers?user_id=%s&login_uuid=%s&sign=%s", "1017163", "0");

		//upAES.createSign("http://s2.upok.com.cn/service_v223.php/user/get_userinfo_byid?user_id=%s&target_user_id=%s&sign=%s", "1104635", "1104635");
		//upAES.createSign("http://test.upok.com.cn:8017/service_v223.php/user/get_userinfo_byid?user_id=%s&target_user_id=%s&sign=%s", "1012837", "1012837");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/discovery/getInfo?user_id=%s&login_uuid=%s&sign=%s", "1012837", "1012837");
		//upAES.createSign("http://test.upok.com.cn:8017/f/discovery/getInfo?user_id=%s&login_uuid=%s&sign=%s", "1037961", "1037961");
		 //upAES.createSign("http://test.upok.com.cn:8017/service_v223.php/watermark/getpackagelistinfo?user_id=%s&login_uuid=%s&sign=%s&v=2", "1037961", "1037961");
		
		//upAES.createSign("http://s2.upok.com.cn/service.php/spot/videoparsing?user_id=%s&video_url=%s&sign=%s", "1012837", "http://v.youku.com/v_show/id_XMTQwMDIwNjQ2OA==_ev_1.html");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/discovery/getInfo?user_id=%s&login_uuid=%s&sign=%s", "1012837", "1037961");
		//upAES.createSign("http://s2.upok.com.cn/f/watermark/getActivityImagesByWid?wid=%s&type=%s&sign=%s", "1169", "1");
		//upAES.createSign("http://s2.upok.com.cn/f/activity/getUserActivityList?user_id=%s&spot_news_minid=%s&sign=%s", "1025265", "0");
		
		//upAES.createSign("https://test.upok.com.cn:444/f/user/getMessages?user_id=%s&login_uuid=%s&sign=%s", "1007174", "0");
		
		//upAES.createSign("https://test.upok.com.cn:444/f/user/getMessagesInfo?user_id=%s&login_uuid=%s&sign=%s", "1007174", "0");
		//upAES.createSign("https://test.upok.com.cn:444/u/pay/getTopIncomeUsers?user_id=%s&login_uuid=%s&sign=%s", "1007174", "0");
		//upAES.createSign("https://test.upok.com.cn:444/u/pay/getTopConsumeUsers?user_id=%s&login_uuid=%s&sign=%s", "1007174", "0");
		
		//upAES.createSign("http://s2.upok.com.cn/f/user/getMessages?user_id=%s&login_uuid=%s&sign=%s", "1025265", "1");
		
		//upAES.createSign("https://test.upok.com.cn:444/u/pay/getDealRecorders?user_id=%s&login_uuid=%s&sign=%s", "100030", "1");
		
		//upAES.createSign("https://s2.upok.com.cn/f/activity/getActivityList?user_id=%s&spot_news_minid=%s&sign=%s&type_id=-3&v=2", "100009", "0");
		
		//upAES.createSign("https://test.upok.com.cn:444/u/activity/praiseAndSendReview?user_id=%s&login_uuid=%s&sign=%s", "100030", "1");
		
		//upAES.createSign("https://s2.upok.com.cn/u/pay/upateIncomeConsume?user_id=%s&login_uuid=%s&sign=%s", "100007", "1");	
		
		//upAES.createSign("https://s2.upok.com.cn/u/pay/upateIncomeConsume?user_id=%s&login_uuid=%s&sign=%s", "1132240", "1");
		
		//upAES.createSign("https://s2.upok.com.cn/u/pay/getActivityDealRecorders?user_id=%s&target_id=%s&sign=%s", "1153578", "373285");
		
		//upAES.createSign("https://test.upok.com.cn:444/f/topic/getTopicComments?user_id=%s&target_id=%s&sign=%s", "1145738", "783");
		
		//upAES.createSign("https://test.upok.com.cn:444/u/pay/getDealRecorders?user_id=%s&login_uuid=%s&sign=%s", "1145738", "0");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/topic/viewTopicCategory?category_id=%s&user_id=%s&sign=%s", "2", "1");

		//upAES.createSign("http://test.upok.com.cn:8017/f/topic/getTopicCategories?user_id=%s&login_uuid=%s&sign=%s", "1145738", "0");
		
		//upAES.createSign("http://test.upok.com.cn:8017/service_v240.php/watermark/getpackagelistinfo?user_id=%s&login_uuid=%s&sign=%s", "2", "1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/f/system/getMusicInfos?user_id=%s&login_uuid=%s&sign=%s", "2", "1");
		
		//upAES.createSign("http://s2.upok.com.cn/f/topic/getTopicCategories?user_id=%s&login_uuid=%s&sign=%s", "1153578", "373285");
		
		//upAES.createSign("http://s2.upok.com.cn/u/pay/getTopIncomeUsers?user_id=%s&login_uuid=%s&sign=%s", "1153578", "373285");
		
		//upAES.createSign("https://test.upok.com.cn:444/u/pay/testLock?user_id=%s&fee=%s&sign=%s", "1025329", "1");
		
		//upAES.createSign("http://test.upok.com.cn:8017/u/pay/requestOrder?user_id=%s&fee=%s&sign=%s&category=101&subCategory=0&targetId=283709&password=/Hkzl0zkzcSALn2ZEUUESA==", "1025329", "1");
		//upAES.createSign("http://s2.upok.com.cn/f/user/getArountUsers?user_id=%s&page_no=%s&sign=%s", "1012837", "1");
		
		
		//upAES.createSign("https://test.upok.com.cn:444/service.php/user/userLogin?login_name=%s&password=%s&sign=%s", "+8613681391572", "111111");
		
		//"user_token":"228fdfd97aea94ddd8db31d6d118488b"
		//upAES.createSign("https://test.upok.com.cn:444/member/f/user/getMessagesJson?user_id=%s&login_uuid=%s&sign=%s", "1012837", "0");
		
		//upAES.createSign("https://s2.upok.com.cn/member/f/activity/getActivityListJson?user_id=%s&spot_news_minid=%s&sign=%s", "1145841", "0");
		
		//upAES.createSign("https://test.upok.com.cn:444/osv/f/app/getGlobalParams?timestamp=%s&app_version=%s&sign=%s", "1145868", "0");
		
		//upAES.createSign("http://s2.upok.com.cn/member/service.php/spot/videoparsingJson?user_id=%s&video_url=%s&sign=%s", "1153578", "http://www.iqiyi.com/v_19rrmbr34s.html");

		//upAES.createSign("http://s2.upok.com.cn/f/discovery/getInfo?user_id=%s&login_uuid=%s&sign=%s", "1157092", "1");
		
//		upAES.createSign("https://test.upok.com.cn:444/member/f/activity/listMagazineInfosJson?user_id=%s&max_m_id=%s&sign=%s", "1145868", "0");
		
		
		//member/f/activity/getActivityListJson
	}		
	
	public void test2(String value) throws Exception 
	{
		String encryptedMessage = encryp(value);
		System.out.println("encryptedMessage:["+encryptedMessage+"]");
		String decryptedMessage = decryp(encryptedMessage);
		System.out.println("decryptedMessage:["+decryptedMessage+"]");
		
		
		String dateString = "Aug 30th 2015 at 11:00AM";
		//String dateString = "Aug 31st 2015 10:30AM";
		dateString = dateString.replace("th", "");
		dateString = dateString.replace("at ", "");
		
		DateFormat df = new SimpleDateFormat("MMM dd yyyy hh:mmaaa", Locale.ENGLISH);
		Date date = df.parse(dateString);  
        
        System.out.println("DateFormat.MEDIUM: " + date.toString());
        
        for (int i = 0; i < 5; i++) {
			System.out.println("pow:["+(int)Math.pow(2,i)+"]");
		}
        
	}
	
	public void createSign(String format , String mp, String ap)
	{
		String mped = encryp(mp);
		System.out.println("mp:["+mped+"]");
		String aped = encryp(ap);
		System.out.println("ap:["+aped+"]");
		
		byte[] osignBytes;
		String osign = "";
		try {
			osignBytes = (mp + ap).getBytes("UTF-8");
			
			osign = getMD5(osignBytes);
			osign = osign.substring(5, osign.length()-5);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}       
		System.out.println("sign:["+osign+"]");
		try {
			String url = String.format(format, URLEncoder.encode(mped,"UTF8"), URLEncoder.encode(aped,"UTF8"), URLEncoder.encode(osign,"UTF8"));
			System.out.println(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String entry(String value){
		UpAES upAES = new UpAES();
		upAES.init();
		return upAES.encryp(value);
	}
	public String disEntry(String value){
		UpAES upAES = new UpAES();
		upAES.init();
		return upAES.decryp(value);
	}

}
