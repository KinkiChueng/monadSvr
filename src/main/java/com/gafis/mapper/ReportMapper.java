package com.gafis.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ReportMapper {

    /**
     * 根据seq,取一批人员信息
     * @param minSeq
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> selectPersonBySeq(@Param("minSeq")long minSeq, @Param("rowNum")String rowNum);

    /**
     * 获取最小的seq
     * @return
     * @throws Exception
     */
	long minSeq(@Param("name")String name);

	/**
	 * 更新seq
	 * @param lastSeq
	 * @return
	 * @throws Exception
	 */
	Boolean updateSeq(@Param("lastSeq")long lastSeq, @Param("name")String name);
}
