package com.jph.xibaibai.model.http;

import android.util.Log;

import com.jph.xibaibai.model.entity.Address;
import com.jph.xibaibai.model.entity.Car;
import com.jph.xibaibai.model.entity.ConfirmOrder;
import com.jph.xibaibai.model.entity.Order;
import com.jph.xibaibai.model.entity.UserInfo;
import com.lidroid.xutils.http.RequestParams;

import java.io.File;

/**
 * Created by jph on 2015/8/12.
 */
public class APIRequests extends BaseAPIRequest implements IAPIRequests {

    private XRequestCallBack XRequestCallBack;

    public APIRequests(XRequestCallBack XRequestCallBack) {
        this.XRequestCallBack = XRequestCallBack;
    }

    @Override
    public void login(String phone, String pswd) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("iphone", phone);
        requestParams.addBodyParameter("pwd", pswd);
        request(XRequestCallBack, Tasks.LOGIN, "/login", requestParams,null);
    }

    @Override
    public void register(String phone, String pswd) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("iphone", phone);
        requestParams.addBodyParameter("pwd", pswd);
        request(XRequestCallBack, Tasks.REGISTER, "/register", requestParams,null);
    }

    @Override
    public void getCarouselAd() {
        request(XRequestCallBack, Tasks.ADCODE, "/lunbo");
    }

    /**
     * DIY项目选择
     */
    @Override
    public void getDIYDatas() {
        request(XRequestCallBack, Tasks.DIYDATACODE, "/diyPro");
    }

    @Override
    public void getBeatyDatas() {
        request(XRequestCallBack, Tasks.BEAUTYATACODE, "/diyCospro");
    }

    @Override
    public void getHomeDIYDatas() {
        request(XRequestCallBack, Tasks.HOMEDIY_LIST, "/indexProList");
    }

    /**
     * 修改用户信息
     *
     * @param id       用户ID    int
     * @param userInfo 用户信息
     */
    @Override
    public void changeUserInfo(int id, UserInfo userInfo, int flagModify) {
        RequestParams requestParams = createRequestParams();
        switch (flagModify) {
            case 1:
                requestParams.addBodyParameter("uname", String.valueOf(userInfo.getUname()));
                break;
            case 2:
                requestParams.addBodyParameter("sex", String.valueOf(userInfo.getSex()));
                break;
            case 3:
                requestParams.addBodyParameter("age", String.valueOf(userInfo.getAge()));
                break;
        }
        requestParams.addBodyParameter("uid", String.valueOf(id));
        Log.i("Tag", "uname=>" + userInfo.getUname() + "/uid=>" + String.valueOf(id) + "/sex=>" + userInfo.getSex() + "/age=>" + userInfo.getAge());
        request(XRequestCallBack, Tasks.CHANGEUSERINFO, "/updateUserInfo", requestParams,null);
    }

    /**
     * 获取用户信息
     *
     * @param uid 用户id
     * @return UserInfo.class
     */
    @Override
    public void getUserInfo(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GETUSERINFO, "/userInfo", requestParams,null);
    }

    @Override
    public void getVersionInfo() {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("version_type", "1");
        request(XRequestCallBack, Tasks.VERSIONINFO, "/version_up", requestParams,null);
    }

    /**
     * 获取洗车服务的时间
     */
    @Override
    public void getServiceTime() {
        RequestParams requestParams = createRequestParams();
        request(XRequestCallBack, Tasks.GETSERVICE_TIME, "/servertime", requestParams,null);
    }

    /**
     * 联网访问服务区域
     */
    @Override
    public void getServiceArea() {
        RequestParams requestParams = createRequestParams();
        request(XRequestCallBack, Tasks.GETSERVICE_AREA, "/server_latlng", requestParams,null);
    }

    /**
     * 查询用户车辆
     *
     * @param uid
     * @return AllCar.class
     */
    @Override
    public void getCar(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GETCAR, "/carSelect", requestParams,null);
    }

    /**
     * 删除用户车辆
     *
     * @param uid         用户id
     * @param id          车辆id
     * @param c_plate_num 车牌号
     */
    @Override
    public void deleteCar(int uid, int id, String c_plate_num) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("c_plate_num", String.valueOf(c_plate_num));
        request(XRequestCallBack, Tasks.DELETECAR, "/deleteCar", requestParams,null);
    }

    /**
     * 修改车辆信息
     *
     * @param car
     */
    @Override
    public void changeCarInfo(Car car) {

    }

    /**
     * 添加用户车辆
     * uid	用户ID	int
     * address	地址	String
     * address_lg	地址经度	String
     * address_lt	地址纬度	String
     * Address_info	详细地址	String
     */
    @Override
    public void addCar(Car car) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(car.getUid()));
        requestParams.addBodyParameter("c_img", String.valueOf(car.getC_img()));
        requestParams.addBodyParameter("c_plate_num", String.valueOf(car.getC_plate_num()));
        requestParams.addBodyParameter("c_type", String.valueOf(car.getC_type()));
        requestParams.addBodyParameter("c_brand", String.valueOf(car.getC_brand()));
        requestParams.addBodyParameter("c_color", String.valueOf(car.getC_color()));
        requestParams.addBodyParameter("add_time", String.valueOf(car.getAdd_time()));
        requestParams.addBodyParameter("c_remark", String.valueOf(car.getC_remark()));
        request(XRequestCallBack, Tasks.ADDCAR, "/carAdd", requestParams,null);
    }

    @Override
    public void changeCar(Car car) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("id", String.valueOf(car.getId()));
        requestParams.addBodyParameter("uid", String.valueOf(car.getUid()));
        requestParams.addBodyParameter("c_img", String.valueOf(car.getC_img()));
        requestParams.addBodyParameter("c_plate_num", String.valueOf(car.getC_plate_num()));
        requestParams.addBodyParameter("c_type", String.valueOf(car.getC_type()));
        requestParams.addBodyParameter("c_brand", String.valueOf(car.getC_brand()));
        requestParams.addBodyParameter("c_color", String.valueOf(car.getC_color()));
        requestParams.addBodyParameter("add_time", String.valueOf(car.getAdd_time()));
        requestParams.addBodyParameter("c_remark", String.valueOf(car.getC_remark()));
        request(XRequestCallBack, Tasks.CHANGE_CAR, "/updateCarInfo", requestParams,null);
    }

    /**
     * 设置用户默认地址
     *
     * @param uid
     * @param id  地址id
     */
    @Override
    public void setDefaultAddress(int uid, int id) {

    }

    /**
     * 设置用户默认车辆
     *
     * @param uid
     * @param id  车辆id
     */
    @Override
    public void setDefaultCar(int uid, int id) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("id", String.valueOf(id));
        request(XRequestCallBack, Tasks.SETDEFAULTCAR, "/setup_default_car", requestParams,null);
    }

    /**
     * 设置（新增，修改，找回）支付密码
     *
     * @param uid         用户ID	int	否
     * @param pay_pwd     旧支付密码	int	否
     * @param new_pay_pwd 新支付密码	int	否
     * @param staut       状态值	int	否	传0时候为第一次新增支付密码，新增支付密码不用传现支付密码，不是新增的时候不要传staut参数，
     */
    @Override
    public void setPayPassword(int uid, String pay_pwd, String new_pay_pwd, int staut) {

    }

    /**
     * 关于我们信息
     *
     * @param official_weixi     官方微信
     * @param official_weixi_url 官方微信地址
     * @param official_weibo     官方微博
     * @param official_weibo_url 官方微博地址
     * @param official_cor_sve   官方客服电话
     */
    @Override
    public void getAbout(String official_weixi, String official_weixi_url, String official_weibo, String official_weibo_url, String official_cor_sve) {

    }

    /**
     * 查询账户余额
     *
     * @param uid 用户id
     * @return AccountBalance.class
     */
    @Override
    public void getAccountBalance(int uid) {

        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GETACCOUNTBALANCE, "/pay_select", requestParams,null);
    }

    /**
     * 修改用户头像
     *
     * @param uid
     * @param u_img
     */
    @Override
    public void changeUserHead(int uid, File u_img) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        if (u_img != null) {
            requestParams.addBodyParameter("file", u_img);
        }
        request(XRequestCallBack, Tasks.CHANGEUSERHEAD, "/updateHeadImg", requestParams,null);
    }

    /**
     * 获取用户资金记录
     *
     * @param uid
     * @return PayRecord.class
     */
    @Override
    public void getPayRecords(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GETPAYRECORDS, "/user_pay_manage", requestParams,null);
    }

    @Override
    public void getTicketList(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.TICKET_LIST, "/userCoupons", requestParams,null);
    }

    /**
     * 查询抵用卷
     *
     * @param uid
     * @return Coupon.class
     */
    @Override
    public void getCoupons(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GETCOUPONS, "/userCoupons", requestParams,null);
    }

    /**
     * 获取优惠券使用信息
     *
     * @param uid
     * @return CouponRecord.class
     */
    @Override
    public void getCouponRecord(int uid) {

    }

    /**
     * 提交反馈
     *
     * @param uid
     * @param content
     */
    @Override
    public void suggestion(int uid, String content) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("content", String.valueOf(content));
        request(XRequestCallBack, Tasks.SUGGESTION, "/userFeedback", requestParams,null);
    }

    /**
     * 产品查询
     *
     * @return Product.class
     */
    @Override
    public void getProducts() {
        request(XRequestCallBack, Tasks.GETPRODUCTS, "/pro_select");
    }

    /**
     * 查询员工位置
     *
     * @retun EmployeeLocation.class
     */
    @Override
    public void locationEmployee() {

    }

    /**
     * 查询订单
     *
     * @param uid   用户ID	Int
     * @param state 查询状态值	String 传1查询进行中传2查询已完成，为空查询全部
     * @return Order.class
     */
    @Override
    public void getOrders(int uid, String state, int page) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("state", String.valueOf(state));
        requestParams.addBodyParameter("page", String.valueOf(page));
        request(XRequestCallBack, Tasks.GETORDERS, "/order_select", requestParams,null);
    }

    /**
     * 新订单
     *
     * @param order
     */
    @Override
    public void newOrder(Order order) {
        RequestParams requestParams = createRequestParams();

        requestParams.addBodyParameter("uid", String.valueOf(order.getUid()));
        requestParams.addBodyParameter("location", order.getLocation());
        requestParams.addBodyParameter("location_lg", order.getLocation_lg());
        requestParams.addBodyParameter("location_lt", order.getLocation_lt());
        requestParams.addBodyParameter("remark", order.getRemark());
        requestParams.addBodyParameter("p_ids", order.getP_ids());
        requestParams.addBodyParameter("total_price", String.valueOf(order.getTotal_price()));
        requestParams.addBodyParameter("order_reg_id", String.valueOf(order.getOrder_reg_id()));
        requestParams.addBodyParameter("plan_time", order.getPlan_time());
        requestParams.addBodyParameter("c_ids", order.getC_ids());
        requestParams.addBodyParameter("p_order_time_cid", String.valueOf(order.getP_order_time_cid()));
        requestParams.addBodyParameter("day", String.valueOf(order.getDay()));
        if (order.getFileVoice() != null) {
            requestParams.addBodyParameter("file", order.getFileVoice());
        }
        request(XRequestCallBack, Tasks.NEWORDER, "/createOrder", requestParams,null);

    }

    @Override
    public void getAddress(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GETADDRESS, "/selectAddress", requestParams,null);
    }

    @Override
    public void setAddress(Address address) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(address.getUid()));
        requestParams.addBodyParameter("address", address.getAddress());
        requestParams.addBodyParameter("address_lg", address.getAddress_lg());
        requestParams.addBodyParameter("address_lt", address.getAddress_lt());
        requestParams.addBodyParameter("address_info", address.getAddress_info());
        Log.i("Tag", "地址备注=>" + address.getAddress_info()+"address_lg="+address.getAddress_lg()+"address_lt="+address.getAddress_lt());
        requestParams.addBodyParameter("address_type", String.valueOf(address.getAddress_type()));

        request(XRequestCallBack, Tasks.SETADDRESS, "/AddAddress", requestParams,null);

    }

    @Override
    public void sendCode(String iphone, String code) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("iphone", String.valueOf(iphone));
        requestParams.addBodyParameter("code", String.valueOf(code));
        request(XRequestCallBack, Tasks.SEND_CODE, "/authCode", requestParams,null);
    }

    @Override
    public void getComment(int uid, int page) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("page", String.valueOf(page));
        request(XRequestCallBack, Tasks.GET_COMMENT, "/comment_select", requestParams,null);
    }

    @Override
    public void addComment(int uid, int emp_id, int order_id, String comment, float star) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("emp_id", String.valueOf(emp_id));
        requestParams.addBodyParameter("order_id", String.valueOf(order_id));
        requestParams.addBodyParameter("comment", String.valueOf(comment));
        requestParams.addBodyParameter("star", String.valueOf(star));
        request(XRequestCallBack, Tasks.ADD_COMMENT, "/comment_insert", requestParams,null);
    }

    @Override
    public void getTimeScope(long day) {
        Log.v("Tag", "tim scope");
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("day", String.valueOf(day));
        request(XRequestCallBack, Tasks.GET_TIME_SCOPE, "/appointTime", requestParams,null);
    }

    @Override
    public void getWashInfo(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.GEWASHCAR_DATA, "/washCoupons");
    }

    @Override
    public void getWashPrice() {
        request(XRequestCallBack, Tasks.GEWASHCAR_PRICE, "/washCoupons");
    }

    /**
     * 下单页面美容界面
     */
    @Override
    public void getBeautyService() {
        request(XRequestCallBack, Tasks.GETBEAUTY_SERVICE, "/productInfo");
    }

    @Override
    public void getDIYProduct() {
        request(XRequestCallBack, Tasks.GET_DIYPRODUCT, "/diy_select");
    }

    @Override
    public void confirmOrderInfo(ConfirmOrder confirmOrder) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", confirmOrder.getUserId());
        requestParams.addBodyParameter("location", confirmOrder.getCarAddress());
        requestParams.addBodyParameter("remark", confirmOrder.getReMark());
        requestParams.addBodyParameter("location_lg", confirmOrder.getCarLocateLg());
        requestParams.addBodyParameter("location_lt", confirmOrder.getCarLocateLt());
        requestParams.addBodyParameter("p_ids", confirmOrder.getProductId());
        requestParams.addBodyParameter("total_price", confirmOrder.getAllTotalPrice() + "");
        requestParams.addBodyParameter("c_ids", confirmOrder.getCarsId());
        if (confirmOrder.getCouponsId() != -1) {
            requestParams.addBodyParameter("coupons_id", confirmOrder.getCouponsId() + "");
        }
        if(confirmOrder.getAppointDay() != 0){
            requestParams.addBodyParameter("day", confirmOrder.getAppointDay()+"");
            requestParams.addBodyParameter("p_order_time_cid", confirmOrder.getAppointTimeId()+"");
            Log.i("Tag", "day=" + confirmOrder.getAppointDay() + "/p_order_time_cid="+confirmOrder.getAppointTimeId());
        }
        Log.i("Tag", "uid=" + confirmOrder.getUserId());
        Log.i("Tag", "location=" + confirmOrder.getCarAddress());
        Log.i("Tag", "location_lg=" + confirmOrder.getCarLocateLg());
        Log.i("Tag", "location_lt=" + confirmOrder.getCarLocateLt());
        Log.i("Tag", "p_ids=" + confirmOrder.getProductId());
        Log.i("Tag", "total_price=" + confirmOrder.getAllTotalPrice());
        Log.i("Tag", "c_ids=" + confirmOrder.getCarsId());
        Log.i("Tag", "coupons_id=" + confirmOrder.getCouponsId());
        if (!"".equals(confirmOrder.getReMark())) {
            requestParams.addBodyParameter("remark", confirmOrder.getReMark());
        }
        if (!"".equals(confirmOrder.getAppointDay())) {
            requestParams.addBodyParameter("day", confirmOrder.getAppointDay()+"");
        }
        if (confirmOrder.getAudioFile() != null) {
            requestParams.addBodyParameter("audio", confirmOrder.getAudioFile());
        }
        request(XRequestCallBack, Tasks.CONFIMORDER, "/createOrder", requestParams,null);
    }

    @Override
    public void getOrderDetail(int order_id) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("order_id", String.valueOf(order_id));
        request(XRequestCallBack, Tasks.ORDER_DETAIL, "/order_msg_select", requestParams,null);
    }

    @Override
    public void getCityLimitRule(String city_name) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("city_name", String.valueOf(city_name));
        request(XRequestCallBack, Tasks.GET_CITY_LIMIT_RULE, "/city_limit", requestParams,null);
    }

    @Override
    public void cancelOrder(int uid, int order_id) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("order_id", String.valueOf(order_id));
        request(XRequestCallBack, Tasks.CANCEL_ORDER, "/cancel_order", requestParams,null);
    }

    @Override
    public void complain(int uid, int order_id, int emp_id, String content) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("order_id", String.valueOf(order_id));
        requestParams.addBodyParameter("emp_id", String.valueOf(emp_id));
        requestParams.addBodyParameter("content", String.valueOf(content));
        request(XRequestCallBack, Tasks.COMPLAIN, "/complaint", requestParams,null);
    }

    @Override
    public void listMessage(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.LIST_MESSAGE, "/admin_msg_select", requestParams,null);
    }

    @Override
    public void getTopUpInfo(int uid, double money) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("money", String.valueOf(money));
        request(XRequestCallBack, Tasks.GET_TOP_UP_INFO, "/recharge", requestParams,null);
    }

    @Override
    public void getAllBrand() {
        request(XRequestCallBack, Tasks.GET_ALL_BRAND, "/carBrands");
    }

    @Override
    public void resetPswd(String iphone, String pwd) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("iphone", iphone);
        requestParams.addBodyParameter("pwd", pwd);
        request(XRequestCallBack, Tasks.REGISTER, "/register2", requestParams,null);
    }

    @Override
    public void sendResetPswdCode(String iphone, String code) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("iphone", String.valueOf(iphone));
        requestParams.addBodyParameter("code", String.valueOf(code));
        request(XRequestCallBack, Tasks.SEND_CODE, "/re_iphone2", requestParams,null);
    }

    @Override
    public void getMyIntegral(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.INTEGRAL, "/mypoints_select", requestParams,null);
    }

    @Override
    public void getVersionInfo(int version_type) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("version_type", String.valueOf(version_type));
        request(XRequestCallBack, Tasks.VERSION_INFO, "/version_up", requestParams,null);
    }

    @Override
    public void withdraw(int uid, double money) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("money", String.valueOf(money));
        request(XRequestCallBack, Tasks.WITHDRAW, "/applyfor", requestParams,null);
    }

    @Override
    public void commitInvalidOrderInfo(int uid, String city, String location, String lt, String lg) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("city", String.valueOf(location));
        requestParams.addBodyParameter("location", String.valueOf(location));
        requestParams.addBodyParameter("location_lt", String.valueOf(lt));
        requestParams.addBodyParameter("location_lg", String.valueOf(lg));
        request(XRequestCallBack, Tasks.COMMIT_INVALID_ORDER, "/order_notopen", requestParams,null);
    }

    @Override
    public void applyOpenCity(int uid, String latitude, String longitude, String location) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("latitude", String.valueOf(latitude));
        requestParams.addBodyParameter("longitude", String.valueOf(longitude));
        requestParams.addBodyParameter("address", location);
        request(XRequestCallBack, Tasks.APPLYOPENCITY, "/applyopne", requestParams,null);
    }

    @Override
    public void getMyOrderList(int uid, int pageIndex, int pageSize) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("p", String.valueOf(pageIndex));
        requestParams.addBodyParameter("pagesize", String.valueOf(pageSize));
        Log.v("Tag", "uid:" + uid);
        request(XRequestCallBack, Tasks.ORDER_LIST, "/orderSelect", requestParams,""+uid);
    }

    @Override
    public void sendComment(String orderId, int uid, String comment, int level) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("orderid", orderId);
        requestParams.addBodyParameter("evaluate", comment);
        requestParams.addBodyParameter("star", String.valueOf(level));
        request(XRequestCallBack, Tasks.ORDER_COMMENT, "/evaluate", requestParams,null);
    }

    @Override
    public void getOrderInformation(String orderId) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("orderid", orderId);
        request(XRequestCallBack, Tasks.ORDER_INFO, "/orderDetail", requestParams,orderId);
    }

    @Override
    public void cancelOrder(String orderId,int uid) {
        Log.v("Tag","orderid:"+orderId+" uid:"+uid);
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("orderid", orderId);
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.CANCEL_ORDER, "/orderCancel", requestParams,orderId);
    }

    @Override
    public void deleteOrder(String orderId) {
//        RequestParams requestParams = createRequestParams();
//        requestParams.addBodyParameter("orderid", orderId);
//        request(XRequestCallBack, Tasks.DELETE_ORDER, "/orderDelete", requestParams);

    }

    @Override
    public void confirmPay(String orderId, int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("orderid", orderId);
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.CONFIRM_PAY, "/confirmPay", requestParams,orderId);
    }

    @Override
    public void getMyCouponInfo(int uid) {
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        request(XRequestCallBack, Tasks.MY_COUPONS, "/couponsInfo", requestParams,null);
    }

    @Override
    public void exchangeCoupon(int uid, String couponCode) {
        Log.v("XHttpRequest","uid:"+uid+" code:"+couponCode);
        RequestParams requestParams = createRequestParams();
        requestParams.addBodyParameter("uid", String.valueOf(uid));
        requestParams.addBodyParameter("exchnum", couponCode);
        request(XRequestCallBack, Tasks.EXCHANGE_COUPON, "/exchangeCoupons", requestParams,null);
    }

}
