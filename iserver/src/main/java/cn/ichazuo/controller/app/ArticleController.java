package cn.ichazuo.controller.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ichazuo.commons.base.BaseController;
import cn.ichazuo.commons.util.DateUtils;
import cn.ichazuo.commons.util.NumberUtils;
import cn.ichazuo.commons.util.StringUtils;
import cn.ichazuo.commons.util.model.Params;
import cn.ichazuo.model.app.ArticleCommentInfo;
import cn.ichazuo.model.app.ArticleInfo;
import cn.ichazuo.model.app.ArticleListInfo;
import cn.ichazuo.model.log.ArticleClickLog;
import cn.ichazuo.model.table.ArticleComment;
import cn.ichazuo.model.table.ArticleCommentFavour;
import cn.ichazuo.service.ArticleService;
import cn.ichazuo.service.CommonService;
import cn.ichazuo.service.LogService;

/**
 * @ClassName: ArticleController 
 * @Description: (文章controller) 
 * @author ZhaoXu
 * @date 2015年8月5日 上午9:56:30 
 * @version V1.0
 */
@RequestMapping("/app")
@Controller("app.articleController")
public class ArticleController extends BaseController{
	@Autowired
	private CommonService commonService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private LogService logService;
	
	/**
	 * @Title: findAricleList 
	 * @Description: (查询文章列表) 
	 * @param type
	 * @param page
	 * @return
	 */
	@ResponseBody//把返回数据转换为JSON格式
	@RequestMapping("/findAricleList")//接口名称
	public JSONObject findAricleList(Long type,Integer page){
		try{
			Params params = new Params(page);
			if(!NumberUtils.isNullOrZero(type)){
				params.putData("type", type);
			}
			List<ArticleListInfo> list = articleService.findArticleList(params);
			list.forEach(info -> {
				info.setCover(commonService.appenUrl(info.getCover()));
			});
			
			Long count = articleService.findArticleListCount(params);
			return ok("查询成功",list,page,count);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findArticleInfo 
	 * @Description: (findArticleInfo) 
	 * @param articleId 文章ID
	 * @param userId 用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findArticleInfo")
	public JSONObject findArticleInfo(Long articleId,Long userId,HttpServletRequest request){
		try{
			if(NumberUtils.isNullOrZero(articleId)){
				return error("参数缺失");
			}
			if(NumberUtils.isNullOrZero(userId)){
				userId = 0L;
			}
			
			ArticleInfo info = articleService.findArticleInfo(articleId);
			if(info == null){
				return error("参数错误");
			}
			
			info.setNumber(articleService.findArticleCommentCount(articleId));
			String url = info.getUrl();
			if(!StringUtils.isNullOrEmpty(url) && !url.startsWith("http://")){
				url = "http://" + url;
			}
			String css = "<link rel='stylesheet' type='text/css' href='http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/appmsg/page_mp_article_improve.css'>";
			css += "<link rel='stylesheet' href='https://res.wx.qq.com/mpres/htmledition/ueditor/themes/iframe.css' />";
			info.setCss(css);
			
			//保存点击日志
			ArticleClickLog log = new ArticleClickLog();
			log.setArticleId(articleId);
			log.setCreateAt(DateUtils.getNowDate());
			log.setIpAddress(request.getRemoteAddr());
			log.setMemberId(userId);
			logService.saveArticleClickLog(log);
			
			return ok("查询成功",info);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: findArticleComment 
	 * @Description: (查询文章评论) 
	 * @param articleId
	 * @param memberId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findArticleComment")
	public JSONObject findArticleComment(Long articleId,Long memberId){
		try{
			if(NumberUtils.isNullOrZero(articleId) || memberId == null){
				return error("参数缺失");
			}
			List<ArticleCommentInfo> commentList = articleService.findArticleComment(articleId);
			commentList.forEach(comment -> {
				comment.setAvatar(commonService.appenUrl(comment.getAvatar()));
				comment.setFavourCount(articleService.findArticleCommentFavourCount(comment.getId()));
				
				if(NumberUtils.isNullOrZero(memberId)){
					comment.setIsFavour(0);
				}else{
					Params params = new Params();
					params.putData("commentId", comment.getId());
					params.putData("memberId", memberId);
					
					ArticleCommentFavour favour = articleService.findArticleCommentFavour(params);
					comment.setIsFavour(favour == null || favour.getStatus() == 0 ? 0 : 1);
				}
			});
			return ok("查询成功",commentList);
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: saveArticleComment 
	 * @Description: (保存文章评论) 
	 * @param articleId
	 * @param memberId
	 * @param conetent
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveArticleComment")
	public JSONObject saveArticleComment(Long articleId,Long memberId,String content,Double star){
		try{
			if(NumberUtils.isNullOrZero(articleId) || NumberUtils.isNullOrZero(memberId)){
				return error("参数缺失");
			}
			if(StringUtils.isNullOrEmpty(content)){
				content = "";
			}
			if(NumberUtils.isNullOrZero(star)){
				star = 10d;
			}
			ArticleComment comment = new ArticleComment();
			comment.setArticleId(articleId);
			comment.setContent(content);
			comment.setMemberId(memberId);
			comment.setStar(star);
			if(articleService.saveArticleComment(comment)){
				return ok("保存成功");
			}
			return error("保存失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
	
	/**
	 * @Title: updateArticleCommentFavour 
	 * @Description: (文章评论点赞) 
	 * @param commentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateArticleCommentFavour")
	public JSONObject updateArticleCommentFavour(Long commentId,Long memberId){
		try{
			if(NumberUtils.isNullOrZero(commentId) || NumberUtils.isNullOrZero(memberId)){
				return error("参数缺失");
			}
			Params params = new Params();
			params.putData("commentId", commentId);
			params.putData("memberId", memberId);
			ArticleCommentFavour favour = articleService.findArticleCommentFavour(params);
			if(favour == null){
				favour = new ArticleCommentFavour();
				favour.setCommentId(commentId);
				favour.setMemberId(memberId);
				if(articleService.saveArticleCommentFavour(favour)){
					return ok("点赞成功");
				}
			}else{
				favour.setStatus(favour.getStatus() == 0 ? 1 : 0);
				if(articleService.updateAticleCommentFavour(favour)){
					return ok("点赞成功");
				}
			}
			return error("点赞失败");
		}catch(Exception e){
			e.printStackTrace();
			return error(APP_SYSTEM_ERROR);
		}
	}
}
