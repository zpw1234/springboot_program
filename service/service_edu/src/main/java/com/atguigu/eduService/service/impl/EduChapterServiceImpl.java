package com.atguigu.eduService.service.impl;

import com.atguigu.eduService.entity.EduChapter;
import com.atguigu.eduService.entity.EduVideo;
import com.atguigu.eduService.entity.chapter.ChapterVo;
import com.atguigu.eduService.entity.chapter.VideoVo;
import com.atguigu.eduService.mapper.EduChapterMapper;
import com.atguigu.eduService.service.EduChapterService;
import com.atguigu.eduService.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-16
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService videoService;

    //返回课程大纲列表，根据课程id查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1.根据课程id查询出所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
        //2.查询小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);
        //创建list集合用于封装最后的数据
        List<ChapterVo> finalList = new ArrayList<>();
        //3.遍历查询出来的章节
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //把chapterVo放入最终集合
            finalList.add(chapterVo);
            //创建集合，用于封装章节中的小节
            List<VideoVo> videoList = new ArrayList<>();

            //4.遍历查询出来的小节
            for (int m = 0; m < eduVideoList.size(); m++) {
                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(m);
                //判断，小节的chapterId,和章节的id是否一样
                if (eduVideo.getChapterId().equals(eduChapter.getId())){
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }
            }

            //把封装之后的小节放入对象中
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }

    //删除章节
    @Override
    public Boolean deleteChapter(String chapterId) {
        //根据chapterId章节id查询小节表，如果查询到数据，不进行删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        //判断
        if(count > 0){
            //查询出小节
            throw new GuliException(20001,"不能删除");
        }else {
            //删除章节
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }
    }

    //根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(null);
    }
}
