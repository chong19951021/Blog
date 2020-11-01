package com.lichong.service;

import com.github.pagehelper.PageInfo;
import com.lichong.entity.Blog;
import com.lichong.queryvo.*;

import java.util.List;

public interface BlogService {
    Blog getBlogById(Long id);
  Blog  getBlogByIdShow(Long id);

    List<BlogQuery> getAllBlog();
    PageInfo getBlogsByTypeId(int pageNum,Long id);

    void saveMidTable(String[] tagIds,Blog blog);

    int saveBlog(Blog blog,String type);
    PageInfo getBlogByTagId(int pageNum,Long id);
    List<Blog> getBlogByTagId1(Long id);


    int updateBlog(Blog showBlog);

    int deleteBlog(Long id);

   PageInfo getBlogBySearch(SearchBlog searchBlog,int pageNum);

    PageInfo<FirstPageBlog> getAllFirstPageBlog(int i ,String query);

    PageInfo getRecommendedBlog(int i);

//    List<FirstPageBlog> getNewBlog();

    List<FirstPageBlog> getSearchBlog(String query);

    DetailedBlog getDetailedBlog(Long id);

    //根据TypeId获取博客列表，在分类页进行的操作
    List<FirstPageBlog> getByTypeId(Long typeId);

    Integer getBlogTotal();

    Integer getBlogViewTotal();

    Integer getBlogCommentTotal();

    Integer getBlogMessageTotal();
}
