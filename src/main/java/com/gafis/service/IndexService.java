package com.gafis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.gafis.mapper.ReportMapper;
import com.gafis.util.CommonUtils;

@Service
public class IndexService {

	@Autowired
    ReportMapper PSMapper;

	/**
	 * 获取最小的seq
	 * @return
	 * @throws Exception
	 */
	public long getMinSeq() throws Exception{
		long minSeq = PSMapper.minSeq(CommonUtils.GetConfigInfo().get("queryName").toString());
		return minSeq;
	}

	/**
	 * 取一批人员信息
	 * @param minSeq
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getPersonBySeq(long minSeq) throws Exception{
		List<Map<String,Object>> personMapList = PSMapper.selectPersonBySeq(minSeq, CommonUtils.GetConfigInfo().get("rowNum").toString());
		return personMapList;
	}

	/**
	 * 更新seq
	 * @param lastSeq
	 * @return
	 * @throws Exception
	 */
	public Boolean updateSeq(long lastSeq) throws Exception{
		Boolean status = PSMapper.updateSeq(lastSeq, CommonUtils.GetConfigInfo().get("queryName").toString());
		return status;
	}
}
