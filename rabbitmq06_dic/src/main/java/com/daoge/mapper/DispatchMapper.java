package com.daoge.mapper;

import org.apache.ibatis.annotations.Insert;

import com.daoge.entity.DispatchEntity;

public interface DispatchMapper {

	/**
	 * 新增派单任务
	 */
	@Insert("INSERT into platoon values (null,#{orderId},#{dispatchRoute},#{takeoutUserId});")
	public int insertDistribute(DispatchEntity distributeEntity);

}
