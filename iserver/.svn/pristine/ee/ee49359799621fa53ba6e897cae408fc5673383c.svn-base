package cn.ichazuo.dao;

import java.util.List;
import java.util.Map;

import cn.ichazuo.model.app.ArticleCommentInfo;
import cn.ichazuo.model.app.ArticleInfo;
import cn.ichazuo.model.app.ArticleListInfo;
import cn.ichazuo.model.table.Article;
import cn.ichazuo.model.table.ArticleComment;
import cn.ichazuo.model.table.ArticleCommentFavour;

/**
 * @ClassName: ArticleDao 
 * @Description: (文章DAO) 
 * @author ZhaoXu
 * @date 2015年8月5日 上午9:56:53 
 * @version V1.0
 */
public interface ArticleDao {
	/**
	 * @Title: findArticleList 
	 * @Description: (查询文章列表) 
	 * @param map
	 * @return
	 */
	public List<ArticleListInfo> findArticleList(Map<String,Object> map);
	
	/**
	 * @Title: findArticleListCount 
	 * @Description: (查询文章列表总数) 
	 * @param map
	 * @return
	 */
	public Long findArticleListCount(Map<String,Object> map);
	
	/**
	 * @Title: findArticleInfo 
	 * @Description: (查询文章信息) 
	 * @param id
	 * @return
	 */
	public ArticleInfo findArticleInfo(Long id);
	
	/**
	 * @Title: findArticleCommentInfo 
	 * @Description: (查询评论) 
	 * @param articleId
	 * @return
	 */
	public List<ArticleCommentInfo> findArticleCommentInfo(Long articleId);
	
	/**
	 * @Title: findArticleCommentFavourCount 
	 * @Description: (查询评论点赞数量) 
	 * @param id
	 * @return
	 */
	public Long findArticleCommentFavourCount(Long id);
	
	/**
	 * @Title: findArticleCommentFavour 
	 * @Description: (查询评论点赞) 
	 * @param map
	 * @return
	 */
	public ArticleCommentFavour findArticleCommentFavour(Map<String,Object> map);
	
	/**
	 * @Title: saveArticleComment 
	 * @Description: (保存文章评论) 
	 * @param comment
	 * @return
	 */
	public int saveArticleComment(ArticleComment comment);
	
	/**
	 * @Title: saveArticleCommentFavour 
	 * @Description: (保存课程评论点赞) 
	 * @param favour
	 * @return
	 */
	public int saveArticleCommentFavour(ArticleCommentFavour favour);
	
	/**
	 * @Title: updateArticleCommentFavour 
	 * @Description: (修改课程评论点赞) 
	 * @param favour
	 * @return
	 */
	public int updateArticleCommentFavour(ArticleCommentFavour favour);
	
	/**
	 * @Title: updateArticleClickNum 
	 * @Description: (修改课程点击数) 
	 * @param id
	 * @return
	 */
	public int updateArticleClickNum(Long id);
	
	/**
	 * @Title: findMemberArticleCommnetCount 
	 * @Description: (查询当天评论数量) 
	 * @param map
	 * @return
	 */
	public Long findMemberArticleCommnetCount(Map<String,Object> map);
	
	/**
	 * @Title: findArticleStar 
	 * @Description: (查询文章评分) 
	 * @param id
	 * @return
	 */
	public Article findArticleStar(Long id);
	
	/**
	 * @Title: updateArticleStar 
	 * @Description: (修改文章评分) 
	 * @param article
	 * @return
	 */
	public int updateArticleStar(Article article);
	
	/**
	 * @Title: findArticleCommentCount 
	 * @Description: (查询文章评论人数) 
	 * @param id
	 * @return
	 */
	public Long findArticleCommentCount(Long id);
}
