<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${namespace}">
  
    <resultMap id="${table['className']?uncap_first}Map" type="${table['className']?uncap_first}">
    	<id column="${table['primaryKey']['columnName']}" property="${table['primaryKey']['fieldName']}"/>
    	<#list table['columns'] as cList>
    		<#if cList['columnName'] != table['primaryKey']['columnName']>
    	<result column="${cList['columnName']}" property="${cList['fieldName']}" />
    		</#if>
		</#list>
    </resultMap>
    
    <insert id="insert" parameterType="${table['className']?uncap_first}">
  		INSERT INTO ${table['tableName']} (
		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${cList['columnName']}, 
  		<#else>
  			${cList['columnName']}
  			</#if>
		</#list>
		)
		VALUES (
		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${'#{'}${cList['fieldName']}},
  		<#else>
  			${'#{'}${cList['fieldName']}}
  			</#if>
		</#list>
		)
  	</insert>
  	
  	<insert id="insertBatch" parameterType="java.util.List">
  		INSERT INTO ${table['tableName']} (
		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${cList['columnName']}, 
  		<#else>
  			${cList['columnName']}
  			</#if>
		</#list>
		)
		VALUES 
		<foreach collection="list" item="record" index="index" separator=",">  
        (
		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${'#{'}record.${cList['fieldName']}},
  		<#else>
  			${'#{'}record.${cList['fieldName']}}
  			</#if>
		</#list>
		)
    	</foreach>
  	</insert>
  
  	<select id="selectByKey" parameterType="java.lang.Integer" resultMap="${table['className']?uncap_first}Map">
  		SELECT
  		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${cList['columnName']}, 
  		<#else>
  			${cList['columnName']}
  			</#if>
		</#list>
		FROM 
			${table['tableName']} 
		WHERE 
			${table['primaryKey']['columnName']} = ${r'#{id}'}
  	</select>
  	
  	<select id="selectByCondition" parameterType="${table['className']?uncap_first}" resultMap="${table['className']?uncap_first}Map">
  		SELECT
  		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${cList['columnName']}, 
  		<#else>
  			${cList['columnName']}
  			</#if>
		</#list>
		FROM 
			${table['tableName']} 
		<where>
		<#list table['columns'] as cList>
  			<#if cList_index = 0>
  			<if test = "${cList['fieldName']} != null"> 
            	${cList['columnName']} = ${'#{'}${cList['fieldName']}}
        	</if>  
  			<#else>
  			<if test = "${cList['fieldName']} != null"> 
  				and ${cList['columnName']} = ${'#{'}${cList['fieldName']}}
  			</if> 
  			</#if>
		</#list>
				or 1 = 2
    	</where>  
  	</select>
  	
  	<select id="selectAll" resultMap="${table['className']?uncap_first}Map">
  		SELECT
  		<#list table['columns'] as cList>
  			<#if cList_has_next>
  			${cList['columnName']}, 
  			<#else>
  			${cList['columnName']}
  			</#if>
		</#list>
		FROM 
			${table['tableName']}
  	</select>
  	
  	<update id="updateByKey" parameterType="${table['className']?uncap_first}">
  		UPDATE ${table['tableName']} 
  		<set>
  		<#list table['columns'] as cList>
  		<#if cList_has_next>
  			<if test = "${cList['fieldName']} != null"> 
           		${cList['columnName']} = ${'#{'}${cList['fieldName']}},
        	</if>  
  			<#else>
  			<if test = "${cList['fieldName']} != null"> 
  				${cList['columnName']} = ${'#{'}${cList['fieldName']}}
  			</if> 
  		</#if> 
		</#list>
  		</set>
  		WHERE
  			${table['primaryKey']['columnName']} = ${'#{'}${table['primaryKey']['fieldName']}}
  	</update>
  	
  	<update id="updateBatch" parameterType="java.util.List">
  		<foreach collection="list" item="record" index="index" open="" close="" separator=";">  
  		UPDATE ${table['tableName']} 
  		<set>
  		<#list table['columns'] as cList>
  		<#if cList_has_next>
  			<if test = "record.${cList['fieldName']} != null"> 
           		${cList['columnName']} = ${'#{'}record.${cList['fieldName']}},
        	</if>  
  			<#else>
  			<if test = "record.${cList['fieldName']} != null"> 
  				${cList['columnName']} = ${'#{'}record.${cList['fieldName']}}
  			</if> 
  		</#if> 
		</#list>
  		</set>
  		WHERE
  			${table['primaryKey']['columnName']} = ${'#{'}record.${table['primaryKey']['fieldName']}}
  		</foreach>
	</update>
  	
  	<delete id="deleteByKey" parameterType="java.lang.Integer">
  		DELETE FROM ${table['tableName']} 
  		WHERE
  			${table['primaryKey']['columnName']} = ${r'#{id}'}
  	</delete>
  	
  	<delete id="deleteBatch" parameterType="java.util.List">
  		DELETE FROM ${table['tableName']} 
  		WHERE
  			${table['primaryKey']['columnName']}
  		IN
  		(
  		<foreach collection="list" item="record" index="index" separator=",">
  			${'#{'}record.${table['primaryKey']['fieldName']}}
  		</foreach>
  		)
  	</delete>
  	
  </mapper>