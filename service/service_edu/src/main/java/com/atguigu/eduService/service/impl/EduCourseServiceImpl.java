package com.atguigu.eduService.service.impl;

import com.atguigu.eduService.entity.EduCourse;
import com.atguigu.eduService.entity.EduCourseDescription;
import com.atguigu.eduService.entity.EduTeacher;
import com.atguigu.eduService.entity.frontvo.CourseFrontVo;
import com.atguigu.eduService.entity.frontvo.CourseWebVo;
import com.atguigu.eduService.entity.vo.CourseInfoVo;
import com.atguigu.eduService.entity.vo.CoursePublishVo;
import com.atguigu.eduService.mapper.EduCourseMapper;
import com.atguigu.eduService.service.EduChapterService;
import com.atguigu.eduService.service.EduCourseDescriptionService;
import com.atguigu.eduService.service.EduCourseService;
import com.atguigu.eduService.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-16
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //添加课程基本信息
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表中添加课程基本信息
        //把CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert <= 0){
            throw new GuliException(20001,"添加课程信息失败");
        }

        //获取添加之后课程id
        String cid = eduCourse.getId();

        //2.向课程简介添加课程简介
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id
        eduCourseDescription.setId(cid);
        eduCourseDescriptionService.save(eduCourseDescription);

        return cid;
    }

    //根据课程查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //2.查询课程描述表
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new GuliException(20001,"修改课程信息失败");
        }

        //修改描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoVo.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(courseDescription);
    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);

        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1.根据课程id，删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //2.删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //3.删除描述
        eduCourseDescriptionService.removeById(courseId);
        //4.删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result == 0){
            throw new GuliException(20001,"删除课程失败");
        }
    }

    //条件查询带分页
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
        //课程id查询所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){ //一级分类
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())){ //二级分类
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){ //关注度
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())){ //价格
            wrapper.orderByDesc("price");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){ //最新
            wrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(pageParam,wrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        //把分页数据获取出来，放到map集合
        Map<String,Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    //根据课程id，编写sql语句查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
