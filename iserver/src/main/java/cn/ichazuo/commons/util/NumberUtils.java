package cn.ichazuo.commons.util;

import java.math.BigDecimal;

/**
 * ClassName: NumberUtils 
 * Description: (数字帮助类) 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:21:06 
 * @version V1.0
 */
public final class NumberUtils {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 2;
	
	/**
	 * Title: isNullOrZero 
	 * Description: (判断数字是否为null或者0,若不是数字类型 返回true) 
	 * @param number
	 * @return
	 * @author ZhaoXu
	 */
	public static boolean isNullOrZero(Number number){
		if(number == null){
			return true;
		}
		Class<?> cls = number.getClass();
		if(cls == Long.class || cls == long.class){
			if(number.longValue() == 0){
				return true;
			}
			return false;
		}
		if(cls == Double.class || cls == double.class){
			if(number.doubleValue() == 0){
				return true;
			}
			return false;
		}
		if(cls == Float.class || cls == float.class){
			if(number.floatValue() == 0){
				return true;
			}
			return false;
		}
		if(cls == Integer.class || cls == int.class){
			if(number.intValue() == 0){
				return true;
			}
			return false;
		}
		if(cls == Byte.class || cls == byte.class){
			if(number.byteValue() == 0){
				return true;
			}
			return false;
		}
		if(cls == short.class || cls == Short.class){
			if(number.shortValue() == 0){
				return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * Title: add 
	 * Description: (精确加法运算) 
	 * @param v1 被加数
	 * @param v2 加数
	 * @return
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 
	 * Title: sub 
	 * Description: (精确减法运算) 
	 * @param v1 被减数
	 * @param v2 减数
	 * @return
	 */
	public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
	
	/**
	 * 
	 * Title: mul 
	 * Description: (精确乘法运算) 
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return
	 */
	public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
	
	/**
	 * 
	 * Title: div 
	 * Description: (精确除法运算,除不尽时scale为指定精度,然后四舍五入) 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 精度,需要精确到小数点以后几位
	 * @return
	 */
	public static double div(double v1,double v2,int scale){
        if(scale < 0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	
	/**
	 * 
	 * Title: div 
	 * Description: (精确除法运算,默认精度) 
	 * @param v1 被除数
	 * @param v2 除数
	 * @return
	 */
	public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }
	
	/**
	 * 
	 * Title: round 
	 * Description: (精确四舍五入操作) 
	 * @param number 数字
	 * @param scale 精度,小数点后保留几位
	 * @return
	 */
	public static double round(double number,int scale){
        if(scale < 0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(number));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	
	private NumberUtils(){
		//..
	}
}
