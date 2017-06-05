package cn.ichazuo.commons.base;

import java.io.Serializable;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: BaseService 
 * @Description: (基本Service) 
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:22:25 
 * @version V1.0
 */
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
public abstract class BaseService extends Base implements Serializable{
	private static final long serialVersionUID = 1L;
	
}
