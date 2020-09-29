package com.atguigu.eduService.service;

import com.atguigu.eduService.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-16
 */
public interface EduVideoService extends IService<EduVideo> {

    //根据课程id删除小节
    void removeVideoByCourseId(String courseId);
}
