package com.sss.car;

import com.blankj.utilcode.dao.Webbiz;
import com.blankj.utilcode.okhttp.callback.StringCallback;

import okhttp3.Call;


/**
 * Created by leilei on 2017/8/9.
 */

@SuppressWarnings("ALL")
public class RequestWeb {

    /**
     * 用户反馈关键词信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call form(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Feedback/form", send, "用户反馈关键词信息", stringCallback);
    }
    /**
     * 用户反馈
     *
     * @param send
     * @param stringCallback
     */
    public static Call insert_feedback(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Feedback/insert_feedback", send, "用户反馈", stringCallback);
    }

    /**
     * 用户是否已经开店
     *
     * @param send
     * @param stringCallback
     */
    public static Call exists_shop(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Shop/exists_shop", send, "用户是否已经开店", stringCallback);
    }

    /**
     * 获取未读消息数量
     *
     * @param send
     * @param stringCallback
     */
    public static Call unreadNumber(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/unread_number", send, "获取未读消息数量", stringCallback);
    }
    /**
     * 获取未读消息
     *
     * @param send
     * @param stringCallback
     */
    public static Call unread(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/unread", send, "获取未读消息", stringCallback);
    }

    /**
     * 设置消息已读
     *
     * @param send
     * @param stringCallback
     */
    public static Call read_window(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Chat/read_window", send, "设置消息已读", stringCallback);
    }


    /**
     * 通知服务器删除消息
     *
     * @param send
     * @param stringCallback
     */
    public static Call close_window(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Chat/close_window", send, "通知服务器删除消息", stringCallback);
    }

