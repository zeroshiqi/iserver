package cn.ichazuo.commons.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * ClassName: PasswdEncryption 
 * Description: (加密类) 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:21:35 
 * @version V1.0
 */
public class PasswdEncryption {
	
	/**
	 * 
	 * Title: generate 
	 * Description: (生成含有随机盐的密码) 
	 * @param password 未加密的数据
	 * @return
	 */
	public static String generate(String password) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sb.append("0");
			}
		}
		String salt = sb.toString();
		password = MD5(password + salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return new String(cs);
	}

	/**
	 * 
	 * Title: verify 
	 * Description: (校验密码是否正确) 
	 * @param password 未加密的数据
	 * @param md5Password 加密后的数据
	 * @return
	 */
	public static boolean verify(String password, String md5Password) {
		try{
			char[] cs1 = new char[32];
			char[] cs2 = new char[16];
			for (int i = 0; i < 48; i += 3) {
				cs1[i / 3 * 2] = md5Password.charAt(i);
				cs1[i / 3 * 2 + 1] = md5Password.charAt(i + 2);
				cs2[i / 3] = md5Password.charAt(i + 1);
			}
			String salt = new String(cs2);
			return MD5(password + salt).equals(new String(cs1));
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Title: md5Hex 
	 * Description: (MD5加密) 
	 * @param str 待加密的字符串 
	 * @return
	 */
	public static String MD5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(str.getBytes("utf-8"));
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return "";
		}
	}
	/**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
           // resultString = byteArrayToHexString(md.digest(resultString.getBytes()));//原文件内容，可能原因是：win2003时系统缺省编码为GBK，win7为utf-8
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));//正确的写法
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }
    
    /** 
     * 加密 
     *  
     * @param content 需要加密的内容 
     * @param password  加密密码 
     * @return 
     */  
    public static byte[] encrypt(String content, String password) {  
            try {             
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                    kgen.init(128, new SecureRandom(password.getBytes()));  
                    SecretKey secretKey = kgen.generateKey();  
                    byte[] enCodeFormat = secretKey.getEncoded();  
                    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                    Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                    byte[] byteContent = content.getBytes("utf-8");  
                    cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
                    byte[] result = cipher.doFinal(byteContent);  
                    return result; // 加密   
            } catch (NoSuchAlgorithmException e) {  
                    e.printStackTrace();  
            } catch (NoSuchPaddingException e) {  
                    e.printStackTrace();  
            } catch (InvalidKeyException e) {  
                    e.printStackTrace();  
            } catch (UnsupportedEncodingException e) {  
                    e.printStackTrace();  
            } catch (IllegalBlockSizeException e) {  
                    e.printStackTrace();  
            } catch (BadPaddingException e) {  
                    e.printStackTrace();  
            }  
            return null;  
    }  
     
    
    /**解密 
     * @param content  待解密内容 
     * @param password 解密密钥 
     * @return 
     */  
    public static byte[] decrypt(byte[] content, String password) {  
            try {  
                     KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                     kgen.init(128, new SecureRandom(password.getBytes()));  
                     SecretKey secretKey = kgen.generateKey();  
                     byte[] enCodeFormat = secretKey.getEncoded();  
                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
                     Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                    cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
                    byte[] result = cipher.doFinal(content);  
                    return result; // 加密   
            } catch (NoSuchAlgorithmException e) {  
                    e.printStackTrace();  
            } catch (NoSuchPaddingException e) {  
                    e.printStackTrace();  
            } catch (InvalidKeyException e) {  
                    e.printStackTrace();  
            } catch (IllegalBlockSizeException e) {  
                    e.printStackTrace();  
            } catch (BadPaddingException e) {  
                    e.printStackTrace();  
            }  
            return null;  
    }  
    

		/**将二进制转换成16进制 
		 * @param buf 
		 * @return 
		 */  
		public static String parseByte2HexStr(byte buf[]) {  
		        StringBuffer sb = new StringBuffer();  
		        for (int i = 0; i < buf.length; i++) {  
		                String hex = Integer.toHexString(buf[i] & 0xFF);  
		                if (hex.length() == 1) {  
		                        hex = '0' + hex;  
		                }  
		                sb.append(hex.toUpperCase());  
		        }  
		        return sb.toString();  
		}
		/**将16进制转换为二进制 
		 * @param hexStr 
		 * @return 
		 */  
		public static byte[] parseHexStr2Byte(String hexStr) {  
		        if (hexStr.length() < 1)  
		                return null;  
		        byte[] result = new byte[hexStr.length()/2];  
		        for (int i = 0;i< hexStr.length()/2; i++) {  
		                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
		                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
		                result[i] = (byte) (high * 16 + low);  
		        }  
		        return result;  
		} 
		//生成随机数字和字母,  
	    public static String getStringRandom() {  
	        String val = "";  
	        Random random = new Random();  
	          
	        //参数length，表示生成几位随机数  
	        for(int i = 0; i < 8; i++) {  
	              
	            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
	            //输出字母还是数字  
	            if( "char".equalsIgnoreCase(charOrNum) ) {  
	                //输出是大写字母还是小写字母  
	                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
	                val += (char)(random.nextInt(26) + temp);  
	            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
	                val += String.valueOf(random.nextInt(10));  
	            }  
	        }  
	        return val;  
	    }
	    
	    /** 
	      * 加密 
	      * 
	      * @param content 需要加密的内容 
	      * @param password  加密密码 
	      * @return 
	      */  
	     public static byte[] encrypt2(String content, String password) {  
	             try {  
	                     SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");  
	                     Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");  
	                     byte[] byteContent = content.getBytes("utf-8");  
	                     cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
	                     byte[] result = cipher.doFinal(byteContent);  
	                     return result; // 加密  
	             } catch (NoSuchAlgorithmException e) {  
	                     e.printStackTrace();  
	             } catch (NoSuchPaddingException e) {  
	                     e.printStackTrace();  
	             } catch (InvalidKeyException e) {  
	                     e.printStackTrace();  
	             } catch (UnsupportedEncodingException e) {  
	                     e.printStackTrace();  
	             } catch (IllegalBlockSizeException e) {  
	                     e.printStackTrace();  
	             } catch (BadPaddingException e) {  
	                     e.printStackTrace();  
	             }  
	             return null;  
	     }  

}
