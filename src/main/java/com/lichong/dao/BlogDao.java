package com.lichong.dao;

import com.lichong.entity.Blog;
import com.lichong.queryvo.*;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface BlogDao {

    Blog getBlogById(Long id);
    Boolean publishIf(Long id);

    List<Blog> getAllBlog();

    List<BlogQuery> getAllBlogQuery();

    int saveBlog(Blog blog);

    int updateBlog(Blog blog);

    int deleteBlog(Long id);

    List<BlogQuery> searchByTitleOrTypeOrRecommend(SearchBlog searchBlog);

    List<FirstPageBlog> getFirstPageBlog(String query);

    List<Blog> getAllRecommendBlog();
    List<Blog> getBlogsByTypeId(Long id);
    void saveMidTable(String[] str,Blog blog);

    List<Blog> getBlogByTagId(Long id);
    List<Blog> getBlogByTagId1(Long id);

    List<FirstPageBlog> getSearchBlog(String query);

    DetailedBlog getDetailedBlog(Long id);

    int updateViews(Long id);

    //    根据博客id查询出评论数量
    int getCommentCountById(Long id);



}