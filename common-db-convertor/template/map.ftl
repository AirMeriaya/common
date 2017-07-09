package ${package}.dao.mapper;

import java.util.List;

import ${importPackage};

public interface ${className} {

	public void insert(${table['className']} record);
	
	public void insertBatch(List<${table['className']}> records);
	
	public ${table['className']} selectByKey(int key);
	
	public List<${table['className']}> selectByCondition(${table['className']} condition);
	
	public List<${table['className']}> selectAll();
	
	public void updateByKey(int key);
	
	public void updateBatch(List<${table['className']}> records);
	
	public void deleteByKey(int key);
	
	public void deleteBatch(List<${table['className']}> records);
}