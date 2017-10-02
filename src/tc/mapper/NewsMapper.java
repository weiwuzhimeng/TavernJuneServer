package tc.mapper;

import org.springframework.stereotype.Repository;

import tc.po.News;

//官方推送持久层
@Repository
public interface NewsMapper {
	
	//根据id返回推送内容给用户
	public News selectNewsById(int id) throws Exception;
	
}
