package org.openokr.application.web;

import com.zzheng.framework.adapter.vo.ResponseResult;
import org.openokr.index.service.IIndexService;
import org.openokr.index.vo.ExecutionVO;
import org.openokr.manage.service.IOkrMessageService;
import org.openokr.manage.service.IOkrObjectService;
import org.openokr.manage.service.IOkrTimeSessionsService;
import org.openokr.manage.vo.MessagesVO;
import org.openokr.manage.vo.ObjectivesExtVO;
import org.openokr.manage.vo.ObjectivesVO;
import org.openokr.manage.vo.OkrObjectSearchVO;
import org.openokr.manage.vo.TimeSessionsExtVO;
import org.openokr.utils.StringUtils;
import org.openokr.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhengzheng on 2018/12/19.
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private IIndexService indexService;

    @Autowired
    private IOkrObjectService okrObjectService;

    @Autowired
    private IOkrMessageService okrMessageService;

    @Autowired
    private IOkrTimeSessionsService okrTimeSessionsService;

/*    @GetMapping(value = "/index.htm")
    public String index(Model model, boolean flag) throws Exception {
        String userName = getCurrentUser().getRealName();
        if (StringUtils.isEmpty(userName)) {
            userName = UserUtils.getUser().getUserName();
        }
        model.addAttribute("flag", flag);
        model.addAttribute("userName", userName);
        model.addAttribute("photoUrl", getCurrentUser().getPhotoUrl());
        // 查询当前时间段
        TimeSessionsExtVO timeSessionsExtVO = okrTimeSessionsService.getDefaultTimeSession();
        model.addAttribute("timeSession", timeSessionsExtVO);
        return "common/index";
    }*/

    @GetMapping(value = "/main.htm")
    public String main(Model model) {
        return "common/main";
    }

    /**
     * 获取OKR列表
     * @return
     */
    @RequestMapping(value = "/getAllOkrList.json")
    @ResponseBody
    public ResponseResult getAllOkrList(String timeSessionId) {
        ResponseResult responseResult = new ResponseResult();
        OkrObjectSearchVO searchVO = new OkrObjectSearchVO();
        searchVO.setTimeSessionId(timeSessionId);
        searchVO.setUserId(getCurrentUserId());
        List<ObjectivesExtVO> objectivesExtList = okrObjectService.getAllOkrList(searchVO);
        responseResult.setInfo(objectivesExtList);
        return responseResult;
    }

    /**
     * 获取执行情况
     * @param type
     * @return
     */
    @RequestMapping(value = "/execution.json")
    @ResponseBody
    public ResponseResult getByApplyNo(String timeSessionId, String type) {
        ResponseResult responseResult = new ResponseResult();
        ObjectivesVO objectivesVO = new ObjectivesVO();
        objectivesVO.setType(type);
        objectivesVO.setOwnerId(getCurrentUserId());
        objectivesVO.setTimeSessionId(timeSessionId);
        ExecutionVO executionVO = indexService.execution(objectivesVO);
        responseResult.setInfo(executionVO);
        return responseResult;
    }

    /**
     * 获取OKR消息提醒
     * 只查询未处理或者未读的消息
     * @return
     */
    @RequestMapping(value = "/getOkrMessage.json")
    @ResponseBody
    public ResponseResult getOkrMessage() {
        ResponseResult responseResult = new ResponseResult();
        List<MessagesVO> messagesList = okrMessageService.getMessageList(null, getCurrentUserId(), 4);
        responseResult.setInfo(messagesList);
        return responseResult;
    }
}