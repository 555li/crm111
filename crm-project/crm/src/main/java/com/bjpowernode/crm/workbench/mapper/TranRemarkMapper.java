package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Mon Mar 14 15:30:06 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Mon Mar 14 15:30:06 CST 2022
     */
    int insert(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Mon Mar 14 15:30:06 CST 2022
     */
    int insertSelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Mon Mar 14 15:30:06 CST 2022
     */
    TranRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Mon Mar 14 15:30:06 CST 2022
     */
    int updateByPrimaryKeySelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Mon Mar 14 15:30:06 CST 2022
     */
    int updateByPrimaryKey(TranRemark record);

    int insertTranRemarkByList(List<TranRemark> list);
}