    /**
     * 分享综合
     *
     * @param send
     * @param stringCallback
     */
    public static Call shareSynthesize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/synthesize", send, "分享综合", stringCallback);
    }

    /**
     * 收藏/取消收藏帖子分类
     *
     * @param send
     * @param stringCallback
     */
    public static Call collectPostsType(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_collect/insert_collect", send, "收藏/取消收藏帖子分类", stringCallback);
    }

    /**
     * 广告
     *
     * @param send
     * @param stringCallback
     */
    public static Call advertisement(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/ad_site/into", send, "搜索-广告", stringCallback);
    }

    /**
     * 搜索-搜索会话
     *
     * @param send
     * @param stringCallback
     */
    public static Call searchConversation(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Chat/index", send, "搜索-搜索会话", stringCallback);
    }

    /**
     * 搜索-搜索用户
     *
     * @param send
     * @param stringCallback
     */
    public static Call searchUser(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/search", send, "搜索-搜索用户", stringCallback);
    }

    /**
     * 获取帖子评论。点赞。分享数据
     *
     * @param send
     * @param stringCallback
     */
    public static Call getPostsPraiseCommentSharenumber(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/get_number", send, "获取帖子评论。点赞。分享数据", stringCallback);
    }

    /**
     * 获取动态评论。点赞。分享数据
     *
     * @param send
     * @param stringCallback
     */
    public static Call getDymaicPraiseCommentSharenumber(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/get_number", send, "获取动态评论。点赞。分享数据", stringCallback);
    }

    /**
     * 登录
     *
     * @param send
     * @param stringCallback
     */
    public static Call login(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Login/into", send, "登录", stringCallback);
    }

    /**
     * 注册
     *
     * @param send
     * @param stringCallback
     */
    public static Call register(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Register/into", send, "注册", stringCallback);
    }

    /**
     * 获取短信验证码
     *
     * @param send
     * @param stringCallback
     */
    public static Call getSMS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sms/into", send, "获取短信验证码", stringCallback);
    }


    /**
     * 修改个人信息(手机,用户真实姓名,昵称,头像,性别,找回密码)
     *
     * @param send
     * @param meaning
     * @param stringCallback
     * @return
     */
    public static Call setUserInfo(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/set_info", send, "修改个人信息(手机,用户真实姓名,昵称,头像,性别,找回密码)---" + meaning, stringCallback);
    }

    /**
     * 重置支付密码
     *
     * @param send
     * @param meaning
     * @param stringCallback
     * @return
     */
    public static Call set_pay_pass(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/set_pay_pass", send, "重置支付密码" + meaning, stringCallback);
    }
    /**
     * 找回登录密码
     *
     * @param send
     * @param stringCallback
     */
    public static Call findPassword(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Register/retrieve", send, "找回登录密码", stringCallback);
    }


    /**
     * 账号申诉
     *
     * @param send
     * @param stringCallback
     */
    public static Call accountComplaint(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Appeal/into", send, "账号申诉", stringCallback);
    }

    /**
     * 用户基本信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call userInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/info", send, "用户基本信息", stringCallback);
    }

    /**
     * 通讯录
     *
     * @param send
     * @param stringCallback
     */
    public static Call address_book(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/address_book", send, "通讯录", stringCallback);
    }

    /**
     * 浏览历史(足迹)
     *
     * @param send
     * @param stringCallback
     */
    public static Call histroy(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_looks/goods", send, "浏览历史(足迹)", stringCallback);
    }
    /**
     * 删除浏览历史(足迹)
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_looks(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_looks/del_looks", send, "删除浏览历史(足迹)", stringCallback);
    }

    /**
     * 清空浏览历史(足迹)
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_all(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_looks/del_all", send, "清空浏览历史(足迹)", stringCallback);
    }

    /**
     * 我的车辆
     *
     * @param send
     * @param stringCallback
     */
    public static Call myCar(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/vehicle_list", send, "我的车辆", stringCallback);
    }

    /**
     * 我的爱车-热门车辆选择列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call hotCar(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/hot", send, "我的爱车-热门车辆选择列表", stringCallback);
    }

    /**
     * 我的爱车-车辆选择列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call carList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/letter", send, "我的爱车-车辆选择列表", stringCallback);
    }

    /**
     * 我的爱车-车辆子品牌
     *
     * @param send
     * @param stringCallback
     */
    public static Call carChildList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/series", send, "我的爱车-车辆子品牌", stringCallback);
    }

    /**
     * 我的爱车-品牌搜索
     *
     * @param send
     * @param stringCallback
     */
    public static Call searchCar(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/search", send, "我的爱车-品牌搜索", stringCallback);
    }

    /**
     * 我的爱车-选择车辆排量
     *
     * @param send
     * @param stringCallback
     */
    public static Call chooseCarDisplacement(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/displacement", send, "我的爱车-选择车辆排量", stringCallback);
    }

    /**
     * 我的爱车-选择车辆年份
     *
     * @param send
     * @param stringCallback
     */
    public static Call chooseCarYear(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/year", send, "我的爱车-选择车辆年份", stringCallback);
    }


    /**
     * 我的爱车-选择车辆款式
     *
     * @param send
     * @param stringCallback
     */
    public static Call chooseCarType(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/style", send, "我的爱车-选择车辆款式", stringCallback);
    }

    /**
     * 我的爱车-创建爱车
     *
     * @param send
     * @param stringCallback
     */
    public static Call createCar(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/insert_vehicle", send, "我的爱车-选择车辆年份", stringCallback);
    }

    /**
     * 我的爱车-选定车辆(设为默认)
     *
     * @param send
     * @param stringCallback
     */
    public static Call chooseCar(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/status_vehicle", send, "我的爱车-选定车辆(设为默认)", stringCallback);
    }

    /**
     * 我的爱车-删除车辆
     *
     * @param send
     * @param stringCallback
     */
    public static Call deleteCar(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/del_vehicle", send, "我的爱车-删除车辆", stringCallback);
    }

    /**
     * 我的爱车-获取我的当前默认车辆信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call myCurrentCarInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/my_vehicle", send, "我的爱车-获取我的当前默认车辆信息", stringCallback);
    }

    /**
     * 我的爱车-获取要修改的车辆某一项属性
     *
     * @param send
     * @param stringCallback
     */
    public static Call getChangeCarInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Brand/change", send, "我的爱车-获取要修改的车辆某一项属性", stringCallback);
    }

    /**
     * 我的爱车-修改我的当前默认车辆信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call updateCurrentCarInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/update_vehicle", send, "我的爱车-修改我的当前默认车辆信息", stringCallback);
    }


    /**
     * 收货地址-获取用户收货地址
     *
     * @param send
     * @param stringCallback
     */
    public static Call getUserAdress(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/address", send, "收货地址-获取用户收货地址", stringCallback);
    }

    /**
     * 收货地址-新增用户收货地址
     *
     * @param send
     * @param stringCallback
     */
    public static Call saveUserAdress(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/save_address", send, "收货地址-新增用户收货地址", stringCallback);
    }

    /**
     * 收货地址-设为默认
     *
     * @param send
     * @param stringCallback
     */
    public static Call setDefaultAddress(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/set_address", send, "收货地址-设为默认", stringCallback);
    }

    /**
     * 收货地址-删除地址
     *
     * @param send
     * @param stringCallback
     */
    public static Call deleteAddress(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/del_address", send, "收货地址-删除地址", stringCallback);
    }

    /**
     * 实体认证-创建/修改
     *
     * @param send
     * @param stringCallback
     * @return
     */
    public static Call setCompany(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/set_company", send, "实体认证-创建/修改---" + meaning, stringCallback);
    }

    /**
     * 实体认证-获取店铺信息
     *
     * @param send
     * @param stringCallback
     * @return
     */
    public static Call getCompany(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/get_company", send, "实体认证-获取店铺信息", stringCallback);
    }

    /**
     * 实体认证-获取店铺图片
     *
     * @param send
     * @param stringCallback
     * @return
     */
    public static Call getCompanyPic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/get_picture", send, "实体认证-获取店铺图片", stringCallback);
    }

    /**
     * 实体认证-上传店铺图片
     *
     * @param send
     * @param stringCallback
     * @return
     */
    public static Call uploadCompanyPic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/upload_picture", send, "实体认证-上传店铺图片", stringCallback);
    }

    /**
     * 实体认证-删除店铺图片
     *
     * @param send
     * @param stringCallback
     * @return
     */
    public static Call delCompanyPic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/del_picture", send, "实体认证-上传店铺图片", stringCallback);
    }

    /**
     * 实体认证-获取服务范围
     *
     * @param send
     * @param stringCallback
     */
    public static Call compayService(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/scope", send, "实体认证-获取服务范围", stringCallback);
    }

    /**
     * 商铺资料_获取店铺信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call getShop(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/shop/get_shop", send, "商铺资料_获取店铺信息", stringCallback);
    }

    /**
     * 商铺资料_设置店铺信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call setShop(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/shop/set_shop", send, "商铺资料_设置店铺信息---" + meaning, stringCallback);
    }


    /**
     * 获取用户基本信息(外层)
     *
     * @param send
     * @param stringCallback
     */
    public static Call getUserInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/my_site", send, "获取用户基本信息(外层)", stringCallback);
    }

    /**
     * 消息-获取聊天列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call getChatList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Chat/index", send, "消息-获取聊天列表", stringCallback);
    }

    /**
     * 消息-上传聊天信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call uploadMessageInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Chat/insert_chat", send, "消息-上传聊天信息", stringCallback);
    }

    /**
     * 是否已经被对方关注
     *
     * @param send
     * @param stringCallback
     */
    public static Call isAttention(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/is_attention", send, "消息-是否已经被对方关注", stringCallback);
    }

    /**
     * 好友操作-关注/取消关注/添加/删除好友/拉黑/取消拉黑/特别关心/取消特别关心
     * 	1关注，2特别关心，3黑名单，4删除
     *
     * @param send
     * @param stringCallback
     */
    public static Call friendOperation(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/into", send, "好友操作-" + meaning, stringCallback);
    }


    /**
     * 好友关系-获取好友,关注,粉丝,最近聊天
     *
     * @param send
     * @param stringCallback
     */
    public static Call friendRelation(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/relation", send, "好友关系-" + meaning, stringCallback);
    }


    /**
     * 设置好友备注
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_remark(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/set_remark", send, "设置好友备注", stringCallback);
    }

    /**
     * 创建群组
     *
     * @param send
     * @param stringCallback
     */
    public static Call createGroup(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/insert_group", send, "创建群组", stringCallback);
    }

    /**
     * 邀请加群
     *
     * @param send
     * @param stringCallback
     */
    public static Call invitationAddGroup(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/invite", send, "邀请加群", stringCallback);
    }
    /**
     * 直接加群
     *
     * @param send
     * @param stringCallback
     */
    public static Call join(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/join", send, "直接加群", stringCallback);
    }
    /**
     * 获取群列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call getGroupList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/group_list", send, "获取群列表", stringCallback);
    }

    /**
     * 获取群信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call getGroupInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/get_group", send, "获取群信息", stringCallback);
    }

    /**
     * 设置群信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call setGroupInfo(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/set_group", send, "设置群信息-" + meaning, stringCallback);
    }

    /**
     * 获取群成员列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call getGroupMember(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/member_list", send, "获取群成员列表", stringCallback);
    }

    /**
     * 设置我在群内的昵称
     *
     * @param send
     * @param stringCallback
     */
    public static Call setMyRemarkInGroup(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/set_remark", send, "设置我在群内的昵称", stringCallback);
    }

    /**
     * 退群
     *
     * @param send
     * @param stringCallback
     */
    public static Call exitGroup(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/quit", send, "退群", stringCallback);
    }
    /**
     * 移除群成员
     *
     * @param send
     * @param stringCallback
     */
    public static Call remove_member(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Group/remove_member", send, "移除群成员", stringCallback);
    }

    /**
     * 获取要举报的条目
     *
     * @param send
     * @param stringCallback
     */
    public static Call complain(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/complain_form", send, "获取要举报的条目", stringCallback);
    }

    /**
     * 举报
     *
     * @param send
     * @param stringCallback
     */
    public static Call reportComplain(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Complain/insert_complain", send, "举报", stringCallback);
    }

    /**
     * 获取用户详细资料
     *
     * @param send
     * @param stringCallback
     */
    public static Call trends_member(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/trends_member", send, "获取用户详细资料", stringCallback);
    }

    /**
     * 获取信誉
     *
     * @param send
     * @param stringCallback
     */
    public static Call reputationList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/credit", send, " 获取信誉", stringCallback);
    }

    /**
     * 删除信誉
     *
     * @param send
     * @param stringCallback
     */
    public static Call deleteReputation(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Credit/del_credit", send, " 删除信誉", stringCallback);
    }

    /**
     * 用户动态信息(左边是日期)
     *
     * @param send
     * @param stringCallback
     */
    public static Call getUserDymaic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/my_trends", send, " 用户动态信息(左边是日期)", stringCallback);
    }

    /**
     * 获取动态
     *
     * @param send
     * @param stringCallback
     */
    public static Call getDymaic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/into", send, " 获取动态", stringCallback);
    }

    /**
     * 发布动态
     *
     * @param send
     * @param stringCallback
     */
    public static Call publishDymaic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/insert_trends", send, "分享-发布动态", stringCallback);
    }

    /**
     * 用户动态信息(左边是头像)
     *
     * @param send
     * @param stringCallback
     */
    public static Call dymaicInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/friend_trends", send, " 用户动态信息(左边是头像)", stringCallback);
    }

    /**
     * 点赞
     *
     * @param send
     * @param stringCallback
     */
    public static Call praise(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_likes/insert_likes", send, " 点赞", stringCallback);
    }

    /**
     * 删除动态
     *
     * @param send
     * @param stringCallback
     */
    public static Call deleteDymaic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/del_trends", send, " 删除动态", stringCallback);
    }

    /**
     * 动态评论
     *
     * @param send
     * @param stringCallback
     */
    public static Call commentDymaic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/comment", send, " 动态评论", stringCallback);
    }

    /**
     * 动态转发
     *
     * @param send
     * @param stringCallback
     */
    public static Call transmitDymaic(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/transmit", send, " 动态转发", stringCallback);
    }

    /**
     * 动态详情
     *
     * @param send
     * @param stringCallback
     */
    public static Call dymaicDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/details", send, " 动态详情", stringCallback);
    }

    /**
     * 动态详情评论
     *
     * @param send
     * @param stringCallback
     */
    public static Call dymaicDetailsComment(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Trends/comment_list", send, " 动态详情评论", stringCallback);
    }

    /**
     * 我的收藏
     *
     * @param send
     * @param stringCallback
     */
    public static Call collect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/collect", send, " 我的收藏", stringCallback);
    }

    /**
     * 我的标签
     *
     * @param send
     * @param stringCallback
     */
    public static Call myLable(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_label/into", send, " 我的标签", stringCallback);
    }

    /**
     * 已选中目标的标签
     *
     * @param send
     * @param stringCallback
     */
    public static Call chooseLabelList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Label/into", send, " 已选中目标的标签", stringCallback);
    }

    /**
     * 上传标签
     *
     * @param send
     * @param stringCallback
     */
    public static Call upLoadLabel(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_label/insert_label", send, " 上传标签", stringCallback);
    }

    /**
     * 获取动态社区文章
     *
     * @param send
     * @param stringCallback
     */
    public static Call communityArticle(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/into", send, " 获取动态社区文章", stringCallback);
    }

    /**
     * 收藏（取消收藏）
     *
     * @param send
     * @param stringCallback
     */
    public static Call postsCollectCancelCollect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_collect/insert_collect", send, " 帖子收藏（取消收藏）", stringCallback);
    }


    /**
     * 获取社区分类
     *
     * @param send
     * @param stringCallback
     */
    public static Call postsCate(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/cate", send, " 获取社区分类", stringCallback);
    }

    /**
     * 删除社区文章
     *
     * @param send
     * @param stringCallback
     */
    public static Call deletePosts(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/del_community", send, " 删除社区文章", stringCallback);
    }

    /**
     * 社区文章详情
     *
     * @param send
     * @param stringCallback
     */
    public static Call postsDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/details", send, " 社区文章详情", stringCallback);
    }

    /**
     * 社区文章评论列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call postsList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/comment_lists", send, " 社区文章评论列表", stringCallback);
    }

    /**
     * 帖子评论
     *
     * @param send
     * @param stringCallback
     */
    public static Call postsComment(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/insert_comment", send, " 帖子评论", stringCallback);
    }


    /**
     * 发布帖子
     *
     * @param send
     * @param stringCallback
     */
    public static Call publishPosts(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/insert_community", send, " 发布帖子", stringCallback);
    }

    /**
     * 发布帖子(特别版)
     *
     * @param send
     * @param stringCallback
     */
    public static Call publishPostsSpecial(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/add_community", send, " 发布帖子(特别版)", stringCallback);
    }

    /**
     * 分类(1车品2车服3消息4分享)
     *
     * @param send
     * @param stringCallback
     */
    public static Call goods_classify(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/classify", send, "分类(1车品2车服3消息4分享)", stringCallback);
    }

    /**
     * 车品子分类(TAB)综合
     *
     * @param send
     * @param stringCallback
     */
    public static Call classify_four(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/classify_four", send, "车品子分类(TAB)", stringCallback);
    }

    /**
     * 车品子分类(TAB)
     *
     * @param send
     * @param stringCallback
     */
    public static Call subclass(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/subclass", send, "车品子分类(TAB)", stringCallback);
    }

    /**
     * 获取热门商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call hot_subclass(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/hot_subclass", send, "获取热门商品", stringCallback);
    }

    /**
     * 车品子分类(竖向列表)
     *
     * @param send
     * @param stringCallback
     */
    public static Call classify_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/classify_goods", send, "车品子分类(竖向列表)", stringCallback);
    }


    /**
     * 车品子分类(竖向列表--综合)
     *
     * @param send
     * @param stringCallback
     */
    public static Call synthesize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/synthesize", send, "车品子分类(竖向列表--综合)", stringCallback);
    }

    /**
     * 车品子分类(竖向列表--更多)
     *
     * @param send
     * @param stringCallback
     */
    public static Call top_goods_list(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/top_goods_list", send, "车品子分类(竖向列表--更多)", stringCallback);
    }


    /**
     * 获取商品列表(首页)---作废
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/get_goods", send, "获取商品列表(首页)", stringCallback);
    }

    /**
     * 获取商品列表(activity)
     *
     * @param send
     * @param stringCallback
     */
    public static Call goods_list(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/goods_list", send, "获取商品列表(activity)", stringCallback);
    }


    /**
     * 商品详情信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call goods_details(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/details", send, "商品详情信息", stringCallback);
    }
    /**
     * 获取订单优惠后价格
     *
     * @param send
     * @param stringCallback
     */
    public static Call coupon_price(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/coupon_price", send, "获取订单优惠后价格", stringCallback);
    }

    /**
     * 获取店铺信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call goods_shop(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Shop/goods_shop", send, "获取店铺信息", stringCallback);
    }

    /**
     * 商品详情图片
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_photo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/get_photo", send, "商品详情图片", stringCallback);
    }

    /**
     * 商品详情评价
     *
     * @param send
     * @param stringCallback
     */
    public static Call goods_comment(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/goods_comment", send, "商品详情评价", stringCallback);
    }


    /**
     * 店铺关注,收藏(取消关注,收藏)
     *
     * @param send
     * @param stringCallback
     */
    public static Call shop_collect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_collect/insert_collect", send, "商品详情评价", stringCallback);
    }

    /**
     * 店铺全部宝贝
     *
     * @param send
     * @param stringCallback
     */
    public static Call shop_all(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/shop_goods", send, "店铺全部宝贝", stringCallback);
    }

    /**
     * 筛选店铺宝贝
     *
     * @param send
     * @param stringCallback
     */
    public static Call filtrate(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/tab_classify", send, "筛选店铺宝贝", stringCallback);
    }


    /**
     * 获取classify_id(淘秒杀淘优惠)
     *
     * @param send
     * @param stringCallback
     */
    public static Call getClassify_id(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/top_classify", send, "获取classify_id(淘秒杀淘优惠)", stringCallback);
    }


    /**
     * 淘秒杀-获取两条数据
     *
     * @param send
     * @param stringCallback
     */
    public static Call getSecKill(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/classify_goods", send, "淘秒杀-获取两条数据", stringCallback);
    }

    /**
     * 淘秒杀-获取更多数据
     *
     * @param send
     * @param stringCallback
     */
    public static Call activity_list(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/activity_list", send, "淘秒杀-获取更多数据", stringCallback);
    }

    /**
     * 淘优惠-获取两条数据
     *
     * @param send
     * @param stringCallback
     */
    public static Call classify_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/classify_coupon", send, "淘优惠-获取两条数据", stringCallback);
    }

    /**
     * 淘优惠_更多
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/get_coupon", send, "淘优惠_更多", stringCallback);
    }

    /**
     * 优惠券详情
     *
     * @param send
     * @param stringCallback
     */
    public static Call couponDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/details", send, "优惠券详情", stringCallback);
    }

    /**
     * 设置优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/set_coupon", send, "设置优惠券", stringCallback);
    }

    /**
     * 购买优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call buyCoupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/submit", send, "购买优惠券", stringCallback);
    }

    /**
     * 退还优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call returnsCoupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/refund", send, "退还优惠券", stringCallback);
    }

    /**
     * 获取加入优惠券店铺
     *
     * @param send
     * @param stringCallback
     */
    public static Call couponShop(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/shop", send, "获取加入优惠券店铺", stringCallback);
    }

    /**
     * 获取加入优惠券的店铺
     *
     * @param send
     * @param stringCallback
     */
    public static Call subject(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/subject", send, "获取加入优惠券店铺", stringCallback);
    }

    /**
     * 获取店铺所支持的优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call shop_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/Api/Coupon/shop_coupon", send, "获取店铺所支持的优惠券", stringCallback);
    }

    /**
     * 点击加入购物车时弹出的商品信息选择框
     *
     * @param send
     * @param stringCallback
     */
    public static Call addToShoppingCartAndChooseDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/details", send, "点击加入购物车时弹出的商品信息选择框", stringCallback);
    }

    /**
     * 获取购物车列表/订单列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call getShoppingCartOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Cart/into", send, "获取购物车列表/预购订单列表", stringCallback);
    }

    /**
     * 购物车数量
     *
     * @param send
     * @param stringCallback
     */
    public static Call cart_num(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/cart_num", send, "购物车数量", stringCallback);
    }
    /**
     * (更新)购物车,预购,订单内的商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call UpdateShoppingCart(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Cart/update_cart", send, "(更新)购物车,预购,订单内的商品", stringCallback);
    }

    /**
     * 删除购物车内的商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call deleteShoppingCart(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Cart/del_cart", send, "删除购物车内的商品", stringCallback);
    }

    /**
     * 添加到购物车或立即下单
     *
     * @param send
     * @param stringCallback
     */
    public static Call addShoppingCartOrOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Cart/insert_cart", send, "添加到购物车或立即下单", stringCallback);
    }


    /**
     * 点击购物车编辑页面下拉时弹出的商品信息选择框
     *
     * @param send
     * @param stringCallback
     */
    public static Call details(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/details", send, "点击购物车编辑页面下拉时弹出的商品信息选择框", stringCallback);
    }

    /**
     * 批量收藏取消收藏
     *
     * @param send
     * @param stringCallback
     */
    public static Call collect_all(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_collect/collect_all", send, "批量收藏取消收藏", stringCallback);
    }

    /**
     * 加入预购
     *
     * @param send
     * @param stringCallback
     */
    public static Call insert_order(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Cart/insert_order", send, "加入预购", stringCallback);
    }

    /**
     * 实物服务保存到草稿箱
     *
     * @param send
     * @param stringCallback
     */
    public static Call save_drafts(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/save_drafts ", send, "实物服务保存到草稿箱", stringCallback);
    }

    /**
     * 综合预购单
     *
     * @param send
     * @param stringCallback
     */
    public static Call compoOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Cart/synthesize", send, "综合预购单", stringCallback);
    }

    /**
     * 获取用户默认地址(服务预购订单)
     *
     * @param send
     * @param stringCallback
     */
    public static Call defaultAddressSynthesize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/address", send, "获取用户默认地址(服务预购订单)", stringCallback);
    }

    /**
     * 获取用户默认车辆
     *
     * @param send
     * @param stringCallback
     */
    public static Call defaultVehicle(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/vehicle", send, "获取用户默认车辆", stringCallback);
    }

    /**
     * 获取用户订单属性
     *
     * @param send
     * @param stringCallback
     */
    public static Call orderAttr(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/attr", send, "获取用户订单属性", stringCallback);
    }

    /**
     * 获取用户预购订单优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/coupon", send, "获取用户预购订单优惠券", stringCallback);
    }

    /**
     * 获取订单提示
     *
     * @param send
     * @param stringCallback
     */
    public static Call orderTip(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Explain/details", send, "获取订单提示", stringCallback);
    }

    /**
     * 验证预留的密码保护信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call reserved(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Register/reserved", send, "验证预留的密码保护信息", stringCallback);
    }

    /**
     * 提交订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call submitOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/submit", send, "提交订单", stringCallback);
    }


    /**
     * 我的收入订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call getOrderinfo(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/income", send, "我的收入订单-" + meaning, stringCallback);
    }

    /**
     * 我的支出订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call getOrderinfoInto(String send, String meaning, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/expend", send, "我的支出订单-" + meaning, stringCallback);
    }

    /**
     * 订单评论(买家版)
     *
     * @param send
     * @param stringCallback
     */
    public static Call commentOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/comment", send, "订单评论(买家版)", stringCallback);
    }

    /**
     * 订单评论(卖家版)
     *
     * @param send
     * @param stringCallback
     */
    public static Call shop_comment(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/shop_comment", send, "订单评论(卖家版)", stringCallback);
    }

    /**
     * 取消订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call cancelOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/cancel", send, "取消订单", stringCallback);
    }

    /**
     * 确认收货
     *
     * @param send
     * @param stringCallback
     */
    public static Call sureOrderGoods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/take_goods", send, "确认收货", stringCallback);
    }

    /**
     * 商家确认收货（换货）
     *
     * @param send
     * @param stringCallback
     */
    public static Call exchange_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/exchange_goods", send, "商家确认收货（换货）", stringCallback);
    }

    /**
     * 商家确认收货（退货）
     *
     * @param send
     * @param stringCallback
     */
    public static Call confirm_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/confirm_goods", send, "商家确认收货（退货）", stringCallback);
    }

    /**
     * 确认服务
     *
     * @param send
     * @param stringCallback
     */
    public static Call service_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/service_goods", send, "确认服务", stringCallback);
    }

    /**
     * 删除已交易成功的订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call deleteOrder(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/del_order", send, "删除已交易成功的订单", stringCallback);
    }

    /**
     * 申请退换货
     *
     * @param send
     * @param stringCallback
     */
    public static Call applyForReturnsAndChange(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_exchange/retreat", send, "申请退换货", stringCallback);
    }

    /**
     * 卖家获取退换货详情
     *
     * @param send
     * @param stringCallback
     */
    public static Call getReturnAndChangeOrderInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_exchange/info", send, "卖家获取退换货详情", stringCallback);
    }

    /**
     * 完善退换货资料
     *
     * @param send
     * @param stringCallback
     */
    public static Call completeReturnsAndChange(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_exchange/complete", send, "完善退换货资料", stringCallback);
    }

    /**
     * 卖家同意或拒绝用户退换货
     *
     * @param send
     * @param stringCallback
     */
    public static Call feedback(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_exchange/feedback", send, "卖家同意或拒绝用户退换货", stringCallback);
    }

    /**
     * 同意和拒绝订单信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call status_exchange(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_exchange/status_exchange", send, "同意和拒绝订单信息", stringCallback);
    }

    /**
     * 发布SOS信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call publisSOS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/submit", send, "发布SOS信息", stringCallback);
    }

    /**
     * 保存SOS信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call saveSOS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/save_drafts", send, "保存SOS信息", stringCallback);
    }

    /**
     * 获取SOS抢单者列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call consent_list(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/consent_list", send, "获取SOS抢单者列表", stringCallback);
    }

    /**
     * 服务商抢SOS单
     *
     * @param send
     * @param stringCallback
     */
    public static Call consent(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/consent", send, "服务商抢SOS单", stringCallback);
    }

    /**
     * 求助者同意SOS单(SOS订单建立)
     *
     * @param send
     * @param stringCallback
     */
    public static Call payment_orderSOS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/payment_order", send, "求助者同意SOS单(SOS订单建立)", stringCallback);
    }

    /**
     * 获取已发布的SOS订单信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_drafts_SOS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/get_drafts", send, "获取已发布的SOS订单信息", stringCallback);
    }

    /**
     * 忽略增援的SOS订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call neglect_SOS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/neglect", send, "忽略增援的SOS订单", stringCallback);
    }

    /**
     * 删除SOS订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_sos(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/del_sos", send, "删除SOS订单", stringCallback);
    }
    /**
     * 求救者确认增援的SOS订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call consent_into_sos(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/consent_into", send, "求救者确认增援的SOS订单", stringCallback);
    }
    /**
     * 求助者列表中取消SOS订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call cancel_AllSOS(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/SOS/cancel_sos", send, "求助者列表中取消SOS订单", stringCallback);
    }
    /**
     * 获取SOS列表信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call getSOSSellerList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/into", send, "获取SOS列表信息", stringCallback);
    }


    /**
     * 获取SOS订单信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call getSOSSellerDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/details", send, "获取SOS订单信息 ", stringCallback);
    }

    /**
     * 完成SOS订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call accomplish(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/accomplish", send, "完成SOS订单 ", stringCallback);
    }

    /**
     * 取消SOS订单信息(卖家版)
     *
     * @param send
     * @param stringCallback
     */
    public static Call cancel_sos(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/SOS/cancel_sos", send, "取消SOS订单信息(卖家版)", stringCallback);
    }

    /**
     * 获取订单详情(卖家版)
     *
     * @param send
     * @param stringCallback
     */
    public static Call getOrderDetailsSeller(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/details", send, "获取订单详情(卖家版)", stringCallback);
    }

    /**
     * 获取订单详情(卖家版)新
     *
     * @param send
     * @param stringCallback
     */
    public static Call getOrderDetailsSeller_detail(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/detail", send, "获取订单详情(卖家版)", stringCallback);
    }

    /**
     * 支出和收入同意或者拒绝
     *
     * @param send
     * @param stringCallback
     */
    public static Call operation(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/operation", send, "支出和收入同意或者拒绝", stringCallback);
    }

    /**
     * 删除支出订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_expend(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/del_expend", send, "删除支出订单", stringCallback);
    }


    /**
     * 验证支付密码是否存在
     *
     * @param send
     * @param stringCallback
     */
    public static Call exits_pass(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member/exits_pass", send, "验证支付密码是否存在", stringCallback);
    }


    /**
     * 验证支付密码是否正确
     *
     * @param send
     * @param stringCallback
     */
    public static Call pay_pass(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member/pay_pass", send, "验证支付密码是否正确", stringCallback);
    }


    /**
     * 立即为订单支付
     *
     * @param send
     * @param stringCallback
     */
    public static Call payment_order(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/payment_order", send, "立即为订单支付", stringCallback);
    }


    /**
     * 完善资料页面
     *
     * @param send
     * @param stringCallback
     */
    public static Call complete(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_exchange/complete", send, "完善资料页面", stringCallback);
    }


    /**
     * 立即发货
     *
     * @param send
     * @param stringCallback
     */
    public static Call deliver_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/deliver_goods", send, "立即发货", stringCallback);
    }

    /**
     * 立即发货
     *
     * @param send
     * @param stringCallback
     */
    public static Call deliver(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/deliver", send, "立即发货", stringCallback);
    }

    /**
     * 消息==>评价
     *
     * @param send
     * @param stringCallback
     */
    public static Call managementOfMessageEvaluation(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Comment/into", send, "消息==>评价", stringCallback);
    }

    /**
     * 消息==>综合
     *
     * @param send
     * @param stringCallback
     */
    public static Call messageSynthesize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/synthesize", send, "消息==>综合", stringCallback);
    }

    /**
     * 置顶/取消置顶
     *
     * @param send
     * @param stringCallback
     */
    public static Call top(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/set_top", send, " 置顶/取消置顶", stringCallback);
    }

    /**
     * 添加车辆信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call add_vehicle(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/add_vehicle", send, " 添加车辆信息", stringCallback);
    }


    /**
     * 获取该账号是否注册
     *
     * @param send
     * @param stringCallback
     */
    public static Call mobile_isset(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/mobile_isset", send, " 获取该账号是否注册", stringCallback);
    }


    /**
     * 更新在线时间
     *
     * @param send
     * @param stringCallback
     */
    public static Call timeStatus(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/status", send, " 更新在线时间", stringCallback);
    }


    /**
     * 获取系统消息
     *
     * @param send
     * @param stringCallback
     */
    public static Call messageSystem(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/into", send, " 获取系统消息", stringCallback);
    }

    /**
     * 消息==>综合==>侧滑删除
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_synthesize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/del_synthesize", send, " 消息==>综合==>侧滑删除", stringCallback);
    }
    /**
     * 消息==>订单==>获取订单信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call messageOrderGetOrderInfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_messages/into", send, "消息==>订单==>获取订单信息", stringCallback);
    }

    /**
     * 我的钱包
     *
     * @param send
     * @param stringCallback
     */
    public static Call wallet(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_wallet/into", send, "我的钱包", stringCallback);
    }

    /**
     * 我的钱包收入
     *
     * @param send
     * @param stringCallback
     */
    public static Call walletIncome(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_finance/income", send, "立即为订单支付", stringCallback);
    }

    /**
     * 我的钱包==>明细查询
     *
     * @param send
     * @param stringCallback
     */
    public static Call walletDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_finance/into", send, "我的钱包==>明细查询", stringCallback);
    }

    /**
     * 我的钱包==>我的积分
     *
     * @param send
     * @param stringCallback
     */
    public static Call integral(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_wallet/integral", send, "我的钱包==>我的积分", stringCallback);
    }

    /**
     * 我的钱包==>积分==>选择赠予人
     *
     * @param send
     * @param stringCallback
     */
    public static Call select_member(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/select_member", send, "我的钱包==>积分==>选择赠予人", stringCallback);
    }

    /**
     * 我的钱包==>积分==>赠送
     *
     * @param send
     * @param stringCallback
     */
    public static Call give_integral(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_wallet/give_integral", send, "我的钱包==>积分==>赠送", stringCallback);
    }

    /**
     * 我的钱包==>优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call walletCoupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_coupon/into", send, "我的钱包==>优惠券", stringCallback);
    }

    /**
     * 我的钱包==>优惠券明细
     *
     * @param send
     * @param stringCallback
     */
    public static Call walletGetCouponDetails(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_coupon/get_coupon", send, "我的钱包==>优惠券明细", stringCallback);
    }

    /**
     * 我的钱包==>赠送优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call give_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_coupon/give_coupon", send, "我的钱包==>赠送优惠券", stringCallback);
    }

    /**
     * 我的钱包==>我的资金
     *
     * @param send
     * @param stringCallback
     */
    public static Call my_balance(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_wallet/balance", send, "我的钱包==>我的资金", stringCallback);
    }

    /**
     * 我的商品==>添加商品==>获取商品类别信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_classify(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/get_classify", send, "我的商品==>添加商品==>获取商品类别信息", stringCallback);
    }

    /**
     * 我的商品==>添加商品==>获取商品规格
     *
     * @param send
     * @param stringCallback
     */
    public static Call classify_size(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/classify_size", send, "我的商品==>添加商品==>获取商品规格", stringCallback);
    }

    /**
     * 我的商品==>添加商品==>发布/更新
     *
     * @param send
     * @param stringCallback
     */
    public static Call insert_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/insert_goods", send, "我的商品==>添加商品==>发布/更新", stringCallback);
    }

    /**
     * 我的商品==>添加商品==>保存商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call save_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/save_goods", send, "我的商品==>添加商品==>保存商品", stringCallback);
    }

    /**
     * 预览商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call preview_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/preview_goods", send, "预览商品", stringCallback);
    }

    /**
     * 我的商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call my_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/my_goods", send, "我的商品", stringCallback);
    }
    /**
     * 我的推广
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/popularize", send, "我的推广", stringCallback);
    }

    /**
     * 我的商品==>删除
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/del_goods", send, "我的商品==>删除", stringCallback);
    }

    /**
     * 我的商品==>商品上架和下架
     *
     * @param send
     * @param stringCallback
     */
    public static Call status_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/status_goods", send, "我的商品==>商品上架和下架", stringCallback);
    }

    /**
     * 我的商品==>获取商品信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call update_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/update_goods", send, "我的商品==>获取商品信息", stringCallback);
    }

    /**
     * 获取平台优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call getCoupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/into", send, "获取平台优惠券", stringCallback);
    }

    /**
     * 获取平台优惠券==>更多优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call getCoupon_more(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/more", send, "获取平台优惠券==>更多优惠券", stringCallback);
    }

    /**
     * 获取平台优惠券==>加入优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call joinCoupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/join", send, "获取平台优惠券==>加入优惠券", stringCallback);
    }

    /**
     * 优惠券管理
     *
     * @param send
     * @param stringCallback
     */
    public static Call manageCoupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/manage", send, "优惠券管理", stringCallback);
    }

    /**
     * 优惠券管理==>删除优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Coupon/del_coupon", send, "优惠券管理==>删除优惠券", stringCallback);
    }

    /**
     * 收藏/取消收藏
     *
     * @param send
     * @param stringCallback
     */
    public static Call insert_collect_cancel_collect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_collect/insert_collect", send, "收藏/取消收藏", stringCallback);
    }

    /**
     * 获取商品收藏
     *
     * @param send
     * @param stringCallback
     */
    public static Call goods_collect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_collect/goods", send, "获取商品收藏", stringCallback);
    }

    /**
     * 宝典分类
     *
     * @param send
     * @param stringCallback
     */
    public static Call cate(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Article/cate", send, "宝典分类", stringCallback);
    }

    /**
     * 宝典分类==>文章列表
     *
     * @param send
     * @param stringCallback
     */
    public static Call catList(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Article/into", send, "宝典分类==>文章列表", stringCallback);
    }

    /**
     * 宝典分类==>文章详情
     *
     * @param send
     * @param stringCallback
     */
    public static Call cateArticle(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Article/details", send, "宝典分类==>文章详情", stringCallback);
    }

    /**
     * 订单查询
     *
     * @param send
     * @param stringCallback
     */
    public static Call order_query(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/order_query", send, "订单查询", stringCallback);
    }

    /**
     * 草稿箱==>订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call drafts_order(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/get_drafts", send, "草稿箱==>订单", stringCallback);
    }

    /**
     * 草稿箱==>order 订单，sos 订单， goos 商品==>删除订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_drafts(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/del_drafts", send, "草稿箱==>order 订单，sos 订单， goos 商品==>删除订单", stringCallback);
    }

    /**
     * 更新订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call update_order(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/update_order", send, "更新订单", stringCallback);
    }

    /**
     * 草稿箱==>SOS
     *
     * @param send
     * @param stringCallback
     */
    public static Call drafts_sos(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sos/drafts", send, "草稿箱==>SOS", stringCallback);
    }

    /**
     * 草稿箱==>商品
     *
     * @param send
     * @param stringCallback
     */
    public static Call drafts_goods(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/drafts", send, "草稿箱==>商品", stringCallback);
    }

    /**
     * 获取用户设置资料
     *
     * @param send
     * @param stringCallback
     */
    public static Call getUsinfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_setting/into", send, "获取用户设置资料", stringCallback);
    }

    /**
     * 设置用户设置资料
     *
     * @param send
     * @param stringCallback
     */
    public static Call setUsinfo(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_setting/set", send, "设置用户设置资料", stringCallback);
    }

    /**
     * 好友消息免打扰
     *
     * @param send
     * @param stringCallback
     */
    public static Call remind(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/remind", send, "好友消息免打扰", stringCallback);
    }

    /**
     * 隐私设置(type:传1不看TA的 不传不让TA看我的)
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_trends(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/set_trends", send, "隐私设置", stringCallback);
    }

    /**
     * 获取隐私
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_trends(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/get_trends", send, "获取隐私", stringCallback);
    }

    /**
     * 分享==>消息==>互动管理==>设置==>特别关心,不看TA动态,不让TA看我动态,黑名单
     *
     * @param send
     * @param stringCallback
     */
    public static Call friend_into(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/friend_into", send, "分享==>消息==>互动管理==>设置==>特别关心,不看TA动态,不让TA看我动态,黑名单", stringCallback);
    }

    /**
     * 是否已收藏社区分类
     *
     * @param send
     * @param stringCallback
     */
    public static Call cate_collect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/cate_collect", send, "是否已收藏社区分类", stringCallback);
    }

    /**
     * 我的搜索记录(记录类型：goods车品，serve服务，shop店铺,city城市)
     *
     * @param send
     * @param stringCallback
     */
    public static Call search_into(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/search_log/into", send, "我的搜索记录", stringCallback);
    }

    /**
     * 设置特别关心,不看TA动态,不让TA看我动态,黑名单
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_relation(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/set_relation", send, "设置特别关心,不看TA动态,不让TA看我动态,黑名单", stringCallback);
    }

    /**
     * 删除特别关心,不看TA动态,不让TA看我动态,黑名单
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_relation(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Friend/del_relation", send, "删除特别关心,不看TA动态,不让TA看我动态,黑名单", stringCallback);
    }
    /**
     * 重置预留信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_reserved(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/set_reserved", send, "重置预留信息", stringCallback);
    }
    /**
     * 综合设置==>获取密保问题
     *
     * @param send
     * @param stringCallback
     */
    public static Call getSafety(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Register/safety", send, "综合设置==>获取密保问题", stringCallback);
    }

    /**
     * 综合设置==>设置密保问题
     *
     * @param send
     * @param stringCallback
     */
    public static Call setSafety(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/set_safety", send, "综合设置==>设置密保问题", stringCallback);
    }

    /**
     * 地址选择==>获取城市信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_city(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/city/into", send, "地址选择==>获取城市信息", stringCallback);
    }

    /**
     * 地址选择==>上传选择过的城市
     *
     * @param send
     * @param stringCallback
     */
    public static Call insert_looks(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/City/insert_looks", send, "地址选择==>获取城市信息", stringCallback);
    }

    /**
     * 地址选择==>城市搜索
     *
     * @param send
     * @param stringCallback
     */
    public static Call search_city(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/City/search", send, "地址选择==>城市搜索", stringCallback);
    }
    /**
     * 我的收藏标签搜索
     *
     * @param send
     * @param stringCallback
     */
    public static Call search_collect(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/search_collect", send, "我的收藏标签搜索", stringCallback);
    }
    /**
     * 获取个人认证资料
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_personage(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/get_personage", send, "获取个人认证资料", stringCallback);
    }

    /**
     * 删除帖子
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_community(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/community/del_community", send, "删除帖子", stringCallback);
    }
    /**
     * 设置个人认证资料
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_personage(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_auth/set_personage", send, "设置个人认证资料", stringCallback);
    }

    /**
     * 新我的支出订单
     *
     * @param send
     * @param stringCallback
     */
    public static Call new_expend(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/expend", send, "新我的支出订单", stringCallback);
    }
    /**
     * 获取订单评价用户信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call order_comment(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/order_comment", send, "获取订单评价用户信息", stringCallback);
    }
    /**
     * 新我的支出订单(单条)
     *
     * @param send
     * @param stringCallback
     */
    public static Call new_expend_single(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/new_expend", send, "新我的支出订单(单条)", stringCallback);
    }
    /**
     * 获取积分可抵用金额
     *
     * @param send
     * @param stringCallback
     */
    public static Call can_integral(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/integral", send, "获取积分可抵用金额", stringCallback);
    }
    /**
     * 获取积分可抵用金额(带验证最小金额)
     *
     * @param send
     * @param stringCallback
     */
    public static Call payment_integral(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Payment/integral", send, "获取积分可抵用金额", stringCallback);
    }
    /**
     * 统一支付
     *
     * @param send
     * @param stringCallback
     */
    public static Call payment_into(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Payment/into", send, "统一支付", stringCallback);
    }
    /**
     * 系统消息设置已读
     *
     * @param send
     * @param stringCallback
     */
    public static Call read_system(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Messages/read", send, "系统消息设置已读", stringCallback);
    }

    /**
     * 评论消息设置已读
     *
     * @param send
     * @param stringCallback
     */
    public static Call read_comment(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Comment/read", send, "评论消息设置已读", stringCallback);
    }

    /**
     * 订单消息设置已读
     *
     * @param send
     * @param stringCallback
     */
    public static Call read_order(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/order_messages/read", send, "订单消息设置已读", stringCallback);
    }
    /**
     * 消息一键已读
     *
     * @param send
     * @param stringCallback
     */
    public static Call oneKeyRead(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Classify/read", send, "消息一键已读", stringCallback);
    }

    /**
     * 全局搜索
     *
     * @param send
     * @param stringCallback
     */
    public static Call all_search_into(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Search/into", send, "全局搜索", stringCallback);
    }
    /**
     * 统一分享后给服务器返回信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call share_report(String send,String url ,StringCallback stringCallback) {
        return Webbiz.requestByString(url, send, "统一分享后给服务器返回信息", stringCallback);
    }

    /**
     * 统一分享
     *
     * @param send
     * @param stringCallback
     */
    public static Call share_info(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/share_info", send, "统一分享", stringCallback);
    }

    /**
     * 发货时从服务器获取支持的快递公司
     *
     * @param send
     * @param stringCallback
     */
    public static Call express_company(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Order/express_company", send, "发货时从服务器获取支持的快递公司", stringCallback);
    }

    /**
     * 推广类型
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_type(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/cate", send, "推广类型", stringCallback);
    }

    /**
     * 推广类型--活动
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_type_subject(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/subject", send, "推广类型--活动", stringCallback);
    }

    /**
     * 推广类型--优惠券
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_type_coupon(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/coupon", send, "推广类型--优惠券", stringCallback);
    }
    /**
     * 推广--城市
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_city(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/city", send, "推广--城市", stringCallback);
    }

    /**
     * 推广类型规则
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_rule(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/rule", send, "推广类型规则", stringCallback);
    }

    /**
     * 是否还有推广位置
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_amount(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/amount", send, "是否还有推广位置", stringCallback);
    }

    /**
     * 推广费用
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_charge(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/charge", send, "推广费用", stringCallback);
    }


    /**
     * 推广提交
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_into(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/into", send, "推广提交", stringCallback);
    }
    /**
     * 我的推广草稿箱
     *
     * @param send
     * @param stringCallback
     */
    public static Call popularize_drafts(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/popularize/drafts", send, "我的推广草稿箱", stringCallback);
    }


    /**
     * 车品车服更多
     *
     * @param send
     * @param stringCallback
     */
    public static Call more_subclass(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/more_subclass", send, "车品车服更多", stringCallback);
    }

    /**
     * 取消推广
     *
     * @param send
     * @param stringCallback
     */
    public static Call cancel_popularize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/cancel_popularize", send, "取消推广", stringCallback);
    }

    /**
     * 获取推广详情信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_info(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/popularize/get_info", send, "获取推广详情信息", stringCallback);
    }

    /**
     * 删除推广信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_popularize(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Popularize/del_drafts", send, "删除推广信息", stringCallback);
    }

    /**
     * 互动管理==》兴趣
     *
     * @param send
     * @param stringCallback
     */
    public static Call collect_cate(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Community/collect_cate", send, "互动管理==》兴趣", stringCallback);
    }


    /**
     * 获取二维码
     *
     * @param send
     * @param stringCallback
     */
    public static Call qr_code(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/qr_code", send, "获取二维码", stringCallback);
    }



    /**
     * 获取默认银行卡
     *
     * @param send
     * @param stringCallback
     */
    public static Call get_default(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_card/get_default", send, "获取默认银行卡", stringCallback);
    }

    /**
     * 获取提现手续费信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call terrace_withdraw(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/chan_pay/terrace_withdraw", send, "获取提现手续费信息", stringCallback);
    }

    /**
     * 银行卡提现
     *
     * @param send
     * @param stringCallback
     */
    public static Call withdraw(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/chan_pay/withdraw", send, "银行卡提现", stringCallback);
    }

    /**
     * 已绑定的银行卡
     *
     * @param send
     * @param stringCallback
     */
    public static Call isBindBankCards(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_card/into", send, "已绑定的银行卡", stringCallback);
    }

    /**
     * 设置默认银行卡
     *
     * @param send
     * @param stringCallback
     */
    public static Call set_default(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_card/set_default", send, "设置默认银行卡", stringCallback);
    }
    /**
     * 验证银行卡号
     *
     * @param send
     * @param stringCallback
     */
    public static Call auth_card(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_card/auth_card", send, "验证银行卡号", stringCallback);
    }
    /**
     * 重新获取验证码
     *
     * @param send
     * @param stringCallback
     */
    public static Call sms_pay(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Sms/pay", send, ".重新获取验证码", stringCallback);
    }
    /**
     * 绑定银行卡
     *
     * @param send
     * @param stringCallback
     */
    public static Call insert_card(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_card/insert_card", send, "绑定银行卡", stringCallback);
    }
    /**
     * 删除已绑定银行卡
     *
     * @param send
     * @param stringCallback
     */
    public static Call del_card(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/member_card/del_card", send, "删除已绑定银行卡", stringCallback);
    }
    /**
     * 银行卡支付
     *
     * @param send
     * @param stringCallback
     */
    public static Call confirm_pay(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Payment/confirm_pay", send, "银行卡支付", stringCallback);
    }
    /**
     * 随机客服
     *
     * @param send
     * @param stringCallback
     */
    public static Call randomService(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/service", send, "随机客服", stringCallback);
    }
    /**
     * 商品规格参数
     *
     * @param send
     * @param stringCallback
     */
    public static Call parameter(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Goods/parameter", send, "商品规格参数", stringCallback);
    }

    /**
     * PC端数据信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call site(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/site", send, "PC端数据信息", stringCallback);
    }

    /**
     * PC端数据信息
     *
     * @param send
     * @param stringCallback
     */
    public static Call service_remark(String send, StringCallback stringCallback) {
        return Webbiz.requestByString(Config.url + "/index.php/Api/Member/service_remark", send, "设置客服备注", stringCallback);
    }


}
