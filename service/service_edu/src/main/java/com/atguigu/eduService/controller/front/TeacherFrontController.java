package com.atguigu.eduService.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduService.entity.EduCourse;
import com.atguigu.eduService.entity.EduTeacher;
import com.atguigu.eduService.service.EduCourseService;
import com.atguigu.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //1 分页查询讲师的方法
    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable long page,@PathVariable long limit){
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(pageTeacher);

        //返回分页中的所有数据
        return R.ok().data(map);
    }

    //查询讲师详情功能
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        //根据讲师id查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        //根据讲师id查询所讲的课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);

        return R.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }
}
