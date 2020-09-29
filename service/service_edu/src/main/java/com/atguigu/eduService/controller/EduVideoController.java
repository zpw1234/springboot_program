package com.atguigu.eduService.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduService.client.VodClient;
import com.atguigu.eduService.entity.EduChapter;
import com.atguigu.eduService.entity.EduVideo;
import com.atguigu.eduService.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-16
 */
@RestController
@RequestMapping("/eduService/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }
    //删除小节
    //TODO 需要完善 删除小节把里面的视频也要删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id获取视频id
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //根据视频id删除
        //判断小节是否有视频
        if (!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeAliyunVideo(videoSourceId);
        }
        videoService.removeById(id);
        return R.ok();
    }
    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }

    //根据章节id查询
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId){
        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("eduVideo",eduVideo);
    }
}

