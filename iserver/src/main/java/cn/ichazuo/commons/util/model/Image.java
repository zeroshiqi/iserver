package cn.ichazuo.commons.util.model;

import java.io.Serializable;

/**
 * @ClassName: Image 
 * @Description: (图片类model) 
 * @author ZhaoXu
 * @date 2015年7月27日 下午3:31:56 
 * @version V1.0
 */
public class Image implements Serializable{
	private static final long serialVersionUID = 1L;
	// ===剪切点x坐标
	private int x;
	private int y;
	// ===剪切点宽度
	private int width;
	private int height;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
