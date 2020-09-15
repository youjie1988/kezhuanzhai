package com.huaheng.mobilewms.https;

import com.huaheng.mobilewms.bean.ApkInfo;
import com.huaheng.mobilewms.bean.CompanyInfo;
import com.huaheng.mobilewms.bean.DictData;
import com.huaheng.mobilewms.bean.GoodsInfo;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.InventoryDetails;
import com.huaheng.mobilewms.bean.InventoryTransaction;
import com.huaheng.mobilewms.bean.Location;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.MaterialInfo;
import com.huaheng.mobilewms.bean.MaterialType;
import com.huaheng.mobilewms.bean.Materialforecast;
import com.huaheng.mobilewms.bean.MobileInventory;
import com.huaheng.mobilewms.bean.MobileTask;
import com.huaheng.mobilewms.bean.ModulesBean;
import com.huaheng.mobilewms.bean.PickingResult;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptInfo;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.bean.Shipment;
import com.huaheng.mobilewms.bean.ShipmentHeader;
import com.huaheng.mobilewms.bean.ShipmentMaterialDetail;
import com.huaheng.mobilewms.bean.ShipmentType;
import com.huaheng.mobilewms.bean.TaskDetailBean;
import com.huaheng.mobilewms.bean.TaskHeader;
import com.huaheng.mobilewms.bean.TodayInfo;
import com.huaheng.mobilewms.bean.TokenBean;
import com.huaheng.mobilewms.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface HttpService {

    //region 公共接口
    /**
     * 登录
     * @param body
     * @return
     */
    @POST(HttpConstant.LOGIN)
    Observable<ApiResponse<ArrayList<UserBean>>> login(@Body RequestBody body);

    /**
     * 登录
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_TOKEN)
    Observable<ApiResponse<String>> getToken(@Body RequestBody body);

    /**
     * 获得用户当前菜单
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_MODULES)
    Observable<ApiResponse<ArrayList<ModulesBean>>> getModules(@Body RequestBody body);

    /**
     * 心跳
     * @param body
     * @return
     */
    @POST(HttpConstant.HEART_BEAT)
    Observable<ApiResponse<String>> heartBeat(@Body RequestBody body);

    /**
     * 获取公司信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_COMPANY_INFO)
    Observable<ApiResponse<ArrayList<CompanyInfo>>> getCompanyInfo(@Body RequestBody body);

    /**
     * 更新apk的地址
     * @param body
     * @return
     */
    @POST(HttpConstant.DOWNLOAD)
    Observable<ApiResponse<ApkInfo>> getUpdateApkInfo(@Body RequestBody body);
    //endregion


    //region 入库接口
    /**
     * 入库单号
     * @param body
     * @return
     */
    @POST(HttpConstant.SCAN_BILL)
    Observable<ApiResponse<ArrayList<ReceiptInfo>>> scanBill(@Body RequestBody body);

    /**
     * 检测容器号
     * @param body
     * @return
     */
    @POST(HttpConstant.SCAN_CONTAINER)
    Observable<ApiResponse<String>> scanContainer(@Body RequestBody body);

    /**
     * 获取物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_MATERIAL)
    Observable<ApiResponse<MaterialInfo>> getMaterial(@Body RequestBody body);

    /**
     * 查询物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_MATERIAL)
    Observable<ApiResponse<List<Material>>> findMaterial(@Body RequestBody body);

    /**
     * 自动生成物料编码
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_MATERIAL_CODE)
    Observable<ApiResponse<String>> createMaterialCode(@Body RequestBody body);


    /**
     * 校验库位信息
     * @param body
     * @return
     */
    @POST(HttpConstant.CHECK_LOCATION)
    Observable<ApiResponse<String>> checkLocation(@Body RequestBody body);

    /**
     * 保存快速收货信息
     * @param body
     * @return
     */
    @POST(HttpConstant.QUICK_SAVE_GOODS)
    Observable<ApiResponse<String>> quickSaveGoods(@Body RequestBody body);

    /**
     * 保存逐次收货单号
     * @param body
     * @return
     */
    @POST(HttpConstant.SAVE_BILL_BY_PIECE)
    Observable<ApiResponse<GoodsInfo>> saveBillByPiece(@Body RequestBody body);

    /**
     * 保存批量收货单号
     * @param body
     * @return
     */
    @POST(HttpConstant.SAVE_BILL_BY_BULK)
    Observable<ApiResponse<String>> saveBillByBulk(@Body RequestBody body);

    /**
     * 保存批量收货单号
     * @param body
     * @return
     */
    @POST(HttpConstant.REPLNISH_WAREHOUSE)
    Observable<ApiResponse<List<GoodsInfo>>> replenishWarehouse(@Body RequestBody body);

    /**
     * 保存批量收货单号
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_CONTAINERCODE)
    Observable<ApiResponse<String>> getContainerCode(@Body RequestBody body);

    /**
     * 容器编码扫描接口
     * @param body
     * @return
     */
    @POST(HttpConstant.SCAN_BARCODE_ADD)
    Observable<ApiResponse<String>> scanBarcodeAdd(@Body RequestBody body);

    /**
     * 获取盘点任务
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_TRANSFER_BY_CONTAINER_CODE)
    Observable<ApiResponse<List<MobileTask>>> findTrasnfer(@Body RequestBody body);

    /**
     * 实盘登记
     * @param body
     * @return
     */
    @POST(HttpConstant.CONFIRM_GAP_QTY)
    Observable<ApiResponse<String>> confirmGapQty(@Body RequestBody body);

    /**
     * 呼叫料盒
     * @param body
     * @return
     */
    @POST(HttpConstant.CALL_BOX)
    Observable<ApiResponse<String>> callBox(@Body RequestBody body);

    /**
     * 移动端查询入库单
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_RECEIPT)
    Observable<ApiResponse<Receipt>> findReceipt(@Body RequestBody body);

    /**
     * 移动端收货
     * @param body
     * @return
     */
    @POST(HttpConstant.QUICK_RECEIPT)
    Observable<ApiResponse<String>> quickReceipt(@Body RequestBody body);

    /**
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_RECEIPT_CODE)
    Observable<ApiResponse<String>> createReceiptCode(@Body RequestBody body);

    /**
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_RECEIPT)
    Observable<ApiResponse<String>> createReceipt(@Body RequestBody body);


    /**
     * 移动端收货
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_LATEST_SHIPMENT)
    Observable<ApiResponse<String>> getLatestShipment(@Body RequestBody body);

    /**
     * 移动端收货
     * @param body
     * @return
     */
    @POST(HttpConstant.LIST_RECEIPT)
    Observable<ApiResponse<String>> listReceipt(@Body RequestBody body);

    /**
     * 移动端搜索7天内入库单
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_RECEIPT)
    Observable<ApiResponse<List<ReceiptHeader>>> searchReceipt(@Body RequestBody body);

    /**
     * 移动端按条件搜索入库单
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_RECEIPT_IN_CONDITION)
    Observable<ApiResponse<List<ReceiptHeader>>> searchReceiptInCondition(@Body RequestBody body);

    /**
     * 移动端收货
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_RECEIPT_TYPE)
    Observable<ApiResponse<List<ReceiptType>>> getReceiptType(@Body RequestBody body);

    /**
     * 获取物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_LATEST_MATERIAL)
    Observable<ApiResponse<List<Material>>> getLatestMaterial(@Body RequestBody body);

    /**
     * 获取物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_LATEST_RECEIPT)
    Observable<ApiResponse<List<ReceiptHeader>>> getLatestReceipt(@Body RequestBody body);

    /**
     * 移动端查询出库单
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_SHIPMENT)
    Observable<ApiResponse<Shipment>> findShipment(@Body RequestBody body);

    /**
     * 移动端查询出库单
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_SHIPMENT)
    Observable<ApiResponse<List<ShipmentHeader>>> searchShipment(@Body RequestBody body);

    /**
     * 移动端查询出库单
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_SHIPMENT_IN_CONDITION)
    Observable<ApiResponse<List<ShipmentHeader>>> searchShipmentInCondition(@Body RequestBody body);

    /**
     * 移动端收货
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_SHIPMENT_TYPE)
    Observable<ApiResponse<List<ShipmentType>>> getShipmentType(@Body RequestBody body);

    /**
     * 移动端自动配盘
     * @param body
     * @return
     */
    @POST(HttpConstant.AUTO_COMBINATION)
    Observable<ApiResponse<List<Integer>>> autoCombination(@Body RequestBody body);

    /**
     * @return
     */
    @POST(HttpConstant.CREATE_SHIPMENT_CODE)
    Observable<ApiResponse<String>> createShipmentCode(@Body RequestBody body);

    /**
     * @return
     */
    @POST(HttpConstant.CREATE_SHIPMENT)
    Observable<ApiResponse<String>> createShipment(@Body RequestBody body);

    /**
     * 移动端手动配盘
     * @param body
     * @return
     */
    @POST(HttpConstant.COMBINATION)
    Observable<ApiResponse<List<Integer>>> combination(@Body RequestBody body);

    /**
     * 移动端创建出库任务
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_SHIPMENT_TASK)
    Observable<ApiResponse<List<Integer>>> createShipmentTask(@Body RequestBody body);

    /**
     * 上架确定容器号
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_RECEIPT_TASK)
    Observable<ApiResponse<String>> createReceiptTask(@Body RequestBody body);

    /**
     * 上架确定位置
     * @param body
     * @return
     */
    @POST(HttpConstant.PUTAWAY_SCAN_LOCATION)
    Observable<ApiResponse<String>> scanLocationCode(@Body RequestBody body);

    /**
     * 上架确定位置
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_REPLENISH_TASK)
    Observable<ApiResponse<Integer>> createReplenishTask(@Body RequestBody body);

    /**
     * 上架确定位置
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_QUICK_TASK)
    Observable<ApiResponse<Integer>> createQuickTask(@Body RequestBody body);

    /**
     * 上架确定位置
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_LOCATION_FROM_CONTAINER)
    Observable<ApiResponse<String>> getLocationFromContainer(@Body RequestBody body);

    /**
     * 获得后端字典数据
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_DICT_LIST_DATA)
    Observable<ApiResponse<List<DictData>>> getDictListData(@Body RequestBody body);
    //endregion


    //region 出库接口
    /**
     * 获得立库任务信息
     * @param code
     * @return
     */
    @GET(HttpConstant.TASK_ASRS_DETAILS)
    Observable<ApiResponse<TaskDetailBean>> getTaskDetails(@Query("code") String code);


    /**
     * 完成立库任务
     * @param body
     * @return
     */
    @POST(HttpConstant.TASK_ASRS_COMPLETE)
    Observable<ApiResponse<List<PickingResult>>> completeTaskDetails(@Body RequestBody body);

    /**
     * 完成立库任务
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_MATERIAL_FORECAST)
    Observable<ApiResponse<List<Materialforecast>>> getMaterialForecast(@Body RequestBody body);

    /**
     * 自动配盘
     * @param body
     * @return
     */
    @POST(HttpConstant.AUTO_SHIPMENT)
    Observable<ApiResponse<List<Integer>>> autoShipment(@Body RequestBody body);

    /**
     * 配盘
     * @param body
     * @return
     */
    @POST(HttpConstant.SHIPMENT)
    Observable<ApiResponse<List<Integer>>> shipment(@Body RequestBody body);

    /**
     * 生成出库任务
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_SHIP_TASK)
    Observable<ApiResponse<List<Integer>>> createShipTask(@Body RequestBody body);

    /**
     * 生成分拣出库任务
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_SORT_SHIP_TASK)
    Observable<ApiResponse<List<Integer>>> createSortShipTask(@Body RequestBody body);
    //endregion

    /**
     * 查找任务
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_TASK)
    Observable<ApiResponse<List<TaskHeader>>> searchTask(@Body RequestBody body);

    /**
     * 查找任务
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_TASK_IN_CONDITION)
    Observable<ApiResponse<List<TaskHeader>>> searchTaskInCondition(@Body RequestBody body);

    /**
     * 查找
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_TASK)
    Observable<ApiResponse<MobileTask>> findTask(@Body RequestBody body);
    //endregion

    //region 库存接口
    /**
     * 获得库存信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_INVENTORY_INFO)
    Observable<ApiResponse<ArrayList<MobileInventory>>> getInventoryInfo(@Body RequestBody body);

    /**
     * 获得库存理想词
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_INVENTORY_FORECAST)
    Observable<ApiResponse<ArrayList<String>>> getInventoryForecast(@Body RequestBody body);

    /**
     * 获得库存理想词
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_INVENTORY)
    Observable<ApiResponse<List<InventoryDetail>>> searchInventory(@Body RequestBody body);

    /**
     * 获得库存理想词
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_INVENTORY_IN_CONDITION)
    Observable<ApiResponse<List<InventoryDetail>>> searchInventoryInCondition(@Body RequestBody body);

    /**
     * 查询物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_MATERIAL_IN_CONDITION)
    Observable<ApiResponse<List<Material>>> searchMaterialInCondition(@Body RequestBody body);

    /**
     * 新增物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.ADD_MATERIAL)
    Observable<ApiResponse<String>> addMaterial(@Body RequestBody body);

    /**
     * 新增物料信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_MATERIAL_TYPE)
    Observable<ApiResponse<List<MaterialType>>> getMaterialType(@Body RequestBody body);


    /**
     * 获得库存理想词
     * @param body
     * @return
     */
    @POST(HttpConstant.FIND_INVENTORY)
    Observable<ApiResponse<InventoryDetail>> findInvneotry(@Body RequestBody body);

    /**
     * 获得库存理想词
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_TODAY_INFO)
    Observable<ApiResponse<TodayInfo>> searchTodayInfo(@Body RequestBody body);

    /**
     * 创建出库查看
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_CHECK_OUTTASK)
    Observable<ApiResponse<List<Integer>>> createCheckOutTask(@Body RequestBody body);

    /**
     * 创建移库任务
     * @param body
     * @return
     */
    @POST(HttpConstant.LIBRARY_TRANSFER)
    Observable<ApiResponse<Integer>>  transfer(@Body RequestBody body);

    /**
     * 执行任务
     * @param body
     * @return
     */
    @POST(HttpConstant.EXECUTE_TASK)
    Observable<ApiResponse<String>> execute(@Body RequestBody body);

    /**
     * 执行任务列表
     * @param body
     * @return
     */
    @POST(HttpConstant.EXECUTE_TASK_LIST)
    Observable<ApiResponse<String>> executeList(@Body RequestBody body);

    /**
     * 判断是不是库位
     * @param body
     * @return
     */
    @POST(HttpConstant.IS_LOCATION)
    Observable<ApiResponse<String>> isLocation(@Body RequestBody body);

    /**
     * 判断是不是库位
     * @param body
     * @return
     */
    @POST(HttpConstant.IS_CONTAINER)
    Observable<ApiResponse<String>> isContainer(@Body RequestBody body);


    /**
     * 获取库位信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_LOCATION)
    Observable<ApiResponse<ArrayList<String>>> getLocation(@Body RequestBody body);

    /**
     * 完成任务
     * @param body
     * @return
     */
    @POST(HttpConstant.COMPLETE_TASK_BYWMS)
    Observable<ApiResponse<String>> completeTaskByWMS(@Body RequestBody body);

    /**
     * 完成批量任务
     * @param body
     * @return
     */
    @POST(HttpConstant.COMPLETE_TASK_LIST_BYWMS)
    Observable<ApiResponse<String>> completeTaskListByWMS(@Body RequestBody body);


    /**
     * 空托入库
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_EMPRY_IN)
    Observable<ApiResponse<Integer>> createEmptyIn(@Body RequestBody body);

    /**
     * 空托出库
     * @param body
     * @return
     */
    @POST(HttpConstant.CREATE_EMPRY_OUT)
    Observable<ApiResponse<Integer>> createEmptyOut(@Body RequestBody body);

    /**
     * 获得过去7天的收货和发货信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_7DAYS_SHIPMENT)
    Observable<ApiResponse<List<InventoryDetails>>> get7daysShipment(@Body RequestBody body);

    /**
     * 获得过去7天的收货和发货信息
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_TODAY_SHIPMENT_DETAILS)
    Observable<ApiResponse<List<ShipmentMaterialDetail>>> getTodayShipmentDetail(@Body RequestBody body);

    /**
     *  拣选台回库
     * @param body
     * @return
     */
    @POST(HttpConstant.PICKING_RETURN)
    Observable<ApiResponse<String>> pickingReturn(@Body RequestBody body);

    /**
     * @param body
     * @return
     */
    @POST(HttpConstant.PICK_LOCATION)
    Observable<ApiResponse<List<Location>>> pickLocation(@Body RequestBody body);

    /**
     * @param body
     * @return
     */
    @POST(HttpConstant.GET_EMPTY_CONTAINER_INLOCATION)
    Observable<ApiResponse<List<Location>>> getEmptyContainerInLocation(@Body RequestBody body);
    //endregion

    /**
     * 获得库存理想词
     * @param body
     * @return
     */
    @POST(HttpConstant.SEARCH_INVENTORY_TRANSACTION_IN_CONDITION)
    Observable<ApiResponse<List<InventoryTransaction>>> searchInventoryTransactionInCondition(@Body RequestBody body);


}
