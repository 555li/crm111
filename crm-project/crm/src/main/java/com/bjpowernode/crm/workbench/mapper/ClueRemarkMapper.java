package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat Mar 12 15:46:10 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat Mar 12 15:46:10 CST 2022
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat Mar 12 15:46:10 CST 2022
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat Mar 12 15:46:10 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat Mar 12 15:46:10 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat Mar 12 15:46:10 CST 2022
     */
    int updateByPrimaryKey(ClueRemark record);

    /**
     * 根据clueId查询所有线索下的备注信息
     * @return
     */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);

    /**
     * 根据clueId查询该线索下所有的备注信息
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemarkByClueId(String clueId);
    int deleteClueRemarkByClueId(String clueId);

}