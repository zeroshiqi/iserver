package cn.ichazuo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ichazuo.commons.base.BaseService;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.dao.ArticleDao;
import cn.ichazuo.model.app.ArticleCommentInfo;
import cn.ichazuo.model.app.ArticleInfo;
import cn.ichazuo.model.app.ArticleListInfo;
import cn.ichazuo.model.table.Article;
import cn.ichazuo.model.table.ArticleComment;
import cn.ichazuo.model.table.ArticleCommentFavour;

/**
 * @ClassName: ArticleService 
 * @Description: (文章Service) 
 * @author ZhaoXu
 * @date 2015年8月5日 上午9:57:34 
 * @version V1.0
 */
@Service("articleService")
public class ArticleService extends BaseService{
	private static final long serialVersionUID = 1L;
	@Resource
	private ArticleDao articleDao;
	
	/**
	 * @Title: findArticleList 
	 * @Description: (查询文章列表) 
	 * @param params
	 * @return
	 */
	public List<ArticleListInfo> findArticleList(Params params){
		List<ArticleListInfo> list = articleDao.findArticleList(params.getMap());
		return list;
	}
	
	/**
	 * @Title: findArticleListCount 
	 * @Description: (查询文章列表总数) 
	 * @param params
	 * @return
	 */
	public Long findArticleListCount(Params params){
		return articleDao.findArticleListCount(params.getMap());
	}
	
	/**
	 * @Title: findArticleInfo 
	 * @Description: (查询文章信息) 
	 * @param id
	 * @return
	 */
	public ArticleInfo findArticleInfo(Long id){
		//修改点击数
		articleDao.updateArticleClickNum(id);
		return articleDao.findArticleInfo(id);
	}
	
	/**
	 * @Title: findArticleCommentCount 
	 * @Description: (查询文章评论人数) 
	 * @param id
	 * @return
	 */
	public Long findArticleCommentCount(Long id){
		return articleDao.findArticleCommentCount(id);
	}
	
	/**
	 * @Title: findArticleComment 
	 * @Description: (查询文章评论) 
	 * @param id
	 * @return
	 */
	public List<ArticleCommentInfo> findArticleComment(Long id){
		return articleDao.findArticleCommentInfo(id);
	}
	
	/**
	 * @Title: findArticleCommentFavourCount 
	 * @Description: (查询课程评论点赞数) 
	 * @param commentId
	 * @return
	 */
	public Long findArticleCommentFavourCount(Long commentId){
		return articleDao.findArticleCommentFavourCount(commentId);
	}
	
	/**
	 * @Title: findArticleCommentFavour 
	 * @Description: (查询文章评论点赞) 
	 * @param params
	 * @return
	 */
	public ArticleCommentFavour findArticleCommentFavour(Params params){
		return articleDao.findArticleCommentFavour(params.getMap());
	}
	
	/**
	 * @Title: saveArticleComment 
	 * @Description: (保存文章评论) 
	 * @param comment
	 * @return
	 */
	public boolean saveArticleComment(ArticleComment comment){
		Params params = new Params();
		params.putData("articleId", comment.getArticleId());
		params.putData("memberId", comment.getMemberId());
		if(articleDao.findMemberArticleCommnetCount(params.getMap()) == 0){
			Article article = articleDao.findArticleStar(comment.getArticleId());
			article.setStar(article.getStar() == 0 ? comment.getStar() : NumberUtils.div(NumberUtils.add(article.getStar(), comment.getStar()), 2));
			articleDao.updateArticleStar(article);
		}
		return articleDao.saveArticleComment(comment) > 0;
	}
	
	/**
	 * @Title: saveArticleCommentFavour 
	 * @Description: (保存文章评论点赞) 
	 * @param favour
	 * @return
	 */
	public boolean saveArticleCommentFavour(ArticleCommentFavour favour){
		return articleDao.saveArticleCommentFavour(favour) > 0;
	}
	
	/**
	 * @Title: updateAticleCommentFavour 
	 * @Description: (更新文章评论点赞) 
	 * @param favour
	 * @return
	 */
	public boolean updateAticleCommentFavour(ArticleCommentFavour favour){
		return articleDao.updateArticleCommentFavour(favour) > 0;
	}
}
