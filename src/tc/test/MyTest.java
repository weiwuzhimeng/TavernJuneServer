package tc.test;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import tc.mapper.UserMapper;

public class MyTest {

	public static void main(String[] args) throws Exception {
		new MyTest().start();
	}
	
	public void start() throws Exception {
		
//		//放在src下的路劲：SqlMapConfig.xml
//		//放在某个Folder(mybatis)下的路径：mybatis/SqlMapConfig.xml
//		//所以书写路径是不需要带外层的例如：src、config等文件夹的
//		String resource = "mybatis/sqlMapConfig.xml";
//		InputStream inputStream = Resources.getResourceAsStream(resource);
//		SqlSessionFactory sessionFactory = 
//				new SqlSessionFactoryBuilder().build(inputStream);
//		SqlSession sqlSession = sessionFactory.openSession();
//		
//		//通过反射传入UserMapper接口的.class文件，mybatis会自动创建出UserMapper的实现类
//		//userMapper就是mybatis帮我们创建的接口的实现类对象
//		//前提条件书写的xml需要和接口有四点相同
//		RelateMapper rm = sqlSession.getMapper(RelateMapper.class);
//		List<Relate> list = rm.selectRelateById(1);
//		//int id = rm.selectIdById(1);
//		for (Relate relate : list) {
//			System.out.println("它的好友有："+relate.getRemoteUserId());
//		}
//		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^"+list);
	}
}
