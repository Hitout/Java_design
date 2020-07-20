// package com.tmp;
//
// import com.fpx.common.util.StringUtils;
// import com.fpx.ironforge.reconciliation.analyse.constant.*;
// import com.fpx.ironforge.reconciliation.analyse.dto.*;
// import com.fpx.ironforge.reconciliation.analyse.dto.no.ManagementRequestDTO;
// import com.fpx.ironforge.reconciliation.analyse.dto.no.PayableRequestDTO;
// import com.fpx.ironforge.reconciliation.analyse.dto.no.PayableRequestDetailDTO;
// import com.fpx.ironforge.reconciliation.analyse.dto.singlefee.ManagementSingleFeeDTO;
// import com.fpx.ironforge.reconciliation.analyse.dto.singlefee.PayableSingleFeeDTO;
// import com.fpx.ironforge.reconciliation.analyse.dto.singlefee.SingleFeeCheckResultReCheckRequestParam;
// import com.fpx.ironforge.reconciliation.analyse.entity.SingleFeeCheckResult;
// import com.fpx.ironforge.reconciliation.analyse.mapper.ISingleFeeCheckResultMapper;
// import com.fpx.ironforge.reconciliation.analyse.service.ISingleFeeCheckResultService;
// import com.fpx.ironforge.reconciliation.analyse.service.ISingleFeeCheckService;
// import com.fpx.ironforge.reconciliation.analyse.service.ISupplierBillCheckRecordService;
// import com.fpx.ironforge.reconciliation.analyse.service.api.IManagementWrapperService;
// import com.fpx.ironforge.reconciliation.analyse.service.api.IPayableWrapperService;
// import com.fpx.ironforge.reconciliation.analyse.util.BigDecimalUtil;
// import com.fpx.ironforge.reconciliation.analyse.util.DateUtils;
// import com.fpx.ironforge.reconciliation.analyse.util.ThreadPoolExecutorUtils;
// import com.fpx.ironforge.reconciliation.multithread.executor.*;
// import com.fpx.ironforge.reconciliation.multithread.executor.exception.MultiThreadModeExecuteException;
// import com.fpx.ironforge.reconciliation.multithread.executor.model.GlobalTaskResult;
// import com.fpx.ironforge.reconciliation.multithread.executor.model.HandleDataTask;
// import com.fpx.ironforge.reconciliation.multithread.executor.model.PrefetchDataTask;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.collections.CollectionUtils;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import tk.mybatis.mapper.entity.Example;
// import tk.mybatis.mapper.entity.Example.Criteria;
//
// import javax.annotation.PreDestroy;
// import javax.annotation.Resource;
// import java.math.BigDecimal;
// import java.util.*;
// import java.util.concurrent.LinkedBlockingDeque;
// import java.util.concurrent.ThreadPoolExecutor;
// import java.util.concurrent.TimeUnit;
// import java.util.function.Function;
// import java.util.stream.Collectors;
//
// /**
//  * 单费用项对账业务逻辑实现类
//  * @author yanguangx
//  * @date 2019/12/4
//  */
// @Slf4j
// @Service
// public class SingleFeeCheckServiceImpl implements ISingleFeeCheckService {
//
//     private static final Integer PAGE_SIZE = 500;
//
//     @Resource
//     private ISingleFeeCheckResultMapper singleFeeCheckResultMapper;
//     @Resource
//     private IManagementWrapperService managementWrapperService;
//     @Resource
//     private IPayableWrapperService payableWrapperService;
//     @Resource
//     private ISupplierBillCheckRecordService supplierBillCheckRecordService;
//     @Resource
//     private ISingleFeeCheckResultService singleFeeCheckResultService;
//
//     @Value("${reCheck.forceUpdate:}")
//     private Boolean forceUpdate;
//
//     /** 首次对账线程池 */
//     private static final ThreadPoolExecutor CHECK_FETCH_DATA_EXECUTOR = ThreadPoolExecutorUtils.newExecutor("check-fee-prefetch-data-", 3, 3, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(256));
//     private static final ThreadPoolExecutor CHECK_HANDLE_DATA_EXECUTOR = ThreadPoolExecutorUtils.newExecutor("check-fee-handle-data-", 1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(16));
//
//     /** 重对账线程池 */
//     private static final ThreadPoolExecutor RECHECK_FETCH_DATA_EXECUTOR = ThreadPoolExecutorUtils.newExecutor("recheck-fee-prefetch-data-", 3, 3, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(256));
//     private static final ThreadPoolExecutor RECHECK_HANDLE_DATA_EXECUTOR = ThreadPoolExecutorUtils.newExecutor("recheck-fee-handle-data-", 5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(8));
//
//     /** 首次对账线程任务执行器 */
//     private static final MultiThreadModeExcecutor<SingleFeeCheckCompareDTO> CHECK_EXECUTOR = new DefaultMultiThreadModeExcecutor<>();
//     /** 重对账线程任务执行器 */
//     private static final MultiThreadModeExcecutor<SingleFeeReCheckCompareDTO> RECHECK_EXECUTOR = new DefaultMultiThreadModeExcecutor<>();
//
//     @PreDestroy
//     private void destroy() {
//         ThreadPoolExecutorUtils.shutdownExecutor(RECHECK_FETCH_DATA_EXECUTOR);
//         ThreadPoolExecutorUtils.shutdownExecutor(RECHECK_HANDLE_DATA_EXECUTOR);
//         ThreadPoolExecutorUtils.shutdownExecutor(CHECK_FETCH_DATA_EXECUTOR);
//         ThreadPoolExecutorUtils.shutdownExecutor(CHECK_HANDLE_DATA_EXECUTOR);
//     }
//
//     @Override
//     public void check(CheckParamDTO param) {
//         //1.拆分和抓取数据任务
//         //1.1 获取账单订单数量，计算出总页数
//         Integer total = managementWrapperService.getItemCount(param);
//         int totalPageNum = total % PAGE_SIZE == 0 ? total / PAGE_SIZE : total / PAGE_SIZE + 1;
//         //1.2 根据页数拆分抓取数据任务
//         List<PrefetchDataTask<SingleFeeCheckCompareDTO>> prefetchDataTasks = new ArrayList<>();
//         for (int i = 1; i <= totalPageNum; i++) {
//             int currentPageNum = i;
//             prefetchDataTasks.add(() -> checkPrefetchData(param, currentPageNum));
//         }
//
//         //2.对账任务
//         HandleDataTask<SingleFeeCheckCompareDTO> handleDataTask = (data, billFid) -> {
//             checkHandleData(data);
//             return null;
//         };
//
//         //3.后置处理，更新对账进度
//         IHandleDataPostHandler<SingleFeeCheckCompareDTO> postHandler = (data, billFid, progress, returnHandleDataResult) -> {
//             log.info("更新对账进度：" + (float)progress.getCurrentHandled()/progress.getTotal());
//             supplierBillCheckRecordService.updateProgress(progress.getTotal(), progress.getCurrentHandled(), (Long) billFid);
//             return null;
//         };
//
//         //4.全局任务后置处理器，待所有数据处理任务完成后将调用该方法
//         IGlobalTaskPostHandler globalTaskPostHandler = new IGlobalTaskPostHandler() {
//             @Override
//             public Object postForSuccess(Object o, GlobalTaskResult globalTaskResult) {
//                 return null;
//             }
//
//             @Override
//             public Object postForFail(Object o, GlobalTaskResult globalTaskResult, MultiThreadModeExecuteException exception) {
//                 throw exception;
//             }
//         };
//
//         //5.构造任务参数
//         IGlobalTaskBuilder<SingleFeeCheckCompareDTO> globalTaskBuilder = new GlobalTaskBuilder<>();
//         globalTaskBuilder.prefetchDataExecutor(CHECK_FETCH_DATA_EXECUTOR).handleDataExecutor(CHECK_HANDLE_DATA_EXECUTOR)
//                 .prefetchDataTasks(prefetchDataTasks)
//                 .handleDataTask(handleDataTask)
//                 .globalParamater(param.getBillFid())
//                 .handleDataPostHandler(postHandler)
//                 .globalTaskPostHandler(globalTaskPostHandler);
//
//         //6.提交任务，等待任务完成
//         CHECK_EXECUTOR.submit(globalTaskBuilder);
//
//         //防止并发更新进度丢失修改，再次更新对账进度
//         supplierBillCheckRecordService.updateProgress(totalPageNum, totalPageNum, param.getBillFid());
//     }
//
//     /**
//      * 对账
//      * @param data 对账数据
//      */
//     private void checkHandleData(SingleFeeCheckCompareDTO data) {
//         List<SingleFeeCheckResult> resultList = new ArrayList<>();
//         // 比对
//         data.getManagementSingleFeeDTOList().forEach(managementSingleFeeDTO -> {
//             PayableSingleFeeDTO payableSingleFeeDTO = data.getBillItemIdPayableMap().get(managementSingleFeeDTO.getBillItemId());
//             SingleFeeCheckResult singleFeeCheckResult = compare(managementSingleFeeDTO, payableSingleFeeDTO);
//             resultList.add(singleFeeCheckResult);
//         });
//
//         if (CollectionUtils.isNotEmpty(resultList)) {
//             singleFeeCheckResultMapper.insertBatchSelective(resultList);
//         }
//     }
//
//     private SingleFeeCheckResult compare(ManagementSingleFeeDTO managementSingleFeeDTO, PayableSingleFeeDTO payableSingleFeeDTO) {
//         SingleFeeCheckResult result = new SingleFeeCheckResult();
//         result.setBillFid((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getBillFid())?managementSingleFeeDTO.getBillFid():0L);
//         result.setBillItemId((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getBillItemId())?managementSingleFeeDTO.getBillItemId():0L);
//         result.setSupplierCode((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierCode())?managementSingleFeeDTO.getSupplierCode():"");
//         result.setSupplierNo((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierNo())?managementSingleFeeDTO.getSupplierNo():"");
//         result.setSupplierServiceCode((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierServiceCode())?managementSingleFeeDTO.getSupplierServiceCode():"");
//         result.setSupplierServiceName((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierServiceName())?managementSingleFeeDTO.getSupplierServiceName():"");
//         result.setSupplierAccountNumber((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierAccountNumber())?managementSingleFeeDTO.getSupplierAccountNumber():"");
//         result.setSupplierInvoiceNumber((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierInvoiceNumber())?managementSingleFeeDTO.getSupplierInvoiceNumber():"");
//         result.setSupplierInvoiceDate((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierInvoiceDate())?managementSingleFeeDTO.getSupplierInvoiceDate():DateUtils.buildEmptyDate(DateUtils.TIME_ZONE_CHAINA));
//         result.setSupplierBusinessDate((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierBusinessDate())?managementSingleFeeDTO.getSupplierBusinessDate():DateUtils.buildEmptyDate(DateUtils.TIME_ZONE_CHAINA));
//         result.setSupplierCustomerWaybillNo((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierCustomerWaybillNo())?managementSingleFeeDTO.getSupplierCustomerWaybillNo():"");
//         result.setSupplierOriginCountry((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierOriginCountry())?managementSingleFeeDTO.getSupplierOriginCountry():"");
//         result.setSupplierDestinationCountry((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierDestinationCountry())?managementSingleFeeDTO.getSupplierDestinationCountry():"");
//         result.setSupplierPieces((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierPieces())?managementSingleFeeDTO.getSupplierPieces():0);
//         result.setSupplierWeight((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierWeight())?managementSingleFeeDTO.getSupplierWeight():BigDecimal.ZERO);
//         result.setSupplierWeightUnit((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierWeightUnit())?managementSingleFeeDTO.getSupplierWeightUnit():"");
//         result.setSupplierCurrencyCode((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierCurrencyCode())?managementSingleFeeDTO.getSupplierCurrencyCode():"");
//         result.setSupplierAccountPeriod((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierAccountPeriod())?managementSingleFeeDTO.getSupplierAccountPeriod():"");
//         result.setSupplierFeeName((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getSupplierFeeName())?managementSingleFeeDTO.getSupplierFeeName():"");
//         result.setSupplierFeeAmount((null != managementSingleFeeDTO && null != managementSingleFeeDTO.getSupplierFeeAmount()) ? managementSingleFeeDTO.getSupplierFeeAmount() : BigDecimal.ZERO);
//         result.setFpxFeeName((null!=managementSingleFeeDTO&&null!=managementSingleFeeDTO.getFpxFeeName())?managementSingleFeeDTO.getFpxFeeName():"");
//         result.setFpxFeeCode((null != managementSingleFeeDTO && null != managementSingleFeeDTO.getFpxFeeCode()) ? managementSingleFeeDTO.getFpxFeeCode() : "");
//
//         buildPayableResult(payableSingleFeeDTO, result);
//
//         buildCompareResult(payableSingleFeeDTO, result);
//
//         result.setCreatedBy("sys");
//         return result;
//     }
//
//     private void buildCompareResult(PayableSingleFeeDTO payableSingleFeeDTO, SingleFeeCheckResult result) {
//         BigDecimal diffWeight = result.getSupplierWeight().subtract(result.getFpxWeight());
//         result.setDiffWeight(diffWeight);
//         BigDecimal diffFeeVal = result.getSupplierFeeAmount().subtract(result.getFpxFeeAmount());
//         result.setDiffFeeVal(diffFeeVal);
//
//         // 对账成功：应付存在该订单->该订单已计费->存在该费用项
//         String checkStatus = (null != payableSingleFeeDTO
//                 && EnumPayableNoStatus.BC.getCode().equals(payableSingleFeeDTO.getStatus())
//                 && null != payableSingleFeeDTO.getPayableFeeId())
//                 ? EnumCheckStatus.S.name() : EnumCheckStatus.F.name();
//         result.setCheckStatus(checkStatus);
//         // 记录对账备注：应付无此单号 || 存在该订单->已计费->（应付无此费用项 || 有此费用项->应付单号XXX）
//         String checkRemark = null == payableSingleFeeDTO ? EnumNoCheckResultErrorCode.NOTFOUND.getCode()
//                 : EnumPayableNoStatus.BC.getCode().equals(payableSingleFeeDTO.getStatus()) ? (null != payableSingleFeeDTO.getPayableFeeId()) ? ""
//                 : EnumNoCheckResultErrorCode.FEE_NOTFOUND.getCode()
//                 : EnumNoCheckResultErrorCode.valueOf(payableSingleFeeDTO.getStatus()).getCode();
//         result.setCheckRemark(checkRemark);
//         // 对账结果状态正常：对账成功且费用项无差异
//         result.setCheckResultStatus((EnumCheckStatus.S.name().equals(checkStatus) && BigDecimalUtil.compareTo(diffFeeVal, BigDecimal.ZERO)) ? EnumCheckResultStatus.Y.name() : EnumCheckResultStatus.E.name());
//     }
//
//     private void buildPayableResult(PayableSingleFeeDTO payableSingleFeeDTO, SingleFeeCheckResult result) {
//         result.setPayableId((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getPayableId()) ? payableSingleFeeDTO.getPayableId() : 0);
//         result.setPayableFeeId((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getPayableFeeId()) ? payableSingleFeeDTO.getPayableFeeId() : 0);
//         result.setFpxChannelMnemonic((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getFpxChannelMnemonic()) ? payableSingleFeeDTO.getFpxChannelMnemonic():"");
//         result.setFpxBusinessNo((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getBusinessNo()) ? payableSingleFeeDTO.getBusinessNo():"");
//         result.setFpxPieces((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getItemQty()) ? payableSingleFeeDTO.getItemQty():0);
//         result.setFpxWeight((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getFpxWeight()) ? payableSingleFeeDTO.getFpxWeight(): BigDecimal.ZERO);
//         result.setFpxBusinessDate((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getBusinessDate()) ? payableSingleFeeDTO.getBusinessDate() : DateUtils.buildEmptyDate(DateUtils.TIME_ZONE_CHAINA));
//         result.setFpxDestinationCountry((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getFpxDestCode()) ? payableSingleFeeDTO.getFpxDestCode() : "");
//         result.setFpxCustomerWaybillNo((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getDeliveryCode()) ? payableSingleFeeDTO.getDeliveryCode():"");
//         result.setFpxCustomerCode((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getCustomerCode()) ? payableSingleFeeDTO.getCustomerCode():"");
//         result.setFpxSalesMan((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getSalesMan()) ? payableSingleFeeDTO.getSalesMan():"");
//         result.setFpxSalesCustomerService((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getSalesCustomerService()) ? payableSingleFeeDTO.getSalesCustomerService():"");
//         result.setFpxBusOperationStatus((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getBusOperationStatus())? payableSingleFeeDTO.getBusOperationStatus():"");
//         result.setFpxReturnFlag((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getReturnFlag())?payableSingleFeeDTO.getReturnFlag():"");
//         result.setFpxCheckinDate((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getCheckinDate()) ? payableSingleFeeDTO.getCheckinDate() : DateUtils.buildEmptyTimestamp(DateUtils.TIME_ZONE_CHAINA));
//         result.setFpxCheckoutDate((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getCheckoutDate()) ? payableSingleFeeDTO.getCheckoutDate() : DateUtils.buildEmptyTimestamp(DateUtils.TIME_ZONE_CHAINA));
//         result.setFpxFeeAmount((null != payableSingleFeeDTO && null != payableSingleFeeDTO.getFeeAmount() && result.getFpxFeeCode().equals(payableSingleFeeDTO.getFeeCode())) ? payableSingleFeeDTO.getFeeAmount() : BigDecimal.ZERO);
//         // 计费更新时间为空则设为 1970-01-01 00:00:01
//         result.setBillingDateUpdated((null!=payableSingleFeeDTO && null!=payableSingleFeeDTO.getDateUpdated()) ? payableSingleFeeDTO.getDateUpdated() : new Date(1000L));
//     }
//
//     /**
//      * 抓取数据并构造对账参数
//      * @param param
//      * @param currentPageNum
//      * @return 对账数据
//      */
//     private SingleFeeCheckCompareDTO checkPrefetchData(CheckParamDTO param, int currentPageNum) {
//         String checkMode = param.getCheckMode();
//         String supplierCode = param.getSupplierCode();
//
//         //1.构造账单请求参数
//         ManagementRequestDTO managementRequest = new ManagementRequestDTO();
//         managementRequest.setBillFid(param.getBillFid());
//         managementRequest.setCheckMode(checkMode);
//         managementRequest.setSupplierCode(supplierCode);
//         managementRequest.setPageNum(currentPageNum);
//         managementRequest.setPageSize(PAGE_SIZE);
//
//         //2.请求数据管理服务，获取对账所需账单数据
//         List<ManagementSingleFeeDTO> managementList = managementWrapperService.getSingleFeeListByPage(managementRequest);
//
//         //3.幂等
//         removeRepeat(managementList);
//
//         //4.获取应付数据
//         PayableRequestDTO payableRequest = buildPayableRequestParam(supplierCode, checkMode, managementList);
//         List<PayableSingleFeeDTO> payableList = payableWrapperService.getSingleFeeOrderList(payableRequest);
//
//         //5.数据模型转换并构造对账数据
//         Map<Long, PayableSingleFeeDTO> payableMap = payableList.stream().collect(
//                 Collectors.toMap(PayableSingleFeeDTO::getBillItemId, Function.identity(), (k1, k2)->k2));
//         SingleFeeCheckCompareDTO compareDTO = new SingleFeeCheckCompareDTO();
//         compareDTO.setManagementSingleFeeDTOList(managementList);
//         compareDTO.setBillItemIdPayableMap(payableMap);
//
//         return compareDTO;
//     }
//
//     /**
//      * 幂等，排除请求列表中与数据库中billItemId一致的单
//      * @param managementList
//      */
//     private void removeRepeat(List<ManagementSingleFeeDTO> managementList) {
//         Map<Long, ManagementSingleFeeDTO> managementMap = managementList.stream().collect(
//                 Collectors.toMap(ManagementSingleFeeDTO::getBillItemId, Function.identity(), (k1, k2) -> k2));
//         List<Long> repeatBillItemIdList = singleFeeCheckResultMapper.selectByBillItemIdCollection(managementMap.keySet());
//         repeatBillItemIdList.forEach(billItemId -> managementList.remove(managementMap.get(billItemId)));
//     }
//
//     /**
//      * 构建应付请求参数
//      * @param supplierCode
//      * @param managementList
//      * @return
//      */
//     private PayableRequestDTO buildPayableRequestParam(String supplierCode, String checkMode, List<ManagementSingleFeeDTO> managementList) {
//         PayableRequestDTO payableRequest = new PayableRequestDTO();
//
//         List<PayableRequestDetailDTO> requestDetailList = new ArrayList<>();
//         payableRequest.setSupplierCode(supplierCode);
//         payableRequest.setCheckMode(checkMode);
//         payableRequest.setPayableRequestDetailList(requestDetailList);
//         managementList.forEach(managementDTO -> {
//             PayableRequestDetailDTO payableRequestDetail = new PayableRequestDetailDTO();
//             payableRequestDetail.setBillItemId(managementDTO.getBillItemId());
//             payableRequestDetail.setBusinessDate(managementDTO.getSupplierBusinessDate());
//             payableRequestDetail.setSupplierNo(managementDTO.getSupplierNo());
//             payableRequestDetail.setFeeCode(managementDTO.getFpxFeeCode());
//             requestDetailList.add(payableRequestDetail);
//         });
//         return payableRequest;
//     }
//
//     @Override
//     public void reCheck(ReCheckParamDTO param) {
//         //1.拆分和抓取数据任务
//         //1.1 获取对账结果数量，计算出总页数
//         Integer total = singleFeeCheckResultService.getCountReCheckable(param.getSupplierCode(), param.getBillFid());
//         int totalPageNum = total % PAGE_SIZE == 0 ? total / PAGE_SIZE : total / PAGE_SIZE + 1;
//         //1.2 根据页数拆分抓取数据任务
//         List<PrefetchDataTask<SingleFeeReCheckCompareDTO>> prefetchDataTasks = new ArrayList<>();
//         for (int i = 1; i <= totalPageNum; i++) {
//             int currentPageNum = i;
//             prefetchDataTasks.add(() -> reCheckPrefetchData(param, currentPageNum));
//         }
//
//         //2.重对账任务
//         HandleDataTask<SingleFeeReCheckCompareDTO> handleDataTask = (data, billFid) -> {
//             reCheckHandleData(data);
//             return null;
//         };
//
//         //3.后置处理，更新对账进度
//         IHandleDataPostHandler<SingleFeeReCheckCompareDTO> postHandler = (data, billFid, progress, returnHandleDataResult) -> {
//             log.info("更新对账进度：" + (float)progress.getCurrentHandled()/progress.getTotal());
//             supplierBillCheckRecordService.updateProgress(progress.getTotal(), progress.getCurrentHandled(), (Long) billFid);
//             return null;
//         };
//
//         //4.全局任务后置处理器，待所有数据处理任务完成后将调用该方法
//         IGlobalTaskPostHandler globalTaskPostHandler = new IGlobalTaskPostHandler() {
//             @Override
//             public Object postForSuccess(Object o, GlobalTaskResult globalTaskResult) {
//                 return null;
//             }
//
//             @Override
//             public Object postForFail(Object o, GlobalTaskResult globalTaskResult, MultiThreadModeExecuteException exception) {
//                 throw exception;
//             }
//         };
//
//         //5.构造任务参数
//         IGlobalTaskBuilder<SingleFeeReCheckCompareDTO> globalTaskBuilder = new GlobalTaskBuilder<>();
//         globalTaskBuilder.prefetchDataExecutor(RECHECK_FETCH_DATA_EXECUTOR).handleDataExecutor(RECHECK_HANDLE_DATA_EXECUTOR)
//                 .prefetchDataTasks(prefetchDataTasks)
//                 .handleDataTask(handleDataTask)
//                 .globalParamater(param.getBillFid())
//                 .handleDataPostHandler(postHandler)
//                 .globalTaskPostHandler(globalTaskPostHandler);
//
//         //6.提交任务，等待任务完成
//         RECHECK_EXECUTOR.submit(globalTaskBuilder);
//
//         //防止并发更新进度丢失修改，再次更新对账进度
//         supplierBillCheckRecordService.updateProgress(totalPageNum, totalPageNum, param.getBillFid());
//     }
//
//     /**
//      * 重对账
//      * @param data 重对账数据
//      */
//     private void reCheckHandleData(SingleFeeReCheckCompareDTO data) {
//         data.getSingleFeeCheckResultList().forEach(singleFeeCheckResult -> {
//             if (EnumCheckConfirmStatus.WC.getCode().equals(singleFeeCheckResult.getSettleConfirmStatus()) ||
//                     EnumCheckConfirmStatus.CF.getCode().equals(singleFeeCheckResult.getSettleConfirmStatus())){
//                 PayableSingleFeeDTO payableSingleFeeDTO = data.getBillItemIdPayableMap().get(singleFeeCheckResult.getBillItemId());
//
//                 if (null == payableSingleFeeDTO) {
//                     //如果应付不存在，则强制更新
//                     SingleFeeCheckResult result = reCompare(singleFeeCheckResult, payableSingleFeeDTO);
//                     singleFeeCheckResultService.updateSingleFeeCheckResult(result);
//                 } else if (forceUpdate || !singleFeeCheckResult.getBillingDateUpdated().equals(payableSingleFeeDTO.getDateUpdated())) {
//                     //应付存在 或者有强制更新标志
//                     SingleFeeCheckResult result = reCompare(singleFeeCheckResult, payableSingleFeeDTO);
//                     singleFeeCheckResultService.updateSingleFeeCheckResult(result);
//                 }
//             }
//         });
//     }
//
//     private SingleFeeCheckResult reCompare(SingleFeeCheckResult singleFeeCheckResult, PayableSingleFeeDTO payableSingleFeeDTO) {
//         SingleFeeCheckResult result = new SingleFeeCheckResult();
//         result.setBillItemId(singleFeeCheckResult.getBillItemId());
//         result.setSupplierCode(singleFeeCheckResult.getSupplierCode());
//         result.setSupplierWeight(singleFeeCheckResult.getSupplierWeight());
//         result.setSupplierFeeAmount(singleFeeCheckResult.getSupplierFeeAmount());
//         result.setFpxFeeCode(singleFeeCheckResult.getFpxFeeCode());
//
//         buildPayableResult(payableSingleFeeDTO, result);
//
//         buildCompareResult(payableSingleFeeDTO, result);
//
//         result.setSupplierWeight(null);
//         result.setSupplierFeeAmount(null);
//         result.setFpxFeeCode(null);
//         //结算相关
//         result.setSettleConfirmStatus(EnumCheckConfirmStatus.WC.getCode());
//         result.setSettleConfirmRemark("");
//         result.setSettleStatus(EnumCheckSettleStatus.NS.getCode());
//         result.setSettleAccountPeriod("");
//         result.setSettleErrType("");
//         result.setSettleType("");
//         result.setSettleBy("");
//         result.setSettleTime(DateUtils.buildEmptyTimestamp(DateUtils.TIME_ZONE_CHAINA));
//
//         return result;
//     }
//
//     /**
//      * 构建应付请求参数
//      * @param supplierCode
//      * @param singleFeeCheckResultList
//      * @return
//      */
//     private PayableRequestDTO buildPayableRequest(String supplierCode, String checkMode, List<SingleFeeCheckResult> singleFeeCheckResultList) {
//         PayableRequestDTO payableRequest = new PayableRequestDTO();
//
//         List<PayableRequestDetailDTO> requestDetailList = new ArrayList<>();
//         payableRequest.setSupplierCode(supplierCode);
//         payableRequest.setCheckMode(checkMode);
//         payableRequest.setPayableRequestDetailList(requestDetailList);
//         singleFeeCheckResultList.forEach(singleFeeCheckResult -> {
//             PayableRequestDetailDTO payableRequestDetail = new PayableRequestDetailDTO();
//             payableRequestDetail.setBillItemId(singleFeeCheckResult.getBillItemId());
//             payableRequestDetail.setBusinessDate(singleFeeCheckResult.getSupplierBusinessDate());
//             payableRequestDetail.setSupplierNo(singleFeeCheckResult.getSupplierNo());
//             payableRequestDetail.setFeeCode(singleFeeCheckResult.getFpxFeeCode());
//             requestDetailList.add(payableRequestDetail);
//         });
//         return payableRequest;
//     }
//
//     /**
//      * （重对账）抓取数据并构造对账参数
//      * @param param
//      * @param currentPageNum
//      * @return 对账数据
//      */
//     private SingleFeeReCheckCompareDTO reCheckPrefetchData(ReCheckParamDTO param, int currentPageNum) {
//         String checkMode = param.getCheckMode();
//         String supplierCode = param.getSupplierCode();
//         Long billFid = param.getBillFid();
//
//         //1.获取单费用项对账结果数据
//         List<SingleFeeCheckResult> singleFeeCheckResultList = singleFeeCheckResultService.getListReCheckable(supplierCode, billFid, currentPageNum, PAGE_SIZE);
//
//         //2.更换调整单号，获取应付数据
//         for (SingleFeeCheckResult result : singleFeeCheckResultList) {
//             if(StringUtils.isNotEmpty(result.getCheckReplaceNo())){
//                 result.setSupplierNo(result.getCheckReplaceNo());
//             }
//         }
//         PayableRequestDTO request = buildPayableRequest(supplierCode, checkMode, singleFeeCheckResultList);
//         List<PayableSingleFeeDTO> payableList = payableWrapperService.getSingleFeeOrderList(request);
//
//         //3.数据模型转换并构造对账数据
//         Map<Long, PayableSingleFeeDTO> payableMap = payableList.stream().collect(
//                 Collectors.toMap(PayableSingleFeeDTO::getBillItemId, Function.identity(), (k1, k2)->k2));
//         SingleFeeReCheckCompareDTO singleFeeReCheckCompareDTO = new SingleFeeReCheckCompareDTO();
//         singleFeeReCheckCompareDTO.setSingleFeeCheckResultList(singleFeeCheckResultList);
//         singleFeeReCheckCompareDTO.setBillItemIdPayableMap(payableMap);
//
//         return singleFeeReCheckCompareDTO;
//     }
//
//     @Override
//     public void batchRecheck(SingleFeeCheckResultReCheckRequestParam param) {
//         // 参数校验
//
//         // 根据billItemId列表查询需要重新对账的结果记录
//         Example queryExample = new Example(SingleFeeCheckResult.class);
//         Criteria queryCriteria = queryExample.createCriteria();
//         queryCriteria.andIn("billItemId", param.getBillItemIds());
//         List<String> settleConfirmStatus = new ArrayList<>();
//         settleConfirmStatus.add(EnumCheckConfirmStatus.WC.getCode());
//         settleConfirmStatus.add(EnumCheckConfirmStatus.CF.getCode());
//         queryCriteria.andIn("settleConfirmStatus", settleConfirmStatus);
//         List<SingleFeeCheckResult> singleFeeCheckResultList = singleFeeCheckResultMapper.selectByExample(queryExample);
//         if(CollectionUtils.isEmpty(singleFeeCheckResultList)){
//             return;
//         }
//
//         // 获取billItemId对应的应付数据
//         for (SingleFeeCheckResult result : singleFeeCheckResultList) {
//             // 换单号替换处理
//             if(StringUtils.isNotEmpty(result.getCheckReplaceNo())){
//                 result.setSupplierNo(result.getCheckReplaceNo());
//             }
//         }
//         PayableRequestDTO request = buildPayableRequest(param.getSupplierCode(), param.getType(), singleFeeCheckResultList);
//         List<PayableSingleFeeDTO> payableList = payableWrapperService.getSingleFeeOrderList(request);
//
//         // 进行对账并更新对账结果
//         Map<Long,PayableSingleFeeDTO> payableMap = new HashMap<Long,PayableSingleFeeDTO>(payableList.size());
//         if (CollectionUtils.isNotEmpty(payableList)){
//             for (PayableSingleFeeDTO payableNoDTO : payableList) {
//                 payableMap.put(payableNoDTO.getBillItemId(), payableNoDTO);
//             }
//         }
//
//         // 4.数据对比,更新
//         for (SingleFeeCheckResult result : singleFeeCheckResultList) {
//             if(EnumCheckConfirmStatus.WC.getCode().equals(result.getSettleConfirmStatus())||EnumCheckConfirmStatus.CF.getCode().equals(result.getSettleConfirmStatus())){
//                 PayableSingleFeeDTO payableSingleFeeDTO = payableMap.get(result.getBillItemId());
//                 SingleFeeCheckResult updateBean = reCompare(result, payableSingleFeeDTO);
//                 singleFeeCheckResultService.updateSingleFeeCheckResult(updateBean);
//             }
//         }
//     }
// }
