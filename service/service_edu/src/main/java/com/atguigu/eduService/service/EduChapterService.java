package com.atguigu.eduService.service;

import com.atguigu.eduService.entity.EduChapter;
import com.atguigu.eduService.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-16
 */
public interface EduChapterService extends IService<EduChapter> {

    //返回课程大纲列表，根据课程id查询
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节
    Boolean deleteChapter(String chapterId);

    //根据课程id删除章节
    void removeChapterByCourseId(String courseId);
}
